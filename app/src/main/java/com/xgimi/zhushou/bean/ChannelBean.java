package com.xgimi.zhushou.bean;

/**
 * Created by 霍长江 on 2016/8/31.
 */
public class ChannelBean {
    public String id;
    public String year;
    public String area;
    public String kind;



    public ChannelBean() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    @Override
    public String toString() {
        return "ChannelBean{" +
                "id='" + id + '\'' +
                ", year='" + year + '\'' +
                ", area='" + area + '\'' +
                ", kin='" + kind + '\'' +
                '}';
    }
}
