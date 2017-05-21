package ba.unsa.etf.rma.elvircrn.movieinfo.services.interfaces;

import java.util.Map;

import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import rx.Observable;

import ba.unsa.etf.rma.elvircrn.movieinfo.services.dto.ActorSearchResponseDTO;
import retrofit2.http.GET;

public interface ISearchService {
    @GET("3/search/person")
    Observable<ActorSearchResponseDTO> searchActorsByName(@Query("query")String name);
}
