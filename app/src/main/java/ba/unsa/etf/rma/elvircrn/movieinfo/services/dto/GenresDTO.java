package ba.unsa.etf.rma.elvircrn.movieinfo.services.dto;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GenresDTO {

    @SerializedName("genres")
    @Expose
    private List<GenreDTO> genres = null;

    public List<GenreDTO> getGenres() {
        return genres;
    }

    public void setGenres(List<GenreDTO> genres) {
        this.genres = genres;
    }
}
