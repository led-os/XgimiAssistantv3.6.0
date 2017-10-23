package com.xgimi.zhushou.bean;

import java.util.List;

/**
 * Created by Administrator on 2016/8/27.
 */
public class MovieCollectBeen {
    public List<data> data;
    public int code;
    public String message;
    public static class data{
        public String id;
        public String video_id;
        public String video_title;
        public String video_type;
        public String video_cover;
        public String add_time;
    }
}
