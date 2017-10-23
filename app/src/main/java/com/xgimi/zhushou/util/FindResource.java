package com.xgimi.zhushou.util;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.os.Build.VERSION;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.MediaStore.Files;
import android.provider.MediaStore.Files.FileColumns;
import android.provider.MediaStore.Video.Thumbnails;
import android.support.v4.util.ArrayMap;
import android.util.Log;

import com.xgimi.zhushou.R;
import com.xgimi.zhushou.bean.FileInfo;
import com.xgimi.zhushou.bean.ImageInfo;
import com.xgimi.zhushou.bean.VideoInfo;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Vector;

import de.greenrobot.event.EventBus;


public class FindResource {
    public void startFindImages() {
        new Thread(findImages).start();
    }

    public void startFindVideos() {
        new Thread(findVideos).start();
    }

    public void startFindFiles() {
        new Thread(findFiles).start();
    }

    public Context mContext;

    public FindResource(Context context) {
        this.mContext = context;
    }

    public enum FileCategory {
        Music, Video, Picture, Theme, Doc, Zip, Apk, Custom, Other, Favorite, PPT
    }

    Runnable findVideos = new Runnable() {

        @Override
        public void run() {
            GlobalConsts.videoList.clear();
            GlobalConsts.videoMapGroup.clear();
            GlobalConsts.mVideoKeyList.clear();
            GlobalConsts.mVideoMap.clear();
            Cursor cursor = null;
            // Video Container
            getRootDir(mContext);
            GetQQVideo("/storage/emulated/0/tencent/QQfile_recv/");
            GetQQVideo(getRootDir(mContext) + "/XGIMI/");
            cursor = mContext.getContentResolver().query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                    new String[]{MediaStore.Video.Media._ID, MediaStore.Video.Media.TITLE, MediaStore.Video.Media.DATA,
                            MediaStore.Video.Media.ARTIST, MediaStore.Video.Media.MIME_TYPE, MediaStore.Video.Media.SIZE,
                            MediaStore.Video.Media.DURATION}, null, null, null);

            if (cursor == null || cursor.getCount() == 0) {
                return;
            }

            if (cursor.moveToFirst()) {
                do {
                    long id = cursor.getInt(0);
                    String title = cursor.getString(1);
                    String filePath = cursor.getString(2);
                    String mimeType = cursor.getString(4);
                    long size = cursor.getLong(5);

                    if (size < 10) {
                        continue;
                    }
                    long duration = cursor.getLong(6);
                    Log.e("mimeType", "" + mimeType);
                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put("id", id);
                    map.put("title", title);
                    map.put("filepath", filePath);
                    map.put("size", size);
                    map.put("duration", duration);
                    if (filePath.equals("")) {
                        return;
                    }
                    Bitmap tempThumb = ThumbnailUtils.createVideoThumbnail(filePath, Thumbnails.MINI_KIND);
                    if (tempThumb == null) {
                        tempThumb = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.app_icon);
                    }
                    tempThumb = ThumbnailUtils.extractThumbnail(tempThumb, 250, 150);
                    map.put("tempThumb", tempThumb);

                    GlobalConsts.videoList.add(map);
                    GlobalConsts.videoMapGroup.put(GlobalConsts.VIDEO_PREFIX + id, filePath);
                    if (!GlobalConsts.mVideoMap.containsKey(filePath)) {
                        List<VideoInfo> fileList = new ArrayList<>();
                        VideoInfo video = new VideoInfo();
                        video.setId(id);
                        video.setFilePath(filePath);
                        video.setDuration(duration);
                        video.setTitle(title);
                        video.setTempThumb(tempThumb);
                        fileList.add(video);
                        GlobalConsts.mVideoMap.put(filePath, fileList);
                    } else {
                        List<VideoInfo> fileList = GlobalConsts.mVideoMap.get(filePath);
                        VideoInfo video = new VideoInfo();
                        video.setId(id);
                        video.setTitle(title);
                        video.setDuration(duration);
                        video.setFilePath(filePath);
                        video.setTempThumb(tempThumb);
                        if (!videoContains(fileList, video)) {
                            fileList.add(video);
                        }
                    }
                } while (cursor.moveToNext());
            }
            if (VERSION.SDK_INT < 14) {
                cursor.close();
            }
            for (Entry<String, List<VideoInfo>> e : GlobalConsts.mVideoMap.entrySet()) {
                GlobalConsts.mVideoKeyList.add(e.getKey());
            }
            //视频去重
            removeDuplicate1(GlobalConsts.videoList);
            EventBus.getDefault().post(new VideoInfo());
        }
    };


    Runnable findFiles = new Runnable() {

        @Override
        public void run() {
            Cursor cursor = null;
            GlobalConsts.pptMapGroup.clear();
            GlobalConsts.pdfMapGroup.clear();
            GlobalConsts.docMapGroup.clear();
            GlobalConsts.exlMapGroup.clear();
            GlobalConsts.txtMapGroup.clear();
            GlobalConsts.apkMapGroup.clear();
            GlobalConsts.files.clear();
            GetQQFile("/storage/emulated/0/tencent/QQfile_recv/");
            getRootDir(mContext);
            GetQQFile(getRootDir(mContext) + "/XGIMI/");
            FileCategory[] fileCategorys = {FileCategory.Doc, FileCategory.Apk};
            for (FileCategory file : fileCategorys) {
                String selection = buildSelectionByCategory(file);
                cursor = mContext.getContentResolver().query(Files.getContentUri("external"), new String[]{FileColumns._ID, FileColumns.DATA, FileColumns.SIZE, FileColumns.MIME_TYPE}, selection, null, null);
                Log.e("lianjie", String.valueOf(Files.getContentUri("external")));
                if (cursor == null || cursor.getCount() == 0) {
//					cursor = mContext.getContentResolver().query(Uri.parse("/storage/emulated/0/tencent/QQfile_recv/"), new String[] { FileColumns._ID, FileColumns.DATA, FileColumns.SIZE, FileColumns.MIME_TYPE }, selection, null, null);
//					if(cursor == null || cursor.getCount() == 0){
                    continue;
//					}
                }
                if (cursor.moveToFirst()) {
                    do {
                        String filePath = cursor.getString(1);
                        Log.e("lujing", filePath);
                        long size = cursor.getLong(2);
                        if (!new File(filePath).exists() || filePath.substring(filePath.lastIndexOf(File.separator) + 1, filePath.length()).startsWith(".") || (size < 10)) {
                            continue;
                        }
                        String id = "";
                        if (filePath.endsWith(".ppt") || filePath.endsWith(".PPT") || filePath.endsWith(".pptx") || filePath.endsWith(".PPTX")) {
                            id = GlobalConsts.FILE_PREFIX_PPT + cursor.getInt(0);
                            Map<String, Object> map = new HashMap<String, Object>();
                            map.put("id", id);
                            map.put("filepath", filePath);
                            map.put("size", size);
                            GlobalConsts.pptMapGroup.add(map);
                        } else if (filePath.endsWith(".pdf") || filePath.endsWith(".PDF")) {
                            id = GlobalConsts.FILE_PREFIX_PDF + cursor.getInt(0);
                            Map<String, Object> map = new HashMap<String, Object>();
                            map.put("id", id);
                            map.put("filepath", filePath);
                            map.put("size", size);
                            GlobalConsts.pdfMapGroup.add(map);
                        } else if (filePath.endsWith(".doc") || filePath.endsWith(".DOC") || filePath.endsWith(".docx") || filePath.endsWith(".DOCX")) {
                            id = GlobalConsts.FILE_PREFIX_WORD + cursor.getInt(0);
                            Map<String, Object> map = new HashMap<String, Object>();
                            map.put("id", id);
                            map.put("filepath", filePath);
                            map.put("size", size);
                            GlobalConsts.docMapGroup.add(map);
                        } else if (filePath.endsWith(".xls") || filePath.endsWith(".XLS") || filePath.endsWith(".xlsx") || filePath.endsWith(".XLSX")) {
                            id = GlobalConsts.FILE_PREFIX_EXL + cursor.getInt(0);
                            Map<String, Object> map = new HashMap<String, Object>();
                            map.put("id", id);
                            map.put("filepath", filePath);
                            map.put("size", size);
                            GlobalConsts.exlMapGroup.add(map);
                        } else if (filePath.endsWith(".apk") || filePath.endsWith(".APK")) {
                            id = GlobalConsts.FILE_PREFIX_APK + cursor.getInt(0);
                            String title = GlobalConsts.FILE_PREFIX_APK + cursor.getInt(1);
                            Map<String, Object> map = new HashMap<String, Object>();
                            map.put("id", id);
                            map.put("filepath", filePath);
                            map.put("size", size);
                            map.put("package", Util.getPackage(mContext, filePath));
                            map.put("name", Util.getAppName(mContext, filePath));
                            map.put("version", Util.getAppVersioncode(mContext, filePath));
                            map.put("icon", Util.getApkIcon(mContext, filePath));
                            map.put("title", title);
                            GlobalConsts.apkMapGroup.add(map);
                        } else if (filePath.endsWith(".txt") || filePath.endsWith(".TXT")) {
                            id = GlobalConsts.FILE_PREFIX_TXT + cursor.getInt(0);
                            Map<String, Object> map = new HashMap<String, Object>();
                            map.put("id", id);
                            map.put("filepath", filePath);
                            map.put("size", size);
                            GlobalConsts.txtMapGroup.add(map);
                        }
                        GlobalConsts.fileMapGroup.put(id, filePath);
                    } while (cursor.moveToNext());
                }
            }
            //文件去重
            removeDuplicate(GlobalConsts.pptMapGroup);
            removeDuplicate(GlobalConsts.pdfMapGroup);
            removeDuplicate(GlobalConsts.docMapGroup);
            removeDuplicate(GlobalConsts.exlMapGroup);
            removeDuplicate(GlobalConsts.txtMapGroup);
            removeDuplicate(GlobalConsts.apkMapGroup);
            GlobalConsts.files.add(GlobalConsts.pptMapGroup);
            GlobalConsts.files.add(GlobalConsts.pdfMapGroup);
            GlobalConsts.files.add(GlobalConsts.docMapGroup);
            GlobalConsts.files.add(GlobalConsts.exlMapGroup);
            GlobalConsts.files.add(GlobalConsts.txtMapGroup);
            GlobalConsts.files.add(GlobalConsts.apkMapGroup);

            if (VERSION.SDK_INT < 14) {
                cursor.close();
            }
            EventBus.getDefault().post(new FileInfo());
        }
    };

    private String buildSelectionByCategory(FileCategory cat) {
        String selection = null;
        switch (cat) {
            case Theme:
                selection = FileColumns.DATA + " LIKE '%.mtz'";
                break;
            case Doc:
                selection = buildDocSelection();
                break;
            case Zip:
                selection = "(" + FileColumns.MIME_TYPE + " == '" + Util.sZipFileMimeType + "')";
                break;
            case Apk:
                selection = FileColumns.DATA + " LIKE '%.apk'";
                break;
            case PPT:
                selection = FileColumns.DATA + " LIKE '%.ppt'";
                break;
            default:
                selection = null;
        }
        return selection;
    }

    private String buildDocSelection() {
        StringBuilder selection = new StringBuilder();
        Iterator<String> iter = Util.sDocMimeTypesSet.iterator();
        while (iter.hasNext()) {
            selection.append("(" + FileColumns.MIME_TYPE + "=='" + iter.next() + "') OR ");
        }
        return selection.substring(0, selection.lastIndexOf(")") + 1);
    }

    Runnable findImages = new Runnable() {

        @Override
        public void run() {
            try {
                GlobalConsts.mImgMap.clear();
                GlobalConsts.mImgKeyList.clear();
                Cursor cursor = null;
                //修改
                getRootDir(mContext);
//                GetQQImage("/storage/emulated/0/tencent/QQ_Images/");
//                GetQQImage(getRootDir(mContext) + "/XGIMI/");

                // Image Container
                cursor = mContext.getContentResolver().query(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        new String[]{MediaStore.Images.Media._ID, MediaStore.Images.Media.TITLE, MediaStore.Images.Media.DATA,
                                MediaStore.Images.Media.MIME_TYPE, MediaStore.Images.Media.SIZE, MediaStore.Images.Media.DISPLAY_NAME,},
                        MediaStore.Images.Media.MIME_TYPE + "=? or " + MediaStore.Images.Media.MIME_TYPE + "=?",
                        new String[]{"image/jpeg", "image/png"}, MediaStore.Images.Media.DATE_MODIFIED);

                while (cursor != null && cursor.moveToNext()) {
                /* 获取图片的路径 */
                    String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                    String p = new File(path).getParentFile().getName();
                    if ("zhushou".equals(p)) {
//                        XGIMILOG.D("path = " + path);
                    }

                    
//				if(path==null){
//					return;
//				}
                    if (path != null && new File(path).exists()) {
                        // 创建服务的表
                        long id = cursor.getLong(0);
                        GlobalConsts.imageMapGroup.put(GlobalConsts.IMAGE_PREFIX + id, path);
                    /* 获取该图片的父路径名 */
                        String parentName = new File(path).getParentFile().getName();
//                        XGIMILOG.D(new File(path).getAbsolutePath());
                    /* 保存数据信息 */
                        long size = cursor.getLong(4);
                        if (size < 1024 * 10) {
                            continue;
                        }
                        if (!GlobalConsts.mImgMap.containsKey(parentName)) {
                            List<ImageInfo> fileList = new ArrayList<ImageInfo>();
                            ImageInfo image = new ImageInfo();
                            image.setId(id);
                            image.setUrl(path);
                            fileList.add(image);
//                            GlobalConsts.mImgKeyList.add(parentName);
                            GlobalConsts.mImgMap.put(parentName, fileList);
                        } else {
                            List<ImageInfo> fileList = GlobalConsts.mImgMap.get(parentName);
                            ImageInfo image = new ImageInfo();
                            image.setId(id);
                            image.setUrl(path);
                            if (!imgContains(fileList, image)) {
                                fileList.add(image);
                            } else {
                            }
                        }
                    }
                }
                if (VERSION.SDK_INT < 14) {
                    cursor.close();
                }

                for (Entry<String, List<ImageInfo>> e : GlobalConsts.mImgMap.entrySet()) {
                    GlobalConsts.mImgKeyList.add(e.getKey());
                }
                Collections.sort(GlobalConsts.mImgKeyList);

//                XGIMILOG.E("查找图片完成");
                //图片去重
//                removeDuplicate2(GlobalConsts.mImgKeyList);
                EventBus.getDefault().post(new ImageInfo());
            } catch (Exception e) {
            }
        }
    };

    // 获取QQ下载传输的目录下的保存到本地默认目录下所有的图片
    public Vector<String> GetQQImage(String fileAbsolutePath) {
        Vector<String> vecFile = new Vector<String>();
        File file = new File(fileAbsolutePath);
        File[] subFile = file.listFiles();
        if (subFile != null) {
            for (int iFileLength = 0; iFileLength < subFile.length; iFileLength++) {
                // 判断是否为文件夹
                if (!subFile[iFileLength].isDirectory()) {
                    String filename = subFile[iFileLength].getName();
                    String path = subFile[iFileLength].getPath();

//				long size=subFile[iFileLength].length();
//				String id = "";
//				String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
//				if(path==null){
//					return;
//				}
                    if (path != null && new File(path).exists() && (path.endsWith(".jpeg") || path.endsWith(".jpg") || path.endsWith(".png"))) {
                        // 创建服务的表
                        long id = subFile[iFileLength].length();
                        GlobalConsts.imageMapGroup.put(GlobalConsts.IMAGE_PREFIX + id, path);
                    /* 获取该图片的父路径名 */
                        String parentName = new File(path).getParentFile().getName();
//                        XGIMILOG.D(new File(path).getAbsolutePath());
                    /* 保存数据信息 */
                        long size = subFile[iFileLength].length();
                        if (size < 1024 * 10) {
//						continue;
                        }
                        if (!GlobalConsts.mImgMap.containsKey(parentName)) {
                            List<ImageInfo> fileList = new ArrayList<ImageInfo>();
                            ImageInfo image = new ImageInfo();
                            image.setId(id);
                            image.setUrl(path);
                            fileList.add(image);
                            GlobalConsts.mImgMap.put(parentName, fileList);
                        } else {
                            List<ImageInfo> fileList = GlobalConsts.mImgMap.get(parentName);
                            ImageInfo image = new ImageInfo();
                            image.setId(id);
                            image.setUrl(path);
                            if (!imgContains(fileList, image)) {
                                fileList.add(image);
                            }
                        }
                        for (Entry<String, List<ImageInfo>> e : GlobalConsts.mImgMap.entrySet()) {
                            GlobalConsts.mImgKeyList.add(e.getKey());
                        }
                        //图片去重
//                        removeDuplicate2(GlobalConsts.mImgKeyList);
                    }
                    // 判断是否为MP4结尾

                }
            }
        }

        return vecFile;
    }

    private boolean imgContains(List<ImageInfo> fileList, ImageInfo image) {
        boolean res = false;
        for (int i = 0; i < fileList.size(); i++) {
            if (fileList.get(i).getUrl().equals(image.getUrl())) {
                res = true;
                break;
            }
        }
        return res;
    }

    // 获取QQ下载传输的目录下的保存到本地默认目录下所有的视频
    public Vector<String> GetQQVideo(String fileAbsolutePath) {
        Vector<String> vecFile = new Vector<String>();
        File file = new File(fileAbsolutePath);
        File[] subFile = file.listFiles();
        if (subFile != null) {
            for (int iFileLength = 0; iFileLength < subFile.length; iFileLength++) {
                // 判断是否为文件夹
                if (!subFile[iFileLength].isDirectory()) {
                    String filename = subFile[iFileLength].getName();
//				String filePath=subFile[iFileLength].getPath();
//				long size=subFile[iFileLength].length();
//				String id = "";
                    if (filename.trim().toLowerCase().endsWith(".mp4") || filename.trim().toLowerCase().endsWith(".rmvb") || filename.trim().toLowerCase().endsWith(".mkv")
                            || filename.trim().toLowerCase().endsWith(".mov")) {
                        long id = subFile[iFileLength].length();
                        String title = subFile[iFileLength].getName();
                        String filePath = subFile[iFileLength].getPath();
                        String mimeType = subFile[iFileLength].getName();
                        long size = subFile[iFileLength].length();
                        long duration = subFile[iFileLength].length();
//                        Log.e("mimeType", "" + mimeType);
                        Map<String, Object> map = new HashMap<String, Object>();
                        map.put("id", id);
                        map.put("title", title);
                        map.put("filepath", filePath);
                        map.put("size", size);
                        map.put("duration", duration);
                        if (filePath.equals("")) {
//					return;
                        }
                        Bitmap tempThumb = ThumbnailUtils.createVideoThumbnail(filePath, Thumbnails.MINI_KIND);
                        if (tempThumb == null) {
                            tempThumb = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.app_icon);
                        }
                        tempThumb = ThumbnailUtils.extractThumbnail(tempThumb, 250, 150);
                        map.put("tempThumb", tempThumb);

                        GlobalConsts.videoList.add(map);
                        GlobalConsts.videoMapGroup.put(GlobalConsts.VIDEO_PREFIX + id, filePath);
                        if (!GlobalConsts.mVideoMap.containsKey(filePath)) {
                            List<VideoInfo> fileList = new ArrayList<>();
                            VideoInfo video = new VideoInfo();
                            video.setId(id);
                            video.setFilePath(filePath);
                            video.setTitle(title);
                            video.setDuration(duration);
                            video.setTempThumb(tempThumb);
                            fileList.add(video);
                            GlobalConsts.mVideoMap.put(filePath, fileList);
                        } else {
                            List<VideoInfo> fileList = GlobalConsts.mVideoMap.get(filePath);
                            VideoInfo video = new VideoInfo();
                            video.setId(id);
                            video.setTitle(title);
                            video.setDuration(duration);
                            video.setFilePath(filePath);
                            video.setTempThumb(tempThumb);
                            if (!videoContains(fileList, video)) {
                                fileList.add(video);
                            }
                        }

                        for (Entry<String, List<VideoInfo>> e : GlobalConsts.mVideoMap.entrySet()) {
                            GlobalConsts.mVideoKeyList.add(e.getKey());
                        }
                        //视频去重
                        removeDuplicate1(GlobalConsts.videoList);
                    }

                }
            }
        }

        return vecFile;
    }

    private boolean videoContains(List<VideoInfo> fileList, VideoInfo video) {
        boolean res = false;
        for (int i = 0; i < fileList.size(); i++) {
            if (fileList.get(i).getFilePath().equals(video.getFilePath())) {
                res = true;
                break;
            }
        }
        return res;
    }

    // 获取QQ下载传输的目录下的保存到本地默认目录下所有的文件
    public Vector<String> GetQQFile(String fileAbsolutePath) {
        Vector<String> vecFile = new Vector<String>();
        File file = new File(fileAbsolutePath);
        File[] subFile = file.listFiles();
        if (subFile != null) {
            for (int iFileLength = 0; iFileLength < subFile.length; iFileLength++) {
                // 判断是否为文件夹
                if (!subFile[iFileLength].isDirectory()) {
                    String filename = subFile[iFileLength].getName();
                    String filePath = subFile[iFileLength].getPath();
                    long size = subFile[iFileLength].length();
                    String id = "";
                    if (filePath.endsWith(".ppt") || filePath.endsWith(".PPT") || filePath.endsWith(".pptx") || filePath.endsWith(".PPTX")) {
                        id = GlobalConsts.FILE_PREFIX_PPT + subFile[iFileLength].length();
                        Map<String, Object> map = new HashMap<String, Object>();
                        map.put("id", id);
                        map.put("filepath", filePath);
                        map.put("size", size);
                        GlobalConsts.pptMapGroup.add(map);
                    } else if (filePath.endsWith(".pdf") || filePath.endsWith(".PDF")) {
                        id = GlobalConsts.FILE_PREFIX_PDF + subFile[iFileLength].length();
                        Map<String, Object> map = new HashMap<String, Object>();
                        map.put("id", id);
                        map.put("filepath", filePath);
                        map.put("size", size);

                        GlobalConsts.pdfMapGroup.add(map);
                    } else if (filePath.endsWith(".doc") || filePath.endsWith(".DOC") || filePath.endsWith(".docx") || filePath.endsWith(".DOCX")) {
                        id = GlobalConsts.FILE_PREFIX_WORD + subFile[iFileLength].length();
                        Map<String, Object> map = new HashMap<String, Object>();
                        map.put("id", id);
                        map.put("filepath", filePath);
                        map.put("size", size);
                        GlobalConsts.docMapGroup.add(map);
                    } else if (filePath.endsWith(".xls") || filePath.endsWith(".XLS") || filePath.endsWith(".xlsx") || filePath.endsWith(".XLSX")) {
                        id = GlobalConsts.FILE_PREFIX_EXL + subFile[iFileLength].length();
                        Map<String, Object> map = new HashMap<String, Object>();
                        map.put("id", id);
                        map.put("filepath", filePath);
                        map.put("size", size);
                        GlobalConsts.exlMapGroup.add(map);
                    } else if (filePath.endsWith(".apk") || filePath.endsWith(".APK")) {
                        id = GlobalConsts.FILE_PREFIX_APK + subFile[iFileLength].length();
                        String title = GlobalConsts.FILE_PREFIX_APK + subFile[iFileLength].getName();
                        Map<String, Object> map = new HashMap<String, Object>();
                        map.put("id", id);
                        map.put("filepath", filePath);
                        map.put("size", size);
                        map.put("package", Util.getPackage(mContext, filePath));
                        map.put("name", Util.getAppName(mContext, filePath));
                        map.put("version", Util.getAppVersioncode(mContext, filePath));
                        map.put("icon", Util.getApkIcon(mContext, filePath));
                        map.put("title", title);
                        GlobalConsts.apkMapGroup.add(map);
                    } else if (filePath.endsWith(".txt") || filePath.endsWith(".TXT")) {
                        id = GlobalConsts.FILE_PREFIX_TXT + subFile[iFileLength].length();
                        Map<String, Object> map = new HashMap<String, Object>();
                        map.put("id", id);
                        map.put("filepath", filePath);
                        map.put("size", size);
                        GlobalConsts.txtMapGroup.add(map);
                    }
                    GlobalConsts.fileMapGroup.put(id, filePath);
                    // 判断是否为MP4结尾
//				if (filename.trim().toLowerCase().endsWith(".doc")||filename.trim().toLowerCase().endsWith(".pdf")) {
//					vecFile.add(filename);
//					Log.e("mingzi",filename);
//				}
                }
            }
        }
        return vecFile;
    }

    public Map<String, Object> myMap = new ArrayMap<>();
    public Map<String, Object> myMap1 = new ArrayMap<>();

    //视频去掉重复的数据
    private synchronized List<Map<String, Object>> removeDuplicate1(List<Map<String, Object>> data) {

        Map<String, Object> myMap = new ArrayMap<>();
        Map<String, Object> myMap1 = new ArrayMap<>();
        if (data == null) {
            return null;
        }
        if (data.size() == 0) {
            return data;
        }
        for (int i = 0; i < data.size(); i++) {
            if (i < data.size()) {
                myMap = data.get(i);
                for (int j = data.size() - 1; j > i; j--) {
                    myMap1 = data.get(j);
                    if (myMap1.get("id").toString().equals(myMap.get("id").toString()) || getFileNameNoEx(myMap1.get("filepath").toString()).equals(getFileNameNoEx(myMap.get("filepath").toString())) || myMap1.get("size").toString().equals(myMap.get("size").toString())
                            || myMap1.get("title").toString().equals(myMap.get("title").toString()) || myMap1.get("duration").toString().equals(myMap.get("duration").toString())) {
                        data.remove(j);
                    }
                }
            }
        }
        return data;
    }

    /*
     * Java文件操作 获取不带扩展名的文件名,视频
     */
    public static String getFileNameNoEx(String filename) {
        if ((filename != null) && (filename.length() > 0)) {
            int dot = filename.lastIndexOf('.');
            if ((dot > -1) && (dot < (filename.length()))) {
                return filename.substring(0, dot);
            }
        }
        return filename;
    }


    Object object = new Object();

    //文件去掉重复的数据
    private synchronized List<Map<String, Object>> removeDuplicate(List<Map<String, Object>> data) {

        Map<String, Object> myMap = new ArrayMap<>();
        Map<String, Object> myMap1 = new ArrayMap<>();
        if (data == null) {
            return null;
        }
        if (data.size() == 0) {
            return data;
        }

        for (int i = 0; i < data.size(); i++) {
            if (i < data.size()) {
                myMap = data.get(i);
                for (int j = data.size() - 1; j > i; j--) {
                    myMap1 = data.get(j);

                    if (myMap1.get("id").toString().equals(myMap.get("id").toString()) || myMap1.get("filepath").toString().equals(myMap.get("filepath").toString()) || myMap1.get("size").toString().equals(myMap.get("size").toString())) {
                        data.remove(j);
                    }
                }
            }
        }
        return data;
    }

    public String ImageMap;
    public String ImageMap1;

    //图片去掉重复的数据
    private List<String> removeDuplicate2(List<String> data) {
        for (int i = 0; i < data.size(); i++) {
            if (i < data.size()) {
                ImageMap = data.get(i);
                for (int j = data.size() - 1; j > i; j--) {
                    ImageMap1 = data.get(j);
                    if (ImageMap.equals(ImageMap1)) {
                        data.remove(j);
                    }
                }
            }
        }
        return data;
    }

    /**
     * 获取根目录(应用cache 或者 SD卡)
     * <br>
     * <br>
     * 优先获取SD卡根目录[/storage/sdcard0]
     * <br>
     * <br>
     * 应用缓存目录[/data/data/应用包名/cache]
     * <br>
     *
     * @param context 上下文
     * @return
     */
    public static String rootDir;

    public static String getRootDir(Context context) {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            // 优先获取SD卡根目录[/storage/sdcard0]
//            Log.e("mulu", Environment.getExternalStorageDirectory().getAbsolutePath());
            rootDir = Environment.getExternalStorageDirectory().getAbsolutePath();
            return Environment.getExternalStorageDirectory().getAbsolutePath();

        } else {
            // 应用缓存目录[/data/data/应用包名/cache]
//            Log.e("mulu1", context.getCacheDir().getAbsolutePath());
            return context.getCacheDir().getAbsolutePath();

        }
    }
}
