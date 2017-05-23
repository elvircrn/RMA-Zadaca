package ba.unsa.etf.rma.elvircrn.movieinfo.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;

import ba.unsa.etf.rma.elvircrn.movieinfo.databinding.GenreListItemBinding;
import ba.unsa.etf.rma.elvircrn.movieinfo.models.Genre;

public class GenreAdapter extends RecyclerView.Adapter<GenreAdapter.ViewHolder> {
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final GenreListItemBinding binding;
        public ViewHolder(GenreListItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Genre genre) {
            binding.setGenre(genre);
        }
    }

    private ArrayList<Genre> genres;
    public GenreAdapter(ArrayList<Genre> genres) {
        this.genres = genres;
    }

    @Override
    public GenreAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater;
        layoutInflater = LayoutInflater.from(parent.getContext());
        GenreListItemBinding itemBinding =
                GenreListItemBinding.inflate(layoutInflater, parent, false);
        return new GenreAdapter.ViewHolder(itemBinding);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Genre genre = genres.get(position);
        holder.bind(genre);
    }

    @Override
    public int getItemCount() {
        if (genres == null)
            return 0;
        return genres.size();
    }
}
