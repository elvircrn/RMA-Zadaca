package ba.unsa.etf.rma.elvircrn.movieinfo.dao;

import java.util.List;

import ba.unsa.etf.rma.elvircrn.movieinfo.DataProvider;
import ba.unsa.etf.rma.elvircrn.movieinfo.models.Genre;

public class GenreDbService {
    public static void addGenres(List<Genre> genres) {
        for (Genre genre : genres) {
            DataProvider.getInstance().getDb().genreDAO()
                    .insertAll(genre);
        }
    }
}
