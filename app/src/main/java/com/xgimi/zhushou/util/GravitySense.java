package com.xgimi.zhushou.util;


import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

import com.xgimi.zhushou.App;

public class GravitySense implements SensorEventListener {

	public static boolean gs_revert = true; // 重力感应横屏操作 设置为true默认横屏操作
	private SensorManager sm = null;
	private App myApp;

	private GravitySenseListener listener = null;
	private GravitySenseListener.Point point = new GravitySenseListener.Point(0, 0, 0);

	public GravitySense(Context context) {
		myApp = (App) context.getApplicationContext();

		// 获取传感器服务
		sm = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);

		registerListener();

	}

	private void registerListener() {

		// 注册传感器中 重力加速 的回调
		sm.registerListener(this,
				sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
				SensorManager.SENSOR_DELAY_GAME);

	}

	public void unRegisterSensorListener() {
		sm.unregisterListener(this);
	}

	public void setGravitySensorListener(GravitySenseListener listener) {
		this.listener = listener;
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {

	}

	@Override
	public void onSensorChanged(SensorEvent event) {

		GravitySenseListener.TYPE type = GravitySenseListener.TYPE.NULL;

		switch (event.sensor.getType()) {

		case Sensor.TYPE_ACCELEROMETER: {

			type = GravitySenseListener.TYPE.ACCELEROMETER;
			int divisor = 1;

			switch (myApp.getGravitySenseStatus()) {
			case 1:
				divisor = 4; // low
				break;
			case 2:
				divisor = 2; // mid
				break;
			case 3:
				divisor = 1; // high
				break;
			}

			// 手机横屏幕， x,y 交换
			if (gs_revert) {
				point.set(-(event.values[1] / divisor),
						-(event.values[0] / divisor),
						-(event.values[2] / divisor));
			} else {
				point.set(event.values[0] / divisor, event.values[1] / divisor,
						event.values[2] / divisor);
			}

			listener.gravitySenseListener(point, type);
			Log.e("---", "xxxx");
			break;
		}
		}
	}
}
