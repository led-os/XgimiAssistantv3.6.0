package com.xgimi.zhushou.bean;

import java.util.List;
/**
 * Created by 霍长江 on 2016/8/26.
 */
public class IqiyiBean {
    public int code;
    public String message;
    public List<DataBean> data;
    public static class DataBean {
        public String id;
        public String video_id;
        public String from;
        public String episode;
        public String title;
        public GmIntentBean gm_intent;
        public static class GmIntentBean {
            public String id;
            public String p;
            public String n;
            public String o;
            public String v;
            public String u;
            public List<GmIsBean> gm_is;
            public static class GmIsBean {
                public String m;
                public String i;
            }
        }
    }
}
