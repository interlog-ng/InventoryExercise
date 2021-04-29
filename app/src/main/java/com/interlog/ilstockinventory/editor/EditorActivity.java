package com.interlog.ilstockinventory.editor;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.interlog.ilstockinventory.R;
import com.thebluealliance.spectrum.SpectrumPalette;

public class EditorActivity extends AppCompatActivity implements EditorView {

    EditText et_cust, et_prod, et_addr, et_date;
    ProgressDialog progressDialog;
    SpectrumPalette palette;

    EditorPresenter presenter;

    int id;
    String productName, customerName, locationAddr, reportingDate;

    Menu actionMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        et_cust = findViewById(R.id.custNm);
        et_prod = findViewById(R.id.prodNm);
        et_addr = findViewById(R.id.locAdr);
        //et_date = findViewById(R.id.rDate);
        palette = findViewById(R.id.palette);

      /*  palette.setOnColorSelectedListener(
                clr -> color = clr
        ); */


        //Progress Dialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");

        presenter = new EditorPresenter(this);

        Intent intent = getIntent();
        id = intent.getIntExtra("id", 0);
        productName = intent.getStringExtra("productName");
        customerName = intent.getStringExtra("customerName");
        locationAddr = intent.getStringExtra("locationAddr");
        //color = intent.getIntExtra("color", 0); // example getIntExtra

        setDataFromIntentExtra();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_editor, menu);
        actionMenu = menu;

        if (id != 0) {
            actionMenu.findItem(R.id.edit).setVisible(true);
            actionMenu.findItem(R.id.delete).setVisible(true);
            actionMenu.findItem(R.id.save).setVisible(false);
            actionMenu.findItem(R.id.update).setVisible(false);
        } else {
            actionMenu.findItem(R.id.edit).setVisible(false);
            actionMenu.findItem(R.id.delete).setVisible(false);
            actionMenu.findItem(R.id.save).setVisible(true);
            actionMenu.findItem(R.id.update).setVisible(false);
        }

        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        String productName = et_prod.getText().toString().trim();
        String customerName = et_cust.getText().toString().trim();
        String locationAddr = et_addr.getText().toString().trim();

       // int color = this.color;

        switch (item.getItemId()) {
            case R.id.save:
                //Save
                if (productName.isEmpty()) {
                    et_prod.setError("Please enter a title");
                } else if (customerName.isEmpty()) {
                    et_cust.setError("Please enter a note");
                } else  if (locationAddr.isEmpty()) {
                    et_addr.setError("Please enter address");
                } else {
                    presenter.saveNote(productName, customerName, locationAddr);
                }
                return true;

            case R.id.edit:

                editMode();
                actionMenu.findItem(R.id.edit).setVisible(false);
                actionMenu.findItem(R.id.delete).setVisible(false);
                actionMenu.findItem(R.id.save).setVisible(false);
                actionMenu.findItem(R.id.update).setVisible(true);

                return true;

            case R.id.update:
                //Update

                if (productName.isEmpty()) {
                    et_prod.setError("Please enter a title");
                } else if (customerName.isEmpty()) {
                    et_cust.setError("Please enter a note");
                } else if (locationAddr.isEmpty()) {
                    et_addr.setError("Please enter address");
                } else {
                    presenter.updateNote(id, productName, customerName, locationAddr);
                }

                return true;

            case R.id.delete:

                AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
                alertDialog.setTitle("Confirm !");
                alertDialog.setMessage("Are you sure?");
                alertDialog.setNegativeButton("Yes", (dialog, which) -> {
                    dialog.dismiss();
                    presenter.deleteNote(id);
                });
                alertDialog.setPositiveButton("Cancel",
                        (dialog, which) -> dialog.dismiss());

                alertDialog.show();

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public void showProgress() {
        progressDialog.show();
    }

    @Override
    public void hideProgress() {
        progressDialog.hide();
    }

    @Override
    public void onRequestSuccess(String message) {
        Toast.makeText(EditorActivity.this,
                message,
                Toast.LENGTH_SHORT).show();
        setResult(RESULT_OK);
        finish(); //back to main activity
    }

    @Override
    public void onRequestError(String message) {
        Toast.makeText(EditorActivity.this,
                message,
                Toast.LENGTH_SHORT).show();
    }

    private void setDataFromIntentExtra() {

        if (id != 0) {
            et_prod.setText(productName);
            et_cust.setText(customerName);
            et_addr.setText(locationAddr);

            getSupportActionBar().setTitle("Update Information");
            readMode();
        } else {
           // palette.setSelectedColor(getResources().getColor(R.color.white));
            //color = getResources().getColor(R.color.white);
            editMode();
        }

    }

    private void editMode() {
        et_prod.setFocusableInTouchMode(true);
        et_cust.setFocusableInTouchMode(true);
        et_addr.setFocusableInTouchMode(true);
       // palette.setEnabled(true);
    }

    private void readMode() {
        et_prod.setFocusableInTouchMode(false);
        et_cust.setFocusableInTouchMode(false);
        et_addr.setFocusableInTouchMode(false);
        et_prod.setFocusable(false);
        et_cust.setFocusable(false);
        et_addr.setFocusable(false);
        //palette.setEnabled(false);
    }
}
