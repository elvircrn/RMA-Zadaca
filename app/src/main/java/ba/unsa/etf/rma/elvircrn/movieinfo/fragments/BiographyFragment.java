package ba.unsa.etf.rma.elvircrn.movieinfo.fragments;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.ArrayList;

import ba.unsa.etf.rma.elvircrn.movieinfo.DataProvider;
import ba.unsa.etf.rma.elvircrn.movieinfo.R;
import ba.unsa.etf.rma.elvircrn.movieinfo.databinding.ActorBiographyFragmentBinding;
import ba.unsa.etf.rma.elvircrn.movieinfo.helpers.Rx;
import ba.unsa.etf.rma.elvircrn.movieinfo.interfaces.ITaggable;
import ba.unsa.etf.rma.elvircrn.movieinfo.managers.GenreManager;
import ba.unsa.etf.rma.elvircrn.movieinfo.managers.PeopleManager;
import ba.unsa.etf.rma.elvircrn.movieinfo.mappers.GenreMapper;
import ba.unsa.etf.rma.elvircrn.movieinfo.mappers.PersonMapper;
import ba.unsa.etf.rma.elvircrn.movieinfo.models.Actor;
import ba.unsa.etf.rma.elvircrn.movieinfo.models.Genre;
import ba.unsa.etf.rma.elvircrn.movieinfo.models.Movie;
import ba.unsa.etf.rma.elvircrn.movieinfo.services.dto.GenresDTO;
import ba.unsa.etf.rma.elvircrn.movieinfo.services.dto.PersonDTO;
import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;

public class BiographyFragment extends Fragment implements ITaggable {
    private final static String ACTOR_PARAM_TAG = "PersonDTO";

    public static String getActorParamTag() {
        return ACTOR_PARAM_TAG;
    }

    private Actor actor = new Actor();
    private CompositeDisposable subscriberHolder = new CompositeDisposable();
    private ActorBiographyFragmentBinding binding;

    public static final String FRAGMENT_TAG = "biographyTag";

    public BiographyFragment() {
    }

    public static String getTypeFragmentTag() {
        return FRAGMENT_TAG;
    }

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
        ImageButton imdbButton = (ImageButton) getView().findViewById(R.id.imdbButton);
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

        ImageButton shareBio = (ImageButton) getView().findViewById(R.id.shareButton);
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
    public String toString() {
        return FRAGMENT_TAG;
    }

    @Override
    public String getFragmentTag() {
        return FRAGMENT_TAG;
    }

    public void setActor(Actor actor) {
        this.actor = actor;



        if (DataProvider.getInstance().getGenres().isEmpty()) {
            Observable.zip(GenreManager.getInstance().getGenres().toObservable(), PeopleManager.getInstance().getDetails(actor.getId()).toObservable(),
                    new BiFunction<GenresDTO, PersonDTO, ArrayList<Genre>>() {
                        @Override
                        public ArrayList<Genre> apply(@NonNull GenresDTO genresDTO, @NonNull PersonDTO personDTO) throws Exception {
                            return null;
                        }
                    })
                    .subscribe(new Consumer<ArrayList<Genre>>() {
                        @Override
                        public void accept(@NonNull ArrayList<Genre> genres) throws Exception {

                        }
                    });



            GenreManager.getInstance().getGenres()
                    .toObservable()
                    .compose(Rx.<GenresDTO>applySchedulers())
                    .retry()
                    .subscribe(new Consumer<GenresDTO>() {
                        @Override
                        public void accept(@NonNull GenresDTO genresDTO) throws Exception {
                            DataProvider.getInstance().setGenres(GenreMapper.toGenres(genresDTO));
                        }
                    });

        }



        subscriberHolder.add(
                PeopleManager.getInstance().getDetails(actor.getId())
                        .toObservable()
                        .compose(Rx.<PersonDTO>applySchedulers())
                        .retry(3)
                        .compose(Rx.<PersonDTO>applyError())
                        .subscribe(new Consumer<PersonDTO>() {
                            @Override
                            public void accept(@NonNull PersonDTO personDTO) throws Exception {
                                BiographyFragment.this.actor = PersonMapper.toActorFromActor(BiographyFragment.this.actor, personDTO);
                                binding.setActor(BiographyFragment.this.actor);
                                ArrayList<Integer> genreIds = new ArrayList<>();

                                DataProvider.getInstance().getSelectedGenres().clear();

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
                        })
        );


        binding.setActor(actor);
    }

    @Override
    public void onPause() {
        super.onPause();
        subscriberHolder.dispose();
    }
}
