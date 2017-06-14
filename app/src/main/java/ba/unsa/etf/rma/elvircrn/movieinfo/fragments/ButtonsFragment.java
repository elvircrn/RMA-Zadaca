package ba.unsa.etf.rma.elvircrn.movieinfo.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import ba.unsa.etf.rma.elvircrn.movieinfo.R;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Optional;
import butterknife.Unbinder;

public class ButtonsFragment extends Fragment {
    private OnFragmentInteractionListener mListener;
    private Unbinder unbinder;

    @Optional
    @OnClick({R.id.actorsButton, R.id.genresButton, R.id.directorsButton,
            R.id.moviesButton, R.id.othersButton, R.id.actorsButtonWide,
            R.id.moviesButtonWide})
    public void onClick(View v) {
        if (mListener != null)
            mListener.onFragmentInteraction(v);
    }

    public ButtonsFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_buttons, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(View v);
    }
}
