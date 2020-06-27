package com.codeyard.aakraman3;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.le.AdvertiseCallback;
import android.bluetooth.le.AdvertiseData;
import android.bluetooth.le.AdvertiseSettings;
import android.bluetooth.le.BluetoothLeAdvertiser;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.Random;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

public class MainActivity extends AppCompatActivity {
    private static final String USER_ID = "USER_ID";
    final String TAG = MainActivity.class.getName();
    private final int REQUEST_ENABLE_BT = 2;
    private Context context;
    private BluetoothAdapter bluetoothAdapter;
    private String userID = "";

    //View
    private TextView userIDTeztView;

    private static String random(int len) {
        Random generator = new Random();
        StringBuilder randomStringBuilder = new StringBuilder();
        int randomLength = generator.nextInt(len);
        char tempChar;
        for (int i = 0; i < randomLength; i++) {
            tempChar = (char) (generator.nextInt(96) + 32);
            randomStringBuilder.append(tempChar);
        }
        return randomStringBuilder.toString();
    }

    @Override
    public void onCreate(Bundle saved) {

        super.onCreate(saved);

        setContentView(R.layout.activity_main);
        context = MainActivity.this;
        userIDTeztView = findViewById(R.id.userID);
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            finish();
        }

        bluetoothAdapter = BluetoothUtils.getBluetoothAdapter();

        if (!BluetoothUtils.isBluetoothEnabled(bluetoothAdapter)) {
            enableBluetooth();
        }

        UserIDModel userIDModel = new UserIDModel(this.context);


        userID = userIDModel.getUserId();
        if (userID.equals("")) {
//            For now genreate random
            userID = random(12);
            userIDModel.setUserID(userID);
        }
        BluetoothUtils.setBluetoothName(bluetoothAdapter, userID);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1001);
        BluetoothLeAdvertiser advertiser = bluetoothAdapter.getBluetoothLeAdvertiser();

        AdvertiseSettings settings = new AdvertiseSettings.Builder()
                .setAdvertiseMode(AdvertiseSettings.ADVERTISE_MODE_LOW_LATENCY)
                .setTxPowerLevel(AdvertiseSettings.ADVERTISE_TX_POWER_HIGH)
                .setConnectable(false)
                .build();

        AdvertiseCallback advertisingCallback = new AdvertiseCallback() {
            @Override
            public void onStartSuccess(AdvertiseSettings settingsInEffect) {
                super.onStartSuccess(settingsInEffect);
            }

            @Override
            public void onStartFailure(int errorCode) {
                Log.e("BLE", "Advertising onStartFailure: " + errorCode);
                super.onStartFailure(errorCode);
            }
        };
        AdvertiseData data = new AdvertiseData.Builder()
                .setIncludeDeviceName(true)
                .build();
        advertiser.startAdvertising(settings, data, advertisingCallback);


//        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
//        ft.replace(R.id.fragHolder, new ScanListFragment());
//        ft.commit();

//        start the service
        Intent startServiceIntent = new Intent(this, AutoScannerService.class);
        startService(startServiceIntent);

    }


    //
    private void enableBluetooth() {
        Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
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

    @Override
    protected void onResume() {
        super.onResume();

        // Ensures Bluetooth is enabled on the device.  If Bluetooth is not currently enabled,
        // fire an intent to display a dialog asking the user to grant permission to enable it.
        if (!bluetoothAdapter.isEnabled()) {
            if (!bluetoothAdapter.isEnabled()) {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // User chose not to enable Bluetooth.
        if (requestCode == REQUEST_ENABLE_BT && resultCode == Activity.RESULT_CANCELED) {
            finish();
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.testing_console) {
            startActivity(new Intent(MainActivity.this, TestingActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }
}

