package ba.unsa.etf.rma.elvircrn.movieinfo.managers;

import ba.unsa.etf.rma.elvircrn.movieinfo.factories.services.ServiceFactory;
import ba.unsa.etf.rma.elvircrn.movieinfo.services.dto.MovieCreditsDTO;
import ba.unsa.etf.rma.elvircrn.movieinfo.services.dto.PersonDTO;
import ba.unsa.etf.rma.elvircrn.movieinfo.services.interfaces.IPeopleService;
import rx.Observable;

public class PeopleManager {
    private IPeopleService service;

    private static class LazyHolder {
        static final PeopleManager INSTANCE = new PeopleManager();
    }

    public static PeopleManager getInstance() {
        return LazyHolder.INSTANCE;
    }

    private PeopleManager() {
        service = ServiceFactory.getInstance().createPeopleService();
    }

    public Observable<MovieCreditsDTO> getMovieCredits(int personId) {
        return service.getMovieCredits(personId);
    }

    public Observable<PersonDTO> getDetails(int personId) {
        return service.getDetails(personId);
    }
}
