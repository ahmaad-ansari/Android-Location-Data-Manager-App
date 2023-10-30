package com.example.sofe4640uassignment2;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class GeocodingUtils {

    private Context context;
    private Geocoder geocoder;

    // Constructor for the GeocodingUtils class
    public GeocodingUtils(Context context) {
        this.context = context;
        this.geocoder = new Geocoder(context, Locale.getDefault());
    }

    // Method to get addresses from a list of JSON objects representing locations
    public List<String> getAddressesFromLocations(List<JSONObject> locations) {
        // Create a list to store the resulting addresses
        List<String> addresses = new ArrayList<>();

        // Iterate through the list of JSON objects representing locations
        for (JSONObject location : locations) {
            try {
                // Extract latitude and longitude from the JSON object
                double lat = location.getDouble("lat");
                double lng = location.getDouble("lng");

                // Use the geocoder to get the address for the provided latitude and longitude
                List<Address> addressList = geocoder.getFromLocation(lat, lng, 1);

                // If there is at least one address found
                if (!addressList.isEmpty()) {
                    // Get the first address from the list
                    Address address = addressList.get(0);

                    // Add the first address line (typically the full address) to the result list
                    addresses.add(address.getAddressLine(0));
                }
            } catch (IOException e) {
                // Handle IO exceptions, typically related to geocoding
                e.printStackTrace();
                addresses.add("Error fetching address");
            } catch (Exception e) {
                // Handle other exceptions that might occur during processing
                e.printStackTrace();
            }
        }

        // Return the list of addresses
        return addresses;
    }
}
