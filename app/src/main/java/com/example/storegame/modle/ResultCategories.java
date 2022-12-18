package com.example.storegame.modle;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResultCategories {

    @SerializedName("categories")
    @Expose
    private List<Category> categories = null;

    public List<Category> getCategories() {
        return categories;
    }

}
