package com.xgimi.zhushou.bean;

import java.util.List;

/**
 * Created by 霍长江 on 2016/8/10.
 */
public class MovieByCategory {
    /**
     * msg : success
     * code : 200
     * data : {"type":[{"id":1,"category_id":1,"name":"爱情"},{"id":2,"category_id":1,"name":"喜剧"},{"id":3,"category_id":1,"name":"动作"},{"id":4,"category_id":1,"name":"科幻"},{"id":5,"category_id":1,"name":"动画"},{"id":6,"category_id":1,"name":"恐怖"},{"id":7,"category_id":1,"name":"犯罪"},{"id":8,"category_id":1,"name":"惊悚"},{"id":9,"category_id":1,"name":"悬疑"},{"id":10,"category_id":1,"name":"其他"}],"year":[{"name":2017},{"name":"2016-2011"},{"name":"2010-2000"},{"name":"90年代"},{"name":"80年代"}],"area":[{"category_id":1,"name":"华语"},{"category_id":1,"name":"美国"},{"category_id":1,"name":"欧洲"},{"category_id":1,"name":"韩国"},{"category_id":1,"name":"日本"},{"category_id":1,"name":"泰国"},{"category_id":1,"name":"其他"}]}
     */

    public String msg;
    public int code;
    public DataBean data;

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

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        public List<TypeBean> type;
        public List<YearBean> year;
        public List<AreaBean> area;

        public List<TypeBean> getType() {
            return type;
        }

        public void setType(List<TypeBean> type) {
            this.type = type;
        }

        public List<YearBean> getYear() {
            return year;
        }

        public void setYear(List<YearBean> year) {
            this.year = year;
        }

        public List<AreaBean> getArea() {
            return area;
        }

        public void setArea(List<AreaBean> area) {
            this.area = area;
        }

        public static class TypeBean {
            /**
             * id : 1
             * category_id : 1
             * name : 爱情
             */

            public int id;
            public int category_id;
            public String name;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public int getCategory_id() {
                return category_id;
            }

            public void setCategory_id(int category_id) {
                this.category_id = category_id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }
        }

        public static class YearBean {
            /**
             * name : 2017
             */

            public String name;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }
        }

        public static class AreaBean {
            /**
             * category_id : 1
             * name : 华语
             */

            public int category_id;
            public String name;

            public int getCategory_id() {
                return category_id;
            }

            public void setCategory_id(int category_id) {
                this.category_id = category_id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }
        }
    }


//    public int code;
//    public String message;
//    public List<DataBean> data;
//    public static class DataBean {
//        public String id;
//        public String name;
//    }


}
