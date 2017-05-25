package ba.unsa.etf.rma.elvircrn.movieinfo.services;

import ba.unsa.etf.rma.elvircrn.movieinfo.services.dto.ActorSearchResponseDTO;
import ba.unsa.etf.rma.elvircrn.movieinfo.services.interfaces.ISearchService;
import io.reactivex.Single;

public class SearchService extends BaseService implements ISearchService {
    private final ISearchService theMovieDBService;

    public SearchService(ISearchService theMovieDBService) {
        this.theMovieDBService = theMovieDBService;
    }

    @Override
    public Single<ActorSearchResponseDTO> searchActorsByName(String name) {
        return theMovieDBService.searchActorsByName(name);
    }
}