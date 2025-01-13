package tn.esprit.myapplication;

import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.InputFilter;
import android.text.InputType;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


import java.util.concurrent.Executors;

import tn.esprit.myapplication.database.AppDatabase;
import tn.esprit.myapplication.entity.Vehicle;

public class AddVehicleActivity extends AppCompatActivity {

    private Spinner spinnerVehicleMark;
    private EditText etModelName, etColor, etPlateNumber, etSerialNumber;
    private Button btnSubmit, btnConsultList, btnSelectImage;
    private ImageView ivVehicleImage;
    private Uri imageUri;

    private static final int REQUEST_CAMERA_PERMISSION = 100;

    private final ActivityResultLauncher<Intent> cameraLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    if (imageUri != null) {
                        ivVehicleImage.setImageURI(imageUri);
                    }
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_vehicle);

        // Initialize UI elements
        spinnerVehicleMark = findViewById(R.id.spinner_vehicle_mark);
        etModelName = findViewById(R.id.et_model_name);
        etColor = findViewById(R.id.et_color);
        etPlateNumber = findViewById(R.id.et_plate_number);
        etSerialNumber = findViewById(R.id.et_serial_number);
        btnSubmit = findViewById(R.id.btn_submit);
        btnConsultList = findViewById(R.id.btn_consult_vehicles);
        ivVehicleImage = findViewById(R.id.iv_vehicle_image);
        btnSelectImage = findViewById(R.id.btn_select_image);

        // Set up the spinner with vehicle marks
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.vehicle_marks, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerVehicleMark.setAdapter(adapter);

        // Set input filters for other fields
        etModelName.setFilters(new InputFilter[]{new InputFilter.LengthFilter(10)});
        etPlateNumber.setInputType(InputType.TYPE_CLASS_NUMBER);
        etPlateNumber.setFilters(new InputFilter[]{new InputFilter.LengthFilter(3)});
        etSerialNumber.setInputType(InputType.TYPE_CLASS_NUMBER);
        etSerialNumber.setFilters(new InputFilter[]{new InputFilter.LengthFilter(4)});

        // Request camera permission at runtime
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.CAMERA},
                    REQUEST_CAMERA_PERMISSION);
        }

        // Set click listener for the camera button
        btnSelectImage.setOnClickListener(v -> openCamera());

        // Set click listener for the Submit button
        btnSubmit.setOnClickListener(v -> {
            // Collect data from EditTexts and Spinner
            String vehicleMark = spinnerVehicleMark.getSelectedItem().toString();
            String modelName = etModelName.getText().toString().trim();
            String color = etColor.getText().toString().trim();
            String plateNumber = etPlateNumber.getText().toString().trim();
            String serialNumber = etSerialNumber.getText().toString().trim();

            // Validate inputs
            if (vehicleMark.isEmpty() || modelName.isEmpty() || color.isEmpty() ||
                    plateNumber.isEmpty() || serialNumber.isEmpty()) {
                Toast.makeText(AddVehicleActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            } else {
                // Convert image URI to String (if available)
                String imageUriString = imageUri != null ? imageUri.toString() : "";

                // Insert into Room database asynchronously
                Executors.newSingleThreadExecutor().execute(() -> {
                    AppDatabase db = AppDatabase.getInstance(AddVehicleActivity.this);
                    Vehicle vehicle = new Vehicle(vehicleMark, modelName, color, plateNumber, serialNumber, imageUriString , 1);
                    db.vehicleDao().insert(vehicle);

                    runOnUiThread(() -> Toast.makeText(AddVehicleActivity.this, "Vehicle added successfully!", Toast.LENGTH_SHORT).show());
                });
            }
        });

        // Set click listener for the "Consult Vehicle List" button
        btnConsultList.setOnClickListener(v -> {
            Intent intent = new Intent(AddVehicleActivity.this, VehicleListActivity.class);
            startActivity(intent);
        });
    }

    // Open the camera to take a picture
    private void openCamera() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (cameraIntent.resolveActivity(getPackageManager()) != null) {
            ContentValues values = new ContentValues();
            values.put(MediaStore.Images.Media.TITLE, "New Vehicle Image");
            values.put(MediaStore.Images.Media.DESCRIPTION, "Vehicle Image taken with camera");

            imageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            cameraLauncher.launch(cameraIntent);
        } else {
            Toast.makeText(this, "Camera is not available", Toast.LENGTH_SHORT).show();
        }
    }

    // Handle the result of the permission request
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openCamera();
            } else {
                Toast.makeText(this, "Camera permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
