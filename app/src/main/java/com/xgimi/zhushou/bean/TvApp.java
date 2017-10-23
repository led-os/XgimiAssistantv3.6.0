package com.xgimi.zhushou.bean;

import java.util.List;

/**
 * Created by 霍长江 on 2016/8/17.
 */
public class TvApp {
    public String message;
    public int code;
    public List<Appitem> appList;
    public static class Appitem{
        public String appName; // app名字
        public String packageName; // 包名
        public long length; // app大小
        public long firstInstallTime;// 安装时间
        public boolean systemApp;// 系统应用
    }
}
