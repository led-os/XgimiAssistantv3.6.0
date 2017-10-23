package com.xgimi.zhushou.bean;

import java.util.List;

/**
 * Created by Administrator on 2016/8/27.
 */
public class AppCollectBeen {
    public List<data> data;
    public int code;
    public String message;
    public static class data{
        public String id;
        public String title;
        public String icon;
        public String package_name;
        public String version;
        public String file_size;
        public String download_url;
    }
}
