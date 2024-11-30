package com.example.booking;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class HotelAdapter extends RecyclerView.Adapter<HotelAdapter.HotelViewHolder> {

    private final List<Hotel> hotelList;
    private final OnItemClickListener onItemClickListener;

    // Interface pour le clic
    public interface OnItemClickListener {
        void onItemClick(int hotelId, String price, String hotelName);
    }

    public HotelAdapter(List<Hotel> hotelList, OnItemClickListener onItemClickListener) {
        this.hotelList = hotelList;
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public HotelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_hotel, parent, false);
        return new HotelViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HotelViewHolder holder, int position) {
        Hotel hotel = hotelList.get(position);
        holder.nameTextView.setText(hotel.getName());
        holder.locationTextView.setText(hotel.getLocation());
        holder.priceTextView.setText("Prix: " + hotel.getPrice() + " TND");
        holder.descriptionTextView.setText(hotel.getDescription());

        // Charger l'image avec Glide
        Glide.with(holder.itemView.getContext())
                .load(hotel.getImage())
                .placeholder(R.drawable.refresh) // Image par défaut pendant le chargement
                .error(R.drawable.baseline_error_24) // Image d'erreur si le chargement échoue
                .into(holder.imageViewHotel);

        // Gérer le clic
        holder.itemView.setOnClickListener(v -> {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(hotel.getId_hotel(), hotel.getPrice(), hotel.getName());
            }
        });
    }

    @Override
    public int getItemCount() {
        return hotelList.size();
    }

    public static class HotelViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView, locationTextView, priceTextView, descriptionTextView;
        ImageView imageViewHotel;

        public HotelViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            locationTextView = itemView.findViewById(R.id.locationTextView);
            priceTextView = itemView.findViewById(R.id.priceTextView);
            descriptionTextView = itemView.findViewById(R.id.descriptionTextView);
            imageViewHotel = itemView.findViewById(R.id.imageViewHotel);
        }
    }
}
