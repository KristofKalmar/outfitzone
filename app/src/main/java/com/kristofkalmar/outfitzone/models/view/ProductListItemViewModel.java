package com.kristofkalmar.outfitzone.models.view;

import androidx.lifecycle.ViewModel;

import com.kristofkalmar.outfitzone.models.ProductItem;

public class ProductListItemViewModel extends ViewModel {
    private ProductItem currentItem;

    public ProductItem getCurrentItem() {
        return currentItem;
    }

    public void setCurrentItem(ProductItem currentItem) {
        this.currentItem = currentItem;
    }
}
