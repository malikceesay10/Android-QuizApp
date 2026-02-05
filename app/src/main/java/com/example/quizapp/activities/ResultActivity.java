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
        setContentView(R.layout.activity_result);

        // Verknüpfung der Views aus activity_result.xml
        TextView tvSummary = findViewById(R.id.tv_summary);
        Button btnNewQuiz = findViewById(R.id.btn_new_quiz);
        Button btnHome = findViewById(R.id.btn_go_home);

        // Daten aus dem Intent abrufen, die in QuizActivity übergeben wurden
        ArrayList<String> questions = getIntent().getStringArrayListExtra("QUESTIONS");
        ArrayList<String> userAnswers = getIntent().getStringArrayListExtra("USER_ANSWERS");
        ArrayList<String> correctAnswers = getIntent().getStringArrayListExtra("CORRECT_ANSWERS");

        // Zusammenfassungstext erstellen
        StringBuilder sb = new StringBuilder();
        if (questions != null && userAnswers != null && correctAnswers != null) {
            for (int i = 0; i < questions.size(); i++) {
                sb.append("Frage ").append(i + 1).append(": ").append(questions.get(i)).append("\n");
                sb.append("Deine Wahl: ").append(userAnswers.get(i));

                if (userAnswers.get(i).equals(correctAnswers.get(i))) {
                    sb.append(" ✅\n\n");
                } else {
                    sb.append(" ❌\n(Richtig: ").append(correctAnswers.get(i)).append(")\n\n");
                }
            }
        }
        tvSummary.setText(sb.toString());

        // Button: Neues Quiz (leitet zur Kategoriewahl zurück)
        btnNewQuiz.setOnClickListener(v -> {
            Intent intent = new Intent(ResultActivity.this, CategoryActivity.class);
            startActivity(intent);
            finish();
        });

        // Button: Startseite (bereinigt den Activity-Stapel)
        btnHome.setOnClickListener(v -> {
            Intent intent = new Intent(ResultActivity.this, HomeActivity.class);
            // Diese Flags löschen CategoryActivity und QuizActivity aus dem Hintergrund-Speicher
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
        });
    }
}