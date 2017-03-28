package ba.unsa.etf.rma.elvircrn.movieinfo.models;

public class Director {
    protected String name;
    protected String lastName;

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

    public Director(String name, String lastName) {
        this.name = name;
        this.lastName = lastName;
    }
}
