package com.xgimi.zhushou.bean;

import java.util.List;

/**
 * Created by 霍长江 on 2016/8/19.
 */
public class UrlBean {

    public DataBean data;
 

    public int code;
    public String message;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public static class DataBean {
        /**
         * id : 1
         * live_id : 11
         * channel_id : cctv1
         * name : CCTV-1综合
         * english_name : CCTV-1 synthesis
         * icon : http://download.dianshijia.cn/tvlive/data/channel_logos/v3/CCTV1.png
         * url : http://gslb.live.video123456.com/gslb?stream_id=cctv1HD_1300&tag=live&ext=m3u8&sign=live_tv
         * chn_type : 1
         * gm_intent : {"id":"6","n":"电视家","u":"http://yaokong.wukongtv.com/appstore/yaokong.php?p=dsj","p":"com.elinkway.tvlive2","gm_is":{"i":"#Intent;action=com.elinkway.tvlive2.playUrl;launchFlags=0x10000000;component=com.elinkway.tvlive2/.activity.ThirdLauncherSplashActivity;S.data_source=http%3A%2F%2F7xo8iy.com2.z0.glb.qiniucdn.com%2F3rd.json;S.from=com.xgimi.vcontrol;S.channel_url=http%3A%2F%2Fgslb.live.video123456.com%2Fgslb%3Fstream_id%3Dcctv1HD_1300%26tag%3Dlive%26ext%3Dm3u8%26sign%3Dlive_tv;end","m":"3"}}
         */

        public List<RecommendBean> recommend;
        /**
         * name : 美女直播
         * desc : 一大波美女来袭
         * img : /recommend/subject/567226c33993a.jpg
         * type : douyu
         * url : http://m.douyu.com/roomlists/yz
         */

        public List<GameLiveBean> game_live;

        public List<RecommendBean> getRecommend() {
            return recommend;
        }

        public void setRecommend(List<RecommendBean> recommend) {
            this.recommend = recommend;
        }

        public List<GameLiveBean> getGame_live() {
            return game_live;
        }

        public void setGame_live(List<GameLiveBean> game_live) {
            this.game_live = game_live;
        }

        public static class RecommendBean {
            public String id;
            public String live_id;
            public String channel_id;
            public String name;
            public String english_name;
            public String icon;
            public String url;
            public String chn_type;
            /**
             * id : 6
             * n : 电视家
             * u : http://yaokong.wukongtv.com/appstore/yaokong.php?p=dsj
             * p : com.elinkway.tvlive2
             * gm_is : {"i":"#Intent;action=com.elinkway.tvlive2.playUrl;launchFlags=0x10000000;component=com.elinkway.tvlive2/.activity.ThirdLauncherSplashActivity;S.data_source=http%3A%2F%2F7xo8iy.com2.z0.glb.qiniucdn.com%2F3rd.json;S.from=com.xgimi.vcontrol;S.channel_url=http%3A%2F%2Fgslb.live.video123456.com%2Fgslb%3Fstream_id%3Dcctv1HD_1300%26tag%3Dlive%26ext%3Dm3u8%26sign%3Dlive_tv;end","m":"3"}
             */

            public GmIntentBean gm_intent;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getLive_id() {
                return live_id;
            }

            public void setLive_id(String live_id) {
                this.live_id = live_id;
            }

            public String getChannel_id() {
                return channel_id;
            }

            public void setChannel_id(String channel_id) {
                this.channel_id = channel_id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getEnglish_name() {
                return english_name;
            }

            public void setEnglish_name(String english_name) {
                this.english_name = english_name;
            }

            public String getIcon() {
                return icon;
            }

            public void setIcon(String icon) {
                this.icon = icon;
            }

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }

            public String getChn_type() {
                return chn_type;
            }

            public void setChn_type(String chn_type) {
                this.chn_type = chn_type;
            }

            public GmIntentBean getGm_intent() {
                return gm_intent;
            }

            public void setGm_intent(GmIntentBean gm_intent) {
                this.gm_intent = gm_intent;
            }

            public static class GmIntentBean {
                public String id;
                public String n;
                public String u;
                public String p;
                /**
                 * i : #Intent;action=com.elinkway.tvlive2.playUrl;launchFlags=0x10000000;component=com.elinkway.tvlive2/.activity.ThirdLauncherSplashActivity;S.data_source=http%3A%2F%2F7xo8iy.com2.z0.glb.qiniucdn.com%2F3rd.json;S.from=com.xgimi.vcontrol;S.channel_url=http%3A%2F%2Fgslb.live.video123456.com%2Fgslb%3Fstream_id%3Dcctv1HD_1300%26tag%3Dlive%26ext%3Dm3u8%26sign%3Dlive_tv;end
                 * m : 3
                 */

                public GmIsBean gm_is;

                public String getId() {
                    return id;
                }

                public void setId(String id) {
                    this.id = id;
                }

                public String getN() {
                    return n;
                }

                public void setN(String n) {
                    this.n = n;
                }

                public String getU() {
                    return u;
                }

                public void setU(String u) {
                    this.u = u;
                }

                public String getP() {
                    return p;
                }

                public void setP(String p) {
                    this.p = p;
                }

                public GmIsBean getGm_is() {
                    return gm_is;
                }

                public void setGm_is(GmIsBean gm_is) {
                    this.gm_is = gm_is;
                }

                public static class GmIsBean {
                    public String i;
                    public String m;

                    public String getI() {
                        return i;
                    }

                    public void setI(String i) {
                        this.i = i;
                    }

                    public String getM() {
                        return m;
                    }

                    public void setM(String m) {
                        this.m = m;
                    }
                }
            }
        }

        public static class GameLiveBean {
            public String name;
            public String desc;
            public String img;
            public String type;
            public String url;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getDesc() {
                return desc;
            }

            public void setDesc(String desc) {
                this.desc = desc;
            }

            public String getImg() {
                return img;
            }

            public void setImg(String img) {
                this.img = img;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }
        }
    }
}
