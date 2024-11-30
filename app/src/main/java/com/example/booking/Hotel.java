package com.example.booking;

public class Hotel {
    private int id_hotel;
    private String name;
    private String location;
    private String description;
    private String price;
    private String image; // Nouvelle propriété

    public Hotel(int id_hotel, String name, String location, String description, String price, String image) {
        this.id_hotel = id_hotel;
        this.name = name;
        this.location = location;
        this.description = description;
        this.price = price;
        this.image = image; // Initialiser l'image
    }

    public int getId_hotel() {
        return id_hotel;
    }

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }

    public String getDescription() {
        return description;
    }

    public String getPrice() {
        return price;
    }

    public String getImage() {
        return image; // Getter pour l'image
    }
}
