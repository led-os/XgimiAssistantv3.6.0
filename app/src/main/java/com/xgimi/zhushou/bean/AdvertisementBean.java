package com.xgimi.zhushou.bean;

import java.util.List;

/**
 * Created by 霍长江 on 2016/10/19.
 */
public class AdvertisementBean {
    public int code;
    public String message;
    public List<DataBean> data;
    public static class DataBean {
        public String ad_title;
        public String ad_description;
        public String ad_imgurl;
        public String ad_actionurl;
        public String created_at;
        public String type;
        public String content;
        public String type_id;
    }
}
