package com.xgimi.zhushou.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.xgimi.zhushou.App;
import com.xgimi.zhushou.BaseActivity;
import com.xgimi.zhushou.R;
import com.xgimi.zhushou.bean.CodeMsg;
import com.xgimi.zhushou.bean.GimiUser;
import com.xgimi.zhushou.inter.CommonCallBack;
import com.xgimi.zhushou.netUtil.HttpRequest;
import com.xgimi.zhushou.util.ThridLogo;
import com.xgimi.zhushou.util.XGIMILOG;

import java.net.URLEncoder;
import java.util.Timer;
import java.util.TimerTask;

public class ThridRegistActivity extends BaseActivity implements OnClickListener {

    private EditText phoneNumber;
    private EditText yanzhengma;
    private EditText passWord;
    private String openId;
    private String type;
    private String unid;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thrid_regist);
        initExras();
        initView();
        initData();
    }

    private void initExras() {
        Intent intent = getIntent();
        if (intent != null) {
            openId = intent.getStringExtra("openid");
            type = intent.getStringExtra("type");
            if (type.equals("weixin")) {
                unid = intent.getStringExtra("unid");
            }
        }
    }

    private void initView() {

        controlTitle(findViewById(R.id.id_toolbar), true, true, false, false);
        tv.setText("手机号绑定");
        phoneNumber = (EditText) findViewById(R.id.phone);
        yanzhengma = (EditText) findViewById(R.id.yanzheng);
        passWord = (EditText) findViewById(R.id.password);
        getYanZhengMa = (Button) findViewById(R.id.button);
        Button zhuce = (Button) findViewById(R.id.zhuce);
        zhuce.setOnClickListener(this);
        getYanZhengMa.setOnClickListener(this);
    }

    private void initData() {

    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        String phonen = phoneNumber.getText().toString().trim();
        String yanzheng = yanzhengma.getText().toString().trim();
        String pass = passWord.getText().toString().trim();
        switch (v.getId()) {
            case R.id.button:
                if (TextUtils.isEmpty(phonen)) {
                    Toast.makeText(this, "请输入手机号", Toast.LENGTH_SHORT).show();
                    return;
                }
                HttpRequest.getInstance(this).getThridYanZhengMa(type, openId, unid, phonen, URLEncoder.encode("+86"), new CommonCallBack<CodeMsg>() {

                    @Override
                    public void onStart() {

                    }

                    @Override
                    public void onSuccess(CodeMsg data) {
                        XGIMILOG.E("发送验证码请求结果 : " + new Gson().toJson(data));
                        if (data != null) {
                            if (data.code == 200) {
                                Toast.makeText(ThridRegistActivity.this, "发送成功", Toast.LENGTH_SHORT).show();
                                statrTimer();
                            }
                        }
                    }

                    @Override
                    public void onFailed(String reason) {
                        Toast.makeText(ThridRegistActivity.this, reason, Toast.LENGTH_SHORT).show();
//                        if (reason.equals("发送失败")) {
//                            Toast.makeText(ThridRegistActivity.this, "验证码获取失败", Toast.LENGTH_SHORT).show();
//                        } else if (reason.equals("已注册")) {
//                            Toast.makeText(ThridRegistActivity.this, "该手机号已注册", Toast.LENGTH_SHORT).show();
//                            handler.sendEmptyMessage(1);
//                        } else if (reason.equals("手机号格式错误")) {
//                            Toast.makeText(ThridRegistActivity.this, reason, Toast.LENGTH_SHORT).show();
//                        }
                    }
                });
                break;
            case R.id.zhuce:
                if (TextUtils.isEmpty(phonen)) {
                    Toast.makeText(this, "请输入手机号", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(yanzheng)) {
                    Toast.makeText(this, "请输入验证码", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(yanzheng)) {
                    Toast.makeText(this, "请输入密码", Toast.LENGTH_SHORT).show();
                    return;
                }
                showDilog("绑定中...");
                HttpRequest.getInstance(ThridRegistActivity.this).thridRegist(type, openId, unid, null, phonen, yanzheng, pass, "+86", new CommonCallBack<GimiUser>() {
                    @Override
                    public void onSuccess(GimiUser data) {
                        // TODO Auto-generated method stub
                        MissDilog();
                        if (data.code == 200) {
                            XGIMILOG.E("");
                            Toast.makeText(ThridRegistActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                            App.getContext().saveLoginInfo(data);
                            finish();
                        } else {
                            Toast.makeText(ThridRegistActivity.this, "登录失败 : " + data.message, Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onStart() {

                    }

                    @Override
                    public void onFailed(String reason) {
                        MissDilog();
                        Toast.makeText(ThridRegistActivity.this, "登录失败 : " + reason, Toast.LENGTH_SHORT).show();
                        XGIMILOG.E("" + reason);
                    }
                });
                break;

            default:
                break;
        }
    }

    private Timer timer;
    private TimerTask task;

    public void statrTimer() {
        if (timer == null)
            timer = new Timer();
        getYanZhengMa.setClickable(false);

        if (task == null) {
            task = new TimerTask() {
                @Override
                public void run() {
                    handler.sendEmptyMessage(0);
                }
            };

            if (timer != null && task != null) {
                timer.schedule(task, 0, 1000);
            }

        }
    }

    int a = 60;
    Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 0:
                    a--;
                    getYanZhengMa.setText("请" + a + "秒后重发");
                    if (a <= 0) {
                        stopTimer();
                        a = 60;
                        getYanZhengMa.setText("重新发送");
                        getYanZhengMa.setClickable(true);
                    }
                    break;
                case 1:
                    getYanZhengMa.setText("重新发送");
                    getYanZhengMa.setClickable(false);
                    showLogoDilog();
                    logo.setLisener(new ThridLogo.RegistLisener() {
                        @Override
                        public void succes() {
                            // TODO Auto-generated method stub
                            MissDilog();
//						Intent intent=new Intent(ThridRegistActivity.this,FindFragment.class);
//						startActivity(intent);
                            finish();
                        }

                        @Override
                        public void onstart() {
                            // TODO Auto-generated method stub
                            showDilog("绑定中...");
                        }

                        @Override
                        public void fail(String reason) {
                            // TODO Auto-generated method stub
                            MissDilog();
                            Toast.makeText(ThridRegistActivity.this, reason, Toast.LENGTH_SHORT).show();
                            showLogoDilog();
                        }
                    });
                    break;
                default:
                    break;
            }
        }
    };
    private Button getYanZhengMa;
    private ThridLogo logo;

    public void stopTimer() {
        getYanZhengMa.setClickable(true);
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        if (task != null) {
            task.cancel();
            task = null;
        }
    }


    private void showLogoDilog() {
        if (logo == null)
            logo = new ThridLogo(ThridRegistActivity.this, type, openId, unid, phoneNumber.getText().toString().trim());
        logo.show();
    }

}
