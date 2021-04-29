package com.interlog.ilstockinventory;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DataBaseHelper extends SQLiteOpenHelper {
    public static final String TAG = "DataBaseHelper";
    public static final String TABLE_NAME = "fmcgstock11";
    public static final String COL1 = "id";
    public static final String COL18 = "USERID";
    public static final String COL16 = "RANDOMNUM";
    public static final String COL2 = "CUSTNAM";
    public static final String COL3 = "ADDR";
    public static final String COL4 = "DATE";
   // public static final String COL5 = "MKTNAM";
    public static final String COL6 = "PRODTNAM";
    public static final String COL7 = "MEASUR";
    public static final String COL8 = "NUMCAT";
    public static final String COL9 = "QTYCAT";
    public static final String COL10 = "TOTCAT";
    public static final String COL11 = "NUMROL";
    public static final String COL12 = "QTYROL";
    public static final String COL13 = "TOTROL";
    public static final String COL14 = "EXTPCS";
    public static final String COL25 = "EXTRPP";
    public static final String COL15 = "TOTPCS";

    public static final String COL17 = "CARTNPRIC";
    public static final String COL19 = "ROLLPRIC";
    public static final String COL20 = "PIECPRIC";
    public static final String COL21 = "TOTCAP";
    public static final String COL22 = "TOTROP";
    public static final String COL23 = "TOTPCP";
    public static final String COL24 = "TOTVP";

    public static final String SYNC_STATUS = "syncstatus";

    public DataBaseHelper(Context context) {
        super(context, TABLE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_NAME + " (id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "USERID TEXT, RANDOMNUM TEXT, CUSTNAM TEXT, ADDR TEXT, DATE TEXT, PRODTNAM TEXT, MEASUR TEXT, NUMCAT TEXT, QTYCAT TEXT, TOTCAT TEXT, NUMROL TEXT, QTYROL TEXT, TOTROL TEXT, " +
                "EXTPCS TEXT, EXTRPP TEXT, TOTPCS TEXT, CARTNPRIC TEXT, ROLLPRIC TEXT, PIECPRIC TEXT, TOTCAP TEXT, TOTROP TEXT, TOTPCP TEXT, TOTVP TEXT, syncstatus integer)";
        db.execSQL(createTable);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);

    }

    public boolean addData(String userId, String randomNo, String custmName, String address, String dat, String prodName, String measureT, String numbCat, String qttyCat, String totCat, String numbRol, String qttyRol, String totlRol, String extrPcs, String extPPr, String totlPcs, String cartPr, String rollPrc, String picPrc, String totaCtP, String totaRoP, String totaPcP, String totVP, int sync_status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL18, userId);
        contentValues.put(COL16, randomNo);
        contentValues.put(COL2, custmName);
        contentValues.put(COL3, address);
        contentValues.put(COL4, dat);
       // contentValues.put(COL5, mrktName);
        contentValues.put(COL6, prodName);
        contentValues.put(COL7, measureT);
        contentValues.put(COL8, numbCat);
        contentValues.put(COL9, qttyCat);
        contentValues.put(COL10, totCat);
        contentValues.put(COL11, numbRol);
        contentValues.put(COL12, qttyRol);
        contentValues.put(COL13, totlRol);
        contentValues.put(COL14, extrPcs);
        contentValues.put(COL25, extPPr);
        contentValues.put(COL15, totlPcs);
        contentValues.put(COL17, cartPr);
        contentValues.put(COL19, rollPrc);
        contentValues.put(COL20, picPrc);
        contentValues.put(COL21, totaCtP);
        contentValues.put(COL22, totaRoP);
        contentValues.put(COL23, totaPcP);
        contentValues.put(COL24, totVP);
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
    public List<String> getLOA() {
        List<String> labels = new ArrayList<String>();
        // Select All Query
        //String selectQuery = "SELECT * FROM " + TABLE_NAME;
        String selectQuery = "SELECT * FROM " + TABLE_NAME + " LIMIT 3";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                labels.add(cursor.getString(4));
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
    public List<String> getPN() {
        List<String> labels = new ArrayList<String>();
        // Select All Query
        //String selectQuery = "SELECT * FROM " + TABLE_NAME;
        String selectQuery = "SELECT * FROM " + TABLE_NAME + " LIMIT 3";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                labels.add(cursor.getString(5));
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
    public List<String> getMNT() {
        List<String> labels = new ArrayList<String>();
        // Select All Query
        //String selectQuery = "SELECT * FROM " + TABLE_NAME;
        String selectQuery = "SELECT * FROM " + TABLE_NAME + " LIMIT 3";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                labels.add(cursor.getString(7));
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