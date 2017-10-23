package com.xgimi.zhushou.bean;

import java.util.List;

/**
 * Created by Administrator on 2016/8/12.
 */
public class MVTuijian {
    public List<data> data;
    public int code;
    public String message;
    public static class data{
        public String mv_id;
        public String mv_title;
        public String mv_artist;
        public String mv_thumb;
        public String mv_play_address;
        public String collect_id;
    }
}
