package com.example.quizapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.quizapp.R;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private EditText etEmail, etPassword;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        // UI-Komponenten mit den IDs aus der XML-Datei verknüpfen
        etEmail = findViewById(R.id.et_login_email);
        etPassword = findViewById(R.id.et_login_password);

        Button btnLoginExecute = findViewById(R.id.btn_login_execute);

        // Click-Listener: Definiert, was beim Drücken des Buttons passiert
        btnLoginExecute.setOnClickListener(v -> {
            // Texte werden aus den Feldern geholt und .trim() wird genutzt, um Leerzeichen zu entfernen
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            // Wir prüfen, ob der User Felder leer gelassen hat
            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Bitte E-Mail und Passwort eingeben", Toast.LENGTH_SHORT).show();
            } else {
                // Wenn Eingaben vorhanden sind, wird die Login-Methode aufgerufen
                performLogin(email, password);
            }
        });
    }

    // Methode für die Kommunikation mit dem Firebase-Server
    private void performLogin(String email, String password) {
        // Anfrage an Firebase mit den eingegebenen Daten
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    // Überprüfung, ob der Login-Vorgang erfolgreich war
                    if (task.isSuccessful()) {
                        // Bei Erfolg: Wechsel zur HomeActivity
                        startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                        finish();
                    } else {
                        // Bei Fehlern (z.B. falsches Passwort): Anzeige einer Fehlermeldung
                        // task.getException() liefert den genauen Grund vom Server
                        Toast.makeText(LoginActivity.this, "Login fehlgeschlagen: " +
                                task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }
}