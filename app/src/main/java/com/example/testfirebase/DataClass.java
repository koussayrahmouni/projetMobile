package com.example.testfirebase;

import com.google.firebase.database.Exclude;

import java.io.Serializable;

public class DataClass implements Serializable {
    private String spotName;
    private boolean isAvailable;
    private int imageResource;
    private String spotId;  // Optional field
    private int price;      // Optional field

    // Default constructor (required for Firebase)
    public DataClass() {
        // Initialize with default values or leave empty
        this.spotId = "unknown";
        this.price = 0;
    }

    // Constructor with all arguments
    public DataClass(String spotName, boolean isAvailable, int imageResource, String spotId, int price) {
        this.spotName = spotName;
        this.isAvailable = isAvailable;
        this.imageResource = imageResource;
        this.spotId = spotId;
        this.price = price;
    }

    // Getters and Setters
    public String getSpotName() {
        return spotName;
    }

    public void setSpotName(String spotName) {
        this.spotName = spotName;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    public int getImageResource() {
        return imageResource;
    }

    public void setImageResource(int imageResource) {
        this.imageResource = imageResource;
    }

    public String getSpotId() {
        return spotId;
    }

    public void setSpotId(String spotId) {
        this.spotId = spotId;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    @Exclude
    public String toString() {
        return "DataClass{" +
                "spotName='" + spotName + '\'' +
                ", isAvailable=" + isAvailable +
                ", imageResource=" + imageResource +
                ", spotId='" + spotId + '\'' +
                ", price=" + price +
                '}';
    }
}
