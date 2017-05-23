package ba.unsa.etf.rma.elvircrn.movieinfo.mappers;

import java.util.ArrayList;
import java.util.List;

import ba.unsa.etf.rma.elvircrn.movieinfo.models.Movie;
import ba.unsa.etf.rma.elvircrn.movieinfo.services.dto.MovieDTO;

public class MovieMapper {
    public static Movie toMovie(MovieDTO movieDTO) {
        Movie movie = new Movie();
        // TODO: Finish mapping
        movie.setGenreIds(movieDTO.getGenreIds());
        return movie;
    }

    public static List<Movie> toMovies(List<MovieDTO> movieDTOs) {
        List<Movie> movies = new ArrayList<>();
        if (movieDTOs == null)
            return movies;
        for (MovieDTO movieDTO : movieDTOs)
            movies.add(toMovie(movieDTO));
        return movies;
    }
}
