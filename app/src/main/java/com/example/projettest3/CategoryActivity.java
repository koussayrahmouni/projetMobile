package com.example.projettest3;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CategoryActivity extends AppCompatActivity {

    private Button addCategoryButton;
    private AppDatabase database;
    private RecyclerView recyclerView;
    private CategoryAdapter categoryAdapter;  // Vous devez créer cet adaptateur pour afficher les catégories

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        // Initialiser la base de données
        database = AppDatabase.getInstance(this);

        // Initialiser RecyclerView et son adaptateur
        recyclerView = findViewById(R.id.category_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        categoryAdapter = new CategoryAdapter(database.categorieParkingDao());  // Passer le DAO à l'adaptateur
        recyclerView.setAdapter(categoryAdapter);

        // Initialiser le bouton pour ajouter une catégorie
        addCategoryButton = findViewById(R.id.add_category_button);

        // Lorsqu'on clique sur le bouton "Ajouter une catégorie"
        addCategoryButton.setOnClickListener(v -> showAddCategoryDialog());

        // Charger les catégories existantes dans RecyclerView
        loadCategories();
    }

    // Méthode pour afficher un dialogue permettant d'ajouter une catégorie
    private void showAddCategoryDialog() {
        // Créer le dialogue avec la vue de l'éditeur
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_add_category, null);
        final EditText categoryNameInput = dialogView.findViewById(R.id.category_name_input);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Ajouter une catégorie")
                .setView(dialogView)
                .setPositiveButton("Ajouter", (dialog, which) -> {
                    String categoryName = categoryNameInput.getText().toString().trim();
                    if (!categoryName.isEmpty()) {
                        // Ajouter la catégorie à la base de données
                        CategorieParking newCategory = new CategorieParking(categoryName);
                        database.categorieParkingDao().insert(newCategory);

                        // Mettre à jour l'affichage
                        loadCategories();

                        // Afficher un message de succès
                        Toast.makeText(CategoryActivity.this, "Catégorie ajoutée : " + categoryName, Toast.LENGTH_SHORT).show();
                    } else {
                        // Afficher un message d'erreur si le nom est vide
                        Toast.makeText(CategoryActivity.this, "Le nom de la catégorie ne peut pas être vide.", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Annuler", (dialog, which) -> dialog.dismiss())
                .show();
    }

    // Méthode pour charger les catégories existantes et les afficher dans RecyclerView
    private void loadCategories() {
        List<CategorieParking> categories = database.categorieParkingDao().getAllCategories();
        categoryAdapter.updateData(categories);  // Vous devez définir la méthode updateData dans l'adaptateur
    }
}
