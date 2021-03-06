package ba.unsa.etf.rma.elvircrn.movieinfo.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

import ba.unsa.etf.rma.elvircrn.movieinfo.models.Actor;
import io.reactivex.Flowable;

@Dao
public interface ActorDAO {
    @Query("SELECT * FROM actor")
    Flowable<List<Actor>> getAll();

    @Query("SELECT * FROM actor WHERE id IN (:actorIds)")
    Flowable<List<Actor>> loadAllByIds(int[] actorIds);

    @Query("SELECT * FROM actor WHERE name LIKE :first")
    Flowable<List<Actor>> findByName(String first);

    @Query("SELECT * FROM actor WHERE id = (:id)")
    Flowable<List<Actor>> findById(int id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(Actor... actors);

    @Delete
    void delete(Actor actor);
}
