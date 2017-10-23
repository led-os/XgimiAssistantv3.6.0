package com.xgimi.zhushou.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by 霍长江 on 2016/8/15.
 */
public class FilmDetailBean implements Serializable{



    public DataBean data;
    public int code;
    public String message;


    public static class DataBean implements Serializable{
        public String title;
        public String image;
        public String id;
        public String description;
        public String actors;
        public String area;
        public String director;
        public String kind;
        public String category;
        public String year;
        public int updateto;
        public int length;


        public List<SourceBean> source;


        public List<RecommendBean> recommend;



        public static class SourceBean implements Serializable{
            public String from;
            public String icon;
            public String df_icon;
            public GmIntentBean gm_intent;

            public static class GmIntentBean implements Serializable{
                public String id;
                public String source;
                public String ap;
                public String p;
                public String n;
                public String v;
                public String o;
                public String u;

                public GmIsBean gm_is;

                public static class GmIsBean implements Serializable{
                    public String i;
                    public String episode;
                    public String m;
                }
            }

        }

        public static class RecommendBean implements Serializable{
            public String gm_id;
            public String name;
            public String cover;
            public String video_id;

        }
    }
}
