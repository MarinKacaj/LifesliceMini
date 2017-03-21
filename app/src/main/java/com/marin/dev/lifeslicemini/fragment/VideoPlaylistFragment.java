package com.marin.dev.lifeslicemini.fragment;

import android.Manifest;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ConcatenatingMediaSource;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.marin.dev.lifeslicemini.LifesliceMiniApp;
import com.marin.dev.lifeslicemini.R;
import com.marin.dev.lifeslicemini.adapter.UserVideoAdapter;
import com.marin.dev.lifeslicemini.domain.UserVideo;
import com.marin.dev.lifeslicemini.domain.intermediate.UserVideoListDataResponse;
import com.marin.dev.lifeslicemini.service.UserVideoService;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnItemClick;
import de.hdodenhof.circleimageview.CircleImageView;
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

    @BindView(R.id.empty_state)
    View emptyState;
    @BindView(R.id.content)
    View content;
    @BindView(R.id.video_player)
    SimpleExoPlayerView simpleExoPlayerView;
    @BindView(R.id.user_video_container)
    ListView userVideoContainer;
    @BindString(R.string.app_name)
    String appName;
    private LifesliceMiniApp app;
    private UserVideoAdapter userVideoAdapter;
    private List<UserVideo> userVideos = new ArrayList<>();
    private SimpleExoPlayer videoPlayer;
    private int previousVideoPosition = 0;
    private int currentVideoPosition = 0;
    private boolean isCurrentVideoPositionManuallyUpdated = false;

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
        setupPlayer();
        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        app = ((LifesliceMiniApp) getActivity().getApplication());
    }

    private void setupPlayer() {
        BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        TrackSelection.Factory videoTrackSelectionFactory =
                new AdaptiveTrackSelection.Factory(bandwidthMeter);
        TrackSelector trackSelector =
                new DefaultTrackSelector(videoTrackSelectionFactory);
        LoadControl loadControl = new DefaultLoadControl();

        videoPlayer = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector, loadControl);

        simpleExoPlayerView.setPlayer(videoPlayer);

        VideoPlayerEventListener videoPlayerEventListener = new VideoPlayerEventListener();
        videoPlayer.addListener(videoPlayerEventListener);
    }

    private void updatePlayer(List<UserVideo> userVideos) {
        // Produces DataSource instances through which media data is loaded.
        String userAgent = Util.getUserAgent(getContext(), appName);
        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(getContext(), userAgent);
        // Produces Extractor instances for parsing the media data.
        ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
        // This is the MediaSource representing the media to be played.
        List<MediaSource> mediaSources = new ArrayList<>(userVideos.size());
        for (UserVideo userVideo : userVideos) {
            String videoUrl = userVideo.getVideoUrl();
            Uri videoUri = Uri.parse(videoUrl);
            MediaSource videoSource = new ExtractorMediaSource(videoUri,
                    dataSourceFactory, extractorsFactory, null, null);
            mediaSources.add(videoSource);
        }
        ConcatenatingMediaSource sequentialMediaSource = new ConcatenatingMediaSource(mediaSources.toArray(new MediaSource[mediaSources.size()]));
        // Prepare the videoPlayer with the source.
        videoPlayer.prepare(sequentialMediaSource);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        videoPlayer.release();
        videoPlayer = null;
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

    @OnItemClick(R.id.user_video_container)
    void resetPlayerUserVideoSequenceFromCurrent(int position) {
        previousVideoPosition = currentVideoPosition;
        currentVideoPosition = position;
        isCurrentVideoPositionManuallyUpdated = true;
        List<UserVideo> currentUpToLastUserVideos = userVideos.subList(position, userVideos.size());
        updatePlayer(currentUpToLastUserVideos);
    }

    private void updateVideoRow(int position, boolean isPlaying) {
        int firstVisiblePosition = userVideoContainer.getFirstVisiblePosition();
        View rowView = userVideoContainer.getChildAt(position - firstVisiblePosition);

        if (rowView != null) {
            int avatarBorderColor;
            if (isPlaying) {
                avatarBorderColor = ContextCompat.getColor(getContext(), R.color.colorImageBorderPlaying);
            } else {
                avatarBorderColor = ContextCompat.getColor(getContext(), R.color.colorImageBorder);
            }
            CircleImageView avatarView = (CircleImageView) rowView.findViewById(R.id.user_avatar);
            avatarView.setBorderColor(avatarBorderColor);
        }
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
            updatePlayer(userVideos);
            isCurrentVideoPositionManuallyUpdated = true; // We're at zero
            switchToContentState();
        }

        @Override
        public void onFailure(Call<UserVideoListDataResponse> call, Throwable t) {
            Toast.makeText(getContext(), "Failure", Toast.LENGTH_LONG).show();
            userVideos.clear();
            userVideoAdapter.notifyDataSetChanged();
            updatePlayer(userVideos);
            switchToEmptyState();
        }
    }

    private class VideoPlayerEventListener implements ExoPlayer.EventListener {

        @Override
        public void onTimelineChanged(Timeline timeline, Object manifest) {
        }

        @Override
        public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {
            updateVideoRow(previousVideoPosition, false);
            if (isCurrentVideoPositionManuallyUpdated) {
                isCurrentVideoPositionManuallyUpdated = false;
            } else {
                previousVideoPosition = currentVideoPosition;
                currentVideoPosition++;
            }
            updateVideoRow(currentVideoPosition, true);
        }

        @Override
        public void onLoadingChanged(boolean isLoading) {
        }

        @Override
        public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        }

        @Override
        public void onPlayerError(ExoPlaybackException error) {
            Toast.makeText(getContext(), R.string.video_play_error_message, Toast.LENGTH_LONG).show();
        }

        @Override
        public void onPositionDiscontinuity() {
        }
    }
}
