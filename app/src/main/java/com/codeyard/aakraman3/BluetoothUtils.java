package com.codeyard.aakraman3;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.pm.PackageManager;

class BluetoothUtils {


    /**
     * @return rteturns true if the device has bluetootjh
     */
//    Checks if Bluettoth is available
    static boolean isBluetoothAvailable() {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        return bluetoothAdapter != null;
    }

    /**
     * @param context Context
     * @return Returns true if the device has BLE Available
     */
    static boolean isBLEAvaialble(Context context) {
        return context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE);
    }

    /**
     * @return Returns the instance of the defualt bluetooth adapter
     */
    static BluetoothAdapter getBluetoothAdapter() {
        return BluetoothAdapter.getDefaultAdapter();
    }

    static boolean isBluetoothEnabled(BluetoothAdapter bluetoothAdapter) {
        return bluetoothAdapter.isEnabled();
    }

    static void setBluetoothName(BluetoothAdapter bluetoothAdapter, String userID) {
        bluetoothAdapter.setName(userID);
    }

}
