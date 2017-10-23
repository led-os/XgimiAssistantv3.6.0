package com.xgimi.device.utils;

import android.content.Context;
import android.net.nsd.NsdManager;
import android.net.nsd.NsdServiceInfo;
import android.os.Build;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;
import com.xgimi.device.device.GMDevice;
import com.xgimi.zhushou.util.XGIMILOG;

import java.net.ServerSocket;

/**
 * Created by XGIMI on 2017/7/26.
 */

public class NsdUtil {

    private static final String DEFAULT_DEVICE_NAME = "极米无屏电视";
    private static final String LOCAL_SERVICE_NAME = "xgimi_assitant_phone";
    private static final String SERVICE_TYPE = "_xgimi._tcp";

    private static NsdManager.RegistrationListener nsRegListener;
    private static NsdManager.DiscoveryListener nsDicListener;
    private static Context mContext;
    private static IDeviceDiscoverListener mListsner;

    public static void init(Context context) {
        mContext = context;
        registerService();
    }

    /**
     * 注册
     */
    private static void registerService() {
        // 注意：注册网络服务时不要对端口进行硬编码，通过如下这种方式为你的网络服务获取
        // 一个可用的端口号.
        int port = 0;
        try {
            ServerSocket sock = new ServerSocket(0);
            port = sock.getLocalPort();
            sock.close();
        } catch (Exception e) {
//            Toast.makeText(getApplicationContext(), "can not set port", Toast.LENGTH_SHORT);
        }

        // 注册网络服务的名称、类型、端口
        NsdServiceInfo nsdServiceInfo = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
            nsdServiceInfo = new NsdServiceInfo();
            nsdServiceInfo.setServiceName(LOCAL_SERVICE_NAME);
            nsdServiceInfo.setServiceType(SERVICE_TYPE);
            nsdServiceInfo.setPort(port);
        }


        // 实现一个网络服务的注册事件监听器，监听器的对象应该保存起来以便之后进行注销
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            nsRegListener = new NsdManager.RegistrationListener() {
                @Override
                public void onUnregistrationFailed(NsdServiceInfo arg0, int arg1) {
                }

                @Override
                public void onServiceUnregistered(NsdServiceInfo arg0) {
                }

                @Override
                public void onServiceRegistered(NsdServiceInfo arg0) {
                }

                @Override
                public void onRegistrationFailed(NsdServiceInfo arg0, int arg1) {
                }
            };
        }
        // 获取系统网络服务管理器，准备之后进行注册
        NsdManager nsdManager = (NsdManager) mContext.getSystemService(Context.NSD_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            nsdManager.registerService(nsdServiceInfo, NsdManager.PROTOCOL_DNS_SD, nsRegListener);
        }
    }


    /**
     * 发现局域网中的设备
     */
    public static void discoverService(IDeviceDiscoverListener listsner) {
        mListsner = listsner;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            nsDicListener = new NsdManager.DiscoveryListener() {
                @Override
                public void onStopDiscoveryFailed(String serviceType, int errorCode) {
                }

                @Override
                public void onStartDiscoveryFailed(String serviceType, int errorCode) {

                }

                @Override
                public void onServiceLost(NsdServiceInfo serviceInfo) {
                }

                @Override
                public void onServiceFound(NsdServiceInfo serviceInfo) {
                    // 发现网络服务时就会触发该事件
                    // 可以通过switch或if获取那些你真正关心的服务
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        if (serviceInfo != null && !serviceInfo.getServiceName().equals(LOCAL_SERVICE_NAME) && serviceInfo.getServiceType().startsWith(SERVICE_TYPE)) {
                            XGIMILOG.E("Found device by nsd : " + new Gson().toJson(serviceInfo));
    //                        Device device = new Device();
    //                        device.setName(DEFAULT_DEVICE_NAME);
    //                        device.setIp(serviceInfo.getServiceName());
    //                        if (mListsner != null) {
    //                            mListsner.onDeviceDiscovered(device);
    //                        }
                        }
                    }
                }

                @Override
                public void onDiscoveryStopped(String serviceType) {
                }

                @Override
                public void onDiscoveryStarted(String serviceType) {
                }
            };
        }
        NsdManager nsdManager = (NsdManager) mContext.getSystemService(Context.NSD_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            nsdManager.discoverServices(SERVICE_TYPE, NsdManager.PROTOCOL_DNS_SD, nsDicListener);
        }
    }

    /**
     * 注销
     */
    public static void unregisterService() {
        NsdManager nsdManager = (NsdManager) mContext.getSystemService(Context.NSD_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            nsdManager.stopServiceDiscovery(nsDicListener); // 关闭网络发现
            nsdManager.unregisterService(nsRegListener);    // 注销网络服务
        }
    }

    public static void stopDiscovery() {
        NsdManager nsdManager = (NsdManager) mContext.getSystemService(Context.NSD_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN && nsDicListener != null) {
            nsdManager.stopServiceDiscovery(nsDicListener); // 关闭网络发现
        }
    }



    public static interface IDeviceDiscoverListener {
        void onDeviceDiscovered(GMDevice device);
    }
}
