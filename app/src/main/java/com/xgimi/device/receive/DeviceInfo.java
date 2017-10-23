package com.xgimi.device.receive;

/**
 * Created by 霍长江 on 2016/8/23.
 */
public class DeviceInfo {
    public DeviceInfoBean deviceInfo;
    public String msgid;
    public int action;
    public static class DeviceInfoBean {
        public String devicetype;
        public String ipaddress;
        public int versioncode;
    }
}
