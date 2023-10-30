package com.example.sofe4640uassignment2;

import android.provider.BaseColumns;

public final class LocationContract {

    private LocationContract() {}

    public static class LocationEntry implements BaseColumns {
        public static final String TABLE_NAME = "location";
        public static final String COLUMN_NAME_ADDRESS = "address";
        public static final String COLUMN_NAME_LATITUDE = "latitude";
        public static final String COLUMN_NAME_LONGITUDE = "longitude";
    }
}
