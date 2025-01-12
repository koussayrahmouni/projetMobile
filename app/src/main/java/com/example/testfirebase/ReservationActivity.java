package com.example.testfirebase;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ReservationActivity extends AppCompatActivity {

    private TextView totalAmountTextView;
    private Spinner timeSpinner;
    private Button paymentButton;
    private ReservationData reservationData; // Instance to hold reservation data
    private static final String TAG = "ReservationActivity"; // For Logcat

    // Firebase Database reference
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation); // Ensure this matches your layout file name

        // Enable back navigation in the ActionBar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        // Initialize views
        totalAmountTextView = findViewById(R.id.totalAmountTextView);
        timeSpinner = findViewById(R.id.timeSpinner);
        paymentButton = findViewById(R.id.paymentButton);

        // Initialize Firebase Database with the specified URL
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://testfirebase-39a4d-default-rtdb.firebaseio.com/");
        databaseReference = database.getReference("Reservations");

        // Retrieve the spot data from the Intent
        String spotName = getIntent().getStringExtra("spot_name");
        int selectedSpotIndex = getIntent().getIntExtra("selected_spot_index", -1);
        TextView spotNameTextView = findViewById(R.id.spotNameTextView);
        spotNameTextView.setText(spotName);

        // Initialize ReservationData with spot name, initial price, and hours
        reservationData = new ReservationData(spotName, 0, 0);
        Log.d(TAG, "ReservationData initialized: " + reservationData);

        // Configure the Spinner with time options
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.time_options, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        timeSpinner.setAdapter(adapter);

        // Spinner item selection listener
        timeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                updateReservation(position); // Update reservation data
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Log.d(TAG, "No time selected");
            }
        });

        // Payment button click listener
        paymentButton.setOnClickListener(v -> {
            Log.d(TAG, "Payment button clicked: " + reservationData);

            // Save the reservation data to Firebase
            saveReservationToFirebase(reservationData);

            // Return the selected spot index to DetailActivity
            Intent resultIntent = new Intent();
            resultIntent.putExtra("reserved_spot_index", selectedSpotIndex);
            setResult(RESULT_OK, resultIntent);
            finish(); // Close the activity
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed(); // Go back to the previous activity
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void updateReservation(int position) {
        int price = 0;
        int hours = 0;

        // Update the reservation based on the selected time
        switch (position) {
            case 0: // 1 hour
                hours = 1;
                price = 10;
                break;
            case 1: // 2 hours
                hours = 2;
                price = 18;
                break;
            case 2: // 3 hours
                hours = 3;
                price = 25;
                break;
            case 3: // 6 hours
                hours = 6;
                price = 40;
                break;
        }

        // Update the ReservationData object
        reservationData.setHours(hours);
        reservationData.setPrice(price);

        // Update the total amount on the UI
        totalAmountTextView.setText("Total Amount: $" + price);
        Log.d(TAG, "Updated reservation: " + reservationData);
    }

    private void saveReservationToFirebase(ReservationData data) {
        String reservationId = databaseReference.push().getKey(); // Generate a unique ID
        if (reservationId != null) {
            // Save reservation data to Firebase
            databaseReference.child(reservationId).setValue(data)
                    .addOnSuccessListener(aVoid -> Log.d(TAG, "Reservation saved successfully"))
                    .addOnFailureListener(e -> Log.e(TAG, "Failed to save reservation: " + e.getMessage()));
        } else {
            Log.e(TAG, "Failed to generate reservation ID");
        }
    }
}
