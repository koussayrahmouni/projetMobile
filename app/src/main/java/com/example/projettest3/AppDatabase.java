package com.example.projettest3;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Parking.class, FavoriteParking.class, CategorieParking.class}, version = 3, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase instance;

    public abstract ParkingDao parkingDao();
    public abstract FavoriteParkingDao favoriteParkingDao();
    public abstract CategorieParkingDao categorieParkingDao();  // Ajouter le DAO pour CategorieParking

    public static synchronized AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, "parking_database")
                    .fallbackToDestructiveMigration()  // Permet de migrer la base de données en cas de changement de version
                    .allowMainThreadQueries()         // Permet de faire des requêtes sur le thread principal (pas recommandé en production)
                    .build();
        }
        return instance;
    }
}
