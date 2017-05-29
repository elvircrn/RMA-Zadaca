package ba.unsa.etf.rma.elvircrn.movieinfo.mappers;

import java.util.ArrayList;
import java.util.List;

import ba.unsa.etf.rma.elvircrn.movieinfo.models.CrewItem;
import ba.unsa.etf.rma.elvircrn.movieinfo.models.Movie;
import ba.unsa.etf.rma.elvircrn.movieinfo.services.dto.CastItemDTO;
import ba.unsa.etf.rma.elvircrn.movieinfo.services.dto.CrewItemDTO;
import ba.unsa.etf.rma.elvircrn.movieinfo.services.dto.MovieDTO;

public class MovieMapper {
    public static Movie toMovie(MovieDTO movieDTO) {
        Movie movie = new Movie();
        movie.setAdult(movieDTO.getAdult());
        movie.setGenreIds(movieDTO.getGenreIds());
        movie.setOverview(movieDTO.getOverview());
        movie.setOriginalTitle(movieDTO.getOriginalTitle());
        movie.setReleaseDate(movieDTO.getReleaseDate());
        movie.setFirstAirDate(movieDTO.getFirstAirDate());
        movie.setId(movieDTO.getId());
        return movie;
    }

    public static Movie toMovie(CastItemDTO castItemDTO) {
        Movie movie = new Movie();
        movie.setId(castItemDTO.getId());
        movie.setName(castItemDTO.getTitle());
        movie.setReleaseDate(castItemDTO.getReleaseDate());
        return movie;
    }

    public static List<Movie> toMovieList(List<CastItemDTO> castItemDTOs) {
        List<Movie> movies = new ArrayList<>();
        if (castItemDTOs == null || castItemDTOs.isEmpty())
            return movies;
        for (CastItemDTO castItemDTO : castItemDTOs)
            movies.add(toMovie(castItemDTO));
        return movies;
    }

    public static List<CrewItem> toCrewItems(List<CrewItemDTO> crewItemDTOs) {
        List<CrewItem> crewItems = new ArrayList<>();

        if (crewItemDTOs == null || crewItemDTOs.isEmpty())
            return crewItems;

        for (CrewItemDTO crewItemDTO : crewItemDTOs) {
            crewItems.add(toCrew(crewItemDTO));
        }

        return crewItems;
    }



    public static CrewItem toCrew(CrewItemDTO crewItemDTO) {
        CrewItem crewItem = new CrewItem();
        crewItem.setId(crewItemDTO.getId());
        crewItem.setDepartment(crewItemDTO.getDepartment());
        crewItem.setId(crewItemDTO.getId());
        crewItem.setJob(crewItemDTO.getJob());
        crewItem.setName(crewItemDTO.getName());
        crewItem.setProfilePath(crewItemDTO.getProfilePath());
        return crewItem;
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
