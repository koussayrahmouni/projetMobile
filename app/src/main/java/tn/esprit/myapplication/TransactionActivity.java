package tn.esprit.myapplication;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import tn.esprit.myapplication.database.AppDatabase;
import tn.esprit.myapplication.entity.Wallet;
import tn.esprit.myapplication.directory.WalletDao;

public class TransactionActivity extends AppCompatActivity {

    private LinearLayout transactionLayout;
    private int userId;
    private TextView balanceTextView;
    private Button addMoneyButton, useMoneyButton;

    private double totalBalance; // Attribute to store the total balance
    private final ExecutorService executor = Executors.newSingleThreadExecutor();  // Executor to run background tasks

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction);

        // Initialize views
        transactionLayout = findViewById(R.id.transactionLayout);
        balanceTextView = findViewById(R.id.balanceTextView);
        addMoneyButton = findViewById(R.id.addMoneyButton);
        useMoneyButton = findViewById(R.id.useMoneyButton);

        // Get the current user ID (replace with actual user ID from login)
        userId = 1;

        // Display the current balance
        updateBalance();

        // Add money button logic
        addMoneyButton.setOnClickListener(v -> {
            double amountToAdd = 50.0; // Example amount to add (can be fetched from UI input)
            addMoneyToWallet(amountToAdd);
        });

        // Use money button logic
        useMoneyButton.setOnClickListener(v -> {
            double amountToUse = 20.0; // Example amount to use (can be fetched from UI input)
            useMoneyFromWallet(amountToUse);
        });
    }

    // Fetch wallet details and update UI with balance in a background thread
    private void updateBalance() {
        executor.execute(() -> {
            // Get the wallet DAO
            AppDatabase db = AppDatabase.getInstance(this);
            WalletDao walletDao = db.walletDao();

            // Fetch wallet balance for the given user ID
            double balance = walletDao.getWalletBalance(userId);

            // Update UI with the balance using the main thread
            new Handler(Looper.getMainLooper()).post(() -> {
                totalBalance = balance;  // Store the balance
                balanceTextView.setText("Balance: " + totalBalance);
            });
        });
    }

    // Add money to the wallet and update balance
    private void addMoneyToWallet(double amount) {
        executor.execute(() -> {
            // Get the wallet DAO
            AppDatabase db = AppDatabase.getInstance(this);
            WalletDao walletDao = db.walletDao();

            // Get current wallet balance
            double currentBalance = walletDao.getWalletBalance(userId);

            // Add the new amount to the balance
            double newBalance = currentBalance + amount;

            // Update wallet balance in the database
            walletDao.updateWalletBalance(userId, newBalance);

            // Update the UI with the new balance
            new Handler(Looper.getMainLooper()).post(() -> {
                Toast.makeText(this, "Money added successfully!", Toast.LENGTH_SHORT).show();
                updateBalance();  // Refresh balance after update
            });
        });
    }

    // Use money from the wallet and update balance
    private void useMoneyFromWallet(double amount) {
        executor.execute(() -> {
            // Get the wallet DAO
            AppDatabase db = AppDatabase.getInstance(this);
            WalletDao walletDao = db.walletDao();

            // Get current wallet balance
            double currentBalance = walletDao.getWalletBalance(userId);

            // Check if there is enough balance
            if (currentBalance >= amount) {
                // Deduct the amount from the balance
                double newBalance = currentBalance - amount;

                // Update wallet balance in the database
                walletDao.updateWalletBalance(userId, newBalance);

                // Update the UI with the new balance
                new Handler(Looper.getMainLooper()).post(() -> {
                    Toast.makeText(this, "Money used successfully!", Toast.LENGTH_SHORT).show();
                    updateBalance();  // Refresh balance after update
                });
            } else {
                // Not enough balance
                new Handler(Looper.getMainLooper()).post(() -> {
                    Toast.makeText(this, "Insufficient balance!", Toast.LENGTH_SHORT).show();
                });
            }
        });
    }
}
