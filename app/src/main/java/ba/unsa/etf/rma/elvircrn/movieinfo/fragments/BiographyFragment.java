package ba.unsa.etf.rma.elvircrn.movieinfo.fragments;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import ba.unsa.etf.rma.elvircrn.movieinfo.DataProvider;
import ba.unsa.etf.rma.elvircrn.movieinfo.R;
import ba.unsa.etf.rma.elvircrn.movieinfo.dao.ActorDbService;
import ba.unsa.etf.rma.elvircrn.movieinfo.databinding.ActorBiographyFragmentBinding;
import ba.unsa.etf.rma.elvircrn.movieinfo.helpers.Rx;
import ba.unsa.etf.rma.elvircrn.movieinfo.interfaces.ITaggable;
import ba.unsa.etf.rma.elvircrn.movieinfo.managers.MovieManager;
import ba.unsa.etf.rma.elvircrn.movieinfo.managers.PeopleManager;
import ba.unsa.etf.rma.elvircrn.movieinfo.mappers.GenreMapper;
import ba.unsa.etf.rma.elvircrn.movieinfo.mappers.PersonMapper;
import ba.unsa.etf.rma.elvircrn.movieinfo.models.Actor;
import ba.unsa.etf.rma.elvircrn.movieinfo.models.Director;
import ba.unsa.etf.rma.elvircrn.movieinfo.models.Genre;
import ba.unsa.etf.rma.elvircrn.movieinfo.services.dto.CastItemDTO;
import ba.unsa.etf.rma.elvircrn.movieinfo.services.dto.CrewItemDTO;
import ba.unsa.etf.rma.elvircrn.movieinfo.services.dto.MovieCreditsDTO;
import ba.unsa.etf.rma.elvircrn.movieinfo.services.dto.MovieDTO;
import ba.unsa.etf.rma.elvircrn.movieinfo.services.dto.PersonDTO;
import ba.unsa.etf.rma.elvircrn.movieinfo.view.RxCheckBox;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Function3;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;

import static ba.unsa.etf.rma.elvircrn.movieinfo.mappers.DirectorMapper.DIRECTOR_ROLE;

public class BiographyFragment extends Fragment implements ITaggable {
    private final static String ACTOR_PARAM_TAG = "PersonDTO";

    @BindView(R.id.bookmarkedCheckBox)
    CheckBox bookmarked;

    Observable<Boolean> checkBoxStream;

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

        ButterKnife.bind(this, binding.getRoot());

        bookmarked.setEnabled(false);

        initCheckBox();

        if (getArguments().containsKey(getActorParamTag())) {
            setActor((Actor) getArguments().get(getActorParamTag()));
        } else {
            setActor(new Actor());
        }

        return binding.getRoot();
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

                if (i.resolveActivity(getActivity().getPackageManager()) != null) {
                    startActivity(i);
                }
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        if (actor != null)
            savedInstanceState.putParcelable(getActorParamTag(), actor);
    }

    void initCheckBox() {
        checkBoxStream = RxCheckBox.fromCheckBox(bookmarked);
        subscriberHolder.add(
                checkBoxStream.compose(Rx.<Boolean>applyDbSchedulers())
                        .subscribe(new Consumer<Boolean>() {
                            @Override
                            public void accept(@NonNull Boolean bookmarked) throws Exception {
                                if (bookmarked) {
                                    ActorDbService.addActor(binding.getActor());
                                } else {
                                    ActorDbService.deleteActor(binding.getActor());
                                }
                            }
                        })
        );
    }

    @Override
    public String toString() {
        return FRAGMENT_TAG;
    }

    @Override
    public String getFragmentTag() {
        return FRAGMENT_TAG;
    }

    public void setActor(final Actor actor) {

        this.actor = actor;
        binding.setActor(actor);
        binding.notifyChange();

        Observable<MovieCreditsDTO> creditsStream = PeopleManager.getInstance().getMovieCredits(actor.getId()).retry().toObservable().take(1);

        Observable<List<MovieDTO>> moviesStream = creditsStream
                .flatMap(new Function<MovieCreditsDTO, ObservableSource<CastItemDTO>>() {
                    @Override
                    public ObservableSource<CastItemDTO> apply(@NonNull MovieCreditsDTO movieCreditsDTO) throws Exception {
                        return Observable.fromIterable(movieCreditsDTO.getCast());
                    }
                })
                .flatMap(new Function<CastItemDTO, ObservableSource<MovieDTO>>() {
                    @Override
                    public ObservableSource<MovieDTO> apply(@NonNull CastItemDTO castItemDTO) throws Exception {
                        return MovieManager.getInstance().getMovie(castItemDTO.getId()).subscribeOn(Schedulers.newThread()).retry().toObservable();
                    }
                })
                .take(7)
                .toSortedList(new Comparator<MovieDTO>() {
                    @Override
                    public int compare(MovieDTO o1, MovieDTO o2) {
                        if (o1 != null && o2 != null)
                            return (o2.getReleaseDate().compareTo(o1.getReleaseDate()));
                        else
                            return 0;
                    }
                })
                .toObservable();

        Observable<List<MovieCreditsDTO>> fullCreditsStream = moviesStream
                .flatMap(new Function<List<MovieDTO>, ObservableSource<MovieDTO>>() {
                    @Override
                    public ObservableSource<MovieDTO> apply(@NonNull List<MovieDTO> movieDTOs) throws Exception {
                        return Observable.fromIterable(movieDTOs);
                    }
                })
                .flatMap(new Function<MovieDTO, ObservableSource<MovieCreditsDTO>>() {
                    @Override
                    public ObservableSource<MovieCreditsDTO> apply(@NonNull MovieDTO movieDTO) throws Exception {
                        return MovieManager.getInstance()
                                .getMovieCredits(movieDTO.getId())
                                .retry()
                                .toObservable()
                                .subscribeOn(Schedulers.newThread());
                    }
                })
                .toList()
                .toObservable();


        Observable<Actor> actorService = Observable.zip(PeopleManager.getInstance().getDetails(actor.getId()).toObservable(),
                fullCreditsStream,
                moviesStream,
                new Function3<PersonDTO, List<MovieCreditsDTO>, List<MovieDTO>, Actor>() {

                    @Override
                    public Actor apply(@NonNull PersonDTO personDTO,
                                       @NonNull List<MovieCreditsDTO> credits,
                                       @NonNull List<MovieDTO> movieDTOs) throws Exception {

                        Actor actor = PersonMapper.toActorFromActor(BiographyFragment.this.actor, personDTO);

                        ArrayList<Genre> genres = DataProvider.getInstance().getSelectedGenres();
                        ArrayList<Director> directors = DataProvider.getInstance().getDirectors();
                        DataProvider.getInstance().getSelectedGenres().clear();

                        for (MovieDTO movieDTO : movieDTOs) {
                            boolean foundGenre = false;
                            for (Genre genre : genres) {
                                if (movieDTO.getGenres() != null &&
                                        !movieDTO.getGenres().isEmpty() &&
                                        genre.getId() == movieDTO.getGenres().get(0).getId())
                                    foundGenre = true;
                            }
                            if (!foundGenre)
                                genres.add(GenreMapper.toGenre(movieDTO.getGenres().get(0)));

                            for (MovieCreditsDTO movieCreditsDTO : credits)
                                if (Objects.equals(movieCreditsDTO.getId(), movieDTO.getId()))
                                    for (CrewItemDTO crewItem : movieCreditsDTO.getCrew()) {
                                        if (!crewItem.getJob().equals(DIRECTOR_ROLE))
                                            continue;

                                        boolean directorFound = false;
                                        for (Director director : directors)
                                            if (director.getId() == crewItem.getId())
                                                directorFound = true;

                                        if (!directorFound)
                                            directors.add(new Director(crewItem.getId(), crewItem.getName(), ""));
                                    }
                        }

                        DataProvider.getInstance().setGenres(genres);
                        DataProvider.getInstance().setDirectors(directors);

                        actor.setGenres(genres);
                        actor.setDirectors(directors);

                        return actor;
                    }
                })
                .retry()
                .take(1);

        subscriberHolder.add(
                ActorDbService.fullActor(actor.getId())
                        .take(1)
                        .compose(Rx.<Actor>applySchedulers())
                        .subscribe(new Consumer<Actor>() {
                            @Override
                            public void accept(@NonNull Actor actor) throws Exception {
                                bookmarked.setEnabled(true);
                                bookmarked.setChecked(actor.getId() != -1);
                            }
                        }));

        subscriberHolder.add(
                Observable.merge(ActorDbService.fullActor(actor.getId()), actorService)
                        .compose(Rx.<Actor>applySchedulers())
                        .filter(new Predicate<Actor>() {
                            @Override
                            public boolean test(@NonNull Actor actor) throws Exception {
                                return actor.getId() != -1;
                            }
                        })
                        .take(1)
                        .subscribe(new Consumer<Actor>() {
                            @Override
                            public void accept(@NonNull Actor actor) throws Exception {
                                binding.setActor(actor);
                                binding.notifyChange();
                            }
                        })
        );

    }

    @Override
    public void onStop() {
        super.onStop();
        subscriberHolder.dispose();
    }
}
