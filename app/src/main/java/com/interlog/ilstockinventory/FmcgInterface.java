package com.interlog.ilstockinventory;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface FmcgInterface  {
    @FormUrlEncoded
    @POST("interlogmobile/inventory.php")
    Call<ResponseBody> submitResponse(
            @Field("userID") String userID,
            @Field("randomNumber") String randomNumber,
            @Field("customerName") String customerName,
            @Field("locationAddr") String locationAddr,
            //@Field("marketName") String marketName,
            @Field("productName") String productName,
            @Field("reportingDate") String reportingDate,
            @Field("measmt") String measmt,
            @Field("noCartons") String noCartons,
            @Field("qtyCartons") String qtyCartons,
            @Field("totalAllCartons") String totalAllCartons,
            @Field("noRolls") String noRolls,
            @Field("qtyRolls") String qtyRolls,
            @Field("totalAllRolls") String totalAllRolls,
            @Field("extraPieces") String extraPieces,
            @Field("extraPP") String extraPP,
            @Field("allTotal") String allTotal,
            @Field("cartonPrice") String cartonPrice,
            @Field("rollPrice") String rollPrice,
            @Field("piecePrice") String piecePrice,
            @Field("totalCartonPc") String totalCartonPc,
            @Field("totalRollPc") String totalRollPc,
            @Field("totalPiecePc") String totalPiecePc,
            @Field("totalAmount") String totalAmount);

}
