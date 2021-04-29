package com.interlog.ilstockinventory;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface Api {

    String BASE_URL = "http://interlog-ng.com/";

    @GET("interlogmobile/viewproduct.php")
    Call<List<Product>> getFarmers();
}
