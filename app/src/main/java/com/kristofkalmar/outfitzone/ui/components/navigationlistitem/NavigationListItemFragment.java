package com.kristofkalmar.outfitzone.ui.components.navigationlistitem;

import android.os.Bundle;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.graphics.Bitmap;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.kristofkalmar.outfitzone.R;
import com.kristofkalmar.outfitzone.helper.ImageLoader;
import com.kristofkalmar.outfitzone.helper.IsDarkMode;
import com.kristofkalmar.outfitzone.models.NavigationListItem;
import com.kristofkalmar.outfitzone.models.view.NavigationListItemViewModel;

public class NavigationListItemFragment extends Fragment {
    private boolean hideBorder;
    private NavigationListItem data;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_navigation_item, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        NavigationListItemViewModel viewModel = new ViewModelProvider(this).get(NavigationListItemViewModel.class);

        if (viewModel.getCurrentItem() != null) {
            data = viewModel.getCurrentItem();
        }

        viewModel.setCurrentItem(data);
        updateUI();
    }

    private void updateUI() {
        if (hideBorder) {
            View border = getView().findViewById(R.id.navigation_item_border);

            if (border != null) {
                ViewGroup parent = (ViewGroup) border.getParent();
                if (parent != null) {
                    parent.removeView(border);
                }
            }
        }

        if (getView() != null) {
            TextView titleView = getView().findViewById(R.id.navigation_item_title);
            if (titleView != null) {
                titleView.setText(data.getTitle());
            }

            ImageView imageView = getView().findViewById(R.id.navigation_item_image);
            if (imageView != null) {
                boolean isDarkMode = IsDarkMode.isDarkModeEnabled(requireContext());
                String imageFileName = isDarkMode ? data.getImageURI() + "_dark" : data.getImageURI();

                Bitmap bitmap = ImageLoader.loadImageFromAssets(requireContext(), imageFileName + ".png");
                if (bitmap != null) {
                    imageView.setImageBitmap(bitmap);
                }
            }
        }
    }

    public void bindData(NavigationListItem data, boolean hideBorder) {
        this.hideBorder = hideBorder;
        this.data = data;
    }
}
