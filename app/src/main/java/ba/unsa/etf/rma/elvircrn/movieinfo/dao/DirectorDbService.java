package ba.unsa.etf.rma.elvircrn.movieinfo.dao;

import java.util.List;

import ba.unsa.etf.rma.elvircrn.movieinfo.DataProvider;
import ba.unsa.etf.rma.elvircrn.movieinfo.models.Director;

public class DirectorDbService {
    public static void addDirectors(List<Director> directors) {
        for (Director director : directors) {
            DataProvider.getInstance().getDb().directorDAO()
                    .insertAll(director);
        }
    }
}
