package ba.unsa.etf.rma.elvircrn.movieinfo.services.interfaces;

import ba.unsa.etf.rma.elvircrn.movieinfo.services.dto.MovieCreditsDTO;
import rx.Observable;

import ba.unsa.etf.rma.elvircrn.movieinfo.services.dto.PersonDTO;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface IPeopleService {
    @GET("person/{person_id}")
    Observable<PersonDTO> getDetails(@Path("person_id")int personId);

    @GET("person/{person_id}/movie_credits")
    Observable<MovieCreditsDTO> getMovieCredits(@Path("person_id")int personId);

}
