package com.xgimi.zhushou.activity;

import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.xgimi.device.device.FeedbackInfo;
import com.xgimi.device.device.VcontrolCmd;
import com.xgimi.device.util.GMSdkCheck;
import com.xgimi.device.vcontrolcmd.VcontrolControl;
import com.xgimi.zhushou.BaseActivity;
import com.xgimi.zhushou.R;
import com.xgimi.zhushou.adapter.IntelligenceAdapter;
import com.xgimi.zhushou.bean.IntelligenceBean;
import com.xgimi.zhushou.util.Constant;
import com.xgimi.zhushou.widget.IntelligenceDilog;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import confignetwork.WifiAdminSimple;
import de.greenrobot.event.EventBus;

public class IntelligenceActivity extends BaseActivity {
    private static final String TAG = "MainActivity";

    private ListView lvDevice;
    List<String> listData;
    private MessageThread messageThread;
    private static final int PORT = 8009;
    private boolean isTurnOff = false;
    private SwipeRefreshLayout swipeRefreshLayout;
    private IntelligenceDilog mDilog;

    private boolean isSearching = false;
    private ImageView mIv_regresh;
    private List<IntelligenceBean> mDatas = new ArrayList<>();
    private IntelligenceAdapter mAdatper;
    private Animation anim;
    private Button fab;
    private String mDeviceIp;
    private LinearLayout ll_tisi;
    private TextView mtv_ip;
    private RelativeLayout relativeLayout;
    private RelativeLayout rl_one;
    private View mView;
    private ImageView iv_turn_switch;
    private VcontrolCmd.ControlCmd.SmartScreen smartScreen;
    private WifiAdminSimple mWifiAdmin;
    private int isUpDown = -1;
    private boolean isUp = false;

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            MissDilog();
            relativeLayout.setVisibility(View.VISIBLE);
            rl_one.setVisibility(View.GONE);
        }
    };
    private ImageView iv_play;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mView = View.inflate(this, R.layout.activity_intelligence, null);
        setContentView(mView);

        EventBus.getDefault().register(this);
        iv_play = (ImageView) findViewById(R.id.iv_play);
        controlTitle(findViewById(R.id.id_toolbar), true, true, false, false);
        TextView tv_curent_wifi = (TextView) findViewById(R.id.tv_current_wifi);
        mWifiAdmin = new WifiAdminSimple(this);
        String apSsid = mWifiAdmin.getWifiConnectedSsid();
        if (apSsid != null) {
            tv_curent_wifi.setText("当前已连接网络: " + apSsid);
        } else {
            tv_curent_wifi.setText("当前已连接网络: 未知");
        }
        if (Constant.netStatus) {
            VcontrolControl.getInstance().sendNewCommand(VcontrolControl.getInstance().getConnectControl(new VcontrolCmd(20000, "2", GMSdkCheck.appId, null,
                    null, new VcontrolCmd.ControlCmd(9, 4, 0, null, null, null), null, null)));
        }
        tv.setText("智能配件");
        fab = (Button) findViewById(R.id.peizhi);
        anim = AnimationUtils.loadAnimation(this, R.anim.xuanzuanone);
        relativeLayout = (RelativeLayout) findViewById(R.id.rl_connect_later);
        rl_one = (RelativeLayout) findViewById(R.id.rl_one);
        iv_turn_switch = (ImageView) findViewById(R.id.iv_turn_switch);
        iv_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                control(2);
            }
        });

        iv_turn_switch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isTurnOff) {
                    iv_turn_switch.setImageResource(R.drawable.inter_close);
                    isTurnOff = true;
                    smartScreen.setState(0);
                    VcontrolControl.getInstance().sendNewCommand(VcontrolControl.getInstance().getConnectControl(new VcontrolCmd(20000, "2", GMSdkCheck.appId, null,
                            null, new VcontrolCmd.ControlCmd(20, smartScreen), null, null)));
                } else {
                    iv_turn_switch.setImageResource(R.drawable.inter_open);
                    isTurnOff = false;
                    smartScreen.setState(1);
                    VcontrolControl.getInstance().sendNewCommand(VcontrolControl.getInstance().getConnectControl(new VcontrolCmd(20000, "2", GMSdkCheck.appId, null,
                            null, new VcontrolCmd.ControlCmd(20, smartScreen), null, null)));
                }
            }
        });
        LinearInterpolator lir = new LinearInterpolator();
        anim.setInterpolator(lir);
        fab.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(final View view) {
                mDilog = new IntelligenceDilog(IntelligenceActivity.this);
                mDilog.show();
                mDilog.setmLisener(new ConfigureCallBack() {
                    @Override
                    public void getConfigIp(String ip) {

                        mDeviceIp = ip;
                        rl_one.setVisibility(View.GONE);
                        VcontrolControl.getInstance().sendNewCommand(VcontrolControl.getInstance().getConnectControl(new VcontrolCmd(20000, "2", GMSdkCheck.appId, null,
                                null, new VcontrolCmd.ControlCmd(20, smartScreen), null, null)));
                        relativeLayout.setVisibility(View.VISIBLE);
                        smartScreen = new VcontrolCmd.ControlCmd.SmartScreen("智能幕布", ip, 1);
                        mtv_ip.setText("(" + mDeviceIp + ")");
                        messageThread = new MessageThread();
                        new Thread(messageThread).start();
                        mHandler.sendEmptyMessage(1);
//                        new Handler().postDelayed(new Runnable() {
//                            public void run() {
//                                getDeviceInfo();
//                            }
//                        }, 2000);
                    }
                });
            }
        });


        TextView textView = (TextView) findViewById(R.id.iv_tv_tishi);
        textView.setText(Html
                .fromHtml(descString(), getImageGetterInstance(), null));
        mtv_ip = (TextView) findViewById(R.id.tv_ip);
        ImageView iv_up = (ImageView) findViewById(R.id.iv_up);
        ImageView iv_down = (ImageView) findViewById(R.id.iv_down);
        mIv_regresh = (ImageView) findViewById(R.id.iv_refresh);
        mIv_regresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SearchAsyncTask().execute();
                mDatas.clear();
                mIv_regresh.startAnimation(anim);
            }
        });
        iv_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                control(0);
            }
        });
        iv_down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                control(1);
            }
        });
        ll_tisi = (LinearLayout) findViewById(R.id.ll_tishi);
        lvDevice = (ListView) findViewById(R.id.listview);
        mAdatper = new IntelligenceAdapter(this, mDatas);

        lvDevice.setAdapter(mAdatper);

        lvDevice.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mDeviceIp = mAdatper.getItem(position).getIp();
                showDilog("连接中...");
                mtv_ip.setText("(" + mDeviceIp + ")");
//                messageThread = new MessageThread();
//                isUpDown=-1;
//                new Thread(messageThread).start();
                mHandler.sendEmptyMessage(1);


//                new Handler().postDelayed(new Runnable() {
//                    public void run() {
//                        getDeviceInfo();
//                    }
//                }, 2000);


//                ControlDilog dialog = new ControlDilog(IntelligenceActivity.this, mAdatper.getItem(position).getIp());
//                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);  //去除标题栏
//                dialog.show();
            }
        });
        new SearchAsyncTask().execute();
    }

    /**
     * 字符串
     *
     * @return
     */
    private String descString() {
        return "点击" + "<img src='" + R.drawable.small_up
                + "'/>" + "按钮升起幕布,点击" + "<img src='" + R.drawable.small_down
                + "'/>" + "按钮落下幕布" + "";

    }

    /**
     * ImageGetter用于text图文混排
     *
     * @return
     */
    public Html.ImageGetter getImageGetterInstance() {
        Html.ImageGetter imgGetter = new Html.ImageGetter() {
            @Override
            public Drawable getDrawable(String source) {
                int fontH = (int) (getResources().getDimension(
                        R.dimen.text_size) * 1.5);
                int id = Integer.parseInt(source);
                Drawable d = getResources().getDrawable(id);
                int height = fontH;
                int width = (int) ((float) d.getIntrinsicWidth() / (float) d
                        .getIntrinsicHeight()) * fontH;
                if (width == 0) {
                    width = d.getIntrinsicWidth();
                }
                d.setBounds(0, 0, width, height);
                return d;
            }
        };
        return imgGetter;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onPause() {
        super.onPause();

        isSearching = false;

    }

    private class SearchAsyncTask extends AsyncTask<Void, Integer, String> {

        InetAddress deviceAddress = null;
        DatagramSocket socket = null;

        @Override
        protected String doInBackground(Void... params) {
            isSearching = true;

            try {
                socket = new DatagramSocket();
            } catch (SocketException e) {
                e.printStackTrace();
            }

            //接收
            Thread receiveThread = new Thread(new Runnable() {
                @Override
                public void run() {

                    byte[] dataReceive = new byte[1024];
                    DatagramPacket packetReceive = new DatagramPacket(dataReceive, dataReceive.length);

                    try {
                        socket.receive(packetReceive);

                        String reply = new String(dataReceive, 0, packetReceive.getLength());
                        if (reply.contains("I'm a screen Controller")) {
                            isSearching = false;
                            deviceAddress = packetReceive.getAddress();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            });

            receiveThread.start();

            int cnt = 3; //搜索次数

            while (isSearching) {
//                Log.i(TAG, "开始搜索...");

                if (--cnt == 0) {
                    break;
                }

                //发送
                String data = "Are You Screen Controller?";

                DatagramPacket packetSend = null;
                try {
                    packetSend = new DatagramPacket(data.getBytes(), data.getBytes().length,
                            InetAddress.getByName("255.255.255.255"), 12419);
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }

                try {
                    socket.send(packetSend);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            receiveThread.interrupt();

            if (deviceAddress != null) {
                return deviceAddress.toString().substring(1);  //去除ip地址前的斜杠
            } else {
                return null;
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mDatas.clear();
            mAdatper.dataChange(mDatas);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s != null) {
                ll_tisi.setVisibility(View.GONE);
                lvDevice.setVisibility(View.VISIBLE);
                if (mDilog != null)
                    mDilog.dismiss();
                for (int i = 0; i < mDatas.size(); i++) {
                    if (mDatas.get(i).equals(s)) {
                        return;
                    }
                }
                mDatas.add(new IntelligenceBean(s, "智能幕布"));
                smartScreen = new VcontrolCmd.ControlCmd.SmartScreen("智能幕布", s, 1);
                mAdatper.dataChange(mDatas);
                mIv_regresh.clearAnimation();

            } else {
//                Toast.makeText(IntelligenceActivity.this, "没有搜索到设备,再次下拉搜索", Toast.LENGTH_SHORT).show();
            }
        }
    }
//连接之后的操作

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            if (msg.what == 0x123) {
                String data = (msg.obj).toString();

                try {
                    JSONObject jsonObject = new JSONObject(data);

                    int cmd = jsonObject.getInt("cmd");

                    if (cmd == 1) {
                        int sn = jsonObject.getInt("sn");
                        String swver = jsonObject.getString("swver");
                        String hdver = jsonObject.getString("hdver");

//                        tvDeviceInfo.setText("序列号：" + sn + "\n\n软件版本：" + swver + "\n\n硬件版本：" + hdver);

                    } else if (cmd == 2) {
                        int result = jsonObject.getInt("result");
                        if (result == 1) { //成功
                            Toast.makeText(IntelligenceActivity.this, "操作成功", Toast.LENGTH_SHORT).show();
                        } else if (result == 0) {  //失败
                            Toast.makeText(IntelligenceActivity.this, "操作失败", Toast.LENGTH_SHORT).show();
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    };


    public void control(int state) {
        if (messageThread == null)
            messageThread = new MessageThread();
        messageThread.setIp(smartScreen.ip);
        messageThread.setMode(state);
        new Thread(messageThread).start();
    }

    public class MessageThread implements Runnable {
        final int PORT = 8009;
        String cmd, ip;

        public void MessageThread() {

        }

        public void setIp(String ip) {
            this.ip = ip;
        }

        public void setMode(int mode) {
            JSONObject dataUp = new JSONObject();
            try {
                dataUp.put("cmd", 2);
                dataUp.put("action", mode);
                cmd = dataUp.toString();
            } catch (JSONException e) {
                e.printStackTrace();
                cmd = null;
            }
        }

        @Override
        public void run() {
            if (TextUtils.isEmpty(cmd)) {
                return;
            }
            BufferedReader br = null;
            PrintWriter pw = null;
            Socket socket = null;
            try {
                socket = new Socket(ip, PORT);
                socket.setSoTimeout(1000);
                br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                pw = new PrintWriter(socket.getOutputStream());
                pw.write(cmd);
                pw.flush();
                String data = null;
                while ((data = br.readLine()) != null) {
                    Log.i(TAG, "接收到的数据：" + data);
                    Message msg = new Message();
                    msg.what = 0x123;
                    msg.obj = data;
                    handler.sendMessage(msg);
                }
            } catch (SocketTimeoutException e) {
                Log.i(TAG, "网络连接超时！");
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (pw != null) {
                        pw.close();
                    }
                    if (br != null) {
                        br.close();
                    }
                    if (socket != null) {
                        socket.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    public interface ConfigureCallBack {
        void getConfigIp(String ip);
    }

    public void onEventMainThread(FeedbackInfo info) {
        if (info.action == 30237) {
            Log.e("info", "info" + info.deviceSetting.autoscreen);
        }
    }

}
