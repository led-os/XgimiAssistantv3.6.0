package com.xgimi.zhushou.bean;

/**
 * Created by 霍长江 on 2016/11/16.
 */
public class UserBean {
    public static final int TYPE_CHECKED = 1;
    public static final int TYPE_NOCHECKED = 0;
    public int type=0;
    public long id;
    public String url;

    public UserBean(String url, long id) {
        this.id = id;
        this.url = url;
    }
}
