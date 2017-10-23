package com.xgimi.zhushou.bean;

import java.util.List;

/**
 * Created by Administrator on 2016/12/12.
 */
public class MyYinShiBeen {
    /**
     * data : [{"id":"5315992","title":"我的个神啊","text":"新片上线了，我的个神啊","type":"21","t_id":"59596","url":"","device_token":"","push_time":"1479957300","img":"/recommend/assistanttips/58365b10983e4.png","img_md5":"bde8eb59d00e06ecf620d2a96c92a151"}]
     * code : 200
     * message : ok
     */

    public int code;
    public String message;
    public List<DataBean> data;

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

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * id : 5315992
         * title : 我的个神啊
         * text : 新片上线了，我的个神啊
         * type : 21
         * t_id : 59596
         * url : 
         * device_token : 
         * push_time : 1479957300
         * img : /recommend/assistanttips/58365b10983e4.png
         * img_md5 : bde8eb59d00e06ecf620d2a96c92a151
         */

        public String id;
        public String title;
        public String text;
        public String type;
        public String t_id;
        public String url;
        public String device_token;
        public String push_time;
        public String img;
        public String img_md5;

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

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getT_id() {
            return t_id;
        }

        public void setT_id(String t_id) {
            this.t_id = t_id;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getDevice_token() {
            return device_token;
        }

        public void setDevice_token(String device_token) {
            this.device_token = device_token;
        }

        public String getPush_time() {
            return push_time;
        }

        public void setPush_time(String push_time) {
            this.push_time = push_time;
        }

        public String getImg() {
            return img;
        }

        public void setImg(String img) {
            this.img = img;
        }

        public String getImg_md5() {
            return img_md5;
        }

        public void setImg_md5(String img_md5) {
            this.img_md5 = img_md5;
        }
    }
}
