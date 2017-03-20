package com.marin.dev.lifeslicemini.fragment;

import android.Manifest;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.marin.dev.lifeslicemini.LifesliceMiniApp;
import com.marin.dev.lifeslicemini.R;
import com.marin.dev.lifeslicemini.domain.intermediate.UserVideoListDataResponse;
import com.marin.dev.lifeslicemini.service.UserVideoService;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link VideoPlaylistFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
@RuntimePermissions
public class VideoPlaylistFragment extends Fragment {

    private LifesliceMiniApp app;

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

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        app = ((LifesliceMiniApp) getActivity().getApplication());
    }

    @Override
    public void onDetach() {
        super.onDetach();
        app = null;
    }

    @NeedsPermission({Manifest.permission.INTERNET})
    public void refresh(String videoTag) {
        UserVideoService userVideoService = app.provideUserVideoService();
        VideoPlaylistCallback videoPlaylistCallback = new VideoPlaylistCallback();
        userVideoService.getUserVideoListDataResponse(videoTag, 1).enqueue(videoPlaylistCallback);
    }

    private class VideoPlaylistCallback implements Callback<UserVideoListDataResponse> {

        @Override
        public void onResponse(Call<UserVideoListDataResponse> call, Response<UserVideoListDataResponse> response) {
            Toast.makeText(getContext(), response.body().getData().getRecords().size() + " records", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onFailure(Call<UserVideoListDataResponse> call, Throwable t) {
            Toast.makeText(getContext(), "Failure", Toast.LENGTH_LONG).show();
        }
    }
}
