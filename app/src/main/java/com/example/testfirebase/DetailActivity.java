package com.example.testfirebase;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class DetailActivity extends AppCompatActivity {

    // Define ImageV..
    // iews for parking spots
    private ImageView[] spots = new ImageView[6];
    private DataClass[] dataClasses; // Array to hold DataClass objects for each spot
    private static final int REQUEST_CODE = 1; // Define a request code for onActivityResult

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail); // Ensure this matches your layout file name

        // Enable back navigation in the ActionBar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        // Initialize DataClass objects for each parking spot
        dataClasses = new DataClass[]{
                new DataClass("Spot 1", true, R.drawable.ic_parking, "spot1_id", 10),
                new DataClass("Spot 2", false, R.drawable.car, "spot2_id", 15),
                new DataClass("Spot 3", true, R.drawable.ic_parking, "spot3_id", 12),
                new DataClass("Spot 4", false, R.drawable.car, "spot4_id", 8),
                new DataClass("Spot 5", true, R.drawable.ic_parking, "spot5_id", 20),
                new DataClass("Spot 6", false, R.drawable.car, "spot6_id", 18)
        };

        // Link ImageViews with layout
        spots[0] = findViewById(R.id.spot1);
        spots[1] = findViewById(R.id.spot2);
        spots[2] = findViewById(R.id.spot3);
        spots[3] = findViewById(R.id.spot4);
        spots[4] = findViewById(R.id.spot5);
        spots[5] = findViewById(R.id.spot6);

        // Update UI based on availability
        for (int i = 0; i < spots.length; i++) {
            updateParkingSpot(spots[i], dataClasses[i].isAvailable());
            final int index = i; // Capture the index for the click listener
            spots[i].setOnClickListener(v -> {
                if (dataClasses[index].isAvailable()) {
                    // Navigate to ReservationActivity if the spot is available
                    Intent intent = new Intent(DetailActivity.this, ReservationActivity.class);
                    intent.putExtra("spot_data", dataClasses[index]); // Pass the DataClass object
                    intent.putExtra("selected_spot_index", index); // Pass the selected spot index
                    startActivityForResult(intent, REQUEST_CODE); // Use startActivityForResult
                } else {
                    Toast.makeText(DetailActivity.this, "This spot is occupied", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            // Navigate back to the previous activity
            onBackPressed(); // This automatically takes you to the previous activity
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            // Get the index of the reserved spot
            int reservedIndex = data.getIntExtra("reserved_spot_index", -1);
            if (reservedIndex != -1) { // Mark the spot as unavailable
                dataClasses[reservedIndex].setAvailable(false);
                updateParkingSpot(spots[reservedIndex], false); // Update UI
                Toast.makeText(this, "Spot " + (reservedIndex + 1) + " has been reserved.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void updateParkingSpot(ImageView spot, boolean isAvailable) {
        if (isAvailable) {
            spot.setImageResource(R.drawable.ic_parking); // Set the image resource for available spots
        } else {
            spot.setImageResource(R.drawable.car); // Set the image resource for occupied spots
        }
    }
}
