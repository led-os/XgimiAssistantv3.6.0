package com.xgimi.zhushou.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;
import android.widget.Toast;

import com.xgimi.device.device.GMDeviceStorage;
import com.xgimi.device.utils.StringUtils;
import com.xgimi.zhushou.BaseActivity;
import com.xgimi.zhushou.R;
import com.xgimi.zhushou.util.XGIMILOG;
import com.xgimi.zhushou.util.YiPingUrl;

public class TontPingQianActivity extends BaseActivity {
    private String path = ""; // 播放地址

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yi_ping);
        TextView tv = (TextView) findViewById(R.id.tv);
        findUrl(GMDeviceStorage.getInstance().getConnectedDevice().ip);
    }

    /**
     * 寻找同屏的播放地址
     */
    public void findUrl(final String IP) {
        new Thread() {
            public void run() {
                YiPingUrl pingSer = new YiPingUrl(IP);
                path = pingSer.getYipingUrl();
                handler.sendEmptyMessage(100);
            }
        }.start();
    }

    Handler handler = new Handler() {

        public void handleMessage(android.os.Message msg) {
            super.handleMessage(msg);
            if (StringUtils.isEmpty(path)) {
                Toast.makeText(TontPingQianActivity.this, "同屏出错", Toast.LENGTH_SHORT).show();
                return;
            }

            XGIMILOG.E(path);
            Intent intent = new Intent(TontPingQianActivity.this, YiPingActivity.class);
            intent.putExtra("path", path);
            intent.putExtra("ip", GMDeviceStorage.getInstance().getConnectedDevice().ip);
            startActivity(intent);
            finish();
        }
    };
}
