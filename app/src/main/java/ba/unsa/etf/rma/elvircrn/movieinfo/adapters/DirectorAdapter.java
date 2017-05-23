package ba.unsa.etf.rma.elvircrn.movieinfo.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;

import ba.unsa.etf.rma.elvircrn.movieinfo.databinding.DirectorListItemBinding;
import ba.unsa.etf.rma.elvircrn.movieinfo.models.Director;

public class DirectorAdapter extends RecyclerView.Adapter<DirectorAdapter.ViewHolder> {
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final DirectorListItemBinding binding;
        public ViewHolder(DirectorListItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Director director) {
            binding.setDirector(director);
        }
    }

    private ArrayList<Director> directors;
    public DirectorAdapter(ArrayList<Director> directors) {
        this.directors = directors;
    }

    @Override
    public DirectorAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater;
        layoutInflater = LayoutInflater.from(parent.getContext());
        DirectorListItemBinding itemBinding =
                DirectorListItemBinding.inflate(layoutInflater, parent, false);
        return new DirectorAdapter.ViewHolder(itemBinding);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Director director = directors.get(position);
        holder.bind(director);
    }

    @Override
    public int getItemCount() {
        return directors.size();
    }
}
