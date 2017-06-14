package ba.unsa.etf.rma.elvircrn.movieinfo.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;

import ba.unsa.etf.rma.elvircrn.movieinfo.databinding.MovieListItemBinding;
import ba.unsa.etf.rma.elvircrn.movieinfo.models.Movie;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final MovieListItemBinding binding;
        public ViewHolder(MovieListItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Movie movie) {
            binding.setMovie(movie);
        }
    }

    private ArrayList<Movie> movies;
    public MovieAdapter(ArrayList<Movie> movies) {
        this.movies = movies;
        setHasStableIds(true);
    }

    public void setMovies(ArrayList<Movie> movies) {
        this.movies = movies;
    }

    @Override
    public MovieAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater;
        layoutInflater = LayoutInflater.from(parent.getContext());
        MovieListItemBinding itemBinding =
                MovieListItemBinding.inflate(layoutInflater, parent, false);
        return new MovieAdapter.ViewHolder(itemBinding);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Movie movie = movies.get(position);
        holder.bind(movie);
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    @Override
    public long getItemId(int position) {
        return movies.get(position).getId();
    }
}
