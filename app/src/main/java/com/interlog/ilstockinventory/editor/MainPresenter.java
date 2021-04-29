package com.interlog.ilstockinventory.editor;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.interlog.ilstockinventory.LoginActivity;
import com.interlog.ilstockinventory.R;
import com.interlog.ilstockinventory.SharedPrefManager;
import com.interlog.ilstockinventory.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainPresenter {

    private MainView view;

    public MainPresenter(MainView view) {
        this.view = view;
    }

/**if (!SharedPrefManager.getInstance(this).isLoggedIn()) {
        finish();
        startActivity(new Intent(this, LoginActivity.class));
    }
    //textViewId = findViewById(R.id.textViewId);
    //getting the current user
    User userID = SharedPrefManager.getInstance(this).getUser();
    //setting the values to the textviews
     //   textViewId.setText(String.valueOf(user.getId())); **/

    void getData(){
        view.showLoading();

        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<List<Note>> call = apiInterface.getNotes();
        // Call<List<Note>> call = ApiClient.getInstance().getNaSurvey().getNotes(userID);
        call.enqueue(new Callback<List<Note>>() {
            @Override
            public void onResponse(@NonNull Call<List<Note>> call, @NonNull Response<List<Note>> response) {
                //System.out.println("call returned: " + response.toString());
                view.hideLoading();
                if(response.isSuccessful()){
                   /* System.out.println("response hes successful: " + response.isSuccessful()+"");
                    System.out.println("response hes messsgae: " + response.message()+"");
                   if (response.body() != null){
                       System.out.println("response body: " + response.body()+"");*/

                       view.onGetResult(response.body());
                   }
                }

            @Override
            public void onFailure(@NonNull Call<List<Note>> call, @NonNull Throwable t) {
              /*System.out.println("Localised msg: " + t.getLocalizedMessage()+"");
                System.out.println("Cause: " + t.getCause()+"");
                System.out.println("Message: " + t.getMessage()+"");*/

                view.hideLoading();
                view.onErrorLoading(t.getLocalizedMessage());
            }
        });
    }

}
