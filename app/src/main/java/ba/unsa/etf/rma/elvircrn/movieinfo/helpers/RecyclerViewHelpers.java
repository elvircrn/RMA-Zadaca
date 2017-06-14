package ba.unsa.etf.rma.elvircrn.movieinfo.helpers;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

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

    public static <U extends RecyclerView.ViewHolder, T extends RecyclerView.Adapter<U>>
    void initializeRecyclerView(RecyclerView recyclerView, T adapter, @NotNull ItemClickSupport.OnItemClickListener listener) {
        initializeRecyclerView(recyclerView, adapter);
        ItemClickSupport.addTo(recyclerView).setOnItemClickListener(listener);
    }

}
