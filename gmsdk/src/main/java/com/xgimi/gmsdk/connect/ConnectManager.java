package com.xgimi.gmsdk.connect;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.google.gson.Gson;
import com.xgimi.gmsdk.bean.device.GMDevice;
import com.xgimi.gmsdk.bean.reply.AppListReply;
import com.xgimi.gmsdk.bean.reply.ConnectReplyPacket;
import com.xgimi.gmsdk.bean.reply.DeviceKeyboardStateChangePacket;
import com.xgimi.gmsdk.bean.reply.Packet;
import com.xgimi.gmsdk.bean.reply.PacketFactory;
import com.xgimi.gmsdk.bean.reply.ScreenShotPacket;
import com.xgimi.gmsdk.bean.reply.SearchReplyPacket;
import com.xgimi.gmsdk.bean.send.GMKeyCommand;
import com.xgimi.gmsdk.bean.send.GMKeyCommandFactory;
import com.xgimi.gmsdk.bean.send.VcontrolCmd;
import com.xgimi.gmsdk.callback.GMConnectListeners;
import com.xgimi.gmsdk.callback.IGMDeviceConnectedListener;
import com.xgimi.gmsdk.callback.IGMDeviceFoundListener;
import com.xgimi.gmsdk.callback.IGMReceivedScreenShotResultListener;


import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * Created by Tommy on 2017/7/26.
 */

public class ConnectManager {

    static final String[] FILE_TYPE_ARR = {".doc", ".txt", ".pdf", ".ppt", ".xls"};
    private static final int LOCAL_PORT = 16751;
    private static final int REMOTE_PORT = 16750;
    private static final int REMOTE_KEY_PORT = 16735;
    private static final int LOCAL_KEY_PORT = 0;
    private static final int TOUCH_PACKATE_PORT = 16737;
    private static final int TOUCH_SOCKET_BIND_PORT = 15557;

    private static final int SEARCH_PORT = 16752;

    private static final int FOUND_DEVICE_BY_UDP = 0;
    private static final int FOUND_DEVICE_BY_NSD = 1;
    private static final int SEARCH_SPACE_TIME = 2000;
    private static final int AUTO_CONNECT_SPACE_TIME = 2000;


    private static ConnectManager mConnectManager;
    private DatagramSocket mSocket;
    private DatagramSocket mKeySocket;
    private DatagramSocket mTouchMouseSocket;

    private BlockingQueue<Packet> mRecvPacketQueue;


    //////////////////  Thread  ////////////
    private RecvThread mRecvThread;
    private ReadThread mReadThread;


    ///////////////////// Flags  /////////////
    private boolean mHashConnectDevice;
    private boolean mNeedHandFoundDevice;
    private boolean mDeviceKeyboardOpen;
    private boolean mAuthenticationAllow;

    private IGMDeviceFoundListener mOnDeviceFoundListener;
    private IGMDeviceConnectedListener mOnDeviceConnectedListener;
    private IGMReceivedScreenShotResultListener mOnReceivedScreenShotListener;


    /**
     * 连接的设备的ip
     */
    private String mRemoteDeviceIP;
    private GMDevice mConnectedDevice;


    ///////////////////////   Handler    //////////////
    private Handler mDeviceFoundHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (mOnDeviceFoundListener != null && mNeedHandFoundDevice) {
                SearchReplyPacket packet = (SearchReplyPacket) msg.obj;
                GMDevice device = new GMDevice();
                device.setIp(packet.getDeviceInfo().getDeviceip());
                device.setName(packet.getDeviceInfo().getDevicename());
                device.setType(packet.getDeviceInfo().getDevicetype());
                device.setVersionCode(packet.getDeviceInfo().getVersioncode());
                XGIMILOG.E("搜索到设备 : " + device.getIp());
                mOnDeviceFoundListener.onDeviceFound(device);
            }
        }
    };

    private Handler mDeviceConnectHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (mOnDeviceConnectedListener != null) {
                mAutoConnectHandler.removeMessages(0);
                mConnectTimeOutHandler.removeMessages(0);
                ConnectReplyPacket packet = (ConnectReplyPacket) msg.obj;
                GMDevice device = new GMDevice();
                device.setIp(packet.getDeviceInfo().getIpaddress());
                device.setType(packet.getDeviceInfo().getDevicetype());
                device.setMac(packet.getDeviceInfo().getMac());
                device.setVersionCode(packet.getDeviceInfo().getVersioncode());
                mRemoteDeviceIP = device.getIp();
                XGIMILOG.E("连接到设备 : " + mRemoteDeviceIP);
                mConnectedDevice = device;
                mHashConnectDevice = true;
                mOnDeviceConnectedListener.onDeviceConnected(device);
            }
//            try {
//                sendNormalCommand(new Gson().toJson(new VcontrolCmd(null, "2", new VcontrolCmd.ControlCmd(7, 3, 0), 20000)));//获取App列表
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
        }
    };

    private Handler mReplyAppListHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            AppListReply packet = (AppListReply) msg.obj;
            String url = String.format(packet.getImagePath(), mRemoteDeviceIP);
        }
    };

    private Handler mDeviceKeyboardStateChangeHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                mDeviceKeyboardOpen = true;
            } else {
                mDeviceKeyboardOpen = false;
            }
        }
    };

    private Handler mScreenShotHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            ScreenShotPacket packet = (ScreenShotPacket) msg.obj;
            String screenUrl = String.format(packet.getImagePath(), mRemoteDeviceIP);
            XGIMILOG.E("截图成功，url = " + screenUrl);
            if (screenUrl != null && mOnReceivedScreenShotListener != null) {
                mOnReceivedScreenShotListener.onReceivedScreenShotResult(screenUrl);
            }
        }
    };


    private Handler mAutoSearchHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            try {
                startSearchDevice();
                mAutoSearchHandler.removeMessages(0);
                mAutoSearchHandler.sendEmptyMessageDelayed(0, SEARCH_SPACE_TIME);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    };

    private Handler mSearchTimeOutHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            mNeedHandFoundDevice = false;
            mAutoSearchHandler.removeMessages(0);
            if (mOnDeviceFoundListener != null) {
                mOnDeviceFoundListener.onTimeOut();
                mOnDeviceFoundListener = null;
            }
        }
    };


    private String mConnectDestIp;

    private Handler mAutoConnectHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            try {
                if (CommonUtil.isIpAddress(mConnectDestIp)) {
                    connectDevice(mConnectDestIp);
                    mAutoConnectHandler.removeMessages(0);
                    mAutoConnectHandler.sendEmptyMessageDelayed(0, AUTO_CONNECT_SPACE_TIME);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    public Handler mConnectTimeOutHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            mAutoConnectHandler.removeMessages(0);
            if (mOnDeviceConnectedListener != null) {
                mOnDeviceConnectedListener.onTimeOut();
                mOnDeviceConnectedListener = null;
            }
        }
    };


    private ConnectManager() {
        try {
            if (mRecvPacketQueue == null) {
                mRecvPacketQueue = new ArrayBlockingQueue<Packet>(100);
            }
            if (mSocket == null) {
                mSocket = new DatagramSocket(LOCAL_PORT);
                mSocket.setReuseAddress(true);
                mSocket.setReceiveBufferSize(1024 * 1024 * 3);
                if (mRecvThread == null) {
                    mRecvThread = new RecvThread();
                }
                mRecvThread.startRecvThread();

                if (mReadThread == null) {
                    mReadThread = new ReadThread();
                }
                mReadThread.startReadThread();
            }

            if (mKeySocket == null) {
                mKeySocket = new DatagramSocket(LOCAL_KEY_PORT);
                mKeySocket.setReuseAddress(true);
                mKeySocket.setReceiveBufferSize(1024);
            }

            if (mTouchMouseSocket == null) {
                mTouchMouseSocket = new DatagramSocket(TOUCH_SOCKET_BIND_PORT);
                mTouchMouseSocket.setReuseAddress(true);
                mTouchMouseSocket.setReceiveBufferSize(1024);
            }

        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    public synchronized static ConnectManager getConnectManager() {
        if (mConnectManager == null) {
            XGIMILOG.initTag("XGIMI_Assistant", true);
            mConnectManager = new ConnectManager();
        }
        return mConnectManager;
    }

    /**
     * 鉴权
     *
     * @param appid
     * @param secrete
     * @return 鉴权结果
     */
    public boolean initAuthentication(String appid, String secrete) {
//        if (appid != null && secrete != null) {
//            mAuthenticationAllow = GMCheckAuthentication.check(appid, secrete);
//        } else {
//            mAuthenticationAllow = false;
//        }
        mAuthenticationAllow = true;
        return mAuthenticationAllow;
    }


    void startSearchDevice() throws Exception {
        mNeedHandFoundDevice = true;
        sendSearchCommand();
    }

    void stopSearchDevice() {
        mNeedHandFoundDevice = false;
        XGIMILOG.E("Stop Search");
    }

    void setOnDeviceFoundListener(IGMDeviceFoundListener listener) {
        this.mOnDeviceFoundListener = listener;
    }

    void setOnDeviceConnectListener(IGMDeviceConnectedListener listener) {
        this.mOnDeviceConnectedListener = listener;
    }

    void connectDevice(String ip) throws Exception {
        sendConnectCommand(ip);
    }

    boolean isConnectedToDevice() {
        if (mRemoteDeviceIP == null || mRemoteDeviceIP.equals("") || !mAuthenticationAllow) {
            mHashConnectDevice = false;
        }
        return mHashConnectDevice;
    }

    public String getConnectedDeviceIp() {
        return mRemoteDeviceIP;
    }


    /**
     * 发送普通命令
     *
     * @param cmd 命令json
     * @return true:发送成功, false:还未连接，发送失败
     */
    void sendNormalCommand(final String cmd) throws Exception {
        if (!isConnectedToDevice()) {
            throw new Exception("Device Not Connetced");
        } else {
            new Thread() {
                @Override
                public void run() {
                    try {
                        byte[] buf = cmd.getBytes();
                        DatagramPacket datagramPacket = new DatagramPacket(buf, buf.length);
                        datagramPacket.setPort(REMOTE_PORT);
                        datagramPacket.setAddress(InetAddress.getByName(mRemoteDeviceIP));
                        mSocket.send(datagramPacket);
                        XGIMILOG.E("\n--------------------------------" +
                                "\nPhone Send To " + mRemoteDeviceIP + " : " + cmd +
                                "\n---------------------------------"
                        );
                    } catch (UnknownHostException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }.start();
        }
    }


    /**
     * 发送按键命令
     *
     * @param keyCommand
     * @throws Exception
     */
    void sendKeyCommand(final GMKeyCommand keyCommand) throws Exception {
        if (!isConnectedToDevice()) {
            throw new Exception("Device Not Connected");
        } else {
            new Thread() {
                @Override
                public void run() {
                    try {
                        byte[] buf = GMKeyCommandFactory.createKeyCommand(keyCommand).getBytes();
                        DatagramPacket datagramPacket = new DatagramPacket(buf, buf.length);
                        datagramPacket.setPort(REMOTE_KEY_PORT);
                        datagramPacket.setAddress(InetAddress.getByName(mRemoteDeviceIP));
                        mKeySocket.send(datagramPacket);
                        XGIMILOG.E("\n--------------------------------" +
                                "\nPhone Send To " + mRemoteDeviceIP + " : " + new String(buf, 0, buf.length) +
                                "\n---------------------------------"
                        );
                    } catch (UnknownHostException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }.start();
        }
    }

    /**
     * 发送连接命令
     *
     * @param destIP
     */
    private void sendConnectCommand(final String destIP) throws Exception {
        if (!mAuthenticationAllow) {
            throw new Exception("Need Authentication");
        } else {
            new Thread() {
                @Override
                public void run() {
                    try {
                        byte[] buf = new Gson().toJson(new VcontrolCmd(10000, "1", "1", null, null, null, null, null)).getBytes();
                        DatagramPacket datagramPacket = new DatagramPacket(buf, buf.length);
                        datagramPacket.setPort(REMOTE_PORT);
                        datagramPacket.setAddress(InetAddress.getByName(destIP));
                        mSocket.send(datagramPacket);
                        XGIMILOG.E("Send Connect Cmd To " + destIP);
                    } catch (UnknownHostException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }.start();
        }
    }

    /**
     * 发送搜索命令
     */
    private void sendSearchCommand() throws Exception {
        if (!mAuthenticationAllow) {
            throw new Exception("Need Authentication");
        } else {
            new Thread() {
                @Override
                public void run() {
                    MulticastSocket searchSocket = null;
                    try {
                        byte[] buf = new Gson().toJson(new VcontrolCmd(9998, "1", "1", null, null, null, null, null)).getBytes();
                        searchSocket = new MulticastSocket();
                        DatagramPacket searchDp = new DatagramPacket(buf, buf.length, InetAddress.getByName("255.255.255.255"), SEARCH_PORT);
                        searchSocket.send(searchDp);
                        XGIMILOG.E("Send Search Cmd");
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        if (searchSocket != null) {
                            searchSocket.close();
                        }
                    }
                }
            }.start();
        }
    }

    void sendTouchMouse(final String touchMsg) throws Exception {
        if (!isConnectedToDevice()) {
            throw new Exception("Device Not Connected");
        } else {
            new Thread() {
                @Override
                public void run() {
                    try {
                        byte[] buf = touchMsg.getBytes();
                        DatagramPacket datagramPacket = new DatagramPacket(buf, buf.length);
                        datagramPacket.setPort(TOUCH_PACKATE_PORT);
                        datagramPacket.setAddress(InetAddress.getByName(mRemoteDeviceIP));
                        mTouchMouseSocket.send(datagramPacket);
                    } catch (UnknownHostException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }.start();
        }
    }


    boolean isKeyboardOpen() {
        return mDeviceKeyboardOpen;
    }

    void setOnReceivedScreenShotResultListener(IGMReceivedScreenShotResultListener listener) {
        this.mOnReceivedScreenShotListener = listener;
    }

    void startSearchDevice(int timeOut) throws Exception {
        startSearchDevice();
        mAutoSearchHandler.removeMessages(0);
        mAutoSearchHandler.sendEmptyMessageDelayed(0, SEARCH_SPACE_TIME);
        mSearchTimeOutHandler.removeMessages(0);
        mSearchTimeOutHandler.sendEmptyMessageDelayed(0, timeOut);
    }

    void connectDevice(String ip, int timeOut) throws Exception {
        mConnectDestIp = ip;
        connectDevice(ip);
        mAutoConnectHandler.removeMessages(0);
        mAutoConnectHandler.sendEmptyMessageDelayed(0, AUTO_CONNECT_SPACE_TIME);
        mConnectTimeOutHandler.removeMessages(0);
        mConnectTimeOutHandler.sendEmptyMessageDelayed(0, timeOut);
    }

    public GMDevice getConnectedDevice() {
        return mConnectedDevice;
    }

    /**
     * 接收线程
     */
    class RecvThread extends Thread {
        boolean isRunning = false;

        @Override
        public void run() {
            XGIMILOG.E("Start Received Thread");
            while (isRunning) {
                byte[] buf = new byte[1024 * 1024 * 3];
                DatagramPacket datagramPacket = new DatagramPacket(buf, buf.length);
                try {
                    mSocket.receive(datagramPacket);
                    String recvString = new String(datagramPacket.getData(), 0, datagramPacket.getData().length).trim();
                    mRecvPacketQueue.add(new Packet(recvString, datagramPacket.getAddress().toString().replace("/", "")));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        void startRecvThread() {
            if (!isRunning && mSocket != null) {
                isRunning = true;
                this.start();
            }
        }
    }

    /**
     * 从阻塞队列读取消息的线程
     */
    class ReadThread extends Thread {
        private boolean isRunning = false;

        @Override
        public void run() {
            XGIMILOG.E("Start Read Msg Thread");
            while (isRunning) {
                try {
                    Packet packet = mRecvPacketQueue.take();
                    Packet readPacket = PacketFactory.genaratePacket(packet);
                    XGIMILOG.E("\n--------------------------------" +
                            "\nPhone Received " + packet.getRealIP() + " : " + packet.getMsg() +
                            "\n--------------------------------"
                    );
                    if (readPacket instanceof SearchReplyPacket) {
                        /**
                         * 返回的消息为搜索到设备的回调
                         */
                        Message msg = new Message();
                        msg.what = FOUND_DEVICE_BY_UDP;
                        msg.obj = readPacket;
                        mDeviceFoundHandler.sendMessage(msg);
                    } else if (readPacket instanceof ConnectReplyPacket) {
                        /**
                         * 返回的消息是连接成功的回调
                         */
                        Message msg = new Message();
                        msg.obj = readPacket;
                        mDeviceConnectHandler.sendMessage(msg);
                    } else if (readPacket instanceof AppListReply) {
                        /**
                         * 返回的消息是获取App列表成功
                         */
                        Message msg = new Message();
                        msg.obj = readPacket;
                        mReplyAppListHandler.sendMessage(msg);
                    } else if (readPacket instanceof DeviceKeyboardStateChangePacket) {
                        /**
                         * 设备端输入框反馈
                         */
                        mDeviceKeyboardStateChangeHandler.sendEmptyMessage(((DeviceKeyboardStateChangePacket) readPacket).getAction());
                    } else if (readPacket instanceof ScreenShotPacket) {
                        /**
                         * 截图
                         */
                        Message msg = new Message();
                        msg.obj = readPacket;
                        mScreenShotHandler.sendMessage(msg);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    Thread.sleep(5);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        public void startReadThread() {
            if (!isRunning) {
                isRunning = true;
                this.start();
            }
        }
    }
}
