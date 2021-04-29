package com.interlog.ilstockinventory.editor;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;

public interface ApiInterface {


    @FormUrlEncoded
    @POST("interlogmobile/save.php")
    Call<Note> saveNote(
            @Field("productName") String productName,
            @Field("customerName") String customerName,
            @Field("locationAddr") String locationAddr
            //@Field("reportingDate") String reportingDate
    );

    @GET("interlogmobile/getinv.php" )
    Call<List<Note>> getNotes(
            //@QueryMap Map<String, String> options

            //@Field("userID") String userID
    );

    @FormUrlEncoded
    @POST("interlogmobile/updateinv.php")
    Call<Note> updateNote(
            @Field("id") int id,
            @Field("productName") String productName,
            @Field("customerName") String customerName,
            @Field("locationAddr") String locationAddr
           // @Field("reportingDate") String reportingDate
    );

    @FormUrlEncoded
    @POST("interlogmobile/deleteinv.php")
    Call<Note> deleteNote(@Field("id") int id
    );
}
