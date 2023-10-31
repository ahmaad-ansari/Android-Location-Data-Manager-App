package com.example.sofe4640uassignment2;

import android.content.Context;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

// LocationUtils is a utility class for reading JSON data from a raw resource file
public class LocationUtils {

    private Context context;

    // Constructor for LocationUtils
    public LocationUtils(Context context) {
        this.context = context;
    }

    // Reads JSON data from a raw resource file and returns a list of JSON objects
    public List<JSONObject> getLocationsFromJson(String jsonContent) {
        List<JSONObject> locationList = new ArrayList<>();
        try {
            // Open the raw resource file "locations.json" and obtain an InputStream
            InputStream inputStream = context.getResources().openRawResource(R.raw.locations);

            // Create a BufferedReader to read the data
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

            // StringBuilder to construct the JSON data
            StringBuilder jsonBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                jsonBuilder.append(line);
            }

            // Parse the JSON data into a JSONArray
            JSONArray jsonArray = new JSONArray(jsonBuilder.toString());

            // Iterate through the JSON array and add each JSON object to the list
            for (int i = 0; i < jsonArray.length(); i++) {
                locationList.add(jsonArray.getJSONObject(i));
            }

            // Close the reader and the input stream
            reader.close();
            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Return the list of JSON objects
        return locationList;
    }
}
