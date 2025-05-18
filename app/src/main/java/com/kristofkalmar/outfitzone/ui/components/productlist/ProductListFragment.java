package com.kristofkalmar.outfitzone.ui.components.productlist;

import android.graphics.Rect;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kristofkalmar.outfitzone.R;
import com.kristofkalmar.outfitzone.adapter.ProductListAdapter;
import com.kristofkalmar.outfitzone.databinding.FragmentProductListBinding;
import com.kristofkalmar.outfitzone.models.ProductItem;
import com.kristofkalmar.outfitzone.ui.more.MoreViewModel;

import java.util.ArrayList;
import java.util.List;

public class ProductListFragment extends Fragment {

    private Boolean horizontal = false;
    private FragmentProductListBinding binding;
    private List<ProductItem> data;
    private Boolean showCartButton = true;
    private RecyclerView recyclerView;
    private ProductListAdapter adapter;

    public ProductListFragment() {
        this.horizontal = false;
    }

    public ProductListFragment(Boolean horizontal, List<ProductItem> data) {
        this.horizontal = horizontal;
        this.data = data;
    }

    public ProductListFragment(Boolean horizontal, List<ProductItem> data, Boolean showCartButton) {
        this.horizontal = horizontal;
        this.data = data;
        this.showCartButton = showCartButton;
    }

    public RecyclerView getRecyclerView() {
        return recyclerView;
    }

    public interface OnRecyclerReadyListener {
        void onReady(RecyclerView recyclerView);
    }

    private OnRecyclerReadyListener listener;

    public void setOnRecyclerReadyListener(OnRecyclerReadyListener listener) {
        this.listener = listener;
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        MoreViewModel moreViewModel =
                new ViewModelProvider(this).get(MoreViewModel.class);

        binding = FragmentProductListBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        return root;
    }

    public void addItems(List<ProductItem> items) {
        adapter.addItems(items);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView recyclerView = getView().findViewById(R.id.product_list_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        this.recyclerView = recyclerView;

        if (!this.horizontal) {
            recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));

            int spacingPx = (int) TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP, 8, getContext().getResources().getDisplayMetrics());

            recyclerView.addItemDecoration(new TwoColumnSpacingDecoration(spacingPx));
        }

        ProductListAdapter adapter = new ProductListAdapter(this.data, getActivity().getSupportFragmentManager(), this.showCartButton);
        adapter.setHorizontal(this.horizontal);
        adapter.setShowCartButton(this.showCartButton);
        recyclerView.setAdapter(adapter);
        this.adapter = adapter;

        if (listener != null) {
            listener.onReady(recyclerView);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    static class TwoColumnSpacingDecoration extends RecyclerView.ItemDecoration {
        private final int spacing;

        public TwoColumnSpacingDecoration(int spacing) {
            this.spacing = spacing;
        }

        @Override
        public void getItemOffsets(@NonNull Rect outRect, @NonNull View view,
                                   @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view);
            int column = position % 2;

            if (column == 0) {
                outRect.right = spacing;
            } else {
                outRect.left = spacing;
            }
        }
    }
}
