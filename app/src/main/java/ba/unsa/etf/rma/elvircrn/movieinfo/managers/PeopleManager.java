package ba.unsa.etf.rma.elvircrn.movieinfo.managers;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import ba.unsa.etf.rma.elvircrn.movieinfo.DataProvider;
import ba.unsa.etf.rma.elvircrn.movieinfo.factories.services.ServiceFactory;
import ba.unsa.etf.rma.elvircrn.movieinfo.fragments.BiographyFragment;
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
import ba.unsa.etf.rma.elvircrn.movieinfo.services.interfaces.IPeopleService;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Single;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import io.reactivex.functions.Function3;
import io.reactivex.schedulers.Schedulers;

import static ba.unsa.etf.rma.elvircrn.movieinfo.mappers.DirectorMapper.DIRECTOR_ROLE;

public class PeopleManager {
    private IPeopleService service;

    private static class LazyHolder {
        static final PeopleManager INSTANCE = new PeopleManager();
    }

    public static PeopleManager getInstance() {
        return LazyHolder.INSTANCE;
    }

    private PeopleManager() {
        service = ServiceFactory.getInstance().createPeopleService();
    }

    public Single<MovieCreditsDTO> getMovieCredits(int personId) {
        return service.getMovieCredits(personId);
    }

    public Single<PersonDTO> getDetails(int personId) {
        return service.getDetails(personId);
    }

    public Observable<Actor> getFullActor(final Actor actorParam) {
        Observable<MovieCreditsDTO> creditsStream = getMovieCredits(actorParam.getId()).retry().toObservable().take(1);

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


        return Observable.zip(getDetails(actorParam.getId()).toObservable(),
                fullCreditsStream,
                moviesStream,
                new Function3<PersonDTO, List<MovieCreditsDTO>, List<MovieDTO>, Actor>() {

                    @Override
                    public Actor apply(@NonNull PersonDTO personDTO,
                                       @NonNull List<MovieCreditsDTO> credits,
                                       @NonNull List<MovieDTO> movieDTOs) throws Exception {

                        Actor actor = PersonMapper.toActorFromActor(actorParam, personDTO);

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
    }
}
