package com.codeyard.aakraman3;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    final int REQUEST_ENABLE_BT = 2;
    Context context;
    BluetoothAdapter bluetoothAdapter;
    SharedPreferences sharedPreferences;
    static String USER_ID;

    final String TAG = MainActivity.class.getName();

    @Override
    public void onCreate(Bundle saved) {

        super.onCreate(saved);

        setContentView(R.layout.activity_main);

        context = MainActivity.this;

        if (BluetoothUtils.isBluetoothAvailable()) {
            Toast.makeText(context, "BL not acail", Toast.LENGTH_SHORT).show();
        }

        if (BluetoothUtils.isBLEAvaialble(context)) {
            Toast.makeText(context, "BLE not avail", Toast.LENGTH_SHORT).show();
        }

        bluetoothAdapter = BluetoothUtils.getBluetoothAdapter();

        if (!BluetoothUtils.isBluetoothEnabled(bluetoothAdapter)) {
            enableBluetooth();
        }

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);

    }


    public void setUserID(String userId) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(USER_ID, userId);
        editor.apply();
    }

    //
    public void enableBluetooth() {
        Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_ENABLE_BT) {
            if (resultCode == RESULT_CANCELED) {
                Log.d(TAG, "onActivityResult: user not turn bl on");
//                TODO make a alert
                finish();
            }
        }
    }

    public String getUserId() {
        return sharedPreferences.getString(USER_ID, "");
    }

    public AlertDialog.Builder createAlert(String message) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage(message);
        return alertDialogBuilder;
    }

    public void showAlert(AlertDialog.Builder alertDialogBuilder) {
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}

