package ba.unsa.etf.rma.elvircrn.movieinfo.helpers;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

/**
 * Pomocne metode za koristenje RecyclerViewera
 */
public class RecyclerViewHelpers {
    public static <U extends RecyclerView.ViewHolder, T extends RecyclerView.Adapter<U>>
    void initializeRecyclerView(RecyclerView recyclerView, T adapter) {
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        recyclerView.getAdapter().notifyDataSetChanged();
    }
}
