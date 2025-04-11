package com.kristofkalmar.outfitzone.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.kristofkalmar.outfitzone.R;
import com.kristofkalmar.outfitzone.helper.ImageLoader;
import com.kristofkalmar.outfitzone.helper.IsDarkMode;

public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        ImageView imageView = findViewById(R.id.welcome_logo);
        if (imageView != null) {
            boolean isDarkMode = IsDarkMode.isDarkModeEnabled(this);
            String imageFileName = isDarkMode ? "logo_dark" : "logo_light";

            Bitmap bitmap = ImageLoader.loadImageFromAssets(this, imageFileName + ".png");
            if (bitmap != null) {
                imageView.setImageBitmap(bitmap);
            }
        }

        ImageView heroImage = findViewById(R.id.hero_image);
        if (heroImage != null) {
            Bitmap bitmap = ImageLoader.loadImageFromAssets(this, "hero_image" + ".jpg");
            if (bitmap != null) {
                heroImage.setImageBitmap(bitmap);
            }
        }

        Button loginButton = findViewById(R.id.login_button);

        loginButton.setOnClickListener(event -> {
            Intent intent = new Intent(WelcomeActivity.this, LoginActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_in_right_bg);
        });

        Button registerButton = findViewById(R.id.register_button);

        registerButton.setOnClickListener(event -> {
            Intent intent = new Intent(WelcomeActivity.this, RegisterActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_in_right_bg);
        });
    }
}
