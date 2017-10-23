package com.xgimi.zhushou.bean;

import java.util.List;

/**
 * Created by 霍长江 on 2016/8/10.
 */
public class AllMovie {
    /**
     * msg : success
     * code : 200
     * data : [{"video_id":"2caf8ab3c3f2600059120a129978d255","name":"北壁","cover":"http://pic7.qiyipic.com/image/20151224/1e/62/v_108736638_m_601_m3_260_360.jpg"},{"video_id":"7a67f4c076c20bcfe431c4fa0fda2e11","name":"乱","cover":"http://pic2.qiyipic.com/image/20150317/2c/58/v_107342338_m_601_m1_260_360.jpg"},{"video_id":"0ea78ebf7ae84cb8db28b0f0868a29e9","name":"决战以拉谷","cover":"http://pic9.qiyipic.com/image/20150316/4f/30/63/v_50546284_m_601_m2_260_360.jpg"},{"video_id":"d24314383ac7f1a8161c730a2509a247","name":"勇气","cover":"http://pic1.qiyipic.com/image/20150319/9c/36/4d/v_50194085_m_601_m2_260_360.jpg"},{"video_id":"9fa15f4c85e87b325aa3e29a7fa9289f","name":"单身男子俱乐部","cover":"http://pic3.qiyipic.com/image/20150415/b4/39/eb/v_106022863_m_601_m2_260_360.jpg"},{"video_id":"f42605a19928ac52974e8a3f2ad78216","name":"僵尸世界大战","cover":"http://pic7.qiyipic.com/image/20160914/89/d0/v_100306376_m_601_m3_260_360.jpg"},{"video_id":"e04516f92a75cc300afdfb0eebcc9136","name":"从心开始","cover":"http://pic2.qiyipic.com/image/20160229/84/fe/v_50120122_m_601_m5_260_360.jpg"},{"video_id":"1c401d307b22af514b8e23c8fd8b79d1","name":"卫斯理传奇","cover":"http://pic0.qiyipic.com/image/20151202/f2/80/v_62815911_m_601_m2_260_360.jpg"},{"video_id":"7599d04723b1d4965909c572c36280c5","name":"一夜大肚","cover":"http://pic5.qiyipic.com/image/20140728/cd/02/v_62645834_m_601_260_360.jpg"},{"video_id":"b8feb16c850e0adede8f9f1ef25bfd94","name":"千钧一发","cover":"http://pic1.qiyipic.com/image/20150317/ac/82/03/v_50116449_m_601_m3_260_360.jpg"},{"video_id":"49aec424c16aeae9289de43d5689f0a1","name":"北京遇上西雅图","cover":"http://pic3.qiyipic.com/image/20160427/cc/06/v_50507397_m_601_m5_260_360.jpg"},{"video_id":"d8dee832204216a02bfd1efad7608bb0","name":"何必有我","cover":"http://pic0.qiyipic.com/image/20151120/7f/2c/v_62817409_m_601_m1_260_360.jpg"},{"video_id":"0f1c8439866c8fc48bb683b15b34a314","name":"后院","cover":"http://pic8.qiyipic.com/image/20141103/71/85/v_108664040_m_601_m1_260_360.jpg"},{"video_id":"a74a58ff2ae578917e39efda064be09b","name":"临终囧事","cover":"http://pic8.qiyipic.com/image/20151110/71/5f/v_50623450_m_601_260_360.jpg"},{"video_id":"69605ac1ee1250f5f2307e2c35c5fe8f","name":"变身超人","cover":"http://pic7.qiyipic.com/image/20151110/5d/25/v_50667741_m_601_m6_260_360.jpg"}]
     */

    public String msg;
    public int code;
    public List<DataBean> data;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * video_id : 2caf8ab3c3f2600059120a129978d255
         * name : 北壁
         * cover : http://pic7.qiyipic.com/image/20151224/1e/62/v_108736638_m_601_m3_260_360.jpg
         */

        public String video_id;
        public String name;
        public String cover;

        public String getVideo_id() {
            return video_id;
        }

        public void setVideo_id(String video_id) {
            this.video_id = video_id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getCover() {
            return cover;
        }

        public void setCover(String cover) {
            this.cover = cover;
        }
    }
//    public int code;
//    public String message;
//    public List<DataBean> data;
//    public static class DataBean {
//        public String title;
//        public String image;
//        public String id;
//        public String description;
//        public String actors;
//        public String area;
//        public String director;
//        public String kind;
//        public String category;
//        public String year;
//        public List<SourceBean> source;
//        public static class SourceBean {
//            public String id;
//            public String from;
//            public String icon;
//            public String df_icon;
//            public String name;
//            public IntentBean intent;
//            public static class IntentBean {
//                public String id;
//                public String source;
//                public String ap;
//                public String p;
//                public String n;
//                public String v;
//                public String o;
//
//                public List<IsBean> iss;
//                public static class IsBean {
//                    public String id;
//                    public String video_id;
//                    public String intent_id;
//                    public String m;
//                    public String i;
//                }
//            }
//        }
//    }


}
