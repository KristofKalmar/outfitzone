package com.kristofkalmar.outfitzone.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.core.content.ContextCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.kristofkalmar.outfitzone.R;
import com.kristofkalmar.outfitzone.helper.ImageLoader;
import com.kristofkalmar.outfitzone.helper.IsDarkMode;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {
    private Uri imageUri;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(this, R.color.background_secondary)));
        }

        Window window = getWindow();
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.background_secondary));
        window.setNavigationBarColor(ContextCompat.getColor(this, R.color.background_secondary));

        ImageView imageView = findViewById(R.id.logo);
        if (imageView != null) {
            Bitmap bitmap = ImageLoader.loadImageFromAssets(this, "placeholder" + ".png");
            if (bitmap != null) {
                imageView.setImageBitmap(bitmap);
            }
        }

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 69 && resultCode == RESULT_OK && data != null) {
            ImageView imageView = findViewById(R.id.logo);

            Uri imageUri = data.getData();

            this.imageUri = imageUri;
            imageView.setImageURI(imageUri);
        }
    }

    void registerData(String firstNameText, String lastNameText, String emailText, String passwordText, String imageURL) {
        Button registerButton = findViewById(R.id.button2);
        ProgressBar progressBar = findViewById(R.id.progressBar);
        AppCompatEditText firstNameField = findViewById(R.id.first_name_text);
        AppCompatEditText lastNameField = findViewById(R.id.last_name_text);
        AppCompatEditText emailField = findViewById(R.id.email_text);
        AppCompatEditText passwordField = findViewById(R.id.password_text);

        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        auth.createUserWithEmailAndPassword(emailText, passwordText)
        .addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Map<String, Object> user = new HashMap<>();
                user.put("firstName", firstNameText);
                user.put("lastName", lastNameText);
                user.put("email", emailText);
                user.put("imageURL", imageURL);

                FirebaseUser currentUser = auth.getCurrentUser();
                String uid = currentUser.getUid();

                db.collection("users").document(uid).set(user)
                .addOnSuccessListener(e -> {
                    Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                });
            } else {
                Toast.makeText(this, getString(R.string.user_registered), Toast.LENGTH_LONG).show();

                progressBar.setVisibility(View.INVISIBLE);
                registerButton.setText(getString(R.string.register));
                registerButton.setBackground(getDrawable(R.drawable.button_primary));
                registerButton.setActivated(true);
                firstNameField.setActivated(true);
                lastNameField.setActivated(true);
                emailField.setActivated(true);
                passwordField.setActivated(true);
            }
        });
    }

    void validate() {
        Button registerButton = findViewById(R.id.button2);
        ProgressBar progressBar = findViewById(R.id.progressBar);
        AppCompatEditText firstNameField = findViewById(R.id.first_name_text);
        AppCompatEditText lastNameField = findViewById(R.id.last_name_text);
        AppCompatEditText emailField = findViewById(R.id.email_text);
        AppCompatEditText passwordField = findViewById(R.id.password_text);

        progressBar.setVisibility(View.VISIBLE);
        registerButton.setText("");
        registerButton.setBackground(getDrawable(R.drawable.button_primary_disabled));
        registerButton.setActivated(false);
        firstNameField.setActivated(false);
        lastNameField.setActivated(false);
        emailField.setActivated(false);
        passwordField.setActivated(false);

        String firstNameText = Objects.requireNonNull(firstNameField.getText()).toString();
        String lastNameText = Objects.requireNonNull(lastNameField.getText()).toString();
        String emailText = Objects.requireNonNull(emailField.getText()).toString();
        String passwordText = Objects.requireNonNull(passwordField.getText()).toString();

        boolean firstNameError = false;
        boolean lastNameError = false;
        boolean emailError = false;
        boolean passwordError = false;

        if (firstNameText.isEmpty()) {
            firstNameField.setError(getString(R.string.empty_field));
            firstNameError = true;
        } else if (!firstNameText.matches("[a-zA-ZáéíóöőúüűÁÉÍÓÖŐÚÜŰ ]+")) {
            firstNameField.setError(getString(R.string.only_strings));
            firstNameError = true;
        } else {
            firstNameField.setError(null);
            firstNameError = false;
        }

        if (lastNameText.isEmpty()) {
            lastNameField.setError(getString(R.string.empty_field));
            lastNameError = true;
        } else if (!lastNameText.matches("[a-zA-ZáéíóöőúüűÁÉÍÓÖŐÚÜŰ ]+")) {
            lastNameField.setError(getString(R.string.only_strings));
            lastNameError = true;
        } else {
            lastNameField.setError(null);
            lastNameError = false;
        }

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

        if (!firstNameError && !lastNameError && !emailError && !passwordError) {
            if (imageUri != null) {
                FirebaseStorage storage = FirebaseStorage.getInstance();
                StorageReference storageRef = storage.getReference();
                StorageReference imageRef = storageRef.child("profile_images/" + this.imageUri.getLastPathSegment());
                UploadTask uploadTask = imageRef.putFile(this.imageUri);

                imageRef.getDownloadUrl().addOnSuccessListener(uri ->
                    uploadTask.addOnSuccessListener(taskSnapshot -> registerData(firstNameText, lastNameText, emailText, passwordText, uri.toString()))
                );
            } else {
                registerData(firstNameText, lastNameText, emailText, passwordText, "");
            }
        } else {
            progressBar.setVisibility(View.INVISIBLE);
            registerButton.setText(getString(R.string.register));
            registerButton.setBackground(getDrawable(R.drawable.button_primary));
            registerButton.setActivated(true);
            firstNameField.setActivated(true);
            lastNameField.setActivated(true);
            emailField.setActivated(true);
            passwordField.setActivated(true);
        }
    }

    void pickImage() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 69);
    }

    void setupInputs() {
        AppCompatButton registerButton = findViewById(R.id.button2);
        AppCompatButton imagePickerButton = findViewById(R.id.image_picker_button);

        imagePickerButton.setOnClickListener(e -> pickImage());
        registerButton.setOnClickListener(e -> validate());
    }
}
