package com.tptien.tpractice.Service;

public class APIService {
    private static final String baseUrl ="https://tstudy.000webhostapp.com/TStudy/";
    public static DataService getService(){
        return APIRetrofitClient.getAPIClient(baseUrl).create(DataService.class);
    }
}
