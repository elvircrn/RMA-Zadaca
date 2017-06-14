package ba.unsa.etf.rma.elvircrn.movieinfo.managers;

import ba.unsa.etf.rma.elvircrn.movieinfo.factories.services.ServiceFactory;
import ba.unsa.etf.rma.elvircrn.movieinfo.services.dto.MovieCreditsDTO;
import ba.unsa.etf.rma.elvircrn.movieinfo.services.dto.MovieDTO;
import ba.unsa.etf.rma.elvircrn.movieinfo.services.dto.MovieSearchResponseDTO;
import ba.unsa.etf.rma.elvircrn.movieinfo.services.interfaces.IMovieService;
import io.reactivex.Single;

public class MovieManager {
    private IMovieService service;

    private static class LazyHolder {
        static final MovieManager INSTANCE = new MovieManager();
    }

    public static MovieManager getInstance() {
        return LazyHolder.INSTANCE;
    }

    private MovieManager() {
        service = ServiceFactory.getInstance().createMovieService();
    }

    public Single<MovieCreditsDTO> getMovieCredits(int movieId) {
        return service.getMovieCredits(movieId);
    }

    public Single<MovieDTO> getMovie(int movieId) {
        return service.getMovie(movieId);
    }
}
