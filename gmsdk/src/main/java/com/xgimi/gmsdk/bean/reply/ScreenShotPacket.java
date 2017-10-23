package com.xgimi.gmsdk.bean.reply;

/**
 * Created by Tommy on 2017/9/6.
 */

public class ScreenShotPacket extends Packet{

    /**
     * action : 30235
     * imagePath : http://%s:16741/storage/emulated/0/Pictures/Screenshots/Screenshot_09-06-19-26-05.jpg
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
