package com.example.projettest3;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ParkingDao {
    @Insert
    void insertParking(Parking parking);

    @Query("SELECT * FROM parking_table WHERE address LIKE '%' || :search || '%'")
    List<Parking> searchByAddress(String search);

    @Query("SELECT * FROM parking_table")
    List<Parking> getAllParkings();

    // Ajouter un parking favori
    @Insert
    void insertFavoriteParking(FavoriteParking favoriteParking);

    // Récupérer tous les parkings favoris
    @Query("SELECT * FROM favorite_parking_table")
    List<FavoriteParking> getAllFavoriteParkings();
}
