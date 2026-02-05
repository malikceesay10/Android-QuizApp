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

        btnErdkunde.setOnClickListener(v -> startQuiz("Erdkunde"));
        btnRandom.setOnClickListener(v -> startQuiz("Zufall"));
    }

    private void startQuiz(String category) {
        Intent intent = new Intent(this, QuizActivity.class);
        intent.putExtra("CATEGORY", category);
        startActivity(intent);
    }
}