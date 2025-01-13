package com.example.projettest3;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "parking_table")
public class Parking {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "address")
    private String address;

    @ColumnInfo(name = "slots")
    private int slots;

    public Parking(String name, String address, int slots) {
        this.name = name;
        this.address = address;
        this.slots = slots;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public int getSlots() { return slots; }
    public void setSlots(int slots) { this.slots = slots; }
}
