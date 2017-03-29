package ba.unsa.etf.rma.elvircrn.movieinfo.helpers;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import ba.unsa.etf.rma.elvircrn.movieinfo.helpers.ItemClickSupport;


/**
 * Skupina(za sada samo jedna) metoda koji sluze za pojednostavljenje inicijalizacije RecyclerView-a
 */
public class RecyclerViewHelpers {
    /**
     * Metoda za inicijalizaciju RecyclerView-a. Layout koji se postavlja je LinearLayout, no to
     * se moze lagano izmjeniti za potrebe nadolazecih spirala.
     * @param recyclerView recyclerView koji se inicijalizira
     * @param <T> adapter
     * @param <U> ViewHolder adaptera koji se koristi u svrhu binding-a
     * @param listener onClick item listener za item u recycler view-u. Moze biti null za slucaj da
     *                 se uopste ne postavlja.
     */
    public static <U extends RecyclerView.ViewHolder, T extends RecyclerView.Adapter<U>>
    void initializeRecyclerView(RecyclerView recyclerView, T adapter, ItemClickSupport.OnItemClickListener listener) {
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        recyclerView.getAdapter().notifyDataSetChanged();

        if (listener != null) {
            ItemClickSupport.addTo(recyclerView).setOnItemClickListener(listener);
        }
    }
}
