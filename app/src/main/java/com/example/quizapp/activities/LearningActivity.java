package com.example.quizapp.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.quizapp.R;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;
import java.util.List;

public class LearningActivity extends AppCompatActivity {

    private RecyclerView rvLearningList;
    private LinearLayout layoutSingleQuestion;
    private TextView tvQuestion;
    private Button btnA, btnB, btnC, btnD, btnBack;

    private List<Question> questionList = new ArrayList<>();
    private QuestionAdapter adapter;
    private Question currentQuestion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learning);

        rvLearningList = findViewById(R.id.rv_learning_list);
        layoutSingleQuestion = findViewById(R.id.layout_single_question);
        tvQuestion = findViewById(R.id.tv_question);
        btnA = findViewById(R.id.btn_optionA);
        btnB = findViewById(R.id.btn_optionB);
        btnC = findViewById(R.id.btn_optionC);
        btnD = findViewById(R.id.btn_optionD);
        btnBack = findViewById(R.id.btn_back_to_list);

        rvLearningList.setLayoutManager(new LinearLayoutManager(this));
        adapter = new QuestionAdapter(questionList, this::showQuestion);
        rvLearningList.setAdapter(adapter);

        btnBack.setOnClickListener(v -> {
            layoutSingleQuestion.setVisibility(View.GONE);
            rvLearningList.setVisibility(View.VISIBLE);
        });

        View.OnClickListener listener = v -> checkAnswer(((Button)v).getText().toString());
        btnA.setOnClickListener(listener);
        btnB.setOnClickListener(listener);
        btnC.setOnClickListener(listener);
        btnD.setOnClickListener(listener);

        loadQuestions();
    }

    private void loadQuestions() {
        String category = getIntent().getStringExtra("CATEGORY");
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Query query = category.equals("Zufall") ? db.collection("questions") : db.collection("questions").whereEqualTo("category", category);

        query.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                questionList.clear();
                for (QueryDocumentSnapshot doc : task.getResult()) {
                    questionList.add(doc.toObject(Question.class));
                }
                adapter.notifyDataSetChanged();
            }
        });
    }

    private void showQuestion(Question q) {
        currentQuestion = q;
        rvLearningList.setVisibility(View.GONE);
        layoutSingleQuestion.setVisibility(View.VISIBLE);
        tvQuestion.setText(q.getQuestionText());
        btnA.setText(q.getOptionA());
        btnB.setText(q.getOptionB());
        btnC.setText(q.getOptionC());
        btnD.setText(q.getOptionD());
    }

    private void checkAnswer(String answer) {
        String result = answer.equals(currentQuestion.getCorrectAnswer()) ? "Richtig!" : "Falsch! Lösung: " + currentQuestion.getCorrectAnswer();
        Toast.makeText(this, result, Toast.LENGTH_SHORT).show();
    }
}