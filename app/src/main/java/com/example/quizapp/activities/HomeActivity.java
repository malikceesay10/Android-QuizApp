package com.example.quizapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import com.example.quizapp.R;
import com.google.firebase.auth.FirebaseAuth;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Lädt das Hauptmenü-Layout nach erfolgreichem Login
        setContentView(R.layout.activity_home);

        // NAVIGATION: Quiz-Modus starten
        // Wir übergeben den String "quiz" als Extra, damit die nächste Activity den Modus kennt
        findViewById(R.id.btn_start_quiz).setOnClickListener(v -> navigateToCategory("quiz"));

        // NAVIGATION: Lern-Modus starten
        // Gleiche Ziel-Activity, aber wir kennzeichnen den Modus als "learning"
        findViewById(R.id.btn_learning_mode).setOnClickListener(v -> navigateToCategory("learning"));

        // LOGOUT-LOGIK
        // Beendet die aktuelle Sitzung in Firebase und kehrt zum Startbildschirm zurück
        findViewById(R.id.btn_logout).setOnClickListener(v -> {
            // Meldet den User vom Server ab
            FirebaseAuth.getInstance().signOut();

            // Nach erfolgreichem logout wird der User zurück zum MainActivity geleitet.
            Intent intent = new Intent(this, MainActivity.class);
            // Löscht alle vorherigen Aktivitäten aus dem Verlauf
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });
    }

    // Zentrale Hilfsmethode für die Navigation mit Datenübergabe
    private void navigateToCategory(String mode) {
        Intent intent = new Intent(this, CategoryActivity.class);
        intent.putExtra("MODE", mode);
        startActivity(intent);
    }
}