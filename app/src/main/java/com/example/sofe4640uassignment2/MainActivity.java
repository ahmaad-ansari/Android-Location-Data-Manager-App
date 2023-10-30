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

// MainActivity is the main activity of the application
public class MainActivity extends AppCompatActivity {

    // UI Elements
    private TextInputEditText editTextAddress;
    private TextInputEditText editTextLatitude;
    private TextInputEditText editTextLongitude;

    // List and Adapter for displaying location information
    private ArrayAdapter<String> adapter;
    private List<String> locationList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize UI elements and adapter
        editTextAddress = findViewById(R.id.editTextAddress);
        editTextLatitude = findViewById(R.id.editTextLatitude);
        editTextLongitude = findViewById(R.id.editTextLongitude);

        ListView listViewLocations = findViewById(R.id.listViewLocations);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, locationList);
        listViewLocations.setAdapter(adapter);

        // Display all stored locations
        displayAllLocations();

        // Try to store locations from a JSON resource file
        try {
            storeLocations();
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    // Display all stored locations in the ListView
    private void displayAllLocations() {
        LocationDbHelper dbHelper = new LocationDbHelper(this);

        // Retrieve a list of location information from the database
        List<JSONObject> locations = dbHelper.getAllLocations();

        locationList.clear();
        for (JSONObject location : locations) {
            try {
                String address = location.getString("address");
                double latitude = location.getDouble("latitude");
                double longitude = location.getDouble("longitude");

                // Create a formatted string with address, latitude, and longitude
                String locationInfo = "Address: " + address + "\nLatitude: " + latitude + ", Longitude: " + longitude;
                locationList.add(locationInfo);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        adapter.notifyDataSetChanged();
    }

    // Store locations from a JSON resource file into the database
    private void storeLocations() throws JSONException {
        LocationUtils locationUtils = new LocationUtils(this);

        // Retrieve a list of location data from a JSON resource file
        List<JSONObject> locations = locationUtils.getLocationsFromJson();

        GeocodingUtils geocodingUtils = new GeocodingUtils(this);

        // Retrieve a list of addresses for the locations
        List<String> addresses = geocodingUtils.getAddressesFromLocations(locations);

        LocationDbHelper dbHelper = new LocationDbHelper(this);

        for (int i = 0; i < addresses.size(); i++) {
            String address = addresses.get(i);
            double latitude = locations.get(i).getDouble("lat");
            double longitude = locations.get(i).getDouble("lng");

            // Insert each location into the database
            dbHelper.insertLocation(address, latitude, longitude);
        }
    }

    // Query and display latitude and longitude for a given address
    public void queryAddress(View view) {
        EditText editTextAddress = findViewById(R.id.editTextAddress);
        TextView textViewLatitude = findViewById(R.id.textViewLatitude);
        TextView textViewLongitude = findViewById(R.id.textViewLongitude);

        String addressQuery = editTextAddress.getText().toString();
        LocationDbHelper dbHelper = new LocationDbHelper(this);

        // Query the database for location data based on the provided address
        JSONObject result = dbHelper.queryLocation(addressQuery);

        if (result != null) {
            try {
                double latitude = result.getDouble("latitude");
                double longitude = result.getDouble("longitude");

                // Display the latitude and longitude
                textViewLatitude.setText("Latitude: " + latitude);
                textViewLongitude.setText("Longitude: " + longitude);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            // Display a message indicating that the address was not found
            textViewLatitude.setText("Latitude: Not Found");
            textViewLongitude.setText("Longitude: Not Found");
        }
        displayAllLocations();
    }

    // Add a new location to the database
    public void addLocation(View view) {
        String address = editTextAddress.getText().toString();
        String latitudeString = editTextLatitude.getText().toString().trim();
        String longitudeString = editTextLongitude.getText().toString().trim();

        if (latitudeString.isEmpty() || longitudeString.isEmpty()) {
            // Display a message if latitude or longitude is missing
            Toast.makeText(this, "Please enter valid latitude and longitude", Toast.LENGTH_SHORT).show();
            return;
        }

        double latitude, longitude;
        try {
            // Parse latitude and longitude values
            latitude = Double.parseDouble(latitudeString);
            longitude = Double.parseDouble(longitudeString);
        } catch (NumberFormatException e) {
            // Display a message if latitude or longitude format is invalid
            Toast.makeText(this, "Invalid latitude or longitude format", Toast.LENGTH_SHORT).show();
            return;
        }

        LocationDbHelper dbHelper = new LocationDbHelper(this);

        // Add the new location to the database
        long result = dbHelper.addLocation(address, latitude, longitude);

        if (result != -1) {
            // Display a message indicating a successful addition
            Toast.makeText(this, "Location added!", Toast.LENGTH_SHORT).show();
        } else {
            // Display a message indicating an error
            Toast.makeText(this, "Error adding location.", Toast.LENGTH_SHORT).show();
        }
        displayAllLocations();
    }

    // Delete a location from the database
    public void deleteLocation(View view) {
        String address = editTextAddress.getText().toString();
        LocationDbHelper dbHelper = new LocationDbHelper(this);

        // Delete the location from the database
        int rowsDeleted = dbHelper.deleteLocationByAddress(address);

        if (rowsDeleted > 0) {
            // Display a message indicating a successful deletion
            Toast.makeText(this, "Location deleted!", Toast.LENGTH_SHORT).show();
        } else {
            // Display a message indicating an error
            Toast.makeText(this, "Error deleting location.", Toast.LENGTH_SHORT).show();
        }
        displayAllLocations();
    }

    // Update an existing location in the database
    public void updateLocation(View view) {
        String address = editTextAddress.getText().toString();
        String latitudeString = editTextLatitude.getText().toString().trim();
        String longitudeString = editTextLongitude.getText().toString().trim();

        if (latitudeString.isEmpty() || longitudeString.isEmpty()) {
            // Display a message if latitude or longitude is missing
            Toast.makeText(this, "Please enter valid latitude and longitude", Toast.LENGTH_SHORT).show();
            return;
        }

        double latitude, longitude;
        try {
            // Parse latitude and longitude values
            latitude = Double.parseDouble(latitudeString);
            longitude = Double.parseDouble(longitudeString);
        } catch (NumberFormatException e) {
            // Display a message if latitude or longitude format is invalid
            Toast.makeText(this, "Invalid latitude or longitude format", Toast.LENGTH_SHORT).show();
            return;
        }

        LocationDbHelper dbHelper = new LocationDbHelper(this);

        // Update the location in the database
        int rowsUpdated = dbHelper.updateLocation(address, address, latitude, longitude); // Using the same address for old and new for

        if (rowsUpdated > 0) {
            Toast.makeText(this, "Location updated!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Error updating location.", Toast.LENGTH_SHORT).show();
        }
        displayAllLocations();
    }
}