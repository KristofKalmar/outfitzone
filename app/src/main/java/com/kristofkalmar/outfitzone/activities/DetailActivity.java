package com.kristofkalmar.outfitzone.activities;

import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Window;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.kristofkalmar.outfitzone.R;
import com.kristofkalmar.outfitzone.databinding.ActivityDetailsBinding;
import com.kristofkalmar.outfitzone.helper.ImageLoader;
import com.kristofkalmar.outfitzone.models.ProductItem;

public class DetailActivity extends AppCompatActivity {
    private ActivityDetailsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Window window = getWindow();
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.black_black));
        window.setNavigationBarColor(ContextCompat.getColor(this, R.color.black_black));
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.black_black)));

        ImageView imageView = findViewById(R.id.details_image);
        String transitionName = getIntent().getStringExtra("transition_name");
        ProductItem product = getIntent().getSerializableExtra("product", ProductItem.class);

        imageView.setTransitionName(transitionName);

        Bitmap placeholder = ImageLoader.loadImageFromAssets(this, "placeholder_product.png");

        Glide.with(this).load(product.getImageURI()).error(placeholder).into(imageView);
    }
}
