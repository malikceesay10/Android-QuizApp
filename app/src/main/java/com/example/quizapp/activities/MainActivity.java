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

        // Wir prüfen sofort beim Start, ob bereits ein User bei Firebase angemeldet ist.
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        // Wenn ein User gefunden wurde (nicht null), leiten wir ihn direkt weiter.
        if (currentUser != null) {
            // Intent zur HomeActivity (Hauptmenü)
            startActivity(new Intent(MainActivity.this, HomeActivity.class));
            // finish() schließt die MainActivity, damit man nicht per "Zurück" zum Login-Screen kommt.
            finish();
            return; // Sicherheitsmaßnahme, um den restlichen Code nicht auszuführen, wenn der User bereits angemeldet ist.
        }

        // Wenn kein User angemeldet ist, laden wir das Start-Layout mit den Buttons.
        setContentView(R.layout.activity_main);

        Button btnToLogin = findViewById(R.id.btn_to_login);
        Button btnToRegister = findViewById(R.id.btn_to_register);

        // Startet die RegisterActivity, wenn der User auf den Registrier-Button klickt.
        btnToRegister.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, RegisterActivity.class));
        });

        // Startet die LoginActivity für bereits registrierte Nutzer.
        btnToLogin.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
        });
    }
}