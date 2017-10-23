package com.xgimi.zhushou.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.controller.listener.SocializeListeners.UMAuthListener;
import com.umeng.socialize.controller.listener.SocializeListeners.UMDataListener;
import com.umeng.socialize.exception.SocializeException;
import com.umeng.socialize.sso.SinaSsoHandler;
import com.umeng.socialize.sso.UMQQSsoHandler;
import com.umeng.socialize.sso.UMSsoHandler;
import com.umeng.socialize.weixin.controller.UMWXHandler;
import com.xgimi.device.utils.StringUtils;
import com.xgimi.zhushou.App;
import com.xgimi.zhushou.BaseActivity;
import com.xgimi.zhushou.R;
import com.xgimi.zhushou.bean.GimiUser;
import com.xgimi.zhushou.inter.CommonCallBack;
import com.xgimi.zhushou.netUtil.HttpRequest;
import com.xgimi.zhushou.util.Constant;
import com.xgimi.zhushou.util.FindPassWord;
import com.xgimi.zhushou.util.ToastUtil;
import com.xgimi.zhushou.util.ToosUtil;
import com.xgimi.zhushou.util.XGIMILOG;

import java.util.Map;
import java.util.Set;

public class LogoActivity extends BaseActivity implements OnClickListener,
        OnTouchListener {

    public static final int OK = 1;
    private EditText name;
    private EditText password;
    private Button denglu;
    Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case OK:
                    MissDilog();
                    finish();
                    break;
                default:
                    break;
            }
        }
    };
    private TextView zuce;
    private ImageView back;
    private ImageView iv_remount;
    private ImageView fl_one;
    private UMSocialService mController;
    private String weiboOpenid;

    private String unid;

    private Handler mCancelDialogHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            MissDilog();
            ToastUtil.getToast("操作超时", LogoActivity.this).show();
        }
    };


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logo);
        initView();
        initData();
    }

    @Override
    protected void onStop() {
        super.onStop();

    }

    private void initView() {
        mController = UMServiceFactory.getUMSocialService("com.umeng.login");

        UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler(this, "1104491791",
                "DIeHXRrC1DCVn3ru");
        qqSsoHandler.addToSocialSDK();

        // 添加微信平台
        UMWXHandler wxHandler = new UMWXHandler(this, "wxdf24aa8a5e969fe2", "19a84011e151450ae8cd0668999730e0");
        wxHandler.addToSocialSDK();


        mController.getConfig().setSsoHandler(new SinaSsoHandler());

        iv_remount = (ImageView) findViewById(R.id.local_yingyong_title)
                .findViewById(R.id.iv_remount);
        setYaokongBackground(iv_remount, this, "qita");
        if (Constant.netStatus) {
            iv_remount.setImageResource(R.drawable.yaokongqi);
        } else {
            iv_remount.setImageResource(R.drawable.gimi_yaokong);
        }
        back = (ImageView) findViewById(R.id.local_yingyong_title).findViewById(R.id.back);
        back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (inputMethodManager.isActive()) {
                    XGIMILOG.E("inputMethodManager.isActive()");
                    if (name.hasFocus()) {
                        inputMethodManager.hideSoftInputFromWindow(
                                name.getApplicationWindowToken(), 0);
                    }
                    if (password.hasFocus()) {
                        inputMethodManager.hideSoftInputFromWindow(
                                password.getApplicationWindowToken(), 0);
                    }
                }
                finish();
            }
        });
//        back(back);
        back.setOnTouchListener(this);
        iv_remount.setOnTouchListener(this);
        TextView local_title = (TextView) findViewById(
                R.id.local_yingyong_title).findViewById(R.id.tv_titile);
        local_title.setText(getString(R.string.denglu));
        name = (EditText) findViewById(R.id.name);
        password = (EditText) findViewById(R.id.password);
        denglu = (Button) findViewById(R.id.denglu);
        denglu.setOnTouchListener(this);
        zuce = (TextView) findViewById(R.id.zhuce);
        TextView findpass = (TextView) findViewById(R.id.findpassword);
        findpass.setOnClickListener(this);

        fl_one = (ImageView) findViewById(R.id.fl_one);
        ImageView fl_two = (ImageView) findViewById(R.id.fl_two);
        ImageView fl_three = (ImageView) findViewById(R.id.fl_three);
        fl_three.setOnClickListener(this);
        fl_two.setOnClickListener(this);
        fl_one.setOnClickListener(this);
    }

    private void initData() {
        denglu.setOnClickListener(this);
        zuce.setOnClickListener(this);
    }

    private void Logo(String name, String password) {
        HttpRequest.getInstance(this).loginPost(this, handler, name, password);
        showDilog("登录中,请稍后...");
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        final String user_name = name.getText().toString().trim();
        final String user_password = password.getText().toString().trim();
        switch (v.getId()) {
            case R.id.denglu:
                ToosUtil.getInstance().addEventUmeng(this, "event_login");
                if (StringUtils.isEmpty(user_name)) {
                    Toast.makeText(this, "请输入账号", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (StringUtils.isEmpty(user_password)) {
                    Toast.makeText(this, "请输入密码", Toast.LENGTH_SHORT).show();
                    return;
                }
                showDilog("请稍后...");
                HttpRequest.getInstance(this).userLogo(this, user_name,
                        user_password, new CommonCallBack<GimiUser>() {

                            @Override
                            public void onSuccess(GimiUser data) {
                                // TODO Auto-generated method stub
                                MissDilog();
                                App.getContext().saveLoginInfo(data);
                                finish();
                            }

                            @Override
                            public void onStart() {
                                // TODO Auto-generated method stub

                            }

                            @Override
                            public void onFailed(String reason) {
                                // TODO Auto-generated method stub
                                MissDilog();
                                Toast.makeText(LogoActivity.this, reason, Toast.LENGTH_SHORT).show();
                            }
                        });
                // Logo(user_name,user_password);
                break;
            case R.id.zhuce:
                ToosUtil.getInstance().addEventUmeng(this, "event_regist");

                Intent intent = new Intent(this, PhoneRegisterActivity.class);
                startActivity(intent);
                break;
            case R.id.findpassword:
                FindPassWord dilog = new FindPassWord(LogoActivity.this, "a");
                dilog.show();
                break;
            case R.id.fl_one:
                showDilog("登录中...");
                ToosUtil.getInstance().addEventUmeng(this, "event_weixin_login");
                mController.doOauthVerify(this, SHARE_MEDIA.WEIXIN, new UMAuthListener() {
                    @Override
                    public void onStart(SHARE_MEDIA platform) {
                        XGIMILOG.E("---------------");
                        mCancelDialogHandler.removeMessages(0);
                        mCancelDialogHandler.sendEmptyMessageDelayed(0, 15000);
                    }

                    @Override
                    public void onError(SocializeException e, SHARE_MEDIA platform) {
                        XGIMILOG.E("---------------");
                        MissDilog();
                        mCancelDialogHandler.removeMessages(0);
                    }

                    @Override
                    public void onComplete(Bundle value, SHARE_MEDIA platform) {

                        mCancelDialogHandler.removeMessages(0);

                        //获取相关授权信息
                        mController.getPlatformInfo(LogoActivity.this, SHARE_MEDIA.WEIXIN, new UMDataListener() {
                            @Override
                            public void onStart() {
                            }

                            @Override
                            public void onComplete(int status, Map<String, Object> info) {
                                if (status == 200 && info != null) {
                                    StringBuilder sb = new StringBuilder();
                                    Set<String> keys = info.keySet();
                                    String unid = info.get("unionid").toString();
                                    String openid = info.get("openid").toString();
                                    XGIMILOG.E("unid = " + unid + ", openid = " + openid);
                                    denglu("weixin", openid, unid);
                                } else {
                                    Log.e("info", "发生错误：" + status);
                                }
                            }
                        });
                    }

                    @Override
                    public void onCancel(SHARE_MEDIA platform) {
                        XGIMILOG.E("---------------");
                        MissDilog();
                        mCancelDialogHandler.removeMessages(0);
                    }
                });
                break;
            case R.id.fl_two:
                showDilog("登录中...");
                ToosUtil.getInstance().addEventUmeng(this, "event_qq_login");

                mController.doOauthVerify(this, SHARE_MEDIA.QQ,
                        new UMAuthListener() {
                            @Override
                            public void onStart(SHARE_MEDIA platform) {
                            }

                            @Override
                            public void onError(SocializeException e,
                                                SHARE_MEDIA platform) {
                                MissDilog();
                            }

                            @Override
                            public void onComplete(Bundle value,
                                                   SHARE_MEDIA platform) {
                                String openId = value.getString("openid");
                                XGIMILOG.E("qq--- openid = " + openId);
                                denglu("qq", openId, null);
                                mController.getPlatformInfo(LogoActivity.this,
                                        SHARE_MEDIA.QQ, new UMDataListener() {
                                            @Override
                                            public void onStart() {
                                            }

                                            @Override
                                            public void onComplete(int status,
                                                                   Map<String, Object> info) {
                                                if (status == 200 && info != null) {
                                                    StringBuilder sb = new StringBuilder();
                                                    Set<String> keys = info
                                                            .keySet();
                                                    for (String key : keys) {
                                                        sb.append(key
                                                                + "="
                                                                + info.get(key)
                                                                .toString()
                                                                + "\r\n");
                                                    }
//												 denglu.setText(sb.toString());
//												Log.d("TestData", sb.toString());
                                                } else {
                                                    Log.d("TestData", "发生错误："
                                                            + status);
                                                }
                                            }
                                        });
                            }

                            @Override
                            public void onCancel(SHARE_MEDIA platform) {
                                MissDilog();
                            }
                        });

                break;
            case R.id.fl_three:
                showDilog("登录中...");
                ToosUtil.getInstance().addEventUmeng(this, "event_weibo_login");

                mController.doOauthVerify(this, SHARE_MEDIA.SINA,
                        new UMAuthListener() {
                            @Override
                            public void onError(SocializeException e,
                                                SHARE_MEDIA platform) {
                                MissDilog();
                            }

                            @Override
                            public void onComplete(Bundle value,
                                                   SHARE_MEDIA platform) {
                                if (value != null
                                        && !TextUtils.isEmpty(value
                                        .getString("uid"))) {

                                    mController.getPlatformInfo(LogoActivity.this,
                                            SHARE_MEDIA.SINA, new UMDataListener() {
                                                @Override
                                                public void onStart() {
                                                }

                                                @Override
                                                public void onComplete(int status,
                                                                       Map<String, Object> info) {
                                                    if (status == 200
                                                            && info != null) {
                                                        StringBuilder sb = new StringBuilder();
                                                        Set<String> keys = info
                                                                .keySet();
                                                        weiboOpenid = info.get("uid").toString();
                                                        XGIMILOG.E("weibo---- , openID = " + weiboOpenid);
                                                        denglu("weibo", weiboOpenid, null);

                                                        Log.d("TestData",
                                                                sb.toString());
                                                    } else {
                                                        Log.d("TestData", "发生错误："
                                                                + status);
                                                    }
                                                }
                                            });

                                } else {
                                    MissDilog();
                                }
                            }

                            @Override
                            public void onCancel(SHARE_MEDIA platform) {
                                MissDilog();
                            }

                            @Override
                            public void onStart(SHARE_MEDIA platform) {
                            }
                        });
                break;
            default:
                break;
        }
    }


    //第三方登陆
    public void denglu(final String type, final String opiend, final String unid) {
        HttpRequest.getInstance(LogoActivity.this).thridLogo(type, opiend, unid, new CommonCallBack<GimiUser>() {

            @Override
            public void onStart() {
            }

            @Override
            public void onSuccess(GimiUser data) {
                XGIMILOG.E("第三方登录成功 : " + new Gson().toJson(data));
                if (data != null && data.data != null && data.code == 200) {
                    MissDilog();
                    App.getContext().saveLoginInfo(data);
                    finish();
                }
            }

            @Override
            public void onFailed(String reason) {
                XGIMILOG.E("第三方登录失败 : " + reason);
                MissDilog();
                XGIMILOG.E("第三方登录 : type = " + type + ", openid = " + opiend + ", unid = " + unid);
                Intent intent = new Intent(LogoActivity.this, ThridRegistActivity.class);
                intent.putExtra("type", type);
                intent.putExtra("openid", opiend);
                intent.putExtra("unid", unid);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (v.getId()) {
            case R.id.iv_user:
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        back.setAlpha(0.6f);
                        break;
                    case MotionEvent.ACTION_UP:
                        back.setAlpha(1.0f);
                        break;
                }
                break;
            case R.id.iv_remount:
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        iv_remount.setAlpha(0.6f);
                        break;
                    case MotionEvent.ACTION_UP:
                        iv_remount.setAlpha(1.0f);
                        break;
                }
                break;
            case R.id.denglu:
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        denglu.setAlpha(0.6f);
                        break;
                    case MotionEvent.ACTION_UP:
                        denglu.setAlpha(1.0f);
                        break;
                }

                break;
        }
        return false;
    }


    @Override
    protected void onActivityResult(int arg0, int arg1, Intent arg2) {
        // TODO Auto-generated method stub
        super.onActivityResult(arg0, arg1, arg2);
        UMSsoHandler ssoHandler = mController.getConfig().getSsoHandler(arg0);
        if (ssoHandler != null) {
            ssoHandler.authorizeCallBack(arg0, arg1, arg2);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Constant.netStatus) {
            iv_remount.setImageResource(R.drawable.yaokongqi);
        } else {
            iv_remount.setImageResource(R.drawable.gimi_yaokong);

        }
    }
}
