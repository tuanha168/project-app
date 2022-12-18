package com.example.storegame.modle;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResultGames {
    @SerializedName("games")
    @Expose
    private List<Game> games;

    public List<Game> getGames() {
        return games;
    }
}
