package ba.unsa.etf.rma.elvircrn.movieinfo.activities;


import android.os.Parcelable;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;


import ba.unsa.etf.rma.elvircrn.movieinfo.DataProvider;
import ba.unsa.etf.rma.elvircrn.movieinfo.R;
import ba.unsa.etf.rma.elvircrn.movieinfo.fragments.ActorListFragment;
import ba.unsa.etf.rma.elvircrn.movieinfo.fragments.BiographyFragment;
import ba.unsa.etf.rma.elvircrn.movieinfo.fragments.ButtonsFragment;
import ba.unsa.etf.rma.elvircrn.movieinfo.fragments.DirectorListFragment;
import ba.unsa.etf.rma.elvircrn.movieinfo.fragments.GenreListFragment;
import ba.unsa.etf.rma.elvircrn.movieinfo.models.Actor;

public class MainActivity extends AppCompatActivity implements ButtonsFragment.OnFragmentInteractionListener, ActorListFragment.OnFragmentInteractionListener {
    /**
     * NARROW - width < 500dp
     * WIDE   - otherwise
     */
    public enum LayoutMode {
        NARROW,
        WIDE
    }


    private Actor selectedActor;

    public LayoutMode getCurrentLayout() {
        return currentLayout;
    }

    public void detectLayoutMode() {
        currentLayout = (findViewById(R.id.frame2) == null) ? LayoutMode.NARROW : LayoutMode.WIDE;
    }

    protected LayoutMode currentLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState == null) {
            DataProvider.getInstance().seed();
        }

        setContentView(R.layout.activity_main);
        detectLayoutMode();

        initFragments(savedInstanceState != null);
    }

    @Override
    protected void onResume() {
        super.onResume();
        DataProvider.getInstance().seed();
    }

    protected void initFragments(boolean isSaved) {
        FragmentManager fm = getSupportFragmentManager();
        if (isSaved) {
            fm.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            fm.executePendingTransactions();
        }


        if (getCurrentLayout() == LayoutMode.WIDE) {
            this.setSingleFragment(BiographyFragment.class,
                    R.id.frame2, BiographyFragment.getTypeFragmentTag(),
                    false, null, BiographyFragment.getActorParamTag(),
                    DataProvider.getInstance().getActors().get(0));
        }

        this.setSingleFragment(ActorListFragment.class,
                R.id.frame1, ActorListFragment.getTypeFragmentTag(),
                false, null, null, null);

    }

    public void displayBiography(Actor actor) {
        this.selectedActor = actor;
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
        else
            fm.beginTransaction().replace(frame, fragment, fragmentTag)
                    .commit();
    }

    public BiographyFragment getBiographyFragment() {
        Fragment fragment = getSupportFragmentManager()
                .findFragmentByTag(BiographyFragment.getTypeFragmentTag());
        if (fragment != null)
            return (BiographyFragment) fragment;
        else
            return null;
    }

    @Override
    public void onFragmentInteraction(Actor actor) {
        displayBiography(actor);
    }

    @Override
    public void onFragmentInteraction(View v) {
        if (getCurrentLayout() == LayoutMode.WIDE) {
            if (v.getId() == R.id.actorsButton) {
                setSingleFragment(ActorListFragment.class,
                        R.id.frame1,
                        ActorListFragment.getTypeFragmentTag(),
                        false, ActorListFragment.getTypeFragmentTag(), null, null);
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
            }
        }
    }
}
