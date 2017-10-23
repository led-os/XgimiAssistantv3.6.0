package com.xgimi.gmsdk.bean.send;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/8/9.
 */
public class VcontrolCmd {

    public int action;
    public String msgid;
    public String appid;
    public String packageName;
    public String version;

    public ThirdPlay thirdPlay;
    public CustomPlay customPlay;
    public ControlCmd controlCmd;
    public PhoneInfo phoneInfo;
    public Statistics statistics;
    public PhotoWall photoWall;

    public VcontrolCmd(int action, PhotoWall photoWall) {
        this.action = action;
        this.photoWall = photoWall;
    }

    public VcontrolCmd(int action, String msgid, String appid) {
        this.action = action;
        this.msgid = msgid;
        this.appid = appid;
    }

    //爱奇艺播放那儿的协议
    public VcontrolCmd(int action, String version, String appid, String packageName, String msgid, CustomPlay customPlay) {
        this.version = version;
        this.appid = appid;
        this.packageName = packageName;
        this.msgid = msgid;
        this.customPlay = customPlay;
        this.action = action;
    }

    //没加统计的其它地方的老协议
    public VcontrolCmd(int action, String msgid, String appid, ThirdPlay thirdPlay, CustomPlay customPlay, ControlCmd controlCmd, PhoneInfo phoneInfo, ArrayList<Dependance> params) {
        this.action = action;
        this.msgid = msgid;
        this.appid = appid;
        this.thirdPlay = thirdPlay;
        this.customPlay = customPlay;
        this.controlCmd = controlCmd;
        this.phoneInfo = phoneInfo;
        this.params = params;
    }

    //加入了事件统计的协议
    public VcontrolCmd(int action, String msgid, String appid, String version, String packageName, ThirdPlay thirdPlay, CustomPlay customPlay, ControlCmd controlCmd, PhoneInfo phoneInfo, ArrayList<Dependance> params) {
        this.action = action;
        this.msgid = msgid;
        this.appid = appid;
        this.version = version;
        this.packageName = packageName;
        this.thirdPlay = thirdPlay;
        this.customPlay = customPlay;
        this.controlCmd = controlCmd;
        this.phoneInfo = phoneInfo;
        this.params = params;
    }

    //加入了事件统计的协议
    public VcontrolCmd(int action, String msgid, String appid, String version, String packageName, Statistics statics) {
        this.action = action;
        this.msgid = msgid;
        this.appid = appid;
        this.version = version;
        this.packageName = packageName;
        this.statistics = statics;
    }

    //文件传输那儿拉我已经传输过的协议
    public VcontrolCmd(String appid, String msgid, ControlCmd controlCmd, int action) {
        this.appid = appid;
        this.msgid = msgid;
        this.controlCmd = controlCmd;
        this.action = action;
    }

    public ArrayList<Dependance> params;

    public static class Dependance {
        public String apk_url;
        public String apk_version;
        public String package_name;

        public Dependance(String apk_url, String apk_version, String package_name) {
            this.apk_url = apk_url;
            this.apk_version = apk_version;
            this.package_name = package_name;
        }
    }

    public static class ThirdPlay {
        /**
         * n : 魔力视频
         * o : false
         * is : [{"i":"#Intent;action=android.intent.action.MAIN;category=android.intent.category.LAUNCHER;launchFlags=0x10000000;component=com.molitv.android/.activity.LauncherActivity;i.number=1;S.appName=wukong;i.cid=97419;i.eid=3705849;i.type=2;S.title=%E5%B0%8F%E9%97%A8%E7%A5%9E;i.value=85851;S.episodetitle=%E7%AC%AC1%E9%9B%86;end","m":1}]
         * u : http://yaokong.wukongtv.com/appstore/yaokong.php?p=moli
         * p : com.molitv.android
         */
        public String n;
        public String o;
        public String u;
        public String i;
        public String m;
        public String p;
        public String type;//类型
        public String name;//名称
        public String id;//id
        public int num;//集数
        public String version;//版本号

        public String contentSource; //内容源
        public String controlSource; //控制来源
        public String controlSourceCode; //控制来源版本号
        public String sourceIntroduction;
        public String mPage;
        public String mSourceLocation;

        public ThirdPlay(String type, String name, String id, int num, String page, String sourceIntroduction, String sourceLocation, String n, String o, String u, String p, String i, String m) {
            this.type = type;
            this.id = id;
            this.num = num;
            this.name = name;
            this.sourceIntroduction = sourceIntroduction;
            this.mPage = page;
            this.mSourceLocation = sourceLocation;
            this.n = n;
            this.o = o;
            this.u = u;
            this.p = p;
            this.i = i;
            this.m = m;
        }


        /**
         * 统计MV的播放事件传过去的协议所带参数
         *
         * @param name MV名称
         * @param id   id
         * @param type
         */
        public ThirdPlay(String name, String id, int num, String type, String contentSource) {
            this.name = name;
            this.id = id;
            this.num = num;
            this.type = type;
            this.contentSource = contentSource;
        }

        public ThirdPlay(String n, String o, String u, String p, String i, String m) {

            this.n = n;
            this.o = o;
            this.u = u;
            this.p = p;
            this.i = i;
            this.m = m;
        }

        //加入了统计的电视台
        public ThirdPlay(String n, String o, String u, String p, String i, String m, String name, String id) {

            this.n = n;
            this.o = o;
            this.u = u;
            this.p = p;
            this.i = i;
            this.m = m;
            this.name = name;
            this.id = id;
        }
    }

    public static class CustomPlay {
        /**
         * 媒体类型
         * 0 视频 1音乐 2图片 3 文件
         */
        public int mediatype;
        /**
         * 区分随心听 电台 百度
         */
        public int type;
        public int pos;
        public RadioInfo radio;
        public int mode;
        public List<PlayList> playlist;


        //手机资源传单张图片的时候
        public CustomPlay(int mediatype, List<PlayList> urls, int type, int position) {
            this.mediatype = mediatype;
            this.type = type;
            this.playlist = urls;
            this.pos = position;
        }

        //爱奇艺搜全网的接口
        public CustomPlay(int mediatype, List<PlayList> urls, int type, int mode, int position) {
            this.mediatype = mediatype;
            this.type = type;
            this.playlist = urls;
            this.mode = mode;
            this.pos = position;
        }

        public CustomPlay(int mediatype, int type, RadioInfo radioInfo, List<PlayList> urls, int position) {
            this.mediatype = mediatype;
            this.type = type;
            this.radio = radioInfo;
            this.playlist = urls;
            this.pos = position;
        }

        public static class RadioInfo {
            public String name;
            public String channelId;
            public String artistId;

            public RadioInfo(String name, String channelId, String artistId) {
                this.name = name;
                this.channelId = channelId;
                this.artistId = artistId;
            }
        }

        public static class PlayList {
            /**
             * 文档类型：需要区分文件类型
             * 如连接中没有后缀名，使用这个表示文件类型
             * ".apk" ,".doc"
             */
            public String mimetype;
            public String id;
            public String number;
            public String title;
            public String singer;
            public String url;
            public String platform_id;
            public String packagename;
            public String iconurl;
            public String duration;
            public String cover_path;
            public String update_time;
            public String versioncode;
            //统计mv音乐电台的统计事件
            public String type;//类型
            public String name;//名称
            public String mId;//id
            public int num;//集数
            public String version;//版本号

            public String contentSource; //内容源
            public String controlSource; //控制来源
            public String controlSourceCode; //控制来源版本号

            //手机资源那儿传图片协议
            public PlayList(String url) {
                this.url = url;
            }

            //爱奇艺搜全网专门的接口
            public PlayList(String title, String url, String contentSource) {
                this.title = title;
                this.url = url;
                this.contentSource = contentSource;
            }

            //没加统计事件的协议
            public PlayList(String mimetype, String id, String number, String title, String singer, String url, String platform_id) {
                this.mimetype = mimetype;
                this.id = id;
                this.number = number;
                this.title = title;
                this.singer = singer;
                this.url = url;
                this.platform_id = platform_id;
            }

            //加入了MV统计的播放和歌单的统计
            public PlayList(String mimetype, String id, String number, String title, String singer, String url, String platform_id, int num, String type, String contentSource) {
                this.mimetype = mimetype;
                this.id = id;
                this.number = number;
                this.title = title;
                this.singer = singer;
                this.url = url;
                this.platform_id = platform_id;
                //加入了统计的字段
//                this.name=name;
//                this.mId=mId;
                this.num = num;
                this.type = type;
                this.contentSource = contentSource;
            }

            public PlayList(String mimetype, String packagename, String iconurl, String url, String title, String versioncode) {
                this.mimetype = mimetype;
                this.iconurl = iconurl;
                this.url = url;
                this.packagename = packagename;
                this.title = title;
                this.versioncode = versioncode;
            }

            public PlayList(String title, String url) {
                this.title = title;
                this.url = url;
            }

            public PlayList(String mimetype, String id, String number, String title, String singer, String url, String platform_id, String packagename, String iconurl, String duration, String cover_path, String update_time, String versioncode) {
                this.mimetype = mimetype;
                this.id = id;
                this.number = number;
                this.title = title;
                this.singer = singer;
                this.url = url;
                this.platform_id = platform_id;
                this.packagename = packagename;
                this.iconurl = iconurl;
                this.duration = duration;
                this.cover_path = cover_path;
                this.update_time = update_time;
                this.versioncode = versioncode;

            }

            //加入统计事件电台,和歌单那儿
            public PlayList(String mimetype, String id, String number, String title, String singer, String url, String platform_id, String packagename, String iconurl, String duration, String cover_path, String update_time, String versioncode, int num, String type, String contentSource, String name, String mId) {
                this.mimetype = mimetype;
                this.id = id;
                this.number = number;
                this.title = title;
                this.singer = singer;
                this.url = url;
                this.platform_id = platform_id;
                this.packagename = packagename;
                this.iconurl = iconurl;
                this.duration = duration;
                this.cover_path = cover_path;
                this.update_time = update_time;
                this.versioncode = versioncode;
                //统计嗦需要的字段
                this.num = num;
                this.type = type;
                this.contentSource = contentSource;
                this.name = name;
                this.mId = mId;
            }
        }
    }

    public static class ControlCmd {
        /**
         * 0 方向控制
         * 1 音乐播放；
         * 2 无极变焦控制
         * 3 3D模式控制
         * 4 图像模式控制
         * 5 语音控制
         * 6 开关机控制
         * 7 应用管理  （type 1 打开 2 卸载，3应用管理）
         * custom 10 视频播放控制（0 seek back  ； 1 seek forward ;  2 play  3 pause  4 next  5 previous）
         * music  11 音乐播放 （2:播放   3 暂停  4下一首  5上一首; 6顺序播放；7单曲循环）
         * picture 12 图片播放 （0 左旋  ； 1 右旋;  2 放大  3 缩小  4 下一个  5 上一个）
         * 智能幕布 20 （ip ,nanme state(0关，1开) ）
         * 9:截屏 (type 1截屏,2清空内存3输入(data))
         */
        public int mode;

        public ControlCmd(int mode, int type, String data) {
            this.mode = mode;
            this.type = type;
            this.data = data;
        }

        //发送关机命令
        public ControlCmd(int mode, String data) {
            this.mode = mode;
            this.data = data;
        }

        public ControlCmd(int mode, int type, int time) {
            this.mode = mode;
            this.type = type;
            this.time = time;
        }

        public ControlCmd(int mode, int type, int delayTime, String voice, List<Float> datas, ZoomFocus zoomfocus) {
            this.mode = mode;
            this.type = type;
            this.delayTime = delayTime;
            this.data = voice;
            this.datas = datas;
            this.zoomfocus = zoomfocus;
        }

        public ControlCmd(int mode, SmartScreen smarts) {
            this.mode = mode;
            this.smartScreens = smarts;
        }

        public int time;

        public SmartScreen smartScreens;

        public static class SmartScreen {
            public String name;
            public String ip;
            public int state;

            public SmartScreen(String name, String ip, int state) {
                this.name = name;
                this.ip = ip;
                this.state = state;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getIp() {
                return ip;
            }

            public void setIp(String ip) {
                this.ip = ip;
            }

            public int getState() {
                return state;
            }

            public void setState(int state) {
                this.state = state;
            }
        }

        public int type;
        public int delayTime;
        public String data;
        public List<Float> datas;
        public ZoomFocus zoomfocus;

        public static class ZoomFocus {
            public int scale;
            public int value;
            public int vvalue;
            public int hvalue;

            public ZoomFocus(int scale, int valu) {
                this.scale = scale;
                this.value = valu;

            }
        }
    }

    public static class PhoneInfo {
        public String systemversion;
        public String phonename;

        public PhoneInfo(String systemversion, String phonename) {
            this.systemversion = systemversion;
            this.phonename = phonename;
        }
    }

    public static class Statistics {
        public String name;
        public String id;
        public String sourceIntroduction;
        public String mPage;
        public String mSourceLocation;
        public int type;
        public String seContent;     //搜索内容
        public String seInputContent;  //搜索输入内容
        public String seType;  //搜索类型

        public Statistics(String name, String id, String sourceIntroduction, String mPage, String mSourceLocation) {
            this.name = name;
            this.type = 1;
            this.id = id;
            this.sourceIntroduction = sourceIntroduction;
            this.mPage = mPage;
            this.mSourceLocation = mSourceLocation;
        }

        public Statistics(String seContent, String seInputContent, String seType) {
            this.type = 2;
            this.seContent = seContent;
            this.seInputContent = seInputContent;
            this.seType = seType;
        }
    }

    public static class PhotoWall {

        /**
         * template : 1
         * imgInfo : [{"imgPosition":"0","imgUrl":"http://192.168.1.102:7766/image-item-32315#/storage/emulated/0/DCIM/Camera/IMG_20170706_160935.jpg"},{"imgPosition":"0","imgUrl":"http://192.168.1.102:7766/image-item-31811#/storage/emulated/0/Pictures/Screenshots/Screenshot_20170629-141518.png"}]
         */

        public int template;
        public List<ImgInfoBean> imgInfo;

        public int getTemplate() {
            return template;
        }

        public void setTemplate(int template) {
            this.template = template;
        }

        public List<ImgInfoBean> getImgInfo() {
            return imgInfo;
        }

        public void setImgInfo(List<ImgInfoBean> imgInfo) {
            this.imgInfo = imgInfo;
        }

        public static class ImgInfoBean {


            /**
             * imgPosition : 0
             * imgUrl : http://192.168.1.102:7766/image-item-32315#/storage/emulated/0/DCIM/Camera/IMG_20170706_160935.jpg
             */

            public ImgInfoBean(int imgPosition, String imgUrl) {
                this.imgPosition = imgPosition;
                this.imgUrl = imgUrl;
            }

            public int imgPosition;
            public String imgUrl;

            public int getImgPosition() {
                return imgPosition;
            }

            public void setImgPosition(int imgPosition) {
                this.imgPosition = imgPosition;
            }

            public String getImgUrl() {
                return imgUrl;
            }

            public void setImgUrl(String imgUrl) {
                this.imgUrl = imgUrl;
            }
        }
    }


}
