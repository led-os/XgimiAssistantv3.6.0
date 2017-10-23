package com.xgimi.zhushou.bean;

import java.util.List;

/**
 * Created by Administrator on 2016/12/13.
 */
public class NewMvList {
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

        public int area_id;
        public String area_name;
        public String order;
        public List<MVList.data> info;

        public int getArea_id() {
            return area_id;
        }

        public void setArea_id(int area_id) {
            this.area_id = area_id;
        }

        public String getArea_name() {
            return area_name;
        }

        public void setArea_name(String area_name) {
            this.area_name = area_name;
        }

        public String getOrder() {
            return order;
        }

        public void setOrder(String order) {
            this.order = order;
        }

        public List<MVList.data> getInfo() {
            return info;
        }

        public void setInfo(List<MVList.data> info) {
            this.info = info;
        }

        public static class InfoBean {
            /**
             * mv_id : 27448
             * mv_title : 孤芳不自赏 电视剧《孤芳不自赏》主题曲
             * mv_artist : 霍尊
             * mv_thumb : /MV/2016/201612/12/s3huigjk78xc9.jpg
             * mv_play_address : http://he.yinyuetai.com/uploads/videos/common/D9670158F0E08362ED40CDF6A68AF4D4.mp4?sc=83031fa844e90174&br=3140&vid=2748645&aid=25716&area=ML&vst=0
             * collect_id : 0
             */

            public String mv_id;
            public String mv_title;
            public String mv_artist;
            public String mv_thumb;
            public String mv_play_address;
            public String collect_id;
            public String mv_type_name;

            public String getMv_id() {
                return mv_id;
            }

            public void setMv_id(String mv_id) {
                this.mv_id = mv_id;
            }

            public String getMv_title() {
                return mv_title;
            }

            public void setMv_title(String mv_title) {
                this.mv_title = mv_title;
            }

            public String getMv_artist() {
                return mv_artist;
            }

            public void setMv_artist(String mv_artist) {
                this.mv_artist = mv_artist;
            }

            public String getMv_thumb() {
                return mv_thumb;
            }

            public void setMv_thumb(String mv_thumb) {
                this.mv_thumb = mv_thumb;
            }

            public String getMv_play_address() {
                return mv_play_address;
            }

            public void setMv_play_address(String mv_play_address) {
                this.mv_play_address = mv_play_address;
            }

            public String getCollect_id() {
                return collect_id;
            }

            public void setCollect_id(String collect_id) {
                this.collect_id = collect_id;
            }
        }
    }
}
