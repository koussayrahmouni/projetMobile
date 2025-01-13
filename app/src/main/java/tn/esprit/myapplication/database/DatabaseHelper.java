package tn.esprit.myapplication.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Database Info
    public static final String DATABASE_NAME = "mobile";
    public static final int DATABASE_VERSION = 3; // Incremented database version to include transactions

    // Users Table Info
    public static final String TABLE_USERS = "users";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_FULL_NAME = "full_name";
    public static final String COLUMN_EMAIL = "email";
    public static final String COLUMN_DOB = "dob";
    public static final String COLUMN_MOBILE = "mobile";
    public static final String COLUMN_PASSWORD = "password";

    // Wallet Table Info
    public static final String TABLE_WALLET = "wallet";
    public static final String COLUMN_WALLET_ID = "wallet_id";
    public static final String COLUMN_CARDHOLDER_NAME = "cardholder_name";
    public static final String COLUMN_CARD_NUMBER = "card_number";
    public static final String COLUMN_EXPIRATION_MONTH = "expiration_month";
    public static final String COLUMN_EXPIRATION_YEAR = "expiration_year";
    public static final String COLUMN_CVV = "cvv";
    public static final String COLUMN_BALANCE_TO_ADD = "balance_to_add";
    public static final String COLUMN_USER_ID = "user_id"; // Foreign key

    // Transactions Table Info
    public static final String TABLE_TRANSACTIONS = "transactions";
    public static final String COLUMN_TRANSACTION_ID = "transaction_id";
    public static final String COLUMN_TRANSACTION_TYPE = "transaction_type"; // "add" or "use"
    public static final String COLUMN_TRANSACTION_AMOUNT = "transaction_amount";
    public static final String COLUMN_TRANSACTION_DATE = "transaction_date"; // Timestamp
    public static final String COLUMN_TRANSACTION_USER_ID = "user_id"; // Foreign key

    // Constructor
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create Users Table
        String CREATE_USERS_TABLE = "CREATE TABLE " + TABLE_USERS + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_FULL_NAME + " TEXT,"
                + COLUMN_EMAIL + " TEXT,"
                + COLUMN_DOB + " TEXT,"
                + COLUMN_MOBILE + " TEXT,"
                + COLUMN_PASSWORD + " TEXT"
                + ")";
        db.execSQL(CREATE_USERS_TABLE);

        // Create Wallet Table
        String CREATE_WALLET_TABLE = "CREATE TABLE " + TABLE_WALLET + "("
                + COLUMN_WALLET_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_CARDHOLDER_NAME + " TEXT,"
                + COLUMN_CARD_NUMBER + " TEXT,"
                + COLUMN_EXPIRATION_MONTH + " TEXT,"
                + COLUMN_EXPIRATION_YEAR + " TEXT,"
                + COLUMN_CVV + " TEXT,"
                + COLUMN_BALANCE_TO_ADD + " REAL,"
                + COLUMN_USER_ID + " INTEGER,"
                + "FOREIGN KEY(" + COLUMN_USER_ID + ") REFERENCES " + TABLE_USERS + "(" + COLUMN_ID + ") ON DELETE CASCADE"
                + ")";
        db.execSQL(CREATE_WALLET_TABLE);

        // Create Transactions Table
        String CREATE_TRANSACTIONS_TABLE = "CREATE TABLE " + TABLE_TRANSACTIONS + "("
                + COLUMN_TRANSACTION_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_TRANSACTION_TYPE + " TEXT,"
                + COLUMN_TRANSACTION_AMOUNT + " REAL,"
                + COLUMN_TRANSACTION_DATE + " TEXT,"
                + COLUMN_TRANSACTION_USER_ID + " INTEGER,"
                + "FOREIGN KEY(" + COLUMN_TRANSACTION_USER_ID + ") REFERENCES " + TABLE_USERS + "(" + COLUMN_ID + ") ON DELETE CASCADE"
                + ")";
        db.execSQL(CREATE_TRANSACTIONS_TABLE);

        // Log success
        Log.d("DatabaseHelper", "Users, Wallet, and Transactions tables created successfully.");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop old tables
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_WALLET);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TRANSACTIONS);

        // Recreate tables
        onCreate(db);

        // Log upgrade
        Log.d("DatabaseHelper", "Database upgraded to version " + newVersion);
    }

    // Add a transaction to the Transactions table
    public void addTransaction(int userId, String type, double amount) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TRANSACTION_USER_ID, userId);
        values.put(COLUMN_TRANSACTION_TYPE, type);
        values.put(COLUMN_TRANSACTION_AMOUNT, amount);
        values.put(COLUMN_TRANSACTION_DATE, getCurrentDate()); // Store the current date
        db.insert(TABLE_TRANSACTIONS, null, values);
        db.close();
    }

    // Get all transactions for a specific user
    public Cursor getTransactionHistory(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_TRANSACTIONS + " WHERE " + COLUMN_TRANSACTION_USER_ID + " = ? ORDER BY " + COLUMN_TRANSACTION_DATE + " DESC";
        return db.rawQuery(query, new String[]{String.valueOf(userId)});
    }

    // Utility function to get the current date in string format
    private String getCurrentDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return sdf.format(new Date());
    }
    public void updateWalletBalance(int userId, double balance) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_BALANCE_TO_ADD, balance);  // Update the balance field

        int rowsAffected = db.update("wallet", values, "user_id = ?", new String[]{String.valueOf(userId)});
        if (rowsAffected > 0) {
            Log.d("Database", "Wallet balance updated successfully");
        } else {
            Log.d("Database", "Failed to update wallet balance");
        }
    }
}
