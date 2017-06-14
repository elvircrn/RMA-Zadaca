package ba.unsa.etf.rma.elvircrn.movieinfo.activities;


import android.Manifest;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.facebook.stetho.Stetho;

import java.util.Locale;

import ba.unsa.etf.rma.elvircrn.movieinfo.DataProvider;
import ba.unsa.etf.rma.elvircrn.movieinfo.R;
import ba.unsa.etf.rma.elvircrn.movieinfo.dal.AppDatabase;
import ba.unsa.etf.rma.elvircrn.movieinfo.dal.DatabaseFactory;
import ba.unsa.etf.rma.elvircrn.movieinfo.fragments.ActorListFragment;
import ba.unsa.etf.rma.elvircrn.movieinfo.fragments.BiographyFragment;
import ba.unsa.etf.rma.elvircrn.movieinfo.fragments.ButtonsFragment;
import ba.unsa.etf.rma.elvircrn.movieinfo.fragments.DirectorListFragment;
import ba.unsa.etf.rma.elvircrn.movieinfo.fragments.GenreListFragment;
import ba.unsa.etf.rma.elvircrn.movieinfo.fragments.MovieCalendarFragment;
import ba.unsa.etf.rma.elvircrn.movieinfo.fragments.MovieListFragment;
import ba.unsa.etf.rma.elvircrn.movieinfo.models.Actor;
import ba.unsa.etf.rma.elvircrn.movieinfo.models.Movie;
import butterknife.ButterKnife;


public class MainActivity extends AppCompatActivity implements ButtonsFragment.OnFragmentInteractionListener,
        ActorListFragment.OnFragmentInteractionListener,
        MovieListFragment.OnFragmentInteractionListener {

    /**
     * NARROW - width < 500dp
     * WIDE   - otherwise
     */
    public enum LayoutMode {
        NARROW,
        WIDE
    }

    public LayoutMode getCurrentLayout() {
        return currentLayout;
    }

    public void detectLayoutMode() {
        currentLayout = (findViewById(R.id.frame2) == null) ? LayoutMode.NARROW : LayoutMode.WIDE;
    }

    AppDatabase db;

    protected LayoutMode currentLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        Stetho.initializeWithDefaults(this);

        String currentLanguage = Locale.getDefault().getDisplayLanguage();
        DataProvider.getInstance().setLocale(currentLanguage);

        setContentView(R.layout.activity_main);
        detectLayoutMode();

        initFragments(savedInstanceState != null);
        initSearch();

        initDb();

        checkPermissions();
    }

    void checkPermissions() {
        // Assume thisActivity is the current activity
        int permissionCheck = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_CALENDAR);

    }

    protected void initDb() {
        DataProvider.initDatabase(DatabaseFactory.create(getApplicationContext()));
    }

    protected void initSearch() {
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    protected void initFragments(boolean isSaved) {
        FragmentManager fm = getSupportFragmentManager();

        fm.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        fm.executePendingTransactions();


        if (getCurrentLayout() == LayoutMode.WIDE && !DataProvider.getInstance().getActors().isEmpty()) {
            this.setSingleFragment(BiographyFragment.class,
                    R.id.frame2, BiographyFragment.getTypeFragmentTag(),
                    false, null, BiographyFragment.getActorParamTag(),
                    DataProvider.getInstance().getActors().get(0));
        }

        this.setSingleFragment(ActorListFragment.class,
                R.id.frame1, ActorListFragment.getTypeFragmentTag(),
                false, null, null, null);

    }

    public void displayMovie(Movie movie) {
        MovieCalendarFragment movieCalendarFragment = getMovieCalendarFragment();

        if (movieCalendarFragment != null && movieCalendarFragment.isVisible()) {
            movieCalendarFragment.setMovie(movie);
        } else {
            this.setSingleFragment(MovieCalendarFragment.class,
                    getCurrentLayout() == LayoutMode.NARROW ? R.id.frame1 : R.id.frame2,
                    MovieCalendarFragment.getTypeFragmentTag(),
                    getCurrentLayout() == LayoutMode.NARROW, MovieCalendarFragment.getTypeFragmentTag(), MovieCalendarFragment.getMovieParamTag(), movie);

        }
    }

    public void displayBiography(Actor actor) {
        BiographyFragment biographyFragment = getBiographyFragment();

        if (biographyFragment != null && biographyFragment.isVisible()) {
            biographyFragment.setActor(actor);
        } else {
            this.setSingleFragment(BiographyFragment.class,
                    getCurrentLayout() == LayoutMode.NARROW ? R.id.frame1 : R.id.frame2,
                    BiographyFragment.getTypeFragmentTag(),
                    getCurrentLayout() == LayoutMode.NARROW, BiographyFragment.getTypeFragmentTag(), BiographyFragment.getActorParamTag(), actor);

        }
    }

    @SuppressWarnings({"unchecked", "TryWithIdenticalCatches"})
    private <TFragment extends Fragment> void setSingleFragment(Class<TFragment> FragmentType,
                                                                @IdRes int frame,
                                                                String fragmentTag,
                                                                boolean addToBackStack,
                                                                @Nullable String backStackTag,
                                                                @Nullable String parcelTag,
                                                                @Nullable Parcelable parcel) {
        FragmentManager fm = getSupportFragmentManager();
        TFragment fragment = (TFragment) fm.findFragmentByTag(fragmentTag);

        if (fragment == null) {
            try {
                fragment = FragmentType.newInstance();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        } else {
            if (!fragment.isVisible()) {
                fm.beginTransaction().remove(fragment).commit();
                fm.executePendingTransactions();
            }
        }

        if (parcelTag != null && parcel != null) {
            Bundle args = new Bundle();
            args.putParcelable(parcelTag, parcel);
            if (fragment.getArguments() == null) {
                fragment.setArguments(args);
            } else {
                fragment.getArguments().putAll(args);
            }
        }

        if (addToBackStack)
            fm.beginTransaction().replace(frame, fragment, fragmentTag)
                    .addToBackStack(backStackTag)
                    .commit();
        else {
            fm.beginTransaction().replace(frame, fragment, fragmentTag)
                    .commit();
        }
    }

    public BiographyFragment getBiographyFragment() {
        Fragment fragment = getSupportFragmentManager()
                .findFragmentByTag(BiographyFragment.getTypeFragmentTag());
        if (fragment != null)
            return (BiographyFragment) fragment;
        else
            return null;
    }
    public MovieCalendarFragment getMovieCalendarFragment() {
        Fragment fragment = getSupportFragmentManager()
                .findFragmentByTag(MovieCalendarFragment.getTypeFragmentTag());
        if (fragment != null)
            return (MovieCalendarFragment) fragment;
        else
            return null;
    }

    @Override
    public void onFragmentInteraction(Actor actor) {
        displayBiography(actor);
    }

    @Override
    public void onFragmentInteraction(Movie movie) {
        displayMovie(movie);
    }

    @Override
    public void onFragmentInteraction(View v) {
        FragmentManager fm = getSupportFragmentManager();
        fm.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        fm.executePendingTransactions();
        if (getCurrentLayout() == LayoutMode.WIDE) {
            if (v.getId() == R.id.actorsButtonWide) {
                setSingleFragment(ActorListFragment.class,
                        R.id.frame1,
                        ActorListFragment.getTypeFragmentTag(),
                        false, ActorListFragment.getTypeFragmentTag(), null, null);

                if (!DataProvider.getInstance().getActors().isEmpty())
                    setSingleFragment(BiographyFragment.class,
                            R.id.frame2,
                            BiographyFragment.getTypeFragmentTag(),
                            false, BiographyFragment.getTypeFragmentTag(), BiographyFragment.getActorParamTag(), DataProvider.getInstance().getActors().get(0));
            } else if (v.getId() == R.id.othersButton) {
                setSingleFragment(DirectorListFragment.class,
                        R.id.frame1,
                        DirectorListFragment.getTypeFragmentTag(),
                        false, DirectorListFragment.getTypeFragmentTag(), null, null);
                setSingleFragment(GenreListFragment.class,
                        R.id.frame2,
                        GenreListFragment.getTypeFragmentTag(),
                        false, GenreListFragment.getTypeFragmentTag(), null, null);
            } else if (v.getId() == R.id.moviesButtonWide) {
                setSingleFragment(MovieListFragment.class,
                        R.id.frame1,
                        MovieListFragment.getTypeFragmentTag(),
                        false, MovieListFragment.getTypeFragmentTag(), null, null);
                if (!DataProvider.getInstance().getMovies().isEmpty())
                    setSingleFragment(MovieCalendarFragment.class,
                        R.id.frame2,
                        MovieCalendarFragment.getTypeFragmentTag(),
                        false, MovieCalendarFragment.getTypeFragmentTag(), MovieCalendarFragment.getMovieParamTag(), DataProvider.getInstance().getMovies().get(0));
            }
        } else if (getCurrentLayout() == LayoutMode.NARROW) {
            if (v.getId() == R.id.actorsButton) {
                setSingleFragment(ActorListFragment.class,
                        R.id.frame1,
                        ActorListFragment.getTypeFragmentTag(),
                        false, ActorListFragment.getTypeFragmentTag(), null, null);
            } else if (v.getId() == R.id.genresButton) {
                setSingleFragment(GenreListFragment.class,
                        R.id.frame1,
                        GenreListFragment.getTypeFragmentTag(),
                        false, GenreListFragment.getTypeFragmentTag(), null, null);
            } else if (v.getId() == R.id.directorsButton) {
                setSingleFragment(DirectorListFragment.class,
                        R.id.frame1,
                        DirectorListFragment.getTypeFragmentTag(),
                        false, DirectorListFragment.getTypeFragmentTag(), null, null);
            } else if (v.getId() == R.id.moviesButton) {
                setSingleFragment(MovieListFragment.class,
                        R.id.frame1,
                        MovieListFragment.getTypeFragmentTag(),
                        false, MovieListFragment.getTypeFragmentTag(), null, null);
            }
        }
    }
}
