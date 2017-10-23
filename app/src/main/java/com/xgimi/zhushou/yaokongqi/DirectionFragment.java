package com.xgimi.zhushou.yaokongqi;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

//import com.xgimi.device.device.GMDeviceController;
//import com.xgimi.device.device.GMDeviceStorage;
//import com.xgimi.device.device.GMKeyCommand;
import com.xgimi.gmsdk.bean.send.GMKeyCommand;
import com.xgimi.gmsdk.callback.OnXGimiTouchListener;
import com.xgimi.gmsdk.connect.GMDeviceProxyFactory;
import com.xgimi.gmsdk.connect.IGMDeviceProxy;
import com.xgimi.zhushou.R;
import com.xgimi.zhushou.fragment.BaseFragment;
import com.xgimi.zhushou.util.AirMouse;
import com.xgimi.zhushou.util.VibratorUtil;
import com.xgimi.zhushou.util.XGIMILOG;
import com.xgimi.zhushou.widget.DirectionView;
import com.xgimi.zhushou.widget.VoiceView;

public class DirectionFragment extends BaseFragment implements OnClickListener,
        OnGestureListener {

    private GMKeyCommand gmKeyCommand;
    private DirectionView directionView;
    private VoiceView voiceView;
    private ImageView back;
    private ImageView menu;
    private ImageView home;
    private String fangshi;
    private ImageView touch;
    // 手势
    private GestureDetector mGestureDetector;
    private AirMouse airMouse;
    private Button feisu;
    private TextView oK;
    private RelativeLayout rl_ok;
    private IGMDeviceProxy mDeviceProxy;

    public static DirectionFragment getInstance(String s) {
        DirectionFragment directionfragment = new DirectionFragment();

        Bundle bundle = new Bundle();
        bundle.putString("fangshi", s);
        directionfragment.setArguments(bundle);
        return directionfragment;
    }

    public void initExras() {
        Bundle arguments = getArguments();
        if (arguments != null) {
            fangshi = arguments.getString("fangshi");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        View view = View.inflate(getActivity(), R.layout.direction, null);
        if (mDeviceProxy == null) {
            mDeviceProxy = GMDeviceProxyFactory.createDeviceProxy();
        }
        initExras();
        initView(view);
        initData();
        return view;
    }

    private void initView(View view) {
        rl_ok = (RelativeLayout) view.findViewById(R.id.rl_ok);
        rl_ok.setOnClickListener(this);
        oK = (TextView) view.findViewById(R.id.tv_ok);
        oK.setOnClickListener(this);
        mGestureDetector = new GestureDetector(getActivity(), this);
        gmKeyCommand = new GMKeyCommand();
        directionView = (DirectionView) view
                .findViewById(R.id.myDirectionPanBtn);
        voiceView = (VoiceView) view.findViewById(R.id.voice);
        back = (ImageView) view.findViewById(R.id.fanhui);
        menu = (ImageView) view.findViewById(R.id.ment);
        home = (ImageView) view.findViewById(R.id.home);
        touch = (ImageView) view.findViewById(R.id.touch);
        touch.setOnTouchListener(new OnXGimiTouchListener(mDeviceProxy));
        ImageView cmback = (ImageView) view.findViewById(R.id.iv);
        cmback.setOnClickListener(this);
        RelativeLayout direction = (RelativeLayout) view
                .findViewById(R.id.direction);
        LinearLayout chumo = (LinearLayout) view.findViewById(R.id.rlchumo);
        feisu = (Button) view.findViewById(R.id.feisu);
        feisu.setOnClickListener(this);
        if (fangshi.equals("direction")) {
            chumo.setVisibility(View.GONE);
            direction.setVisibility(View.VISIBLE);
        } else {
            chumo.setVisibility(View.VISIBLE);
            direction.setVisibility(View.GONE);
            airMouse = new AirMouse(getActivity());
            airMouse.setOnSensorListener(new AirMouse.OnSensorListener() {
                @Override
                public void onSensorChange(float x, float y) {
//                    GMDeviceController.getInstance().sendChuMo(
//                            "TOUCHEVENT:" + (-x) + "+" + (-y));
                }
            });
//			airMouse.setOnSensorListener(new OnSensorListener() {
//
//				@Override
//				public void onSensorChange(float x, float y) {
//					// TODO Auto-generated method stub
//					GMDeviceController.getInstance().sendChuMo(
//							"TOUCHEVENT:" + (-x) + "+" + (-y));
//				}
//			});

        }

    }

    private void initData() {
        touch.setOnClickListener(this);
        back.setOnClickListener(this);
        menu.setOnClickListener(this);
//		home.setOnClickListener(this);
        home.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    gmKeyCommand.setKey(GMKeyCommand.GMKeyHomeDown);// 发送home键的的命令
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    gmKeyCommand.setKey(GMKeyCommand.GMKeyHomeUp);// 发送home键的的命令
                }
                try {
                    mDeviceProxy.sendKeyCommand(gmKeyCommand);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return false;
            }
        });
        voiceView.setOnDirectionListening(new VoiceView.OnVoiceListening() {

            @Override
            public void onDirectionListening(int direction) {
                // TODO Auto-generated method stub
                switch (direction) {
                    case 1:
                        gmKeyCommand.setKey(GMKeyCommand.GMKeyEventVolumeUp);// 发送声音增加的的命令
                        break;
                    case 2:
                        gmKeyCommand.setKey(GMKeyCommand.GMKeyEventVolumeDown);// 发送声音减少的的命令
                        break;
                    default:
                        break;
                }
                try {
                    mDeviceProxy.sendKeyCommand(gmKeyCommand);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        directionView.setOnDirectionListening(new DirectionView.OnDirectionListening() {

            @Override
            public void onDirectionListening(int direction) {
                try {
                    if (mDeviceProxy.getConnectedDevice() == null
                            || mDeviceProxy.getConnectedDevice().getType() == null
                            || "".equals(mDeviceProxy.getConnectedDevice().getType())
                            || "mango".equals(mDeviceProxy.getConnectedDevice().getType())
                            || "full_mango".equals(mDeviceProxy.getConnectedDevice().getType())
                            || "full_mstara3".equals(mDeviceProxy.getConnectedDevice().getType())
                            || "mstara3".equals(mDeviceProxy.getConnectedDevice().getType())
                            || "unkonwn".equals(mDeviceProxy.getConnectedDevice().getType())) {
                        switch (direction) {
                            case 1:
                                gmKeyCommand.key = GMKeyCommand.GMKeyEventUp;// 发送向上的命令
                                break;
                            case 2:
                                gmKeyCommand.key = GMKeyCommand.GMKeyEventDown;
                                break;
                            case 3:
                                gmKeyCommand.key = GMKeyCommand.GMKeyEventLeft;
                                break;
                            case 4:
                                gmKeyCommand.key = GMKeyCommand.GMKeyEventRight;
                                break;
                            case 5:
                                gmKeyCommand.key = GMKeyCommand.GMKeyEventOk;
                                break;
                        }
                    } else {
                        switch (direction) {
                            case 1:
                                gmKeyCommand.key = GMKeyCommand.GMKeyupdonw;// 发送向上的命令
                                break;
                            case 2:
                                gmKeyCommand.key = GMKeyCommand.GMKeyDowndonw;// 发送向上的命令
                                break;
                            case 3:
                                gmKeyCommand.key = GMKeyCommand.GMKeyLeftDown;// 发送向上的命令
                                break;
                            case 4:
                                gmKeyCommand.key = GMKeyCommand.GMKeyrightdonw;// 发送向上的命令
                                break;
                            case 5:
                                gmKeyCommand.key = GMKeyCommand.GMKeyOkDown;// 发送向上的命令
                                break;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Shake(50);
                // 想设备发送命令
                try {
                    mDeviceProxy.sendKeyCommand(gmKeyCommand);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onDirectionCancel(int direction) {
                // TODO Auto-generated method stub
                switch (direction) {

                    case 1:
                        gmKeyCommand.key = GMKeyCommand.GMKeyupUp;// 发送向上的命令
                        break;
                    case 2:
                        gmKeyCommand.key = GMKeyCommand.GMKeyDownup;// 发送向上的命令
                        break;
                    case 3:
                        gmKeyCommand.key = GMKeyCommand.GMKeyLeftup;// 发送向上的命令
                        break;
                    case 4:
                        gmKeyCommand.key = GMKeyCommand.GMKeyrightup;// 发送向上的命令
                        break;
                    case 5:
                        gmKeyCommand.key = GMKeyCommand.GMKeyOkUp;// 发送向上的命令
                        break;
                }
                try {
                    mDeviceProxy.sendKeyCommand(gmKeyCommand);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }


    @Override
    public void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        if (airMouse != null) {
            airMouse.stop();
        }
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        Shake(30);
        switch (v.getId()) {
            case R.id.ment:
                gmKeyCommand.setKey(GMKeyCommand.GMKeyEventMenu);// 发送菜单键的的命令
                try {
                    mDeviceProxy.sendKeyCommand(gmKeyCommand);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.fanhui:
            case R.id.iv:
                gmKeyCommand.setKey(GMKeyCommand.GMKeyEventBack);
                try {
                    mDeviceProxy.sendKeyCommand(gmKeyCommand);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.feisu:
                if (feisu.getText().toString().trim().equals("切换至飞鼠")) {
                    if (airMouse != null) {
                        boolean start = airMouse.start();
                        if (!start) {
                            Toast.makeText(getActivity(), "您的手机暂不支持此功能", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                    touch.setVisibility(View.INVISIBLE);
                    oK.setVisibility(View.VISIBLE);

                    feisu.setText("切换至触摸");
                    if (airMouse != null) {
                        boolean start = airMouse.start();
                        if (!start) {
                            // feisu.setVisibility(View.GONE);
                        }
                    }
                } else {
                    if (airMouse != null) {
                        airMouse.stop();
                    }
                    feisu.setText("切换至飞鼠");
                    touch.setVisibility(View.VISIBLE);
                    oK.setVisibility(View.GONE);
                }
                break;
            case R.id.rl_ok:
            case R.id.tv_ok:
                sendMouseMove("MOUSELEFTS:" + "3");
                break;
        }
    }

    /**
     * 获取震动设置
     *
     * @param time
     */
    private void Shake(long time) {
        if (app.readZhuangtai()) {
            VibratorUtil.Vibrate(getActivity(), time);
            // VibratorUtil.Voice(getActivity());
        }
    }

    @Override
    public boolean onDown(MotionEvent e) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        // TODO Auto-generated method stub
        sendMouseMove("MOUSELEFTS:" + "3");
        return true;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
                            float distanceY) {
        // TODO Auto-generated method stub
        sendMouseMove("TOUCHEVENT:" + (-distanceX) + "+" + (-distanceY));

        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                           float velocityY) {
        // TODO Auto-generated method stub
        int to = 0;

        if (Math.abs(velocityX) > 2500 && velocityX > 0) {

            to = 1;

        } else if (Math.abs(velocityX) > 2500 && velocityX < 0) {

            to = -1;
        }

        // sendMouseMove("MOUSEWHEEL:" + to);

        return true;
    }

//    @Override
//    public boolean onTouch(View v, MotionEvent event) {
//        // TODO Auto-generated method stub
//        return mGestureDetector.onTouchEvent(event);
//    }

    /**
     * 移动发送命令函数
     *
     * @param
     * @param
     */
    private void sendMouseMove(String command) {
//        GMDeviceController.getInstance().sendChuMo(command);
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
    }
}
