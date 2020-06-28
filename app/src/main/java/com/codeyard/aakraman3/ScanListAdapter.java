package com.codeyard.aakraman3;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.codeyard.aakraman3.models.BLEDevice;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


public class ScanListAdapter extends RecyclerView.Adapter<ScanListAdapter.ViewHolder> {


    private final List<BLEDevice> mDevices;
    private final Context mContext;
    private OnItemClickListener listener;

    public ScanListAdapter(Context context, List<BLEDevice> devices) {

        mDevices = devices;
        mContext = context;

    }

    private OnItemClickListener getListener() {
        return this.listener;

    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    private Context getContext() {
        return mContext;
    }

    @NonNull
    @Override
    public ScanListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View deviceView = inflater.inflate(R.layout.listitem_device, parent, false);

        return new ViewHolder(deviceView);
    }

    @Override
    public void onBindViewHolder(@NonNull ScanListAdapter.ViewHolder viewHolder, int position) {
        BLEDevice device = mDevices.get(position);
        if (device != null) {
            TextView name = viewHolder.name;
            name.setText(device.name);
            TextView record = viewHolder.record;
            record.setText(device.scanRecord);

            TextView rssi = viewHolder.rssi;
            rssi.setText(String.valueOf(device.rssi));
        }

    }

    @Override
    public int getItemCount() {
        return mDevices.size();
    }

    private interface OnItemClickListener {
        void onItemClick(View itemView, int position);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView name;
        final TextView rssi;
        final TextView record;

        ViewHolder(final View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.device_name);
            rssi = itemView.findViewById(R.id.rssi);
            record = itemView.findViewById(R.id.record);
        }
    }


}
