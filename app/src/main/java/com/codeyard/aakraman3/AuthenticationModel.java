package com.codeyard.aakraman3;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import static com.codeyard.aakraman3.Constants.AUTH;

public class AuthenticationModel {
    private final SharedPreferences sharedPreferences;

    public AuthenticationModel(Context context) {
        this.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public String getAuth() {
        return sharedPreferences.getString(AUTH, "");
    }

    public void setAuth(String userId) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(AUTH, userId);
        editor.apply();
    }
}
