package com.xgimi.zhushou.util;


import com.xgimi.zhushou.bean.ImageInfo;
import com.xgimi.zhushou.bean.Mp3Info;
import com.xgimi.zhushou.bean.VideoInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class GlobalConsts {

    public final static String FILE_PREFIX_WORD = "file-doc-";
    public final static String FILE_PREFIX_EXL = "file-xls-";
    public final static String FILE_PREFIX_PPT = "file-ppt-";
    public final static String FILE_PREFIX_TXT = "file-txt-";
    public final static String FILE_PREFIX_APK = "file-apk-";
    public final static String FILE_PREFIX_PDF = "file-pdf-";

    //media 命令
    private static final int MEDIA_RENDER_CTL_MSG_BASE = 0x100;
    public static final int MEDIA_RENDER_CTL_MSG_SET_AV_URL = (MEDIA_RENDER_CTL_MSG_BASE + 0);
    public static final int MEDIA_RENDER_CTL_MSG_STOP = (MEDIA_RENDER_CTL_MSG_BASE + 1);
    public static final int MEDIA_RENDER_CTL_MSG_PLAY = (MEDIA_RENDER_CTL_MSG_BASE + 2);
    public static final int MEDIA_RENDER_CTL_MSG_PAUSE = (MEDIA_RENDER_CTL_MSG_BASE + 3);
    public static final int MEDIA_RENDER_CTL_MSG_SEEK = (MEDIA_RENDER_CTL_MSG_BASE + 4);
    public static final int MEDIA_RENDER_CTL_MSG_SETVOLUME = (MEDIA_RENDER_CTL_MSG_BASE + 5);
    public static final int MEDIA_RENDER_CTL_MSG_SETMUTE = (MEDIA_RENDER_CTL_MSG_BASE + 6);
    public static final int MEDIA_RENDER_CTL_MSG_SETPLAYMODE = (MEDIA_RENDER_CTL_MSG_BASE + 7);
    public static final int MEDIA_RENDER_CTL_MSG_PRE = (MEDIA_RENDER_CTL_MSG_BASE + 8);
    public static final int MEDIA_RENDER_CTL_MSG_NEXT = (MEDIA_RENDER_CTL_MSG_BASE + 9);
    public static List<Map<String, Object>> videoList = new ArrayList<Map<String, Object>>();
    public static List<List<Map<String, Object>>> files = new ArrayList<List<Map<String, Object>>>();
    public static final String ROOT_PATH = "/";

    public static HashMap<String, List<ImageInfo>> mImgMap = new HashMap<String, List<ImageInfo>>();
    public static List<String> mImgKeyList = new ArrayList<>();

    public static HashMap<String, List<VideoInfo>> mVideoMap = new HashMap<String, List<VideoInfo>>();
    public static List<String> mVideoKeyList = new ArrayList<>();
    //数据表
    public static List<Mp3Info> mp3Infos = null;
    public static List<VideoInfo> mVideoList = new ArrayList<>();
    public static HashMap<String, String> imageMapGroup = new HashMap<String, String>();
    public static HashMap<String, String> musicMapGroup = new HashMap<String, String>();
    public static HashMap<String, String> videoMapGroup = new HashMap<String, String>();
    public static HashMap<String, String> fileMapGroup = new HashMap<String, String>();

    public static List<Map<String, Object>> pdfMapGroup = new ArrayList<Map<String, Object>>();
    public static List<Map<String, Object>> pptMapGroup = new ArrayList<Map<String, Object>>();
    public static List<Map<String, Object>> docMapGroup = new ArrayList<Map<String, Object>>();
    public static List<Map<String, Object>> txtMapGroup = new ArrayList<Map<String, Object>>();
    public static List<Map<String, Object>> apkMapGroup = new ArrayList<Map<String, Object>>();
    public static List<Map<String, Object>> exlMapGroup = new ArrayList<Map<String, Object>>();


    public final static String VIDEO_PREFIX = "video-item-";


    public final static String AUDIO_PREFIX = "audio-item-";
    public final static String IMAGE_PREFIX = "image-item-";
    public final static String FILE_PREFIX = "file-";
    public static final String PHOTOSTOP = "PICTURECONTROL:http:image-com-stop";

}
