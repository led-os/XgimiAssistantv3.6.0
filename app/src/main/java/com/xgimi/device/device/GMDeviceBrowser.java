package com.xgimi.device.device;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.apple.dnssd.BrowseListener;
import com.apple.dnssd.DNSSD;
import com.apple.dnssd.DNSSDException;
import com.apple.dnssd.DNSSDService;
import com.apple.dnssd.ResolveListener;
import com.apple.dnssd.TXTRecord;
import com.google.gson.Gson;
import com.xgimi.device.callback.GMDeviceBrowserListener;
import com.xgimi.device.devicedetail.GMdeviceDetail;
import com.xgimi.device.socket.UdpManager;
import com.xgimi.device.socket.UdpPostSender;
import com.xgimi.device.utils.Consants;
import com.xgimi.device.utils.FileUtils;
import com.xgimi.device.utils.NsdUtil;
import com.xgimi.device.utils.StringUtils;
import com.xgimi.device.utils.ThreadPid;
import com.xgimi.device.vcontrolcmd.VcontrolControl;
import com.xgimi.zhushou.netUtil.NetUtil;
import com.xgimi.zhushou.util.XGIMILOG;

import org.json.JSONArray;
import org.json.JSONObject;
import org.xml.sax.helpers.XMLFilterImpl;

import java.io.File;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;

/**
 * 扫描设备类
 */
public class GMDeviceBrowser implements Runnable {

    private static final String TAG = "GMDeviceBrowser";
    private final String regType = "_xgimi._tcp";
    private String mdnsSo = "libmdnsbin.so";
    private ArrayList<String> listip = new ArrayList<String>();

    private String loacl_mdnspath;
    private Context mcontext;
    private String defaultname = Consants.defaultName;
    private GMDeviceBrowserListener cmdlisener;
    private boolean isStop = false;
    private long mDnssdThreadID;
    private DNSSDTask mDNSSDTask;

    static {
        System.loadLibrary("xmdnssd");
    }

    String multicastHost = "255.255.255.255";
    InetAddress receiveAddress;
    private MulticastSocket s;
    private DatagramSocket lisenSocket;
    public static GMDeviceBrowser brower;
    public static GMDeviceBrowser instance;

    public static GMDeviceBrowser getInstatnce(Context context) {
        if (instance == null) {
            instance = new GMDeviceBrowser(context);
        }
        return instance;
    }

    private GMDeviceBrowser(Context context) {
        this.mcontext = context;
//        conntct();
    }

    /**
     * @param lisener 搜索设备的监听
     */
    public void setDeviceBrowserListener(GMDeviceBrowserListener lisener) {
        this.cmdlisener = lisener;
    }

    /**
     * 开始搜索设备
     */
    public void start() {
        start1();
    }

    Object object = new Object();
    class SendSearchCommondThread extends Thread {
        @Override
        public void run() {
            synchronized (object) {
                sendCommd();
            }
        }
    }


    private void start1() {
        listip.clear();
        isStop = false;
//        UdpManager.getInstance();

        stopSearchThread();

        if (!UdpPostSender.hasConnect && !isRecvThreadRunning) {
            mSearchThread = new SearchThread();
            isRecvThreadRunning = true;
            mSearchThread.start();
            XGIMILOG.D("start1: has not Connect-------------------------------------------");
        } else {
            isRecvThreadRunning = true;
            XGIMILOG.D("start1: has connet------------------------------------------");
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                sendCommd();
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                sendCommd();
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                sendCommd();
            }
        }).start();

//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                // TODO Auto-generated method stub
//                sendCommd();
//            }
//        }).start();
        /**
         * DNSSD搜索
         */
//
//        if (mDNSSDTask == null) {
//            mDNSSDTask = new DNSSDTask();
//            mDNSSDTask.start();
//        }

//        new Thread() {
//            public void run() {
//                try {
//                    DNSSD.browse(regType, new BrowseListener() {
//
//                        private GMDevice device;
//
//                        @Override
//                        public void operationFailed(DNSSDService arg0, int arg1) {
//                        }
//
//                        @Override
//                        public void serviceLost(DNSSDService arg0, int arg1, int arg2, String arg3, String arg4, String arg5) {
//                        }
//
//                        @Override
//                        public void serviceFound(DNSSDService browser, int flags, int ifIndex, final String deviceip, String regType, String domain) {
//                            mDnssdThreadID = Thread.currentThread().getId();
//                            XGIMILOG.E("deviceip = " + deviceip + ", regType = " + regType + ", domain" + domain + ", Thread.currentThread() = " + mDnssdThreadID);
//                            if (isRecvThreadRunning) {
//                                GMDevice device = new GMDevice();
//                                device.setIp(deviceip);
//                                device.setName(defaultname);
//                                if (cmdlisener != null) {
//                                    cmdlisener.GMDevice(device, false);
//                                }
//                            }
//
////                            try {
////                                DNSSD.resolve(0, ifIndex, deviceip, regType, domain, new ResolveListener() {
////
////                                    @Override
////                                    public void operationFailed(DNSSDService arg0, int arg1) {
////                                    }
////
////                                    @Override
////                                    public void serviceResolved(DNSSDService arg0, int arg1, int arg2, String arg3, String arg4, int arg5,
////                                                                TXTRecord txtRecord) {
////
////                                        XGIMILOG.E("Thread.currentThread() = " + Thread.currentThread());
////                                        if (isRecvThreadRunning) {
////                                            String device_name = null;
////                                            String device_type = null;
////                                            String device_version = null;
////                                            device = new GMDevice();
////
////                                            if (txtRecord.getValue("devname") != null) {
////                                                device_name = new String(txtRecord.getValue("devname"));
////                                            }
////                                            if (txtRecord.getValue("deviceType") != null) {
////                                                device_type = new String(txtRecord.getValue("deviceType"));
////                                            }
////                                            if (txtRecord.getValue("deviceVersion") != null) {
////                                                device_version = new String(txtRecord.getValue("deviceVersion"));
////                                            }
////
////                                            if (StringUtils.isEmpty(device_name) || device_name.equals("XGIMI")) {
////                                                device_name = defaultname;
////                                            }
////                                            if (StringUtils.isEmpty(deviceip) || listip.contains(deviceip) || !NetUtil.isIpv4(deviceip)) {
////                                                return;
////                                            }
////                                            listip.add(deviceip);
////                                            device.setIp(deviceip);
////                                            device.setName(device_name);
////                                            XGIMILOG.E("通过dnssd找到设备 : " + new Gson().toJson(device));
////                                            if (cmdlisener != null) {
////                                                cmdlisener.GMDevice(device, false);
////                                            } else {
////                                                XGIMILOG.E("cmdlisener == null");
////                                            }
////                                        } else {
////                                            XGIMILOG.E("搜索已停止");
////                                        }
////                                    }
////                                });
////
////                            } catch (DNSSDException e) {
////                                e.printStackTrace();
////                            }
//                        }
//                    });
//                } catch (DNSSDException e) {
//                    e.printStackTrace();
//                }
//            }
//        }.start();

    }

    /**
     * 停止扫描
     */
    public void stop() {
        try {
            Runtime.getRuntime().exec("kill -9 " + loacl_mdnspath);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void conntct() {
        String assets_mdnspath = "so" + File.separator + mdnsSo;

        loacl_mdnspath = mcontext.getFilesDir().getAbsolutePath() + File.separator + mdnsSo;
        XGIMILOG.E("DNSSD thread : " + loacl_mdnspath);
        CopySofile(assets_mdnspath, loacl_mdnspath);

        //ThreadPid.startBin(loacl_mdnspath);


        ThreadPid.startBin(loacl_mdnspath);
//        try {
//
//            bindReport();
//            new Thread(this).start();
//        } catch (Exception e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }

    }

    private void bindReport() throws SocketException {
        if (lisenSocket == null) {
            lisenSocket = new DatagramSocket(16748);
            lisenSocket.setReuseAddress(true);
        }
    }


    private void CopySofile(String path, String to) {

        File file = new File(to);

        if (!file.exists()) {

            FileUtils.CopyAssetData(mcontext, path, to);

            try {
                String command = "chmod 777 " + loacl_mdnspath;
                Runtime runtime = Runtime.getRuntime();
                runtime.exec(command);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    public void run() {
        XGIMILOG.D("run: start brower old     ---------------------------------");

    }


    private DatagramSocket mSearchSocket;
    public boolean isRecvThreadRunning = false;
    private SearchThread mSearchThread;

    class SearchThread extends Thread {

        @Override
        public void run() {
            XGIMILOG.E("run: start search thread new ------------------");
            while (isRecvThreadRunning) {
                byte buf[] = new byte[1024];
                DatagramPacket dp = new DatagramPacket(buf, 1024);
                try {
                    if (mSearchSocket == null) {
                        mSearchSocket = new DatagramSocket(16751);
                        mSearchSocket.setReuseAddress(true);
                    }
                    mSearchSocket.receive(dp);
                    String recv = new String(dp.getData(), 0, dp.getLength()).trim();
//                    XGIMILOG.D("remote ip = " + dp.getAddress().toString() + "searchThread Recv : " + recv);
                    JSONObject recvJbt = new JSONObject(recv);
                    if (recvJbt.getInt("action") == 9999) {
                        JSONObject j = recvJbt.getJSONObject("deviceInfo");
                        GMDevice device = new GMDevice();
                        device.setIp(dp.getAddress().toString().replace("/", ""));
                        device.setName(j.getString("devicename"));
                        device.setDevicetype(j.getString("devicetype"));
                        device.setType(j.getString("devicetype"));
                        device.setVersion(j.getInt("versioncode"));
                        GMdeviceDetail gm = new GMdeviceDetail();
                        gm.setName(j.getString("devicename"));
                        gm.setIp(dp.getAddress().toString().replace("/", ""));
                        gm.setType(j.getString("devicetype"));
                        gm.setVersion(j.getInt("versioncode"));
                        if (cmdlisener != null) {
                            cmdlisener.GMDevice(device, true);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            mSearchSocket = null;
            mSearchThread = null;
        }
    }

    public void stopSearchThread() {
        XGIMILOG.E("停止搜索");
        isRecvThreadRunning = false;
        mSearchThread = null;
//        killThread();
    }


    private synchronized void sendCommd() {
        int port = 16752;
        String message = VcontrolControl.getInstance().getConnectControl(new VcontrolCmd(9998, "1", "1", null, null, null, null, null));
        try {
            InetAddress group = InetAddress.getByName(multicastHost);
            s = new MulticastSocket();
            DatagramPacket dp = new DatagramPacket(message.getBytes(),
                    message.length(), group, port);
            s.send(dp);
            s.close();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void killThread() {
        // 干掉找设备的线程
        try {
            Runtime.getRuntime().exec("kill -9 " + loacl_mdnspath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private class DNSSDTask extends Thread {

        private boolean mIsDNSSDStarted;

        @Override
        public void run() {
            try {
                mIsDNSSDStarted = true;
                DNSSD.browse(regType, new BrowseListener() {

                    private GMDevice device;

                    @Override
                    public void operationFailed(DNSSDService arg0, int arg1) {
                    }

                    @Override
                    public void serviceLost(DNSSDService arg0, int arg1, int arg2, String arg3, String arg4, String arg5) {
                    }

                    @Override
                    public void serviceFound(DNSSDService browser, int flags, int ifIndex, final String deviceip, String regType, String domain) {
                        mDnssdThreadID = Thread.currentThread().getId();
                        XGIMILOG.E("DNSSD ----------------- \ndeviceip = " + deviceip + ", regType = " + regType + ", domain" + domain + ", Thread.currentThread() = " + mDnssdThreadID);
                        if (isRecvThreadRunning) {
                            GMDevice device = new GMDevice();
                            device.setIp(deviceip);
                            device.setName(defaultname);
                            if (cmdlisener != null) {
                                cmdlisener.GMDevice(device, false);
                            }
                        }
                    }
                });
            } catch (DNSSDException e) {
                e.printStackTrace();
            }
        }

        public boolean isDNSSDStarted() {
            return mIsDNSSDStarted;
        }
    }
}
