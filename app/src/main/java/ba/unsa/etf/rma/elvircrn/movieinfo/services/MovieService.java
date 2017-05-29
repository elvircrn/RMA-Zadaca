package ba.unsa.etf.rma.elvircrn.movieinfo.services;

import ba.unsa.etf.rma.elvircrn.movieinfo.services.dto.MovieCreditsDTO;
import ba.unsa.etf.rma.elvircrn.movieinfo.services.dto.MovieDTO;
import ba.unsa.etf.rma.elvircrn.movieinfo.services.interfaces.IMovieService;
import io.reactivex.Single;
import retrofit2.http.Path;

public class MovieService implements IMovieService {
    private final IMovieService service;

    public MovieService(IMovieService service) {
        this.service = service;
    }

    @Override
    public Single<MovieCreditsDTO> getMovieCredits(@Path("movie_id") int movieId) {
        return service.getMovieCredits(movieId);
    }

    @Override
    public Single<MovieDTO> getMovie(@Path("movie_id") int movieId) {
        return service.getMovie(movieId);
    }
}
