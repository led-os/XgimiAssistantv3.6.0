package com.xgimi.gmsdk.bean.send;

public class Consants {

    public static String defaultName = "极米无屏电视";
    private static final String KEYPRESSES_HEAD = "KEYPRESSES:";
    public static boolean constatus = false;
    public static final int CONNECTSERVICE = 0x1;
    public static final int CONNECTSUCESS = 1;
    public static final int CONNECTFAILURE = 101;
    public static final int CONNECIPFAILUE = 102;
    public static final int RECEIVEDEVICETYPE = 0x1008;
    public static final int GM_ERROR_CODE_NOT_CONNECTED = 103;
    public static final int GM_ERROR_CODE_CMD_NOTFOUND = 104;
    public static final String GMKeyEventLeft = KEYPRESSES_HEAD + "50";
    public static final String GMKeyEventRight = KEYPRESSES_HEAD + "37";
    public static final String GMKeyEventUp = KEYPRESSES_HEAD + "36";
    public static final String GMKeyEventDown = KEYPRESSES_HEAD + "38";
    public static final String GMKeyEventOk = KEYPRESSES_HEAD + "49";
    public static final String GMKeyEventHome = KEYPRESSES_HEAD + "35";

    public static final String GMKeyEventHomeUP = "KEYSSTATUS:35+0";
    public static final String GMKeyEventHomeDOWN = "KEYSSTATUS:35+1";

    public static final String GMKeyEventBack = KEYPRESSES_HEAD + "48";
    public static final String GMKeyEventVolumeUp = KEYPRESSES_HEAD + "115";
    public static final String GMKeyEventVolumeDown = KEYPRESSES_HEAD + "114";
    public static final String GMKeyEventMenu = KEYPRESSES_HEAD + "139";

    //youxianjian
    public static final String GMkeyEVentLeft = KEYPRESSES_HEAD + "169";
    public static final String GMkeyEVentRight = KEYPRESSES_HEAD + "170";
    public static final String GMkeyEVentUp = KEYPRESSES_HEAD + "167";
    public static final String GMkeyEVentDown = KEYPRESSES_HEAD + "168";
    public static final String GMkeyEVentY = KEYPRESSES_HEAD + "158";
    public static final String GMkeyEVentx = KEYPRESSES_HEAD + "157";
    public static final String GMkeyEVentb = KEYPRESSES_HEAD + "155";
    public static final String GMkeyEVenta = KEYPRESSES_HEAD + "154";
    public static final String GMkeyEVentl = KEYPRESSES_HEAD + "160";
    public static final String GMkeyEVentr = KEYPRESSES_HEAD + "161";
    public static final String GMkeyEVentselector = KEYPRESSES_HEAD + "244";


//	public static final String GMKeyEventFocusLeft=KEYPRESSES_HEAD + "49";
//	public static final String GMKeyEventFocusRight=KEYPRESSES_HEAD + "49";


    public static final String COM_FOCUS_ADD_DOWN = "KEYSSTATUS:253+1";//调焦
    public static final String COM_FOCUS_ADD_UP = "KEYSSTATUS:253+0";//调焦抬起

    public static final String COM_FOCUS_REDUCE_DOWN = "KEYSSTATUS:254+1";
    public static final String COM_FOCUS_REDUCE_UP = "KEYSSTATUS:254+0";


    public static final String GMKeyEventTurnOff = KEYPRESSES_HEAD + "30";
    public static final String GMKeyEventThreeD = KEYPRESSES_HEAD + "252";
    public static final String GMKeyEventFunction = KEYPRESSES_HEAD + "251";

    public static final String DEVICE_TYPE_NEW_Z3M_ADD = "mango";
    public static final String DEVICE_TYPE_NEW_Z4X = "z4x";
    public static final String DEVICE_TYPE_NEW_A4AIR = "z4air";
    public static final String DEVICE_TYPE_NEW_Z3S = "mstarnapoli";

    //向左长按
    public static final String GMKeyStatusLeft = "KEYSSTATUS:50+1";//按下
    public static final String GMKeyPressLeft = "KEYSSTATUS:50+0";//抬起


    //向友长按
    public static final String GMKeyStatusRight = "KEYSSTATUS:37+1";//按下
    public static final String GMKeyPressRight = "KEYSSTATUS:37+0";//抬起

    //向上长按
    public static final String GMKeyStatusup = "KEYSSTATUS:36+1";//按下
    public static final String GMKeyPressup = "KEYSSTATUS:36+0";//抬起

    //向下长按

    public static final String GMKeyStatusdown = "KEYSSTATUS:38+1";//按下
    public static final String GMKeyPressdown = "KEYSSTATUS:38+0";//抬起

    //ok键长按

    public static final String GMKeyOkDown = "KEYSSTATUS:49+1";
    public static final String GMKeyOkUp = "KEYSSTATUS:49+0";

    //打开app命令
    public static final String GMKeyOpen = "OPENAPP:";
    public static final String GMKeyXieZai = "UNINSTALLAPP:";

    //清除内存
    public static final String CLEARMEMORY_COM = "CLEARMEMORY";

    //心跳的状态

    public static boolean HeatBeatStatus = true;

    //获取连接ip
    public static String ip;
}
