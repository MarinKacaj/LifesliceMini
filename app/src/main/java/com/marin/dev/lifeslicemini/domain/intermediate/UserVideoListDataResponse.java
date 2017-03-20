package com.marin.dev.lifeslicemini.domain.intermediate;

/**
 * Created by C.R.C on 3/20/2017.
 * Represents the response as provided by the REST API.
 * This class is "volatile" for the domain logic as it is used only for matching the response.
 */
public class UserVideoListDataResponse {

    private UserVideoResponseDataWrapper data;

    public UserVideoResponseDataWrapper getData() {
        return data;
    }
}
