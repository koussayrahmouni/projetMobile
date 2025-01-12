package com.example.testfirebase;

public class ReservationData {
    private String spotName;
    private int price;
    private int hours;

    // Constructeur sans argument pour Firebase
    public ReservationData() {}

    public ReservationData(String spotName, int hours, int price) {
        this.spotName = spotName;
        this.hours = hours;
        this.price = price;
    }

    public String getSpotName() {
        return spotName;
    }

    public void setSpotName(String spotName) {
        this.spotName = spotName;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getHours() {
        return hours;
    }

    public void setHours(int hours) {
        this.hours = hours;
    }

    @Override
    public String toString() {
        return "ReservationData{" +
                "spotName='" + spotName + '\'' +
                ", hours=" + hours +
                ", price=" + price +
                '}';
    }
}
