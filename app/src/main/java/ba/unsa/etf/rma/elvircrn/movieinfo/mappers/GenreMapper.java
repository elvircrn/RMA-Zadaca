package ba.unsa.etf.rma.elvircrn.movieinfo.mappers;

import java.util.ArrayList;
import java.util.List;

import ba.unsa.etf.rma.elvircrn.movieinfo.models.Genre;
import ba.unsa.etf.rma.elvircrn.movieinfo.services.dto.GenreDTO;
import ba.unsa.etf.rma.elvircrn.movieinfo.services.dto.GenresDTO;

public class GenreMapper {

    public static ArrayList<Genre> toGenres(GenresDTO genresDTO) {
        if (genresDTO == null)
            return new ArrayList<>();

        ArrayList<Genre> genres = new ArrayList<>();

        for (GenreDTO genreDTO : genresDTO.getGenres()) {
            genres.add(toGenre(genreDTO));
        }

        return genres;
    }

    public static Genre toGenre(GenreDTO genreDTO) {
        return new Genre(genreDTO.getId(), genreDTO.getName(), genreDTO.getName());
    }
}
