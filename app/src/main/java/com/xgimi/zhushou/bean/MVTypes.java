package com.xgimi.zhushou.bean;

import java.util.List;

/**
 * Created by Administrator on 2016/8/12.
 */
public class MVTypes {
    public List<data> data;
    public int code;
    public String message;
    public static class data{
        public String area_id;
        public String area_name;
        public String order;
    }
}
