package ba.unsa.etf.rma.elvircrn.movieinfo.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ba.unsa.etf.rma.elvircrn.movieinfo.DataProvider;
import ba.unsa.etf.rma.elvircrn.movieinfo.R;
import ba.unsa.etf.rma.elvircrn.movieinfo.adapters.DirectorAdapter;
import ba.unsa.etf.rma.elvircrn.movieinfo.helpers.RecyclerViewHelpers;
import ba.unsa.etf.rma.elvircrn.movieinfo.interfaces.ITaggable;

public class DirectorListFragment extends Fragment implements ITaggable {
    RecyclerView recyclerView;
    DirectorAdapter directorAdapter;

    private static final String FRAGMENT_TAG = "directorListFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.director_list_fragment, container, false);
    }

    public static String getTypeFragmentTag() { return FRAGMENT_TAG; }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        recyclerView = (RecyclerView)getView().findViewById(R.id.directorsList);
        populateDirectors();
    }


    protected void populateDirectors() {
        directorAdapter = new DirectorAdapter(DataProvider.getInstance().getDirectors());
        RecyclerViewHelpers.initializeRecyclerView(recyclerView, directorAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();

        // DataProvider.getInstance().seed();
        recyclerView.getAdapter().notifyDataSetChanged();
    }

    @Override
    public String toString() { return FRAGMENT_TAG; }

    @Override
    public String getFragmentTag() {
        return FRAGMENT_TAG;
    }
}
