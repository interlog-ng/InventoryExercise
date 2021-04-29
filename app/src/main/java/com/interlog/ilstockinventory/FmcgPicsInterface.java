package com.interlog.ilstockinventory;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface FmcgPicsInterface {
    @FormUrlEncoded
    @POST("interlogmobile/pictureLocation.php")
    Call<ResponseBody> submitResponse(
            @Field("userID") String userID,
            @Field("image") String image,
            @Field("locationName") String locationName,
            @Field("currntDate") String currntDate,
            @Field("currntDate2") String currntDate2,
            @Field("currntTime") String currntTime,
            @Field("gpsLocatn") String gpsLocatn,
            @Field("gpsAddrss") String gpsAddrss,
            @Field("randomNumber") String randomNumber);

}
