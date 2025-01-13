package tn.esprit.myapplication;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;

import tn.esprit.myapplication.database.AppDatabase;
import tn.esprit.myapplication.directory.UserDao;

import android.content.Intent;
import java.util.concurrent.Executor;

public class LoginActivity extends AppCompatActivity {
    public static int Useridglobal;
    private EditText editText_login_email, editText_login_pwd;
    private Button button_login;
    private BiometricPrompt biometricPrompt;
    private BiometricPrompt.PromptInfo promptInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize views
        editText_login_email = findViewById(R.id.editText_login_email);
        editText_login_pwd = findViewById(R.id.editText_login_pwd);
        button_login = findViewById(R.id.button_login);

        // Initialize BiometricPrompt
        initBiometricPrompt();

        // Button click listener
        button_login.setOnClickListener(v -> {
            String email = editText_login_email.getText().toString();
            String password = editText_login_pwd.getText().toString();

            if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                Toast.makeText(LoginActivity.this, "Please fill in both fields", Toast.LENGTH_SHORT).show();
            } else {
                // Verify credentials with SQLite for email/password login using a new Thread
                new Thread(() -> {
                    if (checkLoginCredentials(email, password)) {
                        runOnUiThread(() -> {
                            // Login successful
                            Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                            // After successful login, show biometric prompt for Face ID authentication
                            authenticateWithFaceID(email);
                        });
                    } else {
                        runOnUiThread(() -> {
                            // Invalid credentials
                            Toast.makeText(LoginActivity.this, "Invalid Email or Password", Toast.LENGTH_SHORT).show();
                        });
                    }
                }).start();
            }
        });
    }

    // Check user credentials in the database (email/password login)
    private boolean checkLoginCredentials(String email, String password) {
        AppDatabase db = AppDatabase.getInstance(this);
        UserDao userDao = db.userDao();

        // Use the UserDao method to check login credentials
        int count = userDao.checkLoginCredentials(email, password);

        return count > 0;
    }

    // Initialize BiometricPrompt
    private void initBiometricPrompt() {
        Executor executor = ContextCompat.getMainExecutor(this);
        biometricPrompt = new BiometricPrompt(LoginActivity.this, executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationSucceeded(BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                // Authentication succeeded, proceed to ProfileActivity
                String email = editText_login_email.getText().toString();
                new Thread(() -> {
                    int userId = getUserIdByEmail(email);
                    Useridglobal = userId;
                    runOnUiThread(() -> {
                        Intent intent = new Intent(LoginActivity.this, ProfileActivity.class);
                        intent.putExtra("USER_ID", userId);
                        intent.putExtra("USER_EMAIL", email);
                        startActivity(intent);
                    });
                }).start();
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                // Handle authentication failure
                Toast.makeText(LoginActivity.this, "Authentication failed!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAuthenticationError(int errorCode, CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                // Handle authentication error
                Toast.makeText(LoginActivity.this, "Authentication error: " + errString, Toast.LENGTH_SHORT).show();
            }
        });

        // Setup biometric prompt info
        promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Face ID Authentication")
                .setSubtitle("Please authenticate with your face")
                .setNegativeButtonText("Cancel")
                .build();
    }

    // Trigger Face ID authentication
    private void authenticateWithFaceID(String email) {
        biometricPrompt.authenticate(promptInfo);
    }

    // Retrieve user ID from the database based on email (for ProfileActivity)
    @SuppressLint("Range")
    private int getUserIdByEmail(String email) {
        AppDatabase db = AppDatabase.getInstance(this);
        UserDao userDao = db.userDao();
        return userDao.getUserIdByEmail(email);
    }
}
