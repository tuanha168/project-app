package com.example.storegame.Data;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;


public class SharedPreferencesData {
    public static String LOGIN = "login";
    public static String TOKEN = "token";
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    public SharedPreferencesData(Context context) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        editor = sharedPreferences.edit();
    }

    public void setLogin(Boolean isLogin) {
        editor.putBoolean(LOGIN, isLogin);
        editor.commit();
    }

    public Boolean isLogin() {
        return sharedPreferences.getBoolean(LOGIN, false);
    }

    public void setToken(String token) {
        editor.putString(TOKEN, token);
        editor.commit();
    }

    public String getToken() {
        return sharedPreferences.getString(TOKEN, "");
    }
}
