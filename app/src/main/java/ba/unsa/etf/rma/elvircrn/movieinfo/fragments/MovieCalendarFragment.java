package ba.unsa.etf.rma.elvircrn.movieinfo.fragments;

import android.Manifest;
import android.content.ContentResolver;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;

import ba.unsa.etf.rma.elvircrn.movieinfo.R;
import ba.unsa.etf.rma.elvircrn.movieinfo.databinding.FragmentMovieCalendarBinding;
import ba.unsa.etf.rma.elvircrn.movieinfo.models.Movie;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

// these imports are used in the following code

public class MovieCalendarFragment extends Fragment {
    private final static String MOVIE_PARAM_TAG = "Movie";
    private final static String FRAGMENT_TAG = "movieCalendarTag";

    public static String getTypeFragmentTag() {
        return FRAGMENT_TAG;
    }

    public static String getMovieParamTag() {
        return MOVIE_PARAM_TAG;
    }

    @Override
    public String toString() {
        return FRAGMENT_TAG;
    }

    FragmentMovieCalendarBinding binding;

    Movie movie = new Movie();

    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    public MovieCalendarFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments().containsKey(getMovieParamTag())) {
            movie = (Movie) getArguments().get(getMovieParamTag());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_movie_calendar, container, false);
        ButterKnife.bind(this, binding.getRoot());
        binding.setMovie(movie);
        return binding.getRoot();
    }

    @BindView(R.id.datePicker)
    DatePicker datePicker;


    @OnClick(R.id.saveButton)
    private void saveDate(View view) {
    }
}
