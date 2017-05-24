package ba.unsa.etf.rma.elvircrn.movieinfo.managers;

import ba.unsa.etf.rma.elvircrn.movieinfo.factories.services.ServiceFactory;
import ba.unsa.etf.rma.elvircrn.movieinfo.services.dto.ActorSearchResponseDTO;
import ba.unsa.etf.rma.elvircrn.movieinfo.services.interfaces.ISearchService;
import io.reactivex.Single;

/**
 * Initialization-on-demand holder idion za thread-safe i brzu inicijalizaciju.
 */
public class SearchManager {
    ISearchService service;

    private static class LazyHolder {
        static final SearchManager INSTANCE = new SearchManager();
    }

    private SearchManager() {
        service = ServiceFactory.getInstance().createSerachService();
    }

    public Single<ActorSearchResponseDTO> searchActorByName(String name) {
        return service.searchActorsByName(name);
    }

    public static SearchManager getInstance() {
        return LazyHolder.INSTANCE;
    }
}




