package com.marin.dev.lifeslicemini.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.marin.dev.lifeslicemini.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnVideoTagQueryTriggeredListener} interface
 * to handle interaction events.
 * Use the {@link InputFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class InputFragment extends Fragment {

    private OnVideoTagQueryTriggeredListener videoTagProvidedListener;

    @BindView(R.id.tag_query)
    EditText videoTagInput;

    public InputFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment InputFragment.
     */
    public static InputFragment newInstance() {
        return new InputFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_input, container, false);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnVideoTagQueryTriggeredListener) {
            videoTagProvidedListener = (OnVideoTagQueryTriggeredListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnVideoTagQueryTriggeredListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        videoTagProvidedListener = null;
    }

    @OnClick(R.id.tag_query_submission_trigger)
    void onNext() {
        String videoTag = videoTagInput.getText().toString();
        videoTagProvidedListener.onVideoTagQueryTriggered(videoTag);

        InputMethodManager inputManager = (InputMethodManager)
                getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnVideoTagQueryTriggeredListener {

        void onVideoTagQueryTriggered(String videoTag);
    }
}
