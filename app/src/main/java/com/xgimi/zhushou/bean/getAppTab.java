package com.xgimi.zhushou.bean;

import java.util.List;

/**
 * Created by Administrator on 2016/10/24.
 */
public class getAppTab {
    public List<data> data;
    public int code;
    public String message;
    public static class data{
        public String text;
        public String title;
        public String position;
        public red_point red_point;
        public static class red_point{
            public String show;
            public String id;
        }
    }
}
