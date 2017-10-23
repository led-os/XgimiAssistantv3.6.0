package com.xgimi.zhushou.widget;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.xgimi.device.device.GMDeviceController;
import com.xgimi.device.device.GMDeviceStorage;
import com.xgimi.device.device.VcontrolCmd;
import com.xgimi.device.util.GMSdkCheck;
import com.xgimi.device.vcontrolcmd.VcontrolControl;
import com.xgimi.zhushou.R;
import com.xgimi.zhushou.util.XGIMILOG;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class TurnOffDilog implements OnClickListener {
    public Dialog mDialog;
    private AnimationDrawable animationDrawable = null;
    private ImageView zidong;
    private ImageView shangxa;
    private Context mcontext;
    List<ImageView> radios = new ArrayList<ImageView>();
    List<ImageView> tuxiangMode = new ArrayList<ImageView>();
    List<RelativeLayout> rl_radios = new ArrayList<RelativeLayout>();
    private String message;
    private String[] shezi = {"取消3D", "蓝光3D", "上下3D", "左右3D", "自动3D", "2D转3D",
            "上下3D转2D", "左右3D转2D"};
    private String[] tuxiang = {"取消", "15分钟", "30分钟", "1个小时", "2个小时"};
    private Handler mHandler;

    public TurnOffDilog(Context context, String message, Handler mHandler) {
        mcontext = context;
        this.mHandler = mHandler;
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.turnoff, null);
        mDialog = new Dialog(context, R.style.dialog);
        mDialog.setContentView(view);
        mDialog.setCanceledOnTouchOutside(true);
        this.message = message;
        initView(view, message);
    }



    private void initView(View view, String messa) {
        zidong = (ImageView) view.findViewById(R.id.zidong);// 1
        shangxa = (ImageView) view.findViewById(R.id.shangxia);// 2
        ImageView zuoyou = (ImageView) view.findViewById(R.id.zuoyou);// 3
        ImageView languan = (ImageView) view.findViewById(R.id.languang);// 4


        ll_TurnOff = (LinearLayout) view.findViewById(R.id.ll_turnoff);
        ll_Dingshi = (LinearLayout) view.findViewById(R.id.dingshi);
        ImageView iv_kai = (ImageView) view.findViewById(R.id.iv_kai);
        ImageView iv_guan = (ImageView) view.findViewById(R.id.iv_guan);
        ImageView time_add = (ImageView) view.findViewById(R.id.time_add);
        ImageView time_jian = (ImageView) view.findViewById(R.id.time_jian);
        tv_status = (TextView) view.findViewById(R.id.tv_status);
        time_xianshi = (TextView) view.findViewById(R.id.time_xianshi);


        RelativeLayout rl_zidong = (RelativeLayout) view
                .findViewById(R.id.rl_zidong);
        RelativeLayout rl_shangxia = (RelativeLayout) view
                .findViewById(R.id.rl_shangxia);
        RelativeLayout rl_zuoyou = (RelativeLayout) view
                .findViewById(R.id.rl_zuoou);
//        if (GMDeviceStorage.getInstance().gmdevice != null) {
//            if ("synsepalum".equals(GMDeviceStorage.getInstance().gmdevice.devicetype) || "Z5".equals(GMDeviceStorage.getInstance().gmdevice.devicetype)) {
//                rl_zuoyou.setVisibility(View.GONE);
//            }
//        }
        RelativeLayout rl_languang = (RelativeLayout) view
                .findViewById(R.id.rl_languang);
        rl_radios.add(rl_zidong);
        rl_radios.add(rl_shangxia);
        rl_radios.add(rl_zuoyou);
        rl_radios.add(rl_languang);
        for (int i = 0; i < rl_radios.size(); i++) {
            rl_radios.get(i).setOnClickListener(this);
        }
        // zidong.setOnClickListener(this);
        // shangxa.setOnClickListener(this);
        // zuoyou.setOnClickListener(this);
        // languan.setOnClickListener(this);
        radios.add(zidong);
        radios.add(shangxa);
        radios.add(zuoyou);
        radios.add(languan);

        radios.get(0).setImageResource(R.drawable.radio);
        Button send = (Button) view.findViewById(R.id.send);
        Button quxiaofasong = (Button) view.findViewById(R.id.quxxiaofasong);

        send.setOnClickListener(this);
        quxiaofasong.setOnClickListener(this);

        ImageView cancel = (ImageView) view.findViewById(R.id.cancel);
        cancel.setOnClickListener(this);
        iv_kai.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (tv_status.getText().toString().equals("开")) {
                    tv_status.setText("关");
                } else {
                    tv_status.setText("开");
                }
            }
        });
        iv_guan.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (tv_status.getText().toString().equals("开")) {

                    tv_status.setText("关");
                } else {
                    tv_status.setText("开");
                }
            }
        });
        a = 0;
        time_add.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (a == 0) {
                    a = 6;
                }
                a--;
                time_xianshi.setText(tuxiang[a]);
            }
        });
        time_jian.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (a == 4) {
                    a = -1;
                }
                a++;
                time_xianshi.setText(tuxiang[a]);
            }
        });
    }

    public void show() {
        mDialog.show();
    }

    public void setCanceledOnTouchOutside(boolean cancel) {
        mDialog.setCanceledOnTouchOutside(cancel);
    }

    public void dismiss() {
        if (mDialog.isShowing()) {
            mDialog.dismiss();
            // animationDrawable.stop();
        }
    }

    public boolean isShowing() {
        if (mDialog.isShowing()) {
            return true;
        }
        return false;
    }

    int index;
    private int mode;
    private LinearLayout ll_TurnOff;
    private LinearLayout ll_Dingshi;
    private TextView tv_status;
    private TextView time_xianshi;
    private int a = 0;

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.cancel:
                dismiss();
                break;
            case R.id.rl_zidong:
            case R.id.zidong:
                index = 0;
//                sendTurnOff(0, null);
                ll_TurnOff.setVisibility(View.GONE);
                ll_Dingshi.setVisibility(View.GONE);
                break;
            case R.id.rl_shangxia:
            case R.id.shangxia:
                index = 1;
//                sendTurnOff(1, null);
                ll_TurnOff.setVisibility(View.GONE);
                ll_Dingshi.setVisibility(View.GONE);
                break;
            case R.id.rl_zuoou:
            case R.id.zuoyou:
                index = 2;
                ll_TurnOff.setVisibility(View.VISIBLE);
                ll_Dingshi.setVisibility(View.GONE);
                break;
            case R.id.rl_languang:
            case R.id.languang:
                index = 3;
                ll_TurnOff.setVisibility(View.GONE);
                ll_Dingshi.setVisibility(View.VISIBLE);
                break;
            case R.id.send:
                if (index == 0) {
                    GMDeviceController.getInstance().SendJC(sendTurnOff(0, null));
                } else if (index == 1) {
                    GMDeviceController.getInstance().SendJC(sendTurnOff(1, null));
                } else if (index == 2) {
                    if (tv_status.getText().toString().equals("开")) {
                        XGIMILOG.D("开关机");
                        GMDeviceController.getInstance().SendJC(sendTurnOff(4, null));
                    } else {
                        XGIMILOG.D("关光机");
                        GMDeviceController.getInstance().SendJC(sendTurnOff(2, null));
                    }
                } else if (index == 3) {
                    GMDeviceController.getInstance().SendJC(sendTurnOff(3, String.valueOf(a)));
                }
                dismiss();
                break;
            case R.id.quxxiaofasong:
                dismiss();
                break;
        }
        for (int i = 0; i < radios.size(); i++) {
            if (i == index) {
                radios.get(i).setImageResource(R.drawable.radio);
            } else {
                radios.get(i).setImageResource(0);
            }
        }
    }


    public String sendTurnOff(int mode, String delaytime) {
        int delayTime;
        if (delaytime == null) {
            delayTime = 0;
        } else {
            delayTime = Integer.valueOf(delaytime);
        }
        VcontrolControl.getInstance().sendNewCommand(
                VcontrolControl.getInstance().getConnectControl(
                new VcontrolCmd(20000, "2", GMSdkCheck.appId, null, null, new VcontrolCmd.ControlCmd(6, mode, delayTime), null, null))
        );
        if (mode == 0) {
            mHandler.sendEmptyMessageDelayed(100, 4000);
        }
        JSONObject objecet = new JSONObject();
        JSONObject js = new JSONObject();
        try {
            js.put("type", mode);
            js.put("delayTime", delaytime);
            objecet.put("data", js);
            objecet.put("action", 6);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return objecet.toString();
    }

    //显示动画
    public void disPlay() {
        TranslateAnimation mShowAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                -1.0f, Animation.RELATIVE_TO_SELF, 0.0f);
        mShowAction.setDuration(500);
        ll_TurnOff.startAnimation(mShowAction);
        ll_TurnOff.setVisibility(View.VISIBLE);
    }

    //隐藏动画
    public void hide() {
        TranslateAnimation mHiddenAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF,
                0.0f, Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                -1.0f);
        mHiddenAction.setDuration(500);
        mHiddenAction.setAnimationListener(new AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                // TODO Auto-generated method stub

            }
        });
        ll_TurnOff.startAnimation(mHiddenAction);
        ll_TurnOff.setVisibility(View.GONE);

    }
}
