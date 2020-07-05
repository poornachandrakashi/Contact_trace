package com.codeyard.aakraman3;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import com.codeyard.aakraman3.models.BleScanner;
import com.codeyard.aakraman3.models.SimpleScanCallback;
import com.codeyard.aakraman3.models.UserIDModel;
import com.codeyard.aakraman3.server.ServerClass;

import java.util.Objects;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

public class AutoScannerService extends Service implements SimpleScanCallback {
    private final String TAG = AutoScannerService.class.getName();
    private Context context;
    private UserIDModel userIDModel;
    private BleScanner mBleScanner;

    private void startScan() {
        if (mBleScanner == null) {
            mBleScanner = new BleScanner(Objects.requireNonNull(context).getApplicationContext(), this);
        }

        mBleScanner.startBleScan(); // factory for scanner version
        Objects.requireNonNull(context).startService(new Intent(context.getApplicationContext(), BroadcastService.class));
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        Log.d(TAG, "onCreate: started");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager notificationManager =
                    (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
            createChannel(notificationManager);
            Notification notification = new NotificationCompat.Builder(this).setContentTitle("aakrmana").build();
            startForeground(1, notification);

        }

        userIDModel = new UserIDModel(context);
        startScan();
    }

    @TargetApi(26)
    private void createChannel(NotificationManager notificationManager) {
        String name = "Akkramana";
        String description = "Notifications for akakrmamana status";
        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        NotificationChannel mChannel = new NotificationChannel(name, name, importance);
        mChannel.setDescription(description);
        mChannel.enableLights(true);
        mChannel.setLightColor(Color.BLUE);
        notificationManager.createNotificationChannel(mChannel);
    }

    @Override
    public void onBleScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
        Log.d("TAG", "onBleScan: " + device.getName());
        String myId = userIDModel.getUserId();
        String otherId = device.getName();
        String timestamp = String.valueOf(System.currentTimeMillis() / 1000L);
        ServerClass.sendContactTraceData(context, myId, otherId, timestamp);
        Log.d(TAG, "onBleScan: scanned!!!!!! " + myId + " " + otherId + " " + timestamp);
    }

    @Override
    public void onBleScanFailed() {
        Log.d("TAG", "onBleScanFailed: ");
    }
}
