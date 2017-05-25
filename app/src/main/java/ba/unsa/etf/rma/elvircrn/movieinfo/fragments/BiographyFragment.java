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
import java.util.List;
import java.util.Objects;

import ba.unsa.etf.rma.elvircrn.movieinfo.DataProvider;
import ba.unsa.etf.rma.elvircrn.movieinfo.R;
import ba.unsa.etf.rma.elvircrn.movieinfo.databinding.ActorBiographyFragmentBinding;
import ba.unsa.etf.rma.elvircrn.movieinfo.helpers.Rx;
import ba.unsa.etf.rma.elvircrn.movieinfo.interfaces.ITaggable;
import ba.unsa.etf.rma.elvircrn.movieinfo.managers.GenreManager;
import ba.unsa.etf.rma.elvircrn.movieinfo.managers.MovieManager;
import ba.unsa.etf.rma.elvircrn.movieinfo.managers.PeopleManager;
import ba.unsa.etf.rma.elvircrn.movieinfo.mappers.PersonMapper;
import ba.unsa.etf.rma.elvircrn.movieinfo.models.Actor;
import ba.unsa.etf.rma.elvircrn.movieinfo.models.Director;
import ba.unsa.etf.rma.elvircrn.movieinfo.models.Genre;
import ba.unsa.etf.rma.elvircrn.movieinfo.models.Movie;
import ba.unsa.etf.rma.elvircrn.movieinfo.services.dto.CrewItemDTO;
import ba.unsa.etf.rma.elvircrn.movieinfo.services.dto.GenresDTO;
import ba.unsa.etf.rma.elvircrn.movieinfo.services.dto.MovieCreditsDTO;
import ba.unsa.etf.rma.elvircrn.movieinfo.services.dto.PersonDTO;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Function3;
import io.reactivex.schedulers.Schedulers;

import static ba.unsa.etf.rma.elvircrn.movieinfo.mappers.DirectorMapper.DIRECTOR_ROLE;

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
        binding.setActor(actor);

        Observable<List<MovieCreditsDTO>> creditsStream = Observable.fromIterable(actor.getMovies())
                .flatMap(new Function<Movie, ObservableSource<MovieCreditsDTO>>() {
                    @Override
                    public ObservableSource<MovieCreditsDTO> apply(@NonNull Movie movie) throws Exception {
                        return MovieManager.getInstance()
                                .getMovieCredits(movie.getId())
                                .toObservable()
                                .subscribeOn(Schedulers.newThread());
                    }
                })
                .toList()
                .toObservable();

        if (DataProvider.getInstance().getGenres().isEmpty()) {
            subscriberHolder.add(
                    Observable.zip(GenreManager.getInstance().getGenres().toObservable(),
                            PeopleManager.getInstance().getDetails(actor.getId()).toObservable(),
                            creditsStream,
                            new Function3<GenresDTO, PersonDTO, List<MovieCreditsDTO>, Void>() {

                                @Override
                                public Void apply(@NonNull GenresDTO genresDTO, @NonNull PersonDTO personDTO, @NonNull List<MovieCreditsDTO> credits) throws Exception {
                                    BiographyFragment.this.actor = PersonMapper.toActorFromActor(BiographyFragment.this.actor, personDTO);
                                    binding.setActor(BiographyFragment.this.actor);
                                    ArrayList<Integer> genreIds = new ArrayList<>();
                                    ArrayList<Genre> genres = new ArrayList<>();
                                    ArrayList<Integer> directorIds = new ArrayList<>();
                                    DataProvider.getInstance().getSelectedGenres().clear();


                                    // Fill in the genres
                                    for (Movie movie : BiographyFragment.this.actor.getMovies()) {
                                        if (movie.getGenreIds() != null
                                                && !movie.getGenreIds().isEmpty()
                                                && genreIds.contains(movie.getGenreIds().get(0)))
                                            genreIds.add(movie.getGenreIds().get(0));

                                        if (genreIds.size() == 7)
                                            break;
                                    }

                                    for (int genreId : genreIds)
                                        for (Genre genre : DataProvider.getInstance().getGenres())
                                            if (genre.getId() == genreId)
                                                genres.add(genre);

                                    DataProvider.getInstance().setGenres(genres);

                                    // Fill in the directors
                                    DataProvider.getInstance().getDirectors().clear();
                                    for (Movie movie : BiographyFragment.this.actor.getMovies())
                                        if (DataProvider.getInstance().getDirectors().size() < 7)
                                            for (MovieCreditsDTO movieCreditsDTO : credits)
                                                if (DataProvider.getInstance().getDirectors().size() < 7 && Objects.equals(movieCreditsDTO.getId(), movie.getId()))
                                                    for (CrewItemDTO crewItem : movieCreditsDTO.getCrew()) {
                                                        if (DataProvider.getInstance().getDirectors().size() == 7)
                                                            break;

                                                        if (!Objects.equals(crewItem.getJob(), DIRECTOR_ROLE) ||
                                                                DataProvider.getInstance().getDirectors().contains(crewItem.getId()))
                                                            continue;

                                                        boolean found = false;
                                                        for (Director director : DataProvider.getInstance().getDirectors())
                                                            if (director.getId() == crewItem.getId())
                                                                found = true;

                                                        directorIds.add(crewItem.getId());

                                                        if (!found)
                                                            DataProvider.getInstance().getDirectors().add(new Director(crewItem.getId(), crewItem.getName(), ""));
                                                    }
                                    return null;
                                }
                            })
                            .compose(Rx.<Void>applySchedulers())
                            .subscribe()
            );
        } else {
            subscriberHolder.add(
                    PeopleManager.getInstance().getDetails(actor.getId())
                            .toObservable()
                            .compose(Rx.<PersonDTO>applySchedulers())
                            .retry()
                            .compose(Rx.<PersonDTO>applyError())
                            .subscribe(new Consumer<PersonDTO>() {
                                @Override
                                public void accept(@NonNull PersonDTO personDTO) throws Exception {
                                    BiographyFragment.this.actor = PersonMapper.toActorFromActor(BiographyFragment.this.actor, personDTO);
                                    binding.setActor(BiographyFragment.this.actor);
                                    ArrayList<Integer> genreIds = new ArrayList<>();
                                    ArrayList<Genre> genres = new ArrayList<>();

                                    DataProvider.getInstance().getSelectedGenres().clear();

                                    for (Movie movie : BiographyFragment.this.actor.getMovies()) {
                                        for (int genreId : movie.getGenreIds())
                                            if (!genreIds.contains(genreId))
                                                genreIds.add(genreId);
                                    }

                                    for (int genreId : genreIds)
                                        for (Genre genre : DataProvider.getInstance().getGenres())
                                            if (genre.getId() == genreId)
                                                genres.add(genre);

                                    DataProvider.getInstance().setGenres(genres);
                                }
                            })
            );
        }


    }

    @Override
    public void onPause() {
        super.onPause();
        subscriberHolder.dispose();
    }
}
