package ba.unsa.etf.rma.elvircrn.movieinfo.services.dto;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MovieCreditsDTO {

    @SerializedName("cast")
    @Expose
    private List<CastItemDTO> cast = null;
    @SerializedName("crew")
    @Expose
    private List<CrewItemDTO> crew = null;
    @SerializedName("id")
    @Expose
    private Integer id;

    public List<CastItemDTO> getCast() {
        return cast;
    }

    public void setCast(List<CastItemDTO> cast) {
        this.cast = cast;
    }

    public List<CrewItemDTO> getCrew() {
        return crew;
    }

    public void setCrew(List<CrewItemDTO> crew) {
        this.crew = crew;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

}

