package com.xgimi.zhushou.util;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import java.util.List;


public class AirMouse implements SensorEventListener {

    private final double DETAL = 0.2;

    private final int base12 = 12;

    private final int base15 = 15;

    private float lastX = 90;

    private float lastY = 0;

    private Context mContext;

    private SensorManager sensorManager;

    private Sensor sensor;

    private OnSensorListener onSensorListener;

    public AirMouse(Context c) {
        mContext = c;
    }

    /**
     * 开启方向传感器监听
     *
     * @return fasle 没有改传感器，禁用功能    true 正常进行
     */
    private List<Sensor> allSensors;
    @SuppressWarnings("deprecation")
	public boolean start() {
        boolean noSensor = false;
        sensorManager = (SensorManager) mContext.getSystemService(Context.SENSOR_SERVICE);
        if (sensorManager != null) {
        	allSensors=sensorManager.getSensorList(Sensor.TYPE_ALL);
        	for (int i = 0; i < allSensors.size(); i++) {
        		
        		if(Sensor.TYPE_GYROSCOPE==allSensors.get(i).getType()){
        			sensor=sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
        			noSensor=true;
        		}
			}
        } 
        if (sensor != null) {
            sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_GAME);
        }
        return noSensor;
    }

    //关闭传感器监听
    public void stop() {
    	if(sensorManager!=null)
        sensorManager.unregisterListener(this);
    	sensorManager=null;
    }

    public void setOnSensorListener(OnSensorListener l) {
        onSensorListener = l;
    }

    public void onSensorChanged(SensorEvent event) {
        float x = event.values[0];
        float y = event.values[1];
        boolean bContinue = (Math.abs(lastX - x) > DETAL || Math.abs(lastY - y) > DETAL);

        if (bContinue && onSensorListener != null) {
            float tempX = (lastX - x);
            float tempY = (lastY - y);
            if (Math.abs(tempX / tempY) > 2.0) {
                tempY = 0;
            } else if (Math.abs(tempY / tempX) > 2.0) {
                tempX = 0;
            }
            tempX *= base15;
            tempY *= (tempY > 0 ? base12 : base15);
            onSensorListener.onSensorChange(tempX, tempY);
        }
        lastX = x;
        lastY = y;
    }

    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public interface OnSensorListener {
        void onSensorChange(float x, float y);
    }

}