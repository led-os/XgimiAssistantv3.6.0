package com.xgimi.device.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * 关于线程的一些操作
 * 
 * @author liuyang
 *
 */
public class ThreadPid {

	/**
	 * 根据路径启动一个线程
	 * 
	 * @param processName
	 *            路径名
	 * @return
	 */
	public static boolean startBin(String processName) {

		if (isProcessRun(processName)) {
			return true;
		}

		try {

			Runtime runtime = Runtime.getRuntime();

			runtime.exec(processName);

			return true;

		} catch (IOException e) {

			return false;
		}
	}

	/**
	 * 根据线程名查询该线程是否开启
	 * 
	 * @param processName
	 * @return
	 */
	public static Boolean isProcessRun(String filename) {
		try {
			String[] cmd = { "ps" };
			Process proc = Runtime.getRuntime().exec(cmd);
			BufferedReader in = new BufferedReader(new InputStreamReader(
					proc.getInputStream()));
			String string_Temp = in.readLine();
			while (string_Temp != null) {

				if (string_Temp.equals(filename)) {

					return true;
				}

				string_Temp = in.readLine();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return false;
	}
}
