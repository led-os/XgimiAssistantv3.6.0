package com.xgimi.zhushou.aes;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.xgimi.zhushou.bean.AllMovie;
import com.xgimi.zhushou.bean.CancleMovieCollect;
import com.xgimi.zhushou.bean.CollectNum;
import com.xgimi.zhushou.bean.FilmDetailBean;
import com.xgimi.zhushou.bean.IsMovieCollect;
import com.xgimi.zhushou.bean.MovieByCategory;
import com.xgimi.zhushou.bean.MovieCollect;
import com.xgimi.zhushou.bean.MovieCollectBeen;
import com.xgimi.zhushou.bean.MovieRecommend;
import com.xgimi.zhushou.bean.SearchBean;
import com.xgimi.zhushou.bean.SearchMovieResult;
import com.xgimi.zhushou.bean.ZhuanTiBean;
import com.xgimi.zhushou.util.LogUtil;
import com.xgimi.zhushou.util.XGIMILOG;


import org.json.JSONObject;

import java.io.IOException;

import io.vov.vitamio.utils.Log;
import okhttp3.ResponseBody;
import retrofit2.Converter;

/**
 * Created by XGIMI on 2017/7/11.
 */

public class JsonAesResponseBody<T> implements Converter<ResponseBody, T> {
    private static final String TAG = "BodyC2";

    private final Gson gson;
    private final TypeAdapter adapter;
    private Class mClass;

    public JsonAesResponseBody(Gson gson, TypeAdapter adapter, Class mClass) {
        this.gson = gson;
        this.adapter = adapter;
        this.mClass = mClass;
    }

    @Override
    public T convert(ResponseBody value) throws IOException {

        Object resObj = null;
        try {
            String s = value.string();
            JSONObject j = new JSONObject(s);
//            LogUtil.d(XGIMILOG.getTag(), " 原始数据 : " + j.toString());
            resObj = mClass.newInstance();
            String objName = mClass.getSimpleName();
            if ("CollectNum".equals(objName)) {//获取收藏数量信息
                String decryptData = new Gson().toJson(EasyAES.getInstance().decrypt(j.getString("data")));
                decryptData = decryptData.substring(1, decryptData.length()-1);
                ((CollectNum)resObj).msg = j.getString("msg");
                ((CollectNum)resObj).code = j.getInt("code");
                ((CollectNum)resObj).data = new Gson().fromJson(decryptData.replace("\\", ""), CollectNum.Data.class);
            } else if ("MovieCollect".equals(objName)) {//添加影视收藏
                ((MovieCollect)resObj).message = j.getString("msg");
                ((MovieCollect)resObj).code = j.getInt("code");
                ((MovieCollect)resObj).data = EasyAES.getInstance().decrypt(j.getString("data"));
            } else if ("MovieRecommend".equals(objName)) {//首页影视精选
                String decryptData = new Gson().toJson(EasyAES.getInstance().decrypt(j.getString("data")));
                decryptData = decryptData.substring(1, decryptData.length()-1);
//                LogUtil.d(XGIMILOG.getTag(), "首页原始数据 : " + decryptData);
                String data = decryptData.replace("\\\"", "\"").replace("\\\\/", "/").replace("\\\\", "\\");
                ((MovieRecommend)resObj).message = j.getString("msg");
                ((MovieRecommend)resObj).code = j.getInt("code");
                ((MovieRecommend)resObj).data = new Gson().fromJson(data, MovieRecommend.DataBean.class);
            } else if ("IsMovieCollect".equals(objName)) {//检查是否收藏影视
                ((IsMovieCollect)resObj).message = j.getString("msg");
                ((IsMovieCollect)resObj).code = j.getInt("code");
                ((IsMovieCollect)resObj).data = new JSONObject(EasyAES.getInstance().decrypt(j.getString("data"))).getString("msg");
            } else if ("FilmDetailBean".equals(objName)) {//获取电影详情
//                LogUtil.d(XGIMILOG.getTag(), "获取电影详情: " + EasyAES.getInstance().decrypt(j.getString("data")));
                ((FilmDetailBean)resObj).message = j.getString("msg");
                ((FilmDetailBean)resObj).code = j.getInt("code");
                ((FilmDetailBean)resObj).data = new Gson().fromJson(EasyAES.getInstance().decrypt(j.getString("data")).replace("\\/", "/"), FilmDetailBean.DataBean.class);
            } else if ("SearchMovieResult".equals(objName)) {//搜索影视
                String newJson = "{\"msg\":" + j.getString("msg") +",\"code\":"+j.getInt("code")+",\"data\":"+EasyAES.getInstance().decrypt(j.getString("data")).replace("\\/", "/")+"}";
                resObj = new Gson().fromJson(newJson, SearchMovieResult.class);
            } else if ("CancleMovieCollect".equals(objName)) {//取消影视收藏
                ((CancleMovieCollect)resObj).message = j.getString("msg");
                ((CancleMovieCollect)resObj).code = j.getInt("code");
                ((CancleMovieCollect)resObj).data =  EasyAES.getInstance().decrypt(j.getString("data"));
            } else if ("MovieCollectBeen".equals(objName)) {//获取影视收藏列表
                String newJson = "{\"msg\":" + j.getString("msg") +",\"code\":"+j.getInt("code")+",\"data\":"+EasyAES.getInstance().decrypt(j.getString("data")).replace("\\/", "/")+"}";
                resObj = new Gson().fromJson(newJson, MovieCollectBeen.class);
//                LogUtil.d(XGIMILOG.getTag(), new Gson().toJson(resObj));
            } else if ("MovieByCategory".equals(objName)) {//影视筛选条件
                ((MovieByCategory)resObj).msg = j.getString("msg");
                ((MovieByCategory)resObj).code = j.getInt("code");
                ((MovieByCategory)resObj).data = new Gson().fromJson(EasyAES.getInstance().decrypt(j.getString("data")), MovieByCategory.DataBean.class);
            } else if ("AllMovie".equals(objName)) {//推荐列表以及筛选列表
                String newJson = "{\"msg\":" + j.getString("msg") +",\"code\":"+j.getInt("code")+",\"data\":"+EasyAES.getInstance().decrypt(j.getString("data")).replace("\\/", "/")+"}";
//                LogUtil.d(XGIMILOG.getTag(), "获取AllMovie : " + newJson);
                resObj = new Gson().fromJson(newJson, AllMovie.class);
            } else if ("ZhuanTiBean".equals(objName)) {
                ((ZhuanTiBean)resObj).message = j.getString("msg");
                ((ZhuanTiBean)resObj).code = j.getInt("code");
                ((ZhuanTiBean)resObj).data = new Gson().fromJson(EasyAES.getInstance().decrypt(j.getString("data")).replace("\\/", "/"), ZhuanTiBean.DataBean.class);
            } else if ("SearchBean".equals(objName)) {
                String newJson = "{\"msg\":" + j.getString("msg") +",\"code\":"+j.getInt("code")+",\"data\":"+EasyAES.getInstance().decrypt(j.getString("data"))+"}";
                resObj = new Gson().fromJson(newJson, SearchBean.class);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            value.close();
        }
        return (T) resObj;
    }

}
