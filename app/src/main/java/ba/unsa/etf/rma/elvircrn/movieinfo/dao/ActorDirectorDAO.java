package ba.unsa.etf.rma.elvircrn.movieinfo.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

import ba.unsa.etf.rma.elvircrn.movieinfo.models.ActorDirector;
import ba.unsa.etf.rma.elvircrn.movieinfo.models.ActorGenre;
import io.reactivex.Flowable;

@Dao
public interface ActorDirectorDAO {
    @Query("SELECT * from actordirector where actor_id = (:actorId)")
    Flowable<List<ActorDirector>> findActorWithDirectorsById(int actorId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(ActorDirector... actorDirectors);

    @Delete
    void delete(ActorDirector actorDirector);

}
