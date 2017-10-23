package com.xgimi.zhushou.widget;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.waxrain.droidsender.SenderService;
import com.waxrain.droidsender.delegate.Global;
import com.xgimi.device.device.GMDeviceController;
import com.xgimi.device.device.GMDeviceStorage;
import com.xgimi.device.device.GMKeyCommand;
import com.xgimi.device.device.VcontrolCmd;
import com.xgimi.device.util.GMSdkCheck;
import com.xgimi.device.vcontrolcmd.VcontrolControl;
import com.xgimi.zhushou.App;
import com.xgimi.zhushou.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 霍长江 on 2017/5/17.
 */
public class WifiAilplay implements View.OnClickListener{
    public Dialog mDialog;
    List<ImageView> radios = new ArrayList<ImageView>();
    Context mContext;

    public WifiAilplay(Context context, String message) {
        LayoutInflater inflater = LayoutInflater.from(context);
        this.mContext=context;
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
        TextView tv= (TextView) view.findViewById(R.id.text1);
        tv.setText("请输入密码");
        et.setHint("请输入密码");
        send.setText("确认");
        cancel.setText("取消");
            wuji_rl.setVisibility(View.GONE);
            shurukuang.setVisibility(View.VISIBLE);
            rl_focus.setVisibility(View.GONE);

        mDialog.setContentView(view);
        mDialog.setCanceledOnTouchOutside(true);
        initView(view);
        gmCommand = new GMKeyCommand();
    }

    private void initView(View view) {
        tv_xianshi = (TextView) view.findViewById(R.id.tv_xianshi);
        ImageView wujireduce=(ImageView) view.findViewById(R.id.wuji_reduce);
        ImageView wujiadd=(ImageView) view.findViewById(R.id.wuji_add);
        wujireduce.setOnClickListener(this);
        wujiadd.setOnClickListener(this);
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
    private Global.DirentObj playObj;

    @SuppressLint("UseValueOf")
    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub

        switch (v.getId()) {
            case R.id.wuji_reduce:
                String trim = tv_xianshi.getText().toString().trim();
                Integer valueOf = Integer.valueOf(trim);
                valueOf--;
                if(valueOf==-1){
                    valueOf=100;
                }
                tv_xianshi.setText(valueOf+"");
                Integer inter1=new Integer(valueOf);
                wujiBianjiao(inter1.intValue());
//			GMDeviceController.getInstance().SendJC(wujiBianjiao(inter1.intValue()));

                break;
            case R.id.wuji_add:
                String number = tv_xianshi.getText().toString().trim();
                Integer number1 = Integer.valueOf(number);

                number1++;
                if(number1==101){
                    number1=0;
                }
                tv_xianshi.setText(number1+"");
                Integer inter=new Integer(number1);
                wujiBianjiao(inter.intValue());
//			GMDeviceController.getInstance().SendJC(wujiBianjiao(inter.intValue()));
                break;
            case R.id.send:
                for (int i = 0; i< SenderService.dmrList.size(); i++){
                    if(SenderService.dmrList.get(i).localip.equals(GMDeviceStorage.getInstance().getConnectedDevice().ip)){
                        SenderService.upnp.SetCurrentDMR(SenderService.dmrList.get(i));
                    }
                }
                if(playObj==null) {
                    playObj = new Global().new DirentObj(Global.CONTENT_MIRROR, Global.CONTENT_FMT_VIDEO,
                            "Screen Mirroring", "http://droidsender/AirPinMirroring", -1, false, 0, "", null);
                }
                SenderService.upnp.Play(playObj, et.getText().toString().trim());
                App.getContext().saveAilplayPasswrid(et.getText().toString().trim());
                InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(et.getWindowToken(), 0) ;
                dismiss();
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
    public String tuxiangMode(int mode){
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
    public void wujiBianjiao(int mode){

        VcontrolControl.getInstance().sendNewCommand(VcontrolControl.getInstance().getConnectControl(new VcontrolCmd(20000, "2", GMSdkCheck.appId, null,
                null, new VcontrolCmd.ControlCmd(2,1,0,null,null,new VcontrolCmd.ControlCmd.ZoomFocus(1,mode)), null, null)));
        JSONObject jsonObject = new JSONObject();

        JSONObject js2 = new JSONObject();
        JSONObject js4=new JSONObject();

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
