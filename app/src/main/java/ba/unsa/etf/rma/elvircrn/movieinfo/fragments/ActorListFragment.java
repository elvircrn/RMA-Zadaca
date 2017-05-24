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
import io.reactivex.Single;
import io.reactivex.annotations.NonNull;
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


        Observable<ActorSearchResponseDTO> searchStream = RxSearch.fromSearchView(searchView)
                .debounce(300, TimeUnit.MILLISECONDS)
                .filter(new Predicate<String>() {
                    @Override
                    public boolean test(@NonNull String s) throws Exception {
                        return s.length() > 2;
                    }
                })
                .concatMap(new Function<String, Observable<ActorSearchResponseDTO>>() {
                    @Override
                    public Observable<ActorSearchResponseDTO> apply(@NonNull String s) throws Exception {
                        return SearchManager.getInstance().searchActorByName(s).doOnError(new Consumer<Throwable>() {
                            @Override
                            public void accept(@NonNull Throwable throwable) throws Exception {
                                throwable.printStackTrace();
                            }
                        }).toObservable().subscribeOn(Schedulers.newThread());
                    }
                })
                .retry();



        Observable<List<MovieCreditsDTO>> creditsStream = searchStream
                .doOnError(new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        throwable.printStackTrace();
                    }
                })
                .flatMap(new Function<ActorSearchResponseDTO, ObservableSource<List<MovieCreditsDTO>>>() {
                    @Override
                    public ObservableSource<List<MovieCreditsDTO>> apply(@NonNull final ActorSearchResponseDTO actorSearchResponseDTO) throws Exception {
                       return Observable.fromIterable(actorSearchResponseDTO.getActors())
                                .flatMap(new Function<PersonDTO, Observable<MovieCreditsDTO>>() {
                                    @Override
                                    public Observable<MovieCreditsDTO> apply(@NonNull PersonDTO personDTO) throws Exception {
                                        return PeopleManager.getInstance().getMovieCredits(personDTO.getId()).toObservable();
                                    }
                                }).toList().toObservable();
                    }
                });


        creditsStream.subscribe(new Consumer<List<MovieCreditsDTO>>() {
            @Override
            public void accept(@NonNull List<MovieCreditsDTO> movieCreditsDTOs) throws Exception {
                int x = 2;
            }
        });
        searchStream.subscribe(new Consumer<ActorSearchResponseDTO>() {
            @Override
            public void accept(@NonNull ActorSearchResponseDTO actorSearchResponseDTO) throws Exception {
                int x = 2;
            }
        });

        creditsStream.zipWith(searchStream, new BiFunction<List<MovieCreditsDTO>, ActorSearchResponseDTO, ActorSearchResponseDTO>() {
                    @Override
                    public ActorSearchResponseDTO apply(@NonNull List<MovieCreditsDTO> movieCreditsDTOs, @NonNull ActorSearchResponseDTO actorSearchResponseDTO) throws Exception {
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
                }).subscribe(new Consumer<ActorSearchResponseDTO>() {
                            @Override
                            public void accept(@NonNull ActorSearchResponseDTO actorSearchResponseDTO) throws Exception {
                                ArrayList<Actor> actors = PersonMapper.getActorModels(actorSearchResponseDTO.getActors());
                                actorAdapter.setActors(actors);
                                DataProvider.getInstance().setActors(actors);
                                actorAdapter.notifyDataSetChanged();
                            }
                        });


        /*
        Observable<ArrayList<MovieCreditsDTO>> creditsStream = searchStream
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .concatMap(new Func1<ActorSearchResponseDTO, Observable<MovieCreditsDTO>>() {
                    @Override
                    public Observable<MovieCreditsDTO> call(final ActorSearchResponseDTO actorSearchResponseDTO) {
                        final ReplaySubject<MovieCreditsDTO> replaySubject = ReplaySubject.create();

                        final int[] count = {0};
                        for (PersonDTO personDTO : actorSearchResponseDTO.getActors()) {
                            PeopleManager.getInstance().getMovieCredits(personDTO.getId())
                                    .subscribeOn(Schedulers.newThread())
                                    .observeOn(Schedulers.)
                                    .retry()
                                    .subscribe(new Subscriber<MovieCreditsDTO>() {
                                        @Override
                                        public void onCompleted() {
                                        }

                                        @Override
                                        public void onError(Throwable e) {
                                            e.printStackTrace();
                                        }

                                        @Override
                                        public void onNext(MovieCreditsDTO movieCreditsDTO) {
                                            replaySubject.onNext(movieCreditsDTO);
                                            count[0]++;
                                            if (count[0] == actorSearchResponseDTO.getActors().size())
                                                replaySubject.onCompleted();
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

        creditsStream.subscribe(new Subscriber<ArrayList<MovieCreditsDTO>>() {
            @Override
            public void onCompleted() {
                int y = 2;
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
            }

            @Override
            public void onNext(ArrayList<MovieCreditsDTO> movieCreditsDTOs) {
                int x = 1;
            }
        });
/*
        Observable.zip(searchStream.asObservable(), creditsStream.asObservable(), new Func2<ActorSearchResponseDTO, ArrayList<MovieCreditsDTO>, ActorSearchResponseDTO>() {
            @Override
            public ActorSearchResponseDTO call(ActorSearchResponseDTO actorSearchResponseDTO, ArrayList<MovieCreditsDTO> movieCreditsDTOs) {
                ArrayList<PersonDTO> filtered = new ArrayList<>();
                for (PersonDTO personDTO : actorSearchResponseDTO.getActors()) {
                    boolean found = false;
                    for (MovieCreditsDTO movieCreditDTO : movieCreditsDTOs) {
                        if (Objects.equals(personDTO.getId(), movieCreditDTO.getId()) &&
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
                .asObservable()
                .subscribe(new Subscriber<ActorSearchResponseDTO>() {

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(ActorSearchResponseDTO actorSearchResponseDTO) {
                        ArrayList<Actor> actors = PersonMapper.getActorModels(actorSearchResponseDTO.getActors());
                        actorAdapter.setActors(actors);
                        DataProvider.getInstance().setActors(actors);
                        actorAdapter.notifyDataSetChanged();
                    }
                });*/
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
