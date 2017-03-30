package ba.unsa.etf.rma.elvircrn.movieinfo.helpers;


public class JHelpers {
    /* ?? operator */
    public static <T> T coalesce(T one, T two) {
        return one != null ? one : two;
    }
}
