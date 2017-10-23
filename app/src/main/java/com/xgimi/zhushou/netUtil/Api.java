package com.xgimi.zhushou.netUtil;

import com.xgimi.zhushou.BuildConfig;
import com.xgimi.zhushou.aes.EasyAES;
import com.xgimi.zhushou.aes.MyAesToGsonFactory;
import com.xgimi.zhushou.App;
import com.xgimi.zhushou.bean.CollectNum;
import com.xgimi.zhushou.netUtil.inter.ApiServer;
import com.xgimi.zhushou.util.XGIMILOG;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by 霍长江 on 2016/8/5.
 */
public class Api {

    private static final String TAG = "Api";

    private Api() {
    }

    private static final String CACHE_CONTROL = "Cache-Control";
    private static final Object monitor = new Object();
    private static final Object monitor2 = new Object();
    private static final Object monitors = new Object();
    private static final long OK_HTTP_TIME_OUT = 20000;
    private static final Interceptor REWRITE_CACHE_CONTROL_INTERCEPTOR = new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Response originalResponse = chain.proceed(chain.request());
            if (NetUtil.isNetConnected(App.getContext())) {
                int maxAge = 0; // 在线缓存在1分钟内可读取
                return originalResponse.newBuilder()
                        .removeHeader("Pragma")
                        .removeHeader(CACHE_CONTROL)
                        .header(CACHE_CONTROL, "public, max-age=" + maxAge)
                        .build();
            } else {
                int maxStale = 60 * 60 * 24 * 28; // 离线时缓存保存4周
                return originalResponse.newBuilder()
                        .removeHeader("Pragma")
                        .removeHeader(CACHE_CONTROL)
                        .header(CACHE_CONTROL, "public, only-if-cached, max-stale=" + maxStale)
                        .build();
            }
        }
    };

    private static File httpCacheDirectory = new File(App.getContext().getCacheDir(), "mangocache");
    private static int cacheSize = 10 * 1024 * 1024; // 10 MiB
    private static Cache cache = new Cache(httpCacheDirectory, cacheSize);
    private static OkHttpClient client = new OkHttpClient.Builder()
//            .addNetworkInterceptor(REWRITE_CACHE_CONTROL_INTERCEPTOR)
//            .addInterceptor(REWRITE_CACHE_CONTROL_INTERCEPTOR)//不添加离线缓存无效
//            .cache(cache)
            .build();

    private static OkHttpClient mXgimiVideoClient;
// =  new OkHttpClient.Builder().connectTimeout(OK_HTTP_TIME_OUT, TimeUnit.SECONDS).build();

    private static ApiServer mangoApi = null;
    private static ApiServer mXgimiVideoApi = null;


    public static ApiServer getMangoApi() {
        synchronized (monitor) {
            if (mangoApi == null) {
                mangoApi = new Retrofit.Builder()
                        .baseUrl("http://api.xgimi.com/apis/")
                        .client(client)
                        .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                        .addConverterFactory(GsonConverterFactory.create())
                        .build().create(ApiServer.class);
            }
            return mangoApi;
        }
    }

    public static ApiServer getXgimiVideoApi(MyAesToGsonFactory factory) {

        synchronized (monitor2) {
            if (mXgimiVideoClient == null) {
                OkHttpClient.Builder builder = new OkHttpClient.Builder();
                if (BuildConfig.DEBUG) {
                    // https://drakeet.me/retrofit-2-0-okhttp-3-0-config
                    HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
                        @Override
                        public void log(String message) {
                            //打印retrofit日志
//                            XGIMILOG.D("retrofitBack = " + message);
                        }
                    });
                    loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
                    builder.addInterceptor(loggingInterceptor);
                }
//                mXgimiVideoClient = builder.connectTimeout(OK_HTTP_TIME_OUT, TimeUnit.SECONDS).build();
                mXgimiVideoClient = builder.build();
            }
            if (mXgimiVideoApi == null) {
                mXgimiVideoApi = new Retrofit.Builder()
                        .baseUrl("https://screenapi.xgimi.com/v3/")
//                        .baseUrl("http://screenapi.t.xgimi.com/v3/")
                        .client(mXgimiVideoClient)
                        .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                        .addConverterFactory(factory)
                        .build()
                        .create(ApiServer.class);
            }
        }
        return mXgimiVideoApi;
    }

//    public static long test() {
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
//        sdf.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));//设置TimeZone为上海时间
//        Date now = new Date();//获取本地时间
//        try {
//            now = sdf.parse(sdf.format(now));//将本地时间转换为转换时间为东八区
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
////        XGIMILOG.D("时间 : " + (now.getTime() / 1000));
//        return now.getTime();
//    }

    public static String getEncodeParam(String[] keys, String[] values) throws Exception {
        if (keys.length != values.length) {
            throw new Exception("keySize != valueSize");
        }
        JSONObject j = new JSONObject();
        try {
            for (int i = 0; i < keys.length; i++) {
                if ("page".equals(keys[i])) {
                    j.put("page", Integer.valueOf(values[i]));
                } else if ("category_id".equals(keys[i])) {
                    j.put("category_id", Integer.valueOf(values[i]));
                } else if ("id".equals(keys[i])) {
                    j.put("id", Integer.valueOf(values[i]));
                } else {
                    j.put(keys[i], values[i]);
                }
            }
//            XGIMILOG.D((System.currentTimeMillis() / 1000) + ", " + (test() / 1000));
            j.put("time", String.valueOf(System.currentTimeMillis() / 1000));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String res = URLEncoder.encode(EasyAES.getInstance().encrypt(j.toString()), "UTF-8");
//        XGIMILOG.D("未加密参数 : " + j.toString()
//                + ", 加密参数 : " + EasyAES.getInstance().encrypt(j.toString()).trim()
//                + ",urlEncode : " + res
//        );
        return EasyAES.getInstance().encrypt(j.toString().trim());
    }
}
