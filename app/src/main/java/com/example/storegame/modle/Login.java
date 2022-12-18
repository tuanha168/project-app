package com.example.storegame.modle;

import com.google.gson.annotations.SerializedName;

public class Login {
    @SerializedName("username")
    String username;
    @SerializedName("password")
    String passWord;

    public String getUsername() {
        return username;
    }

    public Login(String username, String passWord) {
        this.username = username;
        this.passWord = passWord;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    @Override
    public String toString() {
        return "{" +
                "\"username\":\"" + username + '\"' +
                ", \"password\":\"" + passWord + '\"' +
                '}';
    }
}
