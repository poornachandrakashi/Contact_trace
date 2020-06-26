package com.codeyard.aakraman3;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import static com.codeyard.aakraman3.Constants.USER_ID;

class UserIDModel {
    private SharedPreferences sharedPreferences;

    UserIDModel(Context context) {
        this.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    void setUserID(String userId) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(USER_ID, userId);
        editor.apply();
    }

    String getUserId() {
        return sharedPreferences.getString(USER_ID, "");
    }

}
