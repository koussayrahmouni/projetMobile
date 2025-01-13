package com.example.projettest3;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class FavoriteParkingsActivity extends AppCompatActivity {

    private AppDatabase database;
    private RecyclerView recyclerView;
    private FavoriteParkingAdapter favoriteParkingAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_parkings);

        // Initialiser la base de données
        database = AppDatabase.getInstance(this);

        // Initialisation du RecyclerView et de l'adaptateur
        recyclerView = findViewById(R.id.favorite_parking_list);
        favoriteParkingAdapter = new FavoriteParkingAdapter(database.favoriteParkingDao().getAllFavoriteParkings(), database);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(favoriteParkingAdapter);

        // Récupérer la liste des parkings favoris
        List<FavoriteParking> favoriteParkings = database.favoriteParkingDao().getAllFavoriteParkings();
        favoriteParkingAdapter.updateData(favoriteParkings);
    }
}
