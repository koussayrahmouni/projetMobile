package com.example.projettest3;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface FavoriteParkingDao {

    @Insert




    void insert(FavoriteParking favoriteParking);

    @Delete
    void delete(FavoriteParking favoriteParking);

    @Query("SELECT * FROM favorite_parking_table")
    List<FavoriteParking> getAllFavoriteParkings();

    // Correction de la méthode update pour accepter des paramètres explicites
    @Query("UPDATE favorite_parking_table SET name = :name, address = :address WHERE id = :id")
    void update(int id, String name, String address);

}
