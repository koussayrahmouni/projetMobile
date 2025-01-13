package com.example.projettest3;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "favorite_parking_table")
public class FavoriteParking {
    @PrimaryKey(autoGenerate = true)
    private int id;

    private String name;
    private String address;

    public FavoriteParking(String name, String address) {
        this.name = name;
        this.address = address;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
}
