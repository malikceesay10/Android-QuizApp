package com.example.quizapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.quizapp.activities.MainActivity;
import com.example.quizapp.R;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private EditText etEmail, etPassword;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // WICHTIG: Wir nutzen jetzt das eigene Login-Layout
        setContentView(R.layout.activity_login);

        // Firebase Initialisierung
        mAuth = FirebaseAuth.getInstance();

        // Verknüpfung der Views mit den neuen IDs aus activity_login.xml
        etEmail = findViewById(R.id.et_login_email);
        etPassword = findViewById(R.id.et_login_password);
        Button btnLoginExecute = findViewById(R.id.btn_login_execute);

        // Klick-Logik für den Login
        btnLoginExecute.setOnClickListener(v -> {
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if (!email.isEmpty() && !password.isEmpty()) {
                loginUser(email, password);
            } else {
                Toast.makeText(this, "Bitte alle Felder ausfüllen", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loginUser(String email, String password) {
        // Firebase Methode zum Einloggen bestehender User
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(this, "Willkommen zurück!", Toast.LENGTH_SHORT).show();

                        // Weiterleitung zur HomeActivity (Folie 08)
                        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                        startActivity(intent);

                        // finish() sorgt dafür, dass man beim Zurück-Button nicht wieder im Login landet
                        finish();
                    }
                });
    }
}