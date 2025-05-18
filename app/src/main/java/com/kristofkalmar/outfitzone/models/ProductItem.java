package com.kristofkalmar.outfitzone.models;

import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.Serializable;

public class ProductItem implements Serializable {
    private String id;
    private String imageURI;
    private String title;
    private String description;
    private Integer price;
    private Integer size;

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    private Integer amount;

    public ProductItem() {}

    public ProductItem(String id, String imageURI, String title, String description, Integer price, Integer size) {
        this.id = id;
        this.imageURI = imageURI;
        this.title = title;
        this.description = description;
        this.price = price;
        this.size = size;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImageURI() {
        return imageURI;
    }

    public void setImageURI(String imageURI) {
        this.imageURI = imageURI;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public static ProductItem convertData(QueryDocumentSnapshot data) {
        ProductItem item = new ProductItem();

        item.id = data.getId();
        item.imageURI = data.getString("imageURI");
        item.title = data.getString("title");
        item.description = data.getString("description");
        item.price = data.getLong("price").intValue();
        item.size = data.getLong("size").intValue();

        return item;
    }
}
