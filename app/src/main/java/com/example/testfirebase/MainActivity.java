package com.example.testfirebase;
import androidx.core.view.GravityCompat;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    private RecyclerView recyclerView;
    private List<DataClass> dataList;
    private MyAdapter adapter;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // Ensure this matches your XML layout file name
        FirebaseApp.initializeApp(this);
        initializeUIComponents();
        initializeFirebaseDatabase();
        loadDataFromFirebase();
    }

    private void initializeUIComponents() {
        Log.d(TAG, "initializeUIComponents: Initializing UI components");

        // Initialize Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Initialize DrawerLayout and NavigationView
        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        // Set up ActionBarDrawerToggle
        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_nav, R.string.close_nav);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // Handle navigation item clicks
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_home:
                        // Handle home action
                        Toast.makeText(MainActivity.this, "Home selected", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.nav_settings:
                        // Handle settings action
                        Toast.makeText(MainActivity.this, "Settings selected", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.nav_about:
                        // Handle about action
                        Toast.makeText(MainActivity.this, "About selected", Toast.LENGTH_SHORT).show();
                        break;
                }
                drawerLayout.closeDrawers(); // Close the drawer after selection
                return true;
            }
        });

        // Initialize RecyclerView
        recyclerView = findViewById(R.id.recyclerView);
        dataList = new ArrayList<>();
        adapter = new MyAdapter(this, dataList);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.setAdapter(adapter);

        Log.d(TAG, "initializeUIComponents: RecyclerView initialized successfully");
    }

    private void initializeFirebaseDatabase() {
        Log.d(TAG, "initializeFirebaseDatabase: Initializing Firebase Database");

        databaseReference = FirebaseDatabase.getInstance().getReference("ParkingSpots");
        Log.d(TAG, "initializeFirebaseDatabase: Firebase Database initialized");
    }

    private void loadDataFromFirebase() {
        Log.d(TAG, "loadDataFromFirebase: Loading data from Firebase");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                dataList.clear(); // Clear existing data in the list before adding new data
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    DataClass spot = snapshot.getValue(DataClass.class); // Assuming you have a DataClass that matches the Firebase structure
                    if (spot != null) {
                        dataList.add(spot); // Add the spot to the list
                    }
                }
                adapter.notifyDataSetChanged(); // Notify the adapter to update the UI
                Log.d(TAG, "loadDataFromFirebase: Data loaded successfully");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "loadDataFromFirebase: Failed to load data - " + databaseError.getMessage());
                Toast.makeText(MainActivity.this, "Failed to load data: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}