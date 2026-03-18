package com.example.quizapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.quizapp.R;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {

    private EditText etEmail, etPassword, etConfirm;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Lädt das XML-Layout für die Registrierung
        setContentView(R.layout.activity_register);

        // Verbindung zum Firebase-Projekt herstellen
        mAuth = FirebaseAuth.getInstance();

        // Verknüpfung der Java-Variablen mit den IDs aus der XML
        etEmail = findViewById(R.id.et_email);
        etPassword = findViewById(R.id.et_password);
        etConfirm = findViewById(R.id.et_password_confirm);
        Button btnRegister = findViewById(R.id.btn_register_execute);

        // Click-Listener für den Registrierungs-Button
        btnRegister.setOnClickListener(v -> {
            // Auslesen der Daten und Entfernen von Leerzeichen mit .trim()
            String email = etEmail.getText().toString().trim();
            String pass = etPassword.getText().toString().trim();
            String confirm = etConfirm.getText().toString().trim();

            // Erweiterte Validierung für die Registrierung
            // Wir prüfen: Sind Felder leer? Stimmen die Passwörter überein? Ist es lang genug?
            if (email.isEmpty() || pass.isEmpty()) {
                Toast.makeText(this, "Felder dürfen nicht leer sein", Toast.LENGTH_SHORT).show();
            } else if (!pass.equals(confirm)) {
                // Lokaler Vergleich verhindert unnötige Server-Anfragen bei Tippfehlern
                Toast.makeText(this, "Passwörter stimmen nicht überein", Toast.LENGTH_SHORT).show();
            } else if (pass.length() < 6) {
                // Firebase fordert aus Sicherheitsgründen mindestens 6 Zeichen
                Toast.makeText(this, "Passwort muss mind. 6 Zeichen haben", Toast.LENGTH_SHORT).show();
            } else {
                // Wenn alle lokalen Checks bestanden sind, wird der User in der Cloud angelegt
                createNewUser(email, pass);
            }
        });
    }

    private void createNewUser(String email, String password) {
        // Befehl zum Erstellen eines neuen Accounts
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Bei Erfolg: Kurze Bestätigung und Wechsel zum Hauptmenü
                        Toast.makeText(this, "Account erstellt!", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(RegisterActivity.this, HomeActivity.class));
                        // Schließen der Activity, um den Registrierungs-Stack zu leeren
                        finish();
                    } else {
                        // Fehlermeldung vom Server (z.B. E-Mail existiert bereits)
                        Toast.makeText(this, "Fehler: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }
}