package com.interlog.ilstockinventory;

import android.app.DatePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.List;
import java.util.Random;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FmcgStock extends AppCompatActivity {
    EditText dt, firstNum, secondNum, fstNum, secNum, extraPcs, crtnPrc, rolPrc, pcPrc;
    TextView totalCP, totalRP, answer, randomNos, textViewId, totalCtP, totalRoP, totalpcP, tvPri, exPp;
    Spinner custName, mktName;
    AutoCompleteTextView locatAdr, prodtName, meas;
    DatePickerDialog datePickerDialog;
    DataBaseHelper dataBaseHelper;
    BroadcastReceiver broadcastReceiver;

    public static final int SYNC_STATUS_OK = 1;
    public static final int SYNC_STATUS_FAILED = 0;
    //a broadcast to know weather the data is synced or not
    public static final String DATA_SAVED_BROADCAST = "net.simplifiedcoding.datasaved";
    public static final String UI_UPDATE_BROADCAST = "com.interlog.ilstockinventory.uiupdatebroadcast";
    public static final String URL_SAVE_NAME = "http://interlog-ng.com/interlogmobile/inventory.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stock_fmcg);

        dataBaseHelper = new DataBaseHelper(this);

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

        //registering the broadcast receiver to update sync status
        registerReceiver(broadcastReceiver, new IntentFilter(DATA_SAVED_BROADCAST));
        registerReceiver(new NetworkChecker(), new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        dataBaseHelper.getData();
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                dataBaseHelper.getData();
            }
        };

        final int random = new Random().nextInt(10000) + 299999;
        randomNos = findViewById(R.id.randomNos);
        randomNos.setText(Integer.toString(random));

        meas = findViewById(R.id.meas);
        dt = findViewById(R.id.dt);
        firstNum = findViewById(R.id.firstNum);
        secondNum = findViewById(R.id.secondNum);
        fstNum = findViewById(R.id.fstNum);
        secNum = findViewById(R.id.secNum);
        totalCP = findViewById(R.id.totalCP);
        totalRP = findViewById(R.id.totalRP);
        extraPcs = findViewById(R.id.extraPcs);
        answer = findViewById(R.id.answer);

        custName = findViewById(R.id.custName);
        locatAdr = findViewById(R.id.locatAdr);
        // mktName = findViewById(R.id.mktName);
        prodtName = findViewById(R.id.prodtName);

        crtnPrc = findViewById(R.id.crtnPrc);
        rolPrc = findViewById(R.id.rolPrc);
        pcPrc = findViewById(R.id.pcPrc);
        totalCtP = findViewById(R.id.totalCtP);
        totalRoP = findViewById(R.id.totalRoP);
        totalpcP = findViewById(R.id.totalpcP);
        tvPri = findViewById(R.id.tvPri);

        exPp = findViewById(R.id.exPp);

        dataSubmit();
        populateLocAd();
        populateProdName();
        populateMeas();

        dt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR); // current year
                int mMonth = c.get(Calendar.MONTH); // current month
                int mDay = c.get(Calendar.DAY_OF_MONTH); // current day

                datePickerDialog = new DatePickerDialog(FmcgStock.this, new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        // set day of month , month and year value in the edit text
                        dt.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);

                    }
                }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });


        TextWatcher autoAddTextWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                double myNum1 = TextUtils.isEmpty(firstNum.getText().toString()) ? 0 : Double.parseDouble(firstNum.getText().toString());
                double myNum2 = TextUtils.isEmpty(secondNum.getText().toString()) ? 0 : Double.parseDouble(secondNum.getText().toString());

                double sum = myNum1 * myNum2;
                totalCP.setText(Double.toString(sum));

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        };

        firstNum.addTextChangedListener(autoAddTextWatcher);
        secondNum.addTextChangedListener(autoAddTextWatcher);

        // total roll price
        TextWatcher autoAddTextWatcher2 = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                double myNub1 = TextUtils.isEmpty(fstNum.getText().toString()) ? 0 : Double.parseDouble(fstNum.getText().toString());
                double myNub2 = TextUtils.isEmpty(secNum.getText().toString()) ? 0 : Double.parseDouble(secNum.getText().toString());

                double summ = myNub1 * myNub2;
                totalRP.setText(Double.toString(summ));

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        };

        fstNum.addTextChangedListener(autoAddTextWatcher2);
        secNum.addTextChangedListener(autoAddTextWatcher2);

        TextWatcher autoAddTextWatcher3 = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                double myTot1 = TextUtils.isEmpty(totalCP.getText().toString()) ? 0 : Double.parseDouble(totalCP.getText().toString());
                double myTot2 = TextUtils.isEmpty(totalRP.getText().toString()) ? 0 : Double.parseDouble(totalRP.getText().toString());
                double exPcs = TextUtils.isEmpty(extraPcs.getText().toString()) ? 0 : Double.parseDouble(extraPcs.getText().toString());

                double summm = myTot1 + myTot2 + exPcs;
                answer.setText(Double.toString(summm));

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        };

        totalCP.addTextChangedListener(autoAddTextWatcher3);
        totalRP.addTextChangedListener(autoAddTextWatcher3);
        extraPcs.addTextChangedListener(autoAddTextWatcher3);

        TextWatcher autoAddTextWatcher8 = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                double myPcp = TextUtils.isEmpty(pcPrc.getText().toString()) ? 0 : Double.parseDouble(pcPrc.getText().toString());
                double exPcs = TextUtils.isEmpty(extraPcs.getText().toString()) ? 0 : Double.parseDouble(extraPcs.getText().toString());

                double extPPr = myPcp * exPcs;
                exPp.setText(Double.toString(extPPr));

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        };

        pcPrc.addTextChangedListener(autoAddTextWatcher8);
        extraPcs.addTextChangedListener(autoAddTextWatcher8);

        TextWatcher autoAddTextWatcher4 = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                double totCtn = TextUtils.isEmpty(firstNum.getText().toString()) ? 0 : Double.parseDouble(firstNum.getText().toString());
                double prPctn = TextUtils.isEmpty(crtnPrc.getText().toString()) ? 0 : Double.parseDouble(crtnPrc.getText().toString());

                double mult1 = totCtn * prPctn;
                totalCtP.setText(Double.toString(mult1));

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        };

        firstNum.addTextChangedListener(autoAddTextWatcher4);
        crtnPrc.addTextChangedListener(autoAddTextWatcher4);

        TextWatcher autoAddTextWatcher5 = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                double totRo = TextUtils.isEmpty(fstNum.getText().toString()) ? 0 : Double.parseDouble(fstNum.getText().toString());
                double prRo = TextUtils.isEmpty(rolPrc.getText().toString()) ? 0 : Double.parseDouble(rolPrc.getText().toString());

                double mult2 = totRo * prRo;
                totalRoP.setText(Double.toString(mult2));
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        };

        fstNum.addTextChangedListener(autoAddTextWatcher5);
        rolPrc.addTextChangedListener(autoAddTextWatcher5);

        TextWatcher autoAddTextWatcher6 = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                double totPccs = TextUtils.isEmpty(answer.getText().toString()) ? 0 : Double.parseDouble(answer.getText().toString());
                double prppc = TextUtils.isEmpty(pcPrc.getText().toString()) ? 0 : Double.parseDouble(pcPrc.getText().toString());

                double mult3 = totPccs * prppc;
                totalpcP.setText(Double.toString(mult3));
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        };

        answer.addTextChangedListener(autoAddTextWatcher6);
        pcPrc.addTextChangedListener(autoAddTextWatcher6);


        TextWatcher autoAddTextWatcher7 = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                double vCt = TextUtils.isEmpty(totalCtP.getText().toString()) ? 0 : Double.parseDouble(totalCtP.getText().toString());
                double vRo = TextUtils.isEmpty(totalRoP.getText().toString()) ? 0 : Double.parseDouble(totalRoP.getText().toString());
                double vPc = TextUtils.isEmpty(exPp.getText().toString()) ? 0 : Double.parseDouble(exPp.getText().toString());

                double sum4 = vCt + vRo + vPc;
                tvPri.setText(Double.toString(sum4));
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        };

        totalCtP.addTextChangedListener(autoAddTextWatcher7);
        totalRoP.addTextChangedListener(autoAddTextWatcher7);
        exPp.addTextChangedListener(autoAddTextWatcher7);
    }

    private void populateLocAd() {
        //Log.d(TAG, "populateListView: Displaying data in the ListView.");
        dataBaseHelper = new DataBaseHelper(this);
        List<String> lables = dataBaseHelper.getLOA();
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, lables);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        locatAdr.setAdapter(dataAdapter);

    }
    private void populateProdName() {
        //Log.d(TAG, "populateListView: Displaying data in the ListView.");
        dataBaseHelper = new DataBaseHelper(this);
        List<String> lables = dataBaseHelper.getPN();
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, lables);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        prodtName.setAdapter(dataAdapter);

    }
    private void populateMeas() {
        //Log.d(TAG, "populateListView: Displaying data in the ListView.");
        dataBaseHelper = new DataBaseHelper(this);
        List<String> lables = dataBaseHelper.getMNT();
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, lables);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        meas.setAdapter(dataAdapter);

    }


    private void dataSubmit() {
        final String userId = textViewId.getText().toString();
        final String randomNo = randomNos.getText().toString();
        final String custmName = custName.getSelectedItem().toString();
        final String address = locatAdr.getText().toString();
       //final String mrktName = mktName.getSelectedItem().toString();
        final String prodName = prodtName.getText().toString();

        final String dat = dt.getText().toString();
        final String measureT = meas.getText().toString();
        final String numbCat = firstNum.getText().toString();
        final String qttyCat = secondNum.getText().toString();
        final String totCat = totalCP.getText().toString();

        final String numbRol = fstNum.getText().toString();
        final String qttyRol = secNum.getText().toString();
        final String totlRol = totalRP.getText().toString();
        final String extrPcs = extraPcs.getText().toString();
        final String extPPr = exPp.getText().toString();
        final String totlPcs = answer.getText().toString();

        final String cartPr = crtnPrc.getText().toString();
        final String rollPrc = rolPrc.getText().toString();
        final String picPrc = pcPrc.getText().toString();

        final String totaCtP = totalCtP.getText().toString();
        final String totaRoP = totalRoP.getText().toString();
        final String totaPcP = totalpcP.getText().toString();
        final String totVP = tvPri.getText().toString();

        if (custmName.isEmpty()) {
            Toast.makeText(FmcgStock.this, "some fields are empty", Toast.LENGTH_SHORT).show();
            return;
        }
        if (address.isEmpty()) {
            Toast.makeText(FmcgStock.this, "some fields are empty", Toast.LENGTH_SHORT).show();
            return;
        }
      /*  if (mrktName.isEmpty()) {
            Toast.makeText(FmcgStock.this, "some fields are empty", Toast.LENGTH_SHORT).show();
            return;
        }*/
        if (prodName.isEmpty()) {
            Toast.makeText(FmcgStock.this, "some fields are empty", Toast.LENGTH_SHORT).show();
            return;
        }

        if (dat.isEmpty()) {
            dt.setError("some fields are empty");
            dt.requestFocus();
            return;
        }
        if (measureT.isEmpty()) {
            meas.setError("some fields are empty");
            meas.requestFocus();
            return;
        }
        if (numbCat.isEmpty()) {
            firstNum.setError("some fields are empty");
            firstNum.requestFocus();
            return;
        }
        if (qttyCat.isEmpty()) {
            secondNum.setError("some fields are empty");
            secondNum.requestFocus();
            return;
        }
        if (totCat.isEmpty()) {
            totalCP.setError("some fields are empty");
            totalCP.requestFocus();
            return;
        }
        if (numbRol.isEmpty()) {
            fstNum.setError("some fields are empty");
            fstNum.requestFocus();
            return;
        }
        if (qttyRol.isEmpty()) {
            secNum.setError("some fields are empty");
            secNum.requestFocus();
            return;
        }
        if (totlRol.isEmpty()) {
            totalRP.setError("some fields are empty");
            totalRP.requestFocus();
            return;
        }
        if (extrPcs.isEmpty()) {
            extraPcs.setError("some fields are empty");
            extraPcs.requestFocus();
            return;
        }
        if (totlPcs.isEmpty()) {
            answer.setError("some fields are empty");
            answer.requestFocus();
            return;
        }
        if (cartPr.isEmpty()) {
            crtnPrc.setError("some fields are empty");
            crtnPrc.requestFocus();
            return;
        }
        if (rollPrc.isEmpty()) {
            rolPrc.setError("some fields are empty");
            rolPrc.requestFocus();
            return;
        }
        if (picPrc.isEmpty()) {
            pcPrc.setError("some fields are empty");
            pcPrc.requestFocus();
            return;
        }

        /**dataBaseHelper.addData(custmName, address, dat, mrktName, prodName, measureT, numbCat, qttyCat, totCat, numbRol, qttyRol, totlRol, extrPcs, totlPcs);
         Toast.makeText(FmcgStock.this, "Submitted...", Toast.LENGTH_SHORT).show();
         Intent intent = new Intent(FmcgStock.this, FmcgStock.class);
         startActivity(intent);**/

        /** do data upload using api call **/
        Call<ResponseBody> call = RetrofitClient
                .getInstance()
                .getNaSurvey()
                .submitResponse(userId, randomNo, custmName, address, prodName, dat, measureT, numbCat, qttyCat, totCat, numbRol, qttyRol, totlRol, extrPcs, extPPr, totlPcs, cartPr, rollPrc, picPrc, totaCtP, totaRoP, totaPcP, totVP);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    JSONObject obj = new JSONObject(String.valueOf(response));
                    if (!obj.getBoolean("error")) {
                        //if there is a success
                        //storing the name to sqlite with status synced
                        dataBaseHelper.addData(userId, randomNo, custmName, address, prodName, dat, measureT, numbCat, qttyCat, totCat, numbRol, qttyRol, totlRol, extrPcs, extPPr, totlPcs, cartPr, rollPrc, picPrc, totaCtP, totaRoP, totaPcP, totVP, SYNC_STATUS_OK);
                    } else {
                        //if there is some error
                        //saving the name to sqlite with status unsynced
                        dataBaseHelper.addData(userId, randomNo, custmName, address, prodName, dat, measureT, numbCat, qttyCat, totCat, numbRol, qttyRol, totlRol, extrPcs, extPPr, totlPcs, cartPr, rollPrc, picPrc, totaCtP, totaRoP, totaPcP, totVP, SYNC_STATUS_FAILED);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                try {
                    // String s = response.body().toString();
                    Toast.makeText(FmcgStock.this, "Submitted...", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(FmcgStock.this, FmcgStock.class);
                    startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                dataBaseHelper.addData(userId, randomNo, custmName, address, prodName, dat, measureT, numbCat, qttyCat, totCat, numbRol, qttyRol, totlRol, extrPcs, extPPr, totlPcs, cartPr, rollPrc, picPrc, totaCtP, totaRoP, totaPcP, totVP, SYNC_STATUS_FAILED);
                //Toast.makeText(SurveyActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
                Toast.makeText(FmcgStock.this, "data has been saved on phone and will submitted once there is internet connection", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(FmcgStock.this, FmcgStock.class);
                startActivity(intent);
            }
        });
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.submitBtn:
                dataSubmit();
                break;
        }
    }

    public void onStart(){
        super.onStart();
        registerReceiver(broadcastReceiver, new IntentFilter(UI_UPDATE_BROADCAST));
    }
}

