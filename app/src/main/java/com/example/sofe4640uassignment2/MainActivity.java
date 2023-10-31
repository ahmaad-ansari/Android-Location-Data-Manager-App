package com.example.sofe4640uassignment2;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int PICK_JSON_FILE_REQUEST_CODE = 123; // Replace with your request code
    private static final String FILENAME = "locations.json";

    private TextView JSONTextView;
    private Uri selectedJsonFileUri; // Declare a variable to store the selected JSON file URI

    private LocationDbHelper dbHelper;
    private GeocodingUtils geocodingUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new LocationDbHelper(this);
        dbHelper.clearDatabase();

        JSONTextView = findViewById(R.id.JSONTextView);

        copyRawResourceToExternalStorage(FILENAME);
        geocodingUtils = new GeocodingUtils(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_JSON_FILE_REQUEST_CODE && resultCode == RESULT_OK) {
            if (data != null) {
                // Get the selected JSON file URI
                selectedJsonFileUri = data.getData();
                // Handle the selected JSON file
                handleJsonFile(selectedJsonFileUri);
            }
        }
    }

    public void uploadFile(View view) {
        // Create an intent to open a file picker
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("application/json"); // Set the MIME type to filter JSON files

        startActivityForResult(intent, PICK_JSON_FILE_REQUEST_CODE);
    }

    private void handleJsonFile(Uri jsonFileUri) {
        try {
            // Read the contents of the JSON file
            InputStream inputStream = getContentResolver().openInputStream(jsonFileUri);
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line).append("\n");
            }
            inputStream.close();

            // Set the JSON content to the TextView
            JSONTextView.setText(stringBuilder.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void generateLocationsDatabase(View view) {
        if (selectedJsonFileUri != null) {
            try {
                // Read the JSON file content
                String jsonContent = readJsonFile(selectedJsonFileUri);

                // Log the JSON content to the console
                Log.d("JSON_CONTENT", jsonContent);

                // Now, parse the JSON content and store locations in the database
                storeLocationsFromJson(jsonContent);
                Toast.makeText(this, "Locations stored in the database.", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                startActivity(intent);
            } catch (JSONException e) {
                e.printStackTrace();

                // Log the error message to the console
                Log.e("JSON_PARSING_ERROR", "Error parsing JSON: " + e.getMessage());

                Toast.makeText(this, "Error parsing JSON.", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "No JSON file selected.", Toast.LENGTH_SHORT).show();
        }
    }

    private void copyRawResourceToExternalStorage(String filename) {
        try {
            // Create a file in the "Downloads" folder on external storage
            File externalFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), filename);

            // Create output stream for the external file
            try (OutputStream os = new FileOutputStream(externalFile);
                 InputStream is = getResources().openRawResource(R.raw.locations)) {
                // Copy the contents from the raw resource to the external file
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = is.read(buffer)) != -1) {
                    os.write(buffer, 0, bytesRead);
                }
            }

            // Now the file is copied to the "Downloads" folder on external storage and is publicly accessible.
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String readJsonFile(Uri jsonFileUri) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            // Open an input stream for the selected JSON file
            InputStream inputStream = getContentResolver().openInputStream(jsonFileUri);

            // Create a reader to read the input stream
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

            // Read the file line by line and append it to the StringBuilder
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }

    private void storeLocationsFromJson(String jsonContent) throws JSONException {
        // Parse the JSON content and store locations in the database
        JSONArray jsonArray = new JSONArray(jsonContent);

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject locationObject = jsonArray.getJSONObject(i);
            double latitude = locationObject.getDouble("lat");
            double longitude = locationObject.getDouble("lng");

            // Get the address for the location using geocoding
            List<String> addresses = geocodingUtils.getAddressesFromLocations(
                    Collections.singletonList(locationObject)
            );

            // Use the first address from the list (assuming one address is returned)
            String address = addresses.isEmpty() ? "" : addresses.get(0);

            // Insert the new location object into the database
            dbHelper.insertLocation(address, latitude, longitude);
        }
    }
}