package tn.esprit.myapplication;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

import tn.esprit.myapplication.database.AppDatabase;
import tn.esprit.myapplication.directory.UserDao;
import tn.esprit.myapplication.entity.User;

public class RegisterActivity extends AppCompatActivity {

    private EditText editText_register_full_name, editText_register_email, editText_register_dob, editText_register_mobile, editText_register_password;
    private ProgressBar progressBar;
    private DatePickerDialog picker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getSupportActionBar().setTitle("Register");

        // Initialize views
        editText_register_full_name = findViewById(R.id.editText_register_full_name);
        editText_register_email = findViewById(R.id.editText_register_email);
        editText_register_dob = findViewById(R.id.editText_register_dob);
        editText_register_mobile = findViewById(R.id.editText_register_mobile);
        editText_register_password = findViewById(R.id.editText_register_password);
        progressBar = findViewById(R.id.progressBar); // Initialize ProgressBar

        editText_register_dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar = Calendar.getInstance();
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);
                picker = new DatePickerDialog(RegisterActivity.this, new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        editText_register_dob.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
                    }
                }, year, month, day);
                picker.show();
            }
        });

        Button buttonRegister = findViewById(R.id.button_register);
        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fullName = editText_register_full_name.getText().toString();
                String email = editText_register_email.getText().toString();
                String dob = editText_register_dob.getText().toString();
                String mobile = editText_register_mobile.getText().toString();
                String password = editText_register_password.getText().toString();

                if (TextUtils.isEmpty(fullName)) {
                    Toast.makeText(RegisterActivity.this, "Please enter your full name", Toast.LENGTH_SHORT).show();
                    editText_register_full_name.setError("is required");
                    editText_register_full_name.requestFocus();
                } else if (TextUtils.isEmpty(email)) {
                    Toast.makeText(RegisterActivity.this, "Please enter your email", Toast.LENGTH_SHORT).show();
                    editText_register_email.setError("is required");
                    editText_register_email.requestFocus();
                } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    Toast.makeText(RegisterActivity.this, "Please enter a valid email", Toast.LENGTH_SHORT).show();
                    editText_register_email.setError("is required");
                    editText_register_email.requestFocus();
                } else if (TextUtils.isEmpty(dob)) {
                    Toast.makeText(RegisterActivity.this, "Please enter your birthday", Toast.LENGTH_SHORT).show();
                    editText_register_dob.setError("is required");
                    editText_register_dob.requestFocus();
                } else if (TextUtils.isEmpty(mobile)) {
                    Toast.makeText(RegisterActivity.this, "Please enter your mobile number", Toast.LENGTH_SHORT).show();
                    editText_register_mobile.setError("is required");
                    editText_register_mobile.requestFocus();
                } else if (mobile.length() != 8) {
                    Toast.makeText(RegisterActivity.this, "Please enter a valid mobile number", Toast.LENGTH_SHORT).show();
                    editText_register_mobile.setError("is required");
                    editText_register_mobile.requestFocus();
                } else if (password.length() < 6) {
                    Toast.makeText(RegisterActivity.this, "Please enter a valid password", Toast.LENGTH_SHORT).show();
                    editText_register_password.setError("is required");
                    editText_register_password.requestFocus();
                } else {
                    progressBar.setVisibility(View.VISIBLE); // Show ProgressBar
                    registerUser(fullName, email, dob, mobile, password);
                }
            }
        });
    }

    private void registerUser(String fullName, String email, String dob, String mobile, String password) {
        User user = new User();
        user.setFullName(fullName);
        user.setEmail(email);
        user.setDob(dob);
        user.setMobile(mobile);
        user.setPassword(password);

        new Thread(new Runnable() {
            @Override
            public void run() {
                AppDatabase db = AppDatabase.getInstance(RegisterActivity.this);
                UserDao userDao = db.userDao();

                long result = userDao.insertUser(user);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.GONE); // Hide ProgressBar
                        if (result == -1) {
                            Toast.makeText(RegisterActivity.this, "Registration Failed", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(RegisterActivity.this, "Registration Successful", Toast.LENGTH_SHORT).show();
                            Log.d("RegisterActivity", "Inserted user ID: " + result);
                        }
                    }
                });
            }
        }).start();
    }
}
