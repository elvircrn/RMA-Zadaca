package ba.unsa.etf.rma.elvircrn.movieinfo;


import java.util.ArrayList;

public class DataProvider {
    private static DataProvider instance = null;

    public static void setInstance(DataProvider instance) {
        DataProvider.instance = instance;
    }

    public ArrayList<Glumac> getGlumci() {
        return glumci;
    }

    public void setGlumci(ArrayList<Glumac> glumci) {
        this.glumci = glumci;
    }

    public ArrayList<Reziser> getReziseri() {
        return null;
    }

    public void setReziseri(ArrayList<Reziser> reziseri) {
        this.reziseri = reziseri;
    }

    private ArrayList<Glumac> glumci;
    private ArrayList<Reziser> reziseri;

    protected DataProvider() {
        glumci = new ArrayList<>();
        reziseri = new ArrayList<>();
    }

    public static DataProvider getInstance() {
        if (instance == null)
            instance = new DataProvider();
        return instance;
    }

    public void seed() {
        glumci.add(new GlumacBuilder().setIme("Stan")
                .setPrezime("Lee")
                .setGodinaRodjenja(1922)
                .setMjestoRodjenja("New York")
                .setRating(5)
                .createGlumac());
    }
}
