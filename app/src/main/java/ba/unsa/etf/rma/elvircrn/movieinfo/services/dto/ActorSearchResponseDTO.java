package ba.unsa.etf.rma.elvircrn.movieinfo.services.dto;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class ActorSearchResponseDTO {

    @SerializedName("page")
    @Expose
    private Integer page;
    @SerializedName("results")
    @Expose
    private List<PersonDTO> actors = null;
    @SerializedName("total_actors")
    @Expose
    private Integer totalActors;
    @SerializedName("total_pages")
    @Expose
    private Integer totalPages;


    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public List<PersonDTO> getActors() {
        return actors;
    }

    public void setActors(ArrayList<PersonDTO> actors) {
        this.actors = actors;
    }

    public Integer getTotalActors() {
        return totalActors;
    }

    public void setTotalActors(Integer totalActors) {
        this.totalActors = totalActors;
    }

    public Integer getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(Integer totalPages) {
        this.totalPages = totalPages;
    }

}
