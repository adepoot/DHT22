package com.example.dht22_v2.data.remote;
import com.example.dht22_v2.data.model.TSAnswersResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface TSService {

    @GET("feeds.json?api_key=IVJB65V23SYN8BPZ")
    Call<TSAnswersResponse> getAnswers();

    @GET("feeds.json?api_key=IVJB65V23SYN8BPZ")
    Call<TSAnswersResponse> getAnswers(@Query("days") String tags);
}
