package com.example.booking;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginFragment extends Fragment {

    private EditText etLogin, etPassword;
    private Button btnLogin;
    private AuthService authService;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        etLogin = view.findViewById(R.id.etLogin);
        etPassword = view.findViewById(R.id.etPassword);
        btnLogin = view.findViewById(R.id.btnLogin);

        // Configuration de Retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.1.11/booking/controllers/") // Remplace par ton URL
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        authService = retrofit.create(AuthService.class);

        btnLogin.setOnClickListener(v -> {
            String login = etLogin.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if (login.isEmpty() || password.isEmpty()) {
                Toast.makeText(getContext(), "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
            } else {
                authenticateUser(login, password);
            }
        });

        return view;
    }

    private void authenticateUser(String login, String password) {
        AuthRequest authRequest = new AuthRequest(login, password);
        Call<AuthResponse> call = authService.login(authRequest);
        call.enqueue(new Callback<AuthResponse>() {
            @Override
            public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    AuthResponse authResponse = response.body();
                    if (authResponse.isSuccess()) {
                        User user = authResponse.getData();

                        Log.d("LoginFragment", "User ID: " + user.getId());
                        Log.d("LoginFragment", "User Name: " + user.getName());

                        Toast.makeText(getContext(), "Connexion réussie", Toast.LENGTH_SHORT).show();
                        showMainFeatures(user);
                    } else {
                        Toast.makeText(getContext(), authResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getContext(), "Erreur de réponse du serveur", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<AuthResponse> call, Throwable t) {
                Log.e("LoginFragment", "Erreur de connexion : ", t);
                Toast.makeText(getContext(), "Erreur de connexion : " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }

        });
    }

    private void showMainFeatures(User user) {
        if (user != null && user.getId() != null && !user.getId().isEmpty() ) {
            Intent intent = new Intent(getActivity(), MainActivity2.class);

            intent.putExtra("userId", user.getId()); // Si vous stockez l'ID comme int
            startActivity(intent);
            requireActivity().finish();
        } else {
            Toast.makeText(getContext(), "Données utilisateur manquantes ou invalides", Toast.LENGTH_SHORT).show();
        }
    }
}
