package tn.esprit.myapplication;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricPrompt;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import tn.esprit.myapplication.database.AppDatabase;
import tn.esprit.myapplication.entity.Wallet;
import tn.esprit.myapplication.directory.WalletDao;

public class WalletActivity extends AppCompatActivity {

    private EditText cardholderNameInput, cardNumberInput, expirationMonthInput, expirationYearInput, cvvInput, balanceToAddInput;
    private Button addMoneyButton;
    private TextView cardholderNameDisplay, cardNumberDisplay, expirationDateDisplay, cvvDisplay, balanceDisplay;
    private int userId;
    private BiometricPrompt biometricPrompt;
    private BiometricPrompt.PromptInfo promptInfo;
    private WalletDao walletDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wallet_activity);

        // Initialize Room Database
        AppDatabase db = AppDatabase.getInstance(this);
        walletDao = db.walletDao();

        // Bind UI components
        cardholderNameInput = findViewById(R.id.editText_cardholder_name);
        cardNumberInput = findViewById(R.id.editText_card_number);
        expirationMonthInput = findViewById(R.id.editText_expiration_month);
        expirationYearInput = findViewById(R.id.editText_expiration_year);
        cvvInput = findViewById(R.id.editText_cvv);
        balanceToAddInput = findViewById(R.id.editText_balance_to_add);
        addMoneyButton = findViewById(R.id.button_add_money);
        userId = getIntent().getIntExtra("USER_ID", -1);

        // Bind TextViews for displaying card info
        cardholderNameDisplay = findViewById(R.id.tvCardHolderLabel);
        cardNumberDisplay = findViewById(R.id.tvCardNumber);
        expirationDateDisplay = findViewById(R.id.tvExpiryLabel);
        cvvDisplay = findViewById(R.id.cvv_display);

        // Set button click listener
        addMoneyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addMoneyToWallet();
            }
        });
// Add listeners to input fields to update the display dynamically
        cardholderNameInput.addTextChangedListener(new SimpleTextWatcher(cardholderNameDisplay));
        cardNumberInput.addTextChangedListener(new SimpleTextWatcher(cardNumberDisplay));
        expirationYearInput.addTextChangedListener(new SimpleTextWatcher(expirationDateDisplay));
        cvvInput.addTextChangedListener(new SimpleTextWatcher(cvvDisplay));
        // Initialize BiometricPrompt for fingerprint authentication
        Executor executor = Executors.newSingleThreadExecutor();
        biometricPrompt = new BiometricPrompt(WalletActivity.this, executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationSucceeded(BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                Log.d("Fingerprint", "Authentication succeeded!");
                // Use runOnUiThread to access UI components after authentication
                runOnUiThread(() -> displayWallet());
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                Log.d("Fingerprint", "Authentication failed!");
                runOnUiThread(() -> Toast.makeText(WalletActivity.this, "Authentication failed", Toast.LENGTH_SHORT).show());
            }
        });

        promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Fingerprint Authentication")
                .setSubtitle("Please authenticate to access your wallet")
                .setNegativeButtonText("Cancel")
                .build();

        biometricPrompt.authenticate(promptInfo);
    }

    private void displayWallet() {
        // Display wallet data from Room database
        // For example, you can load and display the wallet information here
    }

    private void addMoneyToWallet() {
        String cardholderName = cardholderNameInput.getText().toString().trim();
        String cardNumber = cardNumberInput.getText().toString().trim();
        String expirationMonth = expirationMonthInput.getText().toString().trim();
        String expirationYear = expirationYearInput.getText().toString().trim();
        String cvv = cvvInput.getText().toString().trim();
        String balanceToAddStr = balanceToAddInput.getText().toString().trim();

        if (cardholderName.isEmpty() || cardNumber.isEmpty() || expirationMonth.isEmpty() ||
                expirationYear.isEmpty() || cvv.isEmpty() || balanceToAddStr.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        double balanceToAdd;
        try {
            balanceToAdd = Double.parseDouble(balanceToAddStr);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid balance amount", Toast.LENGTH_SHORT).show();
            return;
        }

        if (balanceToAdd <= 0) {
            Toast.makeText(this, "Balance must be greater than 0", Toast.LENGTH_SHORT).show();
            return;
        }

        // Insert/Update wallet into the Room database asynchronously
        Wallet wallet = new Wallet();
        wallet.setCardholderName(cardholderName);
        wallet.setCardNumber(cardNumber);
        wallet.setExpirationMonth(Integer.parseInt(expirationMonth));
        wallet.setExpirationYear(Integer.parseInt(expirationYear));
        wallet.setCvv(cvv);
        wallet.setBalanceToAdd(balanceToAdd);
        wallet.setUserId(userId);

        // Insert into database in the background thread
        new Thread(new Runnable() {
            @Override
            public void run() {
                Wallet existingWallet = walletDao.getWalletsByUserId(userId).isEmpty() ? null : walletDao.getWalletsByUserId(userId).get(0);
                if (existingWallet != null) {
                    existingWallet.setBalanceToAdd(existingWallet.getBalanceToAdd() + balanceToAdd);
                    walletDao.updateWalletBalance(userId, existingWallet.getBalanceToAdd());
                    runOnUiThread(() -> Toast.makeText(WalletActivity.this, "Balance updated successfully!", Toast.LENGTH_SHORT).show());
                } else {
                    walletDao.insertWallet(wallet);
                    runOnUiThread(() -> Toast.makeText(WalletActivity.this, "Wallet created and money added successfully!", Toast.LENGTH_SHORT).show());
                }
            }
        }).start();
    }
    private class SimpleTextWatcher implements android.text.TextWatcher {
        private TextView textView;

        SimpleTextWatcher(TextView textView) {
            this.textView = textView;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {}

        @Override
        public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
            textView.setText(charSequence);
        }

        @Override
        public void afterTextChanged(android.text.Editable editable) {}
    }
}
