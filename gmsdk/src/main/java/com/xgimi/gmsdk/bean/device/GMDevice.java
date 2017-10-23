package com.xgimi.gmsdk.bean.device;

/**
 * Created by Tommy on 2017/7/26.
 */

public class GMDevice {
    private String ip;
    private String type;
    private String mac;
    private String name;
    private int versionCode;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(int versionCode) {
        this.versionCode = versionCode;
    }

    public GMDevice() {
    }

    public GMDevice(String ip, String type, String mac) {
        this.ip = ip;
        this.type = type;
        this.mac = mac;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }


    @Override
    public String toString() {
        return "GMDevice{" +
                "ip='" + ip + '\'' +
                ", type='" + type + '\'' +
                ", mac='" + mac + '\'' +
                ", name='" + name + '\'' +
                ", versionCode=" + versionCode +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }

        if (!(o instanceof GMDevice)) {
            return false;
        }

        GMDevice another = (GMDevice) o;
        if (another.getIp() == null || another.getName() == null) {
            return false;
        }

        return another.getIp().equals(this.getIp()) && another.getName().equals(this.getName());
    }
}
