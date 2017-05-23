package ba.unsa.etf.rma.elvircrn.movieinfo.services.interfaces;

import ba.unsa.etf.rma.elvircrn.movieinfo.services.dto.ActorSearchResponseDTO;
import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ISearchService {
    @GET("search/person")
    Observable<ActorSearchResponseDTO> searchActorsByName(@Query("query")String name);
}
