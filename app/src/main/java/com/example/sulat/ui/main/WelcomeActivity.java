package com.example.sulat.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.splashscreen.SplashScreen;

import com.example.sulat.R;
import com.example.sulat.ui.auth.LoginActivity;
import com.example.sulat.ui.auth.SignUpActivity;
import com.google.firebase.auth.FirebaseAuth;

public class WelcomeActivity extends AppCompatActivity{
    Button btnLogin, btnSignup;
    FirebaseAuth fAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //SplashScreen.installSplashScreen(this);
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_welcome);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        //BUTTONS FOR LOGIN AND SIGNUP
        btnLogin = findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(v -> {
            Intent intentLogin = new Intent(WelcomeActivity.this, LoginActivity.class);
            startActivity(intentLogin);
            finish();
        });


        btnSignup = findViewById(R.id.btnRegister);
        btnSignup.setOnClickListener(v -> {
            Intent intentSignup = new Intent(WelcomeActivity.this, SignUpActivity.class);
            startActivity(intentSignup);
            finish();
        });
    }
}