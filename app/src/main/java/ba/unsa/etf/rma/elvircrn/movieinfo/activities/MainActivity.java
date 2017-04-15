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

public class MainActivity extends AppCompatActivity implements ButtonsFragment.OnFragmentInteractionListener {
    /**
     * NARROW - width < 500dp
     * WIDE   - otherwise
     */
    enum LayoutMode {
        NARROW, WIDE
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
        setButtonOnClickListeners();
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
                    ((MainActivity) v.getContext()).setSingleFragment(ActorListFragment.class,
                            R.id.frame1,
                            ActorListFragment.getTypeFragmentTag(),
                            true, ActorListFragment.getTypeFragmentTag(), null, null);
                    ((MainActivity) v.getContext()).setSingleFragment(BiographyFragment.class,
                            R.id.frame2,
                            BiographyFragment.getTypeFragmentTag(),
                            true, ActorListFragment.getTypeFragmentTag(), BiographyFragment.getActorParamTag(), DataProvider.getInstance().getActors().get(0));
                }
            }
        });

        (findViewById(R.id.othersButton)).setOnClickListener(new View.OnClickListener() {
            @SuppressWarnings("TryWithIdenticalCatches")
            @Override
            public void onClick(View v) {
                if (v.getContext() instanceof MainActivity) {
                    ((MainActivity) v.getContext()).setSingleFragment(DirectorListFragment.class,
                            R.id.frame1,
                            DirectorListFragment.getTypeFragmentTag(),
                            true, DirectorListFragment.getTypeFragmentTag(), null, null);
                    ((MainActivity) v.getContext()).setSingleFragment(GenreListFragment.class,
                            R.id.frame2,
                            GenreListFragment.getTypeFragmentTag(),
                            true, DirectorListFragment.getTypeFragmentTag(), null, null);
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
                    ((MainActivity) v.getContext()).setSingleFragment(DirectorListFragment.class,
                            R.id.frame1,
                            DirectorListFragment.getTypeFragmentTag(),
                            true, DirectorListFragment.getTypeFragmentTag(), null, null);
                }
            }
        });


        (findViewById(R.id.genresButton)).setOnClickListener(new View.OnClickListener() {
            @SuppressWarnings("TryWithIdenticalCatches")
            @Override
            public void onClick(View v) {
                if (v.getContext() instanceof MainActivity) {
                    ((MainActivity) v.getContext()).setSingleFragment(GenreListFragment.class,
                            R.id.frame1,
                            GenreListFragment.getTypeFragmentTag(),
                            true, GenreListFragment.getTypeFragmentTag(), null, null);
                }
            }
        });

        (findViewById(R.id.actorsButton)).setOnClickListener(new View.OnClickListener() {
            @SuppressWarnings("TryWithIdenticalCatches")
            @Override
            public void onClick(View v) {
                if (v.getContext() instanceof MainActivity) {
                    ((MainActivity) v.getContext()).setSingleFragment(ActorListFragment.class,
                            R.id.frame1,
                            ActorListFragment.getTypeFragmentTag(),
                            true, ActorListFragment.getTypeFragmentTag(), null, null);
                }
            }
        });
    }

    protected void initFragments(boolean isSaved) {
        if (!isSaved) {
            this.setSingleFragment(ActorListFragment.class,
                    R.id.frame1, ActorListFragment.getTypeFragmentTag(),
                    false, null, null, null);
        }

        if (getCurrentLayout() == LayoutMode.WIDE) {
            this.setSingleFragment(BiographyFragment.class,
                    R.id.frame2, BiographyFragment.getTypeFragmentTag(),
                    false, null, BiographyFragment.getActorParamTag(),
                    DataProvider.getInstance().getActors().get(0));
        }
    }

    public void displayBiography(Actor actor) {
        this.selectedActor = actor;
        BiographyFragment biographyFragment = getBiographyFragment();

        if (biographyFragment != null && biographyFragment.isVisible()) {
            biographyFragment.setActor(actor);
        }
        this.setSingleFragment(BiographyFragment.class,
                getCurrentLayout() == LayoutMode.NARROW ? R.id.frame1 : R.id.frame2,
                BiographyFragment.getTypeFragmentTag(),
                true, BiographyFragment.getTypeFragmentTag(), BiographyFragment.getActorParamTag(), actor);
    }

    /**
     * Genericka metoda koja pojednostavlja proces postavljanja fragmenata.
     */
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
                if (parcelTag != null && parcel != null) {
                    Bundle args = new Bundle();
                    args.putParcelable(parcelTag, parcel);
                    fragment.setArguments(args);
                }
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
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
        Fragment biographyFragment = getSupportFragmentManager()
                .findFragmentByTag(BiographyFragment.getTypeFragmentTag());
        if (biographyFragment != null)
            return (BiographyFragment) biographyFragment;
        else
            return null;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (getCurrentLayout() == LayoutMode.WIDE)
            super.onBackPressed();
    }

    @Override
    public void onFragmentInteraction(View v) {
        if (getCurrentLayout() == LayoutMode.WIDE) {
            if (v.getId() == R.id.actorsButton) {
                setSingleFragment(ActorListFragment.class,
                        R.id.frame1,
                        ActorListFragment.getTypeFragmentTag(),
                        true, ActorListFragment.getTypeFragmentTag(), null, null);

                setSingleFragment(BiographyFragment.class,
                        R.id.frame2,
                        BiographyFragment.getTypeFragmentTag(),
                        true, ActorListFragment.getTypeFragmentTag(), BiographyFragment.getActorParamTag(), DataProvider.getInstance().getActors().get(0));

            } else if (v.getId() == R.id.othersButton) {

            }
        } else if (getCurrentLayout() == LayoutMode.NARROW) {
            if (v.getId() == R.id.actorsButton) {

            } else if (v.getId() == R.id.genresButton) {

            } else if (v.getId() == R.id.directorsButton) {

            }
        }
    }
}
