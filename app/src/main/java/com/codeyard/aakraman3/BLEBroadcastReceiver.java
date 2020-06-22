package com.codeyard.aakraman3;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.Objects;

public class BLEBroadcastReceiver extends BroadcastReceiver {

    public static final String RECEIVE_UPDATE = "BLE_UPDATES";
    private static final String TAG = "BLEBroadcastReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(TAG, "Received broadcast");
        String action = intent.getAction();
        if (Objects.requireNonNull(action).equals(RECEIVE_UPDATE)) {
            Log.i(TAG, "Received ble update from service!");
        }
    }
}
