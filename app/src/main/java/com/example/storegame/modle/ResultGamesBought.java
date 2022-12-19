package com.example.storegame.modle;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResultGamesBought {

    @SerializedName("games")
    @Expose
    private List<GameBought> games = null;

    public List<GameBought> getGames() {
        return games;
    }

    public void setGames(List<GameBought> games) {
        this.games = games;
    }
}
