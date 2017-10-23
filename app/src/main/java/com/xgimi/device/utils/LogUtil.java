package com.xgimi.device.utils;

import android.util.Log;

public class LogUtil {
	
	public static final String LOG_TAG = "---Debug---";
	
	private static boolean isDebug = true;
	
	public static void setDebug(boolean b){
		isDebug = b;
	}
	
	public static int i(String message){
		if(isDebug){
			return Log.i(LOG_TAG, message);
		}
		return -1;
	}
	public static int i(String tag,String message){
		if(isDebug){
			return Log.i(tag, message);
		}
		return -1;
	}
	
	public static int w(String message){
		if(isDebug){
			return Log.w(LOG_TAG, message);
		}
		return -1;
	}
	public static int w(String tag,String message){
		if(isDebug){
			return Log.w(tag, message);
		}
		return -1;
	}
	
	public static int w(Throwable tr){
		if(isDebug){
			return Log.w(LOG_TAG, tr);
		}
		return -1;
	}
	public static int w(String tag,Throwable tr){
		if(isDebug){
			return Log.w(tag, tr);
		}
		return -1;
	}
	
	public static int W(String message,Throwable tr){
		if(isDebug){
			return Log.w(LOG_TAG, message, tr);
		}
		return -1;
	}
	public static int W(String tag,String message,Throwable tr){
		if(isDebug){
			return Log.w(tag, message, tr);
		}
		return -1;
	}
	
	public static int d(String message){
		if(isDebug){
			return Log.d(LOG_TAG, message);
		}
		return -1;
	}
	public static int d(String tag,String message){
		if(isDebug){
			return Log.d(tag, message);
		}
		return -1;
	}
	
	public static int d(String message,Throwable tr){
		if(isDebug){
			return Log.d(LOG_TAG, message,tr);
		}
		return -1;
	}
	public static int d(String tag,String message,Throwable tr){
		if(isDebug){
			return Log.d(tag, message,tr);
		}
		return -1;
	}
	
	public static int e(String message){
		if(isDebug){
			return Log.e(LOG_TAG, message);
		}
		return -1;
	}
	public static int e(String tag,String message){
		if(isDebug){
			return Log.e(tag, message);
		}
		return -1;
	}
	
	public static int e(String message,Throwable tr){
		if(isDebug){
			return Log.e(LOG_TAG, message, tr);
		}
		return -1;
	}
	public static int e(String tag,String message,Throwable tr){
		if(isDebug){
			return Log.e(tag, message, tr);
		}
		return -1;
	}
	
	public static int v(String message){
		if(isDebug){
			return Log.v(LOG_TAG, message);
		}
		return -1;
	}
	public static int v(String tag,String message){
		if(isDebug){
			return Log.v(tag, message);
		}
		return -1;
	}
	
	public static int v(String message,Throwable tr){
		if(isDebug){
			return Log.v(LOG_TAG, message, tr);
		}
		return -1;
	}
	public static int v(String tag,String message,Throwable tr){
		if(isDebug){
			return Log.v(tag, message, tr);
		}
		return -1;
	}

}
