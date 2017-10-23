package com.xgimi.zhushou.bean;

/**
 * Created by 霍长江 on 2016/10/12.
 */
public class IntelligenceBean {
    public String ip;
    public String name;


    public IntelligenceBean(String ip, String name) {
        this.ip = ip;
        this.name = name;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
