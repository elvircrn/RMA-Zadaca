/*
    TODO: Pass parcelable arrays
    TODO: Fix fragment replacing
    TODO: Translate
 */

package ba.unsa.etf.rma.elvircrn.movieinfo.activities;

import android.os.Parcelable;
import android.provider.ContactsContract;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;


import ba.unsa.etf.rma.elvircrn.movieinfo.DataProvider;
import ba.unsa.etf.rma.elvircrn.movieinfo.R;
import ba.unsa.etf.rma.elvircrn.movieinfo.fragments.ActorListFragment;
import ba.unsa.etf.rma.elvircrn.movieinfo.fragments.BiographyFragment;
import ba.unsa.etf.rma.elvircrn.movieinfo.fragments.DirectorListFragment;
import ba.unsa.etf.rma.elvircrn.movieinfo.fragments.GenreListFragment;
import ba.unsa.etf.rma.elvircrn.movieinfo.listeners.IFragmentChangeListener;
import ba.unsa.etf.rma.elvircrn.movieinfo.models.Actor;

public class MainActivity extends AppCompatActivity {
    /**
     * NARROW - width < 500dp
     * WIDE   - otherwise
     */
    enum LayoutMode { NARROW, WIDE }

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
        setContentView(R.layout.activity_main);

        detectLayoutMode();


        DataProvider.getInstance().seed();
        setButtonOnClickListeners();
        initFragments();
    }

    @Override
    protected void onResume() {
        super.onResume();
        DataProvider.getInstance().seed();
    }

    protected void setButtonOnClickListeners() {
        if (getCurrentLayout() == LayoutMode.NARROW)
            setButtonOnClickListenersNarrow();
        else
            setButtonOnClickListenersWide();
    }

    protected void setButtonOnClickListenersWide() {
        (findViewById(R.id.actorsButton)).setOnClickListener(new View.OnClickListener() {
            @SuppressWarnings("TryWithIdenticalCatches")
            @Override
            public void onClick(View v) {
                if (v.getContext() instanceof MainActivity) {
                    ((MainActivity) v.getContext()).setFragment(ActorListFragment.class,
                            R.id.frame1,
                            ActorListFragment.getTypeFragmentTag(),
                            true, null, null);
                    ((MainActivity) v.getContext()).setFragment(BiographyFragment.class,
                            R.id.frame2,
                            BiographyFragment.getTypeFragmentTag(),
                            true, BiographyFragment.getActorParamTag(), DataProvider.getInstance().getActors().get(0));

                }
            }
        });

        (findViewById(R.id.othersButton)).setOnClickListener(new View.OnClickListener() {
            @SuppressWarnings("TryWithIdenticalCatches")
            @Override
            public void onClick(View v) {
                if (v.getContext() instanceof MainActivity) {
                    ((MainActivity) v.getContext()).setFragment(DirectorListFragment.class,
                            R.id.frame1,
                            DirectorListFragment.getTypeFragmentTag(),
                            true, null, null);
                    ((MainActivity) v.getContext()).setFragment(GenreListFragment.class,
                            R.id.frame2,
                            GenreListFragment.getTypeFragmentTag(),
                            true, null, null);
                }
            }
        });
    }

    protected void setButtonOnClickListenersNarrow() {
        (findViewById(R.id.directorsButton)).setOnClickListener(new View.OnClickListener() {
            @SuppressWarnings("TryWithIdenticalCatches")
            @Override
            public void onClick(View v) {
                if (v.getContext() instanceof MainActivity) {
                    ((MainActivity) v.getContext()).setFragment(DirectorListFragment.class,
                            R.id.frame1,
                            DirectorListFragment.getTypeFragmentTag(),
                            true, null, null);
                }
            }
        });


        (findViewById(R.id.genresButton)).setOnClickListener(new View.OnClickListener() {
            @SuppressWarnings("TryWithIdenticalCatches")
            @Override
            public void onClick(View v) {
                if (v.getContext() instanceof MainActivity) {
                    ((MainActivity) v.getContext()).setFragment(GenreListFragment.class,
                            R.id.frame1,
                            GenreListFragment.getTypeFragmentTag(),
                            true, null, null);
                }
            }
        });

        (findViewById(R.id.actorsButton)).setOnClickListener(new View.OnClickListener() {
            @SuppressWarnings("TryWithIdenticalCatches")
            @Override
            public void onClick(View v) {
                if (v.getContext() instanceof MainActivity) {
                    ((MainActivity) v.getContext()).setFragment(ActorListFragment.class,
                            R.id.frame1,
                            ActorListFragment.getTypeFragmentTag(),
                            true, null, null);
                }
            }
        });
    }

    protected void initFragments()  {
        this.setFragment(ActorListFragment.class,
                R.id.frame1, ActorListFragment.getTypeFragmentTag(),
                false, null, null);

        if (getCurrentLayout() == LayoutMode.WIDE) {
            this.setFragment(BiographyFragment.class,
                    R.id.frame2, BiographyFragment.getTypeFragmentTag(),
                    false, BiographyFragment.getActorParamTag(),
                    DataProvider.getInstance().getActors().get(0));
        }
    }

    public void displayBiography(Actor actor) {
        BiographyFragment biographyFragment = getBiographyFragment();

        if (biographyFragment != null) {
            biographyFragment.setActor(actor);
        } else {
            this.setFragment(BiographyFragment.class,
                    getCurrentLayout() == LayoutMode.NARROW ? R.id.frame1 : R.id.frame2,
                    BiographyFragment.getTypeFragmentTag(),
                    false, BiographyFragment.getActorParamTag(), actor);
        }
    }

    /**
     * Genericka metoda koja pojednostavlja proces postavljanja fragmenata.
     */
    @SuppressWarnings({"unchecked", "TryWithIdenticalCatches"})
    private <TFragment extends Fragment> void setFragment(Class<TFragment> FragmentType,
                                                          @IdRes int frame, String fragmentTag,
                                                          boolean addToBackStack,
                                                          @Nullable String parcelTag,
                                                          @Nullable Parcelable parcel) {
        FragmentManager fm = getSupportFragmentManager();
        TFragment fragment = (TFragment) fm.findFragmentByTag(fragmentTag);
        if (fragment == null) {
            try {
                fragment = FragmentType.newInstance();
                if (parcelTag != null && parcel != null) {
                    Bundle args = new Bundle();
                    args.putParcelable(parcelTag, parcel);
                    fragment.setArguments(args);
                }
                if (addToBackStack)
                    fm.beginTransaction().replace(frame, fragment, fragmentTag)
                            .addToBackStack(fragmentTag).commit();
                else
                    fm.beginTransaction().replace(frame, fragment, fragmentTag).commit();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        } /*else {
            fm.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }*/
    }

    public BiographyFragment getBiographyFragment() {
        Fragment biographyFragment = getSupportFragmentManager()
                .findFragmentByTag(BiographyFragment.getTypeFragmentTag());
        if (biographyFragment != null)
            return (BiographyFragment)biographyFragment;
        else
            return null;
    }
}
