package ba.unsa.etf.rma.elvircrn.movieinfo.models;


import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;

@Entity
public class Director {

    @ColumnInfo(name = "id")
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
}
