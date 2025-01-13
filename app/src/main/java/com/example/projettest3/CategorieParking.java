package com.example.projettest3;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "categorie_parking")
public class CategorieParking {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String name;

    // Constructeur
    public CategorieParking(String name) {
        this.name = name;
    }

    // Getters et setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
