package com.xgimi.zhushou.util;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff.Mode;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.xgimi.device.device.GMDeviceController;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/*import com.xgimi.yiping.MultitouchListener.Point;
import com.xgimi.yiping.MultitouchListener.TYPE;*/

public class MultitouchView extends SurfaceView implements
		SurfaceHolder.Callback {
	public static int MAX_TOUCHPOINTS = 10;
	private List<MultitouchListener.Point> point;
	private MultitouchListener listener;

	private int video_wigth;
	private int real_hight;

	private void executeInConstructor() {
		setZOrderOnTop(true);
		getHolder().setFormat(PixelFormat.TRANSPARENT);
		setBackgroundColor(Color.TRANSPARENT);
		setFocusable(true); // make sure we get key events
		setFocusableInTouchMode(true); // make sure we get touch events
		point = new ArrayList<MultitouchListener.Point>();
	}

	public MultitouchView(Context context, int videoWigth, int realHight) {
		super(context);
		this.video_wigth = videoWigth;
		this.real_hight = realHight;
		executeInConstructor();

	}

	public MultitouchView(Context context, AttributeSet attrs) {
		super(context, attrs);
		executeInConstructor();
	}

	public void setOnMultitouchListener(MultitouchListener listener) {
		this.listener = listener;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {

		final int pointerCount = event.getPointerCount();
		MultitouchListener.TYPE type = MultitouchListener.TYPE.NULL;
		point.clear();

		switch (event.getAction() & MotionEvent.ACTION_MASK) {
		case MotionEvent.ACTION_DOWN: {
			type = MultitouchListener.TYPE.SINGLE_PRESS;
			GMDeviceController.getInstance().SendJC(sendJson());
			break;
		}
		case MotionEvent.ACTION_UP: {

			type = MultitouchListener.TYPE.SINGLE_RELEASE;// type = TYPE.SINGLE_RELEASE
			break;
		}
		case MotionEvent.ACTION_POINTER_DOWN: {
			break;
		}
		case MotionEvent.ACTION_POINTER_UP: {
			break;
		}
		case MotionEvent.ACTION_MOVE: {
			if (pointerCount == 1) {
				type = MultitouchListener.TYPE.SINGLE_MOTION;
			} else {
				type = MultitouchListener.TYPE.MULTITOUCH;
			}
		}
		default:
			break;
		}

		if (type == MultitouchListener.TYPE.SINGLE_RELEASE) {
			if (this.listener != null) {
				listener.touchEvent(point, type);
			}
		}

		if (type != MultitouchListener.TYPE.NULL) {
			for (int index = 0; index < pointerCount; index++) {

				int dec = (getWidth() - video_wigth) / 2;
				float px = 0.0f;
				float py = 0.0f;
				if (event.getX(index) < dec) {
					px = 0.0f;

				} else {
					px = (event.getX(index) - dec) / video_wigth;
				}
				if (px > 1.0f) {
					px = 1.0f;
				}
				px += 0.005f;// pach

				py = (event.getY(index)) / real_hight;

				py += 0.011f;// pach adjust

				if (py > 1.0f) {
					py = 1.0f;
				}
				point.add(new MultitouchListener.Point(px,py));
			}
			if (this.listener != null) {
				listener.touchEvent(point, type);
			}

		}

		return true;
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		Canvas cvs = getHolder().lockCanvas();
		try {
			cvs.drawColor(Color.TRANSPARENT, Mode.CLEAR);
		} catch (NullPointerException e) {
			e.printStackTrace();
		} finally {
			getHolder().unlockCanvasAndPost(cvs);
		}
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		point.clear();
	}
	
	 //发送json数据给投影一
		public String sendJson(){
				JSONObject jsonObject=new JSONObject();
				JSONObject js2=new JSONObject();
						try {
					js2.put("data", jsonObject);
					js2.put("action", 8);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				System.out.println(js2.toString());
				return js2.toString();
		}	
	
	
}
