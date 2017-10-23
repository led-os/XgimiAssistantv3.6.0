package com.waxrain.droidsender;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.media.MediaCodec;
import android.media.MediaCodecInfo;
import android.media.MediaFormat;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.preference.PreferenceManager;
import android.provider.Settings.Secure;
import android.util.Log;
import android.view.Surface;

import com.waxrain.droidsender.delegate.DelegateUPnP;
import com.waxrain.droidsender.delegate.Global;
import com.xgimi.zhushou.MainActivity;
import com.xgimi.zhushou.R;
import com.xgimi.zhushou.util.SaveData;
import com.xgimi.zhushou.util.XGIMILOG;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class SenderService extends Service {

    private static final String TAG = "SenderService";
    private static Context _self = null;
    public static boolean servStarted = false;
    public static boolean serviceExiting = false;

    public static int isMirrorFlingerSupport = 0; // 0/1/2
    public static final int CPU_UNKNOWN = 0;
    public static final int CPU_ARM = 1;
    public static final int CPU_MIPS = 2;
    public static final int CPU_X86 = 3;
    public static int cpu_Type = CPU_X86; // TODO: Google TV ?
    public static String cpu_Arch = "";
    public static String ssp = "";
    private static String GRABBER_BINNAME = "grabber";
    public static String FLINGER_DETECTFN = ".fg_attached";
    public static int mirrorServRequested = 0;
    public static boolean mirrorServPending = false;
    public static boolean mirrorServFirstStarted = false;
    public static boolean mirrorServStarted = false;
    public static int tfb = 0;
    public static boolean needUpgrade = true;
    public static int cug = 1;
    public static String BindOnIface = "";
    private static ArrayList<Message> serviceParam = null;
    private static boolean serviceParam_Locked = true;
    private SharedPreferences settings = null;
    private SharedPreferences.Editor editor = null;

    private int mHwEncErrors = 0, mHwEncErrorMax = 100;
    private Object mHwEncoder = null; // For Android below 4.1.1
    private Object mHwEncBuffInfo = null; // For Android below 4.1.1
    private ByteBuffer[] mHwEncInBuffers = null;
    private ByteBuffer[] mHwEncOutBuffers = null;
    private final long kHwEncInTimeOutUs = 500 * 1000; // For dequeueInputBuffer()
    private final long kHwEncOutTimeOutUs = 50 * 1000; // For dequeueOutputBuffer()
    private final long kHwEncRetryTimeOutUs = 3 * 1000; // For dequeueOutputBuffer()
    private long mHwEncStartMs = 0;
    private int mHwEncWidth = 0;
    private int mHwEncHeight = 0;
    private byte[] ya = null; // transfer YUV data in JNI
    private byte[] ea = null; // transfer H264 ES data in JNI
    public static int hws = 0;
    public static int hwrf = 0;
    private byte[] mCodecConfigData = null;
    public static String ad = null;
    public static DelegateUPnP upnp = null;
    public static List<Global.DeviceObj> dmrList = new ArrayList<Global.DeviceObj>();

    private static native int el(String ifname, String sfname, String ofname);

    public native int msa(String optmac, int source, int width, int height);

    public static native int msp(int quit);

    public native int gss();

    public static native int gsu();

    public static native String gsa();

    public native int sac(String accList, int preAuth);

    public static native int sl(int loc, String version); // 0:en/1:cn

    public native static int sr(int rot);

    public native static int sem(int mode);

    public native static void lkm(int index);

    public native static void ukm(int index);

    public static native String cd(String passwd, int codec);

    private native void gie(int dbgProxyMode);

    private native int cnc(String bindOn);

    public static native String ha();

    public static native String ia();

    public static native int eap(int enable, String fname);

    public static native int as(int isroot, int sdk, String savepath);

    public static native int fs(int isroot, int sdk, String savepath);

    public static native int rst(int delay); // Delayed restart

    public static native void asr(int mfmt);

    public static native int isa();

    static {
        try {
            Log.i(Global.LOGTAG, "droidsender(load libpreloader)");
            System.loadLibrary("preloader");
            Global.do_sleep(100);
            el("", "", ""); // test lib
            Global.preloaderLoaded = true;
        } catch (UnsatisfiedLinkError el) {
            el.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static final int mar = 1;
    public Handler hdl = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    };

    private String check_WiFiStaIface() {
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connMgr != null) {
            State wifiState = State.UNKNOWN;
            if (connMgr != null && connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI) != null)
                wifiState = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState();
            if (wifiState == State.CONNECTED)
                return "wlan0";
        }
        return null;
    }

    private String check_MobileIface() {
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connMgr != null) {
            State mobileState = State.UNKNOWN;
            if (connMgr != null && connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE) != null)
                mobileState = connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState();
            if (mobileState == State.CONNECTED)
                return "ppp0";
        }
        return null;
    }

    private String check_WiFiApIface() {
        WifiManager wifiMgr = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        if (wifiMgr != null) {
            try {
                Method method = wifiMgr.getClass().getMethod("isWifiApEnabled");
                boolean val = (Boolean) method.invoke(wifiMgr);
                if (val == true) // WIFI_AP_STATE_ENABLED
                    return "wlan0";
            } catch (Exception ex) {
            } catch (Throwable t) {
            }
            try {
                Method method = wifiMgr.getClass().getMethod("getWifiApState");
                int val = (Integer) method.invoke(wifiMgr);
                if (val == 13 || val == 3) // WIFI_AP_STATE_ENABLED == 13 || WIFI_STATE_ENABLED == 3
                    return "wlan0";
            } catch (Exception ex) {
            } catch (Throwable t) {
            }
        }
        return null;
    }

    private String check_AnyNetIface() {
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connMgr != null) {
            NetworkInfo info = connMgr.getActiveNetworkInfo();
            if (info != null && info.isAvailable())
                return "lo";
        }
        // ConnectivityManager doesn't work on Android 4.0 on MTK5502???
        return null;
    }

    private int NetworkTest() {
        if (check_WiFiStaIface() != null) {
            // In JNI mode, checkNetConnection() will auto skip 3G network adapters
            //Log.i(LOG_TAG, "WIFI network connected");
            return 1;
        } else if (check_MobileIface() != null) {
            //Log.i(LOG_TAG, "MOBILE network connected");
            /*if (Config.MOBILE_START == 0)
				return 0;*/
            return 2;
        } else if (check_WiFiApIface() != null) {
            //Log.i(LOG_TAG, "WIFI AP network started");
            return 3;
        } else if (check_AnyNetIface() != null) {
            //Log.i(LOG_TAG, "Some network connected");
            return 4;
        }
        return 255;
    }

    private void resetHwFlag() {
        if (hwrf != 0)
            hwrf = 0;
        hws = 0;
    }

    private void resetHwEncoder(boolean recreate, int width, int height, int if_interval, int fps, int bps) {
        try {
            if (mHwEncoder != null) {
                ((MediaCodec) mHwEncoder).stop();
                ((MediaCodec) mHwEncoder).release();
            }
        } catch (Exception e) {
        }
        mHwEncoder = null;
        mHwEncBuffInfo = null;
        mHwEncInBuffers = null;
        mHwEncOutBuffers = null;
        mHwEncWidth = 0;
        mHwEncHeight = 0;
        mHwEncErrors = 0;
        if (hws == 0)
            return;
        if (recreate == true) {
            String mime = "video/avc";
            try {
                mHwEncoder = (MediaCodec) MediaCodec.createEncoderByType(mime);
                if (mHwEncoder != null) {
                    MediaFormat mFormat = MediaFormat.createVideoFormat(mime, width, height);
                    mFormat.setInteger(MediaFormat.KEY_BIT_RATE, bps);
                    mFormat.setInteger(MediaFormat.KEY_FRAME_RATE, fps);
                    mFormat.setInteger(MediaFormat.KEY_COLOR_FORMAT, MediaCodecInfo.CodecCapabilities.COLOR_FormatYUV420SemiPlanar);
                    mFormat.setInteger(MediaFormat.KEY_I_FRAME_INTERVAL, if_interval);
                    ((MediaCodec) mHwEncoder).configure(mFormat, null, null/*crypto*/, MediaCodec.CONFIGURE_FLAG_ENCODE);
                    ((MediaCodec) mHwEncoder).start();
                    mHwEncBuffInfo = (MediaCodec.BufferInfo) new MediaCodec.BufferInfo();
                    mHwEncInBuffers = ((MediaCodec) mHwEncoder).getInputBuffers();
                    mHwEncOutBuffers = ((MediaCodec) mHwEncoder).getOutputBuffers();
                    Log.i(Global.LOGTAG, "i/o Buffers = " + mHwEncInBuffers.length + "/" + mHwEncOutBuffers.length);
                    mHwEncWidth = width;
                    mHwEncHeight = height;
                    hwrf = 0;
                    hws = 1;
                }
            } catch (Exception e) {
                resetHwFlag();
                resetHwEncoder(false, 0, 0, 0, 0, 0);
            }
        }
    }

    public int hwi(int width, int height, int if_interval, int fps, int bps) {
        boolean recreate = true;
        if (width <= 0 || height <= 0)
            recreate = false;
        resetHwEncoder(recreate, width, height, if_interval, fps, bps);
        mHwEncStartMs = 0;
        return 0;
    }

    public int hwe() {
        if (hws == 0)
            return -1;
        if (mHwEncoder == null)
            return -1;
        int ret = -1;
        ea = null;
        try {
            mHwEncInBuffers = ((MediaCodec) mHwEncoder).getInputBuffers();
            mHwEncOutBuffers = ((MediaCodec) mHwEncoder).getOutputBuffers();
            int inBufIndex = ((MediaCodec) mHwEncoder).dequeueInputBuffer(kHwEncInTimeOutUs);
            if (inBufIndex >= 0) {
                if (mHwEncStartMs <= 0)
                    mHwEncStartMs = System.currentTimeMillis();
                ByteBuffer dstBuf = mHwEncInBuffers[inBufIndex];
                int dstBuf_len = 0;
                dstBuf.clear();
                dstBuf.put(ya);
                dstBuf_len = ya.length;
                //dstBuf.flip(); // Will shrink buffer size?
                ((MediaCodec) mHwEncoder).queueInputBuffer(
                        inBufIndex,
                        0/*offset*/,
                        dstBuf_len,
                        (System.currentTimeMillis() - mHwEncStartMs) * 1000/*us*/,
                        0/*not EOS*/);
                int res = -1;
                long outputTimeout = kHwEncOutTimeOutUs;
                do {
                    res = ((MediaCodec) mHwEncoder).dequeueOutputBuffer((MediaCodec.BufferInfo) mHwEncBuffInfo, outputTimeout);
                    if (res >= 0) {
                        mHwEncErrors = 0;
                        ret = 1;
                        int outBufIndex = res;
                        ByteBuffer srcBuffer = mHwEncOutBuffers[outBufIndex];
                        int frm_size = ((MediaCodec.BufferInfo) mHwEncBuffInfo).size;
                        byte[] frm_data = new byte[frm_size];
                        srcBuffer.get(frm_data);
                        int frm_type = ((MediaCodec.BufferInfo) mHwEncBuffInfo).flags;
                        //Log.i(Global.LOGTAG,"GotFrame["+Global.byte2HexString(frm_data,6)+"]: index = "+outBufIndex+", type = "+frm_type+", size = "+frm_size);
                        if (frm_type == MediaCodec.BUFFER_FLAG_CODEC_CONFIG) {
                            mCodecConfigData = new byte[frm_size];
                            System.arraycopy(frm_data, 0, mCodecConfigData, 0, frm_size);
                            outputTimeout = kHwEncOutTimeOutUs;
                        } else {
                            if (frm_type == MediaCodec.BUFFER_FLAG_KEY_FRAME) {
                                if (mCodecConfigData != null && (frm_data[4] & 0x1f) != 7/*SPS*/) {
                                    byte[] frm2_data = new byte[frm_size + mCodecConfigData.length];
                                    System.arraycopy(mCodecConfigData, 0, frm2_data, 0, mCodecConfigData.length);
                                    System.arraycopy(frm_data, 0, frm2_data, mCodecConfigData.length, frm_size);
                                    frm_size = frm_size + mCodecConfigData.length;
                                    frm_data = frm2_data;
                                }
                            }
                            outputTimeout = 0;//kHwEncRetryTimeOutUs;
                        }
                        ((MediaCodec) mHwEncoder).releaseOutputBuffer(outBufIndex, false);
                        //if ((((MediaCodec.BufferInfo)mHwEncBuffInfo).flags & MediaCodec.BUFFER_FLAG_END_OF_STREAM) != 0)
                        //	ret = -1;
                        ea = frm_data;
                    } else if (res == MediaCodec.INFO_OUTPUT_BUFFERS_CHANGED) {
                        mHwEncErrors = 0;
                        res = ret = 0;
                        mHwEncOutBuffers = ((MediaCodec) mHwEncoder).getOutputBuffers();
                        Log.i(Global.LOGTAG, "i/o Buffers2 = " + mHwEncInBuffers.length + "/" + mHwEncOutBuffers.length);
                    } else if (res == MediaCodec.INFO_OUTPUT_FORMAT_CHANGED) {
                        //Log.i(Global.LOGTAG,"dequeueOutputBuffer: INFO_OUTPUT_FORMAT_CHANGED");
                        mHwEncErrors = 0;
                        ret = 0;
                        MediaFormat oformat = ((MediaCodec) mHwEncoder).getOutputFormat();
                        int width = oformat.getInteger(MediaFormat.KEY_WIDTH);
                        int height = oformat.getInteger(MediaFormat.KEY_HEIGHT);
                        Log.i(Global.LOGTAG, "vc: (" + width + "x" + height + ")");
                    } else if (res == MediaCodec.INFO_TRY_AGAIN_LATER) {
                        //Log.i(Global.LOGTAG,"dequeueOutputBuffer: INFO_TRY_AGAIN_LATER");
                        if (mHwEncErrors++ < mHwEncErrorMax) {
                            if (ret < 0)
                                ret = 0; // Last dequeueOutputBuffer may success
                        } else {
                            ret = -1;
                        }
                        //Global.do_sleep((int)kHwEncRetryTimeOutUs/1000);
                    }
                } while (res >= 0 && outputTimeout > 0);
            }
        } catch (Exception e) {
        }
        if (ret < 0) {
            resetHwFlag();
            resetHwEncoder(false, 0, 0, 0, 0, 0);
        }
        return ret;
    }

    private int imp(int width, int height) {
        if (android.os.Build.VERSION.SDK_INT < 21)
            return -1;
        Message msgInform = Message.obtain();
        msgInform.arg1 = width;
        msgInform.arg2 = height;
        msgInform.what = 1;
        if (MainActivity.mainHandler != null) {
            MainActivity.pendingRequest++;
            MainActivity.mainHandler.sendMessage(msgInform);
        }
        int timeout = 9000;
        while (timeout > 0) {
            if (MainActivity.mMediaProjection != null &&
                    MainActivity.mVirtualDisplay != null &&
                    MainActivity.pendingRequest <= 0)
                return 0;
            Global.do_sleep(100);
            timeout -= 100;
        }
        if (MainActivity.mMediaProjection != null &&
                MainActivity.mVirtualDisplay == null)
            return -255; // Maybe resolution is not supported
        return -1;
    }

    private int smp() {
        if (android.os.Build.VERSION.SDK_INT < 21)
            return -1;
        if (MainActivity.mainHandler != null)
            MainActivity.mainHandler.sendEmptyMessage(2);
        return 0;
    }

    private int rmd() {
        if (android.os.Build.VERSION.SDK_INT < 21)
            return -1;
        return MainActivity.readDone_ScreenCapture();
    }

    private byte[] rmp() {
        if (android.os.Build.VERSION.SDK_INT < 21)
            return null;
        return MainActivity.read_ScreenCapture();
    }

    private int wmp() {
        if (android.os.Build.VERSION.SDK_INT < 21)
            return 0;
        return MainActivity.get_ScreenCaptureWidth();
    }

    private int hmp() {
        if (android.os.Build.VERSION.SDK_INT < 21)
            return 0;
        return MainActivity.get_ScreenCaptureHeight();
    }

    private int ead(int enable) {
        return -1;
    }

    private int gsr() {
        if (MainActivity._display != null) {
            int rotation = MainActivity._display.getRotation();
            if (rotation == Surface.ROTATION_0)
                return 0;
            else if (rotation == Surface.ROTATION_90)
                return 90;
            else if (rotation == Surface.ROTATION_180)
                return 180;
            else if (rotation == Surface.ROTATION_270)
                return 270;
        }
        return 0;
    }

    private void checkGrabberInit() {
        if (getGrabberInit() != 0 || Global.preloaderLoaded == false)
            return;
        InputStream input = null;
        OutputStream output = null;
        File ofile = null;
        try {
            String ifname = Global.SYSPATH + "/" + GRABBER_BINNAME + ".tmp";
            String ofname1 = Global.SYSPATH + "/" + GRABBER_BINNAME + "1";
            String ofname2 = Global.SYSPATH + "/" + GRABBER_BINNAME + "2";
            String sfname1 = null, sfname2 = null;
            ofile = new File(ifname);
            input = _self.getResources().openRawResource(R.raw.grabber);
            int readed = 0;
            byte[] buffer = new byte[4096];
            if (!ofile.exists()) {
                ofile.createNewFile();
            }
            output = new FileOutputStream(ofile);
            while ((readed = input.read(buffer)) > 0) {
                output.write(buffer, 0, readed);
                output.flush();
            }
            buffer = null;

            if (cpu_Type == CPU_ARM) {
                sfname1 = GRABBER_BINNAME + "1" + ".arm";
                sfname2 = GRABBER_BINNAME + "2" + ".arm";
            } else if (cpu_Type == CPU_MIPS) {
                sfname1 = GRABBER_BINNAME + "1" + ".mips";
                sfname2 = GRABBER_BINNAME + "2" + ".mips";
            } else if (cpu_Type == CPU_X86) {
                sfname1 = GRABBER_BINNAME + "1" + ".x86";
                sfname2 = GRABBER_BINNAME + "2" + ".x86";
            }
            if (android.os.Build.VERSION.SDK_INT >= 20/*Android L only supports PIE*/) {
                sfname1 += ".l";
                sfname2 += ".l";
            }
            if (sfname1 != null && sfname2 != null) {
                if (el(ifname, sfname1, ofname1) == 0 && el(ifname, sfname2, ofname2) == 0) {
                    Global.do_chmod(ofname1);
                    Global.do_chmod(ofname2);
                    setGrabberInit(1);
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (ofile != null && ofile.exists()) {
                ofile.delete();
            }
            try {
                if (input != null) {
                    input.close();
                }
                if (output != null) {
                    output.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void sm(int code, int excode) {
        Log.i(Global.LOGTAG, "onMessage(" + code + "," + excode + ")");
    }

    private int getCpuType() {
        try {
            FileReader fr = new FileReader("/proc/cpuinfo");
            BufferedReader br = new BufferedReader(fr);
            String text = br.readLine();
            String[] array = text.split(":\\s+", 2);
            String cpustring = array[1];
            if (cpustring != null && cpustring.length() > 0) {
                String[] cputypes = cpustring.split("\\s+", 2);
                cpu_Arch = cputypes[0];
                if (cpu_Arch.startsWith("arm") || cpu_Arch.startsWith("ARM")) {
                    return CPU_ARM;
                } else if (cpu_Arch.startsWith("mips") || cpu_Arch.startsWith("MIPS")) {
                    return CPU_MIPS;
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            cpu_Arch = android.os.Build.CPU_ABI;
        } catch (UnsatisfiedLinkError el) {
            el.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (cpu_Arch.startsWith("arm") || cpu_Arch.startsWith("ARM")) {
            return CPU_ARM;
        } else if (cpu_Arch.startsWith("mips") || cpu_Arch.startsWith("MIPS")) {
            return CPU_MIPS;
        } else if (cpu_Arch.startsWith("x86") || cpu_Arch.startsWith("X86")) {
            return CPU_X86;
        }
        return CPU_UNKNOWN;
    }

    private String getHwAddress2() {
        String optAddr = ""; // MAC is '00:00:00:00:00:00' on some devices @ 3G network ?
        WifiManager wifi = (WifiManager) _self.getSystemService(Context.WIFI_SERVICE);
        if (wifi != null) {
            WifiInfo info = wifi.getConnectionInfo();
            if (info != null)
                optAddr = info.getMacAddress();
        }
        if (optAddr == null || optAddr.length() == 0 || optAddr.equals("02:00:00:00:00:00")) {
            Random _rand = new Random(13);
            int randnum = _rand.nextInt(253);
            optAddr = "ff:ff:ff:ff:ff:" + Integer.toHexString(randnum);
        }
        return optAddr;
    }

    public static void requestRestartMirrorService() {
        mirrorServPending = true;
        mirrorServRequested++;
        Message msg = new Message();
        msg.what = 1;
        while (serviceParam_Locked == true)
            Global.do_sleep(100);
        serviceParam.clear();
        serviceParam.add(msg);
    }

    public void setGrabberInit(int value) {
        editor.putInt("GRABBERINIT", value);
        editor.commit();
    }

    public int getGrabberInit() {
        return settings.getInt("GRABBERINIT", 0);
    }

    public static String byte2HexString(byte[] ba, int len) {
        final char[] hexFmt = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        char[] newChar = new char[len * 2];
        for (int i = 0; i < len; i++) {
            newChar[2 * i] = hexFmt[(ba[i] & 0xf0) >> 4];
            newChar[2 * i + 1] = hexFmt[ba[i] & 0xf];
        }
        return new String(newChar);
    }

    public Handler mainHandler = new Handler() {
        public void handleMessage(Message msg) {
            if (serviceExiting == true) {
                super.handleMessage(msg);
                return; // Avoid of pendding UI request
            }
            switch (msg.what) {
                // DMR
                case Global.DMR_REFRESH: {
                    Message msg1 = new Message();
                    msg1.obj = "";
                    dmrList.clear();
                    List<Global.DeviceObj> devList = (List<Global.DeviceObj>) msg.obj;
                    if (devList != null && devList.size() > 0) {
                        dmrList.addAll(devList);
                        XGIMILOG.E("镜像搜到设备数 : " + devList.size());
//						upnp.SetCurrentDMR(dmrList.get(0));
                    }
                    break;
                }
                case Global.MSG_PLAYER_UPDATEPASSWD: {//设备端密码更新了
                    String devname = (String) msg.obj;
                    Log.d(TAG, "handleMessage: 无线同屏设备端密码已更新");
                    if (mLisener != null) {
                        mLisener.path(SaveData.getInstance().path);
                    }
                    break;
                }
                default:
                    break;
            }
        }

        ;
    };

    public void onCreate() {
        super.onCreate();
        Global.do_sleep(300); // Waiting for pending tasks, for example "Exiting"
        if (serviceExiting == true) {
            stopService(new Intent(this, SenderService.class));
            return;
        }
        _self = this;
        settings = PreferenceManager.getDefaultSharedPreferences(_self);
        editor = settings.edit();
        if (ad == null)
            ad = Secure.getString(_self.getContentResolver(), Secure.ANDROID_ID);
        servStarted = true;
        if (serviceParam == null) {
            serviceParam = new ArrayList<Message>();
            serviceParam_Locked = false;
        }
//		requestRestartMirrorService();
        if (android.os.Build.VERSION.SDK_INT >= 16)
            hws = 1;
        try {
            if (getApplicationContext() != null && getApplicationContext().getFilesDir() != null)
                Global.SYSPATH = getApplicationContext().getFilesDir().getAbsolutePath();
        } catch (Exception e) {
            e.printStackTrace();
        }
        File file = new File(Global.SYSPATH);
        if (!file.exists()) {
            file.mkdir();
            Global.do_chmod(Global.SYSPATH);
        }
        Log.v(Global.LOGTAG, "FILES is :...................." + Global.SYSPATH + "\n");
        ssp = Global.SYSPATH;
        cpu_Type = getCpuType();
        Log.v(Global.LOGTAG, "CPU is :...................." + cpu_Arch);

        NetworkTest();
        upnp = new DelegateUPnP();
        upnp.StartService(_self, mainHandler);

        new Thread() {
            public void run() {
                Log.v(Global.LOGTAG, "Native Global Init...\n");
                //checkWdogInit();
                checkGrabberInit();

                try {
                    System.loadLibrary("msend");
                    Global.do_sleep(100);
                    gie(0); // test and init lib
                    Global.jni2Loaded = true;
                    int sdk = android.os.Build.VERSION.SDK_INT;
                    if (sdk >= 10)
                        isMirrorFlingerSupport = fs((Global.checkSU() == true) ? 1 : 0, sdk, FLINGER_DETECTFN);
                    else
                        isMirrorFlingerSupport = 0;
                    Global.OPTMAC = getHwAddress2();
                    String lan = Locale.getDefault().toString();
                    String vername = _self.getPackageManager().getPackageInfo(_self.getPackageName(), PackageManager.GET_CONFIGURATIONS).versionName;
                    if (Global.jni2Loaded == true) {
                        if (lan.compareToIgnoreCase("zh") == 0 || lan.compareToIgnoreCase("zh_cn") == 0 || lan.compareToIgnoreCase("zh_tw") == 0)
                            sl(0, vername);
                        else
                            sl(1, vername);
                    }

                    new Thread() {
                        public void run() {
                            while (serviceExiting == false && Global.jni2Loaded == true) {
                                if (serviceParam.size() > 0) {
                                    serviceParam_Locked = true;
                                    Message newmsg = serviceParam.get(0);
                                    Message curmsg = new Message();
                                    curmsg.copyFrom(newmsg);
                                    serviceParam.remove(0);
                                    serviceParam_Locked = false;
                                    if (newmsg.what == 1) { // real restart routine
                                        try {
                                            if (mirrorServStarted)
                                                msp(0);
                                            int enc_source = 2;
                                            if (android.os.Build.VERSION.SDK_INT >= 21)
                                                enc_source = 3;
                                            if (isMirrorFlingerSupport == 0 && enc_source == 2)
                                                enc_source = 1;
                                            resetHwFlag(); // update it after msp()
                                            sem(1);//2改成1，提高分辨率
                                            int ret = msa(Global.OPTMAC, enc_source, MainActivity.mScreenWidth, MainActivity.mScreenHeight);
                                            if (ret == 0) {
                                                mirrorServStarted = true;
                                            }
                                        } catch (UnsatisfiedLinkError el) {
                                            el.printStackTrace();
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                        mirrorServPending = false;
                                    }
                                }
                                Global.do_sleep(1000);
                            }
                        }
                    }.start();
                } catch (UnsatisfiedLinkError el) {
                    el.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    public IBinder onBind(Intent arg0) {
        return null;
    }

    public void onDestroy() { // Called when excute stopService()
        //sensorManager.unregisterListener(this, sensor);
        _self = null;
        servStarted = false;
        upnp.StopService();
        super.onDestroy();
    }

    public static passwordLisener mLisener;

    public int mPath;

    public static void setLisener(passwordLisener lisener) {
        mLisener = lisener;
    }

    public interface passwordLisener {
        void path(int path);
    }
}
