package com.marin.dev.lifeslicemini.domain;

/**
 * Created by C.R.C on 3/20/2017.
 * Represents a user video. Stores only the fields required for this sample app.
 */
public class UserVideo {

    private String videoLowURL;
    private String username;
    private String thumbnailUrl;

    public String getVideoLowURL() {
        return videoLowURL;
    }

    public String getUsername() {
        return username;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }
}
