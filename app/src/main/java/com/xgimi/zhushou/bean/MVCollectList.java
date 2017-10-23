package com.xgimi.zhushou.bean;

import java.util.List;

/**
 * Created by Administrator on 2016/8/25.
 */
public class MVCollectList {
    public List<data> data;
    public int code;
    public String message;
    public static class data{
        public String collect_id;
        public String mf_id;
        public String mf_type;
        public String mf_title;
        public String mf_cover;
        public String mf_author;
        public String mf_play_address;
        public String add_time;
    }
}
