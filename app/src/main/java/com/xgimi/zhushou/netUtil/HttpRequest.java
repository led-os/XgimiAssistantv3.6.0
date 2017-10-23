package com.xgimi.zhushou.netUtil;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.FileAsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.xgimi.device.util.MD5Util;
import com.xgimi.device.utils.FileUtils;
import com.xgimi.device.utils.StringUtils;
import com.xgimi.zhushou.App;
import com.xgimi.zhushou.R;
import com.xgimi.zhushou.activity.FindPassActivity;
import com.xgimi.zhushou.activity.LogoActivity;
import com.xgimi.zhushou.bean.AvatarshowMsg;
import com.xgimi.zhushou.bean.CodeMsg;
import com.xgimi.zhushou.bean.GimiUser;
import com.xgimi.zhushou.bean.HeadPhonto;
import com.xgimi.zhushou.bean.Individuality;
import com.xgimi.zhushou.bean.TvApp;
import com.xgimi.zhushou.inter.CommonCallBack;
import com.xgimi.zhushou.util.XGIMILOG;

import org.apache.http.Header;
import org.apache.http.client.params.ClientPNames;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

/**
 * Created by 霍长江 on 2016/8/17.
 */
public class HttpRequest {
    public static HttpRequest httprequest = null;
    private static AsyncHttpClient httpclient = new AsyncHttpClient();
    ;
    private Context mContext;

    private HttpRequest(Context context) {
        this.mContext = context;
    }

    public static HttpRequest getInstance(Context context) {
        if (httprequest == null) {
            httprequest = new HttpRequest(context);
        }
        return httprequest;
    }

    // 投影app相关
    public void getTouYingApp(String url, final CommonCallBack<TvApp> callbck) {

        httpclient.post(url, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
                // TODO Auto-generated method stub
                try {
                    String res = new String(arg2);
                    if (StringUtils.isEmpty(res)) {
                        return;
                    }

                    try {
                        res = URLDecoder.decode(res, "UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    Gson gson = new Gson();
                    TvApp movie = gson.fromJson(res, TvApp.class);
                    callbck.onSuccess(movie);
                } catch (Exception e) {
                    // TODO: handle exception
                }

            }

            @Override
            public void onFailure(int arg0, Header[] arg1, byte[] arg2,
                                  Throwable arg3) {
                // TODO Auto-generated method stub
            }
        });
    }

    // 上传头像
    public void upLoadTouXiang(String uid, String dizhi,
                               final CommonCallBack<HeadPhonto> callback) {
        String url = HttpUrl.user_touxiang;
        RequestParams pasParams = new RequestParams();
        pasParams.put("uid", uid);
        pasParams.put("avatar", dizhi);
        httpclient.post(url, pasParams, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
                // TODO Auto-generated method stub
                try {
                    String res = new String(arg2);
                    Gson gson = new Gson();
                    HeadPhonto movie = gson.fromJson(res, HeadPhonto.class);
                    if (movie.code == 200) {
                        callback.onSuccess(movie);
                    } else if (movie.code == 500) {
                        callback.onFailed("上传失败");
                    }
                    App app = (App) mContext.getApplicationContext();
                    if (movie.data != null) {
                        GimiUser data = app.getLoginInfo();
                        data.data.avatar = movie.data.avatar;
                        app.saveLoginInfo(data);
                    }
                } catch (Exception e) {
                    // TODO: handle exception
                }

            }

            @Override
            public void onFailure(int arg0, Header[] arg1, byte[] arg2,
                                  Throwable arg3) {
                // TODO Auto-generated method stub
                callback.onFailed("上传失败");
            }
        });

    }

    /**
     * 用户登录的post请求,登陆成功后会本地保存用户信息，注意密码需要md5加密之后请求 传过来一个handler处理结果
     *
     * @param context 上下文对象
     * @param handler 消息处理
     * @param name    用户名
     * @param pwd     待验证的密码
     */
    public void loginPost(final Context context, final Handler handler,
                          final String name, final String pwd) {

        RequestParams params = new RequestParams();
        params.put("username", name);
        params.put("pwd", MD5Util.getMD5String(pwd));

        if (netBeark(mContext)) {
            return;
        }
        httpclient.post(HttpUrl.getPostAddr(context, HttpUrl.USER_LOGIN_POST),
                params, new AsyncHttpResponseHandler() {

                    @Override
                    public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {

                        try {

                            String res = new String(arg2);

                            Log.e("loginGeturl_res", res);

                            Gson gson = new Gson();

                            GimiUser gimiUser = gson.fromJson(res,
                                    GimiUser.class);

                            if (gimiUser.code == 200) {
                                if (gimiUser.data.avatar != null) {
                                    getUrseAvatarUrl(context, gimiUser, true);
                                }

                                handler.sendEmptyMessage(LogoActivity.OK);

                                App myApp = (App) context
                                        .getApplicationContext();

                                myApp.saveLoginInfo(gimiUser);
                                myApp.SaveUserPwd(pwd);
                            } else {
                            }

                        } catch (Exception e) {
                        }

                    }

                    @Override
                    public void onFailure(int arg0, Header[] arg1, byte[] arg2,
                                          Throwable arg3) {
                        // Message message = handler.obtainMessage();
                        // if (arg3.toString().contains("UnknownHostException"))
                        // {
                        // message.arg1 = 0;
                        // }
                        //
                        // Log.e("---------UnknownHostException ",
                        // arg3.toString());
                        // handler.sendMessage(message);
                    }
                });
    }

    /**
     * 查询用户大头像地址
     *
     * @param context  上下文对象
     * @param user     用户对象
     * @param needload 是否需要加载头像
     */
    public void getUrseAvatarUrl(final Context context, final GimiUser user,
                                 final boolean needload) {

        final App myApp = (App) context.getApplicationContext();

        RequestParams params = new RequestParams();

        params.put("user_id", user.data.uid);
        if (netBeark(mContext)) {
            return;
        }
        params.put("token", user.data.token);
        params.put("size", "big");

        httpclient.post(user.data.avatar, params,
                new AsyncHttpResponseHandler() {

                    @Override
                    public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {

                        try {

                            String res = new String(arg2);

                            Gson gson = new Gson();

                            AvatarshowMsg avatarshowMsg = gson.fromJson(res,
                                    AvatarshowMsg.class);

                            if (avatarshowMsg.code == 200
                                    && !StringUtils
                                    .isEmpty(avatarshowMsg.data.avatar)) {
                                if (needload) {
                                    LoadUserAvatar(context,
                                            avatarshowMsg.data.avatar);
                                }
                                myApp.setUserAvatarUrl(avatarshowMsg.data.avatar);
                            }
                        } catch (Exception e) {
                        }
                    }

                    @Override
                    public void onFailure(int arg0, Header[] arg1, byte[] arg2,
                                          Throwable arg3) {
                    }
                });
    }

    /**
     * 直接下载用户头像原图
     *
     * @param
     */
    public void LoadUserAvatar(final Context context, final String avatarpath) {
        final App myApp = (App) context.getApplicationContext();
        if (netBeark(mContext)) {
            return;
        }
        final String path = myApp.avatarpath;
        final String path_temp = path + "_temp";

        httpclient.setTimeout(1000 * 23);
        httpclient.getHttpClient().getParams()
                .setParameter(ClientPNames.ALLOW_CIRCULAR_REDIRECTS, true);
        httpclient.get(avatarpath, new FileAsyncHttpResponseHandler(new File(
                path_temp)) {

            @Override
            public void onSuccess(int arg0, Header[] arg1, File arg2) {
                FileUtils.reNamePath(path_temp, path);
                myApp.updateAvatar();
            }

            @Override
            public void onFailure(int arg0, Header[] arg1, Throwable arg2,
                                  File arg3) {
            }
        });

    }

    /*
 * 每次请求之前检查一下网络连接状态
 */
    private boolean netBeark(Context context) {

        if (!HttpUrl.isNetworkConnected(context)) {
            // Toast.makeText(context, R.string.network_not_connected,
            // 0).show();
            return true;
        }

        return false;

    }
    // 登陆接口

    public void userLogo(final Context context, String username,
                         String password, final CommonCallBack<GimiUser> callback) {
        String url = HttpUrl.user_denglu;
        RequestParams params = new RequestParams();
        params.put("username", username);
        params.put("pwd", password);
        if (netBeark(mContext)) {
            return;
        }
        httpclient.post(url, params, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
                // TODO Auto-generated method stub
                try {
                    String res = new String(arg2);
                    Gson gson = new Gson();
                    GimiUser codemsg = gson.fromJson(res, GimiUser.class);
                    if (codemsg.code == 200) {
                        callback.onSuccess(codemsg);
                        App myApp = (App) context.getApplicationContext();
                        myApp.saveLoginInfo(codemsg);
                    } else if (codemsg.code == 500) {
                        callback.onFailed("密码或用户名错误");
                    }
                } catch (Exception e) {
                    // TODO: handle exception
                    callback.onFailed("服务器数据异常");
                }

            }

            @Override
            public void onFailure(int arg0, Header[] arg1, byte[] arg2,
                                  Throwable arg3) {
                // TODO Auto-generated method stub
                callback.onFailed(HttpUrl.failedReason(arg0));
            }
        });
    }

    // 设置用户信息
    public void setUserInforMation(String uid, String username,
                                   String password, final CommonCallBack<CodeMsg> callback) {
        String url = HttpUrl.user_information;
        RequestParams params = new RequestParams();
        params.put("uid", uid);
        params.put("username", username);
        if (netBeark(mContext)) {
            return;
        }
        params.put("password", password);
        httpclient.post(url, params, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
                // TODO Auto-generated method stub
                try {
                    String res = new String(arg2);
                    Gson gson = new Gson();
                    CodeMsg movie = gson.fromJson(res, CodeMsg.class);
                    callback.onSuccess(movie);
                } catch (Exception e) {
                    // TODO: handle exception
                }


            }

            @Override
            public void onFailure(int arg0, Header[] arg1, byte[] arg2,
                                  Throwable arg3) {
                // TODO Auto-generated method stub
                callback.onFailed(HttpUrl.failedReason(arg0));
            }
        });
    }

    // 注册接口
    public void getUserZhuCe(final Context context, String tel,
                             String yanzhengma, final CommonCallBack<GimiUser> callback) {
        String url = HttpUrl.user_zhuce;
        RequestParams params = new RequestParams();
        if (netBeark(mContext)) {
            return;
        }
        params.put("tel", tel);
        params.put("tel_area", "+86");
        params.put("validate", yanzhengma);
        httpclient.post(url, params, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
                // TODO Auto-generated method stub
                try {
                    String res = new String(arg2);
                    Gson gson = new Gson();
                    GimiUser movie = gson.fromJson(res, GimiUser.class);
                    callback.onSuccess(movie);
                    App myApp = (App) context.getApplicationContext();
                    myApp.saveLoginInfo(movie);
                } catch (Exception e) {
                    // TODO: handle exception
                    callback.onFailed("注册失败");
                }

            }

            @Override
            public void onFailure(int arg0, Header[] arg1, byte[] arg2,
                                  Throwable arg3) {
                // TODO Auto-generated method stub
                callback.onFailed(HttpUrl.failedReason(arg0));
            }
        });

    }

    // 用户接口类检验用户是否已注册
    public void getZhuCeYanZhengMa(String tel,
                                   final CommonCallBack<CodeMsg> callback) {
        String url = HttpUrl.user_get_yanzhengma;
        RequestParams params = new RequestParams();
        params.put("tel", tel);
        params.put("tel_area", "+86");
        if (netBeark(mContext)) {
            return;
        }
        httpclient.post(url, params, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
                // TODO Auto-generated method stub
                try {
                    String res = new String(arg2);
                    Gson gson = new Gson();
                    CodeMsg movie = gson.fromJson(res, CodeMsg.class);
                    callback.onSuccess(movie);
                } catch (Exception e) {
                    // TODO: handle exception
                }

            }

            @Override
            public void onFailure(int arg0, Header[] arg1, byte[] arg2,
                                  Throwable arg3) {
                // TODO Auto-generated method stub
                callback.onFailed(HttpUrl.failedReason(arg0));
            }
        });
    }

    /**
     * 忘记密码的网路请求，服务器会发出一封邮件
     *
     * @param context 上下文
     * @param handler 消息处理
     * @param email   邮箱地址
     */
    public void ForgetPwd(final Context context, final Handler handler,
                          final String email) {

        RequestParams params = new RequestParams();
        params.put("email", email);
        if (netBeark(mContext)) {
            return;
        }

        httpclient.post(HttpUrl.getPostAddr(context, HttpUrl.passwordyouxiang),
                params, new AsyncHttpResponseHandler() {

                    @Override
                    public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {

                        try {

                            String res = new String(arg2);

                            Gson gson = new Gson();

                            CodeMsg codeMsg = gson.fromJson(res, CodeMsg.class);

                            if (codeMsg.code == 200) {

                                handler.sendEmptyMessage(FindPassActivity.FORGETPWDSENDOK);

                            } else {

                                Message message = handler.obtainMessage();
                                message.what = FindPassActivity.FORGETPWDSENDFAIL;
                                message.obj = forgetPwd(codeMsg.code);

                                handler.sendMessage(message);

                            }

                        } catch (Exception e) {
                            Toast.makeText(context,
                                    R.string.xgimiservice_exception, Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(int arg0, Header[] arg1, byte[] arg2,
                                          Throwable arg3) {
                        handler.sendEmptyMessage(FindPassActivity.FORGETPWDSENDFAIL);
                    }
                });
    }

    /*
 * 忘记密码(发送密码找回邮件)
 */
    public static String forgetPwd(int code) {

        String codeExplain = "发送失败";
        switch (code) {
            case 940:
                codeExplain = "Email不存在或格式不对";
                break;
            case 945:
                codeExplain = "该Email不存在相应用户";
                break;
            case 950:
                codeExplain = "Email发送失败";
                break;
            case 804:
                codeExplain = "用户名不存在";
                break;
        }
        return codeExplain;
    }

    // 找回密码时获取验证码接口
    public void getPasswordYanzheng(String tel,
                                    final CommonCallBack<CodeMsg> callback) {
        String url = HttpUrl.user_findPassword;
        RequestParams params = new RequestParams();
        if (netBeark(mContext)) {
            return;
        }
        params.put("tel", tel);
        httpclient.post(url, params, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
                // TODO Auto-generated method stub
                try {
                    String res = new String(arg2);
                    XGIMILOG.E("找回密码验证码 : " + res);
                    Gson gson = new Gson();
                    CodeMsg movie = gson.fromJson(res, CodeMsg.class);
                    if (movie.code == 200) {
                        callback.onSuccess(movie);
                    } else {
                        callback.onFailed(movie.message);
                    }
                } catch (Exception e) {
                    // TODO: handle exception
                }

            }

            @Override
            public void onFailure(int arg0, Header[] arg1, byte[] arg2,
                                  Throwable arg3) {
                // TODO Auto-generated method stub
                callback.onFailed(HttpUrl.failedReason(arg0));
            }
        });
    }

    // 找回密码接口
    public void getPassword(final Context context, String tel, String password,
                            String val, final CommonCallBack<GimiUser> callback) {
        String url = HttpUrl.user_password;
        RequestParams params = new RequestParams();
        params.put("tel", tel);
        params.put("pwd", val);
        if (netBeark(mContext)) {
            return;
        }
        params.put("validate", password);
        httpclient.post(url, params, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
                // TODO Auto-generated method stub
                try {
                    String res = new String(arg2);
                    Gson gson = new Gson();
                    GimiUser movie = gson.fromJson(res, GimiUser.class);
                    XGIMILOG.E("----找回密码:" + res);
                    if (movie.code == 500) {
                        callback.onFailed(movie.message);
                    } else {
                        callback.onSuccess(movie);
                        App myApp = (App) context.getApplicationContext();
                        myApp.saveLoginInfo(movie);
                    }
                } catch (Exception e) {
                    // TODO: handle exception
                }

            }

            @Override
            public void onFailure(int arg0, Header[] arg1, byte[] arg2,
                                  Throwable arg3) {
                // TODO Auto-generated method stub
                callback.onFailed(HttpUrl.failedReason(arg0));
            }
        });
    }

    //第三方账号登陆
    public void thridLogo(String type, String openid, String unionid, final CommonCallBack<GimiUser> callback) {
        String url = HttpUrl.thridLogo;
        RequestParams params = new RequestParams();
        params.put("type", type);
        params.put("openid", openid);
        if (unionid != null) {
            params.put("unionid", unionid);
        }
        httpclient.post(url, params, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
                // TODO Auto-generated method stub
                try {

                    String res = new String(arg2);
                    Gson gson = new Gson();
                    XGIMILOG.E("第三方登录 : ------  " + res);
                    GimiUser codemsg = gson.fromJson(res, GimiUser.class);
                    if (codemsg.code == 200) {
                        callback.onSuccess(codemsg);
//                        App myApp = (App) mContext.getApplicationContext();
//                        myApp.saveLoginInfo(codemsg);
                    } else if (codemsg.code == 500) {
                        callback.onFailed("未注册");
                    }
                } catch (Exception e) {
                    // TODO: handle exception
                }

            }

            @Override
            public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
                // TODO Auto-generated method stub

            }
        });
    }

    public void getThridYanZhengMa(String type, String openId, String unid, String phoneNumber, String area, final CommonCallBack<CodeMsg> callback) {
        String url = HttpUrl.thridYanZhengMa;
        RequestParams params = new RequestParams();
        params.put("type", type);
        params.put("openid", openId);
        if (unid != null)
            params.put("unionid", unid);
        params.put("mobile", phoneNumber);
        params.put("mobile_area", area);
        XGIMILOG.E("url = " + url + ", parmas = " + params.toString());
        httpclient.get(url + "&" + params.toString(), new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                try {
                    String res = new String(bytes);
                    Gson gson = new Gson();
                    XGIMILOG.E("验证码请求结果 = " + res);
                    CodeMsg movie = gson.fromJson(res, CodeMsg.class);
                    if (movie.code == 200) {
                        callback.onSuccess(movie);
                    } else if (movie.code == 500) {
                        callback.onFailed(movie.message);
                    }
                } catch (Exception e) {
                }
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {

            }
        });
//        httpclient.post(url, params, new AsyncHttpResponseHandler() {
//
//            @Override
//            public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
//                try {
//                    String res = new String(arg2);
//                    Gson gson = new Gson();
//                    XGIMILOG.E("验证码请求结果 = " + res);
//                    CodeMsg movie = gson.fromJson(res, CodeMsg.class);
//                    if (movie.code == 200) {
//                        callback.onSuccess(movie);
//                    } else if (movie.code == 500) {
//                        callback.onFailed(movie.message);
//                    }
//                } catch (Exception e) {
//                }
//            }
//
//            @Override
//            public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
//            }
//        });
    }

    // 第三方账号注册
    public void thridRegist(String type, String openid, String unionid,
                            String userName, String phonenumber, String yanzhengma, String password, String area, final CommonCallBack<GimiUser> callback) {

        String url = HttpUrl.sanfang;
        RequestParams params = new RequestParams();
        params.put("type", type);
        params.put("openid", openid);
        if (unionid != null)
            params.put("unionid", unionid);
        if (userName != null)
            params.put("username", userName);
        params.put("mobile", phonenumber);
        params.put("validate", yanzhengma);
        params.put("password", password);
        params.put("mobile_area", area);

        httpclient.post(url, params, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
                // TODO Auto-generated method stub
                String res = new String(arg2);
                Gson gson = new Gson();
                GimiUser codemsg = gson.fromJson(res, GimiUser.class);
                if (codemsg != null) {
                    XGIMILOG.E("user  = " + new Gson().toJson(codemsg));
                    if (codemsg.code == 200) {
                        XGIMILOG.E("");
                        callback.onSuccess(codemsg);
                    } else if (codemsg.code == 500) {
                        XGIMILOG.E("");
                        callback.onFailed(codemsg.message);
                    } else if (codemsg.code == 0) {
                        XGIMILOG.E("");
                        callback.onFailed(codemsg.message);
                    }
                } else {
                    callback.onFailed("服务器数据库异常");
                }
            }

            @Override
            public void onFailure(int arg0, Header[] arg1, byte[] arg2,
                                  Throwable arg3) {
                callback.onFailed(arg3.getMessage());
            }
        });
    }

    //第三方账号绑定
    public void thridBangDing(String type, String openid, String unionid,
                              String userName, String password, final CommonCallBack<GimiUser> callback) {
        String url = HttpUrl.thridBangDing;
        RequestParams params = new RequestParams();
        params.put("type", type);
        params.put("openid", openid);
        if (unionid != null)
            params.put("unionid", unionid);
        params.put("username", userName);
        params.put("password", password);
        httpclient.post(url, params, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
                // TODO Auto-generated method stub
                try {
                    String res = new String(arg2);
                    Gson gson = new Gson();
                    GimiUser codemsg = gson.fromJson(res, GimiUser.class);
                    if (codemsg.code == 200) {
                        callback.onSuccess(codemsg);
                        App myApp = (App) mContext.getApplicationContext();
                        myApp.saveLoginInfo(codemsg);
                    } else if (codemsg.code == 500) {
                        callback.onFailed(codemsg.message);
                    } else if (codemsg.code == 0) {
                        callback.onFailed(codemsg.message);
                    }
                } catch (Exception e) {
                    // TODO: handle exception
                    callback.onFailed("服务器数据异常");
                }
            }

            @Override
            public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
                // TODO Auto-generated method stub

            }
        });
    }

    //影视个性推荐
    public void getIndividuality(String mac, int page, int pagesize, final CommonCallBack<Individuality> callback) {
        final String url = HttpUrl.Individuality;
        RequestParams params = new RequestParams();
        params.put("mac", mac);
        params.put("page", page);
        params.put("pageSize", pagesize);
        httpclient.post(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
                // TODO Auto-generated method stub
                try {
                    String res = new String(arg2);
                    Gson gson = new Gson();
                    Individuality indivi = gson.fromJson(res, Individuality.class);
                    if (indivi.code == 200) {
                        Log.e("laile", res);
                        Log.e("laileurl", url);
                        callback.onSuccess(indivi);
                    } else if (indivi.code == 500) {
                        callback.onFailed(indivi.message);
                    } else if (indivi.code == 0) {
                        callback.onFailed(indivi.message);
                    }
                } catch (Exception e) {
                    // TODO: handle exception
                    callback.onFailed("服务器数据异常");
                }
            }

            @Override
            public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
                // TODO Auto-generated method stub

            }
        });
    }

    /**
     * 下载网络图片
     *
     * @param handler   消息处理
     * @param imagepath 图片的网络地址
     */
    public void downloadImage(final Handler handler, final String imagepath) {
        String save_path = imagepath.substring(imagepath.lastIndexOf("/") + 1,
                imagepath.length());
        if (netBeark(mContext)) {
            return;
        }

        final File localfile = FileUtils.createFile(App.getContext().ExternalImageDir,
                save_path);
        httpclient.setTimeout(1000 * 15);
        httpclient.get(imagepath, new FileAsyncHttpResponseHandler(localfile) {

            @Override
            public void onSuccess(int arg0, Header[] arg1, File arg2) {
                Message message = handler.obtainMessage(101);
                message.obj = localfile.getAbsolutePath();
                handler.sendMessage(message);
            }

            @Override
            public void onFailure(int arg0, Header[] arg1, Throwable arg2,
                                  File arg3) {
            }
        });
    }


}
