package com.example.sofe4640uassignment2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

// LocationDbHelper is a helper class for managing a SQLite database related to location information
public class LocationDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "Locations.db";
    private static final int DATABASE_VERSION = 1;

    // SQL statement to create the database table
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + LocationContract.LocationEntry.TABLE_NAME + " (" +
                    LocationContract.LocationEntry._ID + " INTEGER PRIMARY KEY," +
                    LocationContract.LocationEntry.COLUMN_NAME_ADDRESS + " TEXT," +
                    LocationContract.LocationEntry.COLUMN_NAME_LATITUDE + " REAL," +
                    LocationContract.LocationEntry.COLUMN_NAME_LONGITUDE + " REAL)";

    // SQL statement to delete the database table
    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + LocationContract.LocationEntry.TABLE_NAME;

    // Constructor for LocationDbHelper
    public LocationDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Called when the database is first created
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    // Called when the database needs to be upgraded
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    public void clearDatabase() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + LocationContract.LocationEntry.TABLE_NAME);
        db.close();
    }

    // Insert a location into the database
    public void insertLocation(String address, double latitude, double longitude) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(LocationContract.LocationEntry.COLUMN_NAME_ADDRESS, address);
        values.put(LocationContract.LocationEntry.COLUMN_NAME_LATITUDE, latitude);
        values.put(LocationContract.LocationEntry.COLUMN_NAME_LONGITUDE, longitude);
        db.insert(LocationContract.LocationEntry.TABLE_NAME, null, values);
    }

    // Query a location by address and return it as a JSON object
    public JSONObject queryLocation(String addressQuery) {
        SQLiteDatabase db = getReadableDatabase();
        String[] projection = {
                LocationContract.LocationEntry.COLUMN_NAME_LATITUDE,
                LocationContract.LocationEntry.COLUMN_NAME_LONGITUDE
        };
        String selection = LocationContract.LocationEntry.COLUMN_NAME_ADDRESS + " = ?";
        String[] selectionArgs = { addressQuery };
        Cursor cursor = db.query(
                LocationContract.LocationEntry.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        if (cursor.moveToFirst()) {
            double latitude = cursor.getDouble(
                    cursor.getColumnIndexOrThrow(LocationContract.LocationEntry.COLUMN_NAME_LATITUDE)
            );
            double longitude = cursor.getDouble(
                    cursor.getColumnIndexOrThrow(LocationContract.LocationEntry.COLUMN_NAME_LONGITUDE)
            );

            JSONObject locationData = new JSONObject();
            try {
                locationData.put("latitude", latitude);
                locationData.put("longitude", longitude);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            cursor.close();
            return locationData;
        } else {
            cursor.close();
            return null; // Address not found
        }
    }

    // Add a location and return the row ID
    public long addLocation(String address, double latitude, double longitude) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(LocationContract.LocationEntry.COLUMN_NAME_ADDRESS, address);
        values.put(LocationContract.LocationEntry.COLUMN_NAME_LATITUDE, latitude);
        values.put(LocationContract.LocationEntry.COLUMN_NAME_LONGITUDE, longitude);
        return db.insert(LocationContract.LocationEntry.TABLE_NAME, null, values);
    }

    // Delete a location by address and return the number of rows affected
    public int deleteLocationByAddress(String address) {
        SQLiteDatabase db = getWritableDatabase();
        return db.delete(
                LocationContract.LocationEntry.TABLE_NAME,
                LocationContract.LocationEntry.COLUMN_NAME_ADDRESS + " = ?",
                new String[]{address}
        );
    }

    // Update a location and return the number of rows affected
    public int updateLocation(String oldAddress, String newAddress, double newLatitude, double newLongitude) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(LocationContract.LocationEntry.COLUMN_NAME_ADDRESS, newAddress);
        values.put(LocationContract.LocationEntry.COLUMN_NAME_LATITUDE, newLatitude);
        values.put(LocationContract.LocationEntry.COLUMN_NAME_LONGITUDE, newLongitude);
        return db.update(
                LocationContract.LocationEntry.TABLE_NAME,
                values,
                LocationContract.LocationEntry.COLUMN_NAME_ADDRESS + " = ?",
                new String[]{oldAddress}
        );
    }

    // Retrieve a list of all locations as JSON objects
    public List<JSONObject> getAllLocations() {
        SQLiteDatabase db = getReadableDatabase();
        List<JSONObject> locationList = new ArrayList<>();
        Cursor cursor = db.query(
                LocationContract.LocationEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null
        );

        while (cursor.moveToNext()) {
            JSONObject locationData = new JSONObject();
            try {
                String address = cursor.getString(cursor.getColumnIndexOrThrow(LocationContract.LocationEntry.COLUMN_NAME_ADDRESS));
                double latitude = cursor.getDouble(cursor.getColumnIndexOrThrow(LocationContract.LocationEntry.COLUMN_NAME_LATITUDE));
                double longitude = cursor.getDouble(cursor.getColumnIndexOrThrow(LocationContract.LocationEntry.COLUMN_NAME_LONGITUDE));

                locationData.put("address", address);
                locationData.put("latitude", latitude);
                locationData.put("longitude", longitude);

                locationList.add(locationData);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        cursor.close();
        return locationList;
    }
}
