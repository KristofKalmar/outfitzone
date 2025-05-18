package com.kristofkalmar.outfitzone.ui.orders;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.kristofkalmar.outfitzone.R;
import com.kristofkalmar.outfitzone.adapter.OrderAdapter;
import com.kristofkalmar.outfitzone.models.OrderItem;
import com.kristofkalmar.outfitzone.models.ProductItem;

import java.util.ArrayList;
import java.util.List;

public class OrdersFragment extends Fragment {
    private List<OrderItem> data = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getActivity().setTitle(R.string.navigation_orders);

        return inflater.inflate(R.layout.fragment_orders, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseAuth auth = FirebaseAuth.getInstance();

        RecyclerView recyclerView = getView().findViewById(R.id.order_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        OrderAdapter adapter = new OrderAdapter(this.data);
        recyclerView.setAdapter(adapter);

        db.collection("purchases").whereEqualTo("user", auth.getCurrentUser().getUid()).get().addOnCompleteListener(event -> {
            QuerySnapshot data = event.getResult();

            List<OrderItem> items = new ArrayList<>();

            data.forEach(order -> {
                OrderItem item = OrderItem.convertData(order);
                items.add(item);
            });

            this.data = items;
            adapter.setItems(items);
        });
    }
}
