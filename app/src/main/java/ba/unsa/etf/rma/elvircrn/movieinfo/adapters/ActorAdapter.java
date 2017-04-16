package ba.unsa.etf.rma.elvircrn.movieinfo.adapters;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.databinding.ViewDataBinding;



import java.util.ArrayList;

import ba.unsa.etf.rma.elvircrn.movieinfo.fragments.BiographyFragment;
import ba.unsa.etf.rma.elvircrn.movieinfo.databinding.ActorListItemBinding;
import ba.unsa.etf.rma.elvircrn.movieinfo.models.Actor;

public class ActorAdapter extends RecyclerView.Adapter<ActorAdapter.ViewHolder> {
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ActorListItemBinding binding;
        public ViewHolder(ActorListItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Actor actor) {
            binding.setActor(actor);
        }
    }

    private ArrayList<Actor> actors;
    public ActorAdapter(ArrayList<Actor> actors) {
        this.actors = actors;
        setHasStableIds(true);
    }

    @Override
    public ActorAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater =
                LayoutInflater.from(parent.getContext());
        ActorListItemBinding itemBinding =
                ActorListItemBinding.inflate(layoutInflater, parent, false);
        return new ActorAdapter.ViewHolder(itemBinding);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Actor actor = actors.get(position);
        holder.bind(actor);
    }

    @Override
    public int getItemCount() {
        return actors.size();
    }

    @Override
    public long getItemId(int position) {
        return actors.get(position).getId();
    }
}
