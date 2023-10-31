# Android Location Data Manager App

## Introduction

The Android Location Data Manager App is a comprehensive solution for managing and querying location data. Users can input location data either manually or via a JSON format, offering flexibility and efficiency. Built upon SQLite, this app guarantees quick and reliable data management processes.

## Screenshots

Below are screenshots of the Android Location Data Manager app in action:

<div style="display: flex; justify-content: space-between;">
  <img src="https://github.com/ahmaad-ansari/SOFE4640U-Assignment-2/assets/88805493/4a932ad1-98cb-4a46-8a02-dadce16cda98" alt="App Screenshot2" width="300">
  <img src="https://github.com/ahmaad-ansari/SOFE4640U-Assignment-2/assets/88805493/6e76d819-61d9-41bb-a44a-1b16e1709027" alt="App Screenshot3" width="300">
  <img src="https://github.com/ahmaad-ansari/SOFE4640U-Assignment-2/assets/88805493/6109e7e6-5133-47ac-8073-e604222cd3d4" alt="App Screenshot4" width="300">
  <img src="https://github.com/ahmaad-ansari/SOFE4640U-Assignment-2/assets/88805493/b35b5168-dffb-4f16-8d56-27ab1e12a076" alt="App Screenshot1" width="300">
</div>

## Features

- Intuitive interface for manual data input and JSON imports.
- SQLite database integration ensuring efficient storage and query operations.
- Seamlessly transfer location data between user interface and backend through interactive buttons and views.

## Source Code Structure

The app's source code is logically organized into various classes, with each having its dedicated responsibility:

1. **MainActivity**: Manages the main user interface, allowing users to input data or supply JSON for location data input.
2. **DatabaseHelper**: A utility class providing methods for SQLite operations, including insertion, update, deletion, and querying of location data.
3. **LocationData**: Represents the object model for location data, encapsulating attributes such as address, latitude, and longitude.
4. **JsonParser**: Responsible for parsing the JSON input and converting it to `LocationData` objects.

## How the App Works

- In `MainActivity`, users can manually input location data or provide a JSON.
- After data input, pressing the appropriate button triggers data storage into the SQLite database or a JSON parsing operation.
- Data management operations are facilitated by `DatabaseHelper`, which interfaces with the SQLite database.
- Location data entries can be queried, with results dynamically displayed in the app's UI.
- JSON data inputs are processed by `JsonParser`, creating `LocationData` objects for storage in the database.

## Working with SQLite Database

SQLite provides a lightweight and efficient way to store and manage location data. `DatabaseHelper` serves as the primary interface for database operations, using SQLite queries to insert, delete, update, or fetch data. This class's methods ensure data integrity and quick retrieval, enhancing the app's overall performance.

## Using the LocationData Object

Each location entry is represented as a `LocationData` object, encapsulating information such as address, latitude, and longitude. This object-oriented approach streamlines data handling, ensuring that each data entry is consistent and well-structured.

## Input Validation and Error Handling

This app incorporates meticulous input validation to enhance user experience:

- **Empty or Invalid Inputs**: Checks are in place to ensure fields are not left empty and that inputs, especially for latitude and longitude, are valid numbers.
- **JSON Parsing**: Errors in JSON formatting or content are gracefully caught, and users are informed of any discrepancies.

## Getting Started

To explore the Android Location Data Manager App:

1. Clone or download the repository to your machine.
2. Open the project in Android Studio.
3. Compile and run the app on an Android emulator or a physical device.
