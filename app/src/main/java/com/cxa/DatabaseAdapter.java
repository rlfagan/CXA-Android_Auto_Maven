/********************************************************************************************
 * Copyright (C) 2020 Acoustic, L.P. All rights reserved.
 *
 * NOTICE: This file contains material that is confidential and proprietary to
 * Acoustic, L.P. and/or other developers. No license is granted under any intellectual or
 * industrial property rights of Acoustic, L.P. except as may be provided in an agreement with
 * Acoustic, L.P. Any unauthorized copying or distribution of content from this file is
 * prohibited.
 ********************************************************************************************/
package com.cxa;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

public class DatabaseAdapter {
    public static final String KEY_ROWID = "_id";
    public static final String KEY_SUBTOTAL = "subtotal";
    public static final String KEY_SHIPPING = "shippingFee";
    public static final String KEY_TAX = "estimatedTax";
    public static final String KEY_TOTAL = "orderTotal";
    public static final String KEY_PRODUCT_DESCRIPTION = "productDescription";
    public static final String KEY_QUANTITY = "quantity";

    private static final String TAG = "DatabaseAdapter";
    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;

    private static final String DATABASE_NAME = "Products";
    private static final String SQLITE_TABLE = "Purchases";
    private static final int DATABASE_VERSION = 1;

    private final Context mCtx;

    private static final String DATABASE_CREATE =
            "CREATE TABLE if not exists " + SQLITE_TABLE + " (" +
                    KEY_ROWID + " integer PRIMARY KEY autoincrement," +
                    KEY_SUBTOTAL + "," +
                    KEY_SHIPPING + "," +
                    KEY_TAX + "," +
                    KEY_TOTAL + "," +
                    KEY_PRODUCT_DESCRIPTION + "," +
                    KEY_QUANTITY + ");";

    private static class DatabaseHelper extends SQLiteOpenHelper {

        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            Log.w(TAG, DATABASE_CREATE);
            db.execSQL(DATABASE_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS " + SQLITE_TABLE);
            onCreate(db);
        }
    }

    public DatabaseAdapter(Context ctx) {
        this.mCtx = ctx;
    }

    public DatabaseAdapter open() throws SQLException {
        mDbHelper = new DatabaseHelper(mCtx);
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        if (mDbHelper != null) {
            mDbHelper.close();
        }
    }

    public long addPurchase(String subtotal, String shippingFee,
                            String estimatedTax, String orderTotal, String productDescription, String quantity) {

        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_SUBTOTAL, subtotal);
        initialValues.put(KEY_SHIPPING, shippingFee);
        initialValues.put(KEY_TAX, estimatedTax);
        initialValues.put(KEY_TOTAL, orderTotal);
        initialValues.put(KEY_PRODUCT_DESCRIPTION, productDescription);
        initialValues.put(KEY_QUANTITY, quantity);


        return mDb.insert(SQLITE_TABLE, null, initialValues);
    }


    public boolean deleteAllPurchases() {

        String query = "SELECT  * FROM " + SQLITE_TABLE;

        Cursor cursor = mDb.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                if (!cursor.getString(5).equals("total")) {
                    deleteProduct(cursor.getString(5));
                }
            } while (cursor.moveToNext());
        }

        updateSubtotal(1, "$0.00", "$0.00", "$0.00", "$0.00");

        return true;


    }

    public Cursor fetchAllPurchases() {

        Cursor mCursor = mDb.query(SQLITE_TABLE, new String[]{KEY_ROWID,
                        KEY_SUBTOTAL, KEY_SHIPPING, KEY_TAX, KEY_TOTAL},
                null, null, null, null, null);

        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    public String getCurrentSubtotal() {

        String query = "SELECT  * FROM " + SQLITE_TABLE;

        Cursor cursor = mDb.rawQuery(query, null);
        cursor.moveToFirst(); //go to first row

        return cursor.getString(1);
    }


    public void getAllTables() {
        String query = "SELECT  * FROM " + SQLITE_TABLE;
        Cursor cursor = mDb.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {

                Log.d("1 - Regular price", cursor.getString(0));
                Log.d("2 - ", cursor.getString(1));
                Log.d("3)third column)", cursor.getString(2));
                Log.d("4)forth column)", cursor.getString(3));
                Log.d("5)fifth column)", cursor.getString(4));
                Log.d("6)sixth column)", cursor.getString(5));
                Log.d("7 - Quantity", cursor.getString(6));

            } while (cursor.moveToNext());
        }

    }

    //GET

    public void updateSubtotal(long rowId, String newSubtotal, String newShipping, String newTax, String finalTotal) {

        ContentValues args = new ContentValues();
        args.put(KEY_ROWID, rowId);
        args.put(KEY_SUBTOTAL, newSubtotal);
        args.put(KEY_SHIPPING, newShipping);
        args.put(KEY_TAX, newTax);
        args.put(KEY_TOTAL, finalTotal);

        int i = mDb.update(SQLITE_TABLE, //table
                args, // column/value
                KEY_ROWID + " = ?", // selections
                new String[]
                        {String.valueOf(rowId)}); //selection args


    }

    public int updateQuantity(String productName, String newQuantity) {

        ContentValues args = new ContentValues();
        args.put(KEY_PRODUCT_DESCRIPTION, productName);
        args.put(KEY_QUANTITY, newQuantity);

        int i = mDb.update(SQLITE_TABLE, //table
                args, // column/value
                KEY_PRODUCT_DESCRIPTION + " = ?", // selections
                new String[]
                        {String.valueOf(productName)}); //selection args

        return i;

    }

    public void deleteProduct(String productName) {

        String table = SQLITE_TABLE;
        String whereClause = KEY_PRODUCT_DESCRIPTION + "=?";
        String[] whereArgs = new String[]{String.valueOf(productName)};
        mDb.delete(table, whereClause, whereArgs);


    }


    public long getElementsInDB() {
        return DatabaseUtils.queryNumEntries(mDb, SQLITE_TABLE);
    }

    public boolean checkProductPurchased(String fieldValue) {
        String Query = "Select * from " + SQLITE_TABLE + " where " + KEY_PRODUCT_DESCRIPTION + " = " + "'" + fieldValue + "';";

        Cursor cursor = mDb.rawQuery(Query, null);
        if (cursor.getCount() <= 0) {
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }

    public String getTableName() {
        return SQLITE_TABLE;
    }

    public ArrayList<String> getObjectsPurchased() {
        ArrayList<String> objectsPurchased = new ArrayList<String>();

        String query = "SELECT  * FROM " + SQLITE_TABLE;

        Cursor cursor = mDb.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                objectsPurchased.add(cursor.getString(5));
            } while (cursor.moveToNext());
        }

        return objectsPurchased;
    }

    public ArrayList<String> getListOfPrices() {
        ArrayList<String> objectsPurchased = new ArrayList<String>();

        String query = "SELECT  * FROM " + SQLITE_TABLE;

        Cursor cursor = mDb.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                objectsPurchased.add(cursor.getString(1));
            } while (cursor.moveToNext());
        }

        return objectsPurchased;
    }

    public ArrayList<String> getListOfQuantities() {
        ArrayList<String> objectsPurchased = new ArrayList<String>();

        String query = "SELECT  * FROM " + SQLITE_TABLE;

        Cursor cursor = mDb.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                objectsPurchased.add(cursor.getString(6));
            } while (cursor.moveToNext());
        }

        return objectsPurchased;
    }


}
