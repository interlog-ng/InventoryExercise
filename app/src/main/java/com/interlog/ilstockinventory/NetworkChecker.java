package com.interlog.ilstockinventory;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.interlog.ilstockinventory.DataBaseHelper.TAG;

public class NetworkChecker extends BroadcastReceiver {
    private Context context;
    private DataBaseHelper db;
    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;

        db = new DataBaseHelper(context);

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        //if there is a network
        if (activeNetwork != null) {
            //if connected to wifi or mobile data plan
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI || activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {

                //getting all the unsynced names
                Cursor cursor = db.getUnsyncedNames();
                if (cursor.moveToFirst()) {
                    do {
                        //calling the method to save the unsynced name to MySQL
                        userSignUp(
                                cursor.getInt(cursor.getColumnIndex(DataBaseHelper.COL1)),
                                cursor.getString(cursor.getColumnIndex(DataBaseHelper.COL18)),
                                cursor.getString(cursor.getColumnIndex(DataBaseHelper.COL16)),
                                cursor.getString(cursor.getColumnIndex(DataBaseHelper.COL2)),
                                cursor.getString(cursor.getColumnIndex(DataBaseHelper.COL3)),
                                cursor.getString(cursor.getColumnIndex(DataBaseHelper.COL4)),
                               // cursor.getString(cursor.getColumnIndex(DataBaseHelper.COL5)),
                                cursor.getString(cursor.getColumnIndex(DataBaseHelper.COL6)),
                                cursor.getString(cursor.getColumnIndex(DataBaseHelper.COL7)),
                                cursor.getString(cursor.getColumnIndex(DataBaseHelper.COL8)),
                                cursor.getString(cursor.getColumnIndex(DataBaseHelper.COL9)),
                                cursor.getString(cursor.getColumnIndex(DataBaseHelper.COL10)),
                                cursor.getString(cursor.getColumnIndex(DataBaseHelper.COL11)),
                                cursor.getString(cursor.getColumnIndex(DataBaseHelper.COL12)),
                                cursor.getString(cursor.getColumnIndex(DataBaseHelper.COL13)),
                                cursor.getString(cursor.getColumnIndex(DataBaseHelper.COL14)),
                                cursor.getString(cursor.getColumnIndex(DataBaseHelper.COL25)),
                                cursor.getString(cursor.getColumnIndex(DataBaseHelper.COL15)),
                                cursor.getString(cursor.getColumnIndex(DataBaseHelper.COL17)),
                                cursor.getString(cursor.getColumnIndex(DataBaseHelper.COL19)),
                                cursor.getString(cursor.getColumnIndex(DataBaseHelper.COL20)),
                                cursor.getString(cursor.getColumnIndex(DataBaseHelper.COL21)),
                                cursor.getString(cursor.getColumnIndex(DataBaseHelper.COL22)),
                                cursor.getString(cursor.getColumnIndex(DataBaseHelper.COL23)),
                                cursor.getString(cursor.getColumnIndex(DataBaseHelper.COL24))
                        );
                    } while (cursor.moveToNext());
                }
                Log.d(TAG, "add data: adding ");
            }
        }
    }

    private void userSignUp(final int id, final String userId, final String randomNo, final String custmName, final String address, final String dat, final String prodName, final String measureT, final String numbCat, final String qttyCat, final String totCat, final String numbRol, final String qttyRol, final String totlRol, final String extrPcs, final String extPPr, final String totlPcs, final String cartPr, final String rollPrc, final String picPrc, final String totaCtP, final String totaRoP, final String totaPcP, final String totVP){
        /** do user registration using api call **/
        Call<ResponseBody> call = RetrofitClient
                .getInstance()
                .getNaSurvey()
                .submitResponse(userId, randomNo, custmName, address, dat, prodName, measureT, numbCat, qttyCat, totCat, numbRol, qttyRol, totlRol, extrPcs, extPPr, totlPcs, cartPr, rollPrc, picPrc, totaCtP, totaRoP, totaPcP, totVP);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    JSONObject obj = new JSONObject(String.valueOf(response));
                    if (!obj.getBoolean("error")) {
                        //updating the status in sqlite
                        db.updateNameStatus(id, FmcgStock.SYNC_STATUS_OK);

                        //sending the broadcast to refresh the list
                        context.sendBroadcast(new Intent(FmcgStock.DATA_SAVED_BROADCAST));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Map<String, String> params = new HashMap<>();
                params.put("userId", userId);
                params.put("randomNo", randomNo);
                params.put("custmName", custmName);
                params.put("address", address);
                params.put("dat", dat);
              //  params.put("mrktName", mrktName);
                params.put("prodName", prodName);
                params.put("measureT", measureT);
                params.put("numbCat", numbCat);
                params.put("qttyCat", qttyCat);
                params.put("totCat", totCat);
                params.put("numbRol", numbRol);
                params.put("qttyRol", qttyRol);
                params.put("totlRol", totlRol);
                params.put("extrPcs", extrPcs);
                params.put("extPPr", extPPr);
                params.put("totlPcs", totlPcs);
                params.put("cartPr", cartPr);
                params.put("rollPrc", rollPrc);
                params.put("picPrc", picPrc);
                params.put("totaCtP", totaCtP);
                params.put("totaRoP", totaRoP);
                params.put("totaPcP", totaPcP);
                params.put("totVP", totVP);
                return;

            }
        });

    }
}