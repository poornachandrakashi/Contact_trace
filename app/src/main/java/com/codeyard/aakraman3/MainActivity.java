package com.codeyard.aakraman3;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.TextView;

import java.util.Random;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    final static String USER_ID = "USER_ID";
    final int REQUEST_ENABLE_BT = 2;
    Context context;
    String TAG = MainActivity.class.getName();

    BluetoothAdapter bluetoothAdapter;
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            // When discovery finds a device
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
//                Called every time new device is scanned
                //TODO send the data to server or to local db
//                Contact @poorna for info
                BluetoothDevice bluetoothDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_NAME);
                Log.d(TAG, "onReceive: " + bluetoothDevice.getName());


            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                Log.v(TAG, "Entered the Finished ");
                bluetoothAdapter.startDiscovery();
            }
        }
    };



    String userId;
    SharedPreferences sharedPreferences;
    TextView userIDTextView;

    public static String randomString(int len) {
        final String DATA = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        Random RANDOM = new Random();
        StringBuilder sb = new StringBuilder(len);

        for (int i = 0; i < len; i++) {
            sb.append(DATA.charAt(RANDOM.nextInt(DATA.length())));
        }

        return sb.toString();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = MainActivity.this;

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);

        userIDTextView = findViewById(R.id.userID);
        userIDTextView.setText("NO USER ID FOUND");

        if (!BluetoothUtils.isBluetoothAvailable()) {
            Log.d(TAG, "onCreate: BL not avail");
//            TODO make it a dialog
        }
        if (!BluetoothUtils.isBLEAvaialble(context)) {
            Log.d(TAG, "onCreate: BLE not avail");
//            TODO make if a dialog
        }

        bluetoothAdapter = BluetoothUtils.getBluetoothAdapter();

        if (!BluetoothUtils.isBluetoothEnabled(bluetoothAdapter)) {
            enableBluetooth();
        }

//        get the id and then store it.


        userId = getUserId();
        if (userId.equals("")) {
//            TODO prompt user for login once that is figured otu
//            for now ;ets give a rand id
            userId = randomString(10);

            setUserID(userId);
        }
        userIDTextView.setText(userId);
        BluetoothUtils.setBluetoothName(bluetoothAdapter, userId);

//         Register for broadcasts when a device is discovered
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        this.registerReceiver(mReceiver, filter);


    }

    public void setUserID(String userId) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(USER_ID, userId);
        editor.apply();
    }

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
}

