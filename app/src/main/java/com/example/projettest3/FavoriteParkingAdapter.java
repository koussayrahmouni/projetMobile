package com.example.projettest3;

import android.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class FavoriteParkingAdapter extends RecyclerView.Adapter<FavoriteParkingAdapter.FavoriteParkingViewHolder> {

    private List<FavoriteParking> favoriteParkingList;
    private AppDatabase database;

    public FavoriteParkingAdapter(List<FavoriteParking> favoriteParkingList, AppDatabase database) {
        this.favoriteParkingList = favoriteParkingList;
        this.database = database;
    }

    @Override
    public FavoriteParkingViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.favorite_parking_item, parent, false);
        return new FavoriteParkingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FavoriteParkingViewHolder holder, int position) {
        FavoriteParking favoriteParking = favoriteParkingList.get(position);
        holder.nameText.setText(favoriteParking.getName());
        holder.addressText.setText(favoriteParking.getAddress());

        // Listener pour le bouton de mise à jour
        holder.updateButton.setOnClickListener(v -> {
            showUpdateDialog(holder.itemView, favoriteParking, position);
        });

        // Listener pour le bouton de suppression
        holder.deleteButton.setOnClickListener(v -> {
            database.favoriteParkingDao().delete(favoriteParking);
            favoriteParkingList.remove(position);
            notifyItemRemoved(position);
        });
    }

    @Override
    public int getItemCount() {
        return favoriteParkingList.size();
    }

    public void updateData(List<FavoriteParking> newFavoriteParkingList) {
        this.favoriteParkingList = newFavoriteParkingList;
        notifyDataSetChanged();
    }

    private void showUpdateDialog(View view, FavoriteParking favoriteParking, int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
        builder.setTitle("Modifier le Parking");

        // Utiliser un layout personnalisé pour la boîte de dialogue
        View dialogView = LayoutInflater.from(view.getContext()).inflate(R.layout.dialog_update_parking, null);
        builder.setView(dialogView);

        EditText nameInput = dialogView.findViewById(R.id.edit_parking_name);
        EditText addressInput = dialogView.findViewById(R.id.edit_parking_address);

        // Préremplir les champs avec les données existantes
        nameInput.setText(favoriteParking.getName());
        addressInput.setText(favoriteParking.getAddress());

        builder.setPositiveButton("Mettre à jour", null); // Listener défini plus tard pour garder le contrôle
        builder.setNegativeButton("Annuler", (dialog, which) -> dialog.dismiss());

        AlertDialog dialog = builder.create();
        dialog.show();

        // Définir le listener après `dialog.show()` pour un meilleur contrôle
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {
            String newName = nameInput.getText().toString().trim();
            String newAddress = addressInput.getText().toString().trim();

            if (TextUtils.isEmpty(newName) || TextUtils.isEmpty(newAddress)) {
                nameInput.setError(TextUtils.isEmpty(newName) ? "Le nom est requis" : null);
                addressInput.setError(TextUtils.isEmpty(newAddress) ? "L'adresse est requise" : null);
            } else {
                try {
                    // Mettre à jour l'objet et la base de données
                    favoriteParking.setName(newName);
                    favoriteParking.setAddress(newAddress);
                    database.favoriteParkingDao().update(favoriteParking.getId(), newName, newAddress);

                    // Mettre à jour la liste et rafraîchir l'affichage
                    favoriteParkingList.set(position, favoriteParking);
                    notifyItemChanged(position);

                    dialog.dismiss(); // Fermer le dialogue après succès
                } catch (Exception e) {
                    e.printStackTrace();
                    nameInput.setError("Erreur lors de la mise à jour");
                }
            }
        });
    }

    class FavoriteParkingViewHolder extends RecyclerView.ViewHolder {
        TextView nameText, addressText;
        Button deleteButton, updateButton;

        FavoriteParkingViewHolder(View itemView) {
            super(itemView);
            nameText = itemView.findViewById(R.id.favorite_parking_name);
            addressText = itemView.findViewById(R.id.favorite_parking_address);
            deleteButton = itemView.findViewById(R.id.btn_delete_favorite);
            updateButton = itemView.findViewById(R.id.btn_update_favorite); // Bouton de mise à jour
        }
    }
}
