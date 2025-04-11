package com.kristofkalmar.outfitzone.activities;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.core.content.ContextCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.kristofkalmar.outfitzone.R;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(this, R.color.background_secondary)));
        }

        Window window = getWindow();
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.background_secondary));
        window.setNavigationBarColor(ContextCompat.getColor(this, R.color.background_secondary));

        setupInputs();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_out_right, R.anim.slide_out_right_bg);
    }

    void validate() {
        Button registerButton = findViewById(R.id.button2);
        ProgressBar progressBar = findViewById(R.id.progressBar);
        AppCompatEditText emailField = findViewById(R.id.email_text);
        AppCompatEditText passwordField = findViewById(R.id.password_text);

        progressBar.setVisibility(View.VISIBLE);
        registerButton.setText("");
        registerButton.setBackground(getDrawable(R.drawable.button_primary_disabled));
        registerButton.setActivated(false);
        emailField.setActivated(false);
        passwordField.setActivated(false);

        String emailText = Objects.requireNonNull(emailField.getText()).toString();
        String passwordText = Objects.requireNonNull(passwordField.getText()).toString();

        boolean emailError = false;
        boolean passwordError = false;

        if (emailText.isEmpty()) {
            emailField.setError(getString(R.string.empty_field));
            emailError = true;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(emailText).matches()) {
            emailField.setError(getString(R.string.incorrect_email));
            emailError = true;
        } else {
            emailField.setError(null);
            emailError = false;
        }

        if (passwordText.isEmpty()) {
            passwordField.setError(getString(R.string.empty_field));
            passwordError = true;
        } else if (passwordText.length() < 8 || !passwordText.matches(".*\\d.*")) {
            passwordField.setError(getString(R.string.password_weak));
            passwordError = true;
        } else {
            passwordField.setError(null);
            passwordError = false;
        }

        if (!emailError && !passwordError) {
            FirebaseAuth auth = FirebaseAuth.getInstance();

            auth.signInWithEmailAndPassword(emailText, passwordText)
            .addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                } else {
                    emailField.setError(getString(R.string.bad_auth));

                    progressBar.setVisibility(View.INVISIBLE);
                    registerButton.setText(getString(R.string.register));
                    registerButton.setBackground(getDrawable(R.drawable.button_primary));
                    registerButton.setActivated(true);
                    emailField.setActivated(true);
                    passwordField.setActivated(true);
                }
            });
        } else {
            progressBar.setVisibility(View.INVISIBLE);
            registerButton.setText(getString(R.string.register));
            registerButton.setBackground(getDrawable(R.drawable.button_primary));
            registerButton.setActivated(true);
            emailField.setActivated(true);
            passwordField.setActivated(true);
        }
    }

    void setupInputs() {
        AppCompatButton loginButton = findViewById(R.id.button2);

        loginButton.setOnClickListener(e -> validate());
    }
}
