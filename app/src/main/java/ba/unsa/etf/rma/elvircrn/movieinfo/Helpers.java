package ba.unsa.etf.rma.elvircrn.movieinfo;


public class Helpers {
    public static <T> T coalesce(T one, T two) {
        return one != null ? one : two;
    }
}
