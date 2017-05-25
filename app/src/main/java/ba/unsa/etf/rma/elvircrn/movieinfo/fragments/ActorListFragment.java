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
import ba.unsa.etf.rma.elvircrn.movieinfo.helpers.ItemClickSupport;
import ba.unsa.etf.rma.elvircrn.movieinfo.helpers.RecyclerViewHelpers;
import ba.unsa.etf.rma.elvircrn.movieinfo.helpers.Rx;
import ba.unsa.etf.rma.elvircrn.movieinfo.interfaces.ITaggable;
import ba.unsa.etf.rma.elvircrn.movieinfo.managers.PeopleManager;
import ba.unsa.etf.rma.elvircrn.movieinfo.managers.SearchManager;
import ba.unsa.etf.rma.elvircrn.movieinfo.mappers.PersonMapper;
import ba.unsa.etf.rma.elvircrn.movieinfo.models.Actor;
import ba.unsa.etf.rma.elvircrn.movieinfo.services.dto.ActorSearchResponseDTO;
import ba.unsa.etf.rma.elvircrn.movieinfo.services.dto.MovieCreditsDTO;
import ba.unsa.etf.rma.elvircrn.movieinfo.services.dto.PersonDTO;
import ba.unsa.etf.rma.elvircrn.movieinfo.view.RxSearch;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;


public class ActorListFragment extends Fragment implements ITaggable {
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
        if (subscriberHolder != null)
            subscriberHolder.dispose();
    }

    protected void initSearchView(View view) {
        searchView = (SearchView) view.findViewById(R.id.searchView);

        searchStream = RxSearch.fromSearchView(searchView)
                .debounce(300, TimeUnit.MILLISECONDS)
                .filter(new Predicate<String>() {
                    @Override
                    public boolean test(@NonNull String s) throws Exception {
                        return s.length() > 2;
                    }
                })
                .switchMap(new Function<String, Observable<ActorSearchResponseDTO>>() {
                    @Override
                    public Observable<ActorSearchResponseDTO> apply(@NonNull String s) throws Exception {
                        return SearchManager.getInstance().searchActorByName(s).doOnError(new Consumer<Throwable>() {
                            @Override
                            public void accept(@NonNull Throwable throwable) throws Exception {
                                throwable.printStackTrace();
                            }
                        }).toObservable();
                    }
                })
                .retry();

        creditsStream = searchStream
                .compose(Rx.<ActorSearchResponseDTO>applyError())
                .switchMap(new Function<ActorSearchResponseDTO, ObservableSource<List<MovieCreditsDTO>>>() {
                    @Override
                    public ObservableSource<List<MovieCreditsDTO>> apply(@NonNull final ActorSearchResponseDTO actorSearchResponseDTO) throws Exception {
                        return Observable.fromIterable(actorSearchResponseDTO.getActors())
                                // Since the order is not relevant in this context, use flatMap
                                .flatMap(new Function<PersonDTO, Observable<MovieCreditsDTO>>() {
                                    @Override
                                    public Observable<MovieCreditsDTO> apply(@NonNull PersonDTO personDTO) throws Exception {
                                        // Subscribe on new thread for immediate requests for each actor
                                        return PeopleManager.getInstance().getMovieCredits(personDTO.getId()).toObservable().subscribeOn(Schedulers.newThread());
                                    }
                                }).retry().toList().toObservable();
                    }
                });


        subscriberHolder.add(
                creditsStream.zipWith(searchStream, new BiFunction<List<MovieCreditsDTO>, ActorSearchResponseDTO, ActorSearchResponseDTO>() {
                    @Override
                    public ActorSearchResponseDTO apply(@NonNull final List<MovieCreditsDTO> movieCreditsDTOs, @NonNull ActorSearchResponseDTO actorSearchResponseDTO) throws Exception {
                        // TODO: Convert to stream because everything is a stream
                        ArrayList<PersonDTO> filtered = new ArrayList<>();
                        for (PersonDTO personDTO : actorSearchResponseDTO.getActors()) {
                            boolean found = false;
                            for (MovieCreditsDTO movieCreditDTO : movieCreditsDTOs) {
                                if (personDTO.getId().equals(movieCreditDTO.getId()) &&
                                        movieCreditDTO.getCast() != null) {
                                    found = true;
                                    break;
                                }
                            }

                            if (found)
                                filtered.add(personDTO);
                        }
                        actorSearchResponseDTO.setActors(filtered);
                        return actorSearchResponseDTO;
                    }
                })
                        .compose(Rx.<ActorSearchResponseDTO>applySchedulers())
                        .subscribe(new Consumer<ActorSearchResponseDTO>() {
                            @Override
                            public void accept(@NonNull ActorSearchResponseDTO actorSearchResponseDTO) throws Exception {
                                ArrayList<Actor> actors = PersonMapper.getActorModels(actorSearchResponseDTO.getActors());
                                actorAdapter.setActors(actors);
                                DataProvider.getInstance().setActors(actors);
                                actorAdapter.notifyDataSetChanged();
                            }
                        })
        );

    }

    // Subscribers have to be mindful of android's app lifecycle.
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
