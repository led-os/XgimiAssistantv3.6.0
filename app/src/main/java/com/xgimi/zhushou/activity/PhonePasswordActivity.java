package com.xgimi.zhushou.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.xgimi.zhushou.BaseActivity;
import com.xgimi.zhushou.R;
import com.xgimi.zhushou.bean.CodeMsg;
import com.xgimi.zhushou.bean.GimiUser;
import com.xgimi.zhushou.inter.CommonCallBack;
import com.xgimi.zhushou.netUtil.HttpRequest;
import com.xgimi.zhushou.util.Constant;
import com.xgimi.zhushou.util.RegexUtils;
import com.xgimi.zhushou.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PhonePasswordActivity extends BaseActivity implements OnTouchListener {
    private EditText et;
    private Button zhuce;
    private String phoneNumber;
    private int buzhou = 1;
    List<TextView> tvs = new ArrayList<TextView>();
    private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {

            switch (msg.what) {
                case 0:
                    for (int i = 0; i < 2; i++) {
                        TextView textView = tvs.get(i);
                        if (i == 1) {
                            textView.setTextColor(Color.parseColor("#4392f3"));
                        } else {
                            textView.setTextColor(Color.parseColor("#919191"));

                        }
                    }
                    rl_password.setVisibility(View.VISIBLE);
                    et.setText("");
                    et.setHint("请输入验证码");
                    zhuce.setText("完成");
                    break;
            }
        }

    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);
        initView();
        initData();
    }

    private void initView() {
        random = new Random();


        iv_remount = (ImageView) findViewById(R.id.title).findViewById(R.id.iv_remount);
        setYaokongBackground(iv_remount, this, "qita");
        if (Constant.netStatus) {
            iv_remount.setImageResource(R.drawable.yaokongqi);
        } else {
            iv_remount.setImageResource(R.drawable.gimi_yaokong);
        }
        back = (ImageView) findViewById(R.id.title).findViewById(R.id.back);
        back(back);
        back.setOnTouchListener(this);
        iv_remount.setOnTouchListener(this);
        TextView title = (TextView) findViewById(R.id.tv_titile);
        title.setText("找回密码");
        et = (EditText) findViewById(R.id.name);
        zhuce = (Button) findViewById(R.id.zhuce);
        one = (TextView) findViewById(R.id.one).findViewById(R.id.tv);
        two = (TextView) findViewById(R.id.two).findViewById(R.id.tv);
        rl_password = (RelativeLayout) findViewById(R.id.password);
        password = (EditText) findViewById(R.id.name_password);
        two.setText("2.输入验证码");
        tvs.add(one);
        tvs.add(two);
        tvs.get(0).setTextColor(Color.parseColor("#4392f3"));
        view_xian = findViewById(R.id.view_xian);

        quxyu = (Button) findViewById(R.id.quyu);
        xuxian = (ImageView) findViewById(R.id.user_one);
        zhuce.setOnTouchListener(this);
    }

    private void initData() {
        zhuce.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                String edittextString = et.getText().toString().trim();
                if (buzhou == 1) {
                    if (StringUtils.isEmpty(edittextString)) {
                        Toast.makeText(PhonePasswordActivity.this, "请输入手机号", Toast.LENGTH_SHORT).show();
                    } else {
                        showDilog("获取中...");
                        getYanZhengMa(phoneNumber);
                    }
                } else if (buzhou == 2) {
                    if (StringUtils.isEmpty(edittextString) || StringUtils.isEmpty(password.getText().toString().trim())) {
                        Toast.makeText(PhonePasswordActivity.this, "请输入验证码", Toast.LENGTH_SHORT).show();
                    } else {
                        showDilog("获取中...");
                        zhuce(phoneNumber, et.getText().toString().trim(), password.getText().toString().trim());
                    }
                }
            }
        });
        et.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
                String edittextString = et.getText().toString().trim();
                if (RegexUtils.checkMobile(edittextString)) {
                    zhuce.setBackgroundResource(R.drawable.logo);
                    phoneNumber = edittextString;
                } else if (!RegexUtils.checkMobile(edittextString)) {
                    if (buzhou == 1) {
                        zhuce.setBackgroundResource(R.drawable.weixuanzhonglogo);
                    }
                }
            }
        });
    }

    public void getYanZhengMa(final String tel) {
        HttpRequest.getInstance(this).getPasswordYanzheng(tel, new CommonCallBack<CodeMsg>() {

            @Override
            public void onSuccess(CodeMsg data) {
                // TODO Auto-generated method stub
                MissDilog();
                if (data.code == 200) {
                    buzhou = 2;
                    view_xian.setVisibility(View.GONE);
                    quxyu.setVisibility(View.GONE);
                    xuxian.setVisibility(View.VISIBLE);
                }
                mHandler.sendEmptyMessage(0);
            }

            @Override
            public void onStart() {
                // TODO Auto-generated method stub

            }

            @Override
            public void onFailed(String reason) {
                // TODO Auto-generated method stub
                MissDilog();
                Toast.makeText(PhonePasswordActivity.this, reason, Toast.LENGTH_SHORT).show();
            }
        });
    }

    GimiUser user;
    private TextView one;
    private TextView two;
    private TextView three;
    private Random random;
    private EditText password;
    private RelativeLayout rl_password;
    private View view_xian;
    private Button quxyu;
    private ImageView xuxian;
    private ImageView iv_remount;
    private ImageView back;

    //zhuce
    public void zhuce(String tel, String yanzhengma, String mima) {
        HttpRequest.getInstance(this).getPassword(this, tel, yanzhengma, mima, new CommonCallBack<GimiUser>() {

            @Override
            public void onSuccess(GimiUser data) {
                // TODO Auto-generated method stub
                MissDilog();
//				Intent Intent=new Intent(PhonePasswordActivity.this,FindFragment.class);
//				startActivity(Intent);
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
                Toast.makeText(PhonePasswordActivity.this, reason, Toast.LENGTH_SHORT).show();
            }
        });
    }

    //设置用户信息
    public void setUserInformation(String uid, String username, String password) {
        HttpRequest.getInstance(this).setUserInforMation(uid, username, password, new CommonCallBack<CodeMsg>() {
            @Override
            public void onSuccess(CodeMsg data) {
                // TODO Auto-generated method stub
                MissDilog();
//				Intent Intent=new Intent(PhonePasswordActivity.this,FindFragment.class);
//				startActivity(Intent);
                finish();
            }

            @Override
            public void onStart() {
                // TODO Auto-generated method stub
            }

            @Override
            public void onFailed(String reason) {
                // TODO Auto-generated method stub

            }
        });
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        // TODO A
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
            case R.id.zhuce:
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        zhuce.setAlpha(0.6f);
                        break;
                    case MotionEvent.ACTION_UP:
                        zhuce.setAlpha(1.0f);
                        break;
                }
                break;
        }
        return false;
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
