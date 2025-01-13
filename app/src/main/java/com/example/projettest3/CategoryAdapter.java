package com.example.projettest3;

import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {

    private List<CategorieParking> categories;
    private CategorieParkingDao categorieParkingDao;

    public CategoryAdapter(CategorieParkingDao categorieParkingDao) {
        this.categories = new ArrayList<>();
        this.categorieParkingDao = categorieParkingDao;
    }

    @Override
    public CategoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CategoryViewHolder holder, int position) {
        CategorieParking category = categories.get(position);
        holder.nameTextView.setText(category.getName());

        // Bouton de mise à jour
        holder.updateButton.setOnClickListener(v -> {
            showUpdateCategoryDialog(v, category);  // Ouvrir le dialogue de mise à jour
        });

        // Bouton de suppression
        holder.deleteButton.setOnClickListener(v -> {
            // Suppression de la catégorie
            categorieParkingDao.delete(category);  // Vous devez implémenter cette méthode dans votre DAO
            // Mise à jour de l'affichage
            loadCategories();
        });
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    // Méthode pour mettre à jour les données
    public void updateData(List<CategorieParking> newCategories) {
        this.categories = newCategories;
        notifyDataSetChanged();
    }

    // Méthode pour recharger les catégories après suppression ou mise à jour
    private void loadCategories() {
        this.categories = categorieParkingDao.getAllCategories();
        notifyDataSetChanged();
    }

    // Afficher le dialogue de mise à jour de la catégorie
    private void showUpdateCategoryDialog(View v, CategorieParking category) {
        // Créer le dialogue avec la vue de l'éditeur
        View dialogView = LayoutInflater.from(v.getContext()).inflate(R.layout.dialog_update_category, null); // Utilisation du bon layout
        final EditText categoryNameInput = dialogView.findViewById(R.id.category_name_input);
        categoryNameInput.setText(category.getName());  // Préciser le nom actuel de la catégorie

        AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
        builder.setTitle("Modifier la catégorie")
                .setView(dialogView)
                .setPositiveButton("Mettre à jour", (dialog, which) -> {
                    String updatedCategoryName = categoryNameInput.getText().toString().trim();
                    if (!updatedCategoryName.isEmpty()) {
                        // Mise à jour de la catégorie dans la base de données
                        category.setName(updatedCategoryName);
                        categorieParkingDao.update(category);  // Ajout de la méthode update dans le DAO
                        // Mettre à jour l'affichage
                        loadCategories();
                    } else {
                        // Afficher un message d'erreur si le nom est vide
                        Toast.makeText(v.getContext(), "Le nom de la catégorie ne peut pas être vide.", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Annuler", (dialog, which) -> dialog.dismiss())
                .show();
    }

    public static class CategoryViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView;
        Button deleteButton, updateButton;

        public CategoryViewHolder(View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.category_name);
            deleteButton = itemView.findViewById(R.id.delete_button);  // Référence au bouton de suppression
            updateButton = itemView.findViewById(R.id.update_button);  // Référence au bouton de mise à jour
        }
    }
}
