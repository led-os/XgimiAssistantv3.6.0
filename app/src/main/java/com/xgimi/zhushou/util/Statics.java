package com.xgimi.zhushou.util;

import android.content.Context;

import com.xgimi.device.device.VcontrolCmd;
import com.xgimi.device.util.GMSdkCheck;
import com.xgimi.device.vcontrolcmd.VcontrolControl;
import com.xgimi.zhushou.App;
/**
 * Created by 霍长江 on 2017/1/17.
 */
public class Statics {
    public static Statics instance;
    public static Statics getInstance(){
        if(instance==null){
            instance=new Statics();
        }
        return instance;
    }
    //影视的点击事件统计
    public void sendStatics(Context context,String name,String id,String sourceInsight,String page,String souInshtLocation){
        if(Constant.netStatus) {
            VcontrolControl.getInstance().sendNewCommand(VcontrolControl.getInstance().getConnectControl(new VcontrolCmd(40000, "2", GMSdkCheck.appId, DeviceUtils.getappVersion(context), App.getContext().PACKAGENAME, new VcontrolCmd.Statistics(name, id, sourceInsight, page, souInshtLocation))));
        }
    }
    public void sendSearchStatics(Context context,String seContent, String seInputContent, String seType){
        if(Constant.netStatus){
            VcontrolControl.getInstance().sendNewCommand(VcontrolControl.getInstance().getConnectControl(new VcontrolCmd(40000, "2", GMSdkCheck.appId, DeviceUtils.getappVersion(context), App.getContext().PACKAGENAME, new VcontrolCmd.Statistics(seContent,seInputContent,seType))));
        }
    }

}
