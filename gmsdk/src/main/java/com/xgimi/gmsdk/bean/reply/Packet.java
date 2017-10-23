package com.xgimi.gmsdk.bean.reply;

/**
 * Created by Tommy on 2017/7/26.
 */

public class Packet {

    private String msg;
    private String realIP;

    public Packet() {
    }

    public Packet(String msg, String realIP) {
        this.msg = msg;
        this.realIP = realIP;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getRealIP() {
        return realIP;
    }

    public void setRealIP(String realIP) {
        this.realIP = realIP;
    }
}
