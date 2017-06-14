package ba.unsa.etf.rma.elvircrn.movieinfo.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.SearchView;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import ba.unsa.etf.rma.elvircrn.movieinfo.DataProvider;
import ba.unsa.etf.rma.elvircrn.movieinfo.R;
import ba.unsa.etf.rma.elvircrn.movieinfo.adapters.MovieAdapter;
import ba.unsa.etf.rma.elvircrn.movieinfo.helpers.ItemClickSupport;
import ba.unsa.etf.rma.elvircrn.movieinfo.helpers.RecyclerViewHelpers;
import ba.unsa.etf.rma.elvircrn.movieinfo.helpers.Rx;
import ba.unsa.etf.rma.elvircrn.movieinfo.managers.SearchManager;
import ba.unsa.etf.rma.elvircrn.movieinfo.mappers.MovieMapper;
import ba.unsa.etf.rma.elvircrn.movieinfo.models.Movie;
import ba.unsa.etf.rma.elvircrn.movieinfo.services.dto.MovieSearchResponseDTO;
import ba.unsa.etf.rma.elvircrn.movieinfo.view.RxSearch;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;

public class MovieListFragment extends Fragment {
    @BindView(R.id.moviesList)
    RecyclerView recyclerView;
    @BindView(R.id.movieSearchView)
    SearchView searchView;
    @BindView(R.id.movies_progress_bar)
    ProgressBar progressBar;

    ItemClickSupport.OnItemClickListener mListener;

    public static final String SEARCH_STRING_TAG = "searchTag";

    MovieAdapter movieAdapter;

    private static final String FRAGMENT_TAG = "movieListFragment";

    public static String getTypeFragmentTag() {
        return FRAGMENT_TAG;
    }


    public MovieListFragment() {
    }

    private void initMovieList() {
        if (mListener == null) {
            mListener = new ItemClickSupport.OnItemClickListener() {
                @Override
                public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                    if (getActivity() instanceof MovieListFragment.OnFragmentInteractionListener) {
                        Movie movie = DataProvider.getInstance().getMovies().get(position);
                        MovieListFragment.OnFragmentInteractionListener mainActivity = (MovieListFragment.OnFragmentInteractionListener) getActivity();
                        mainActivity.onFragmentInteraction(movie);
                    }
                }
            };
        }

        if (movieAdapter == null) {
            movieAdapter = new MovieAdapter(DataProvider.getInstance().getMovies());
        }

        RecyclerViewHelpers.initializeRecyclerView(recyclerView,
                movieAdapter,
                mListener);

        recyclerView.getAdapter().notifyDataSetChanged();
        recyclerView.setItemViewCacheSize(50);
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        recyclerView.setHasFixedSize(true);

        hideSearch();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movies, container, false);
        ButterKnife.bind(this, view);
        if (savedInstanceState != null && savedInstanceState.containsKey("Search") &&
                savedInstanceState.get("Search") != null) {
            initSearchView(savedInstanceState.get("Search").toString());
        } else {
            initSearchView(null);
        }
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initMovieList();
    }


    private void showSearch() {
        progressBar.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.INVISIBLE);
    }

    private void hideSearch() {
        progressBar.setVisibility(View.INVISIBLE);
        recyclerView.setVisibility(View.VISIBLE);
    }


    public void initSearchView(String initialSearch) {
        hideSearch();
        Observable<String> textStream =
                // subscriberHolder.add(
                RxSearch.fromSearchView(searchView, initialSearch)
                        .debounce(300, TimeUnit.MILLISECONDS)
                        .filter(new Predicate<String>() {
                            @Override
                            public boolean test(@NonNull String s) throws Exception {
                                return s.length() > 2;
                            }
                        });

        textStream.compose(Rx.<String>applySchedulers())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(@NonNull String s) throws Exception {
                        showSearch();
                    }
                });

        textStream.switchMap(new Function<String, ObservableSource<ArrayList<Movie>>>() {
            @Override
            public ObservableSource<ArrayList<Movie>> apply(@NonNull String s) throws Exception {
                return SearchManager.getInstance().searchMovieByName(s)
                        .onErrorReturn(new Function<Throwable, MovieSearchResponseDTO>() {
                            @Override
                            public MovieSearchResponseDTO apply(@NonNull Throwable throwable) throws Exception {
                                return new MovieSearchResponseDTO();
                            }
                        })
                        .toObservable()
                        .map(new Function<MovieSearchResponseDTO, ArrayList<Movie>>() {
                            @Override
                            public ArrayList<Movie> apply(@NonNull MovieSearchResponseDTO movieSearchResponseDTO) throws Exception {
                                return MovieMapper.toMoviesFromSearch(movieSearchResponseDTO);
                            }
                        });
            }
        })
        .compose(Rx.<ArrayList<Movie>>applySchedulers())
        .subscribe(new Consumer<ArrayList<Movie>>() {
            @Override
            public void accept(@NonNull ArrayList<Movie> movies) throws Exception {
                movieAdapter.setMovies(movies);
                movieAdapter.notifyDataSetChanged();
                DataProvider.getInstance().setMovies(movies);
                hideSearch();
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Movie movie);
    }

    @Override
    public String toString() {
        return FRAGMENT_TAG;
    }

}
