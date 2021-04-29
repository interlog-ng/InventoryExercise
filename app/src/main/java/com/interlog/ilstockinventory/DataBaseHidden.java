package com.interlog.ilstockinventory;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DataBaseHidden extends SQLiteOpenHelper {
    public static final String TAG = "DataBaseHidden";
    public static final String TABLE_NAME = "fmcgpdt10";

    public static final String COL1 = "id";
    public static final String COL9 = "USERID";
    public static final String COL2 = "PCTURE";
    public static final String COL8 = "LOCATNAME";
    public static final String COL3 = "CURRRD";
    public static final String COL4 = "CURRRDD";
    public static final String COL5 = "CURRRT";
    public static final String COL6 = "GPSLOCA";
    public static final String COL7 = "GPSADDR";
    public static final String COL10 = "RANDOMNUM";
    public static final String SYNC_STATUS = "syncstatus";

    public DataBaseHidden(Context context) {
        super(context, TABLE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_NAME + " (id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "USERID TEXT, PCTURE TEXT, LOCATNAME TEXT, CURRRD TEXT, CURRRDD TEXT, CURRRT TEXT, GPSLOCA TEXT, GPSADDR TEXT, RANDOMNUM, syncstatus integer)";
        db.execSQL(createTable);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);

    }

    public boolean addData(String userId, String Image, String locatName, String currtd, String currtdd, String currtt, String gpsloct, String gpsaddrr, String randNm, int sync_status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL9, userId);
        contentValues.put(COL2, Image);
        contentValues.put(COL8, locatName);
        contentValues.put(COL3, currtd);
        contentValues.put(COL4, currtdd);
        contentValues.put(COL5, currtt);
        contentValues.put(COL6, gpsloct);
        contentValues.put(COL7, gpsaddrr);
        contentValues.put(COL10, randNm);
        contentValues.put(SYNC_STATUS, sync_status);

        long result = db.insert(TABLE_NAME, null, contentValues);

        //if data is inserted correctly it will return -1
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public Cursor getData() {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME;
        Cursor data = db.rawQuery(query, null);

        return data;
    }

    public boolean updateNameStatus(int id, int sync_status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(SYNC_STATUS, sync_status);
        db.update(TABLE_NAME, contentValues, COL1 + "=" + id, null);
        db.close();
        return true;
    }

    /*
     * this method is for getting all the unsynced name
     * so that we can sync it with database
     * */
    public Cursor getUnsyncedNames() {
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT * FROM " + TABLE_NAME + " WHERE " + SYNC_STATUS + " = 0;";
        Cursor c = db.rawQuery(sql, null);
        return c;
    }

    public List<String> getFN() {
        List<String> labels = new ArrayList<String>();
        // Select All Query
        //String selectQuery = "SELECT * FROM " + TABLE_NAME;
        String selectQuery = "SELECT * FROM " + TABLE_NAME + " LIMIT 5";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                labels.add(cursor.getString(2));
                //  labels.add(cursor.getString(3));
                // labels.add(cursor.getString(4));
                // labels.add(cursor.getString(5));
            } while (cursor.moveToNext());
        }

        // closing connection
        cursor.close();
        db.close();

        // returning lables
        return labels;
    }

}