package com.xgimi.zhushou.bean;

import android.graphics.Bitmap;

/**
 * Created by Administrator on 2016/12/5.
 */
public class VideoInfo {
    public static final int TYPE_CHECKED = 1;
    public static final int TYPE_NOCHECKED = 0;
    public int type;
    public Long id;
    public String title;
    public String filePath;
    public Long size;
    public Long duration;
    public String time;
    public Bitmap tempThumb;
    private boolean isUpload;
    public VideoInfo() {
        isUpload = false;
    }
    public VideoInfo(String time){
        this.time=time;
    }
    public void setDuration(Long duration){
        this.duration=duration;
    }
    public Long getDuration(){
        return duration;
    }
    public void setTime(String time){
        this.time=time;
    }
    public String getTime(){
        return time;
    }
    public VideoInfo(Long id,String filePath){
        super();
        this.id=id;
        this.filePath=filePath;
        isUpload = false;
    }
    public String getTitle(){
        return title;
    }
    public void setTitle(String title){
        this.title=title;
    }
    public void setType(int type){
        this.type=type;
    }
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
    public boolean isUpload() {
        return isUpload;
    }

    public void setUpload(boolean isUpload) {
        this.isUpload = isUpload;
    }
    public void setTempThumb(Bitmap tempThumb){
     this.tempThumb=tempThumb;
    }
}
