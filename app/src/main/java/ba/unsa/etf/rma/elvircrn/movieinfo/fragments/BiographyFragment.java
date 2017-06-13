package ba.unsa.etf.rma.elvircrn.movieinfo.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.Toast;

import ba.unsa.etf.rma.elvircrn.movieinfo.R;
import ba.unsa.etf.rma.elvircrn.movieinfo.dao.ActorDbService;
import ba.unsa.etf.rma.elvircrn.movieinfo.databinding.ActorBiographyFragmentBinding;
import ba.unsa.etf.rma.elvircrn.movieinfo.helpers.Rx;
import ba.unsa.etf.rma.elvircrn.movieinfo.interfaces.ITaggable;
import ba.unsa.etf.rma.elvircrn.movieinfo.managers.PeopleManager;
import ba.unsa.etf.rma.elvircrn.movieinfo.models.Actor;
import ba.unsa.etf.rma.elvircrn.movieinfo.view.RxCheckBox;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Predicate;

public class BiographyFragment extends Fragment implements ITaggable {
    private final static String ACTOR_PARAM_TAG = "PersonDTO";
    private  final static String FRAGMENT_TAG = "biographyTag";

    public static String getTypeFragmentTag() {
        return FRAGMENT_TAG;
    }

    @BindView(R.id.bookmarkedCheckBox)
    CheckBox bookmarked;

    Observable<Boolean> checkBoxStream;

    public static String getActorParamTag() {
        return ACTOR_PARAM_TAG;
    }

    private Actor actor = new Actor();
    private CompositeDisposable subscriberHolder = new CompositeDisposable();
    private ActorBiographyFragmentBinding binding;

    public BiographyFragment() { }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments().containsKey(getActorParamTag())) {
            this.actor = (Actor) getArguments().get(getActorParamTag());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.actor_biography_fragment, container, false);
        ButterKnife.bind(this, binding.getRoot());
        initCheckBox();
        hideProgress();
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        loadBiography();
    }

    @OnClick(R.id.shareButton)
    public void shareClick(View view) {
        Intent i = new Intent(Intent.ACTION_SEND);
        i.putExtra(Intent.EXTRA_TEXT, actor.getBiography());
        i.setType("text/plain");

        if (i.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivity(i);
        }
    }
    @OnClick(R.id.imdbButton)
    public void imdbClick(View view) {
        Intent i = new Intent(Intent.ACTION_VIEW);
        if (actor.getImdbLink() != null && !actor.getImdbLink().isEmpty())
            i.setData(Uri.parse(actor.getImdbLink()));
        else
            Toast.makeText(getContext(), String.valueOf(R.string.imdb_toast_error), 3)
                    .show();
        startActivity(i);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        if (actor != null)
            savedInstanceState.putParcelable(getActorParamTag(), actor);
    }

    void initCheckBox() {
        checkBoxStream = RxCheckBox.fromCheckBox(bookmarked);
        subscriberHolder.add(
                checkBoxStream.compose(Rx.<Boolean>applyDbSchedulers())
                        .subscribe(new Consumer<Boolean>() {
                            @Override
                            public void accept(@NonNull Boolean bookmarked) throws Exception {
                                if (bookmarked) {
                                    ActorDbService.addActor(BiographyFragment.this.actor);
                                } else {
                                    ActorDbService.deleteActor(BiographyFragment.this.actor);
                                }
                            }
                        })
        );
    }

    @Override
    public String toString() {
        return FRAGMENT_TAG;
    }

    @Override
    public String getFragmentTag() {
        return FRAGMENT_TAG;
    }

    public void setActor(Actor actor) {
        this.actor = actor;
        loadBiography();
    }

    ProgressDialog progress;
    private ProgressDialog getProgressBar() {
        if (progress == null) {
            progress = new ProgressDialog(getContext());
            progress.setTitle("Loading");
            progress.setMessage("Wait while loading...");
            progress.setCancelable(false); // Disable dismiss by tapping outside of the dialog
        }
        return progress;
    }

    private void showProgress() {
        getProgressBar().show();
    }

    private void hideProgress() {
        getProgressBar().dismiss();
    }

    public void loadBiography() {
        showProgress();
        bookmarked.setEnabled(false);
        subscriberHolder.add(
                ActorDbService.getFullActor(actor.getId())
                        .take(1)
                        .compose(Rx.<Actor>applySchedulers())
                        .subscribe(new Consumer<Actor>() {
                            @Override
                            public void accept(@NonNull Actor actor) throws Exception {
                                bookmarked.setChecked(actor.getId() != -1);
                            }
                        }));

        subscriberHolder.add(
                ActorDbService.getFullActor(actor.getId())
                        .mergeWith(PeopleManager.getInstance().getFullActor(actor))
                        .compose(Rx.<Actor>applySchedulers())
                        .filter(new Predicate<Actor>() {
                            @Override
                            public boolean test(@NonNull Actor actor) throws Exception {
                                return actor.getId() != -1;
                            }
                        })
                        .take(1)
                        .subscribe(new Consumer<Actor>() {
                            @Override
                            public void accept(@NonNull Actor actor) throws Exception {
                                binding.setActor(actor);
                                binding.notifyChange();
                                BiographyFragment.this.actor = actor;
                                bookmarked.setEnabled(true);
                                hideProgress();
                            }
                        })
        );
    }

    @Override
    public void onPause() {
        super.onPause();
        hideProgress();
    }

    @Override
    public void onStop() {
        super.onStop();
        subscriberHolder.dispose();
        hideProgress();
    }
}
