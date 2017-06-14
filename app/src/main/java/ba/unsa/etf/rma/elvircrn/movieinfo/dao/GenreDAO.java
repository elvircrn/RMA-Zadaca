package ba.unsa.etf.rma.elvircrn.movieinfo.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

import ba.unsa.etf.rma.elvircrn.movieinfo.models.Genre;
import io.reactivex.Flowable;

@Dao
public interface GenreDAO {
    @Query("SELECT * FROM genre")
    Flowable<List<Genre>> getAll();

    @Query("SELECT * FROM genre WHERE id IN (:genreIds)")
    Flowable<List<Genre>> loadAllByIds(int[] genreIds);

    @Query("SELECT * FROM genre WHERE name LIKE :first")
    Flowable<List<Genre>> findByName(String first);

    @Query("SELECT * FROM genre WHERE id = (:id)")
    Flowable<List<Genre>> findById(int id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(Genre... genres);

    @Delete
    void delete(Genre genre);
}
