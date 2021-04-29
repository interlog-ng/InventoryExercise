package com.interlog.ilstockinventory;


import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient2 {

    private static final String BASE_URL = "http://interlog-ng.com/";
    private static RetrofitClient2 mInstance;
    private Retrofit retrofit;

    private RetrofitClient2(){
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static synchronized RetrofitClient2 getInstance(){
        if(mInstance == null){
            mInstance = new RetrofitClient2();
        }
        return mInstance;
    }

    public FmcgPicsInterface getDTP() {
        return  retrofit.create(FmcgPicsInterface.class);
    }

}
