package com.xgimi.zhushou.inter;

/**
 * Created by 霍长江 on 2016/8/17.
 */
public interface CommonCallBack<T> {

    void onStart();

    void onSuccess(T data);

    void onFailed(String reason);
}