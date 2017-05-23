package ba.unsa.etf.rma.elvircrn.movieinfo.services.interfaces;

import ba.unsa.etf.rma.elvircrn.movieinfo.services.dto.GenresDTO;
import io.reactivex.Observable;
import retrofit2.http.GET;

public interface IGenreService {
    @GET("genre/movie/list")
    Observable<GenresDTO> getGenres();
}
