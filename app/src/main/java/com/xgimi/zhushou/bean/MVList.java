package com.xgimi.zhushou.bean;

import java.util.List;

/**
 * Created by Administrator on 2016/8/16.
 */
public class MVList {
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
        public String mv_type_name;
        public data(String mv_id,String mv_title,String mv_artist,String mv_thumb,String mv_play_address,String collect_id,String mv_type_name){
            this.mv_id=mv_id;
            this.mv_title=mv_title;
            this.mv_artist=mv_artist;
            this.mv_thumb=mv_thumb;
            this.mv_play_address=mv_play_address;
            this.collect_id=collect_id;
            this.mv_type_name=mv_type_name;
        }
    }
}
