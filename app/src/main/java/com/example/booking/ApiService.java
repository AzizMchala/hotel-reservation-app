package com.example.booking;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiService {
    @GET("booking/controllers/allHotel.php")
    Call<ApiResponse<List<Hotel>>> getAllHotels();
}
