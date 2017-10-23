package com.xgimi.device.device;

import java.util.List;

/**
 * Created by Administrator on 2016/8/18.
 */
public class FeedbackInfo {

    /**
     *
     */
    public int action;
    public String msgid;
    public DeviceInfo deviceInfo;
    public DownloadInfo downloadInfo;
    public InstallInfo installInfo;
    public PlayInfo playInfo;
    public ExtraInfo extraInfo;
    public String imagePath;
    public Intelligence deviceSetting;
    public List<fileLists> filespath;

public static class fileLists{
    public static final int TYPE_CHECKED = 1;
    public static final int TYPE_NOCHECKED = 0;
    public int type;
    public String Filepath;
    public String Type;
    public String Imagepath;
    public String time;
    public fileLists(String Filepath,String Type,String Imagepath,String time){
        this.Filepath=Filepath;
        this.Type=Type;
        this.Imagepath=Imagepath;
        this.time=time;
    }
    public void setMyType(int type){
        this.type=type;
    }
    public String getFilepath(){
        return Filepath;
    }
    public String getType(){
        return Type;
    }
    public String getImagepath(){
        return Imagepath;
    }
    public String getTime(){
        return time;
    }
}


    public static class Intelligence{
        public String autoscreen;
    }

    public static class DeviceInfo{
        public String devicetype;
        public String ipaddress;
        public int versioncode;
        public String deviceip;
        public String devicename;
        public String device;
        public String mac;


        public DeviceInfo() {

        }
        public DeviceInfo(String devicetype, String ipaddress,int versioncode) {
            this.devicetype = devicetype;
            this.ipaddress = ipaddress;
            this.versioncode = versioncode;
        }
    }

    public static class DownloadInfo{
        public int progress;
        public String filename;
        public String savepath;

        public DownloadInfo(int progress, String filename, String savepath) {
            this.progress = progress;
            this.filename = filename;
            this.savepath = savepath;
        }
    }

    public static class InstallInfo{
        /**
         * 0 失败 ；1 成功；
         */
        public int stat;
        public String packagename;

        public InstallInfo(int stat) {
            this.stat = stat;
        }
    }

    public static class PlayInfo{
        /**
         * 资源来源
         */
        public String resourcetype;
        /**
         * 0 视频 ，1音乐，2图片，3文件
         */
        public int mediatype;
        public String playingname;
        public long currenttime;
        public long totaltime;

        public PlayInfo(String resourcetype, int mediatype, String playingname, long currenttime, long totaltime) {
            this.resourcetype = resourcetype;
            this.mediatype = mediatype;
            this.playingname = playingname;
            this.currenttime = currenttime;
            this.totaltime = totaltime;
        }
    }

    public static class ExtraInfo{
        public String message;

        public ExtraInfo(String message) {
            this.message = message;
        }
    }
}
