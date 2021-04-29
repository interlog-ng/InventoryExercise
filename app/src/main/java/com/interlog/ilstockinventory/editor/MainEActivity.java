package com.interlog.ilstockinventory.editor;


import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;
import android.widget.Toast;

import com.interlog.ilstockinventory.LoginActivity;
import com.interlog.ilstockinventory.R;
import com.interlog.ilstockinventory.SharedPrefManager;
import com.interlog.ilstockinventory.User;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static java.sql.DriverManager.println;

public class MainEActivity extends AppCompatActivity implements MainView {

    private static final int INTENT_EDIT = 200;
    RecyclerView recyclerView;
    SwipeRefreshLayout swipeRefresh;

    MainPresenter presenter;
    MainAdapter adapter;
    MainAdapter.ItemClickListener itemClickListener;

    List<Note> note;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maine);

        // call user id
        if (!SharedPrefManager.getInstance(this).isLoggedIn()) {
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }
        TextView textViewId = findViewById(R.id.textViewId);
        //getting the current user
        User user = SharedPrefManager.getInstance(this).getUser();
        //setting the values to the textviews
        textViewId.setText(String.valueOf(user.getId()));

          /*String userID = textViewId.getText().toString();

            Call<ResponseBody> call = ApiClient
                    .getInstance()
                    .getNaSurvey()
                    .getNotes(userID);

            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {

                }
            }); */

        swipeRefresh = findViewById(R.id.swipe_refresh);
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager((this)));

        presenter = new MainPresenter(this);
        presenter.getData();
        System.out.println("userId: " + user.getId());
        swipeRefresh.setOnRefreshListener(
                () -> presenter.getData() //presenter.getData();
        );

        itemClickListener = ((view, position) -> {

            int id = note.get(position).getId();
            int userID = note.get(position).getUserID(); // new
            String customerName = note.get(position).getCustomerName();
            String productName = note.get(position).getProductName();
            String locationAddr = note.get(position).getLocationAddr();
            String reportingDate = note.get(position).getReportingDate();
           // String color = note.get(position).getNote(); // example used int
            Toast.makeText(this, customerName, Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(this, EditorActivity.class);
            intent.putExtra("id", id);
            intent.putExtra("userID", userID); //new
            intent.putExtra("customerName", customerName);
            intent.putExtra("productName", productName);
            intent.putExtra("locationAddr", locationAddr);
            intent.putExtra("reportingDate", reportingDate);
            //intent.putExtra("color", color);
            startActivityForResult(intent, INTENT_EDIT);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == INTENT_EDIT && resultCode == RESULT_OK) {
            presenter.getData();
        }
    }


    @Override
    public void showLoading() {
        swipeRefresh.setRefreshing(true);
    }

    @Override
    public void hideLoading() {
        swipeRefresh.setRefreshing(false);
    }

    @Override
    public void onGetResult(List<Note> notes) {
        adapter = new MainAdapter(this, notes, itemClickListener);
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);

        note = notes;
    }

    @Override
    public void onErrorLoading(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
