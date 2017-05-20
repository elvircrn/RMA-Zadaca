package ba.unsa.etf.rma.elvircrn.movieinfo.services;

import rx.Observable;

import ba.unsa.etf.rma.elvircrn.movieinfo.services.dto.ActorSearchResponseDTO;
import ba.unsa.etf.rma.elvircrn.movieinfo.services.interfaces.ITheMovieDBService;

public class TheMovieDBService implements ITheMovieDBService {
    private final ITheMovieDBService theMovieDBService;

    public TheMovieDBService(ITheMovieDBService theMovieDBService) {
        this.theMovieDBService = theMovieDBService;
    }

    @Override
    public Observable<ActorSearchResponseDTO> searchActorsByName(String name) {
        return theMovieDBService.searchActorsByName(name);
    }

    public interface GetActorSearchResultCallback{
        void onSuccess(ActorSearchResponseDTO cityListResponse);
        void onError(Throwable e);
    }
}
