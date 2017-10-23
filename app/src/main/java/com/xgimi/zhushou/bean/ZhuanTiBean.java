package com.xgimi.zhushou.bean;

import java.util.List;

/**
 * Created by 霍长江 on 2016/8/23.
 */
public class ZhuanTiBean {

    public DataBean data;
    public int code;
    public String message;
    public static class DataBean {
        public String title;
        public String description;
        public String image;
        public List<ContentsBean> contents;
        public static class ContentsBean {
            public String type;
            public String content_id;
            public String name;
            public String img;
            public String description;
        }
    }
}
