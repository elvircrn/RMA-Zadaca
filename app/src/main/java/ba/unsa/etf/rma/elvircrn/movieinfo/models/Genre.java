package ba.unsa.etf.rma.elvircrn.movieinfo.models;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.TreeMap;

@Entity
public class Genre implements Parcelable {
    @ColumnInfo(name = "id")
    @PrimaryKey
    protected int id;
    @ColumnInfo(name = "name")
    protected String name;

    @ColumnInfo(name = "img_url")
    protected String imgUrl = "genredefault";
    @Ignore
    private static String locale;

    @Ignore
    public Genre() { }

    @Ignore
    public Genre(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public Genre(int id, String name, String imgUrl) {
        this.id = id;
        this.name = name;
        this.setImgUrl(imgUrl);
    }

    @Ignore
    public Genre(String name, String imgUrl) {
        this.name = name;
        this.setImgUrl(imgUrl);
    }

    public static void setLocale(String locale) {
        Genre.locale = locale;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl.replace(" ", "").toLowerCase();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) { this.id = id; }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.name);
        dest.writeString(this.imgUrl);
    }

    protected Genre(Parcel in) {
        this.id = in.readInt();
        this.name = in.readString();
        this.imgUrl = in.readString();
    }

    public static final Parcelable.Creator<Genre> CREATOR = new Parcelable.Creator<Genre>() {
        public Genre createFromParcel(Parcel source) {
            return new Genre(source);
        }

        public Genre[] newArray(int size) {
            return new Genre[size];
        }
    };
}
