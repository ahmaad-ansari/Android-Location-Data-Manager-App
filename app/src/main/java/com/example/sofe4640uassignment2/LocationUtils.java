package com.example.sofe4640uassignment2;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class LocationUtils {

    private Context context;

    public LocationUtils(Context context) {
        this.context = context;
    }

    public List<JSONObject> getLocationsFromJson() {
        List<JSONObject> locationList = new ArrayList<>();
        try {
            InputStream inputStream = context.getResources().openRawResource(R.raw.locations);
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder jsonBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                jsonBuilder.append(line);
            }
            JSONArray jsonArray = new JSONArray(jsonBuilder.toString());
            for (int i = 0; i < jsonArray.length(); i++) {
                locationList.add(jsonArray.getJSONObject(i));
            }
            reader.close();
            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return locationList;
    }
}
