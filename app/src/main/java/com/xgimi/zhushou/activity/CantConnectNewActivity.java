package com.xgimi.zhushou.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.gson.Gson;


import com.xgimi.gmsdk.bean.device.GMDevice;
import com.xgimi.gmsdk.bean.reply.AppListReply;
import com.xgimi.gmsdk.callback.IGMDeviceConnectedListener;
import com.xgimi.gmsdk.connect.GMDeviceProxyFactory;
import com.xgimi.gmsdk.connect.IGMDeviceProxy;
import com.xgimi.zhushou.App;
import com.xgimi.zhushou.BaseActivity;
import com.xgimi.zhushou.R;
import com.xgimi.zhushou.netUtil.NetUtil;
import com.xgimi.zhushou.util.Constant;
import com.xgimi.zhushou.util.SaveTVApp;
import com.xgimi.zhushou.util.ToastUtil;
import com.xgimi.zhushou.util.ToosUtil;
import com.xgimi.zhushou.util.XGIMILOG;
import com.xgimi.zhushou.widget.AutoInputIp;
import com.zxing.android.CaptureActivity;

import de.greenrobot.event.EventBus;

/**
 * Created by Administrator on 2017/1/16.
 */
public class CantConnectNewActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "CantConnectNewActivity";
    private static final int CONNECT_TIME_OUT = 1;
    private static final int COUNT_TIME_MAX = 10000;

    private RelativeLayout mScanByCameraRl;
    private RelativeLayout mConnectByIpRl;
    private static final int REQUEST_CODE_SCAN = 0x0000;
    private static final String DECODED_CONTENT_KEY = "codedContent";
    private static final String DECODED_BITMAP_KEY = "codedBitmap";
    private String SSID;
    private boolean isLive = false;
    private AutoInputIp mInputIPDg;
    private boolean mIsActivityDetroy = false;
    private IGMDeviceProxy mDeviceProxy;

    private IGMDeviceConnectedListener mDeviceConnecteListener = new IGMDeviceConnectedListener() {
        @Override
        public void onDeviceConnected(GMDevice device) {
            MissDilog();
            ToastUtil.getToast("连接成功", CantConnectNewActivity.this).show();
            setResult(2);
            CantConnectNewActivity.this.finish();
        }

        @Override
        public void onTimeOut() {
            if (mIsActivityDetroy) {
                return;
            }
            MissDilog();
            ToastUtil.getToast("连接超时", CantConnectNewActivity.this).show();
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cant_connect_new);
        initView();
        if (mDeviceProxy == null) {
            mDeviceProxy = GMDeviceProxyFactory.createDeviceProxy();
        }
    }


    private void initView() {
        isLive = false;
        controlTitle(findViewById(R.id.id_toolbar), true, true, false, false);
        tv.setText("连接极米无屏电视");
        mScanByCameraRl = (RelativeLayout) findViewById(R.id.rl_scan);
        mConnectByIpRl = (RelativeLayout) findViewById(R.id.rl_scan_by_ip);
        mScanByCameraRl.setOnClickListener(this);
        mScanByCameraRl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CantConnectNewActivity.this,
                        CaptureActivity.class);
                startActivityForResult(intent, REQUEST_CODE_SCAN);
            }
        });
        mConnectByIpRl.setClickable(true);
        mConnectByIpRl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showInputIpDialog();
            }
        });
    }

    private AutoInputIp.onListener mInputIpListener = new AutoInputIp.onListener() {
        @Override
        public void onSend(String ip) {
            if (getIp2(ip).equals(getIp2(getPhoneIp())) && !getIp2(ip).equals("")) {
                showDilog("连接中...");
                XGIMILOG.E("正在连接到 : " + ip);
                try {
                    mDeviceProxy.connectDevice(ip, mDeviceConnecteListener, 10000);
                } catch (Exception e) {
                    XGIMILOG.E(e.getMessage());
                    e.printStackTrace();
                }
            } else {
                ToastUtil.getToast("不在同一个网络", CantConnectNewActivity.this).show();
            }

        }
    };


    private void showInputIpDialog() {
        if (mInputIPDg == null) {
            mInputIPDg = new AutoInputIp(this);
            mInputIPDg.setCanceledOnTouchOutside(true);
            mInputIPDg.setLisener(mInputIpListener);
        }
        mInputIPDg.show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 扫描二维码/条码回传
        if (requestCode == REQUEST_CODE_SCAN && resultCode == RESULT_OK) {
            if (data != null) {
                String content = data.getStringExtra(DECODED_CONTENT_KEY);
                if (content.contains("ip=")) {
                    String ip = getIp(content);
                    String ssID = getSSID(content);
                    XGIMILOG.D("机器ip网段" + getIp2(ip) + "手机ip网段" + getIp2(getPhoneIp()));
                    XGIMILOG.D("机器ip" + ip + "手机ip" + getPhoneIp());
                    if (getWifi().replace("\"", "").equals(ssID) && content.contains("ssid=")) {
                        showDilog("连接中...");
                        try {
                            mDeviceProxy.connectDevice(ip, mDeviceConnecteListener, 10000);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    } else if (getIp2(ip).equals(getIp2(getPhoneIp())) && !getIp2(ip).equals("")) {
                        showDilog("连接中...");
                        try {
                            mDeviceProxy.connectDevice(ip, mDeviceConnecteListener, 10000);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        ToastUtil.getToast("不在同一个网络", this).show();
                    }
                }
                MissDilog();
            }
        }
    }




    @Override
    protected void onDestroy() {
        mIsActivityDetroy = true;
        super.onDestroy();
        isLive = true;
    }

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:

                    Intent intent = new Intent(CantConnectNewActivity.this, RemountActivity.class);
                    startActivity(intent);
                    finish();
                    break;
            }
        }
    };

    //获取SSID与当前的比较
    public static String getSSID(String ssID) {
        String jieguo = ssID.substring(ssID.indexOf("ssid=") + 5);
        return getSSID1(jieguo);
    }

    public static String getSSID1(String ssID) {
        String jieguo = ssID.substring(0, ssID.indexOf("&ip"));
        return jieguo;
    }

    public static String getIp(String fileName) {
        String jieguo = fileName.substring(fileName.indexOf("ip=") + 1);
        getIp1(jieguo);
        return getIp1(jieguo);
    }

    public static String getIp1(String fileName) {
        String jieguo = fileName.substring(fileName.indexOf("=") + 1);
        String newStr2 = jieguo.replaceAll("-", ".");
        String regexString = ".*(\\d{3}(\\.\\d{1,3}){3}).*";
        String IPString = newStr2.replaceAll(regexString, "$1");
        return IPString;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_scan:
                Intent intent = new Intent(CantConnectNewActivity.this,
                        CaptureActivity.class);
                startActivityForResult(intent, REQUEST_CODE_SCAN);
                break;
        }
    }

    // 获取wifi
    public String getWifi() {
        @SuppressLint("WifiManagerLeak") WifiManager wifiMgr = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = wifiMgr.getConnectionInfo();
        String wifiId = info != null ? info.getSSID() : null;
        return wifiId;
    }

    //获取手机ip地址
    public String getPhoneIp() {
        //获取wifi服务
        @SuppressLint("WifiManagerLeak") WifiManager wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        //判断wifi是否开启
        if (!wifiManager.isWifiEnabled()) {
            wifiManager.setWifiEnabled(true);
        }
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        int ipAddress = wifiInfo.getIpAddress();
        String ip = intToIp(ipAddress);
        return ip;
    }

    private String intToIp(int i) {

        return (i & 0xFF) + "." +
                ((i >> 8) & 0xFF) + "." +
                ((i >> 16) & 0xFF) + "." +
                (i >> 24 & 0xFF);
    }




    public String getIp2(String result) {
        XGIMILOG.D("ip = " + result);
//        String jieguo = ssID.substring(0, ssID.indexOf("."));
        String[] sourceStrArray = result.split("\\.");
        if (sourceStrArray != null && sourceStrArray.length > 3) {
            return sourceStrArray[0] + "." + sourceStrArray[1] + "." + sourceStrArray[2];
        }
        return "";
    }



}
