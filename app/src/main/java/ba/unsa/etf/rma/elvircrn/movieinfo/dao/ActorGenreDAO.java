package ba.unsa.etf.rma.elvircrn.movieinfo.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

import java.util.List;

import ba.unsa.etf.rma.elvircrn.movieinfo.models.ActorGenre;
import ba.unsa.etf.rma.elvircrn.movieinfo.models.ActorWithGenres;
import io.reactivex.Flowable;

@Dao
public interface ActorGenreDAO {
    @Query("SELECT * from actor where id = (:actorId)")
    Flowable<List<ActorWithGenres>> findActorWithGenresById(int actorId);
}
