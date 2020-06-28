package com.codeyard.aakraman3;

import android.app.Service;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.codeyard.aakraman3.models.BleScanner;
import com.codeyard.aakraman3.models.SimpleScanCallback;
import com.codeyard.aakraman3.models.UserIDModel;
import com.codeyard.aakraman3.server.ServerClass;

import java.util.Objects;

import androidx.annotation.Nullable;

public class AutoScannerService extends Service implements SimpleScanCallback {
    private final String TAG = "TAG";
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
        userIDModel = new UserIDModel(context);
        startScan();
    }

    @Override
    public void onBleScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
        Log.d("TAG", "onBleScan: " + device.getName());
        String myId = userIDModel.getUserId();
        String otherId = device.getName();
        String timestamp = String.valueOf(System.currentTimeMillis() / 1000L);

        new ServerClass().sendContactTraceData(myId, otherId, timestamp, this);
    }

    @Override
    public void onBleScanFailed() {
        Log.d("TAG", "onBleScanFailed: ");
    }
}
