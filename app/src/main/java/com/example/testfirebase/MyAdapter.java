package com.example.testfirebase;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    private Context context;
    private List<DataClass> dataList;

    public MyAdapter(Context context, List<DataClass> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    // Method to update the list if needed (for example, after a successful data update)
    public void updateList(List<DataClass> newList) {
        this.dataList = newList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recycler_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        DataClass item = dataList.get(position);

        // Set the title and description of the parking spot
        holder.recTitle.setText(item.getSpotName());
        holder.recDesc.setText(item.isAvailable() ? "Available" : "Occupied");

        // Set the image for the parking spot, check for image resource ID
        if (item.getImageResource() != 0) {
            holder.recImage.setImageResource(item.getImageResource());
        }

        // Handle payment button click
        holder.paymentButton.setOnClickListener(v -> {
            if (item.isAvailable()) {
                // If the spot is available, navigate to ReservationActivity
                Intent intent = new Intent(context, ReservationActivity.class);
                intent.putExtra("spot_name", item.getSpotName());  // Pass the spot name
                intent.putExtra("spot_id", item.getSpotId());      // Optionally, pass the spot ID or any other details
                intent.putExtra("price", item.getPrice());          // Optionally, pass the price for reservation
                context.startActivity(intent);
            } else {
                // If the spot is occupied, show a Toast message
                Toast.makeText(context, "This spot is occupied", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    // ViewHolder class to hold the UI elements for each parking spot
    static class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView recImage;
        TextView recTitle, recDesc;
        Button paymentButton;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            // Initialize UI components
            recImage = itemView.findViewById(R.id.recImage);
            recTitle = itemView.findViewById(R.id.recTitle);
            recDesc = itemView.findViewById(R.id.recDesc);
            paymentButton = itemView.findViewById(R.id.paymentButton);
        }
    }
}
