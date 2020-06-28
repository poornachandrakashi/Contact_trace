package com.codeyard.aakraman3;

import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;

import com.codeyard.aakraman3.models.BLEDevice;
import com.codeyard.aakraman3.models.BleScanner;
import com.codeyard.aakraman3.models.SimpleScanCallback;
import com.codeyard.aakraman3.server.ServerClass;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ScanListFragment extends Fragment implements SimpleScanCallback {
    final String TAG = this.getClass().getName();
    private final List<BLEDevice> mDevices = new ArrayList<>();
    private final Map<String, BLEDevice> deviceMap = new HashMap<>();
    private final BLEBroadcastReceiver mMessageReceiver = new BLEBroadcastReceiver();
    private ServerClass serverClass;
    private ScanListAdapter sAdapter;
    private Handler mHandler;
    private BleScanner mBleScanner;
    private boolean mScanning = false;
    private Button scanButton;
    private ProgressBar pBar;
    private UserIDModel userIDModel;

    @Override
    public void onBleScanFailed() {

    }

    @Override
    public void onBleScan(final BluetoothDevice device, final int rssi, final byte[] scanRecord) {
        Log.d("TAG", "onBleScan callback reached on UI thread :" + "device: " + device + " rssi: " + rssi + " scanR: " + Arrays.toString(scanRecord));
        BLEDevice d = new BLEDevice(device.getName(), rssi, scanRecord);

        if (!deviceMap.containsKey(device.toString())) {

            deviceMap.put(device.toString(), d);
            mDevices.add(d);

        } else {
            //in 15 seconds the rssi can change
            mDevices.remove(deviceMap.get(device.toString()));
            deviceMap.put(device.toString(), d);
            mDevices.add(d);
            sAdapter.notifyDataSetChanged();
        }
        sAdapter.notifyDataSetChanged();

        String myId = userIDModel.getUserId();
        String otherId = device.getName();
        String timestamp = String.valueOf(System.currentTimeMillis() / 1000L);

        serverClass.sendContactTraceData(myId, otherId, timestamp, this.getActivity());

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mHandler = new Handler();
        sAdapter = new ScanListAdapter(
                Objects.requireNonNull(
                        getActivity())
                        .getApplicationContext(),
                mDevices);

        setRetainInstance(true);

        serverClass = new ServerClass();

        userIDModel = new UserIDModel(getActivity());
    }


    private void scanLeDevice() {

        // Stops scanning after a pre-defined scan period.
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mScanning = false;
                updateStopScanButton();
                mBleScanner.stopBleScan();
                mBleScanner = null;
                Objects.requireNonNull(getActivity()).stopService(new Intent(getActivity().getApplicationContext(), BroadcastService.class));


            }
        }, Constants.SCAN_PERIOD);


        mScanning = true;
        if (mBleScanner == null) {
            mBleScanner = new BleScanner(Objects.requireNonNull(getActivity()).getApplicationContext(), this);
        }

        mBleScanner.startBleScan(); // factory for scanner version
        Objects.requireNonNull(getActivity()).startService(new Intent(getActivity().getApplicationContext(), BroadcastService.class));


    }


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_scanlist, container,
                false);

        scanButton = view.findViewById(R.id.scanButton);

        scanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!mScanning) {
                    Log.d("TAG", "scanning");
                    mDevices.clear();
                    deviceMap.clear();
                    updateStartScanButton();
                    scanLeDevice();
                    mScanning = true;
                }
            }
        });
        pBar = view.findViewById(R.id.loading_spinner);

        RecyclerView rvDevices = view.findViewById(R.id.rvDevices);
        rvDevices.setAdapter(sAdapter);
        rvDevices.setLayoutManager(new LinearLayoutManager(Objects.requireNonNull(getActivity()).getApplicationContext()));

        return view;

    }


    private void updateStartScanButton() {
        pBar.setVisibility(View.VISIBLE);
        scanButton.setText(R.string.Scanning);
        scanButton.setTextColor(getResources().getColor(R.color.gray));
        scanButton.setClickable(false);
    }


    private void updateStopScanButton() {
        pBar.setVisibility(View.INVISIBLE);
        scanButton.setText(R.string.StartScan);
        scanButton.setClickable(true);
    }

//    private void emptyList(boolean empty) {
//        if (empty) {
//            rvDevices.setVisibility(View.GONE);
//            emptyView.setVisibility(View.VISIBLE);
//        } else {
//            rvDevices.setVisibility(View.VISIBLE);
//            emptyView.setVisibility(View.GONE);
//
//        }
//    }


    @Override
    public void onResume() {
        super.onResume();

        LocalBroadcastManager.getInstance(Objects.requireNonNull(getActivity()).getApplicationContext()).registerReceiver(
                mMessageReceiver, new IntentFilter(BLEBroadcastReceiver.RECEIVE_UPDATE));
    }


    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(Objects.requireNonNull(getActivity()).getApplicationContext()).unregisterReceiver(mMessageReceiver);
    }

}
