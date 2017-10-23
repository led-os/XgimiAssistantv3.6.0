package com.xgimi.gmsdk.bean.reply;

/**
 * Created by Tommy on 2017/7/26.
 */

public class SearchReplyPacket extends Packet{

    /**
     * action : 9999
     * deviceInfo : {"deviceip":"192.168.2.161","devicename":"客厅","devicetype":"guava","versioncode":0}
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
         * deviceip : 192.168.2.161
         * devicename : 客厅
         * devicetype : guava
         * versioncode : 0
         */

        private String deviceip;
        private String devicename;
        private String devicetype;
        private int versioncode;

        public String getDeviceip() {
            return deviceip;
        }

        public void setDeviceip(String deviceip) {
            this.deviceip = deviceip;
        }

        public String getDevicename() {
            return devicename;
        }

        public void setDevicename(String devicename) {
            this.devicename = devicename;
        }

        public String getDevicetype() {
            return devicetype;
        }

        public void setDevicetype(String devicetype) {
            this.devicetype = devicetype;
        }

        public int getVersioncode() {
            return versioncode;
        }

        public void setVersioncode(int versioncode) {
            this.versioncode = versioncode;
        }
    }
}
