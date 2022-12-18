package com.example.storegame.modle;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Carts {
    @SerializedName("cart")
    @Expose
    private List<String> cart = null;

    public List<String> getCart() {
        return cart;
    }

    public void setCart(List<String> cart) {
        this.cart = cart;
    }

    public Carts(List<String> cart) {
        this.cart = cart;
    }
}
