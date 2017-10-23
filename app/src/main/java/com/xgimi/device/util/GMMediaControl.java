package com.xgimi.device.util;

import com.xgimi.device.device.GMDeviceController;

/**
 * Created by 霍长江 on 2016/8/25.
 */
public class GMMediaControl {
    private static GMMediaControl threedController;
    public static GMMediaControl getInstance(){
        if(threedController==null){
            threedController=new GMMediaControl();
        }
        return threedController;
    }
    private GMMediaControl(){
    }
    public void openVideo(String url,int type){
        GMDeviceController.getInstance().SendJC(JsonData.getInstance().sendFileJson(url,type));
    }
}
