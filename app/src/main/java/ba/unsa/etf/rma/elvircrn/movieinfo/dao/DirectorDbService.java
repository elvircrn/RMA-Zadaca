package ba.unsa.etf.rma.elvircrn.movieinfo.dao;

import java.util.ArrayList;
import java.util.List;

import ba.unsa.etf.rma.elvircrn.movieinfo.DataProvider;
import ba.unsa.etf.rma.elvircrn.movieinfo.helpers.JHelpers;
import ba.unsa.etf.rma.elvircrn.movieinfo.models.ActorDirector;
import ba.unsa.etf.rma.elvircrn.movieinfo.models.Director;
import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class DirectorDbService {
    public static void addDirectors(List<Director> directors) {
        for (Director director : directors) {
            DataProvider.getInstance().getDb().directorDAO()
                    .insertAll(director);
        }
    }

    public static Observable<List<Director>> getDirectorStream(int actorId) {
        return DataProvider.getInstance().getDb().actorDirectorDAO()
                .findByActorId(actorId)
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

    public static void deleteInvalidDirectors() {
        Observable<List<Director>> directorObservable = DataProvider.getInstance().getDb().directorDAO().getAll().toObservable();
        Observable<List<Integer>> validDirectorIds = DataProvider.getInstance().getDb().actorDirectorDAO()
                .getAll()
                .toObservable()
                .map(new Function<List<ActorDirector>, List<Integer>>() {
                    @Override
                    public List<Integer> apply(@NonNull List<ActorDirector> actorDirectors) throws Exception {
                        return JHelpers.toList(Observable.fromIterable(actorDirectors)
                                .map(new Function<ActorDirector, Integer>() {
                                    @Override
                                    public Integer apply(@NonNull ActorDirector actorDirector) throws Exception {
                                        return actorDirector.getId();
                                    }
                                })
                                .distinct()
                                .blockingIterable());
                    }
                });

        Observable.zip(validDirectorIds.take(1), directorObservable.take(1), new BiFunction<List<Integer>, List<Director>, List<Director>>() {
            @Override
            public List<Director> apply(@NonNull List<Integer> integers, @NonNull List<Director> directors) throws Exception {
                List<Director> directorsForDelete = new ArrayList<>();

                for (Director director : directors) {
                    if (!integers.contains(director.getId())) {
                        directorsForDelete.add(director);
                    }
                }

                return directorsForDelete;
            }
        })
                .take(1)
                .observeOn(Schedulers.newThread())
                .subscribe(new Consumer<List<Director>>() {
                    @Override
                    public void accept(@NonNull List<Director> directors) throws Exception {
                        for (Director director : directors) {
                            DataProvider.getInstance().getDb().directorDAO().delete(director);
                        }
                    }
                });
    }
}
