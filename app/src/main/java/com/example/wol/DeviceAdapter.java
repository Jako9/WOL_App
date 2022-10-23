package com.example.wol;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import static com.example.wol.Util.*;
import static com.example.wol.Status.*;

import java.util.ArrayList;
import java.util.HashMap;

public class DeviceAdapter extends RecyclerView.Adapter<DeviceAdapter.DeviceViewHolder> {
    RecyclerView mRecyclerView;
    FragmentManager mFragmentManager;
    @Override
    public DeviceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.device, parent, false);
        DeviceViewHolder dvh = new DeviceViewHolder(v);
        mRecyclerView = parent.findViewById(R.id.deviceList);
        return dvh;
    }

    @Override
    public void onBindViewHolder(DeviceViewHolder holder, int position) {
        Device currentItem = mDeviceList.get(position);
        holder.mTextView1.setText(currentItem.getName());
        holder.mTextView2.setText(currentItem.getMac());
        holder.mEditButton.setOnClickListener(view ->  {
            AddDeviceDialogue addDeviceDialogue = new AddDeviceDialogue(currentItem, position);
            addDeviceDialogue.show(mFragmentManager,"edit device dialog");
        });
        holder.mDeleteButton.setOnClickListener(view -> {
            DeleteDeviceDialogue deleteDeviceDialogue = new DeleteDeviceDialogue(position);
            deleteDeviceDialogue.show(mFragmentManager,"delete device dialog");
        });
        holder.mDevice.setOnClickListener(view ->  {
            Network.sendPostRequest(currentItem, new CallbackStatus() {
                @Override
                public void onSuccess(String response) {
                    Snackbar.make(mRecyclerView, response, Snackbar.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(String error) {
                    Snackbar.make(mRecyclerView, error, Snackbar.LENGTH_SHORT).show();
                }
            });
        });
    }

    @Override
    public int getItemCount() {
        return mDeviceList.size();
    }

    public void setStatus(Device device, Status status){
        DeviceViewHolder holder;
        try {
            holder = (DeviceViewHolder) mRecyclerView.findViewHolderForAdapterPosition(mDeviceList.indexOf(device));
        }
        catch (Exception e){
            Log.e("Error", "Device not found");
            return;
        }
        if (status == ONLINE) {
            holder.mOnline.setVisibility(View.VISIBLE);
            holder.mOffline.setVisibility(View.INVISIBLE);
        } else {
            holder.mOnline.setVisibility(View.INVISIBLE);
            holder.mOffline.setVisibility(View.VISIBLE);
        }
    }

    public static class DeviceViewHolder extends RecyclerView.ViewHolder {
        public TextView mTextView1;
        public TextView mTextView2;
        public ImageView mEditButton;
        public ImageView mDeleteButton;
        public RelativeLayout mDevice;
        public ImageView mOffline;
        public ImageView mOnline;

        public DeviceViewHolder(View itemView) {
            super(itemView);
            mTextView1 = itemView.findViewById(R.id.deviceName);
            mTextView2 = itemView.findViewById(R.id.deviceMac);
            mEditButton = itemView.findViewById(R.id.deviceEdit);
            mDevice = itemView.findViewById(R.id.device);
            mOffline = itemView.findViewById(R.id.deviceOffline);
            mOnline = itemView.findViewById(R.id.deviceOnline);
            mDeleteButton = itemView.findViewById(R.id.deviceDelete);
        }
    }

    private ArrayList<Device> mDeviceList;

    public DeviceAdapter(ArrayList<Device> deviceList, FragmentManager fragmentManager) {
        mDeviceList = deviceList;
        mFragmentManager = fragmentManager;
    }
}
