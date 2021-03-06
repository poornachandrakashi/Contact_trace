package com.codeyard.aakraman3.models;

import android.annotation.TargetApi;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import java.util.List;

@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class LollipopBleScanner extends BleScanner.BaseBleScanner {
    private final static String TAG = LollipopBleScanner.class.getName();


    private BluetoothLeScanner mBluetoothScanner = null;
    private final BluetoothAdapter mBluetoothAdapter;
    private final SimpleScanCallback mScanCallback;
    private final ScanCallback scanCallback = new ScanCallback() {
        @Override
        public void onScanResult(final int callbackType, final ScanResult result) {
            super.onScanResult(callbackType, result);
            Log.d(TAG, "onScanResult: " + callbackType + " ScanResult:" + result);

            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    if (result.getScanRecord() != null) {
                        mScanCallback.onBleScan(result.getDevice(), result.getRssi(), result.getScanRecord().getBytes());
                    }
                }
            });

        }


        @Override
        public void onBatchScanResults(List<ScanResult> results) {
            super.onBatchScanResults(results);
            Log.d(TAG, "onBatchScanResults()");
        }

        @Override
        public void onScanFailed(int errorCode) {
            //error code 3 or 1 todo
            if (errorCode != 3 && errorCode != 1) {
                Log.d(TAG, "onScanFailed: " + errorCode);
                //mScanCallback.onBleScanFailed(BleScanState.newInstance(errorCode));
            }
        }
    };

    public LollipopBleScanner(SimpleScanCallback callback) {
        mScanCallback = callback;
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter != null) {
            mBluetoothScanner = mBluetoothAdapter.getBluetoothLeScanner();
        }


    }

    @Override
    public void onStartBleScan(long timeoutMillis) {
        long delay = timeoutMillis == 0 ? defaultTimeout : timeoutMillis;
        if (mBluetoothScanner != null && mBluetoothAdapter != null && mBluetoothAdapter.isEnabled()) {
            try {

                mBluetoothScanner.startScan(scanCallback);
                //mBluetoothScanner.startScan(scanCallback);
                isScanning = true;
            } catch (Exception e) {
                isScanning = false;
                Log.e(TAG, e.toString());
            }
            timeoutHandler.postDelayed(timeoutRunnable, delay);
        } else {
            mScanCallback.onBleScanFailed();
        }
        Log.d(TAG, "mBluetoothScanner.startScan()");
    }

    @Override
    public void onStartBleScan() {
        if (mBluetoothScanner != null && mBluetoothAdapter != null && mBluetoothAdapter.isEnabled()) {
            try {

                mBluetoothScanner.startScan(scanCallback);
                //mBluetoothScanner.startScan(scanCallback);
                isScanning = true;
            } catch (Exception e) {
                isScanning = false;
                mScanCallback.onBleScanFailed();
                Log.e(TAG, e.toString());
            }
        } else {
            mScanCallback.onBleScanFailed();
        }
        Log.d(TAG, "mBluetoothScanner.startScan()");
    }

    @Override
    public void onStopBleScan() {
        isScanning = false;
        if (mBluetoothScanner != null && mBluetoothAdapter != null && mBluetoothAdapter.isEnabled()) {
            try {
                mBluetoothScanner.stopScan(scanCallback);
            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
        }
        Log.d(TAG, "mBluetoothScanner.stopScan()");
    }

    @Override
    public void onBleScanFailed() {
        mScanCallback.onBleScanFailed();
    }
}
