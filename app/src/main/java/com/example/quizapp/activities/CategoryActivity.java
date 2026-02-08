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
        setContentView(R.layout.activity_category);

        Button btnErdkunde = findViewById(R.id.btn_erdkunde);
        Button btnRandom = findViewById(R.id.btn_random);
        Button btnWissenschaft = findViewById(R.id.btn_wissenschaft);
        Button btnSport = findViewById(R.id.btn_sport);
        Button btnEssen = findViewById(R.id.btn_essen);

        btnErdkunde.setOnClickListener(v -> startQuiz("Erdkunde"));
        btnWissenschaft.setOnClickListener(v -> startQuiz("Wissenschaft"));
        btnSport.setOnClickListener(v -> startQuiz("Sport"));
        btnEssen.setOnClickListener(v -> startQuiz("Essen"));
        btnRandom.setOnClickListener(v -> startQuiz("Zufall"));
    }

    private void startQuiz(String category) {
        String mode = getIntent().getStringExtra("MODE");
        Intent intent;
        if ("learning".equals(mode)) {
            intent = new Intent(this, LearningActivity.class);
        } else {
            intent = new Intent(this, QuizActivity.class);
        }
        intent.putExtra("CATEGORY", category);
        startActivity(intent);
    }
}