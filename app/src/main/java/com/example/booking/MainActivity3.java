package com.example.booking;

import android.app.DatePickerDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import org.json.JSONObject;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MainActivity3 extends AppCompatActivity {

    private static final String CHANNEL_ID = "reservation_channel";
    private EditText arrivalDateEditText, departureDateEditText;
    private TextView totalPriceText, nomHotel;
    private String pricePerDay, hotelName, userId;
    private int hotelId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        arrivalDateEditText = findViewById(R.id.arrivalDateEditText);
        departureDateEditText = findViewById(R.id.departureDateEditText);
        totalPriceText = findViewById(R.id.totalPriceText);
        nomHotel = findViewById(R.id.nomHotel);
        Button confirmButton = findViewById(R.id.confirme);

        // Initialisation des données
        hotelId = getIntent().getIntExtra("HOTEL_ID", -1);
        userId = getIntent().getStringExtra("USER_ID");
        pricePerDay = getIntent().getStringExtra("PRICE");
        hotelName = getIntent().getStringExtra("HOTEL_NAME");

        nomHotel.setText(hotelName);

        arrivalDateEditText.setOnClickListener(v -> showDatePickerDialog(arrivalDateEditText));
        departureDateEditText.setOnClickListener(v -> showDatePickerDialog(departureDateEditText));

        arrivalDateEditText.addTextChangedListener(new android.text.TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                updateTotalPrice();
            }

            @Override
            public void afterTextChanged(android.text.Editable editable) {}
        });

        departureDateEditText.addTextChangedListener(new android.text.TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                updateTotalPrice();
            }

            @Override
            public void afterTextChanged(android.text.Editable editable) {}
        });

        // Créer le canal de notification
        createNotificationChannel();

        // Action de clic pour le bouton "Confirmer"
        confirmButton.setOnClickListener(v -> submitReservation());
    }

    private void showDatePickerDialog(EditText targetEditText) {
        Calendar calendar = Calendar.getInstance();
        new DatePickerDialog(this,
                (DatePicker view, int year, int month, int dayOfMonth) ->
                        targetEditText.setText(dayOfMonth + "/" + (month + 1) + "/" + year),
                calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)
        ).show();
    }

    private void updateTotalPrice() {
        String arrivalDateString = arrivalDateEditText.getText().toString();
        String departureDateString = departureDateEditText.getText().toString();

        if (arrivalDateString.isEmpty() || departureDateString.isEmpty()) return;

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        try {
            Date arrivalDate = sdf.parse(arrivalDateString);
            Date departureDate = sdf.parse(departureDateString);

            if (arrivalDate == null || departureDate == null || departureDate.before(arrivalDate)) return;

            long days = (departureDate.getTime() - arrivalDate.getTime()) / (1000 * 60 * 60 * 24);
            double totalPrice = Double.parseDouble(pricePerDay) * days;
            totalPriceText.setText(String.format(Locale.getDefault(), "%.2f TND", totalPrice));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void submitReservation() {
        String arrivalDate = convertDateFormat(arrivalDateEditText.getText().toString());
        String departureDate = convertDateFormat(departureDateEditText.getText().toString());
        String totalPrice = totalPriceText.getText().toString().replace(" TND", "");

        if (arrivalDate == null || departureDate == null) {
            runOnUiThread(() -> Toast.makeText(this, "Format de date invalide", Toast.LENGTH_LONG).show());
            return;
        }

        new Thread(() -> {
            try {
                URL url = new URL("http://192.168.1.11/booking/controllers/reservation.php");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setDoOutput(true);

                JSONObject json = new JSONObject();
                json.put("id_user", userId);
                json.put("id_hotel", hotelId);
                json.put("check_in_date", arrivalDate); // Format YYYY-MM-DD
                json.put("check_out_date", departureDate); // Format YYYY-MM-DD
                json.put("total_price", totalPrice);

                OutputStream os = conn.getOutputStream();
                os.write(json.toString().getBytes());
                os.flush();
                os.close();

                int responseCode = conn.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    runOnUiThread(() -> {
                        Toast.makeText(this, "Réservation réussie!", Toast.LENGTH_LONG).show();
                        showNotification("Réservation réussie", "Votre réservation à " + hotelName + " est confirmée!");
                    });
                } else {
                    runOnUiThread(() -> Toast.makeText(this, "Erreur de réservation", Toast.LENGTH_LONG).show());
                }
            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(() -> Toast.makeText(this, "Erreur : " + e.getMessage(), Toast.LENGTH_LONG).show());
            }
        }).start();
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Réservation";
            String description = "Notifications pour les réservations";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void showNotification(String title, String message) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1, builder.build());
    }

    private String convertDateFormat(String dateString) {
        SimpleDateFormat inputFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        try {
            Date date = inputFormat.parse(dateString);
            return outputFormat.format(date);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
