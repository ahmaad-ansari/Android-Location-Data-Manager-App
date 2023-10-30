package com.example.sofe4640uassignment2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TextInputEditText editTextAddress;
    private TextInputEditText editTextLatitude;
    private TextInputEditText editTextLongitude;

    private ArrayAdapter<String> adapter;
    private List<String> locationList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextAddress = findViewById(R.id.editTextAddress);
        editTextLatitude = findViewById(R.id.editTextLatitude);
        editTextLongitude = findViewById(R.id.editTextLongitude);

        ListView listViewLocations = findViewById(R.id.listViewLocations);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, locationList);
        listViewLocations.setAdapter(adapter);
        displayAllLocations();

        try {
            storeLocations();
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    private void displayAllLocations() {
        LocationDbHelper dbHelper = new LocationDbHelper(this);
        List<JSONObject> locations = dbHelper.getAllLocations();

        locationList.clear();
        for (JSONObject location : locations) {
            try {
                String address = location.getString("address");
                double latitude = location.getDouble("latitude");
                double longitude = location.getDouble("longitude");
                locationList.add("Address: " + address + "\nLatitude: " + latitude + ", Longitude: " + longitude);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        adapter.notifyDataSetChanged();
    }

    private void storeLocations() throws JSONException {
        LocationUtils locationUtils = new LocationUtils(this);
        List<JSONObject> locations = locationUtils.getLocationsFromJson();

        GeocodingUtils geocodingUtils = new GeocodingUtils(this);
        List<String> addresses = geocodingUtils.getAddressesFromLocations(locations);

        LocationDbHelper dbHelper = new LocationDbHelper(this);

        for (int i = 0; i < addresses.size(); i++) {
            String address = addresses.get(i);
            double latitude = locations.get(i).getDouble("lat");
            double longitude = locations.get(i).getDouble("lng");
            dbHelper.insertLocation(address, latitude, longitude);
        }
    }

    public void queryAddress(View view) {
        EditText editTextAddress = findViewById(R.id.editTextAddress);
        TextView textViewLatitude = findViewById(R.id.textViewLatitude);
        TextView textViewLongitude = findViewById(R.id.textViewLongitude);

        String addressQuery = editTextAddress.getText().toString();
        LocationDbHelper dbHelper = new LocationDbHelper(this);

        JSONObject result = dbHelper.queryLocation(addressQuery);

        if (result != null) {
            try {
                double latitude = result.getDouble("latitude");
                double longitude = result.getDouble("longitude");

                textViewLatitude.setText("Latitude: " + latitude);
                textViewLongitude.setText("Longitude: " + longitude);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            textViewLatitude.setText("Latitude: Not Found");
            textViewLongitude.setText("Longitude: Not Found");
        }
        displayAllLocations();
    }

    public void addLocation(View view) {
        String address = editTextAddress.getText().toString();
        String latitudeString = editTextLatitude.getText().toString().trim();
        String longitudeString = editTextLongitude.getText().toString().trim();

        if (latitudeString.isEmpty() || longitudeString.isEmpty()) {
            Toast.makeText(this, "Please enter valid latitude and longitude", Toast.LENGTH_SHORT).show();
            return;
        }

        double latitude, longitude;
        try {
            latitude = Double.parseDouble(latitudeString);
            longitude = Double.parseDouble(longitudeString);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid latitude or longitude format", Toast.LENGTH_SHORT).show();
            return;
        }

        LocationDbHelper dbHelper = new LocationDbHelper(this);
        long result = dbHelper.addLocation(address, latitude, longitude);

        if (result != -1) {
            Toast.makeText(this, "Location added!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Error adding location.", Toast.LENGTH_SHORT).show();
        }
        displayAllLocations();
    }

    public void deleteLocation(View view) {
        String address = editTextAddress.getText().toString();
        LocationDbHelper dbHelper = new LocationDbHelper(this);
        int rowsDeleted = dbHelper.deleteLocationByAddress(address);

        if (rowsDeleted > 0) {
            Toast.makeText(this, "Location deleted!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Error deleting location.", Toast.LENGTH_SHORT).show();
        }
        displayAllLocations();
    }

    public void updateLocation(View view) {
        String address = editTextAddress.getText().toString();
        String latitudeString = editTextLatitude.getText().toString().trim();
        String longitudeString = editTextLongitude.getText().toString().trim();

        if (latitudeString.isEmpty() || longitudeString.isEmpty()) {
            Toast.makeText(this, "Please enter valid latitude and longitude", Toast.LENGTH_SHORT).show();
            return;
        }

        double latitude, longitude;
        try {
            latitude = Double.parseDouble(latitudeString);
            longitude = Double.parseDouble(longitudeString);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid latitude or longitude format", Toast.LENGTH_SHORT).show();
            return;
        }

        LocationDbHelper dbHelper = new LocationDbHelper(this);
        int rowsUpdated = dbHelper.updateLocation(address, address, latitude, longitude); // Using the same address for old and new for this example

        if (rowsUpdated > 0) {
            Toast.makeText(this, "Location updated!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Error updating location.", Toast.LENGTH_SHORT).show();
        }
        displayAllLocations();
    }
}