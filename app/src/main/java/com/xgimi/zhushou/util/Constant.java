package com.xgimi.zhushou.util;

public class Constant {

	public static float rawy=0.0f;
	public static boolean netStatus = false; // 设备的连接状态
	private static final String KEYPRESSES_HEAD = "KEYPRESSES:";
	public static final String COM_PowerOff = KEYPRESSES_HEAD + "30";// 关机键000000
	public static final String COM_SetOk = KEYPRESSES_HEAD + "49";
	public static final String COM_Up = KEYPRESSES_HEAD + "36";// 导航上下左右
	public static final String COM_Down = KEYPRESSES_HEAD + "38";
	public static final String COM_Left = KEYPRESSES_HEAD + "50";
	public static final String COM_Right = KEYPRESSES_HEAD + "37";
	public static final String COM_IncreaseVolume = KEYPRESSES_HEAD + "115";
	public static final String COM_DecreaseVolume = KEYPRESSES_HEAD + "114";// 加减音量
	public static final String COM_Mute = KEYPRESSES_HEAD + "113";// 静音
	public static final String COM_Back = KEYPRESSES_HEAD + "48";
	public static final String COM_Menu = KEYPRESSES_HEAD + "139";
	public static final String COM_Misckey = KEYPRESSES_HEAD + "251"; // 功能键，小健盘
	public static final String COM_3D = KEYPRESSES_HEAD + "252"; // 一键3D
	public static final String COM_Home = KEYPRESSES_HEAD + "35";

	public static final String SCREENSHOT_COM = "SCREENSHOT"; // 截屏

	public static final String OPENAPP_HEAD = "OPENAPP:";
	public static final String UNINSTALLAPP_HEAD = "UNINSTALLAPP:";
	public static final String CLEARMEMORY_COM = "CLEARMEMORY";

	public static final String COM_MouseRight = "MOUSERIGHT:";// 鼠标右键
	public static final String COM_MouseLeft = "MOUSELEFTS:";// 鼠标左键
	public static final String COM_GESTURE = "MOUSEWHEEL:";// 滚轮向前

	public static final String COM_TOUCHEVENT = "TOUCHEVENT:";
	public static final String COM_MouseMove_argx = "X";
	public static final String COM_MouseMove_argy = "Y";// 鼠标控制x、y的坐标


	public static boolean isZ3S = false;
	public static boolean isZ3MP = false;
	
}
