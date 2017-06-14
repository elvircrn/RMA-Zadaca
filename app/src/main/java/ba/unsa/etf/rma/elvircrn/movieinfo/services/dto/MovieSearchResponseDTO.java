package ba.unsa.etf.rma.elvircrn.movieinfo.services.dto;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class MovieSearchResponseDTO implements Parcelable {
    @SerializedName("results")
    @Expose
    private List<MovieDTO> movieDTOs;

    public MovieSearchResponseDTO() {
        movieDTOs = new ArrayList<>();
    }

    public MovieSearchResponseDTO(List<MovieDTO> movieDTOs) {
        this.movieDTOs = movieDTOs;
    }

    public List<MovieDTO> getMovieDTOs() {

        return movieDTOs;
    }

    public void setMovieDTOs(List<MovieDTO> movieDTOs) {
        this.movieDTOs = movieDTOs;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(this.movieDTOs);
    }

    protected MovieSearchResponseDTO(Parcel in) {
        this.movieDTOs = new ArrayList<MovieDTO>();
        in.readList(this.movieDTOs, List.class.getClassLoader());
    }

    public static final Parcelable.Creator<MovieSearchResponseDTO> CREATOR = new Parcelable.Creator<MovieSearchResponseDTO>() {
        public MovieSearchResponseDTO createFromParcel(Parcel source) {
            return new MovieSearchResponseDTO(source);
        }

        public MovieSearchResponseDTO[] newArray(int size) {
            return new MovieSearchResponseDTO[size];
        }
    };
}
