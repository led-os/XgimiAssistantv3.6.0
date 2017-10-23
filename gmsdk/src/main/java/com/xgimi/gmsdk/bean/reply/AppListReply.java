package com.xgimi.gmsdk.bean.reply;

/**
 * Created by Tommy on 2017/7/26.
 */

public class AppListReply extends Packet {

    /**
     * action : 30236
     * imagePath : http://%s:16741/data/data/com.xgimi.vcontrol/app_appDatas/list
     */
    private int action;
    private String imagePath;

    public int getAction() {
        return action;
    }

    public void setAction(int action) {
        this.action = action;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
}
