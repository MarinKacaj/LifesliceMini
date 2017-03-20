package com.marin.dev.lifeslicemini.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.marin.dev.lifeslicemini.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link VideoPlaylistFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class VideoPlaylistFragment extends Fragment {

    public VideoPlaylistFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment VideoPlaylistFragment.
     */
    public static VideoPlaylistFragment newInstance() {
        return new VideoPlaylistFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_video_playlist, container, false);
    }

    public void refresh(String videoTag) {
        Toast.makeText(getContext(), "Video tag: " + videoTag, Toast.LENGTH_LONG).show();
    }
}
