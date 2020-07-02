package com.codeyard.aakraman3.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.IBinder;

import com.codeyard.aakraman3.R;
import com.codeyard.aakraman3.constants.Constants;
import com.codeyard.aakraman3.utils.BluetoothUtils;
import com.codeyard.aakraman3.utils.InternetUtils;

import androidx.annotation.Nullable;

public class NotificationService extends Service {
    NotificationManager notificationManager;
    BroadcastReceiver blStateChangeBroadCastReciever = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (!BluetoothUtils.isBluetoothEnabled(BluetoothUtils.getBluetoothAdapter())) {
                sendNotification(R.string.bluetooth_off, getString(R.string.bluetooth_off));
            }
        }
    };
    BroadcastReceiver InternetStateChangeBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (InternetUtils.getConnectivityStatusString(NotificationService.this) == Constants.NO_CONNECTION || InternetUtils.getConnectivityStatusString(NotificationService.this) == Constants.NO_INFO) {
                sendNotification(R.string.no_internet, getString(R.string.no_internet));
            }
        }
    };

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (!(BluetoothUtils.isBLEAvaialble(this) && BluetoothUtils.isBluetoothAvailable())) {
            stopSelf();
        }
        IntentFilter intentFilter = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
        this.registerReceiver(blStateChangeBroadCastReciever, intentFilter);

        IntentFilter intentFilter1 = new IntentFilter(ConnectivityManager.EXTRA_NO_CONNECTIVITY);
        this.registerReceiver(InternetStateChangeBroadcastReceiver, intentFilter1);
    }

    public void sendNotification(int id, String message) {
        Notification notification = new Notification.Builder(this).setContentText(message).build();
        notificationManager.notify(id, notification);
    }
}
