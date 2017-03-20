package com.marin.dev.lifeslicemini.service;

import com.marin.dev.lifeslicemini.domain.intermediate.UserVideoListDataResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by C.R.C on 3/20/2017.
 * Represents part of the remote API services that are related to user videos.
 */
public interface UserVideoService {

    String TAG_PARAMETER = "tag";

    @GET("timelines/tags/{" + TAG_PARAMETER + "}")
    Call<UserVideoListDataResponse> getUserVideoListDataResponse(@Path(TAG_PARAMETER) String videoTag,
                                                                 @Query("page") int pageNum);
}
