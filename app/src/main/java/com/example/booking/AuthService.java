package com.example.booking;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AuthService {

    // Point de terminaison de l'API pour le login
    @POST("auth.php") // Remplacez par le chemin exact de votre API
    Call<AuthResponse> login(@Body AuthRequest authRequest);
}
