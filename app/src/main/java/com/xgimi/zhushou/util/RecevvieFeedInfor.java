package com.xgimi.zhushou.util;

import android.content.Context;

import com.xgimi.device.callback.FeedBackInforInterface;
import com.xgimi.device.device.FeedbackInfo;

/**
 * Created by 霍长江 on 2016/8/29.
 */
public class RecevvieFeedInfor implements FeedBackInforInterface{
    public static RecevvieFeedInfor instance;
    public Context context;
    public static RecevvieFeedInfor getInstance(Context  context){
        if(instance==null){
            instance=new RecevvieFeedInfor(context);
        }
        return instance;
    }
    private RecevvieFeedInfor(Context context){
        this.context=context;
    }
    @Override
    public void receive(FeedbackInfo info) {

    }
}
