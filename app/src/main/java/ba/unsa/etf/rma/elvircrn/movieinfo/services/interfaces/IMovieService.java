package ba.unsa.etf.rma.elvircrn.movieinfo.services.interfaces;

import ba.unsa.etf.rma.elvircrn.movieinfo.models.Movie;
import ba.unsa.etf.rma.elvircrn.movieinfo.services.dto.MovieCreditsDTO;
import ba.unsa.etf.rma.elvircrn.movieinfo.services.dto.MovieDTO;
import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface IMovieService {
    @GET("movie/{movie_id}/credits")
    Single<MovieCreditsDTO> getMovieCredits(@Path("movie_id")int movieId);

    @GET("movie/{movie_id}")
    Single<MovieDTO> getMovie(@Path("movie_id")int movieId);
}
