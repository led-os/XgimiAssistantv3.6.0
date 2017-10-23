package com.xgimi.gmsdk.bean.reply;


/**
 * Created by Tommy on 2017/7/26.
 */

public class ConnectReplyPacket extends Packet {

    /**
     * action : 10001
     * deviceInfo : {"device":"guava","devicetype":"guava","ipaddress":"192.168.1.111","mac":"00-88-87-22-33-57","versioncode":20606}
     * msgid : 1
     */

    private int action;
    private DeviceInfoBean deviceInfo;
    private String msgid;

    public int getAction() {
        return action;
    }

    public void setAction(int action) {
        this.action = action;
    }

    public DeviceInfoBean getDeviceInfo() {
        return deviceInfo;
    }

    public void setDeviceInfo(DeviceInfoBean deviceInfo) {
        this.deviceInfo = deviceInfo;
    }

    public String getMsgid() {
        return msgid;
    }

    public void setMsgid(String msgid) {
        this.msgid = msgid;
    }

    public static class DeviceInfoBean {
        /**
         * device : guava
         * devicetype : guava
         * ipaddress : 192.168.1.111
         * mac : 00-88-87-22-33-57
         * versioncode : 20606
         */

        private String device;
        private String devicetype;
        private String ipaddress;
        private String mac;
        private int versioncode;

        public String getDevice() {
            return device;
        }

        public void setDevice(String device) {
            this.device = device;
        }

        public String getDevicetype() {
            return devicetype;
        }

        public void setDevicetype(String devicetype) {
            this.devicetype = devicetype;
        }

        public String getIpaddress() {
            return ipaddress;
        }

        public void setIpaddress(String ipaddress) {
            this.ipaddress = ipaddress;
        }

        public String getMac() {
            return mac;
        }

        public void setMac(String mac) {
            this.mac = mac;
        }

        public int getVersioncode() {
            return versioncode;
        }

        public void setVersioncode(int versioncode) {
            this.versioncode = versioncode;
        }
    }
}
