package ba.unsa.etf.rma.elvircrn.movieinfo.dal;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;

import ba.unsa.etf.rma.elvircrn.movieinfo.dao.ActorDAO;
import ba.unsa.etf.rma.elvircrn.movieinfo.dao.converters.GenderConverter;
import ba.unsa.etf.rma.elvircrn.movieinfo.models.Actor;

@Database(entities = {Actor.class}, version = 3)
@TypeConverters({GenderConverter.class})
public abstract class AppDatabase extends RoomDatabase {
    public abstract ActorDAO actorDAO();
}
