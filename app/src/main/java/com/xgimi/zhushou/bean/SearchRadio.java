package com.xgimi.zhushou.bean;

import java.util.List;

/**
 * Created by Administrator on 2016/8/17.
 */
public class SearchRadio {
    public List<data> data;
    public int code;
    public String message;
    public static class data{
        public int fm_id;
        public String fm_title;
        public String fm_cover;
        public String medium_thumb;
        public String large_thumb;
    }
}
