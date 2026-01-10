package com.example.quizapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Button halloButton = findViewById(R.id.button3);

        halloButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                halloButton.setText("Welt");
            }
        });

        final Button okButton = findViewById(R.id.okButton);

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Ihr Code hier:
                Toast.makeText(view.getContext(), "OK gedrückt", Toast.LENGTH_SHORT).show();
            }
        });
    }
}