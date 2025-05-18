package com.kristofkalmar.outfitzone.ui.cart;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.kristofkalmar.outfitzone.R;
import com.kristofkalmar.outfitzone.models.ProductItem;
import com.kristofkalmar.outfitzone.ui.components.productlist.ProductListFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

public class CartFragment extends Fragment {

    private List<ProductItem> items = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getActivity().setTitle(R.string.cart_activity);

        return inflater.inflate(R.layout.activity_cart, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();

        view.findViewById(R.id.bottom_container).setVisibility(View.INVISIBLE);

        view.findViewById(R.id.purchase_button).setOnClickListener(event -> {
            Map<String, Object> data = new HashMap<>();

            List<String> products = new ArrayList<>();
            Integer amount = 0;

            for (ProductItem productItem : this.items) {
                amount += productItem.getPrice();
                products.add(productItem.getId());
            }

            data.put("user", auth.getCurrentUser().getUid());
            data.put("amount", amount);
            data.put("products", products);

            db.collection("purchases").add(data).addOnCompleteListener(task -> {
                CollectionReference cartRef = db.collection("cart").document(auth.getUid()).collection("products");

                cartRef.get().addOnCompleteListener(task2 -> {
                    if (task2.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task2.getResult()) {
                            cartRef.document(document.getId()).delete();
                        }

                        db.collection("cart").document(auth.getUid()).delete();
                    }

                    BottomNavigationView bottomNav = getActivity().findViewById(R.id.nav_view);
                    bottomNav.setSelectedItemId(R.id.navigation_orders);
                });
            });
        });

        db.collection("cart")
                .document(user.getUid())
                .collection("products")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<DocumentSnapshot> favoriteDocs = task.getResult().getDocuments();
                        List<Task<DocumentSnapshot>> productTasks = new ArrayList<>();

                        Map<String, Integer> productAmounts = new HashMap<>();

                        for (DocumentSnapshot favoriteDoc : favoriteDocs) {
                            String productId = favoriteDoc.getId();

                            Long amount = favoriteDoc.getLong("amount");
                            productAmounts.put(productId, amount != null ? amount.intValue() : 1);

                            Task<DocumentSnapshot> productTask = db.collection("products")
                                    .document(productId)
                                    .get();
                            productTasks.add(productTask);
                        }

                        Tasks.whenAllSuccess(productTasks)
                                .addOnSuccessListener(results -> {
                                    List<ProductItem> items = new ArrayList<>();

                                    for (Object result : results) {
                                        if (result instanceof DocumentSnapshot) {
                                            DocumentSnapshot product = (DocumentSnapshot) result;

                                            ProductItem item = new ProductItem();
                                            item.setId(product.getId());
                                            item.setImageURI(product.getString("imageURI"));
                                            item.setTitle(product.getString("title"));
                                            item.setDescription(product.getString("description"));

                                            Long price = product.getLong("price");
                                            Long size = product.getLong("size");

                                            item.setPrice(price != null ? price.intValue() : 0);
                                            item.setSize(size != null ? size.intValue() : 0);

                                            int amount = productAmounts.getOrDefault(product.getId(), 1);
                                            item.setAmount(amount);

                                            items.add(item);
                                        }
                                    }

                                    if (getActivity() != null && !getChildFragmentManager().isStateSaved()) {
                                        this.items = items;

                                        if (!items.isEmpty()) {
                                            view.findViewById(R.id.bottom_container).setVisibility(View.VISIBLE);
                                        }

                                        Fragment childFragment = new ProductListFragment(true, items, false);
                                        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
                                        transaction.replace(R.id.product_list_container_2, childFragment).commit();
                                    }
                                })
                                .addOnFailureListener(e -> {
                                });

                    } else {
                    }
                });
    }
}
