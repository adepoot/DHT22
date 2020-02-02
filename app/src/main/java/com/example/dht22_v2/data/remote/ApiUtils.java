package com.example.dht22_v2.data.remote;

public class ApiUtils {

    public static final String BASE_URL = "https://api.thingspeak.com/channels/838110/";

    public static TSService getTSService() {
        return RetrofitClient.getClient(BASE_URL).create(TSService.class);
    }
}
