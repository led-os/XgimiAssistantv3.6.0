package com.xgimi.zhushou.bean;

import java.util.List;

/**
 * Created by 霍长江 on 2016/8/13.
 */
public class SearchLiveTv {


  

    public int code;
    public String message;
   

    public List<DataBean> data;



    public static class DataBean {
        public String id;
        public String live_id;
        public String channel_id;
        public String name;
        public String english_name;
        public String icon;
        public String url;
        public String chn_type;
       

        public GmIntentBean gm_intent;



        public static class GmIntentBean {
            public String id;
            public String n;
            public String u;
            public String p;
            /**
             * i : #Intent;action=com.elinkway.tvlive2.playUrl;launchFlags=0x10000000;component=com.elinkway.tvlive2/.activity.ThirdLauncherSplashActivity;S.data_source=http%3A%2F%2F7xo8iy.com2.z0.glb.qiniucdn.com%2F3rd.json;S.from=com.xgimi.vcontrol;S.channel_url=http%3A%2F%2Fgslb.live.video123456.com%2Fgslb%3Fstream_id%3Dws_hunanwsHD_1800%26tag%3Dlive%26ext%3Dm3u8%26sign%3Dlive_tv;end
             * m : 1
             */

            public GmIsBean gm_is;



            public static class GmIsBean {
                public String i;
                public String m;


            }
        }
    }
}
