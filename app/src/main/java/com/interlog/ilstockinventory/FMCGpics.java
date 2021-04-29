package com.interlog.ilstockinventory;

import android.Manifest;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FMCGpics extends AppCompatActivity implements View.OnClickListener, LocationListener {
    EditText gpsLoc, gpsAddr, curr_d, curr_t, curr_d2, randomNos;
    EditText textViewId, locaName;
    ImageView imageView;
    private static final int IMAGE_REQUEST = 777;
    private Bitmap bitmap;
    DataBaseHidden dataBaseHidden;
    Button choseImg1, submitBtn;
    BroadcastReceiver broadcastReceiver;
    // LocationListener locationListener;

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    LocationManager locationManager;

    public static final int SYNC_STATUS_OK = 1;
    public static final int SYNC_STATUS_FAILED = 0;
    //a broadcast to know weather the data is synced or not
    public static final String DATA_SAVED_BROADCAST = "net.simplifiedcoding.datasaved";
    public static final String UI_UPDATE_BROADCAST = "com.nasurvey.icaptech.uiupdatebroadcast";
    public static final String URL_SAVE_NAME = "http://namarkets.com/nasurvey/newfarmer.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fmcgpics);

        imageView = findViewById(R.id.imageView1);

        // call user id
        if (!SharedPrefManager.getInstance(this).isLoggedIn()) {
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }
        textViewId = findViewById(R.id.textViewId);
        //getting the current user
        User user = SharedPrefManager.getInstance(this).getUser();
        //setting the values to the textviews
        textViewId.setText(String.valueOf(user.getId()));

        dataBaseHidden = new DataBaseHidden(this);

        //registering the broadcast receiver to update sync status
        registerReceiver(broadcastReceiver, new IntentFilter(DATA_SAVED_BROADCAST));
        registerReceiver(new NetworkChecker(), new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        dataBaseHidden.getData();
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                dataBaseHidden.getData();
            }
        };

        checkLocationPermission();
        //create a date string.
        String date_n = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(new Date());
        String date_m = new SimpleDateFormat("hh mm ss", Locale.getDefault()).format(new Date());

        Calendar calendar = Calendar.getInstance();
        String currentDate = DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime());

        curr_d = findViewById(R.id.curr_d);
        curr_t = findViewById(R.id.curr_t);
        curr_d2 = findViewById(R.id.curr_d2);

        curr_d.setText(date_n);
        curr_t.setText(date_m);
        curr_d2.setText(currentDate);
        locaName = findViewById(R.id.locaName);
        gpsLoc = findViewById(R.id.gpsLoc);
        gpsAddr = findViewById(R.id.gpsAddr);

        final int random = new Random().nextInt(10000) + 99999;
        randomNos = findViewById(R.id.randomNos);
        randomNos.setText(Integer.toString(random));

        choseImg1 = findViewById(R.id.choseImgg);

        submitBtn = findViewById(R.id.submitBtn);

        //dataSubmit();

        if (null == imageView) {
            Log.e("Error", "Ouh! there is no there is no child view with R.id.imageView ID within my parent view View.");
        }
        choseImg1.setOnClickListener(this);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        Location location = locationManager.getLastKnownLocation(locationManager.NETWORK_PROVIDER);
        //function for longitude and latitude coordinates:
        onLocationChanged(location);

        dataSubmit();
    }

        private void dataSubmit() {

            final String userId = textViewId.getText().toString();
            final String Image = imageToString();
            final String locatName = locaName.getText().toString();
            final String currtd = curr_d.getText().toString();
            final String currtdd = curr_d2.getText().toString();
            final String currtt = curr_t.getText().toString();
            final String gpsloct = gpsLoc.getText().toString();
            final String gpsaddrr = gpsAddr.getText().toString();
            final String randNm = randomNos.getText().toString();

            if (locatName.equals("")) {
                Toast.makeText(FMCGpics.this, "please enter location name", Toast.LENGTH_LONG).show();
                return;
            }
            if (gpsloct.equals("")) {
                Toast.makeText(FMCGpics.this, "please turn on your internet", Toast.LENGTH_LONG).show();
                return;
            }
            /** do data upload using api call **/
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
                            //if there is a success
                            //storing the name to sqlite with status synced
                            dataBaseHidden.addData(userId, Image, locatName, currtd, currtdd, currtt, gpsloct, gpsaddrr, randNm, SYNC_STATUS_OK);
                        } else {
                            //if there is some error
                            //saving the name to sqlite with status unsynced
                            dataBaseHidden.addData(userId, Image, locatName, currtd, currtdd, currtt, gpsloct, gpsaddrr, randNm, SYNC_STATUS_FAILED);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    try {
                        // String s = response.body().toString();
                        Toast.makeText(FMCGpics.this, "Submitted...", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(FMCGpics.this, ProfileActivity.class);
                        startActivity(intent);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    dataBaseHidden.addData(userId, Image, locatName, currtd, currtdd, currtt, gpsloct, gpsaddrr, randNm, SYNC_STATUS_FAILED);
                    //Toast.makeText(SurveyActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
                    Toast.makeText(FMCGpics.this, "data has been saved on phone and will submitted once there is internet connection", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(FMCGpics.this, ProfileActivity.class);
                    startActivity(intent);
                }
            });
        }

        //    Intent intent = new Intent(FMCGpics.this, FmcgStock.class);
        //  startActivity(intent);

    //private void dataSubmit() {

    private void selectImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {

            Uri picture = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), picture);
                imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }
    }

    private String imageToString() {
        //Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.id.imageView);
        //Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath);
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
            byte[] imgByte = byteArrayOutputStream.toByteArray();
            return Base64.encodeToString(imgByte, Base64.DEFAULT);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(this)
                        .setTitle(R.string.title_location_permission)
                        .setMessage(R.string.text_location_permission)
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(FMCGpics.this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION);
                            }
                        })
                        .create()
                        .show();

            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        //Request location updates:
                       locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 400, 1, this);
                       locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 400, 1, this);

                    }

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {

            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 400, 1, this);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 400, 1, this);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {

            locationManager.removeUpdates(this);
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        if(location !=null) {
            double longitude = location.getLongitude();
            double latitude = location.getLatitude();
            gpsLoc.setText(String.format("%s\n%s", longitude, latitude));
            Geocoder geo = new Geocoder(getApplicationContext(), Locale.getDefault());
            try {
                List<Address> addresses = geo.getFromLocation(latitude, longitude, 1);
                //gpAddress.setText(addresses.get(0).getFeatureName() + ", " + addresses.get(0).getLocality() +", " + addresses.get(0).getAdminArea() + ", " + addresses.get(0).getCountryName());
                // gpAddress.setText(addresses.get(0).getAddressLine(0) + ", " + addresses.get(0).getLocality() + ", " + addresses.get(0).getCountryName());
                gpsAddr.setText(addresses.get(0).getAddressLine(0));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.submitBtn:
              dataSubmit();
                break;

            case R.id.choseImgg:
                selectImage();
                break;
        }
    }

    public void onStart(){
        super.onStart();
        registerReceiver(broadcastReceiver, new IntentFilter(UI_UPDATE_BROADCAST));
    }

}