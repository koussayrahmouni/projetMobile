package tn.esprit.myapplication.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import tn.esprit.myapplication.directory.TransactionDao;
import tn.esprit.myapplication.directory.UserDao;
import tn.esprit.myapplication.directory.VehicleDao;
import tn.esprit.myapplication.directory.WalletDao;
import tn.esprit.myapplication.entity.Transaction;
import tn.esprit.myapplication.entity.User;
import tn.esprit.myapplication.entity.Vehicle;
import tn.esprit.myapplication.entity.Wallet;

@Database(entities = {User.class, Wallet.class, Transaction.class , Vehicle.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract UserDao userDao();
    public abstract WalletDao walletDao();
    public abstract TransactionDao transactionDao();
    public abstract VehicleDao vehicleDao();
    private static volatile AppDatabase INSTANCE;

    // Method to get the singleton instance
    public static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDatabase.class, "parkinggg")
                            .fallbackToDestructiveMigration() // Handle migrations (optional)
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
