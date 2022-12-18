package com.example.storegame.modle;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResultUser {
    @SerializedName("user")
    @Expose
    private User user;

    public User getUser() {
        return user;
    }

}
