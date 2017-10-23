package com.xgimi.gmsdk.callback;

import com.xgimi.gmsdk.bean.device.GMDevice;

/**
 * Created by XGIMI on 2017/9/8.
 */

public abstract class IGMDeviceConnectedListener {

    public abstract void onDeviceConnected(GMDevice device);

    public void onTimeOut() {
    }
}
