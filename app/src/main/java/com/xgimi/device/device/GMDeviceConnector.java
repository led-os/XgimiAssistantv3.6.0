package com.xgimi.device.device;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.xgimi.device.callback.FeedBackAppGetInterface;
import com.xgimi.device.callback.FeedBackInforInterface;
import com.xgimi.device.callback.GMAppInstallLisener;
import com.xgimi.device.callback.GMDeviceAppChange;
import com.xgimi.device.callback.GMDeviceAppInstallListener;
import com.xgimi.device.callback.GMDeviceBrowserListener;
import com.xgimi.device.callback.GMDeviceConnectorListener;
import com.xgimi.device.callback.GMDeviceHeartLisener;
import com.xgimi.device.callback.GMDeviceMusicInfor;
import com.xgimi.device.callback.GMDeviceOldConnect;
import com.xgimi.device.callback.GMDeviceReceiveListener;
import com.xgimi.device.callback.GMDeviceSerialLisener;
import com.xgimi.device.callback.GMDeviceShowListener;
import com.xgimi.device.callback.HeartBit;
import com.xgimi.device.devicedetail.GMdeviceDetail;
import com.xgimi.device.socket.UdpManager;
import com.xgimi.device.util.GMGetSerialNumber;
import com.xgimi.device.util.HeartBeat;
import com.xgimi.device.utils.Consants;
import com.xgimi.device.utils.FileUtils;
import com.xgimi.device.vcontrolcmd.VcontrolControl;
import com.xgimi.zhushou.App;
import com.xgimi.zhushou.MainActivity;
import com.xgimi.zhushou.activity.CantConnectNewActivity;
import com.xgimi.zhushou.bean.ApplyDetail;
import com.xgimi.zhushou.netUtil.NetUtil;
import com.xgimi.zhushou.util.SaveData;
import com.xgimi.zhushou.util.XGIMILOG;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;


/**
 * 连接设备类
 * class GMDeviceConnector
 */
public class GMDeviceConnector {

    private static final String TAG = "GMDeviceConnector";
    public static GMDeviceConnector gmdc;
    private int connectCount = 0;
    public GMDeviceConnectorListener gmdcl;
    public List<GMDeviceConnectorListener> mConnectListenerList;
    public GMDeviceReceiveListener recevice;
    public GMDeviceShowListener show;
    public GMDeviceMusicInfor musicl;
    public boolean isZhiChi = true;
    private GMDeviceSerialLisener macLiener;
    private GMDevice mGmdevice;

    private GMDeviceBrowserListener mNewDeviceFoundListener;

    private GMDeviceConnector() {
    }


    public void setmNewDeviceFoundListener(GMDeviceBrowserListener mNewDeviceFound) {
        this.mNewDeviceFoundListener = mNewDeviceFound;
    }

    /**
     * 每隔2秒钟重新发起连接
     */
    public Handler mReconnectHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            XGIMILOG.E("mIsConnecting : " + mIsConnecting);
            if (mIsConnecting) {
                XGIMILOG.E("开始自动重连 ---------------");
                connect(GMDeviceStorage.getInstance().gmdevice, false);
                mReconnectHandler.removeMessages(0);
                mReconnectHandler.sendEmptyMessageDelayed(0, 2000);
            }
        }
    };

    public boolean mIsConnecting = false;

    /**
     * @param gmd 连接设备的监听
     */
    public void setGMDeviceConnectorListener(GMDeviceConnectorListener gmd) {
        this.gmdcl = gmd;
    }

    public void addOnConnectListener(GMDeviceConnectorListener connectorListener) {
        if (connectorListener == null) {
            return;
        }
        if (this.mConnectListenerList == null) {
            mConnectListenerList = new ArrayList<GMDeviceConnectorListener>();
        }
        if (mConnectListenerList.size() == 0) {
            mConnectListenerList.add(connectorListener);
        } else {
            for (int i = 0; i < mConnectListenerList.size(); i++) {
                GMDeviceConnectorListener listener = mConnectListenerList.get(i);
                if (listener == null) {
                    mConnectListenerList.remove(i);
                } else {
                    if (listener.getVersion() == connectorListener.getVersion()) {
                        mConnectListenerList.remove(i);
                        XGIMILOG.E("移除相同监听");
                    }
                }
            }
            mConnectListenerList.add(connectorListener);
        }

//        if (connectorListener != null && !mConnectListenerList.contains(connectorListener)) {
//            mConnectListenerList.add(connectorListener);
//        }
        XGIMILOG.D("mConnectListenerList.size() = " + mConnectListenerList.size());
    }

    private void notifyAllConnectListener(GMDevice device) {
        XGIMILOG.D("连接成功，通知所有监听器");
        mIsConnecting = false;//连接成功
        mReconnectHandler.removeMessages(0);
        if (mConnectListenerList == null) {
            return;
        }
        for (int i = 0; i < mConnectListenerList.size(); i++) {
            if (mConnectListenerList.get(i) != null) {
                mConnectListenerList.get(i).deviceConnected(device);
            }
        }
    }

    private void notifyAllConnectListener(GMDevice device, int failedReason) {
        XGIMILOG.D("");
        if (mConnectListenerList == null) {
            return;
        }
        for (int i = 0; i < mConnectListenerList.size(); i++) {
            if (mConnectListenerList.get(i) != null) {
                mConnectListenerList.get(i).deviceNotConnected(device, failedReason);
            }
        }
    }

    //接收消息的监听
    public void setGmRecivceListener(GMDeviceReceiveListener rece) {
        this.recevice = rece;
    }

    public void setGMDeviceShow(GMDeviceShowListener show) {
        this.show = show;
    }

    public void setGmDeviceMusic(GMDeviceMusicInfor info) {
        this.musicl = info;
    }

    public GMDeviceAppInstallListener lisner;

    //检测flyme是否已安装
    public void setGmFlymeLisener(GMDeviceAppInstallListener lisener) {
        this.lisner = lisener;
    }

    //mac地址的监听
    public void setGmSerialNumberLisener(GMDeviceSerialLisener lisener) {
        this.macLiener = lisener;
    }

    //
    GMDeviceHeartLisener heartLisener;

    public void setGmdeiveceLost(GMDeviceHeartLisener lisener) {
        this.heartLisener = lisener;
    }

    //获取新版接口返回的json
    FeedBackInforInterface feedBackInforInterface;

    public void setFeedBackInforInterface(FeedBackInforInterface feedBackInforInterface) {
        this.feedBackInforInterface = feedBackInforInterface;
    }

    GMDeviceOldConnect oldLisener;

    public void setOldLisener(GMDeviceOldConnect liser) {
        this.oldLisener = liser;
    }

    //设置app的监听
    public FeedBackAppGetInterface feedBackInforInterfa;

    public void setFeedBackInforLisener(FeedBackAppGetInterface inforLisener) {
        this.feedBackInforInterfa = inforLisener;
    }

    public GMAppInstallLisener appLisener;

    public void setGMAppInstallLisener(GMAppInstallLisener liserner) {
        this.appLisener = liserner;
    }

    /**
     * 得到连接设备类
     *
     * @return
     */
    public static GMDeviceConnector getInstance() {
        if (gmdc == null) {
            gmdc = new GMDeviceConnector();
        }
        return gmdc;
    }

    public void startListsner() {
//		UdpManager.getInstance().initUdpManager(gm.getIp(), handler);
    }

    /**
     * 连接设备通过传入的设备类
     *
     * @param gm 极米设备类
     */
    public void connect(GMDevice gm) {
        XGIMILOG.D("connect by gm: " + gm.toString());
        isOldConnect = false;
        connectnestat = false;
        HeartBeat.getInstance().isReceiveHeatBeat = true;
        GMDeviceStorage.getInstance().gmdevice = gm;
        mGmdevice = gm;
        Consants.constatus = false;
        connectCount = 0;
        if (handler != null) {
            UdpManager.getInstance().initUdpManager(gm.getIp(), handler);
        }
        isSend = false;
        UdpManager.getInstance().sendJCNewCommand(VcontrolControl.getInstance().getConnectControl(new VcontrolCmd(10000, "1", "1", null, null, null, null, null)));
        handler.sendEmptyMessageDelayed(Consants.CONNECTSERVICE, 1000);
        if (!mIsConnecting) {//第一次发起连接
            XGIMILOG.E("第一次连接，启动自动重连");
            mIsConnecting = true;
            mReconnectHandler.removeMessages(0);
            mReconnectHandler.sendEmptyMessageDelayed(0, 2000);
        }
    }

    public void connect(GMDevice gm, boolean autoReconnect) {
        XGIMILOG.D("connect by gm: " + gm.toString());
        isOldConnect = false;
        connectnestat = false;
        HeartBeat.getInstance().isReceiveHeatBeat = true;
        GMDeviceStorage.getInstance().gmdevice = gm;
        mGmdevice = gm;
        Consants.constatus = false;
        connectCount = 0;
//        if (handler != null && !mIsConnecting) {
//            UdpManager.getInstance().initUdpManager(gm.getIp(), handler);
//        }

        if (handler != null && !mIsConnecting) {
            UdpManager.getInstance().initUdpManager(gm.getIp(), handler);
        } else {
        }

        isSend = false;
        UdpManager.getInstance().sendJCNewCommand(VcontrolControl.getInstance().getConnectControl(new VcontrolCmd(10000, "1", "1", null, null, null, null, null)));
        handler.sendEmptyMessageDelayed(Consants.CONNECTSERVICE, 1000);
        if (!mIsConnecting && autoReconnect) {//第一次发起连接
            XGIMILOG.E("第一次连接，启动自动重连");
            mIsConnecting = true;
            mReconnectHandler.removeMessages(0);
            mReconnectHandler.sendEmptyMessageDelayed(0, 2000);
        }
    }

    /**
     * 是否需要支持心跳
     */

    public void setHeartBeatEnable(boolean isEnable) {
        Consants.HeatBeatStatus = isEnable;
    }

    /**
     * 连接设备通过传入的ip
     *
     * @param ip 手动连接的ip
     */
    public void connect(String ip) {
        Log.d(TAG, "connect: by ip : " + ip);
        isOldConnect = false;
        connectnestat = false;
        GMDevice gm = new GMDevice(ip, "极米无屏电视");
        mGmdevice = gm;
        HeartBeat.getInstance().isReceiveHeatBeat = true;

        if (!FileUtils.isIPAddress(ip)) {
            notifyAllConnectListener(gm, Consants.CONNECIPFAILUE);
            return;
        }
        isSend = false;
        connectnestat = false;
        GMDeviceStorage.getInstance().gmdevice = gm;
        Consants.constatus = false;
        connectCount = 0;
        if (handler != null && !mIsConnecting) {
            XGIMILOG.E("--------");
            UdpManager.getInstance().initUdpManager(gm.getIp(), handler);
        } else {
            XGIMILOG.E("........");
        }
        UdpManager.getInstance().sendJCNewCommand(VcontrolControl.getInstance().getConnectControl(new VcontrolCmd(10000, "1", "1", null, null, null, null, null)));
        handler.sendEmptyMessageDelayed(Consants.CONNECTSERVICE, 1000);
        if (!mIsConnecting) {//第一次发起连接
            XGIMILOG.E("第一次连接，启动自动重连");
            mIsConnecting = true;
            mReconnectHandler.removeMessages(0);
            mReconnectHandler.sendEmptyMessageDelayed(0, 2000);
        }
    }

    public GMDeviceAppChange app;

    public void setGMDeviceAppChange(GMDeviceAppChange change) {
        this.app = change;
    }

    public void getSerialNumber(String ip) {
        UdpManager.getInstance().initUdpManager(ip, handler);
        GMGetSerialNumber.getInstance().getSerialNumber();
    }

    /**
     * 断开与设备的连接
     */
    public void disconnect() {
//		GMDeviceStorage.getInstance().gmdevice=null;
        Consants.constatus = false;
    }

    public boolean connectnestat = false;

    private boolean isOldConnect;

    private boolean isSend = false;

    Handler handler = new Handler() {
        private HeartBit bean;

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Consants.CONNECTSERVICE:
                    connectCount++;
                    if (connectCount < 12 && connectCount > 6 && !connectnestat && !isSend) {
                        isOldConnect = false;
                        UdpManager.getInstance().sendJCommand("GETVERSION");
                        isSend = true;
                    }

                    if (connectCount > 12 && !connectnestat) {
                        if (gmdcl != null && !isOldConnect) {
//                            gmdcl.deviceNotConnected(GMDeviceStorage.getInstance().gmdevice, Consants.CONNECTFAILURE);
                            notifyAllConnectListener(GMDeviceStorage.getInstance().gmdevice, Consants.CONNECTFAILURE);
//					GMDeviceStorage.getInstance().gmdevice=null;
                            connectnestat = true;
                        }
                    }
                    if (!Consants.constatus && connectCount < 13) {
                        handler.sendEmptyMessageDelayed(Consants.CONNECTSERVICE, 1000);
                    }
                    break;
                case UdpManager.CONNECTED:
                    Log.d(TAG, "handleMessage: connected");
                    Consants.constatus = true;
                    isOldConnect = true;
                    GMDeviceStorage.getInstance().gmdevice = mGmdevice;
                    String messageInfo = (String) msg.obj;
                    // 自身ip
                    String phone_ip = messageInfo
                            .substring(messageInfo.indexOf("+") + 1);
                    if (phone_ip != null && !phone_ip.equals("")) {
                        if (GMDeviceStorage.getInstance().gmdevice != null) {
//                            GMDeviceStorage.getInstance().gmdevice.setPhoneIp(App.getContext().getPhoneIp());
                            App.mPhoneIP = App.getContext().getPhoneIp();
                        }
                    }
                    if (gmdcl != null && !connectnestat) {
                        notifyAllConnectListener(GMDeviceStorage.getInstance().gmdevice);
                    }
                    break;
                case UdpManager.RECEIVEDEVICETYPE:
                    String message1 = (String) msg.obj;
                    String devicetyp = message1
                            .substring(message1.indexOf("+") + 1);
                    if (devicetyp != null && !devicetyp.equals("")) {
                        if (GMDeviceStorage.getInstance().gmdevice != null) {
                            GMDeviceStorage.getInstance().gmdevice.setType(devicetyp);
                        }
                    }
                    if (devicetyp.equals("full_mstara3") || devicetyp.equals("mango") || devicetyp.equals("full_mango") || devicetyp.equals("mstarnapoli")) {
                        isZhiChi = false;
                    } else {
                        isZhiChi = true;
                    }
                    break;
                case UdpManager.RECEIVEDMESSAGE:
                    String message = (String) msg.obj;
//				GMDeviceStorage.getInstance().gmdevice.deviceType=(String) msg.obj;
                    if (message.startsWith("SCREENSHOT")) {
                        if (recevice != null) {
//					recevice.receive(msg);
                        }
                    } else if (message.contains("remoteinput")) {
                        String type = message.subSequence(message.length() - 1, message.length()).toString();
                        if (show != null) {
                            show.IsShow(type);
                        }
                    } else if (message.startsWith("{")) {
                        try {

                            jsonObject = new JSONObject(message);
                            int action = jsonObject.getInt("action");
                            if (action == 1001) {
                                if (heartLisener != null) {
                                    int version = jsonObject.optInt("version", 0);
                                    if (bean == null) {
                                        bean = new HeartBit(action, version);
                                    }
                                    bean.setAction(action);
                                    bean.setVersion(version);
                                    heartLisener.deviceLost(bean);
                                }
                            }
                            if (action == 206) {
                                JSONObject jsonObject2 = jsonObject.getJSONObject("data");
                                if (jsonObject2 != null) {
                                    String packageName = jsonObject2.optString("packageName", null);
                                    if (lisner != null) {
                                        lisner.receive(packageName);
                                    }
                                }
                            }
                            if (action == 400) {
//							JSONObject jsonObject2 = jsonObject.getJSONObject("data");
//							if(jsonObject2!=null){
//								String packageName=jsonObject2.optString("mac", null);
//							if(macLiener!=null){
//								Log.e("info",packageName);
//								macLiener.getMacSerialNumber(packageName,Consants.ip);
//							 }
//							}
                            }
                            if (appLisener != null) {
                                appLisener.appInstall(message);
                            }
                            if (musicl != null) {
                                musicl.musicInfor(message);
                            } else {

                            }
                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    } else if (message.startsWith("APPCHANGED")) {
                        if (app != null) {
                            app.receive(message);
                        }
                    }
                    break;
                case UdpManager.FOUND_NEW_DEVICE:
                    if (mNewDeviceFoundListener != null) {
                        // {"action":9999,"deviceInfo":{"deviceip":"192.168.0.178","devicename":"客厅",
                        // "devicetype":"mangomangosteen","versioncode":0},"msgid":"1"}
                        try {
                            JSONObject jbt = new JSONObject((String) msg.obj);
                            JSONObject j = jbt.getJSONObject("deviceInfo");
                            GMDevice device = new GMDevice();
                            device.setIp(j.getString("deviceip"));
                            device.setName(j.getString("devicename"));
                            device.setDevicetype(j.getString("devicetype"));
                            device.setType(j.getString("devicetype"));
                            device.setVersion(j.getInt("versioncode"));
                            GMdeviceDetail gm = new GMdeviceDetail();
                            gm.setName(j.getString("devicename"));
                            gm.setIp(j.getString("deviceip"));
                            gm.setType(j.getString("devicetype"));
                            gm.setVersion(j.getInt("versioncode"));
//                            Consants.gmdevicedetails.add(gm);
                            mNewDeviceFoundListener.GMDevice(device, true);
                        } catch (Exception e) {
                            Log.e(TAG, "handleMessage: " + e.getMessage());
                        }
                    }
                    break;
                case UdpManager.NEWCONNECT:
                    Consants.constatus = true;
                    GMDeviceStorage.getInstance().gmdevice = mGmdevice;
                    String connectInfor = (String) msg.obj;
                    Log.e("fanhuide////", connectInfor);
                    Gson gson = new Gson();
                    FeedbackInfo infor = gson.fromJson(connectInfor, FeedbackInfo.class);
                    if (infor.deviceInfo != null && GMDeviceStorage.getInstance().gmdevice != null) {
                        if (infor.deviceInfo.devicetype != null && !infor.deviceInfo.devicetype.equals(""))
                            if (infor.deviceInfo.ipaddress != null && !infor.deviceInfo.ipaddress.equals("")) {
                                App.mPhoneIP = App.getContext().getPhoneIp();
                            }
                        if (infor.deviceInfo.devicetype != null && !infor.deviceInfo.devicetype.equals(""))
                            GMDeviceStorage.getInstance().gmdevice.setType(infor.deviceInfo.devicetype);
                        GMDeviceStorage.getInstance().gmdevice.setVersion(infor.deviceInfo.versioncode);
                        if (infor.deviceInfo.device != null)
                            GMDeviceStorage.getInstance().gmdevice.setDevicetype(infor.deviceInfo.device);
                        if (infor.deviceInfo.mac != null) {
                            Log.e(TAG, infor.deviceInfo.mac);
                            SaveData.getInstance().deviceMac = infor.deviceInfo.mac;
                            GMDeviceStorage.getInstance().getConnectedDevice().setMac(infor.deviceInfo.mac);
                        }
                        if (mConnectListenerList != null && mConnectListenerList.size() > 0 && !connectnestat) {
                            Log.e(TAG, "发送心跳");
                            JSONObject j = null;
                            try {
                                j = new JSONObject(connectInfor);
                                int action = j.getInt("action");
                                if (action == 10001) {
                                    HeartBeat.getInstance().statrTimer();
                                    GMDeviceConnector.getInstance().setGmdeiveceLost(
                                            HeartBeat.getInstance());
                                    XGIMILOG.D("连接到 : " + GMDeviceStorage.getInstance().gmdevice.getIp());
                                    notifyAllConnectListener(GMDeviceStorage.getInstance().gmdevice);
                                } else if (action == 9999) {
                                    JSONObject jbt = j.getJSONObject("deviceInfo");
                                    GMDevice device = new GMDevice();
                                    device.setIp(jbt.getString("deviceip"));
                                    device.setName(jbt.getString("devicename"));
                                    device.setDevicetype(jbt.getString("devicetype"));
                                    device.setType(jbt.getString("devicetype"));
                                    device.setVersion(jbt.getInt("versioncode"));
                                    GMdeviceDetail gm = new GMdeviceDetail();
                                    gm.setName(jbt.getString("devicename"));
                                    gm.setIp(jbt.getString("deviceip"));
                                    gm.setType(jbt.getString("devicetype"));
                                    gm.setVersion(jbt.getInt("versioncode"));
//                                    Consants.gmdevicedetails.add(gm);
                                    if (mNewDeviceFoundListener != null) {
                                        XGIMILOG.D("mNewDeviceFoundListener != null----" + j.toString());
                                        mNewDeviceFoundListener.GMDevice(device, true);
                                    } else {
                                        XGIMILOG.D("mNewDeviceFoundListener == null----" + j.toString());
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    Log.d(TAG, "handleMessage: new connect action " + infor.action);
                    if (infor.action == 90000) {
                        Log.e(TAG, "卸载了东西");

                        EventBus.getDefault().post(new ApplyDetail());
                    }
                    //下载应用
                    if (feedBackInforInterface != null) {
                        Log.e(TAG, "3");
                        if (infor.downloadInfo != null) {
                            feedBackInforInterface.receive(infor);
                        }
                    }
                    //播放mv
                    if (infor.action == 30201 || infor.action == 20301) {
                        if (infor.playInfo != null) {
                            EventBus.getDefault().post(infor.playInfo);
                        }
                    }
                    if (infor.action == 30235) {
                        if (recevice != null) {
                            recevice.receive(infor);
                        }
                    }
                    //播放音乐
                    if (infor.action == 30211) {
                        if (infor.playInfo != null) {
                            EventBus.getDefault().post(infor.playInfo);
                        }
                    }
                    //下载文件进度
                    if (infor.action == 30234) {
                        if (infor.downloadInfo != null) {
                            EventBus.getDefault().post(infor);
                        }
                    }

                    //下载成功
                    if (infor.action == 30235) {
                        EventBus.getDefault().post(infor);
                    }
                    //获取返回的文件列表
                    if (infor.action == 30238) {
                        EventBus.getDefault().post(infor);
                    }

                    //获取返回的应用
                    if (infor.action == 30236) {
//                        XGIMILOG.D("返回的应用列表 : " + new Gson().toJson(infor));
//						Log.e("getUrl",infor.imagePath);
                        if (infor != null && infor.imagePath != null && feedBackInforInterfa != null) {
                            feedBackInforInterfa.getUrl(infor.imagePath);
                        }
                    }
                    //获取智能幕布开机时的状态
                    if (infor.action == 30237) {
                        EventBus.getDefault().post(infor);
                    }
                    //安装应用成功
                    if (infor.action == 30231) {
                        EventBus.getDefault().post(infor);
                        if (infor.installInfo != null && feedBackInforInterface != null) {
                            feedBackInforInterface.receive(infor);
                        }
                    }
                    //返回的心跳
                    if (infor.action == 10003) {
                        SaveData.getInstance().isNewTv = true;
                        Log.e(TAG, infor.action + "" + SaveData.getInstance().isNewTv);
                        HeartBeat.getInstance().stopTimer();
                    }
                    try {
//						JSONObject jsonObject=new JSONObject(connectInfor);
//						String msgId=jsonObject.optString("msgid");
//						if(msgId.equals("1")){
//							Gson gson=new Gson();
//							DeviceInfo deviceInfo=gson.fromJson(connectInfor,DeviceInfo.class);
//							GMDeviceStorage.getInstance().gmdevice.setPhoneIp(deviceInfo.deviceInfo.ipaddress);
//							GMDeviceStorage.getInstance().gmdevice.setName(deviceInfo.deviceInfo.devicetype);
//							GMDeviceStorage.getInstance().gmdevice.setVersion(deviceInfo.deviceInfo.versioncode);
//							if(gmdcl!=null&&!connectnestat){
//								HeartBeat.getInstance().statrTimer();
//								GMDeviceConnector.getInstance().setGmdeiveceLost(
//										HeartBeat.getInstance());
//								gmdcl.deviceConnected(GMDeviceStorage.getInstance().gmdevice);
//							}
//						}else if(msgId.equals("2")){
//
//						}
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                    break;
                default:
                    break;
            }
        }
    };

    private JSONObject jsonObject;

    public void removeConnectListener(GMDeviceConnectorListener connectListener) {
        if (mConnectListenerList != null && connectListener != null) {
            for (int i = 0; i < mConnectListenerList.size(); i++) {
                GMDeviceConnectorListener listener = mConnectListenerList.get(i);
                if (listener != null) {
                    if (listener == connectListener || listener.getVersion() == connectListener.getVersion()) {
                        XGIMILOG.E("remove connect listener : " + listener);
                        mConnectListenerList.remove(i);
                    }
                }
            }
            XGIMILOG.E("连接监听大小 : " + mConnectListenerList.size());
        }
    }

    public interface OnAppInstallStateChangeListener {
        void onAppInstallStateChange(int state);
    }

}
