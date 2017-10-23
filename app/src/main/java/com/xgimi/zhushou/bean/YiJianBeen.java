package com.xgimi.zhushou.bean;

/**
 * Created by Administrator on 2016/10/22.
 */
public class YiJianBeen {
    public String data;
    public String type;
    public YiJianBeen(){

    }
    public YiJianBeen(String data, String type){
        this.data=data;
        this.type=type;
    }
    public String getData(){
        return data;
    }
    public void setData(String data){
        this.data=data;
    }
    public String getType(){
        return type;
    }
    public void setType(String type){
        this.type=type;
    }
}
