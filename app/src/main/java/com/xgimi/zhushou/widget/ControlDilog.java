package com.xgimi.zhushou.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.xgimi.zhushou.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketTimeoutException;


public class ControlDilog extends Dialog implements View.OnClickListener {
    private static final String TAG = "ControlDialog";

    private Context mContext;
    private String mDeviceIp;

    private TextView tvDeviceInfo;

    private MessageThread messageThread;
    private static final int PORT = 8009;

    public ControlDilog(Context context, String DeviceIp) {
        super(context);

        this.mContext = context;
        this.mDeviceIp = DeviceIp;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_control);

        TextView tvDeviceIp = (TextView) findViewById(R.id.tv_dialog_ip);
        tvDeviceIp.setText("[ " + mDeviceIp + " ]");

        tvDeviceInfo = (TextView) findViewById(R.id.tv_device_info);
        tvDeviceInfo.setMovementMethod(ScrollingMovementMethod.getInstance());
        tvDeviceInfo.setText("序列号：\n\n软件版本：\n\n硬件版本：\n\n幕布状态：");

        TextView tvUp = (TextView) findViewById(R.id.tv_up);
        tvUp.setOnClickListener(this);

        TextView tvDown = (TextView) findViewById(R.id.tv_down);
        tvDown.setOnClickListener(this);


        messageThread = new MessageThread();

        new Thread(messageThread).start();

        new Handler().postDelayed(new Runnable() {
            public void run() {
                getDeviceInfo();
            }
        }, 1000);

    }

    private void getDeviceInfo() {
        JSONObject dataGetInfo = new JSONObject();

        try {
            dataGetInfo.put("cmd", 1);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Message msgGetInfo = new Message();
        msgGetInfo.what = 0x345;
        msgGetInfo.obj = dataGetInfo;
        messageThread.sendHandler.sendMessage(msgGetInfo);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_up:

                JSONObject dataUp = new JSONObject();

                try {
                    dataUp.put("cmd", 2);
                    dataUp.put("action", 0);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Message msgUp = new Message();
                msgUp.what = 0x345;
                msgUp.obj = dataUp;
                messageThread.sendHandler.sendMessage(msgUp);

                break;

            case R.id.tv_down:

                JSONObject dataDown = new JSONObject();

                try {
                    dataDown.put("cmd", 2);
                    dataDown.put("action", 1);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Message msgDown = new Message();
                msgDown.what = 0x345;
                msgDown.obj = dataDown;
                messageThread.sendHandler.sendMessage(msgDown);

                break;
        }
    }

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

                        tvDeviceInfo.setText("序列号：" + sn + "\n\n软件版本：" + swver + "\n\n硬件版本：" + hdver);

                    } else if (cmd == 2) {
                        int result = jsonObject.getInt("result");
                        if (result == 1) { //成功
                            Toast.makeText(mContext, "操作成功", Toast.LENGTH_SHORT).show();
                        } else if (result == 0) {  //失败
                            Toast.makeText(mContext, "操作失败", Toast.LENGTH_SHORT).show();
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    };


    public class MessageThread implements Runnable {
        //接收UI线程消息的Handler对象
        public Handler sendHandler;

        private Socket socket;
        BufferedReader br = null;
        PrintWriter pw = null;

        @Override
        public void run() {

            try {
                socket = new Socket(mDeviceIp, PORT);
                br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                pw = new PrintWriter(socket.getOutputStream());

                //启动一个线程来读取数据
                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        String data = null;

                        try {
                            while ((data = br.readLine()) != null) {
                                Log.i(TAG, "接收到的数据：" + data);

                                Message msg = new Message();
                                msg.what = 0x123;
                                msg.obj = data;
                                handler.sendMessage(msg);
                            }

                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                }).start();

                Looper.prepare();
                sendHandler = new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        super.handleMessage(msg);

                        if (msg.what == 0x345) {
                            pw.write((msg.obj).toString());
                            pw.flush();
                        }
                    }
                };
                //启动Looper
                Looper.loop();

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
}
