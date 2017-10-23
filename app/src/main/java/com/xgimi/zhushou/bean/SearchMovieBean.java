package com.xgimi.zhushou.bean;

import java.util.List;

/**
 * Created by 霍长江 on 2016/8/13.
 */
public class SearchMovieBean {


    public int code;
    public String message;


    public List<DataBean> data;



    public static class DataBean {
        public String title;
        public String image;
        public String id;
        public Object description;
        public String actors;
        public String area;
        public Object director;
        public String kind;
        public String category;
        public String year;


        public List<SourceBean> source;



        public static class SourceBean {
            public String id;
            public String from;
            public String icon;
            public String df_icon;
            public String name;


            public IntentBean intent;



            public static class IntentBean {
                public String id;
                public String source;
                public String ap;
                public String p;
                public String n;
                public String v;
                public String o;
                public String u;
                /**
                 * id : 148294
                 * video_id : 70845
                 * intent_id : 1
                 * m : 1
                 * i : #Intent;action=myvst.intent.action.MediaDetail;launchFlags=0x1400c000;package=net.myvst.v2;S.uuid=6245367362336E6A5565A2;end
                 */

                public List<IsBean> iss;



                public static class IsBean {
                    public String id;
                    public String video_id;
                    public String intent_id;
                    public String m;
                    public String i;


                }
            }
        }
    }
}
