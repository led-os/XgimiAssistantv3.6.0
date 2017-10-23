package com.xgimi.zhushou.bean;

import java.util.List;

/**
 * Created by 霍长江 on 2016/8/9.
 */
public class AllChannel {
    public int code;
    public String message;
    public List<DataBean> data;
    public static class DataBean {
//        public String id;//
        public String chinese_name;
//        public String english_name;//
//        public String identifier;//
//        public String class_identifier;
        public List<ChannelsBean> channels;
        public static class ChannelsBean {
            public String id;
//            public String live_id;//
            public String channel_id;
            public String name;
//            public String english_name;//
            public String icon;
//            public String url;//
//            public String chn_type;//
            public GmIntentBean gm_intent;
            public static class GmIntentBean {
//                public String id;//
                public String n;
                public String u;
                public String p;
                public GmIsBean gm_is;
                public static class GmIsBean {
                    public String i;
                    public String m;
                }
            }
        }
    }
}
