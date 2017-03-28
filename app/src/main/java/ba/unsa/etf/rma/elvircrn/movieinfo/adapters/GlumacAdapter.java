package ba.unsa.etf.rma.elvircrn.movieinfo.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;


import java.util.ArrayList;

import ba.unsa.etf.rma.elvircrn.movieinfo.databinding.GlumacListItemBinding;
import ba.unsa.etf.rma.elvircrn.movieinfo.models.Glumac;

public class GlumacAdapter extends RecyclerView.Adapter<GlumacAdapter.ViewHolder> {
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final GlumacListItemBinding binding;
        public ViewHolder(GlumacListItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Glumac glumac) {
            binding.setGlumac(glumac);
        }
    }

    private ArrayList<Glumac> glumci;
    public GlumacAdapter(ArrayList<Glumac> glumci) {
        this.glumci = glumci;
    }

    @Override
    public GlumacAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater =
                LayoutInflater.from(parent.getContext());
        GlumacListItemBinding itemBinding =
                GlumacListItemBinding.inflate(layoutInflater, parent, false);
        return new GlumacAdapter.ViewHolder(itemBinding);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Glumac glumac = glumci.get(position);
        holder.bind(glumac);
    }

    @Override
    public int getItemCount() {
        return glumci.size();
    }
}
