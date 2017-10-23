package com.xgimi.gmsdk.callback;

import com.xgimi.gmsdk.bean.device.GMDevice;

/**
 * Created by XGIMI on 2017/9/8.
 */

public abstract class IGMDeviceFoundListener {

    public abstract void onDeviceFound(GMDevice device);

    public void onTimeOut() {
    }
}
