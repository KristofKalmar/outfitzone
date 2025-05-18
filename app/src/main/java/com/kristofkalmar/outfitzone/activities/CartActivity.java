package com.kristofkalmar.outfitzone.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.kristofkalmar.outfitzone.R;
import com.kristofkalmar.outfitzone.ui.components.productlist.ProductListFragment;

import java.util.ArrayList;

public class CartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        ProductListFragment childFragment = new ProductListFragment(true, new ArrayList<>());
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.product_list_container_2, childFragment).commit();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_out_right, R.anim.slide_out_right_bg);
    }
}
