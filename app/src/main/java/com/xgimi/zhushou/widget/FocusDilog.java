package com.xgimi.zhushou.widget;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xgimi.device.device.GMDeviceController;
import com.xgimi.device.device.GMKeyCommand;
import com.xgimi.device.device.VcontrolCmd;
import com.xgimi.device.util.GMSdkCheck;
import com.xgimi.device.vcontrolcmd.VcontrolControl;
import com.xgimi.zhushou.R;
import com.xgimi.zhushou.util.StringUtils;
import com.xgimi.zhushou.util.ToosUtil;
import com.xgimi.zhushou.util.XGIMILOG;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class FocusDilog implements OnTouchListener, OnClickListener {
    public Dialog mDialog;
    List<ImageView> radios = new ArrayList<ImageView>();
    Context mContext;

    public FocusDilog(Context context, String message) {
        LayoutInflater inflater = LayoutInflater.from(context);
        this.mContext = context;
        View view = inflater.inflate(R.layout.focus, null);
        mDialog = new Dialog(context, R.style.dialog);
        rl_focus = (RelativeLayout) view.findViewById(R.id.rl_flocus);
        ImageView focuscancel = (ImageView) view.findViewById(R.id.clean);
        ImageView wujicancel = (ImageView) view.findViewById(R.id.iv);
        LinearLayout focus = (LinearLayout) view.findViewById(R.id.foucs);
        RelativeLayout wuji_rl = (RelativeLayout) view.findViewById(R.id.wuji_rl);
        RelativeLayout shurukuang = (RelativeLayout) view.findViewById(R.id.shurukuang);
        Button send = (Button) view.findViewById(R.id.send);
        Button cancel = (Button) view.findViewById(R.id.cancel);
        et = (MyEditText) view.findViewById(R.id.et_search);
        ImageView iv1 = (ImageView) view.findViewById(R.id.iv1);
        iv1.setOnClickListener(this);
        send.setOnClickListener(this);
        cancel.setOnClickListener(this);
        focuscancel.setOnClickListener(this);
        wujicancel.setOnClickListener(this);
        if (message.equals("wuji")) {
            wuji_rl.setVisibility(View.VISIBLE);
            rl_focus.setVisibility(View.GONE);
        }
        if (message.equals("shurukuang")) {
            shurukuang.setVisibility(View.VISIBLE);
            rl_focus.setVisibility(View.GONE);

        }
        mDialog.setContentView(view);
        mDialog.setCanceledOnTouchOutside(true);
        initView(view);
        gmCommand = new GMKeyCommand();
    }

    private void initView(View view) {
        tv_xianshi = (TextView) view.findViewById(R.id.tv_xianshi);
        ImageView wujireduce = (ImageView) view.findViewById(R.id.wuji_reduce);
        ImageView wujiadd = (ImageView) view.findViewById(R.id.wuji_add);
        wujireduce.setOnClickListener(this);
        wujiadd.setOnClickListener(this);


        ImageView focus_add = (ImageView) view.findViewById(R.id.focus_add);
        ImageView focus_reduce = (ImageView) view
                .findViewById(R.id.focus_reduce);
        focus_add.setOnTouchListener(this);
        focus_reduce.setOnTouchListener(this);

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
    private GMKeyCommand gmCommand;
    private TextView tv_xianshi;
    private MyEditText et;
    private RelativeLayout rl_focus;

    public boolean onTouch(View v, MotionEvent event) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.focus_add:
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    gmCommand.setKey(GMKeyCommand.GMKeyEventFocusLeft_down);
                    GMDeviceController.getInstance().sendCommand(
                            gmCommand);
                }
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    // String command = Constant.COM_FOCUS_ADD_UP;
                    // Send(command);
                    gmCommand.setKey(GMKeyCommand.GMKeyEventFocusLeft_up);
                    GMDeviceController.getInstance().sendCommand(
                            gmCommand);
                }
                break;
            case R.id.focus_reduce:
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    gmCommand.setKey(GMKeyCommand.GMKeyEventFocusRight_down);
                    GMDeviceController.getInstance().sendCommand(
                            gmCommand);
                }
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    gmCommand.setKey(GMKeyCommand.GMKeyEventFocusRight_up);
                    GMDeviceController.getInstance().sendCommand(
                            gmCommand);
                }
                break;
        }
        return false;
    }

    @SuppressLint("UseValueOf")
    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub

        switch (v.getId()) {
            case R.id.wuji_reduce:
                String trim = tv_xianshi.getText().toString().trim();
                Integer valueOf = Integer.valueOf(trim);
                valueOf--;
                if (valueOf == -1) {
                    valueOf = 100;
                }
                tv_xianshi.setText(valueOf + "");
                Integer inter1 = new Integer(valueOf);
                wujiBianjiao(inter1.intValue());
//			GMDeviceController.getInstance().SendJC(wujiBianjiao(inter1.intValue()));

                break;
            case R.id.wuji_add:
                String number = tv_xianshi.getText().toString().trim();
                Integer number1 = Integer.valueOf(number);

                number1++;
                if (number1 == 101) {
                    number1 = 0;
                }
                tv_xianshi.setText(number1 + "");
                Integer inter = new Integer(number1);
                wujiBianjiao(inter.intValue());
//			GMDeviceController.getInstance().SendJC(wujiBianjiao(inter.intValue()));
                break;
            case R.id.send:
                if (!StringUtils.isEmpty(et.getText().toString().trim())) {

                    VcontrolControl.getInstance().sendNewCommand(
                            VcontrolControl.getInstance().getConnectControl(
                                    new VcontrolCmd(20000, "2", GMSdkCheck.appId, null,
                                            null, new VcontrolCmd.ControlCmd(9, 3, et.getText().toString().trim()),
                                            null, null)
                            )
                    );
//				GMDeviceController.getInstance().SendJC("inputstring:" + et.getText().toString().trim());
                    ToosUtil.getInstance().addEventUmeng(mContext, "event_device_input");
                }
                break;
            case R.id.cancel:
            case R.id.iv1:
            case R.id.clean:
            case R.id.iv:
                dismiss();
                break;
            default:
                break;
        }
    }


    //发送命令
    public String tuxiangMode(int mode) {
        JSONObject jsonObject = new JSONObject();
        JSONObject js2 = new JSONObject();
        try {
            jsonObject.put("mode", mode);
            js2.put("data", jsonObject);
            js2.put("action", 4);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        System.out.println(js2.toString());
        return js2.toString();
    }

    //发送命令
    public void wujiBianjiao(int mode) {


        VcontrolControl.getInstance().sendNewCommand(
                VcontrolControl.getInstance().getConnectControl(
                        new VcontrolCmd(20000, "2", GMSdkCheck.appId, null,
                                null, new VcontrolCmd.ControlCmd(2, 1, 0, null, null, new VcontrolCmd.ControlCmd.ZoomFocus(1, mode)), null, null)
                )
        );
        JSONObject jsonObject = new JSONObject();

        JSONObject js2 = new JSONObject();
        JSONObject js4 = new JSONObject();

        try {
//				jsonObject.put("mode", js2);
            js2.put("data", jsonObject);
            jsonObject.put("zoomfocus", js4);
            jsonObject.put("type", 1);
            js4.put("scale", null);
            js4.put("value", mode);
            js4.put("vvalue", null);
            js4.put("hvalue", null);
            js2.put("action", 2);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        System.out.println(js2.toString());
//			return js2.toString();
    }
}
