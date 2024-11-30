package com.example.booking;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity2 extends AppCompatActivity implements HotelAdapter.OnItemClickListener {

    private RecyclerView recyclerView;
    private HotelAdapter hotelAdapter;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        // Récupérer l'ID utilisateur depuis l'Intent
        Intent intent = getIntent();
        userId = intent.getStringExtra("userId");

        if (userId == null || userId.isEmpty()) {
            Toast.makeText(this, "ID utilisateur manquant", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Initialiser le RecyclerView
        recyclerView = findViewById(R.id.recyclerViewHotels);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Gérer le bouton retour
        TextView backTextView = findViewById(R.id.backTextView);
        backTextView.setOnClickListener(v -> {
            // Revenir à MainActivity
            Intent backIntent = new Intent(MainActivity2.this, MainActivity.class);
            backIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(backIntent);
            finish();
        });

        fetchHotels();
    }

    private void fetchHotels() {
        ApiService apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);
        apiService.getAllHotels().enqueue(new Callback<ApiResponse<List<Hotel>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<Hotel>>> call, Response<ApiResponse<List<Hotel>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Hotel> hotels = response.body().getData();
                    hotelAdapter = new HotelAdapter(hotels, MainActivity2.this);
                    recyclerView.setAdapter(hotelAdapter);
                } else {
                    Toast.makeText(MainActivity2.this, "Erreur lors de la récupération des données", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<Hotel>>> call, Throwable t) {
                Log.e("API_ERROR", t.getMessage());
                Toast.makeText(MainActivity2.this, "Échec de la connexion au serveur", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onItemClick(int hotelId, String price, String hotelName) {
        // Démarrer MainActivity3 en passant les informations
        Intent intent = new Intent(this, MainActivity3.class);
        intent.putExtra("HOTEL_ID", hotelId);
        intent.putExtra("USER_ID", userId);
        intent.putExtra("PRICE", price);
        intent.putExtra("HOTEL_NAME", hotelName);
        startActivity(intent);
    }
}
