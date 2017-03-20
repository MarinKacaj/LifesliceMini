package com.marin.dev.lifeslicemini;

import android.app.Application;

import com.marin.dev.lifeslicemini.service.UserVideoService;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by C.R.C on 3/20/2017.
 * Provides app-scope dependencies.
 */
public class LifesliceMiniApp extends Application {

    private UserVideoService userVideoService;

    @Override
    public void onCreate() {
        super.onCreate();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.lifeslice.io")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        userVideoService = retrofit.create(UserVideoService.class);
    }

    public UserVideoService provideUserVideoService() {
        return userVideoService;
    }
}
