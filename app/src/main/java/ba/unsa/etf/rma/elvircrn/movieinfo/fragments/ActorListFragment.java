package ba.unsa.etf.rma.elvircrn.movieinfo.fragments;

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
import ba.unsa.etf.rma.elvircrn.movieinfo.helpers.ItemClickSupport;
import ba.unsa.etf.rma.elvircrn.movieinfo.helpers.RecyclerViewHelpers;
import ba.unsa.etf.rma.elvircrn.movieinfo.helpers.Rx;
import ba.unsa.etf.rma.elvircrn.movieinfo.interfaces.ITaggable;
import ba.unsa.etf.rma.elvircrn.movieinfo.managers.SearchManager;
import ba.unsa.etf.rma.elvircrn.movieinfo.mappers.PersonMapper;
import ba.unsa.etf.rma.elvircrn.movieinfo.models.Actor;
import ba.unsa.etf.rma.elvircrn.movieinfo.services.dto.ActorSearchResponseDTO;
import ba.unsa.etf.rma.elvircrn.movieinfo.services.dto.MovieCreditsDTO;
import ba.unsa.etf.rma.elvircrn.movieinfo.view.RxSearch;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;


public class ActorListFragment extends Fragment implements ITaggable {
    private static final String DIRECTOR_NAME_QUERY = "director:";
    private static final String ACTORS_NAME_QUERY   = "actor:";

    RecyclerView recyclerView;
    ActorAdapter actorAdapter;
    ItemClickSupport.OnItemClickListener mListener;
    SearchView searchView;
    Observable<ActorSearchResponseDTO> searchStream;
    Observable<List<MovieCreditsDTO>> creditsStream;

    CompositeDisposable subscriberHolder = new CompositeDisposable();


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
    public void onSaveInstanceState(Bundle savedInstanceState) {
        if (searchView.getQuery() != null && !searchView.getQuery().toString().isEmpty())
            savedInstanceState.putString("Search", searchView.getQuery().toString());
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
        if (savedInstanceState != null && savedInstanceState.containsKey("Search") &&
                savedInstanceState.get("Search") != null) {
            initSearchView(view, savedInstanceState.get("Search").toString());
        } else {
            initSearchView(view, null);
        }
        return view;
    }

    public void onDestroy() {
        super.onDestroy();
        if (subscriberHolder != null)
            subscriberHolder.dispose();
    }

    protected void initSearchView(View view, String initText) {
        searchView = (SearchView) view.findViewById(R.id.searchView);

        subscriberHolder.add(
                RxSearch.fromSearchView(searchView, initText)
                        .debounce(300, TimeUnit.MILLISECONDS)
                        .filter(new Predicate<String>() {
                            @Override
                            public boolean test(@NonNull String s) throws Exception {
                                return s.length() > 2;
                            }
                        })
                        .switchMap(new Function<String, ObservableSource<ArrayList<Actor>>>() {
                            @Override
                            public ObservableSource<ArrayList<Actor>> apply(@NonNull String s) throws Exception {
                                if (s.startsWith(ACTORS_NAME_QUERY)) {
                                    String query = s.substring(ACTORS_NAME_QUERY.length());
                                    return DataProvider.getInstance().getDb().actorDAO().findByName(query).toObservable()
                                            .observeOn(Schedulers.newThread())
                                            .map(new Function<List<Actor>, ArrayList<Actor>>() {
                                                @Override
                                                public ArrayList<Actor> apply(@NonNull List<Actor> actors) throws Exception {
                                                    return new ArrayList<>(actors);
                                                }
                                            });
                                } else if (s.startsWith(DIRECTOR_NAME_QUERY)) {
                                    return DataProvider.getInstance().getDb().actorDAO().findByName(s.substring(DIRECTOR_NAME_QUERY.length() - 1)).toObservable()
                                            .observeOn(Schedulers.newThread())
                                            .map(new Function<List<Actor>, ArrayList<Actor>>() {
                                                @Override
                                                public ArrayList<Actor> apply(@NonNull List<Actor> actors) throws Exception {
                                                    return new ArrayList<>(actors);
                                                }
                                            });
                                } else {
                                    return SearchManager.getInstance().searchActorByName(s).doOnError(new Consumer<Throwable>() {
                                        @Override
                                        public void accept(@NonNull Throwable throwable) throws Exception {
                                            throwable.printStackTrace();
                                        }
                                    }).toObservable().retry()
                                            .map(new Function<ActorSearchResponseDTO, ArrayList<Actor>>() {
                                                @Override
                                                public ArrayList<Actor> apply(@NonNull ActorSearchResponseDTO actorSearchResponseDTO) throws Exception {
                                                    return PersonMapper.getActorModels(actorSearchResponseDTO.getActors());
                                                }
                                            });
                                }
                            }
                        })
                        .compose(Rx.<ArrayList<Actor>>applySchedulers())
                        .subscribe(new Consumer<ArrayList<Actor>>() {
                            @Override
                            public void accept(@NonNull ArrayList<Actor> actors) throws Exception {
                                actorAdapter.setActors(actors);
                                actorAdapter.notifyDataSetChanged();

                                DataProvider.getInstance().setActors(actors);
                            }
                        })
        );
    }

    // Subscribers have to be mindful of Android's app lifecycle.
    @Override
    public void onPause() {
        super.onPause();
        if (subscriberHolder != null)
            subscriberHolder.dispose();
    }

    @Override
    public String toString() {
        return FRAGMENT_TAG;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

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
