package com.xgimi.gmsdk.connect;

/**
 * Created by XGIMI on 2017/9/8.
 */

public class GMDeviceProxyFactory {

    public static IGMDeviceProxy createDeviceProxy() {
        return DeviceProxy.getInstance();
    }
}
