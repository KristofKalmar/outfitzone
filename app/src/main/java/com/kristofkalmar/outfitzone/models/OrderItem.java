package com.kristofkalmar.outfitzone.models;

import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.List;

public class OrderItem {
    private String id;
    private List<String> products;
    private Integer amount;
    private String user;

    public OrderItem() {}

    public OrderItem(String id, List<String> products, Integer amount, String user) {
        this.id = id;
        this.products = products;
        this.amount = amount;
        this.user = user;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public List<String> getProducts() {
        return products;
    }

    public void setProducts(List<String> products) {
        this.products = products;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public static OrderItem convertData(QueryDocumentSnapshot data) {
        OrderItem item = new OrderItem();

        item.id = data.getId();
        item.products = (List<String>) data.get("products");
        item.amount = Integer.parseInt(String.valueOf(data.getLong("amount")));
        item.user = data.getString("user");

        return item;
    }
}
