package ba.unsa.etf.rma.elvircrn.movieinfo;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.annotation.CallSuper;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.Locale;

import ba.unsa.etf.rma.elvircrn.movieinfo.databinding.GlumacListItemBinding;

public class GlumacAdapter extends RecyclerView.Adapter<GlumacAdapter.ViewHolder> {
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public ViewHolder(View itemView) {
            super(itemView);
        }

        public ViewDataBinding getBinding() {
            return DataBindingUtil.getBinding(itemView);
        }
    }

    private ArrayList<Glumac> glumci;
    public GlumacAdapter(ArrayList<Glumac> glumci) {
        this.glumci = glumci;
    }

    @Override
    public GlumacAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        TextView v = (TextView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.glumac_list_item, parent, false);
        RecyclerView.ViewHolder vh = new ViewHolder(v);
        return (ViewHolder) vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.getBinding().setVariable(BR.glumac, glumci.get(position));
        holder.getBinding().executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return glumci.size();
    }
}
