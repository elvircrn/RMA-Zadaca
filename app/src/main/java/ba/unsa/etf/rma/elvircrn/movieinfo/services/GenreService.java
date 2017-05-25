package ba.unsa.etf.rma.elvircrn.movieinfo.services;

import ba.unsa.etf.rma.elvircrn.movieinfo.services.dto.GenresDTO;
import ba.unsa.etf.rma.elvircrn.movieinfo.services.interfaces.IGenreService;
import io.reactivex.Single;

public class GenreService implements IGenreService {
    private final IGenreService service;

    public GenreService(IGenreService service) {
        this.service = service;
    }

    @Override
    public Single<GenresDTO> getGenres() {
        return service.getGenres();
    }
}
