package ba.unsa.etf.rma.elvircrn.movieinfo.services.interfaces;

import rx.Observable;
import ba.unsa.etf.rma.elvircrn.movieinfo.services.dto.GenresDTO;
import retrofit2.http.GET;

public interface IGenreService {
    @GET("genre/movie/list")
    Observable<GenresDTO> getGenres();
}
