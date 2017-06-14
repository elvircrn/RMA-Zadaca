package ba.unsa.etf.rma.elvircrn.movieinfo.fragments;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;

import java.util.Calendar;

import ba.unsa.etf.rma.elvircrn.movieinfo.R;
import ba.unsa.etf.rma.elvircrn.movieinfo.databinding.FragmentMovieCalendarBinding;
import ba.unsa.etf.rma.elvircrn.movieinfo.helpers.MyCalendar;
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
    @BindView(R.id.detailsEditText)
    EditText detailsText;

    void checkPermissions() {
        // Assume thisActivity is the current activity
        int permissionCheck = ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.WRITE_CALENDAR);

        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{ Manifest.permission.WRITE_CALENDAR }, 2);
        }
    }

    private MyCalendar [] getCalendar(Context c) {

        String projection[] = {"_id", "calendar_displayName"};
        Uri calendars;
        calendars = Uri.parse("content://com.android.calendar/calendars");
        MyCalendar[] mCalendars;
        ContentResolver contentResolver = c.getContentResolver();
        Cursor managedCursor = contentResolver.query(calendars, projection, null, null, null);
        mCalendars = new MyCalendar[managedCursor.getCount()];

        if (managedCursor.moveToFirst()){
            String calName;
            int calID;
            int cont= 0;
            int nameCol = managedCursor.getColumnIndex(projection[1]);
            int idCol = managedCursor.getColumnIndex(projection[0]);
            do {
                calName = managedCursor.getString(nameCol);
                calID = managedCursor.getInt(idCol);
                mCalendars[cont] = new MyCalendar(calID, calName);
                cont++;
            } while(managedCursor.moveToNext());
            managedCursor.close();
        }
        return mCalendars;

    }


    @OnClick(R.id.saveButton)
    public void saveDate(View view) {
        MyCalendar[] calendars = getCalendar(getContext());
        int defaultCalendar;

        if (calendars.length == 0) {
            ContentValues values = new ContentValues();
            values.put(CalendarContract.Calendars.CALENDAR_DISPLAY_NAME, "Prvi");
            values.put(CalendarContract.Calendars.NAME,"Prvi");
            Uri updateUri = CalendarContract.Calendars.CONTENT_URI;
            Uri uri = getContext().getContentResolver().insert(updateUri, values);
            defaultCalendar = 1;
        } else {
            defaultCalendar = calendars[0].getId();
        }

        checkPermissions();
        Calendar beginTime = Calendar.getInstance();
        beginTime.set(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth(), 7, 30);

        ContentResolver cr = getActivity().getContentResolver();
        ContentValues cv = new ContentValues();
        cv.put(CalendarContract.Events.TITLE, movie.getTitle());
        cv.put(CalendarContract.Events.DESCRIPTION, detailsText.getText().toString());
        cv.put(CalendarContract.Events.DTSTART, beginTime.getTimeInMillis());
        cv.put(CalendarContract.Events.DTEND, beginTime.getTimeInMillis() + 60 * 60 * 1000);
        cv.put(CalendarContract.Events.CALENDAR_ID, defaultCalendar);
        cv.put(CalendarContract.Events.EVENT_TIMEZONE, Calendar.getInstance().getTimeZone().getID());
        Uri uri = cr.insert(CalendarContract.Events.CONTENT_URI, cv);
    }
}
