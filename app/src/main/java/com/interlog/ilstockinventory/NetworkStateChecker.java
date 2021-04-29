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

import static com.interlog.ilstockinventory.DataBaseHidden.TAG;

public class NetworkStateChecker extends BroadcastReceiver {
    private Context context;
    private DataBaseHidden db;
    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;

        db = new DataBaseHidden(context);

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
                                cursor.getInt(cursor.getColumnIndex(DataBaseHidden.COL1)),
                                cursor.getString(cursor.getColumnIndex(DataBaseHidden.COL9)),
                                cursor.getString(cursor.getColumnIndex(DataBaseHidden.COL2)),
                                cursor.getString(cursor.getColumnIndex(DataBaseHidden.COL8)),
                                cursor.getString(cursor.getColumnIndex(DataBaseHidden.COL3)),
                                cursor.getString(cursor.getColumnIndex(DataBaseHidden.COL4)),
                                cursor.getString(cursor.getColumnIndex(DataBaseHidden.COL5)),
                                cursor.getString(cursor.getColumnIndex(DataBaseHidden.COL6)),
                                cursor.getString(cursor.getColumnIndex(DataBaseHidden.COL7)),
                                cursor.getString(cursor.getColumnIndex(DataBaseHidden.COL10))
                        );
                    } while (cursor.moveToNext());
                }
                Log.d(TAG, "add data: adding ");
            }
        }
    }

    private void userSignUp(final int id, final String userId, final String Image, final String locatName, final String currtd, final String currtdd, final String currtt, final String gpsloct, final String gpsaddrr, final String randNm){
        /** do user registration using api call **/
        Call<ResponseBody> call = RetrofitClient2
                .getInstance()
                .getDTP()
                .submitResponse(userId, Image, locatName, currtd, currtdd, currtt, gpsloct, gpsaddrr, randNm);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    JSONObject obj = new JSONObject(String.valueOf(response));
                    if (!obj.getBoolean("error")) {
                        //updating the status in sqlite
                        db.updateNameStatus(id, FMCGpics.SYNC_STATUS_OK);

                        //sending the broadcast to refresh the list
                        context.sendBroadcast(new Intent(FMCGpics.DATA_SAVED_BROADCAST));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Map<String, String> params = new HashMap<>();
                params.put("userId", userId);
                params.put("Image", Image);
                params.put("locatName", locatName);
                params.put("currtd", currtd);
                params.put("currtdd", currtdd);
                params.put("currtt", currtt);
                params.put("gpsloct", gpsloct);
                params.put("gpsaddrr", gpsaddrr);
                params.put("randNm", randNm);
                return;

            }
        });

    }
}