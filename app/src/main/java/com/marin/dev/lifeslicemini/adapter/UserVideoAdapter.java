package com.marin.dev.lifeslicemini.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.marin.dev.lifeslicemini.R;
import com.marin.dev.lifeslicemini.domain.UserVideo;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by C.R.C on 3/20/2017.
 * Adapts user videos to their layout.
 */
public class UserVideoAdapter extends ArrayAdapter<UserVideo> {

    @LayoutRes
    private final static int ITEM_LAYOUT_RESOURCE = R.layout.video_user_item;

    private int lastPlayingPosition = 0;
    private int currentPlayingPosition = 0;

    public UserVideoAdapter(@NonNull Context context, @NonNull List<UserVideo> userVideos) {
        super(context, ITEM_LAYOUT_RESOURCE, userVideos);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View itemRootView, @NonNull ViewGroup parent) {
        ViewHolder viewHolder;
        if (itemRootView != null) {
            viewHolder = (ViewHolder) itemRootView.getTag();
        } else {
            itemRootView = LayoutInflater.from(getContext()).inflate(ITEM_LAYOUT_RESOURCE, parent, false);
            viewHolder = new ViewHolder(itemRootView);
            itemRootView.setTag(viewHolder);
        }

        UserVideo userVideo = getItem(position);
        String avatarUrl = userVideo.getThumbnailUrl();
        String username = getItem(position).getUsername();

        Picasso.with(getContext()).load(avatarUrl).into(viewHolder.avatarView);
        viewHolder.usernameView.setText(username);

        return itemRootView;
    }

    static class ViewHolder {

        @BindView(R.id.user_avatar)
        CircleImageView avatarView;
        @BindView(R.id.username)
        TextView usernameView;

        public ViewHolder(View rootView) {
            ButterKnife.bind(this, rootView);
        }
    }
}
