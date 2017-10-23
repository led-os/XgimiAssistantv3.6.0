package com.xgimi.zhushou.bean;

import java.util.List;

/**
 * Created by Administrator on 2016/10/19.
 */
public class SearchMovieResult {
    /**
     * msg : success
     * code : 200
     * data : [{"type":"电影","list":[{"video_id":"8cac0847718d7e771c17cb9a7726f7fd","name":"铁甲钢拳","image":"http://pic8.qiyipic.com/image/20150512/a1/c7/17/v_50163097_m_601_m3_260_360.jpg","actors":"休·杰克曼,达科塔·高尤,伊万杰琳·莉莉,安东尼·麦凯,凯文·杜兰","area":"美国","category":"电影","type":"电影","year":"2011年10月","source":[{"from":"iqiyi","icon":"http://img.wukongtv.com/app/icon/video/lizhi.png","df_icon":"http://img.wukongtv.com/app/icon/video/tr_lizhi.png","gm_intent":{"id":2,"source":"iqiyi","ap":"","p":"com.gitvjimi.video","n":"爱奇艺TV版","o":"1","u":"http://apk.xgimi.com/zhushou/gitvjimi20161028.apk","gm_is":{"m":1,"i":"#Intent;S.type=VIDEO;S.albumId=106309700;S.videoId=106309700;S.sourceCode=0;i.series=0;i.channelId=1;end","episode":"1"}}},{"from":"mangguo","icon":"http://img.wukongtv.com/app/icon/video/mango.png","df_icon":"http://img.wukongtv.com/app/icon/video/tr_mango.png","gm_intent":{"id":1,"source":"mangguo","ap":"","p":"com.hunantv.market","n":"芒果TV","o":"1","u":"http://7jptw2.com2.z0.glb.qiniucdn.com/mangguotv-gimi.apk","gm_is":{"m":1,"i":"#Intent;action=com.starcor.hunan.hmd.record;S.cmdex=show_video_detail;B.flag_action_from_matv=true;S.video_id=08694055ef9076bb2df63222f3c9a2f9;end","episode":"1"}}},{"from":"qq","icon":"http://img.wukongtv.com/app/icon/video/qq.png","df_icon":"http://img.wukongtv.com/app/icon/video/tr_qq.png","gm_intent":{"id":20,"source":"qq","ap":"","p":"com.ktcp.tvvideo","n":"腾讯视频TV版","o":"1","u":"http://apk.xgimi.com/tv_video_2.2.1.2052.1501_SNM_NP_SNMBOX_16008.apk","gm_is":{"m":2,"i":"#Intent;action=com.ktcp.video;launchFlags=0x10000000;S.videoId=0oa7e8y22juk60d;end","episode":"1"}}},{"from":"qq","icon":"http://img.wukongtv.com/app/icon/video/qq.png","df_icon":"http://img.wukongtv.com/app/icon/video/tr_qq.png","gm_intent":{"id":20,"source":"qq","ap":"","p":"com.ktcp.tvvideo","n":"腾讯视频TV版","o":"1","u":"http://apk.xgimi.com/tv_video_2.2.1.2052.1501_SNM_NP_SNMBOX_16008.apk","gm_is":{"m":2,"i":"cykew://detail?showid=ef5be084018b11df97c0#Intent;action=android.intent.action.VIEW;end","episode":"1"}}}]}]},{"type":"电视剧","list":[]},{"type":"综艺","list":[]},{"type":"动漫","list":[]},{"type":"纪录片","list":[]}]
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
         * type : 电影
         * list : [{"video_id":"8cac0847718d7e771c17cb9a7726f7fd","name":"铁甲钢拳","image":"http://pic8.qiyipic.com/image/20150512/a1/c7/17/v_50163097_m_601_m3_260_360.jpg","actors":"休·杰克曼,达科塔·高尤,伊万杰琳·莉莉,安东尼·麦凯,凯文·杜兰","area":"美国","category":"电影","type":"电影","year":"2011年10月","source":[{"from":"iqiyi","icon":"http://img.wukongtv.com/app/icon/video/lizhi.png","df_icon":"http://img.wukongtv.com/app/icon/video/tr_lizhi.png","gm_intent":{"id":2,"source":"iqiyi","ap":"","p":"com.gitvjimi.video","n":"爱奇艺TV版","o":"1","u":"http://apk.xgimi.com/zhushou/gitvjimi20161028.apk","gm_is":{"m":1,"i":"#Intent;S.type=VIDEO;S.albumId=106309700;S.videoId=106309700;S.sourceCode=0;i.series=0;i.channelId=1;end","episode":"1"}}},{"from":"mangguo","icon":"http://img.wukongtv.com/app/icon/video/mango.png","df_icon":"http://img.wukongtv.com/app/icon/video/tr_mango.png","gm_intent":{"id":1,"source":"mangguo","ap":"","p":"com.hunantv.market","n":"芒果TV","o":"1","u":"http://7jptw2.com2.z0.glb.qiniucdn.com/mangguotv-gimi.apk","gm_is":{"m":1,"i":"#Intent;action=com.starcor.hunan.hmd.record;S.cmdex=show_video_detail;B.flag_action_from_matv=true;S.video_id=08694055ef9076bb2df63222f3c9a2f9;end","episode":"1"}}},{"from":"qq","icon":"http://img.wukongtv.com/app/icon/video/qq.png","df_icon":"http://img.wukongtv.com/app/icon/video/tr_qq.png","gm_intent":{"id":20,"source":"qq","ap":"","p":"com.ktcp.tvvideo","n":"腾讯视频TV版","o":"1","u":"http://apk.xgimi.com/tv_video_2.2.1.2052.1501_SNM_NP_SNMBOX_16008.apk","gm_is":{"m":2,"i":"#Intent;action=com.ktcp.video;launchFlags=0x10000000;S.videoId=0oa7e8y22juk60d;end","episode":"1"}}},{"from":"qq","icon":"http://img.wukongtv.com/app/icon/video/qq.png","df_icon":"http://img.wukongtv.com/app/icon/video/tr_qq.png","gm_intent":{"id":20,"source":"qq","ap":"","p":"com.ktcp.tvvideo","n":"腾讯视频TV版","o":"1","u":"http://apk.xgimi.com/tv_video_2.2.1.2052.1501_SNM_NP_SNMBOX_16008.apk","gm_is":{"m":2,"i":"cykew://detail?showid=ef5be084018b11df97c0#Intent;action=android.intent.action.VIEW;end","episode":"1"}}}]}]
         */

        public String type;
        public List<ListBean> list;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public List<ListBean> getList() {
            return list;
        }

        public void setList(List<ListBean> list) {
            this.list = list;
        }

        public static class ListBean {
            /**
             * video_id : 8cac0847718d7e771c17cb9a7726f7fd
             * name : 铁甲钢拳
             * image : http://pic8.qiyipic.com/image/20150512/a1/c7/17/v_50163097_m_601_m3_260_360.jpg
             * actors : 休·杰克曼,达科塔·高尤,伊万杰琳·莉莉,安东尼·麦凯,凯文·杜兰
             * area : 美国
             * category : 电影
             * type : 电影
             * year : 2011年10月
             * source : [{"from":"iqiyi","icon":"http://img.wukongtv.com/app/icon/video/lizhi.png","df_icon":"http://img.wukongtv.com/app/icon/video/tr_lizhi.png","gm_intent":{"id":2,"source":"iqiyi","ap":"","p":"com.gitvjimi.video","n":"爱奇艺TV版","o":"1","u":"http://apk.xgimi.com/zhushou/gitvjimi20161028.apk","gm_is":{"m":1,"i":"#Intent;S.type=VIDEO;S.albumId=106309700;S.videoId=106309700;S.sourceCode=0;i.series=0;i.channelId=1;end","episode":"1"}}},{"from":"mangguo","icon":"http://img.wukongtv.com/app/icon/video/mango.png","df_icon":"http://img.wukongtv.com/app/icon/video/tr_mango.png","gm_intent":{"id":1,"source":"mangguo","ap":"","p":"com.hunantv.market","n":"芒果TV","o":"1","u":"http://7jptw2.com2.z0.glb.qiniucdn.com/mangguotv-gimi.apk","gm_is":{"m":1,"i":"#Intent;action=com.starcor.hunan.hmd.record;S.cmdex=show_video_detail;B.flag_action_from_matv=true;S.video_id=08694055ef9076bb2df63222f3c9a2f9;end","episode":"1"}}},{"from":"qq","icon":"http://img.wukongtv.com/app/icon/video/qq.png","df_icon":"http://img.wukongtv.com/app/icon/video/tr_qq.png","gm_intent":{"id":20,"source":"qq","ap":"","p":"com.ktcp.tvvideo","n":"腾讯视频TV版","o":"1","u":"http://apk.xgimi.com/tv_video_2.2.1.2052.1501_SNM_NP_SNMBOX_16008.apk","gm_is":{"m":2,"i":"#Intent;action=com.ktcp.video;launchFlags=0x10000000;S.videoId=0oa7e8y22juk60d;end","episode":"1"}}},{"from":"qq","icon":"http://img.wukongtv.com/app/icon/video/qq.png","df_icon":"http://img.wukongtv.com/app/icon/video/tr_qq.png","gm_intent":{"id":20,"source":"qq","ap":"","p":"com.ktcp.tvvideo","n":"腾讯视频TV版","o":"1","u":"http://apk.xgimi.com/tv_video_2.2.1.2052.1501_SNM_NP_SNMBOX_16008.apk","gm_is":{"m":2,"i":"cykew://detail?showid=ef5be084018b11df97c0#Intent;action=android.intent.action.VIEW;end","episode":"1"}}}]
             */

            public String video_id;
            public String name;
            public String image;
            public String actors;
            public String area;
            public String category;
            public String type;
            public String year;
            public List<SourceBean> source;

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

            public String getImage() {
                return image;
            }

            public void setImage(String image) {
                this.image = image;
            }

            public String getActors() {
                return actors;
            }

            public void setActors(String actors) {
                this.actors = actors;
            }

            public String getArea() {
                return area;
            }

            public void setArea(String area) {
                this.area = area;
            }

            public String getCategory() {
                return category;
            }

            public void setCategory(String category) {
                this.category = category;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public String getYear() {
                return year;
            }

            public void setYear(String year) {
                this.year = year;
            }

            public List<SourceBean> getSource() {
                return source;
            }

            public void setSource(List<SourceBean> source) {
                this.source = source;
            }

            public static class SourceBean {
                /**
                 * from : iqiyi
                 * icon : http://img.wukongtv.com/app/icon/video/lizhi.png
                 * df_icon : http://img.wukongtv.com/app/icon/video/tr_lizhi.png
                 * gm_intent : {"id":2,"source":"iqiyi","ap":"","p":"com.gitvjimi.video","n":"爱奇艺TV版","o":"1","u":"http://apk.xgimi.com/zhushou/gitvjimi20161028.apk","gm_is":{"m":1,"i":"#Intent;S.type=VIDEO;S.albumId=106309700;S.videoId=106309700;S.sourceCode=0;i.series=0;i.channelId=1;end","episode":"1"}}
                 */

                public String from;
                public String icon;
                public String df_icon;
                public GmIntentBean gm_intent;

                public String getFrom() {
                    return from;
                }

                public void setFrom(String from) {
                    this.from = from;
                }

                public String getIcon() {
                    return icon;
                }

                public void setIcon(String icon) {
                    this.icon = icon;
                }

                public String getDf_icon() {
                    return df_icon;
                }

                public void setDf_icon(String df_icon) {
                    this.df_icon = df_icon;
                }

                public GmIntentBean getGm_intent() {
                    return gm_intent;
                }

                public void setGm_intent(GmIntentBean gm_intent) {
                    this.gm_intent = gm_intent;
                }

                public static class GmIntentBean {
                    /**
                     * id : 2
                     * source : iqiyi
                     * ap :
                     * p : com.gitvjimi.video
                     * n : 爱奇艺TV版
                     * o : 1
                     * u : http://apk.xgimi.com/zhushou/gitvjimi20161028.apk
                     * gm_is : {"m":1,"i":"#Intent;S.type=VIDEO;S.albumId=106309700;S.videoId=106309700;S.sourceCode=0;i.series=0;i.channelId=1;end","episode":"1"}
                     */

                    public int id;
                    public String source;
                    public String ap;
                    public String p;
                    public String n;
                    public String o;
                    public String u;
                    public GmIsBean gm_is;

                    public int getId() {
                        return id;
                    }

                    public void setId(int id) {
                        this.id = id;
                    }

                    public String getSource() {
                        return source;
                    }

                    public void setSource(String source) {
                        this.source = source;
                    }

                    public String getAp() {
                        return ap;
                    }

                    public void setAp(String ap) {
                        this.ap = ap;
                    }

                    public String getP() {
                        return p;
                    }

                    public void setP(String p) {
                        this.p = p;
                    }

                    public String getN() {
                        return n;
                    }

                    public void setN(String n) {
                        this.n = n;
                    }

                    public String getO() {
                        return o;
                    }

                    public void setO(String o) {
                        this.o = o;
                    }

                    public String getU() {
                        return u;
                    }

                    public void setU(String u) {
                        this.u = u;
                    }

                    public GmIsBean getGm_is() {
                        return gm_is;
                    }

                    public void setGm_is(GmIsBean gm_is) {
                        this.gm_is = gm_is;
                    }

                    public static class GmIsBean {
                        /**
                         * m : 1
                         * i : #Intent;S.type=VIDEO;S.albumId=106309700;S.videoId=106309700;S.sourceCode=0;i.series=0;i.channelId=1;end
                         * episode : 1
                         */

                        public int m;
                        public String i;
                        public String episode;

                        public int getM() {
                            return m;
                        }

                        public void setM(int m) {
                            this.m = m;
                        }

                        public String getI() {
                            return i;
                        }

                        public void setI(String i) {
                            this.i = i;
                        }

                        public String getEpisode() {
                            return episode;
                        }

                        public void setEpisode(String episode) {
                            this.episode = episode;
                        }
                    }
                }
            }
        }
    }


//    public List<Data> data;
//    public int code;
//    public String message;
//    public static class Data{
//        public String type;
//        public List<list> list;
//        public static class list{
//            public String title;
//            public String image;
//            public String id;
//            public String description;
//            public String actors;
//            public String area;
//            public String director;
//            public String kind;
//            public String category;
//            public String year;
//            public List<source> source;
//            public static class source{
//                public String from;
//                public String icon;
//                public String df_icon;
//                public gm_intent gm_intent;
//                public static class gm_intent{
//                    public String id;
//                    public String source;
//                    public String ap;
//                    public String p;
//                    public String n;
//                    public String v;
//                    public String o;
//                    public String u;
//                    public List<gm_is> gm_is;
//                    public static class gm_is{
//                        public String m;
//                        public String i;
//                        public String episode;
//                    }
//
//                }
//            }
//        }
//    }
}
