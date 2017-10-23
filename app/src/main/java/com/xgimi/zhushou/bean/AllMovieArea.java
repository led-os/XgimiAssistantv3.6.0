package com.xgimi.zhushou.bean;

import java.util.List;

/**
 * Created by 霍长江 on 2016/8/10.
 */
public class AllMovieArea {
    public int code;
    public String message;
    public List<DataBean> data;
    public static class DataBean {
        public String id;
        public String name;
    }
}
