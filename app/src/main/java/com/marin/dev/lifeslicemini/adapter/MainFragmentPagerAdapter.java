package com.marin.dev.lifeslicemini.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.marin.dev.lifeslicemini.fragment.InputFragment;
import com.marin.dev.lifeslicemini.fragment.VideoPlaylistFragment;

/**
 * Created by C.R.C on 3/20/2017.
 * Adapter for the fragments that are shown as a result of interacting with tabs.
 */
public class MainFragmentPagerAdapter extends FragmentPagerAdapter {

    private String[] tabTitles;
    private InputFragment inputFragment;
    private VideoPlaylistFragment videoPlaylistFragment;

    public MainFragmentPagerAdapter(FragmentManager fm, String firstTabTitle, String secondTabTitle) {
        super(fm);
        this.tabTitles = new String[]{firstTabTitle, secondTabTitle};
        inputFragment = InputFragment.newInstance();
        videoPlaylistFragment = VideoPlaylistFragment.newInstance();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return inputFragment;
            case 1:
                return videoPlaylistFragment;
            default:
                throw new RuntimeException("Unexpected tab position");
        }
    }

    @Override
    public int getCount() {
        return tabTitles.length;
    }

    public void refreshVideoPlaylist(String videoTag) {
        videoPlaylistFragment.refresh(videoTag);
    }
}
