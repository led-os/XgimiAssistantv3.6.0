package com.xgimi.zhushou.util;

import com.baidu.music.model.Music;
import com.xgimi.zhushou.qttx.QingTingListInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 霍长江 on 2016/8/25.
 */
public class TvJson {
    public static TvJson instance;
    private TvJson(){

    }
    public static TvJson getInstance(){
        if(instance==null){
            instance=new TvJson();
        }
        return instance;
    }
    //发送歌曲json
    public String songJson(List<Music> mMuscis,int postion){
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        JSONObject js2 = new JSONObject();
        try {
            for (int i = 0; i < mMuscis.size(); i++) {
                JSONObject jsobject3 = new JSONObject();
                Music music = mMuscis.get(i);
                jsobject3.put("id", music.mId);
                jsobject3.put("title", music.mTitle);
                jsobject3.put("singer", music.mArtist);
                jsobject3.put("url", null);
                jsonArray.put(jsobject3);
            }
            jsonObject.put("type", 0);
            jsonObject.put("pos", postion);
            jsonObject.put("playlist", jsonArray);
            js2.put("data", jsonObject);
            js2.put("action", 10);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return js2.toString();
    }

    //发送电台json
    public String radioJson( ArrayList<QingTingListInfo.Data> mData,String mCoverPath,int postion){
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        JSONObject js2 = new JSONObject();
        try {
            for (int i = 0; i < mData.size(); i++) {
                JSONObject jsobject3 = new JSONObject();
                QingTingListInfo.Data data = mData.get(i);
                jsobject3.put("id", data.id);
                jsobject3.put("title", data.title);
                jsobject3.put("singer", null);
                jsobject3.put("url", data.mediainfo.bitrates_url.get(0).file_path);
                jsobject3.put("duration", data.duration);
                jsobject3.put("cover_path", mCoverPath);
                jsonArray.put(jsobject3);
            }
            jsonObject.put("type", -1);
            jsonObject.put("pos", postion);
            jsonObject.put("playlist", jsonArray);
            js2.put("data", jsonObject);
            js2.put("action", 10);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return js2.toString();
    }
}
