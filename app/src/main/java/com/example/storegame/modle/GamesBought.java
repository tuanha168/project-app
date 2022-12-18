package com.example.storegame.modle;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GamesBought {

    @SerializedName("game")
    @Expose
    private String game;
    @SerializedName("date_bought")
    @Expose
    private String dateBought;
    @SerializedName("_id")
    @Expose
    private String id;

    public String getGame() {
        return game;
    }

    public void setGame(String game) {
        this.game = game;
    }

    public String getDateBought() {
        return dateBought;
    }

    public void setDateBought(String dateBought) {
        this.dateBought = dateBought;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}
