package com.example.quizapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.quizapp.R;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class QuizActivity extends AppCompatActivity {

    private TextView tvQuestion;
    private Button btnA, btnB, btnC, btnD;

    private List<Question> questionList = new ArrayList<>();
    private ArrayList<String> userAnswers = new ArrayList<>();
    private int currentQuestionIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        tvQuestion = findViewById(R.id.tv_question);
        btnA = findViewById(R.id.btn_optionA);
        btnB = findViewById(R.id.btn_optionB);
        btnC = findViewById(R.id.btn_optionC);
        btnD = findViewById(R.id.btn_optionD);

        btnA.setOnClickListener(v -> handleAnswer(btnA.getText().toString()));
        btnB.setOnClickListener(v -> handleAnswer(btnB.getText().toString()));
        btnC.setOnClickListener(v -> handleAnswer(btnC.getText().toString()));
        btnD.setOnClickListener(v -> handleAnswer(btnD.getText().toString()));

        loadQuestions();
    }

    private void loadQuestions() {
        String category = getIntent().getStringExtra("CATEGORY");
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("questions").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                List<Question> allAvailable = new ArrayList<>();
                for (QueryDocumentSnapshot doc : task.getResult()) {
                    Question q = doc.toObject(Question.class);
                    if (category.equals("Zufall") || q.getCategory().equals(category)) {
                        allAvailable.add(q);
                    }
                }
                Collections.shuffle(allAvailable);
                // Begrenzung auf maximal 5 Fragen
                questionList = allAvailable.subList(0, Math.min(5, allAvailable.size()));
                showNextQuestion();
            }
        });
    }

    private void showNextQuestion() {
        Question q = questionList.get(currentQuestionIndex);
        tvQuestion.setText(q.getQuestionText());
        btnA.setText(q.getOptionA());
        btnB.setText(q.getOptionB());
        btnC.setText(q.getOptionC());
        btnD.setText(q.getOptionD());
    }

    private void handleAnswer(String answer) {
        userAnswers.add(answer);
        currentQuestionIndex++;

        if (currentQuestionIndex < questionList.size()) {
            showNextQuestion();
        } else {
            // Daten für ResultActivity vorbereiten
            Intent intent = new Intent(this, ResultActivity.class);
            ArrayList<String> questions = new ArrayList<>();
            ArrayList<String> corrects = new ArrayList<>();
            for (Question q : questionList) {
                questions.add(q.getQuestionText());
                corrects.add(q.getCorrectAnswer());
            }
            intent.putStringArrayListExtra("QUESTIONS", questions);
            intent.putStringArrayListExtra("USER_ANSWERS", userAnswers);
            intent.putStringArrayListExtra("CORRECT_ANSWERS", corrects);
            startActivity(intent);
            finish();
        }
    }
}