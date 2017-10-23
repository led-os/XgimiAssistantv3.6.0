package com.xgimi.zhushou.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/1/6.
 */
public class Individuality {

    /**
     * title : 更懂你
     * img : http://img4.imgtn.bdimg.com/it/u=2755422646,699486360&fm=23&gp=0.jpg
     * data : [{"title":"废柴也烦恼","image":"http://i.gtimg.cn/qqlive/img/jpgcache/files/qqvideo/8/8jvgq5qadcfor86.jpg","id":"722739"},{"title":"废柴也烦恼","image":"http://i.gtimg.cn/qqlive/img/jpgcache/files/qqvideo/8/8jvgq5qadcfor86.jpg","id":"722739"},{"title":"废柴也烦恼","image":"http://i.gtimg.cn/qqlive/img/jpgcache/files/qqvideo/8/8jvgq5qadcfor86.jpg","id":"722739"}]
     */

    public DataBean data;
    /**
     * data : {"title":"更懂你","img":"http://img4.imgtn.bdimg.com/it/u=2755422646,699486360&fm=23&gp=0.jpg","data":[{"title":"废柴也烦恼","image":"http://i.gtimg.cn/qqlive/img/jpgcache/files/qqvideo/8/8jvgq5qadcfor86.jpg","id":"722739"},{"title":"废柴也烦恼","image":"http://i.gtimg.cn/qqlive/img/jpgcache/files/qqvideo/8/8jvgq5qadcfor86.jpg","id":"722739"},{"title":"废柴也烦恼","image":"http://i.gtimg.cn/qqlive/img/jpgcache/files/qqvideo/8/8jvgq5qadcfor86.jpg","id":"722739"}]}
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
        public String title;
        public String img;
        /**
         * title : 废柴也烦恼
         * image : http://i.gtimg.cn/qqlive/img/jpgcache/files/qqvideo/8/8jvgq5qadcfor86.jpg
         * id : 722739
         */

        public List<DataBean1> data;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getImg() {
            return img;
        }

        public void setImg(String img) {
            this.img = img;
        }

        public List<DataBean1> getData() {
            return data;
        }

        public void setData(List<DataBean1> data) {
            this.data = data;
        }

        public static class DataBean1 {
            public String title;
            public String image;
            public String id;

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

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }
        }
    }
}
