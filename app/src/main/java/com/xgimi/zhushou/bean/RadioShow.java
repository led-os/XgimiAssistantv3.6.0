package com.xgimi.zhushou.bean;

import java.util.List;

/**
 * Created by Administrator on 2016/8/18.
 */
public class RadioShow {
    public List<data> data;
    public int code;
    public static class data{
        public String program_id;
        public String program_title;
        public String program_duration;
        public String program_play_time;
        public String program_update_time;
        public String program_play_address;
    }
}
