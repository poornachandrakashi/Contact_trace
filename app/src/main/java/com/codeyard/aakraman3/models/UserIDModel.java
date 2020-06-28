package com.codeyard.aakraman3.models;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import static com.codeyard.aakraman3.constants.Constants.USER_ID;

public class UserIDModel {
    private final SharedPreferences sharedPreferences;

    public UserIDModel(Context context) {
        this.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public void setUserID(String userId) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(USER_ID, userId);
        editor.apply();
    }

    public String getUserId() {
        return sharedPreferences.getString(USER_ID, "");
    }

}
