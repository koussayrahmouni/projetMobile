package tn.esprit.myapplication;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import tn.esprit.myapplication.database.AppDatabase;
import tn.esprit.myapplication.directory.UserDao;
import tn.esprit.myapplication.entity.User;

public class ProfileActivity extends AppCompatActivity {

    private TextView textViewFullName, textViewEmail, textViewDOB, textViewMobile;
    private int userId; // Store the userId passed from LoginActivity
    private Button buttonWallet, buttonTransaction, buttonLogout , buttontocars; // Declare buttons for Wallet, Transaction, and Logout

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Initialize views
        textViewFullName = findViewById(R.id.textView_show_full_name);
        textViewEmail = findViewById(R.id.textView_show_email);
        textViewDOB = findViewById(R.id.textView_show_dob);
        textViewMobile = findViewById(R.id.textView_show_mobile);
        buttonWallet = findViewById(R.id.button_wallet);
        buttonTransaction = findViewById(R.id.button_transaction);
        buttonLogout = findViewById(R.id.button_logout);
        buttontocars = findViewById(R.id.button_gotovoiture);// Initialize the Logout button

        // Get userId and email from the intent
        userId = getIntent().getIntExtra("USER_ID", -1);
        String userEmail = getIntent().getStringExtra("USER_EMAIL");

        if (userEmail == null || userEmail.isEmpty() || userId == -1) {
            Toast.makeText(this, "Invalid user data provided", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            // Fetch user data from SQLite database using userId in a background thread
            new Thread(() -> fetchUserData(userId)).start();
        }

        // Set OnClickListener for the wallet button
        buttonWallet.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, WalletActivity.class);
            intent.putExtra("USER_ID", userId);
            startActivity(intent);
        });

        buttontocars.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, AddVehicleActivity.class);
            intent.putExtra("USER_ID", userId);
            startActivity(intent);
        });

        // Set OnClickListener for the transaction button
        buttonTransaction.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, TransactionActivity.class);
            intent.putExtra("USER_ID", userId);
            startActivity(intent);
        });

        // Set OnClickListener for the Logout button
        buttonLogout.setOnClickListener(v -> logout());
    }

    @SuppressLint("Range")
    private void fetchUserData(int userId) {
        AppDatabase db = AppDatabase.getInstance(this);
        UserDao userDao = db.userDao();

        // Fetch the user by ID
        User user = userDao.getUserById(userId);

        // Run the UI update on the main thread
        new Handler(Looper.getMainLooper()).post(() -> {
            if (user != null) {
                // Populate the UI with user data
                textViewFullName.setText(user.getFullName());
                textViewEmail.setText(user.getEmail());
                textViewDOB.setText(user.getDob());
                textViewMobile.setText(user.getMobile());
            } else {
                Toast.makeText(ProfileActivity.this, "User not found", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Logout logic
    private void logout() {
        // Perform any necessary cleanup, such as clearing session data (if implemented)
        Toast.makeText(this, "Logged out successfully", Toast.LENGTH_SHORT).show();

        // Redirect to LoginActivity
        Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Clear the activity stack
        startActivity(intent);
    }
}
