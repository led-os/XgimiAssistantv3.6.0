package com.xgimi.gmsdk.callback;


import com.xgimi.gmsdk.bean.device.GMDevice;

/**
 * Created by Tommy on 2017/7/26.
 */

public class GMConnectListeners {

//    public static interface IGMDeviceFoundListener {
//        void onDeviceFound(GMDevice device);
//    }
//
//    public static interface IGMDeviceConnectedListener {
//        void onDeviceConnected(GMDevice device);
//    }

    public static interface IGMReceivedDeviceMsgListener {
        void onReceivedMsgFromDevice(String msg, int type);
    }
//
//    public static interface IGMReceivedScreenShotResultListener {
//        void onReceivedScreenShotResult(String imgUrl);
//    }

}
