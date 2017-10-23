package com.xgimi.device.vcontrolcmd;

import android.util.Log;

import com.google.gson.Gson;
import com.xgimi.device.device.GMDeviceController;
import com.xgimi.device.device.VcontrolCmd;

/**
 * Created by 霍长江 on 2016/8/23.
 */
public class VcontrolControl {
    public static VcontrolControl instance;

    private VcontrolControl() {
    }

    public static VcontrolControl getInstance() {
        if (instance == null) {
            instance = new VcontrolControl();
        }
        return instance;
    }

    public String getConnectControl(VcontrolCmd cmd) {
        String jsonObject = new Gson().toJson(cmd);
        Log.e("info", jsonObject.toString());
        return jsonObject.toString();
    }

    public void sendNewCommand(String com) {
        GMDeviceController.getInstance().SendNewJC(com);
        Log.e("tongji", com);
    }
}
