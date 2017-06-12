package ba.unsa.etf.rma.elvircrn.movieinfo.dao;


import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

import ba.unsa.etf.rma.elvircrn.movieinfo.models.Director;
import io.reactivex.Flowable;

@Dao
public interface DirectorDAO {
    @Query("SELECT * FROM director")
    Flowable<List<Director>> getAll();

    @Query("SELECT * FROM director WHERE id IN (:directorIds)")
    Flowable<List<Director>> loadAllByIds(int[] directorIds);

    @Query("SELECT * FROM director WHERE name LIKE :first")
    Flowable<List<Director>> findByName(String first);

    @Query("SELECT * FROM director WHERE id = (:id)")
    Flowable<List<Director>> findById(int id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(Director... directors);

    @Delete
    void delete(Director director);
}
