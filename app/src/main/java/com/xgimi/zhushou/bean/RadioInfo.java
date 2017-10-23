package com.xgimi.zhushou.bean;

import java.util.List;

/**
 * Created by Administrator on 2016/8/17.
 */
public class RadioInfo {
    public List<data> data;
    public int code;
    public String message;
    public static class data{
        public String fm_title;
        public String small_thumb;
        public String medium_thumb;
        public String large_thumb;
        public int fm_star;
        public String category_name;
        public String fm_description;
        public int category_id;
        public String fm_author;
        public String collect_id;
    }
}
