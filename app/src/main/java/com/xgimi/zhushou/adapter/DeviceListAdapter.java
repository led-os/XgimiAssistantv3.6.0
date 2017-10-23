package com.xgimi.zhushou.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.xgimi.device.device.GMDevice;
import com.xgimi.device.device.GMDeviceStorage;
import com.xgimi.zhushou.R;
import com.xgimi.zhushou.util.Constant;

import java.util.ArrayList;
import java.util.List;

public class DeviceListAdapter extends BaseAdapter {

    private List<GMDevice> gmDevices;
    private Context mContext;
    private int mPostion = -1;

    public DeviceListAdapter(Context context, List<GMDevice> gm) {
        this.mContext = context;
        this.gmDevices = gm;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return gmDevices.size();
    }

    public void dataChange(List<GMDevice> gm) {
        if (gm != null) {
            List<GMDevice> gms = new ArrayList<>();
//            gm = removeDuplicate(gm);
            gms.addAll(gm);
            this.gmDevices = gms;
            notifyDataSetChanged();
        }
    }

    @Override
    public GMDevice getItem(int position) {
        // TODO Auto-generated method stub
        return gmDevices.get(position);
    }

    // public void changeStatus(int postion){
    // this.mPostion=postion;
    // notifyDataSetChanged();
    // }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        ViewHolder vh;
        View view = convertView;
        if (view == null) {
            vh = new ViewHolder();
            view = View.inflate(mContext, R.layout.deviceadapter, null);
            vh.tv = (TextView) view.findViewById(R.id.tv);
            vh.iv = (ImageView) view.findViewById(R.id.iv);

            vh.tv1 = (TextView) view.findViewById(R.id.tv_ip);
            view.setTag(vh);
        } else {
            vh = (ViewHolder) view.getTag();
        }
        GMDevice gmDevice = gmDevices.get(position);
        vh.tv.setText(gmDevice.name);
        vh.tv1.setText(gmDevice.ip);
        if (GMDeviceStorage.getInstance().getConnectedDevice() != null) {
            if (Constant.netStatus
                    && GMDeviceStorage.getInstance().getConnectedDevice().ip
                    .equals(gmDevice.ip)) {
                vh.iv.setVisibility(View.VISIBLE);
            } else {
                vh.iv.setVisibility(View.INVISIBLE);
            }
        }
        return view;
    }

    class ViewHolder {
        public ImageView iv;
        public TextView tv;
        public TextView tv1;
    }

    public List removeDuplicate(List<GMDevice> list) {
        for (int i = 0; i < list.size() - 1; i++) {
            for (int j = list.size() - 1; j > i; j--) {
                if (list.get(j).getIp().equals(list.get(i).getIp())) {
                    list.remove(j);
                }
            }
        }
        return list;
    }
}
