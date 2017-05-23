package ba.unsa.etf.rma.elvircrn.movieinfo.fragments;

import android.icu.text.Replaceable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

import ba.unsa.etf.rma.elvircrn.movieinfo.DataProvider;
import ba.unsa.etf.rma.elvircrn.movieinfo.R;
import ba.unsa.etf.rma.elvircrn.movieinfo.adapters.ActorAdapter;
import ba.unsa.etf.rma.elvircrn.movieinfo.helpers.ItemClickSupport;
import ba.unsa.etf.rma.elvircrn.movieinfo.helpers.RecyclerViewHelpers;
import ba.unsa.etf.rma.elvircrn.movieinfo.interfaces.ITaggable;
import ba.unsa.etf.rma.elvircrn.movieinfo.managers.PeopleManager;
import ba.unsa.etf.rma.elvircrn.movieinfo.managers.SearchManager;
import ba.unsa.etf.rma.elvircrn.movieinfo.mappers.DirectorMapper;
import ba.unsa.etf.rma.elvircrn.movieinfo.mappers.PersonMapper;
import ba.unsa.etf.rma.elvircrn.movieinfo.models.Actor;
import ba.unsa.etf.rma.elvircrn.movieinfo.models.Director;
import ba.unsa.etf.rma.elvircrn.movieinfo.services.dto.ActorSearchResponseDTO;
import ba.unsa.etf.rma.elvircrn.movieinfo.services.dto.CrewItemDTO;
import ba.unsa.etf.rma.elvircrn.movieinfo.services.dto.MovieCreditsDTO;
import ba.unsa.etf.rma.elvircrn.movieinfo.services.dto.PersonDTO;
import ba.unsa.etf.rma.elvircrn.movieinfo.view.RxSearch;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action2;
import rx.functions.Func0;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.functions.FuncN;
import rx.observables.GroupedObservable;
import rx.schedulers.Schedulers;
import rx.subjects.BehaviorSubject;
import rx.subjects.ReplaySubject;


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
                .filter(new Func1<String, Boolean>() {
                    @Override
                    public Boolean call(String s) {
                        return s.length() > 2;
                    }
                })
                // flatMap ne garantuje redoslijed objekata sto je vazno u ovom slucaju
                // obzirom da korisnik zeli da uvijek vidi rezultat pretrage kao funkcija od
                // unesenog teksta. concatMap rjesava ovaj problem jer cuva redoslijed.
                .concatMap(new Func1<String, Observable<ActorSearchResponseDTO>>() {
                    @Override
                    public Observable<ActorSearchResponseDTO> call(String name) {
                        return SearchManager.getInstance().searchActorByName(name);
                    }
                })
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .concatMap(new Func1<ActorSearchResponseDTO, Observable<MovieCreditsDTO>>() {
                    @Override
                    public Observable<MovieCreditsDTO> call(ActorSearchResponseDTO actorSearchResponseDTO) {
                        final ReplaySubject<MovieCreditsDTO> replaySubject = ReplaySubject.create();

                        for (PersonDTO personDTO : actorSearchResponseDTO.getActors()) {
                            PeopleManager.getInstance().getMovieCredits(personDTO.getId())
                                .subscribe(new Subscriber<MovieCreditsDTO>() {
                                    @Override
                                    public void onCompleted() {
                                        replaySubject.onCompleted();
                                    }

                                    @Override
                                    public void onError(Throwable e) {
                                        e.printStackTrace();
                                    }

                                    @Override
                                    public void onNext(MovieCreditsDTO movieCreditsDTO) {
                                        replaySubject.onNext(movieCreditsDTO);
                                    }
                                });
                        }

                        return replaySubject;
                    }
                })
                .collect(new Func0<ArrayList<MovieCreditsDTO>>() {
                    @Override
                    public ArrayList<MovieCreditsDTO> call() {
                        return new ArrayList<>();
                    }
                }, new Action2<ArrayList<MovieCreditsDTO>, MovieCreditsDTO>() {
                    @Override
                    public void call(ArrayList<MovieCreditsDTO> movieCreditsDTOs, MovieCreditsDTO movieCreditsDTO) {
                        movieCreditsDTOs.add(movieCreditsDTO);
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
