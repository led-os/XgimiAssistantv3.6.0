package com.xgimi.zhushou.bean;

import android.graphics.drawable.Drawable;

/**
 * Created by Administrator on 2016/12/12.
 */
public class FileInfo {
    public String fileName;
    public String fileSize;
    public String time;
    public Drawable icon;
    public String id;
    public String filepath;
    public long size;
    public String packageName;
    private boolean mIsSelcted;

    public boolean isSelcted() {
        return mIsSelcted;
    }

    public void setSelcted(boolean mIsSelcted) {
        this.mIsSelcted = mIsSelcted;
    }

    public FileInfo() {

    }

    public FileInfo(String fileName, String fileSize, String time, Drawable icon, String id, String filepath, long size, String packageName) {
        this.fileName = fileName;
        this.fileSize = fileSize;
        this.time = time;
        this.icon = icon;
        this.id = id;
        this.filepath = filepath;
        this.size = size;
        this.packageName = packageName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void setFileSize(String fileSize) {
        this.fileSize = fileSize;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
