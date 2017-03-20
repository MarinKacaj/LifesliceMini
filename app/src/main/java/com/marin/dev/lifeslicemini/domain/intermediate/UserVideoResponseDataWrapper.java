package com.marin.dev.lifeslicemini.domain.intermediate;

import com.marin.dev.lifeslicemini.domain.UserVideo;

import java.util.List;

/**
 * Created by C.R.C on 3/20/2017.
 * Represents the response's data field as provided by the REST API.
 * This class is "volatile" for the domain logic as it is used only for matching the response.
 */
public class UserVideoResponseDataWrapper {

    private List<UserVideo> records;

    public List<UserVideo> getRecords() {
        return records;
    }
}
