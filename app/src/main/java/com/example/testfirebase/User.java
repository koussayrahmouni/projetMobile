package com.example.testfirebase;
public class User {
    private String name;
    private int age;

    // Constructeur
    public User() {
        // Constructeur vide nécessaire pour Firebase
    }

    public User(String name, int age) {
        this.name = name;
        this.age = age;
    }

    // Getter et Setter pour name
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // Getter et Setter pour age
    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
