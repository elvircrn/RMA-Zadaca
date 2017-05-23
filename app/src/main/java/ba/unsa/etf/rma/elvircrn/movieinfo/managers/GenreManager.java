package ba.unsa.etf.rma.elvircrn.movieinfo.managers;

import ba.unsa.etf.rma.elvircrn.movieinfo.factories.services.ServiceFactory;
import ba.unsa.etf.rma.elvircrn.movieinfo.services.dto.GenresDTO;
import ba.unsa.etf.rma.elvircrn.movieinfo.services.interfaces.IGenreService;
import io.reactivex.Observable;

public class GenreManager {
    IGenreService service;

    private static class LazyHolder {
        static final GenreManager INSTANCE = new GenreManager();
    }

    private GenreManager() {
        this.service = ServiceFactory.getInstance().createGenreService();
    }

    public Observable<GenresDTO> getGenres() {
        return service.getGenres();
    }

    public static GenreManager getInstance() {
        return LazyHolder.INSTANCE;
    }
}
