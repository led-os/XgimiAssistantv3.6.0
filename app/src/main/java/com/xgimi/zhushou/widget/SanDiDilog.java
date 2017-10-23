package com.xgimi.zhushou.widget;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.xgimi.device.device.VcontrolCmd;
import com.xgimi.device.util.GMSdkCheck;
import com.xgimi.device.vcontrolcmd.VcontrolControl;
import com.xgimi.zhushou.R;
import com.xgimi.zhushou.util.XGIMILOG;

import java.util.ArrayList;
import java.util.List;

public class SanDiDilog implements OnClickListener {
    public Dialog mDialog;
    private AnimationDrawable animationDrawable = null;
    private ImageView zidong;
    private ImageView shangxa;
    private Context mcontext;
    List<ImageView> radios = new ArrayList<ImageView>();
    List<ImageView> tuxiangMode = new ArrayList<ImageView>();
    List<RelativeLayout> rl_radios = new ArrayList<RelativeLayout>();
    List<RelativeLayout> rl_tuxiangMode = new ArrayList<RelativeLayout>();
    private String message;
    private String[] shezi = {"取消3D", "蓝光3D", "上下3D", "左右3D", "自动3D", "2D转3D",
            "上下3D转2D", "左右3D转2D"};
    private String[] tuxiang = {"明亮", "标准", "柔和", "自然", "自定义"};

    public SanDiDilog(Context context, String message) {
        mcontext = context;
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.sandidilog, null);
        mDialog = new Dialog(context, R.style.dialog);
        mDialog.setContentView(view);
        mDialog.setCanceledOnTouchOutside(true);
        this.message = message;
        initView(view, message);
    }

    private void initView(View view, String messa) {
        zidong = (ImageView) view.findViewById(R.id.zidong);
        shangxa = (ImageView) view.findViewById(R.id.shangxia);
        ImageView zuoyou = (ImageView) view.findViewById(R.id.zuoyou);
        ImageView languan = (ImageView) view.findViewById(R.id.languang);
        ImageView sxz = (ImageView) view.findViewById(R.id.sxz);
        ImageView zyz = (ImageView) view.findViewById(R.id.zyz);
        ImageView quxiao = (ImageView) view.findViewById(R.id.quxiao);
        ImageView quxiao_one = (ImageView) view.findViewById(R.id.quxiao_one);

        RelativeLayout rl_zidong = (RelativeLayout) view
                .findViewById(R.id.rl_zidong);
        RelativeLayout rl_shangxia = (RelativeLayout) view
                .findViewById(R.id.rl_shangxia);
        RelativeLayout rl_zuoyou = (RelativeLayout) view
                .findViewById(R.id.rl_zuoou);
        RelativeLayout rl_languang = (RelativeLayout) view
                .findViewById(R.id.rl_languang);
        RelativeLayout rl_syz = (RelativeLayout) view.findViewById(R.id.rl_sxz);
        RelativeLayout rl_zyz = (RelativeLayout) view.findViewById(R.id.rl_zyz);
        RelativeLayout rl_quxiao = (RelativeLayout) view
                .findViewById(R.id.rl_quxiao);
        RelativeLayout rl_quxiao_one = (RelativeLayout) view
                .findViewById(R.id.rl_quxiao_one);
        rl_radios.add(rl_zidong);
        rl_radios.add(rl_shangxia);
        rl_radios.add(rl_zuoyou);
        rl_radios.add(rl_languang);
        rl_radios.add(rl_syz);
        rl_radios.add(rl_zyz);
        rl_radios.add(rl_quxiao);
        rl_radios.add(rl_quxiao_one);

        RelativeLayout rl_biaozhun = (RelativeLayout) view
                .findViewById(R.id.rl_biaozhun);
        RelativeLayout rl_mingliang = (RelativeLayout) view
                .findViewById(R.id.rl_mingliang);
        RelativeLayout rl_rouhe = (RelativeLayout) view
                .findViewById(R.id.rl_rouhe);
        RelativeLayout rl_ziran = (RelativeLayout) view
                .findViewById(R.id.rl_ziran);
        RelativeLayout rl_zidingyi = (RelativeLayout) view
                .findViewById(R.id.rl_zidingyi);

        rl_tuxiangMode.add(rl_biaozhun);
        rl_tuxiangMode.add(rl_mingliang);
        rl_tuxiangMode.add(rl_rouhe);
        rl_tuxiangMode.add(rl_ziran);
        rl_tuxiangMode.add(rl_zidingyi);
        for (int i = 0; i < rl_tuxiangMode.size(); i++) {
            rl_tuxiangMode.get(i).setOnClickListener(this);
        }
        for (int i = 0; i < rl_radios.size(); i++) {
            rl_radios.get(i).setOnClickListener(this);
        }
        RelativeLayout rl_sandi = (RelativeLayout) view
                .findViewById(R.id.rl_sandi);

        RelativeLayout rltuxiang = (RelativeLayout) view
                .findViewById(R.id.rl_tuxiang);
        ImageView biaozhun = (ImageView) view.findViewById(R.id.zidong_tuxiang_one);
        ImageView mingliang = (ImageView) view
                .findViewById(R.id.shangxia_tuxiang);
        ImageView rouhe = (ImageView) view.findViewById(R.id.zuoyou_tuxiang);
        ImageView zidingyi = (ImageView) view
                .findViewById(R.id.languang_tuxinag);
        ImageView ziran = (ImageView) view.findViewById(R.id.ziran_tuxiang);

        tuxiangMode.add(biaozhun);
        tuxiangMode.add(mingliang);
        tuxiangMode.add(rouhe);
        tuxiangMode.add(ziran);
        tuxiangMode.add(zidingyi);
        ziran.setOnClickListener(this);
        biaozhun.setOnClickListener(this);
        mingliang.setOnClickListener(this);
        rouhe.setOnClickListener(this);
        zidingyi.setOnClickListener(this);

        zidong.setOnClickListener(this);
        shangxa.setOnClickListener(this);
        zuoyou.setOnClickListener(this);
        languan.setOnClickListener(this);
        sxz.setOnClickListener(this);
        zyz.setOnClickListener(this);
        quxiao.setOnClickListener(this);
        radios.add(zidong);
        radios.add(shangxa);
        radios.add(zuoyou);
        radios.add(languan);
        radios.add(sxz);
        radios.add(zyz);
        radios.add(quxiao);
        radios.add(quxiao_one);
        if (messa.equals("tuxiang")) {
            rl_sandi.setVisibility(View.GONE);
            rltuxiang.setVisibility(View.VISIBLE);
        }
        tuxiangMode.get(0).setImageResource(R.drawable.connected_item);
        radios.get(0).setImageResource(R.drawable.radio);
        Button send = (Button) view.findViewById(R.id.send);
        Button quxiaofasong = (Button) view.findViewById(R.id.quxxiaofasong);

        Button send_tuxiang = (Button) view.findViewById(R.id.send_tuxiang);
        Button quxiaofasong_tuxiang = (Button) view
                .findViewById(R.id.quxxiaofasong_tuxiang);
        send.setOnClickListener(this);
        quxiaofasong.setOnClickListener(this);
        send_tuxiang.setOnClickListener(this);
        quxiaofasong_tuxiang.setOnClickListener(this);

        ImageView cancel = (ImageView) view.findViewById(R.id.cancel);
        ImageView cancel_tuxiang = (ImageView) view
                .findViewById(R.id.cancel_tuxiang);
        cancel.setOnClickListener(this);
        cancel_tuxiang.setOnClickListener(this);

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

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.cancel:
            case R.id.cancel_tuxiang://取消
                dismiss();
                break;
            case R.id.rl_zidong:
            case R.id.zidong://取消3D
                index = 0;
                XGIMILOG.E("index = " + index);
                break;
            case R.id.rl_shangxia:
            case R.id.shangxia://上下3D
                index = 1;
                XGIMILOG.E("index = " + index);
                break;
            case R.id.rl_zuoou:
            case R.id.zuoyou://左右3D
                index = 2;
                XGIMILOG.E("index = " + index);
                break;
            case R.id.rl_languang:
            case R.id.languang://蓝光
                index = 3;
                XGIMILOG.E("index = " + index);
                break;
            case R.id.rl_sxz://自动3D
            case R.id.sxz:
                index = 4;
                XGIMILOG.E("index = " + index);
                break;
            case R.id.rl_zyz://2D转3D
            case R.id.zyz:
                index = 5;
                XGIMILOG.E("index = " + index);
                break;
            case R.id.rl_quxiao://上下3D转2D
            case R.id.quxiao:
                index = 6;
                XGIMILOG.E("index = " + index);
                break;
            case R.id.rl_quxiao_one://左右3D转2D
            case R.id.quxiao_one:
                index = 7;
                XGIMILOG.E("index = " + index);
                break;
            case R.id.send:
                if (index == 0) {
                    mode = 0;
                } else if (index == 1) {
                    mode = 1;
                } else if (index == 2) {
                    mode = 2;
                } else if (index == 3) {
                    mode = 3;
                } else if (index == 4) {
                    mode = 4;
                } else if (index == 5) {
                    mode = 5;
                } else if (index == 6) {
                    mode = 6;
                } else if (index == 7) {
                    mode = 7;
                }
                send3D(mode, 3);
                dismiss();
                break;
            case R.id.quxxiaofasong:
                dismiss();
                break;
            case R.id.send_tuxiang:
                if (index == 0) {
                    mode = 0;
                } else if (index == 1) {
                    mode = 1;
                } else if (index == 2) {
                    mode = 2;
                } else if (index == 3) {
                    mode = 3;
                } else {
                    mode = 4;
                }
                send3D(mode, 4);
//			GMDeviceController.getInstance().SendJC(send3D(mode, 4));
                dismiss();
                // Toast.makeText(mcontext, tuxiang[index],0).show();
                break;
            case R.id.quxxiaofasong_tuxiang:
                dismiss();
                break;
            case R.id.rl_biaozhun:
            case R.id.zidong_tuxiang_one:
                index = 0;
                send3D(index, 4);
//			GMDeviceController.getInstance().SendJC(send3D(index, 4));
                break;
            case R.id.rl_mingliang:
            case R.id.shangxia_tuxiang:
                index = 1;
                send3D(index, 4);

//			GMDeviceController.getInstance().SendJC(send3D(index, 4));
                break;
            case R.id.rl_rouhe:
            case R.id.zuoyou_tuxiang:
                index = 2;
                send3D(index, 4);

//			GMDeviceController.getInstance().SendJC(send3D(index, 4));
                break;
            case R.id.rl_ziran:
            case R.id.ziran_tuxiang:
                index = 3;
                send3D(index, 4);

//			GMDeviceController.getInstance().SendJC(send3D(index, 4));
                break;
            case R.id.rl_zidingyi:
            case R.id.languang_tuxinag:
                index = 4;
                send3D(index, 4);

//			GMDeviceController.getInstance().SendJC(send3D(index, 4));
                break;
        }
        if (message.equals("tuxiang")) {
            for (int i = 0; i < tuxiangMode.size(); i++) {
                if (i == index) {
                    tuxiangMode.get(i).setVisibility(View.VISIBLE);
                } else {
                    tuxiangMode.get(i).setVisibility(View.GONE);
                }
            }
        } else {
            for (int i = 0; i < radios.size(); i++) {
                if (i == index) {
                    radios.get(i).setImageResource(R.drawable.radio);
                } else {
                    radios.get(i).setImageResource(0);
                }
            }
        }
    }

    // 发送命令
    public void send3D(int mode, int action) {
        VcontrolControl.getInstance().sendNewCommand(
                VcontrolControl.getInstance().getConnectControl(
                new VcontrolCmd(20000, "2", GMSdkCheck.appId, null, null, new VcontrolCmd.ControlCmd(action, mode, null), null, null)
                )
        );
//		JSONObject jsonObject = new JSONObject();
//		JSONObject js2 = new JSONObject();
//		try {
//			jsonObject.put("mode", mode);
//			js2.put("data", jsonObject);
//			js2.put("action", action);
//		} catch (JSONException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		System.out.println(js2.toString());
//		return js2.toString();
    }

}
