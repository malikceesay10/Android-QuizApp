package com.example.quizapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import com.example.quizapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 1. Firebase initialisieren
        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        // 2. Prüfen, ob bereits ein User eingeloggt ist
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            // User ist bereits eingeloggt -> Direkt zur HomeActivity springen
            Intent intent = new Intent(MainActivity.this, HomeActivity.class);
            startActivity(intent);
            // MainActivity schließen, damit man mit "Zurück" nicht wieder hier landet
            finish();
            return; // Wichtig, damit der Rest der Methode nicht ausgeführt wird
        }

        // Falls kein User eingeloggt ist, zeige das normale Auswahl-Layout
        setContentView(R.layout.activity_main);

        Button btnToLogin = findViewById(R.id.btn_to_login);
        Button btnToRegister = findViewById(R.id.btn_to_register);

        // Navigation zur Registrierung (Folie 08)
        btnToRegister.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
            startActivity(intent);
        });

        // Navigation zum Login (Folie 08)
        btnToLogin.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
        });
    }
}