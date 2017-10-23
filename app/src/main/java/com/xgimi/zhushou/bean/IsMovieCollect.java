package com.xgimi.zhushou.bean;

/**
 * Created by Administrator on 2016/8/26.
 */
public class IsMovieCollect {
    /**
     * data == no 未收藏
     * data == had 收藏
     */
    public String data;
    public int code;
    public String message;

    @Override
    public String toString() {
        return "IsMovieCollect{" +
                "data='" + data + '\'' +
                ", code=" + code +
                ", message='" + message + '\'' +
                '}';
    }
}
