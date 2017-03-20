package com.marin.dev.lifeslicemini;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.marin.dev.lifeslicemini.adapter.MainFragmentPagerAdapter;
import com.marin.dev.lifeslicemini.fragment.InputFragment;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.karim.MaterialTabs;

public class MainActivity extends AppCompatActivity implements
        InputFragment.OnVideoTagQueryTriggeredListener {

    @BindView(R.id.tabs)
    MaterialTabs tabs;
    @BindView(R.id.tab_pager)
    ViewPager tabPager;

    @BindString(R.string.first_tab_title)
    String firstTabTitle;
    @BindString(R.string.second_tab_title)
    String secondTabTitle;
    private MainFragmentPagerAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_container);

        ButterKnife.bind(this);

        pagerAdapter = new MainFragmentPagerAdapter(
                getSupportFragmentManager(), firstTabTitle, secondTabTitle);
        tabPager.setAdapter(pagerAdapter);
        tabs.setViewPager(tabPager);
    }

    @Override
    public void onVideoTagQueryTriggered(String videoTag) {
        // this stinks
        tabPager.setCurrentItem(1); // why should I know the index of the tab I want?!
        pagerAdapter.refreshVideoPlaylist(videoTag); // one call per action should be enough, why this?!
    }
}
