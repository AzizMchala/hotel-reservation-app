package com.example.booking;

import com.google.gson.annotations.SerializedName;

public class User {
    @SerializedName("id_user") // Mappe "id_user" de la réponse JSON
    private String id;

    @SerializedName("name") // Mappe "name" de la réponse JSON
    private String name;

    @SerializedName("email") // Mappe "email" de la réponse JSON
    private String email;

    // Getters et setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
