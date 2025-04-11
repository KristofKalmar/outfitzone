package com.kristofkalmar.outfitzone.models;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class NavigationListItem
{
    private String imageURI;
    private String title;

    public NavigationListItem(String imageURI, String title) {
        this.imageURI = imageURI;
        this.title = title;
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
}
