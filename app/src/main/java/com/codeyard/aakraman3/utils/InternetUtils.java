package com.codeyard.aakraman3.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.codeyard.aakraman3.constants.Constants;

class InternetUtils {
    public static int getConnectivityStatusString(Context context) {
        int status = Constants.NO_INFO;
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(
                        Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null) {
            if (activeNetwork.getType() ==
                    ConnectivityManager.TYPE_WIFI) {
                status = Constants.WIFI;
                return status;
            } else if (activeNetwork.getType() ==
                    ConnectivityManager.TYPE_MOBILE) {
                status = Constants.MOBILE_DATA;
                return status;
            }
        } else {
            status = Constants.NO_CONNECTION;
            return status;
        }
        return status;
    }
}
