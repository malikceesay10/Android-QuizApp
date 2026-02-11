package com.example.quizapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.quizapp.R;
import java.util.ArrayList;

public class ResultActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Lädt das Layout aus der activity_result.xml und verbindet es mit dieser Java-Datei
        setContentView(R.layout.activity_result);

        // UI-Komponenten initialisieren
        TextView tvSummary = findViewById(R.id.tv_summary);
        Button btnNewQuiz = findViewById(R.id.btn_new_quiz);
        Button btnHome = findViewById(R.id.btn_go_home);

        // Daten aus dem Intent holen, der von der QuizActivity gesendet wurde.
        // Wir erwarten drei Listen: Fragen, die Antworten des Users und die korrekten Antworten.
        ArrayList<String> questions = getIntent().getStringArrayListExtra("QUESTIONS");
        ArrayList<String> userAnswers = getIntent().getStringArrayListExtra("USER_ANSWERS");
        ArrayList<String> correctAnswers = getIntent().getStringArrayListExtra("CORRECT_ANSWERS");

        // Wenn alle drei Listen vorhanden sind, rufen wir die Methode displayResults auf, um die Auswertung anzuzeigen.
        if (questions != null && userAnswers != null && correctAnswers != null) {
            displayResults(tvSummary, questions, userAnswers, correctAnswers);
        }

        // Bei Klick auf anderes Thema, wird der user zur CategoryActivity weitergeleitet, um eine neue Kategorie auszuwählen.
        btnNewQuiz.setOnClickListener(v -> {
            startActivity(new Intent(this, CategoryActivity.class));
            finish(); // Schließt die ResultActivity
        });

        // Bei Klick auf Hauptmenu, wird der user zurück zum Hauptmenü geleitet.
        btnHome.setOnClickListener(v -> {
            Intent intent = new Intent(this, HomeActivity.class);
            // FLAG_ACTIVITY_CLEAR_TOP sorgt dafür, dass alle Activities über der HomeActivity im Stapel gelöscht werden.
            // Der User soll nicht durch Drücken der Zurück-Taste bereits abgeschlossene Quiz-Runden erneut sehen.
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        });
    }

    // DIE AUSWERTUNGS-LOGIK
    private void displayResults(TextView tv, ArrayList<String> q, ArrayList<String> u, ArrayList<String> c) {
        // Wir starten mit einem leeren Text
        String summaryText = "";
        int score = 0;

        // Wir laufen mit einer Schleife durch alle Fragen durch
        for (int i = 0; i < q.size(); i++) {
            // Für jede Frage fügen wir die Frage, die Antwort des Users und die Korrektheit der Antwort zum Text hinzu.
            summaryText = summaryText + "Frage " + (i + 1) + ": " + q.get(i) + "\n";
            summaryText = summaryText + "Deine Antwort: " + u.get(i);

            if (u.get(i).equals(c.get(i))) {
                // Bei richtiger Antwort fügen wir ein Häkchen hinzu und erhöhen den Score um 1.
                summaryText = summaryText + " ✅\n\n";
                score++;
            } else {
                // Bei falscher Antwort fügen wir ein Kreuz hinzu und zeigen die korrekte Antwort an.
                summaryText = summaryText + " ❌\n(Korrekt: " + c.get(i) + ")\n\n";
            }
        }

        // Gesamtergebnis oben an den Text hängen und im TextView anzeigen
        String finalOutput = "Ergebnis: " + score + "/" + q.size() + "\n\n" + summaryText.toString();
        tv.setText(finalOutput);
    }
}