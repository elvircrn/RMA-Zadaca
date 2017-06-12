package ba.unsa.etf.rma.elvircrn.movieinfo.dao;

import java.util.List;

import ba.unsa.etf.rma.elvircrn.movieinfo.DataProvider;
import ba.unsa.etf.rma.elvircrn.movieinfo.helpers.Rx;
import ba.unsa.etf.rma.elvircrn.movieinfo.models.Actor;
import ba.unsa.etf.rma.elvircrn.movieinfo.models.ActorDirector;
import ba.unsa.etf.rma.elvircrn.movieinfo.models.ActorGenre;
import ba.unsa.etf.rma.elvircrn.movieinfo.models.Director;
import ba.unsa.etf.rma.elvircrn.movieinfo.models.Genre;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Function3;
import io.reactivex.functions.Predicate;


public class ActorDbService {
    public static void deleteActor(Actor actor) {
        DataProvider.getInstance().getDb().actorDAO().delete(actor);
        deleteActorGenre(actor);
        deleteActorDirector(actor);
    }

    private static void deleteActorDirector(Actor actor) {
        DataProvider.getInstance().getDb().actorDirectorDAO().findActorWithDirectorsById(actor.getId())
                .toObservable()
                .compose(Rx.<List<ActorDirector>>applyDbSchedulers())
                .subscribe(new Consumer<List<ActorDirector>>() {
                    @Override
                    public void accept(@NonNull List<ActorDirector> actorDirectors) throws Exception {
                        for (ActorDirector actorDirector : actorDirectors) {
                            DataProvider.getInstance().getDb().actorDirectorDAO().delete(actorDirector);
                        }
                    }
                });
    }

    private static void deleteActorGenre(Actor actor) {
        DataProvider.getInstance().getDb().actorGenreDAO().findActorWithGenresById(actor.getId())
                .toObservable()
                .compose(Rx.<List<ActorGenre>>applyDbSchedulers())
                .subscribe(new Consumer<List<ActorGenre>>() {
                    @Override
                    public void accept(@NonNull List<ActorGenre> actorGenres) throws Exception {
                        for (ActorGenre actorGenre : actorGenres) {
                            DataProvider.getInstance().getDb().actorGenreDAO().delete(actorGenre);
                        }
                    }
                });

    }

    private static Observable<List<Director>> getDirectorStream(int actorId) {
        return DataProvider.getInstance().getDb().actorDirectorDAO()
                .findActorWithDirectorsById(actorId)
                .toObservable()
                .flatMap(new Function<List<ActorDirector>, Observable<List<Director>>>() {
                    @Override
                    public Observable<List<Director>> apply(@NonNull List<ActorDirector> actorDirectors) throws Exception {
                        int[] directorIds = new int[actorDirectors.size()];
                        for (int i = 0; i < actorDirectors.size(); i++) {
                            directorIds[i] = actorDirectors.get(i).getDirectorId();
                        }
                        return DataProvider.getInstance().getDb().directorDAO()
                                .loadAllByIds(directorIds).toObservable();
                    }
                });
    }

    private static Observable<List<Genre>> getGenreStream(int actorId) {
        return DataProvider.getInstance().getDb().actorGenreDAO()
                .findActorWithGenresById(actorId)
                .toObservable()
                .flatMap(new Function<List<ActorGenre>, Observable<List<Genre>>>() {
                    @Override
                    public Observable<List<Genre>> apply(@NonNull List<ActorGenre> actorGenres) throws Exception {
                        int[] genreIds = new int[actorGenres.size()];
                        for (int i = 0; i < actorGenres.size(); i++) {
                            genreIds[i] = actorGenres.get(i).getGenreId();
                        }
                        return DataProvider.getInstance().getDb().genreDAO()
                                .loadAllByIds(genreIds).toObservable();
                    }
                });
    }

    private static Observable<Actor> getBasicActorStream(int actorId) {
        return DataProvider.getInstance().getDb().actorDAO()
                .findById(actorId)
                .map(new Function<List<Actor>, Actor>() {
                    @Override
                    public Actor apply(@NonNull List<Actor> actors) throws Exception {
                        if (actors.isEmpty()) {
                            Actor actor = new Actor();
                            actor.setId(-1);
                            return actor;
                        } else {
                            return actors.get(0);
                        }
                    }
                })
                .toObservable();

    }

    public static Observable<Actor> fullActor(int actorId) {
        return Observable.zip(getBasicActorStream(actorId), getGenreStream(actorId), getDirectorStream(actorId), new Function3<Actor, List<Genre>, List<Director>, Actor>() {
            @Override
            public Actor apply(@NonNull Actor actor, @NonNull List<Genre> genres, @NonNull List<Director> directors) throws Exception {
                actor.setGenres(genres);
                actor.setDirectors(directors);
                return actor;
            }
        });
    }

    public static void addActor(Actor actor) {
        DataProvider.getInstance().getDb().actorDAO().insertAll(actor);
        DirectorDbService.addDirectors(actor.getDirectors());
        GenreDbService.addGenres(actor.getGenres());
        for (Genre genre : actor.getGenres()) {
            DataProvider.getInstance().getDb().actorGenreDAO()
                    .insertAll(new ActorGenre(0, actor.getId(), genre.getId()));
        }
        for (Director director : actor.getDirectors()) {
            DataProvider.getInstance().getDb().actorDirectorDAO()
                    .insertAll(new ActorDirector(0, actor.getId(), director.getId()));
        }
    }

    public static Observable<List<Actor>> findByDirectorName(final String directorName) {
        return DataProvider.getInstance().getDb().actorDAO()
                .getAll()
                .toObservable()
                .compose(Rx.<List<Actor>>applyDbSchedulers())
                .flatMap(new Function<List<Actor>, Observable<Actor>>() {
                    @Override
                    public Observable<Actor> apply(@NonNull List<Actor> actors) throws Exception {
                        return Observable.fromIterable(actors)
                                .flatMap(new Function<Actor, ObservableSource<Actor>>() {
                                    @Override
                                    public ObservableSource<Actor> apply(@NonNull Actor actor) throws Exception {
                                        return fullActor(actor.getId());
                                    }
                                });
                    }
                })
                .toList()
                .toObservable()
                .flatMap(new Function<List<Actor>, Observable<Actor>>() {
                    @Override
                    public Observable<Actor> apply(@NonNull List<Actor> actors) throws Exception {
                        return Observable.fromIterable(actors);
                    }
                })
                .filter(new Predicate<Actor>() {
                    @Override
                    public boolean test(@NonNull Actor actor) throws Exception {
                        for (Director director : actor.getDirectors()) {
                            if (director.getName().contains(directorName))
                                return true;
                        }
                        return false;
                    }
                })
                .toList()
                .toObservable();
    }
}
