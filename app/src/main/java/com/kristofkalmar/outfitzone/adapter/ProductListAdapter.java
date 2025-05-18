package com.kristofkalmar.outfitzone.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityOptionsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.kristofkalmar.outfitzone.R;
import com.kristofkalmar.outfitzone.activities.DetailActivity;
import com.kristofkalmar.outfitzone.helper.ImageLoader;
import com.kristofkalmar.outfitzone.models.ProductItem;
import com.kristofkalmar.outfitzone.ui.components.numberpicker.NumberPickerHorizontal;
import com.kristofkalmar.outfitzone.ui.components.productlist.ProductListFragment;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ProductListAdapter extends RecyclerView.Adapter<ProductListAdapter.ListViewHolder> {
    private List<ProductItem> items;

    public void setHorizontal(Boolean horizontal) {
        this.horizontal = horizontal;
        this.showCartButton = true;
    }

    public void addItems(List<ProductItem> items) {
        int start = items.size();
        this.items.addAll(items);
        notifyItemRangeInserted(start, items.size());
    }

    public void setShowCartButton(Boolean showCartButton) {
        this.showCartButton = showCartButton;
    }

    private Boolean horizontal = false;
    private Context context;
    private Boolean showCartButton = true;

    public ProductListAdapter(List<ProductItem> items, FragmentManager fragmentManager, Boolean showCartButton) {
        this.items = items;
        this.showCartButton = showCartButton;
    }

    @NonNull
    @Override
    public ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(this.horizontal ? R.layout.list_item_horizontal : R.layout.list_item, parent, false);

        this.context = view.getContext();
        return new ProductListAdapter.ListViewHolder(view, this.context);
    }

    @Override
    public void onBindViewHolder(@NonNull ListViewHolder holder, int position) {
        ProductItem item = items.get(position);
        holder.imageView.setTransitionName("item_image_" + position);
        holder.bind(item, this.showCartButton);

        holder.imageView.setOnClickListener(event -> {
            Intent intent = new Intent(context, DetailActivity.class);
            intent.putExtra("image_url", item.getImageURI());
            intent.putExtra("product", item);
            intent.putExtra("transition_name", "item_image_" + position);

            Activity activity = (Activity) context;

            ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                    activity,
                    holder.imageView,
                    holder.imageView.getTransitionName()
            );

            context.startActivity(intent, options.toBundle());
        });
    }

    @Override
    public int getItemCount() {
        return items != null ? items.size() : 0;
    }

    static class ListViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        private Button buyButton;
        private ImageView favoriteImageView;
        private TextView titleText;
        private TextView priceText;
        private NumberPickerHorizontal numberPicker;
        private Context context;
        private FirebaseFirestore db;

        public ListViewHolder(@NonNull View itemView, Context context) {
            super(itemView);

            this.imageView = itemView.findViewById(R.id.item_card_image);
            this.buyButton = itemView.findViewById(R.id.item_card_buy_button);
            this.favoriteImageView = itemView.findViewById(R.id.fav_icon);
            this.titleText = itemView.findViewById(R.id.item_card_name);
            this.priceText = itemView.findViewById(R.id.item_card_price);
            this.numberPicker = itemView.findViewById(R.id.number_picker);

            this.context = context;
            this.db = FirebaseFirestore.getInstance();
        }

        public void bind(ProductItem data, Boolean showCartButton) {
            FirebaseAuth auth = FirebaseAuth.getInstance();
            FirebaseUser user = auth.getCurrentUser();

            if (this.numberPicker != null) {
                this.numberPicker.setValue(data.getAmount());
                this.numberPicker.setMin(0);
                this.numberPicker.setMax(9);

                this.numberPicker.addListener(event -> {
                    if (this.numberPicker.getValue() == 0) {
                        this.db.collection("cart").document(user.getUid()).collection("products").document(data.getId()).delete();
                    }

                    HashMap<String, Object> updateValue = new HashMap<>();
                    updateValue.put("amount", this.numberPicker.getValue());

                    this.db.collection("cart").document(user.getUid()).collection("products").document(data.getId()).update(updateValue);
                });
            }

            if (showCartButton != null) {
                if (!showCartButton) {
                    this.buyButton.setVisibility(View.GONE);
                    this.favoriteImageView.setVisibility(View.GONE );
                }
            }

            if (auth.getCurrentUser() == null) {
                this.buyButton.setVisibility(View.GONE);
                this.favoriteImageView.setVisibility(View.GONE);
            }

            Bitmap placeholder = ImageLoader.loadImageFromAssets(itemView.getContext(), "placeholder_product.png");

            Glide.with(this.context).load(data.getImageURI()).error(placeholder).into(imageView);

            this.titleText.setText(data.getTitle());
            this.priceText.setText(data.getPrice() * 360 + " Ft");

            if (user != null) {
                this.db.collection("favorites").document(user.getUid()).collection("favorites").document(data.getId()).get().addOnCompleteListener(snapshot -> {
                    Boolean exists = snapshot.getResult().exists();

                    Bitmap favoriteImage = ImageLoader.loadImageFromAssets(itemView.getContext(), exists ? "favorite_selected.png" : "favorite.png");
                    if (favoriteImage != null) {
                        favoriteImageView.setImageBitmap(favoriteImage);
                    }
                });
            }

            this.favoriteImageView.setOnClickListener(event -> {
                this.db.collection("favorites").document(user.getUid()).collection("favorites").document(data.getId()).get().addOnCompleteListener(snapshot -> {
                    Boolean exists = snapshot.getResult().exists();

                    if (exists) {
                        this.db.collection("favorites").document(user.getUid()).collection("favorites").document(data.getId()).delete();
                    } else {
                        Map<String, Object> likeData = new HashMap<>();
                        likeData.put("liked", true);

                        this.db.collection("favorites").document(user.getUid()).collection("favorites").document(data.getId()).set(likeData);
                    }

                    Bitmap favoriteImageNew = ImageLoader.loadImageFromAssets(itemView.getContext(), !exists ? "favorite_selected.png" : "favorite.png");
                    if (favoriteImageNew != null) {
                        favoriteImageView.setImageBitmap(favoriteImageNew);
                    }
                });
            });

            this.buyButton.setOnClickListener(event -> {
                this.db.collection("cart").document(user.getUid()).collection("products").document(data.getId()).get().addOnCompleteListener(snapshot -> {
                    Boolean exists = snapshot.getResult().exists();

                    if (exists) {
                        Map<String, Object> productData = new HashMap<>();
                        productData.put("amount", snapshot.getResult().getLong("amount") + 1);

                        this.db.collection("cart").document(user.getUid()).collection("products").document(data.getId()).update(productData);
                    } else {
                        Map<String, Object> productData = new HashMap<>();
                        productData.put("amount", 1);

                        this.db.collection("cart").document(user.getUid()).collection("products").document(data.getId()).set(productData);
                    }
                });
            });
        }
    }
}
