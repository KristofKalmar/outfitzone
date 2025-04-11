package com.kristofkalmar.outfitzone.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kristofkalmar.outfitzone.models.NavigationListItem;
import com.kristofkalmar.outfitzone.ui.components.navigationlistitem.NavigationListItemFragment;

import java.util.List;

public class NavigationListAdapter extends RecyclerView.Adapter<NavigationListAdapter.ListViewHolder> {
    private final List<NavigationListItem> items;
    private final FragmentManager fragmentManager;

    public NavigationListAdapter(List<NavigationListItem> items, FragmentManager fragmentManager) {
        this.items = items;
        this.fragmentManager = fragmentManager;
    }

    @NonNull
    @Override
    public ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        FrameLayout frameLayout = new FrameLayout(parent.getContext());
        frameLayout.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        ));
        frameLayout.setId(View.generateViewId());
        return new ListViewHolder(frameLayout);
    }

    @Override
    public void onBindViewHolder(@NonNull ListViewHolder holder, int position) {
        NavigationListItemFragment fragment = new NavigationListItemFragment();

        fragmentManager.beginTransaction()
                .replace(holder.fragmentContainer.getId(), fragment)
                .commit();

        boolean hideBorder = position == items.size() - 1;

        fragment.bindData(items.get(position), hideBorder);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class ListViewHolder extends RecyclerView.ViewHolder {
        FrameLayout fragmentContainer;

        public ListViewHolder(@NonNull FrameLayout itemView) {
            super(itemView);
            fragmentContainer = itemView;
        }
    }
}