package com.example.sofe4640uassignment2;

import android.provider.BaseColumns;

// Define the LocationContract class for organizing the structure of the database table
public final class LocationContract {

    // Make the constructor private to prevent accidental instantiation
    private LocationContract() {}

    // Define the inner class LocationEntry that implements the BaseColumns interface
    public static class LocationEntry implements BaseColumns {
        // Define constants for the table name and column names
        public static final String TABLE_NAME = "location";          // Table name
        public static final String COLUMN_NAME_ADDRESS = "address";  // Column name for address
        public static final String COLUMN_NAME_LATITUDE = "latitude";// Column name for latitude
        public static final String COLUMN_NAME_LONGITUDE = "longitude";// Column name for longitude
    }
}
