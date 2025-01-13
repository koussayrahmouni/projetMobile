package com.example.projettest3;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private AppDatabase database;
    private ParkingAdapter parkingAdapter;
    private RecyclerView recyclerView;
    private EditText searchBar;
    private Button searchButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialiser la base de données
        database = AppDatabase.getInstance(this);

        // Initialisation des vues
        recyclerView = findViewById(R.id.parking_list);
        searchBar = findViewById(R.id.search_bar);
        searchButton = findViewById(R.id.search_button);

        // Initialisation de l'adaptateur et du RecyclerView
        parkingAdapter = new ParkingAdapter(
                new ArrayList<>(),
                new ParkingAdapter.OnParkingClickListener() {
                    @Override
                    public void onParkingClick(String address) {
                        navigateToParking(address);
                    }
                },
                database // Passer l'instance de la base
        );
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(parkingAdapter);

        // Ajouter des parkings pour le test s'ils n'existent pas déjà dans la base
        if (database.parkingDao().getAllParkings().isEmpty()) {
            database.parkingDao().insertParking(new Parking("Parking Lac 1", "Avenue du Lac d'Annecy, Les Berges du Lac, Tunis", 100));
            database.parkingDao().insertParking(new Parking("Parking Centre-Ville Tunis", "Rue de Marseille, Tunis", 150));
            database.parkingDao().insertParking(new Parking("Parking Habib Bourguiba", "Avenue Habib Bourguiba, Tunis", 200));
            database.parkingDao().insertParking(new Parking("Parking Tunisia Mall", "Tunisia Mall, Les Berges du Lac 2, Tunis", 120));
            database.parkingDao().insertParking(new Parking("Parking Sousse Medina", "Rue de la Corniche, Sousse", 80));
            database.parkingDao().insertParking(new Parking("Parking Monastir Ribat", "Place du Ribat, Monastir", 50));
            database.parkingDao().insertParking(new Parking("Parking Djerba Houmt Souk", "Avenue Habib Bourguiba, Houmt Souk, Djerba", 70));
            database.parkingDao().insertParking(new Parking("Parking Ariana City Center", "Avenue Habib Bourguiba, Ariana", 90));
            database.parkingDao().insertParking(new Parking("Parking Gafsa Centre", "Rue Habib Thameur, Gafsa", 60));
            database.parkingDao().insertParking(new Parking("Parking Gabès Medina", "Rue de la République, Gabès", 50));
        }

        // Afficher tous les parkings au démarrage
        List<Parking> allParkings = database.parkingDao().getAllParkings();
        parkingAdapter.updateData(allParkings);

        // Configurer le bouton de recherche
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String query = searchBar.getText().toString().trim();

                // Si la recherche est vide, afficher tous les parkings
                List<Parking> parkings;
                if (query.isEmpty()) {
                    parkings = database.parkingDao().getAllParkings();
                } else {
                    // Sinon, filtrer par adresse
                    parkings = database.parkingDao().searchByAddress(query);
                }

                // Mettre à jour l'adaptateur avec les résultats
                parkingAdapter.updateData(parkings);
            }
        });

        // Configurer le bouton pour afficher les parkings favoris
        Button showFavoritesButton = findViewById(R.id.show_favorites_button);
        showFavoritesButton.setOnClickListener(v -> {
            // Ouvrir l'activité des parkings favoris
            Intent intent = new Intent(MainActivity.this, FavoriteParkingsActivity.class);
            startActivity(intent);
        });

        // Ajouter un bouton pour ouvrir CategoryActivity
        Button categoryButton = findViewById(R.id.category_button);
        categoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Créer une intention pour démarrer CategoryActivity
                Intent intent = new Intent(MainActivity.this, CategoryActivity.class);
                startActivity(intent);
            }
        });
    }

    // Redirige vers le parking sélectionné
    private void navigateToParking(String address) {
        Uri gmmIntentUri = Uri.parse("geo:0,0?q=" + Uri.encode(address));
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        if (mapIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(mapIntent);
        }
    }
}
