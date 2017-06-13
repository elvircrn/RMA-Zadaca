package ba.unsa.etf.rma.elvircrn.movieinfo.dao;

import java.util.ArrayList;
import java.util.List;

import ba.unsa.etf.rma.elvircrn.movieinfo.DataProvider;
import ba.unsa.etf.rma.elvircrn.movieinfo.helpers.JHelpers;
import ba.unsa.etf.rma.elvircrn.movieinfo.models.ActorGenre;
import ba.unsa.etf.rma.elvircrn.movieinfo.models.Genre;
import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class GenreDbService {
    public static void addGenres(List<Genre> genres) {
        for (Genre genre : genres) {
            DataProvider.getInstance().getDb().genreDAO()
                    .insertAll(genre);
        }
    }

    public static Observable<List<Genre>> getGenreStream(int actorId) {
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

    public static void deleteInvalidGenres() {
        Observable<List<Genre>> genreObservable = DataProvider.getInstance().getDb().genreDAO().getAll().toObservable();
        Observable<List<Integer>> validGenreIds = DataProvider.getInstance().getDb().actorGenreDAO()
                .getAll()
                .toObservable()
                .map(new Function<List<ActorGenre>, List<Integer>>() {
                    @Override
                    public List<Integer> apply(@NonNull List<ActorGenre> actorGenres) throws Exception {
                        return JHelpers.toList(Observable.fromIterable(actorGenres)
                                .map(new Function<ActorGenre, Integer>() {
                                    @Override
                                    public Integer apply(@NonNull ActorGenre actorGenre) throws Exception {
                                        return actorGenre.getId();
                                    }
                                })
                                .distinct()
                                .blockingIterable());
                    }
                });

        Observable.zip(validGenreIds.take(1), genreObservable.take(1), new BiFunction<List<Integer>, List<Genre>, List<Genre>>() {
            @Override
            public List<Genre> apply(@NonNull List<Integer> integers, @NonNull List<Genre> genres) throws Exception {
                List<Genre> genresForDelete = new ArrayList<>();

                for (Genre genre : genres) {
                    if (!integers.contains(genre.getId())) {
                        genresForDelete.add(genre);
                    }
                }

                return genresForDelete;
            }
        })
                .take(1)
                .observeOn(Schedulers.newThread())
                .subscribe(new Consumer<List<Genre>>() {
                    @Override
                    public void accept(@NonNull List<Genre> genres) throws Exception {
                        for (Genre genre : genres) {
                            DataProvider.getInstance().getDb().genreDAO().delete(genre);
                        }
                    }
                });
    }
}
