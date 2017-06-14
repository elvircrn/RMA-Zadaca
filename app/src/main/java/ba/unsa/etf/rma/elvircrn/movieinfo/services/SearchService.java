package ba.unsa.etf.rma.elvircrn.movieinfo.services;

import ba.unsa.etf.rma.elvircrn.movieinfo.services.dto.ActorSearchResponseDTO;
import ba.unsa.etf.rma.elvircrn.movieinfo.services.dto.MovieSearchResponseDTO;
import ba.unsa.etf.rma.elvircrn.movieinfo.services.interfaces.ISearchService;
import io.reactivex.Single;
import retrofit2.http.Query;

public class SearchService extends BaseService implements ISearchService {
    private final ISearchService theMovieDBService;

    public SearchService(ISearchService theMovieDBService) {
        this.theMovieDBService = theMovieDBService;
    }

    @Override
    public Single<ActorSearchResponseDTO> searchActorsByName(String name) {
        return theMovieDBService.searchActorsByName(name);
    }

    @Override
    public Single<MovieSearchResponseDTO> searchMovieByName(@Query("query") String name) {
        return theMovieDBService.searchMovieByName(name);
    }
}
