package ba.unsa.etf.rma.elvircrn.movieinfo.services.interfaces;

import ba.unsa.etf.rma.elvircrn.movieinfo.services.dto.ActorSearchResponseDTO;

import ba.unsa.etf.rma.elvircrn.movieinfo.services.dto.MovieSearchResponseDTO;
import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ISearchService {
    @GET("search/person")
    Single<ActorSearchResponseDTO> searchActorsByName(@Query("query")String name);

    @GET("search/movie")
    Single<MovieSearchResponseDTO> searchMovieByName(@Query("query")String name);
}
