package com.example.quizapp.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.quizapp.R;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class LearningActivity extends AppCompatActivity {

    private LinearLayout llQuestionContainer;
    // Hier wird die Ansicht definiert, die die Details einer einzelnen Frage zeigt (Frage + Antworten)
    private LinearLayout layoutSingleQuestion;
    // Hier werden die UI-Elemente für die Detailansicht definiert
    private TextView tvQuestion;
    private Button btnA, btnB, btnC, btnD, btnBack;

    // Diese Variable speichert die aktuell ausgewählte Frage, damit wir sie in der Detailansicht und bei der Antwortprüfung nutzen können
    private Question selectedQuestion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Das Layout "activity_learning" wird geladen und mit dieser Java-Datei verbunden
        setContentView(R.layout.activity_learning);

        // Hier werden die UI-Komponenten mit den IDs aus der XML-Datei verknüpft, damit wir sie im Code ansprechen können
        llQuestionContainer = findViewById(R.id.ll_question_container);
        layoutSingleQuestion = findViewById(R.id.layout_single_question);
        tvQuestion = findViewById(R.id.tv_question);
        btnA = findViewById(R.id.btn_optionA);
        btnB = findViewById(R.id.btn_optionB);
        btnC = findViewById(R.id.btn_optionC);
        btnD = findViewById(R.id.btn_optionD);
        btnBack = findViewById(R.id.btn_back_to_list);

        // Zurück-Button schaltet die Ansicht einfach wieder um
        btnBack.setOnClickListener(v -> showListView());

        // Gemeinsamer Listener für die Antwort-Buttons (A, B, C, D)
        View.OnClickListener answerListener = v -> {
            Button geklickterButton = (Button) v;
            checkAnswer(geklickterButton.getText().toString());
        };
        btnA.setOnClickListener(answerListener);
        btnB.setOnClickListener(answerListener);
        btnC.setOnClickListener(answerListener);
        btnD.setOnClickListener(answerListener);

        // Jetzt laden wir die Fragen aus der Datenbank, damit sie in der Liste angezeigt werden können
        loadQuestions();
    }

    private void loadQuestions() {
        // 1. Kategorie aus dem Intent auslesen
        String selectedCategory = getIntent().getStringExtra("CATEGORY");
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // 2. Abfrage für Firebase vorbereiten: Je nach Kategorie entweder alle Fragen oder nur die passenden Fragen laden
        com.google.firebase.firestore.Query questionQuery;

        // Wenn "Zufall" gewählt wurde, laden wir alles, sonst filtern wir
        if (selectedCategory.equals("Zufall")) {
            // Fall A: Alle Fragen aus der Sammlung "questions"
            questionQuery = db.collection("questions");
        } else {
            // Fall B: Nur Fragen, deren Feld "category" mit der Auswahl übereinstimmt
            questionQuery = db.collection("questions").whereEqualTo("category", selectedCategory);
        }

        // 3. Die Abfrage bei Firebase ausführen
        questionQuery.get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                // Den Layout-Behälter leeren, bevor wir neue Buttons hinzufügen
                llQuestionContainer.removeAllViews();

                // Wir gehen jedes gefundene Dokument einzeln durch
                for (QueryDocumentSnapshot document : task.getResult()) {
                    // Das Dokument in ein Java-Objekt der Klasse "Question" umwandeln
                    Question questionData = document.toObject(Question.class);

                    // Einen neuen Button für die Liste erstellen
                    Button btnQuestion = new Button(this);
                    btnQuestion.setText(questionData.getQuestionText());

                    // Klick-Aktion für jeden einzelnen Button festlegen
                    btnQuestion.setOnClickListener(v -> {
                        selectedQuestion = questionData;

                        // Die Detail-Ansicht mit den Daten der Frage füllen
                        tvQuestion.setText(questionData.getQuestionText());
                        btnA.setText(questionData.getOptionA());
                        btnB.setText(questionData.getOptionB());
                        btnC.setText(questionData.getOptionC());
                        btnD.setText(questionData.getOptionD());

                        // Von der Liste zur Einzelansicht umschalten
                        showDetailView();
                    });

                    llQuestionContainer.addView(btnQuestion);
                }
            }
        });
    }

    // Prüft, ob die getippte Antwort korrekt ist
    private void checkAnswer(String answer) {
        if (answer.equals(selectedQuestion.getCorrectAnswer())) {
            Toast.makeText(this, "Richtig! ✅", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Falsch! Lösung: " + selectedQuestion.getCorrectAnswer(), Toast.LENGTH_LONG).show();
        }
    }

    // Diese Methode steuert die Sichtbarkeit der beiden Hauptbereiche: Liste und Detailansicht
    private void showListView() {
        // Wenn die Liste angezeigt werden soll, verstecken wir die Detailansicht und zeigen den Container mit den Fragen-Buttons
        layoutSingleQuestion.setVisibility(View.GONE);
        llQuestionContainer.setVisibility(View.VISIBLE);
    }

    private void showDetailView() {
        // Wenn die Detailansicht angezeigt werden soll, verstecken wir den Container mit den Fragen-Buttons und zeigen die Detailansicht
        llQuestionContainer.setVisibility(View.GONE);
        layoutSingleQuestion.setVisibility(View.VISIBLE);
    }
}