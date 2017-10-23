package com.xgimi.gmsdk.bean.reply;

/**
 * Created by Tommy on 2017/9/6.
 */

public class DeviceKeyboardStateChangePacket extends Packet {

    private int action;

    public int getAction() {
        return action;
    }

    public void setAction(int action) {
        this.action = action;
    }
}
