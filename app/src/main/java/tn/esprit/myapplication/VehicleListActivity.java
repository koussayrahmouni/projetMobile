package tn.esprit.myapplication;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import tn.esprit.myapplication.database.AppDatabase;
import tn.esprit.myapplication.entity.Vehicle;

public class VehicleListActivity extends AppCompatActivity {

    private EditText etSearch;
    private ListView lvVehicleList;
    private Button btnReturn;
    private AppDatabase appDatabase;
    private VehicleAdapter vehicleAdapter;
    private ArrayList<Vehicle> vehicleList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle_list);

        // Initialize UI elements
        etSearch = findViewById(R.id.et_search);
        lvVehicleList = findViewById(R.id.lv_vehicle_list);
        btnReturn = findViewById(R.id.btn_return);

        // Initialize Room Database
        appDatabase = AppDatabase.getInstance(this);

        // Load vehicle data from the database
        loadVehicleList();

        // Set click listener for the Return button
        btnReturn.setOnClickListener(v -> {
            Intent intent = new Intent(VehicleListActivity.this, AddVehicleActivity.class);
            startActivity(intent);
        });

        // Set up search functionality
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                vehicleAdapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void loadVehicleList() {
        // Fetch vehicles from the database asynchronously
        appDatabase.vehicleDao().getAllVehicles(LoginActivity.Useridglobal).observe(this, new Observer<List<Vehicle>>() {
            @Override
            public void onChanged(List<Vehicle> vehicles) {
                vehicleList = new ArrayList<>(vehicles);
                vehicleAdapter = new VehicleAdapter(VehicleListActivity.this, vehicleList);
                lvVehicleList.setAdapter(vehicleAdapter);
            }
        });
    }

    private class VehicleAdapter extends ArrayAdapter<Vehicle> {
        private final ArrayList<Vehicle> originalList;
        private final ArrayList<Vehicle> filteredList;
        private final Context context;

        public VehicleAdapter(Context context, ArrayList<Vehicle> vehicles) {
            super(context, R.layout.list_item_vehicle, vehicles);
            this.context = context;
            this.originalList = new ArrayList<>(vehicles);
            this.filteredList = new ArrayList<>(vehicles);
        }

        @Override
        public int getCount() {
            return filteredList.size();
        }

        @Override
        public Vehicle getItem(int position) {
            return filteredList.get(position);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                LayoutInflater inflater = LayoutInflater.from(context);
                convertView = inflater.inflate(R.layout.list_item_vehicle, parent, false);
            }

            // Get references to the UI elements
            TextView tvVehicleMark = convertView.findViewById(R.id.tv_vehicle_mark);
            TextView tvVehicleModel = convertView.findViewById(R.id.tv_vehicle_model);
            TextView tvVehicleColor = convertView.findViewById(R.id.tv_vehicle_color);
            TextView tvVehiclePlate = convertView.findViewById(R.id.tv_vehicle_plate);
            TextView tvVehicleSerial = convertView.findViewById(R.id.tv_vehicle_serial);
            Button btnUpdate = convertView.findViewById(R.id.btn_update);
            Button btnDelete = convertView.findViewById(R.id.btn_delete);
            ImageView ivVehicleImage = convertView.findViewById(R.id.iv_vehicle_image);

            final Vehicle vehicle = filteredList.get(position);

            // Set the vehicle details to respective TextViews
            tvVehicleMark.setText("Mark: " + vehicle.getMark());
            tvVehicleModel.setText("Model: " + vehicle.getModel());
            tvVehicleColor.setText("Color: " + vehicle.getColor());
            tvVehiclePlate.setText("Plate: " + vehicle.getPlate());
            tvVehicleSerial.setText("Serial: " + vehicle.getSerial());

            // Load the vehicle image using Glide
            if (vehicle.getImageUri() != null && !vehicle.getImageUri().isEmpty()) {
                Glide.with(context)
                        .load(vehicle.getImageUri())
                        .into(ivVehicleImage);
            } else {
                ivVehicleImage.setImageResource(R.drawable.default_image);
            }

            // Set up the Update button
            btnUpdate.setOnClickListener(v -> {
                Intent intent = new Intent(context, UpdateVehicleActivity.class);
                intent.putExtra("vehicle_id", vehicle.getId());
                intent.putExtra("vehicle_mark", vehicle.getMark());
                intent.putExtra("vehicle_model", vehicle.getModel());
                intent.putExtra("vehicle_color", vehicle.getColor());
                intent.putExtra("vehicle_plate", vehicle.getPlate());
                intent.putExtra("vehicle_serial", vehicle.getSerial());
                context.startActivity(intent);
            });

            // Set up the Delete button
            btnDelete.setOnClickListener(v -> {
                // Delete vehicle from the Room database
                new Thread(() -> {
                    appDatabase.vehicleDao().delete(vehicle);
                    runOnUiThread(() -> {
                        Toast.makeText(context, "Vehicle deleted successfully!", Toast.LENGTH_SHORT).show();
                        loadVehicleList(); // Refresh the list
                    });
                }).start();
            });

            return convertView;
        }

        @Override
        public android.widget.Filter getFilter() {
            return new android.widget.Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence constraint) {
                    FilterResults results = new FilterResults();
                    if (constraint == null || constraint.length() == 0) {
                        results.values = originalList;
                        results.count = originalList.size();
                    } else {
                        ArrayList<Vehicle> filteredResults = new ArrayList<>();
                        for (Vehicle vehicle : originalList) {
                            if (vehicle.getMark().toLowerCase().contains(constraint.toString().toLowerCase()) ||
                                    vehicle.getModel().toLowerCase().contains(constraint.toString().toLowerCase()) ||
                                    vehicle.getPlate().toLowerCase().contains(constraint.toString().toLowerCase())) {
                                filteredResults.add(vehicle);
                            }
                        }
                        results.values = filteredResults;
                        results.count = filteredResults.size();
                    }
                    return results;
                }

                @Override
                protected void publishResults(CharSequence constraint, FilterResults results) {
                    filteredList.clear();
                    filteredList.addAll((ArrayList<Vehicle>) results.values);
                    notifyDataSetChanged();
                }
            };
        }
    }
}
