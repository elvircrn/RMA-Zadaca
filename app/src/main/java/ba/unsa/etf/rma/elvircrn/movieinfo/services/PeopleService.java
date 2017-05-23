package ba.unsa.etf.rma.elvircrn.movieinfo.services;

import ba.unsa.etf.rma.elvircrn.movieinfo.services.dto.MovieCreditsDTO;
import ba.unsa.etf.rma.elvircrn.movieinfo.services.dto.PersonDTO;
import ba.unsa.etf.rma.elvircrn.movieinfo.services.interfaces.IPeopleService;
import io.reactivex.Observable;
import retrofit2.http.Path;

public class PeopleService implements IPeopleService {
    private final IPeopleService service;

    public PeopleService(IPeopleService service) {
        this.service = service;
    }

    public Observable<PersonDTO> getActorById(int id) {
        return service.getDetails(id);
    }

    @Override
    public Observable<PersonDTO> getDetails(@Path("person_id") int personId) {
        return service.getDetails(personId);
    }

    @Override
    public Observable<MovieCreditsDTO> getMovieCredits(@Path("person_id") int personId) {
        return service.getMovieCredits(personId);
    }
}
