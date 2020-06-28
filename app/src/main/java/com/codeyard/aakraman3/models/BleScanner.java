package com.codeyard.aakraman3.models;

import android.content.Context;
import android.os.Build;
import android.os.Handler;

public class BleScanner {
    private final static String TAG = BleScanner.class.getName();
    private final BaseBleScanner bleScanner;

    public BleScanner(Context context, final SimpleScanCallback callback) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            bleScanner = new LollipopBleScanner(callback);
        } else {
            bleScanner = new JellyBeanBleScanner(context, callback);
        }
    }


    public boolean isScanning() {
        return bleScanner.isScanning;
    }

    public void startBleScan() {
        bleScanner.onStartBleScan();
    }

    public void startBleScan(long timeoutMillis) {
        bleScanner.onStartBleScan(timeoutMillis);
    }

    public void stopBleScan() {
        bleScanner.onStopBleScan();
    }

    public abstract static class BaseBleScanner {
        final static long defaultTimeout = 15 * 1000;
        final Handler timeoutHandler = new Handler();
        final Runnable timeoutRunnable = new Runnable() {
            @Override
            public void run() {
                onStopBleScan();
                onBleScanFailed();
            }
        };
        boolean isScanning;

        protected abstract void onStartBleScan();

        protected abstract void onStartBleScan(long timeoutMillis);

        protected abstract void onStopBleScan();

        protected abstract void onBleScanFailed();
    }
}
