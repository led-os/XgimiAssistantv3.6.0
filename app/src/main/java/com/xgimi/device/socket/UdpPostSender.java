package com.xgimi.device.socket;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.google.gson.Gson;
import com.xgimi.device.device.FeedbackInfo;
import com.xgimi.device.utils.Consants;
import com.xgimi.zhushou.util.LogUtil;
import com.xgimi.zhushou.util.XGIMILOG;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;

/**
 * UDP通信接口类，采用单例模式，用ip和通信端口初始化
 */
public class UdpPostSender extends Thread {

    private static String TAG = "UdpPostSender";

    public static boolean IsThreadDisable = false;

    public static boolean hasConnect = false;

    private Handler mHandler;

    private DatagramSocket udpSocket = null;

    private DatagramPacket dataPacket = null;

    private InetAddress broadcastAddr = null;

    private int defaultPort;

    private int remotePort;

    private String ip;

    public UdpPostSender(String iP, int defaultport, int remoteport, Handler handler) {

        this.ip = iP;
        this.defaultPort = defaultport;
        this.remotePort = remoteport;
        this.mHandler = handler;
        initData();

        if (handler != null) {
            this.start();
        }
    }

    @Override
    public void run() {
        startListen();
    }

    private void initData() {

        try {
            if (udpSocket == null) {
                udpSocket = new DatagramSocket(null);
                udpSocket.setReceiveBufferSize(1024 * 1024 * 3);
                udpSocket.setReuseAddress(true);
                udpSocket.bind(new InetSocketAddress(defaultPort));
                broadcastAddr = InetAddress.getByName(ip);
            }
        } catch (Exception e) {

        }
    }

    public void sendUDPmsg(final String command) {
        XGIMILOG.D("Device Send To Phone " +broadcastAddr.toString()+ " :\n --------------------------------- \n" + command  + "\n --------------------------------- \n");
        byte out[] = command.getBytes();
        dataPacket = new DatagramPacket(out, out.length);
        dataPacket.setPort(remotePort);
        dataPacket.setAddress(broadcastAddr);
        new Thread() {
            public void run() {
                try {
                    udpSocket.send(dataPacket);
                } catch (Exception e) {

                }
            }
        }.start();
    }

    public void startListen() {
        XGIMILOG.D("startListen: " + defaultPort);
        while (!IsThreadDisable) {
//            XGIMILOG.E("");
            hasConnect = true;
            try {
                Message msg = mHandler.obtainMessage();
                byte[] buf = new byte[1024 * 1024 * 3];
                DatagramPacket packet = new DatagramPacket(buf, buf.length);
                initData();
                udpSocket.receive(packet);
                String cmd = new String(packet.getData(), 0, packet.getLength());
                if (cmd.startsWith("{")) {
                    FeedbackInfo infor = new Gson().fromJson(cmd, FeedbackInfo.class);
                    if (infor.deviceInfo != null) {
                        if (infor.deviceInfo.deviceip == null || "".equals(infor.deviceInfo.deviceip)) {
                            infor.deviceInfo.ipaddress = packet.getAddress().toString().replace("/", "");
                        } else if (infor.deviceInfo.ipaddress == null || "".equals(infor.deviceInfo.ipaddress)) {
                            infor.deviceInfo.deviceip = packet.getAddress().toString().replace("/", "");
                        }
                        cmd = new Gson().toJson(infor);
                    }
                }
                XGIMILOG.E("Device Received : \n---------------------------------\nremote ip = " + packet.getAddress().toString() + " info = " + cmd + "\n---------------------------------\n");
                if (udpSocket.getLocalPort() == 16751) {
                    msg.obj = cmd;
                    msg.what = UdpManager.NEWCONNECT;
                    mHandler.sendMessage(msg);
                } else {
                    if (cmd.startsWith("Z3")) {
                        msg.obj = cmd;
                        msg.what = UdpManager.CONNECTED;
                        mHandler.sendMessage(msg);
                    } else if (cmd.startsWith("DEVICETYPE")) {
                        msg.obj = cmd;
                        msg.what = UdpManager.RECEIVEDEVICETYPE;
                        mHandler.sendMessage(msg);
                    } else {
                        Consants.ip = ip;
                        msg.obj = cmd;
                        msg.what = UdpManager.RECEIVEDMESSAGE;
                        mHandler.sendMessage(msg);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void stopListen() {
        IsThreadDisable = true;
    }

}
