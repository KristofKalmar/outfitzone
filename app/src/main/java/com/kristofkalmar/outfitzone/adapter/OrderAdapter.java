package com.kristofkalmar.outfitzone.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kristofkalmar.outfitzone.R;
import com.kristofkalmar.outfitzone.models.OrderItem;
import com.kristofkalmar.outfitzone.models.ProductItem;

import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ListViewHolder> {
    private List<OrderItem> items;
    public OrderAdapter(List<OrderItem> items) {
        this.items = items;
    }

    public void setItems(List<OrderItem> items) {
        int start = items.size();
        this.items = items;
        notifyItemRangeInserted(start, items.size());
    }

    @NonNull
    @Override
    public ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.order_item, parent, false);

        return new OrderAdapter.ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderAdapter.ListViewHolder holder, int position) {
        OrderItem item = items.get(position);
        holder.bind(item);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class ListViewHolder extends RecyclerView.ViewHolder {
        private TextView titleText;
        private TextView priceText;
        public ListViewHolder(@NonNull View itemView) {
            super(itemView);

            this.titleText = itemView.findViewById(R.id.item_card_name);
            this.priceText = itemView.findViewById(R.id.item_card_price);
        }

        public void bind(OrderItem data) {
            this.titleText.setText(String.valueOf(data.getProducts().size() + "Tétel"));
            this.priceText.setText(String.valueOf(data.getAmount() * 360 + " Ft"));
        }
    }
}
