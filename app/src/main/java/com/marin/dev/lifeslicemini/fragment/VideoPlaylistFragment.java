package com.marin.dev.lifeslicemini.fragment;

import android.Manifest;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.marin.dev.lifeslicemini.LifesliceMiniApp;
import com.marin.dev.lifeslicemini.R;
import com.marin.dev.lifeslicemini.adapter.UserVideoAdapter;
import com.marin.dev.lifeslicemini.domain.UserVideo;
import com.marin.dev.lifeslicemini.domain.intermediate.UserVideoListDataResponse;
import com.marin.dev.lifeslicemini.service.UserVideoService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
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
    private UserVideoAdapter userVideoAdapter;
    private List<UserVideo> userVideos = new ArrayList<>();

    @BindView(R.id.empty_state)
    View emptyState;
    @BindView(R.id.content)
    View content;
    @BindView(R.id.video_player)
    SimpleExoPlayerView videoPlayer;
    @BindView(R.id.user_video_container)
    ListView userVideoContainer;

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
        View rootView = inflater.inflate(R.layout.fragment_video_playlist, container, false);
        ButterKnife.bind(this, rootView);
        userVideoAdapter = new UserVideoAdapter(getContext(), userVideos);
        userVideoContainer.setAdapter(userVideoAdapter);
        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        app = ((LifesliceMiniApp) getActivity().getApplication());
    }

    private void setupPlayer() {
        // 1. Create a default TrackSelector
        Handler mainHandler = new Handler();
        BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        TrackSelection.Factory videoTrackSelectionFactory =
                new AdaptiveTrackSelection.Factory(bandwidthMeter);
        TrackSelector trackSelector =
                new DefaultTrackSelector(videoTrackSelectionFactory);
        // 2. Create a default LoadControl
        LoadControl loadControl = new DefaultLoadControl();
        // 3. Create the player
        SimpleExoPlayer player =
                ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector, loadControl);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        app = null;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        VideoPlaylistFragmentPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    @NeedsPermission({Manifest.permission.INTERNET})
    public void refresh(String videoTag) {
        UserVideoService userVideoService = app.provideUserVideoService();
        VideoPlaylistCallback videoPlaylistCallback = new VideoPlaylistCallback();
        userVideoService.getUserVideoListDataResponse(videoTag, 1).enqueue(videoPlaylistCallback);
    }

    private void switchToEmptyState() {
        emptyState.setVisibility(View.VISIBLE);
        content.setVisibility(View.GONE);
    }

    private void switchToContentState() {
        emptyState.setVisibility(View.GONE);
        content.setVisibility(View.VISIBLE);
    }

    private class VideoPlaylistCallback implements Callback<UserVideoListDataResponse> {

        @Override
        public void onResponse(Call<UserVideoListDataResponse> call, Response<UserVideoListDataResponse> response) {
            List<UserVideo> retrievedUserVideos = response.body().getData().getRecords();
            userVideos.clear();
            userVideos.addAll(retrievedUserVideos);
            userVideoAdapter.notifyDataSetChanged();
            switchToContentState();
        }

        @Override
        public void onFailure(Call<UserVideoListDataResponse> call, Throwable t) {
            Toast.makeText(getContext(), "Failure", Toast.LENGTH_LONG).show();
            switchToEmptyState();
        }
    }
}
