package com.myapplication.network;

import com.myapplication.database.models.MediaList;
import retrofit2.Call;
import retrofit2.http.GET;

import java.util.List;

public interface RetrofitInterface {

    @GET("media.json?print=pretty")
    Call<List<MediaList>> getMediaLists();
}