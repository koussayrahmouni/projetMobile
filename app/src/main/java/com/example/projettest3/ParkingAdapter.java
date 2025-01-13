package com.example.projettest3;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ParkingAdapter extends RecyclerView.Adapter<ParkingAdapter.ParkingViewHolder> {

    private List<Parking> parkingList;
    private OnParkingClickListener onParkingClickListener;
    private AppDatabase database;

    public interface OnParkingClickListener {
        void onParkingClick(String address);
    }

    public ParkingAdapter(List<Parking> parkingList, OnParkingClickListener listener, AppDatabase database) {
        this.parkingList = parkingList;
        this.onParkingClickListener = listener;
        this.database = database;
    }

    @Override
    public ParkingViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.parking_item, parent, false);
        return new ParkingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ParkingViewHolder holder, int position) {
        Parking parking = parkingList.get(position);
        holder.addressText.setText(parking.getAddress());
        holder.nameText.setText(parking.getName());

        // Ajouter au favoris
        holder.favoriteButton.setOnClickListener(v -> {
            database.favoriteParkingDao().insert(new FavoriteParking(parking.getName(), parking.getAddress()));
            // Afficher un toast ou message
            Toast.makeText(v.getContext(), "Parking ajouté aux favoris", Toast.LENGTH_SHORT).show();
        });

        holder.itemView.setOnClickListener(v -> onParkingClickListener.onParkingClick(parking.getAddress()));
    }

    @Override
    public int getItemCount() {
        return parkingList.size();
    }

    public void updateData(List<Parking> newParkingList) {
        this.parkingList = newParkingList;
        notifyDataSetChanged();
    }

    class ParkingViewHolder extends RecyclerView.ViewHolder {
        TextView nameText, addressText;
        Button favoriteButton;

        ParkingViewHolder(View itemView) {
            super(itemView);
            nameText = itemView.findViewById(R.id.parking_name);
            addressText = itemView.findViewById(R.id.parking_address);
            favoriteButton = itemView.findViewById(R.id.favorite_button);
        }
    }
}
