package com.kristofkalmar.outfitzone.models.view;

import androidx.lifecycle.ViewModel;

import com.kristofkalmar.outfitzone.models.NavigationListItem;

public class NavigationListItemViewModel extends ViewModel {
    private NavigationListItem currentItem;

    public NavigationListItem getCurrentItem() {
        return currentItem;
    }

    public void setCurrentItem(NavigationListItem currentItem) {
        this.currentItem = currentItem;
    }
}
