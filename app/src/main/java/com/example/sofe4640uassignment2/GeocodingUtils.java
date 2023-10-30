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

    public GeocodingUtils(Context context) {
        this.context = context;
        this.geocoder = new Geocoder(context, Locale.getDefault());
    }

    public List<String> getAddressesFromLocations(List<JSONObject> locations) {
        List<String> addresses = new ArrayList<>();

        for (JSONObject location : locations) {
            try {
                double lat = location.getDouble("lat");
                double lng = location.getDouble("lng");

                List<Address> addressList = geocoder.getFromLocation(lat, lng, 1);
                if (!addressList.isEmpty()) {
                    Address address = addressList.get(0);
                    addresses.add(address.getAddressLine(0));
                }
            } catch (IOException e) {
                e.printStackTrace();
                addresses.add("Error fetching address");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return addresses;
    }
}
