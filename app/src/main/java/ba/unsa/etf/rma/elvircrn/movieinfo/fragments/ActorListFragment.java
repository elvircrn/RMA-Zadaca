package ba.unsa.etf.rma.elvircrn.movieinfo.fragments;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ba.unsa.etf.rma.elvircrn.movieinfo.DataProvider;
import ba.unsa.etf.rma.elvircrn.movieinfo.R;
import ba.unsa.etf.rma.elvircrn.movieinfo.activities.MainActivity;
import ba.unsa.etf.rma.elvircrn.movieinfo.adapters.ActorAdapter;
import ba.unsa.etf.rma.elvircrn.movieinfo.helpers.ItemClickSupport;
import ba.unsa.etf.rma.elvircrn.movieinfo.helpers.RecyclerViewHelpers;
import ba.unsa.etf.rma.elvircrn.movieinfo.interfaces.ITaggable;
import ba.unsa.etf.rma.elvircrn.movieinfo.listeners.IFragmentChangeListener;


public class ActorListFragment extends Fragment implements ITaggable {
    RecyclerView recyclerView;
    public ActorListFragment() { }

    private final static String FRAGMENT_TAG = "actorListFragment";

    @Override
    public String getFragmentTag() { return FRAGMENT_TAG; }

    /* TODO: Refactor when android studio 2.4 lands */
    public static String getTypeFragmentTag() { return FRAGMENT_TAG; }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        recyclerView = (RecyclerView)getView().findViewById(R.id.actorsList);
        populateActors();
    }

    @Override
    public String toString() { return FRAGMENT_TAG; }

    @Override
    public void onResume() {
        super.onResume();
        recyclerView.getAdapter().notifyDataSetChanged();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.actor_list_fragment, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
    }

    protected void populateActors() {
        RecyclerViewHelpers.initializeRecyclerView(recyclerView, new ActorAdapter(DataProvider.getInstance().getActors()),
                new ItemClickSupport.OnItemClickListener() {
                    @Override
                    public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                        if (getActivity() instanceof MainActivity) {
                            ((MainActivity)getActivity()).displayBiography(DataProvider.getInstance().getActors().get(position));
                        }
                    }
                }
        );
    }
}
