package com.example.voiceprescription;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {
    private static final String TAG = "Login";
    private boolean login;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();
        Intent intent = getIntent();
        Button loginBtn = findViewById(R.id.loginBtn);

        if (intent.hasExtra("com.example.voiceprescription.LOGIN")) {
            login = intent.getExtras().getBoolean("com.example.voiceprescription.LOGIN");
            Log.d(TAG, "onCreate: flag= " + login);
            if (!login) {
                loginBtn.setText("Sign Up");
            }
        }

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: login clicked");
                EditText emailEditText = findViewById(R.id.emailET);
                EditText passEditText = findViewById(R.id.passEditText);
                String password = passEditText.getText().toString();
                String email = emailEditText.getText().toString();
                if (login) {
                    Log.d(TAG, "onClick: login true, trying!");
                    mAuth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        FirebaseUser user = mAuth.getCurrentUser();
                                        Log.d(TAG, "onComplete: userLogIn-" + user.getEmail());
                                        finish();
                                    }
                                }
                            });
                } else {
                    Log.d(TAG, "onClick: login false, trying!");
                    mAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        FirebaseUser user = mAuth.getCurrentUser();
                                        Log.d(TAG, "onComplete: userSignedUp- " + user.getEmail());
                                        finish();
                                    } else {
                                        Log.d(TAG, "onComplete: userSignUp FAILED!");
                                    }
                                }
                            });
                }
            }
        });

    }
}
