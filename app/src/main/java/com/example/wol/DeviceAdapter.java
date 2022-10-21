package com.example.wol;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class DeviceAdapter extends RecyclerView.Adapter<DeviceAdapter.DeviceViewHolder> {
    @Override
    public DeviceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.device, parent, false);
        DeviceViewHolder dvh = new DeviceViewHolder(v);
        return dvh;
    }

    @Override
    public void onBindViewHolder(DeviceViewHolder holder, int position) {
        Device currentItem = mDeviceList.get(position);

        holder.mTextView1.setText(currentItem.getName());
        holder.mTextView2.setText(currentItem.getMac());
    }

    @Override
    public int getItemCount() {
        return mDeviceList.size();
    }

    public static class DeviceViewHolder extends RecyclerView.ViewHolder {
        public TextView mTextView1;
        public TextView mTextView2;

        public DeviceViewHolder(View itemView) {
            super(itemView);
            mTextView1 = itemView.findViewById(R.id.deviceName);
            mTextView2 = itemView.findViewById(R.id.deviceMac);
        }
    }

    private ArrayList<Device> mDeviceList;

    public DeviceAdapter(ArrayList<Device> deviceList) {
        mDeviceList = deviceList;
    }
}
