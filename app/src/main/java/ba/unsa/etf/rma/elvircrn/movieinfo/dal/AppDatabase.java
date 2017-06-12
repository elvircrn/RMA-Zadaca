package ba.unsa.etf.rma.elvircrn.movieinfo.dal;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;

import ba.unsa.etf.rma.elvircrn.movieinfo.dao.ActorDAO;
import ba.unsa.etf.rma.elvircrn.movieinfo.dao.ActorGenreDAO;
import ba.unsa.etf.rma.elvircrn.movieinfo.dao.DirectorDAO;
import ba.unsa.etf.rma.elvircrn.movieinfo.dao.GenreDAO;
import ba.unsa.etf.rma.elvircrn.movieinfo.dao.converters.GenderConverter;
import ba.unsa.etf.rma.elvircrn.movieinfo.models.Actor;
import ba.unsa.etf.rma.elvircrn.movieinfo.models.ActorGenre;
import ba.unsa.etf.rma.elvircrn.movieinfo.models.Director;
import ba.unsa.etf.rma.elvircrn.movieinfo.models.Genre;

@Database(entities = {Actor.class, Genre.class, Director.class, ActorGenre.class}, version = 6)
@TypeConverters({GenderConverter.class})
public abstract class AppDatabase extends RoomDatabase {
    public abstract ActorDAO actorDAO();
    public abstract GenreDAO genreDAO();
    public abstract DirectorDAO directorDAO();
    public abstract ActorGenreDAO actorGenreDAO();
}
