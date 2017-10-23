package com.xgimi.zhushou.bean;

import java.util.List;

/**
 * Created by 霍长江 on 2016/8/24.
 */
public class XgimiBean {



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
        public String cate_name;
        public String link_title;
        public String link_address;
        public String cate_icon;

        public String getCate_name() {
            return cate_name;
        }

        public void setCate_name(String cate_name) {
            this.cate_name = cate_name;
        }

        public String getLink_title() {
            return link_title;
        }

        public void setLink_title(String link_title) {
            this.link_title = link_title;
        }

        public String getLink_address() {
            return link_address;
        }

        public void setLink_address(String link_address) {
            this.link_address = link_address;
        }
    }
}
