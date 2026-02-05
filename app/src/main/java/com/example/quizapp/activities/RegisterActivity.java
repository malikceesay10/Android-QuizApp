package com.example.quizapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.quizapp.R;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {

    private EditText etEmail, etPassword, etPasswordConfirm;
    private Button btnRegister;
    private FirebaseAuth mAuth; // Firebase Zugriff

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // 1. Firebase Dienst holen
        mAuth = FirebaseAuth.getInstance();

        // 2. XML Elemente finden
        etEmail = findViewById(R.id.et_email);
        etPassword = findViewById(R.id.et_password);
        etPasswordConfirm = findViewById(R.id.et_password_confirm);
        btnRegister = findViewById(R.id.btn_register_execute);

        // 3. Klick auf den Button
        btnRegister.setOnClickListener(v -> {
            String email = etEmail.getText().toString().trim();
            String pass = etPassword.getText().toString().trim();
            String confirm = etPasswordConfirm.getText().toString().trim();

            if (pass.equals(confirm) && pass.length() >= 6) {
                registerUser(email, pass);
            } else {
                Toast.makeText(this, "Passwörter stimmen nicht überein oder zu kurz!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void registerUser(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(this, "Registrierung erfolgreich!", Toast.LENGTH_SHORT).show();

                        // Direkt nach Reg einloggen und zur Startseite
                        Intent intent = new Intent(RegisterActivity.this, HomeActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
    }
}