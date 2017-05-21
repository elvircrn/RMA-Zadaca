package ba.unsa.etf.rma.elvircrn.movieinfo.fragments;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import ba.unsa.etf.rma.elvircrn.movieinfo.DataProvider;
import ba.unsa.etf.rma.elvircrn.movieinfo.R;
import ba.unsa.etf.rma.elvircrn.movieinfo.adapters.ActorAdapter;
import ba.unsa.etf.rma.elvircrn.movieinfo.common.mappers.ActorMapper;
import ba.unsa.etf.rma.elvircrn.movieinfo.controllers.SearchManager;
import ba.unsa.etf.rma.elvircrn.movieinfo.helpers.ItemClickSupport;
import ba.unsa.etf.rma.elvircrn.movieinfo.helpers.RecyclerViewHelpers;
import ba.unsa.etf.rma.elvircrn.movieinfo.interfaces.ITaggable;
import ba.unsa.etf.rma.elvircrn.movieinfo.models.Actor;
import ba.unsa.etf.rma.elvircrn.movieinfo.services.dto.PersonDTO;
import ba.unsa.etf.rma.elvircrn.movieinfo.services.dto.ActorSearchResponseDTO;
import ba.unsa.etf.rma.elvircrn.movieinfo.view.RxSearch;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;


public class ActorListFragment extends Fragment implements ITaggable {
    RecyclerView recyclerView;
    ActorAdapter actorAdapter;
    ItemClickSupport.OnItemClickListener mListener;
    SearchView searchView;

    public ActorListFragment() {
    }

    private final static String FRAGMENT_TAG = "actorListFragment";

    @Override
    public String getFragmentTag() {
        return FRAGMENT_TAG;
    }

    /* TODO: Refactor when android studio 2.4 lands */
    public static String getTypeFragmentTag() {
        return FRAGMENT_TAG;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        recyclerView = (RecyclerView) getView().findViewById(R.id.actorsList);
        if (recyclerView != null) {
            populateActors();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.actor_list_fragment, container, false);
        initSearchView(view);
        return view;
    }

    public void onDestroy() {
        super.onDestroy();
    }

    protected void initSearchView(View view) {

        searchView = (SearchView) view.findViewById(R.id.searchView);

        RxSearch.fromSearchView(searchView)
                .debounce(300, TimeUnit.MILLISECONDS)
                // flatMap ne garantuje redoslijed objekata sto je vazno u ovom slucaju
                // obzirom da korisnik zeli da uvijek vidi rezultat pretrage kao funkcija od
                // unesenog teksta. concatMap rjesava ovaj problem jer cuva redoslijed.
                .concatMap(new Func1<String, Observable<ActorSearchResponseDTO>>() {
                    @Override
                    public Observable<ActorSearchResponseDTO> call(String name) {
                        return SearchManager.searchActorByName(name);
                    }
                })
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .retry()
                .subscribe(new Subscriber<ActorSearchResponseDTO>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(ActorSearchResponseDTO actorSearchResponseDTO) {
                        List<PersonDTO> personDTOs = actorSearchResponseDTO.getActors();
                        ArrayList<Actor> actors = ActorMapper.getActorModels(personDTOs);
                        actorAdapter.setActors(actors);
                        DataProvider.getInstance().setActors(actors);
                        actorAdapter.notifyDataSetChanged();
                    }
                });

    }

    @Override
    public String toString() {
        return FRAGMENT_TAG;
    }

    @Override
    public void onResume() {
        super.onResume();
    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
    }

    // TODO: Inject ActorAdapter
    protected void populateActors() {
        if (mListener == null) {
            mListener = new ItemClickSupport.OnItemClickListener() {
                @Override
                public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                    if (getActivity() instanceof OnFragmentInteractionListener) {
                        Actor actor = DataProvider.getInstance().getActors().get(position);
                        OnFragmentInteractionListener mainActivity = (OnFragmentInteractionListener) getActivity();
                        mainActivity.onFragmentInteraction(actor);
                    }
                }
            };
        }

        if (actorAdapter == null) {
            actorAdapter = new ActorAdapter(DataProvider.getInstance().getActors());
        }

        RecyclerViewHelpers.initializeRecyclerView(recyclerView,
                actorAdapter,
                mListener);

        recyclerView.getAdapter().notifyDataSetChanged();
        recyclerView.setItemViewCacheSize(50);
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        recyclerView.setHasFixedSize(true);
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Actor actor);
    }
}
