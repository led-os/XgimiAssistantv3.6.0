package com.xgimi.device.device;

public class GMKeyCommand extends GMCommand{
	/**
	 * key:按键
	 */
	public int key;
	public int state;
	/**
	 * GMKeyEventLeft:发送向左的命令
	 */
	public static int GMKeyEventLeft=001;
	/**
	 * GMKeyEventRight:发送向右的命令
	 */
	public static int GMKeyEventRight=002;
	/**
	 * GMKeyEventUp:发送向上的命令
	 */
	public static int GMKeyEventUp=003;
	/**
	 * GMKeyEventUp:发送向下的命令
	 */
	public static int GMKeyEventDown=004;
	/**
	 * GMKeyEventOk:发送确认键的命令
	 */
	public static int GMKeyEventOk=005;
	/**
	 * GMKeyEventHome:发送Home键的命令
	 */
	public static int GMKeyEventHome=006;
	/**
	 * GMKeyEventBack:发送返回键的命令
	 */
	public static int GMKeyEventBack=007;
	/**
	 * GMKeyEventVolumeUp:发送音量加的命令
	 */
	public static int GMKeyEventVolumeUp=010;
	/**
	 * GMKeyEventVolumeDown:发送音量减的命令
	 */
	public static int GMKeyEventVolumeDown=011;
	/**
	 * GMKeyEventMenu:发送菜单的命令
	 */
	public static int GMKeyEventMenu=012;
	/**
	 * GMKeyEventFocusLeft:发送调焦减少的命令
	 */
	public static int GMKeyEventFocusLeft_down=013;
	/**
	 * GMKeyEventFocusRight:发送调焦增加的命令
	 */
	public static int GMKeyEventFocusLeft_up=014;
	
	
	public static int GMKeyEventFocusRight_down=021;
	/**
	 * GMKeyEventFocusRight:发送调焦增加的命令
	 */
	public static int GMKeyEventFocusRight_up=022;
	
	/**
	 * GMKeyEventFocusTurnOFF:发送关机的命令
	 */
	public static int GMKeyEventFocusTurnOFF=015;
	public static int GMKeyEventFocusThreeD=016;
	public static int GMKeyEventFunction=017;
	
	
	
	//发送长按的命令
	public static int GMKeyLeftDown=200;
	public static int GMKeyLeftup=201;
	
	public static int GMKeyrightup=202;
	public static int GMKeyrightdonw=203;
	
	public static int GMKeyupUp=204;
	public static int GMKeyupdonw=205;
	
	public static int GMKeyDownup=206;
	public static int GMKeyDowndonw=207;
	
	public static int GMKeyOkUp=208;
	public static int GMKeyOkDown=209;

	public static int GMKeyHomeUp=210;
	public static int GMKeyHomeDown=211;
	
	
	
	
	//发送打开app的命令
	public static int GMkeyOpen=108;
	public static int GMXieZai=109;
	public static int Voice=23;
	public static int GMKeyEventNeiCun=25;
	
	
	public static int GMKeyGameY=26;
	public static int GMKeyGamex=27;
	public static int GMKeyGameb=28;
	public static int GMKeyGamea=29;
	public static int GMKeyGamel=30;
	public static int GMKeyGamer=31;
	public static int GMKeyGameSelecter=32;
	public static int GMKeyGameleft=33;
	public static int GMKeyGameright=34;
	public static int GMKeyGameup=35;
	public static int GMKeyGamedown=36;
	public GMKeyCommand(int key, int state) {
		this.key = key;
		this.state = state;
		
	}
	public GMKeyCommand() {
	}
	public GMKeyCommand(int key) {
		this.key = key;
	}
	public int getKey() {
		return key;
	}
	public void setKey(int key) {
		this.key = key;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
}
