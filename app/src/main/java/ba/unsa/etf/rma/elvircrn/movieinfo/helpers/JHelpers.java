package ba.unsa.etf.rma.elvircrn.movieinfo.helpers;


import java.util.ArrayList;
import java.util.List;

public class JHelpers {
    /* ?? operator */
    public static <T> T coalesce(T one, T two) {
        return one != null ? one : two;
    }

    public static <E> List<E> toList(Iterable<E> iterable) {
        if(iterable instanceof List) {
            return (List<E>) iterable;
        }
        ArrayList<E> list = new ArrayList<E>();
        if(iterable != null) {
            for(E e: iterable) {
                list.add(e);
            }
        }
        return list;
    }
}
