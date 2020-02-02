package com.example.dht22_v2;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.dht22_v2.data.model.TSAnswersResponse;
import com.example.dht22_v2.data.remote.ApiUtils;
import com.example.dht22_v2.data.remote.TSService;
import com.google.android.material.snackbar.Snackbar;

import retrofit2.Call;
import retrofit2.Response;

public class StatusFragmentViewModel extends ViewModel {

    public final static String LOG_TAG = StatusFragmentViewModel.class.getName();

    private TSService mService;
    private final MutableLiveData<TSData> mData = new MutableLiveData<TSData>();

    public MutableLiveData<TSData> getLoadedData(){
        Log.d(LOG_TAG, "getLoadedData");
        if (mData == null){
            Log.d(LOG_TAG, "Initializing mData");
            getDataFromTS();
        }
        return mData;
    }

    public void getDataFromTS(){
        Log.d(LOG_TAG, "getDataFromTS");
        mService = ApiUtils.getTSService();

        mService.getAnswers("4").enqueue(new retrofit2.Callback<TSAnswersResponse>() {
            @Override
            public void onResponse(Call<TSAnswersResponse> call, Response<TSAnswersResponse> response) {
                Log.i(LOG_TAG, "onResponse");
                if(response.isSuccessful()) {
                    /*
                    * response.body() = com.example.dht22_v2.data.model.TSAnswersResponse@55e33d4
                    * response.body().getFeeds() = [com.example.dht22_v2.data.model.Feed@55e33d4, com.example.dht22_v2.data.model.Feed@6ded97d, ...]
                    * response.body().getFeeds().get(0) = com.example.dht22_v2.data.model.Feed@6ded97d
                    */
                    Log.d(LOG_TAG, "API response: succesfull - " + response.code());
                    mData.setValue(new TSData(response.body().getFeeds()));
                }else {
                    Log.d(LOG_TAG, "API response: NOT succesfull - " + response.code());
                }
            }

            @Override
            public void onFailure(Call<TSAnswersResponse> call, Throwable t) {
                Log.d(LOG_TAG, "onFailure, error loading from API");
            }
        });

    }

}
