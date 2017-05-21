package ba.unsa.etf.rma.elvircrn.movieinfo.fragments;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.net.Uri;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import ba.unsa.etf.rma.elvircrn.movieinfo.databinding.ActorBiographyFancyFragmentBinding;
import ba.unsa.etf.rma.elvircrn.movieinfo.interfaces.ITaggable;
import ba.unsa.etf.rma.elvircrn.movieinfo.listeners.IFragmentChangeListener;
import ba.unsa.etf.rma.elvircrn.movieinfo.models.Actor;
import ba.unsa.etf.rma.elvircrn.movieinfo.R;
import ba.unsa.etf.rma.elvircrn.movieinfo.databinding.ActorBiographyFragmentBinding;

public class BiographyFragment extends Fragment implements ITaggable {
    Actor actor;
    private ActorBiographyFragmentBinding binding;

    private final static String ACTOR_PARAM_TAG = "PersonDTO";
    public static String getActorParamTag() { return ACTOR_PARAM_TAG; }

    public static final String FRAGMENT_TAG = "biographyTag";

    public BiographyFragment() { }

    public static String getTypeFragmentTag() { return FRAGMENT_TAG; }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (getArguments().containsKey(getActorParamTag()))
            actor = (Actor)getArguments().get(getActorParamTag());

        binding = DataBindingUtil.inflate(inflater, R.layout.actor_biography_fragment, container, false);
        View view = binding.getRoot();
        binding.setActor(actor);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        addOnButtonClickListeners();
    }

    void addOnButtonClickListeners() {
        ImageButton imdbButton = (ImageButton)getView().findViewById(R.id.imdbButton);
        imdbButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(actor.getImdbLink()));
                startActivity(i);
            }
        });

        ImageButton shareBio = (ImageButton)getView().findViewById(R.id.shareButton);
        shareBio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_SEND);
                i.putExtra(Intent.EXTRA_TEXT, actor.getBiography());
                i.setType("text/plain");

                // Provjera da li uopste postoji app koji podrzava ovu vrstu intenta. resolveActivity()
                // vraca null za slucaj da ne postoji.
                if (i.resolveActivity(getActivity().getPackageManager()) != null) {
                    startActivity(i);
                }
            }
        });
    }

    @Override
    public String toString() { return FRAGMENT_TAG; }

    @Override
    public String getFragmentTag() {
        return FRAGMENT_TAG;
    }

    public void setActor(Actor actor) {
        this.actor = actor;
        binding.setActor(actor);
    }

}
