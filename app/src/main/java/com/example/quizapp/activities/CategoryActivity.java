package com.example.quizapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import com.example.quizapp.R;

public class CategoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Lädt das Layout für die Kategoriewahl (activity_category.xml)
        setContentView(R.layout.activity_category);

        // Wir suchen die Buttons für die verschiedenen Kategorien in der XML.
        Button btnErdkunde = findViewById(R.id.btn_erdkunde);
        Button btnWissenschaft = findViewById(R.id.btn_wissenschaft);
        Button btnSport = findViewById(R.id.btn_sport);
        Button btnEssen = findViewById(R.id.btn_essen);
        Button btnRandom = findViewById(R.id.btn_random);

        // Jeder Button bekommt einen Click-Listener, der die zentrale Methode onCategorySelected mit der jeweiligen Kategorie aufruft.
        btnErdkunde.setOnClickListener(v -> onCategorySelected("Erdkunde"));
        btnWissenschaft.setOnClickListener(v -> onCategorySelected("Wissenschaft"));
        btnSport.setOnClickListener(v -> onCategorySelected("Sport"));
        btnEssen.setOnClickListener(v -> onCategorySelected("Essen"));
        btnRandom.setOnClickListener(v -> onCategorySelected("Zufall"));
    }

    // ZENTRALE LOGIK DER KATEGORIEWAHL
    private void onCategorySelected(String category) {
        // Wir lesen den Intent aus der HomeActivity wieder aus.
        // So wissen wir, ob der User vorher "Quiz" oder "Lernen" geklickt hat.
        String mode = getIntent().getStringExtra("MODE");

        Intent intent;
        if ("learning".equals(mode)) {
            // Wenn der Lernmodus aktiv ist, starten wir die LearningActivity, Basierend auf
            // diesem Wert entscheidet die Activity wohin es weitergeht
            intent = new Intent(this, LearningActivity.class);
        } else {
            // Standardmäßig starten wir die QuizActivity
            intent = new Intent(this, QuizActivity.class);
        }

        // Wir packen die gewählte Kategorie als neue Information in den Intent.
        // Jetzt weiß die Ziel-Activity sowohl den Modus als auch die Kategorie.
        intent.putExtra("CATEGORY", category);
        startActivity(intent);
    }
}