package ba.unsa.etf.rma.elvircrn.movieinfo.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

import ba.unsa.etf.rma.elvircrn.movieinfo.models.Actor;
import ba.unsa.etf.rma.elvircrn.movieinfo.models.ActorGenre;
import io.reactivex.Flowable;

@Dao
public interface ActorGenreDAO {
    @Query("SELECT * from actorgenre where actor_id = (:actorId)")
    Flowable<List<ActorGenre>> findActorWithGenresById(int actorId);

    @Query("SELECT * from actorgenre")
    Flowable<List<ActorGenre>> getAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(ActorGenre... actorGenres);

    @Delete
    void delete(ActorGenre actorGenre);
}
