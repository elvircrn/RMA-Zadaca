package ba.unsa.etf.rma.elvircrn.movieinfo.fragments;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ba.unsa.etf.rma.elvircrn.movieinfo.DataProvider;
import ba.unsa.etf.rma.elvircrn.movieinfo.R;
import ba.unsa.etf.rma.elvircrn.movieinfo.adapters.GenreAdapter;
import ba.unsa.etf.rma.elvircrn.movieinfo.activities.MainActivity;
import ba.unsa.etf.rma.elvircrn.movieinfo.helpers.RecyclerViewHelpers;
import ba.unsa.etf.rma.elvircrn.movieinfo.interfaces.ITaggable;

public class GenreListFragment extends Fragment implements ITaggable {
    RecyclerView recyclerView;

    private static final String FRAGMENT_TAG = "genreListFragment";

    public GenreListFragment() { }

    public static String getTypeFragmentTag() { return FRAGMENT_TAG; }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.genre_list_fragment, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        recyclerView = (RecyclerView)getView().findViewById(R.id.genresList);
        populateGenres();
    }

    @Override
    public void onResume() {
        super.onResume();

        DataProvider.getInstance().seed();
        recyclerView.getAdapter().notifyDataSetChanged();
    }

    protected void populateGenres() {
        RecyclerViewHelpers.initializeRecyclerView(recyclerView,
                new GenreAdapter(DataProvider.getInstance().getGenres()));
    }

    @Override
    public String toString() { return "genreListFragment"; }

    @Override
    public String getFragmentTag() {
        return FRAGMENT_TAG;
    }
}
