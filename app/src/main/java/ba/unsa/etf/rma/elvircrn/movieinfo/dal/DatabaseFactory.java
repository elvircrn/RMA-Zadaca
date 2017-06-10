package ba.unsa.etf.rma.elvircrn.movieinfo.dal;

import android.arch.persistence.room.Room;
import android.content.Context;

public class DatabaseFactory {
    private static final String MOVIE_DATABASE_NAME = "movie-info";

    public static AppDatabase create(Context context) {
        return Room.databaseBuilder(context,
                AppDatabase.class, MOVIE_DATABASE_NAME).build();
    }

    public static AppDatabase create(Context context, String databaseName) {
        return Room.databaseBuilder(context,
                AppDatabase.class, databaseName).build();
    }
}

