package com.example.storegame.modle;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResultGame {
    @SerializedName("game")
    @Expose
    private Game game;

    public Game getGame() {
        return game;
    }
}
