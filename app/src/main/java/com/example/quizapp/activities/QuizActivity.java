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
    private Button btnA, btnB, btnC, btnD;
    private List<Question> questionList = new ArrayList<>();
    private ArrayList<String> userAnswers = new ArrayList<>();
    private int currentQuestionIndex = 0;
    private CountDownTimer countDownTimer;
    private final long START_TIME_IN_MILLIS = 10000; // 10 Sekunden

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        tvQuestion = findViewById(R.id.tv_question);
        tvTimer = findViewById(R.id.tv_timer);
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

        Query query;
        if (category.equals("Zufall")) {
            query = db.collection("questions");
        } else {
            query = db.collection("questions").whereEqualTo("category", category);
        }

        query.get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null && !task.getResult().isEmpty()) {
                List<Question> allAvailable = new ArrayList<>();
                for (QueryDocumentSnapshot doc : task.getResult()) {
                    allAvailable.add(doc.toObject(Question.class));
                }
                Collections.shuffle(allAvailable);
                questionList = allAvailable.subList(0, Math.min(5, allAvailable.size()));
                showNextQuestion();
            } else {
                finish();
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

        startTimer();
    }

    private void startTimer() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }

        countDownTimer = new CountDownTimer(11000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                tvTimer.setText(String.valueOf(millisUntilFinished / 1000));
            }
            @Override
            public void onFinish() {
                tvTimer.setText("0");
                handleAnswer("Zeit abgelaufen");
            }
        }.start();
    }

    private void handleAnswer(String answer) {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }

        userAnswers.add(answer);
        currentQuestionIndex++;

        if (currentQuestionIndex < questionList.size()) {
            showNextQuestion();
        } else {
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }
}