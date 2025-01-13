package com.example.projettest3;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface CategorieParkingDao {

    // Insérer une nouvelle catégorie de parking
    @Insert
    void insert(CategorieParking categorieParking);

    // Supprimer une catégorie de parking
    @Delete
    void delete(CategorieParking categorieParking);

    // Mettre à jour une catégorie de parking
    @Update
    void update(CategorieParking categorieParking);

    // Récupérer toutes les catégories de parking
    @Query("SELECT * FROM categorie_parking")
    List<CategorieParking> getAllCategories();
}
