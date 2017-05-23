package ba.unsa.etf.rma.elvircrn.movieinfo.services;

import rx.Observable;

import ba.unsa.etf.rma.elvircrn.movieinfo.services.dto.GenresDTO;
import ba.unsa.etf.rma.elvircrn.movieinfo.services.interfaces.IGenreService;

public class GenreService implements IGenreService {
    private final IGenreService service;

    public GenreService(IGenreService service) {
        this.service = service;
    }

    @Override
    public Observable<GenresDTO> getGenres() {
        return service.getGenres();
    }
}
