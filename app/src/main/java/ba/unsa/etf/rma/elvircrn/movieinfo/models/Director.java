package ba.unsa.etf.rma.elvircrn.movieinfo.models;


import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

@Entity
public class Director implements Parcelable {
    @ColumnInfo(name = "id")
    @PrimaryKey
    protected int id = 0;
    @ColumnInfo(name = "name")
    protected String name = "";
    @Ignore
    protected String lastName = "";

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() { return name; }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Director() { }

    @Ignore
    public Director(String name, String lastName) {
        this.name = name;
        this.lastName = lastName;
    }

    @Ignore
    public Director(int id, String name, String lastName) {
        this.id = id;
        this.name = name;
        this.lastName = lastName;
    }

    public String getFullName() {
        return name + " " + lastName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.name);
        dest.writeString(this.lastName);
    }

    protected Director(Parcel in) {
        this.id = in.readInt();
        this.name = in.readString();
        this.lastName = in.readString();
    }

    public static final Parcelable.Creator<Director> CREATOR = new Parcelable.Creator<Director>() {
        public Director createFromParcel(Parcel source) {
            return new Director(source);
        }

        public Director[] newArray(int size) {
            return new Director[size];
        }
    };
}
