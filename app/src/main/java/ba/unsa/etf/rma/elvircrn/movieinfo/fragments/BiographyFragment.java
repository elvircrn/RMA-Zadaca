package ba.unsa.etf.rma.elvircrn.movieinfo.fragments;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.ArrayList;

import ba.unsa.etf.rma.elvircrn.movieinfo.DataProvider;
import ba.unsa.etf.rma.elvircrn.movieinfo.R;
import ba.unsa.etf.rma.elvircrn.movieinfo.mappers.PersonMapper;
import ba.unsa.etf.rma.elvircrn.movieinfo.databinding.ActorBiographyFragmentBinding;
import ba.unsa.etf.rma.elvircrn.movieinfo.interfaces.ITaggable;
import ba.unsa.etf.rma.elvircrn.movieinfo.managers.PeopleManager;
import ba.unsa.etf.rma.elvircrn.movieinfo.models.Actor;
import ba.unsa.etf.rma.elvircrn.movieinfo.models.Genre;
import ba.unsa.etf.rma.elvircrn.movieinfo.models.Movie;
import ba.unsa.etf.rma.elvircrn.movieinfo.services.dto.MovieCreditsDTO;
import ba.unsa.etf.rma.elvircrn.movieinfo.services.dto.MovieDTO;
import ba.unsa.etf.rma.elvircrn.movieinfo.services.dto.PersonDTO;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class BiographyFragment extends Fragment implements ITaggable {
    Actor actor = new Actor();
    private ActorBiographyFragmentBinding binding;

    private final static String ACTOR_PARAM_TAG = "PersonDTO";
    public static String getActorParamTag() { return ACTOR_PARAM_TAG; }

    public static final String FRAGMENT_TAG = "biographyTag";

    public BiographyFragment() { }

    public static String getTypeFragmentTag() { return FRAGMENT_TAG; }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.actor_biography_fragment, container, false);

        if (getArguments().containsKey(getActorParamTag())) {
            setActor((Actor) getArguments().get(getActorParamTag()));
        } else {
            setActor(new Actor());
        }

        View view = binding.getRoot();
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        addOnButtonClickListeners();
    }

    void addOnButtonClickListeners() {
        ImageButton imdbButton = (ImageButton)getView().findViewById(R.id.imdbButton);
        imdbButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                if (actor.getImdbLink() != null && !actor.getImdbLink().isEmpty())
                    i.setData(Uri.parse(actor.getImdbLink()));
                else
                    Toast.makeText(getContext(), String.valueOf(R.string.imdb_toast_error), 3)
                            .show();
                startActivity(i);
            }
        });

        ImageButton shareBio = (ImageButton)getView().findViewById(R.id.shareButton);
        shareBio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_SEND);
                i.putExtra(Intent.EXTRA_TEXT, actor.getBiography());
                i.setType("text/plain");

                // Provjera da li uopste postoji app koji podrzava ovu vrstu intenta. resolveActivity()
                // vraca null za slucaj da ne postoji.
                if (i.resolveActivity(getActivity().getPackageManager()) != null) {
                    startActivity(i);
                }
            }
        });
    }

    @Override
    public String toString() { return FRAGMENT_TAG; }

    @Override
    public String getFragmentTag() {
        return FRAGMENT_TAG;
    }

    public void setActor(Actor actor) {
        this.actor = actor;

        PeopleManager.getInstance().getDetails(actor.getId())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .retry(3)
                .subscribe(new Subscriber<PersonDTO>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        // TODO: Get value from string
                        Toast.makeText(getContext(), String.valueOf(R.string.bio_toast_error), 3)
                                .show();
                    }

                    @Override
                    public void onNext(PersonDTO personDTO) {
                        BiographyFragment.this.actor = PersonMapper.toActorFromActor(BiographyFragment.this.actor, personDTO);
                        binding.setActor(BiographyFragment.this.actor);
                        ArrayList<Integer> genreIds = new ArrayList<>();
                        DataProvider.getInstance().setSelectedGenres(new ArrayList<Genre>());

                        for (Movie movie : BiographyFragment.this.actor.getMovies()) {
                            for (int genreId : movie.getGenreIds())
                                if (!genreIds.contains(genreId))
                                    genreIds.add(genreId);



                        }

                        for (int genreId : genreIds)
                            for (Genre genre : DataProvider.getInstance().getGenres())
                                if (genre.getId() == genreId)
                                    DataProvider.getInstance().getSelectedGenres().add(genre);


                    }
                });



        binding.setActor(actor);
    }

}
