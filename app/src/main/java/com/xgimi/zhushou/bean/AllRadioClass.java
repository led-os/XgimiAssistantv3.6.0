package com.xgimi.zhushou.bean;

import java.util.List;

/**
 * Created by Administrator on 2016/8/17.
 */
public class AllRadioClass {
    public List<data> data;
    public int code;
    public String message;
    public static class data{
        public int category_id;
        public String category_name;
        public int sort_id;
        public String category_thumb;
    }
}
