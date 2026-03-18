package com.example.quizapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.quizapp.R;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class QuizActivity extends AppCompatActivity {

    private TextView tvQuestion, tvTimer;
    // Buttons für die vier Antwortmöglichkeiten
    private Button btnA, btnB, btnC, btnD;

    // Liste, in der die aus Firestore geladenen Fragen gespeichert werden
    private List<Question> questionList = new ArrayList<>();
    // Liste, die die vom User gewählten Antworten speichert
    private ArrayList<String> userAnswers = new ArrayList<>();

    private int currentQuestionIndex = 0;
    // Der Timer-Prozess für das Zeitlimit pro Frage
    private CountDownTimer countDownTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Lädt das Layout für die activity_quiz.xml und verbindet es mit dieser Java-Datei
        setContentView(R.layout.activity_quiz);

        // Verknüpft die UI-Komponenten mit den IDs aus der XML-Datei
        tvQuestion = findViewById(R.id.tv_question);
        tvTimer = findViewById(R.id.tv_timer);
        btnA = findViewById(R.id.btn_optionA);
        btnB = findViewById(R.id.btn_optionB);
        btnC = findViewById(R.id.btn_optionC);
        btnD = findViewById(R.id.btn_optionD);

        // Click-Listener für die Antwort-Buttons
        // Wenn ein Button geklickt wird, rufen wir die Methode handleAnswer auf und übergeben den Text der gewählten Antwort.
        btnA.setOnClickListener(v -> handleAnswer(btnA.getText().toString()));
        btnB.setOnClickListener(v -> handleAnswer(btnB.getText().toString()));
        btnC.setOnClickListener(v -> handleAnswer(btnC.getText().toString()));
        btnD.setOnClickListener(v -> handleAnswer(btnD.getText().toString()));

        // Startet den Prozess, um die Fragen aus der Cloud zu holen
        loadQuestionsFromFirestore();
    }

    // Diese Methode lädt die Fragen aus der Firestore-Datenbank basierend auf der gewählten Kategorie
    private void loadQuestionsFromFirestore() {
        // Wir lesen die gewählte Kategorie aus dem Intent aus (aus CategoryActivity)
        String category = getIntent().getStringExtra("CATEGORY");
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Dynamische Abfrage: Wenn "Zufall" gewählt wurde, laden wir alle Fragen,
        // ansonsten filtern wir in der Datenbank nur nach der gewählten Kategorie.
        Query query = category.equals("Zufall") ?
                db.collection("questions") :
                db.collection("questions").whereEqualTo("category", category);

        // Asynchrone Anfrage an Firestore, um die Fragen zu laden. Sobald die Daten zurückkommen, wird der Listener aufgerufen.
        query.get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                List<Question> allQuestions = new ArrayList<>();
                for (QueryDocumentSnapshot doc : task.getResult()) {
                    // Jedes Dokument wird in ein Question-Objekt umgewandelt und zur Liste hinzugefügt
                    allQuestions.add(doc.toObject(Question.class));
                }

                // Wir mischen die Fragen und nehmen nur die ersten 5 für eine Runde
                Collections.shuffle(allQuestions);
                questionList = allQuestions.subList(0, Math.min(5, allQuestions.size()));

                // Nachdem die Fragen geladen und vorbereitet sind, zeigen wir die erste Frage an.
                if (!questionList.isEmpty()) {
                    showNextQuestion();
                } else {
                    finish(); // Sicherheitshalber schließen, falls die Liste leer ist
                }
            }
        });
    }

    // Bereitet die Anzeige für die nächste Frage vor
    private void showNextQuestion() {
        // Falls noch ein Timer der vorherigen Frage läuft, stoppen wir diesen
        if (countDownTimer != null) countDownTimer.cancel();

        // Wir holen die aktuelle Frage basierend auf dem Index aus der Liste
        Question q = questionList.get(currentQuestionIndex);

        // Texte der Frage und der vier Antwortmöglichkeiten im UI setzen
        tvQuestion.setText(q.getQuestionText());
        btnA.setText(q.getOptionA());
        btnB.setText(q.getOptionB());
        btnC.setText(q.getOptionC());
        btnD.setText(q.getOptionD());

        // Den Countdown für diese Frage starten
        startTimer();
    }

    // Diese Methode startet den Countdown-Timer für die aktuelle Frage. Wenn die Zeit abläuft, wird automatisch eine Antwort verarbeitet.
    private void startTimer() {
        countDownTimer = new CountDownTimer(11000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                tvTimer.setText(String.valueOf(millisUntilFinished / 1000));
            }

            @Override
            public void onFinish() {
                // Wenn die Zeit abläuft, wird dies als automatische Antwort gewertet
                handleAnswer("Zeit abgelaufen");
            }
        }.start();
    }

    // Diese Methode verarbeitet die Antwort des Users
    private void handleAnswer(String answer) {
        // Sobald eine Antwort verarbeitet wird, stoppen wir den Timer
        if (countDownTimer != null) countDownTimer.cancel();

        // Die gegebene Antwort wird in der Liste der User-Antworten gespeichert.
        userAnswers.add(answer);
        // Der Index wird erhöht, um zur nächsten Frage zu wechseln.
        currentQuestionIndex++;

        // Wenn noch Fragen übrig sind, zeigen wir die nächste Frage an.
        if (currentQuestionIndex < questionList.size()) {
            showNextQuestion();
        } else {
            // Wenn alle 5 Fragen beantwortet sind, geht es zur Auswertung
            goToResults();
        }
    }

    // Übergibt die Ergebnisse an die ResultActivity
    private void goToResults() {
        Intent intent = new Intent(this, ResultActivity.class);

        // Wir bereiten zwei Listen vor: Eine mit den Fragen und eine mit den korrekten Antworten, damit die ResultActivity diese Informationen erhält.
        ArrayList<String> questions = new ArrayList<>();
        ArrayList<String> corrects = new ArrayList<>();

        // Wir durchlaufen die Liste der Fragen und füllen die beiden Listen mit den entsprechenden Daten
        for (Question q : questionList) {
            questions.add(q.getQuestionText());
            corrects.add(q.getCorrectAnswer());
        }

        // Die Listen werden als Extras im Intent gespeichert, damit die ResultActivity sie abrufen und anzeigen kann.
        intent.putStringArrayListExtra("QUESTIONS", questions);
        intent.putStringArrayListExtra("USER_ANSWERS", userAnswers);
        intent.putStringArrayListExtra("CORRECT_ANSWERS", corrects);

        startActivity(intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Sicherheitshalber stoppen wir den Timer, falls die Activity unerwartet geschlossen wird.
        if (countDownTimer != null) countDownTimer.cancel();
    }
}