package tn.esprit.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.concurrent.Executors;

import tn.esprit.myapplication.database.AppDatabase;
import tn.esprit.myapplication.entity.Vehicle;

public class UpdateVehicleActivity extends AppCompatActivity {

    private EditText etVehicleMark, etModelName, etColor, etPlateNumber, etSerialNumber;
    private Button btnUpdate;
    private int vehicleId; // Store vehicle ID as an integer

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_vehicle);

        // Retrieve the data passed via Intent
        Intent intent = getIntent();
        vehicleId = intent.getIntExtra("vehicle_id", -1);  // Use member variable
        String vehicleMark = intent.getStringExtra("vehicle_mark");
        String vehicleModel = intent.getStringExtra("vehicle_model");
        String vehicleColor = intent.getStringExtra("vehicle_color");
        String vehiclePlate = intent.getStringExtra("vehicle_plate");
        String vehicleSerial = intent.getStringExtra("vehicle_serial");

        // Set the EditText fields to the current values
        etVehicleMark = findViewById(R.id.et_vehicle_mark);
        etModelName = findViewById(R.id.et_model_name);
        etColor = findViewById(R.id.et_color);
        etPlateNumber = findViewById(R.id.et_plate_number);
        etSerialNumber = findViewById(R.id.et_serial_number);

        etVehicleMark.setText(vehicleMark);
        etModelName.setText(vehicleModel);
        etColor.setText(vehicleColor);
        etPlateNumber.setText(vehiclePlate);
        etSerialNumber.setText(vehicleSerial);

        // Handle the "Update" button
        btnUpdate = findViewById(R.id.btn_submit);  // Use correct ID
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Retrieve the updated data from the EditTexts
                String updatedMark = etVehicleMark.getText().toString().trim();
                String updatedModel = etModelName.getText().toString().trim();
                String updatedColor = etColor.getText().toString().trim();
                String updatedPlate = etPlateNumber.getText().toString().trim();
                String updatedSerial = etSerialNumber.getText().toString().trim();

                // Validate inputs
                if (updatedMark.isEmpty() || updatedModel.isEmpty() || updatedColor.isEmpty() ||
                        updatedPlate.isEmpty() || updatedSerial.isEmpty()) {
                    Toast.makeText(UpdateVehicleActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                } else {
                    // Update vehicle in the database asynchronously using Executor
                    Executors.newSingleThreadExecutor().execute(() -> {
                        AppDatabase db = AppDatabase.getInstance(UpdateVehicleActivity.this);
                        Vehicle vehicle = db.vehicleDao().getVehicleById(vehicleId); // Get the existing vehicle

                        if (vehicle != null) {
                            // Update the vehicle's details
                            vehicle.setMark(updatedMark);
                            vehicle.setModel(updatedModel);
                            vehicle.setColor(updatedColor);
                            vehicle.setPlate(updatedPlate);
                            vehicle.setSerial(updatedSerial);

                            // Update in the database
                            db.vehicleDao().update(vehicle);

                            runOnUiThread(() -> {
                                Toast.makeText(UpdateVehicleActivity.this, "Vehicle updated successfully!", Toast.LENGTH_SHORT).show();
                                finish();  // Close the activity and return to the previous screen
                            });
                        } else {
                            runOnUiThread(() -> {
                                Toast.makeText(UpdateVehicleActivity.this, "Vehicle not found.", Toast.LENGTH_SHORT).show();
                            });
                        }
                    });
                }
            }
        });
    }
}
