package com.xgimi.zhushou.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.RotateAnimation;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.xgimi.gmsdk.bean.device.GMDevice;
import com.xgimi.gmsdk.bean.reply.AppListReply;
import com.xgimi.gmsdk.callback.IGMDeviceConnectedListener;
import com.xgimi.gmsdk.callback.IGMDeviceFoundListener;
import com.xgimi.gmsdk.connect.GMDeviceProxyFactory;
import com.xgimi.gmsdk.connect.IGMDeviceProxy;
import com.xgimi.gmsdk.connect.XGIMILOG;
import com.xgimi.zhushou.BaseActivity;
import com.xgimi.zhushou.R;
import com.xgimi.zhushou.bean.TvApp;
import com.xgimi.zhushou.inter.CommonCallBack;
import com.xgimi.zhushou.netUtil.HttpRequest;
import com.xgimi.zhushou.util.SaveTVApp;
import com.xgimi.zhushou.util.ToastUtil;
import com.xgimi.zhushou.util.ToosUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by XGIMI on 2017/10/18.
 */

public class NewSearchDeviceActivity extends BaseActivity {

    private static final int SEARCH_TIME_OUT = 10000;
    @Bind(R.id.lv_search_result_search_device)
    ListView mSearchResultLv;

    @Bind(R.id.tv_device_num_search_device)
    TextView mSearchSumTv;


    @Bind(R.id.tv_tips_no_device_center)
    TextView mTipsNoDviceCenterTv;

    @Bind(R.id.tv_tips_no_device_bottom)
    TextView mTipsNoDeviceBottomTv;

    @Bind(R.id.pb_search_device)
    ProgressBar mProgress;


    private List<GMDevice> mDeviceList;
    private SearchAdapter mAdapter;

    private IGMDeviceProxy mDeviceProxy;


    /**
     * 搜索到设备的监听
     */
    private IGMDeviceFoundListener mOnDeviceFoundListener = new IGMDeviceFoundListener() {

        @Override
        public void onDeviceFound(GMDevice device) {
            if (!mDeviceList.contains(device)) {
                mSearchSumTv.setText("当前搜索到" + mDeviceList.size() + "个设备");
                mDeviceList.add(device);
                mAdapter.notifyDataSetChanged();
            }
        }

        @Override
        public void onTimeOut() {
            if (mDeviceList.size() == 0) {
                mTipsNoDviceCenterTv.setVisibility(View.VISIBLE);
                mTipsNoDeviceBottomTv.setVisibility(View.GONE);
            } else {
                mTipsNoDeviceBottomTv.setVisibility(View.VISIBLE);
            }
            mDeviceProxy.stopSearchDevice();
            mProgress.setVisibility(View.GONE);
        }

    };


    /**
     * 连接到设备的监听
     */
    private IGMDeviceConnectedListener mOnDeviceConnectedListener = new IGMDeviceConnectedListener() {
        @Override
        public void onDeviceConnected(GMDevice device) {
            XGIMILOG.E("连接成功 : " + device.toString());
            MissDilog();
            ToastUtil.getToast("连接成功", NewSearchDeviceActivity.this).show();
            NewSearchDeviceActivity.this.finish();
        }

        @Override
        public void onTimeOut() {
            MissDilog();
            ToastUtil.getToast("连接超时", NewSearchDeviceActivity.this).show();
        }
    };



    private AdapterView.OnItemClickListener mOnDeviceItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            try {
                mDeviceProxy.connectDevice(mDeviceList.get(position).getIp(), mOnDeviceConnectedListener, 10000);
                mDeviceProxy.stopSearchDevice();
                mTipsNoDeviceBottomTv.setVisibility(View.VISIBLE);
                mProgress.setVisibility(View.GONE);
                showDilog("连接中...");
            } catch (Exception e) {
                XGIMILOG.E(e.getMessage());
                e.printStackTrace();
            }
        }
    };



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_search);
        ButterKnife.bind(this);
        initTitle();
        initView();
    }

    private void initView() {
        mDeviceList = new ArrayList<>();
        mAdapter = new SearchAdapter();
        mSearchResultLv.setAdapter(mAdapter);
        mSearchResultLv.setOnItemClickListener(mOnDeviceItemClickListener);
    }

    @OnClick(R.id.iv_search_device)
    public void onStartSearch(View view) {
        if (mDeviceProxy == null) {
            mDeviceProxy = GMDeviceProxyFactory.createDeviceProxy();
        }
        try {
            if (mDeviceList != null && mDeviceList.size() != 0) {
                mDeviceList.clear();
            }
            mDeviceProxy.searchDevice(mOnDeviceFoundListener, SEARCH_TIME_OUT);
            mProgress.setVisibility(View.VISIBLE);
            mTipsNoDeviceBottomTv.setVisibility(View.GONE);
            mTipsNoDviceCenterTv.setVisibility(View.GONE);
        } catch (Exception e) {
            XGIMILOG.E(e.getMessage());
            e.printStackTrace();
        }
    }


    @OnClick(R.id.tv_tips_no_device_bottom)
    public void onTipsCenterClick(View view) {
        Intent intent = new Intent().setClass(NewSearchDeviceActivity.this, CantConnectNewActivity.class);
        startActivityForResult(intent, 1);
    }

    @OnClick(R.id.tv_tips_no_device_center)
    public void onTipsBottomClick(View View) {
        Intent intent = new Intent().setClass(NewSearchDeviceActivity.this, CantConnectNewActivity.class);
        startActivity(intent);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        XGIMILOG.E(requestCode + ", " + resultCode);
        if (requestCode == 2) {
            finish();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void initTitle() {
        controlTitle(findViewById(R.id.toolbar), true, true, false, false);
        tv.setText("连接极米无屏电视");
    }

    private class SearchAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mDeviceList == null ? 0 : mDeviceList.size();
        }

        @Override
        public Object getItem(int position) {
            return mDeviceList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView == null) {
                convertView = LayoutInflater.from(NewSearchDeviceActivity.this).inflate(R.layout.deviceadapter, parent, false);
                viewHolder = new ViewHolder();
                viewHolder.tvName = (TextView) convertView.findViewById(R.id.tv_device_name_search_device_item);
                viewHolder.tvIp = (TextView) convertView.findViewById(R.id.tv_device_ip_search_device_item);
                viewHolder.ivConnected = (ImageView) convertView.findViewById(R.id.iv_device_connected_search_device_item);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            GMDevice device = mDeviceList.get(position);
            viewHolder.tvName.setText(device.getName());
            viewHolder.tvIp.setText(device.getIp());

            if (mDeviceProxy.isConnectedToDevice()) {
                try {
                    if (device.getIp().equals(mDeviceProxy.getConnectedDevice().getIp())) {
                        viewHolder.ivConnected.setVisibility(View.VISIBLE);
                    } else {
                        viewHolder.ivConnected.setVisibility(View.GONE);
                    }
                } catch (Exception e) {
                    viewHolder.ivConnected.setVisibility(View.GONE);
                    e.printStackTrace();
                }
            } else {
                viewHolder.ivConnected.setVisibility(View.GONE);
            }

            return convertView;
        }
    }

    private class ViewHolder {
        TextView tvName;
        TextView tvIp;
        ImageView ivConnected;
    }
}
