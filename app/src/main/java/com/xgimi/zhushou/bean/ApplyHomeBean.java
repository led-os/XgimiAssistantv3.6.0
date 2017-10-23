package com.xgimi.zhushou.bean;

import java.util.List;

/**
 * Created by 霍长江 on 2016/8/16.
 */
public class ApplyHomeBean {

    public DataBean data;
    /**
     * data : {"ad":[{"id":"555d479cf4d8c25562f1e3a6","title":"世纪佳缘","image":"/recommend/appad/56629fd4414fd.jpg","image_md5":"d45972766900cff9e8a91513204e171b"},{"id":"51c2c591496cec250345f73b","title":"百度地图","image":"/recommend/appad/566fbd05d4c2b.jpg","image_md5":"8a707e41de0a20479ba78bc6f97cb0f5"},{"id":"51b1716c27ff96d6380ca4a8","title":"WPS office","image":"/recommend/appad/566fbcae609d2.png","image_md5":"306389bf14adc8b161a5e0a85a7a2dcf"}],"subject":[{"id":"1","title":"儿童节快乐","description":"儿童节，应用特别推荐","content":[{"id":"3","subject_id":"1","name":"超级坦克大战TV版","icon":"http://img.sfcdn.org/1304f05b5ebcd68f64966b364028b3ea321e9991.png!medium.icon","app_id":"559cc411726e09b53a0c606a"},{"id":"4","subject_id":"1","name":"勇士神剑","icon":"http://img.sfcdn.org/42ff09cb7a59744bab00c1542b8decc5a5d0331d.png!medium.icon","app_id":"559a5e74995b56bb5da3e7d7"},{"id":"5","subject_id":"1","name":"超级坦克大战TV版","icon":"http://img.sfcdn.org/1304f05b5ebcd68f64966b364028b3ea321e9991.png!medium.icon","app_id":"559cc411726e09b53a0c606a"},{"id":"6","subject_id":"1","name":"超级坦克大战TV版","icon":"http://img.sfcdn.org/1304f05b5ebcd68f64966b364028b3ea321e9991.png!medium.icon","app_id":"559cc411726e09b53a0c606a"}]}],"apps":[{"id":"553870eb726e09033ebd6023","title":"爱奇艺TV版","icon":"http://img.sfcdn.org/92155e36215c68bbf81458aa4f59851f0755ef10.png!medium.icon","is_game":"0"},{"id":"534b7a7b3bf55dfe7f00027f","title":"电视家2.0","icon":"http://img.sfcdn.org/38e18dfa2ee7f1c322cbc744fbf9d7ae7959a019.png!medium.icon","is_game":"0"},{"id":"529de2673bf55d5b760002f8","title":"电视猫视频","icon":"http://img.sfcdn.org/11c9b1455028e22be194564045cee79a9f4f128c.png!medium.icon","is_game":"0"},{"id":"5524f6b979b3bf4130b8c7c9","title":"儿童故事视频","icon":"http://img.sfcdn.org/214e7cc85500679d9bc60b48c69790b2a8a24587.png!medium.icon","is_game":"0"},{"id":"54aa40ff726e09a651b0432c","title":"优酷TV版","icon":"http://img.sfcdn.org/7b7dab5bc75fb738dc5619d153ad81ca4d2ff1b6.png!medium.icon","is_game":"0"},{"id":"52709dad3bf55d6c71000424","title":"球迷TV","icon":"http://img.sfcdn.org/e51ce692399adaa223660759376bca5419487dcc.png!medium.icon","is_game":"0"},{"id":"51b31c6127ff96e136e533b4","title":"腾讯视频HD","icon":"http://img.sfcdn.org/a0a8cff22e323f69456fadd48f351914ecae9299.png!medium.icon","is_game":"0"}],"games":[{"id":"5562e1e4b2be362d77d1cfd8","title":"拳皇97高清TV版","icon":"http://img.sfcdn.org/8b71fe334229afb886fd3bdbafbee3edbe7dcd37.png!medium.icon","is_game":"1"},{"id":"53ccba39726e0926433e5890","title":"开心酷跑","icon":"http://img.sfcdn.org/4fcc76108e4ebc3df8f8d258135d859ed9b69117.png!medium.icon","is_game":"1"},{"id":"550fda0576437c9158492b84","title":"雷电复刻版","icon":"http://img.sfcdn.org/83743eb9810809fa8d2cfd0f3f7fa06ca1822780.jpg!medium.icon","is_game":"1"},{"id":"534b94be3bf55de63f000071","title":"合金弹头X","icon":"http://img.sfcdn.org/6031297e4df30e1476de922743803ce673e249c6.png!medium.icon","is_game":"1"},{"id":"541be26296eb120b11b8acff","title":"我和巴蒂","icon":"http://img.sfcdn.org/bdc56e4a164b1dbdddb2c2093427f14083fa71c7.jpg!medium.icon","is_game":"1"},{"id":"52df93ea9c5251b2170000e0","title":"恐龙快打","icon":"http://img.sfcdn.org/9069a54dad38fe14ad0c2969b7b8ce14ec87ef58.png!medium.icon","is_game":"1"},{"id":"52d3c6a59c5251932700011d","title":"坦克大战","icon":"http://img.sfcdn.org/63403bc12c57354f7fcf30e45f7183ec667d895e.png!medium.icon","is_game":"1"}]}
     * code : 200
     * message : ok
     */

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
         * id : 555d479cf4d8c25562f1e3a6
         * title : 世纪佳缘
         * image : /recommend/appad/56629fd4414fd.jpg
         * image_md5 : d45972766900cff9e8a91513204e171b
         */

        public List<AdBean> ad;
        /**
         * id : 1
         * title : 儿童节快乐
         * description : 儿童节，应用特别推荐
         * content : [{"id":"3","subject_id":"1","name":"超级坦克大战TV版","icon":"http://img.sfcdn.org/1304f05b5ebcd68f64966b364028b3ea321e9991.png!medium.icon","app_id":"559cc411726e09b53a0c606a"},{"id":"4","subject_id":"1","name":"勇士神剑","icon":"http://img.sfcdn.org/42ff09cb7a59744bab00c1542b8decc5a5d0331d.png!medium.icon","app_id":"559a5e74995b56bb5da3e7d7"},{"id":"5","subject_id":"1","name":"超级坦克大战TV版","icon":"http://img.sfcdn.org/1304f05b5ebcd68f64966b364028b3ea321e9991.png!medium.icon","app_id":"559cc411726e09b53a0c606a"},{"id":"6","subject_id":"1","name":"超级坦克大战TV版","icon":"http://img.sfcdn.org/1304f05b5ebcd68f64966b364028b3ea321e9991.png!medium.icon","app_id":"559cc411726e09b53a0c606a"}]
         */

        public List<SubjectBean> subject;
        /**
         * id : 553870eb726e09033ebd6023
         * title : 爱奇艺TV版
         * icon : http://img.sfcdn.org/92155e36215c68bbf81458aa4f59851f0755ef10.png!medium.icon
         * is_game : 0
         */

        public List<AppsBean> apps;
        /**
         * id : 5562e1e4b2be362d77d1cfd8
         * title : 拳皇97高清TV版
         * icon : http://img.sfcdn.org/8b71fe334229afb886fd3bdbafbee3edbe7dcd37.png!medium.icon
         * is_game : 1
         */

        public List<GamesBean> games;

        public List<AdBean> getAd() {
            return ad;
        }

        public void setAd(List<AdBean> ad) {
            this.ad = ad;
        }

        public List<SubjectBean> getSubject() {
            return subject;
        }

        public void setSubject(List<SubjectBean> subject) {
            this.subject = subject;
        }

        public List<AppsBean> getApps() {
            return apps;
        }

        public void setApps(List<AppsBean> apps) {
            this.apps = apps;
        }

        public List<GamesBean> getGames() {
            return games;
        }

        public void setGames(List<GamesBean> games) {
            this.games = games;
        }

        public static class AdBean {
            public String id;
            public String title;
            public String image;
            public String image_md5;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getImage() {
                return image;
            }

            public void setImage(String image) {
                this.image = image;
            }

            public String getImage_md5() {
                return image_md5;
            }

            public void setImage_md5(String image_md5) {
                this.image_md5 = image_md5;
            }
        }

        public static class SubjectBean {
            public String id;
            public String title;
            public String description;
            /**
             * id : 3
             * subject_id : 1
             * name : 超级坦克大战TV版
             * icon : http://img.sfcdn.org/1304f05b5ebcd68f64966b364028b3ea321e9991.png!medium.icon
             * app_id : 559cc411726e09b53a0c606a
             */

            public List<ContentBean> content;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getDescription() {
                return description;
            }

            public void setDescription(String description) {
                this.description = description;
            }

            public List<ContentBean> getContent() {
                return content;
            }

            public void setContent(List<ContentBean> content) {
                this.content = content;
            }

            public static class ContentBean {
                public String id;
                public String subject_id;
                public String name;
                public String icon;
                public String app_id;
                public String pacagename;

                public String getId() {
                    return id;
                }
                public String getPacagename(){
                    return pacagename;
                }
                public void setPacagename(String pacagename){
                    this.pacagename=pacagename;
                }

                public void setId(String id) {
                    this.id = id;
                }

                public String getSubject_id() {
                    return subject_id;
                }

                public void setSubject_id(String subject_id) {
                    this.subject_id = subject_id;
                }

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }

                public String getIcon() {
                    return icon;
                }

                public void setIcon(String icon) {
                    this.icon = icon;
                }

                public String getApp_id() {
                    return app_id;
                }

                public void setApp_id(String app_id) {
                    this.app_id = app_id;
                }
            }
        }

        public static class AppsBean {
            public String id;
            public String title;
            public String icon;
            public String is_game;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getIcon() {
                return icon;
            }

            public void setIcon(String icon) {
                this.icon = icon;
            }

            public String getIs_game() {
                return is_game;
            }

            public void setIs_game(String is_game) {
                this.is_game = is_game;
            }
        }

        public static class GamesBean {
            public String id;
            public String title;
            public String icon;
            public String is_game;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getIcon() {
                return icon;
            }

            public void setIcon(String icon) {
                this.icon = icon;
            }

            public String getIs_game() {
                return is_game;
            }

            public void setIs_game(String is_game) {
                this.is_game = is_game;
            }
        }
    }
}
