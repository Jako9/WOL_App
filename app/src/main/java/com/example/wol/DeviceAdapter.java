package com.example.wol;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import static com.example.wol.Util.*;

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
        holder.mEditButton.setOnClickListener(view ->  {
            Snackbar.make(view, "Edit " + currentItem.getName(), Snackbar.LENGTH_SHORT).show();
        });
        holder.mDevice.setOnClickListener(view ->  {
            String ip = currentItem.getIp().equals("") ? DEFAULT_IP : currentItem.getIp();
            int port = currentItem.getPort() == 0 ? DEFAULT_PORT : currentItem.getPort();
            Network.sendGetRequest(ip,port,currentItem.getMac(),KEY,FORMAT);
            Snackbar.make(view, "Wake " + currentItem.getName(), Snackbar.LENGTH_SHORT).show();
        });
    }

    @Override
    public int getItemCount() {
        return mDeviceList.size();
    }

    public static class DeviceViewHolder extends RecyclerView.ViewHolder {
        public TextView mTextView1;
        public TextView mTextView2;
        public FloatingActionButton mEditButton;
        public RelativeLayout mDevice;

        public DeviceViewHolder(View itemView) {
            super(itemView);
            mTextView1 = itemView.findViewById(R.id.deviceName);
            mTextView2 = itemView.findViewById(R.id.deviceMac);
            mEditButton = itemView.findViewById(R.id.deviceEdit);
            mDevice = itemView.findViewById(R.id.device);
        }
    }

    private ArrayList<Device> mDeviceList;

    public DeviceAdapter(ArrayList<Device> deviceList) {
        mDeviceList = deviceList;
    }
}
