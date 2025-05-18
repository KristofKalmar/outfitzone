package com.kristofkalmar.outfitzone.ui.favorites;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.kristofkalmar.outfitzone.R;
import com.kristofkalmar.outfitzone.databinding.FragmentFavoritesBinding;
import com.kristofkalmar.outfitzone.models.ProductItem;
import com.kristofkalmar.outfitzone.ui.components.productlist.ProductListFragment;

import java.util.ArrayList;
import java.util.List;

public class FavoritesFragment extends Fragment {
    private FragmentFavoritesBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getActivity().setTitle(R.string.favorites);

        binding = FragmentFavoritesBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();

        db.collection("favorites")
                .document(user.getUid())
                .collection("favorites")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<DocumentSnapshot> favoriteDocs = task.getResult().getDocuments();
                        List<Task<DocumentSnapshot>> productTasks = new ArrayList<>();

                        for (DocumentSnapshot favoriteDoc : favoriteDocs) {
                            Task<DocumentSnapshot> productTask = db.collection("products")
                                    .document(favoriteDoc.getId())
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

                                            items.add(item);
                                        }
                                    }

                                    if (getActivity() != null && !getChildFragmentManager().isStateSaved()) {
                                        Fragment childFragment = new ProductListFragment(false, items);
                                        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
                                        transaction.replace(R.id.product_list_container_3, childFragment).commit();
                                    }
                                })
                                .addOnFailureListener(e -> {
                                });

                    } else {
                    }
                });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
