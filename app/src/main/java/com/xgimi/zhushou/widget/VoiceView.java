package com.xgimi.zhushou.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageButton;

import com.xgimi.zhushou.R;

/**
 * 遥控器的方向控制，通过简单的背景更换实现
 * 
 * @author liuyang
 * 
 */
public class VoiceView extends ImageButton {

	private OnVoiceListening onDirectionListening;

	private int mIsLeft = 3;
	private int mIsRight = 4;

	public VoiceView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public interface OnVoiceListening {
		void onDirectionListening(int direction);
	}

	/**
	 * 方向键盘的监听 1 上 2 下 3左 4右 5ok
	 * 
	 * @param listening
	 */
	public void setOnDirectionListening(OnVoiceListening listening) {
		onDirectionListening = listening;
	}

	@Override
	public boolean onTouchEvent(MotionEvent paramMotionEvent) {
		float f1 = paramMotionEvent.getX();
		float f2 = paramMotionEvent.getY();
		float f3 = getWidth();
		float f4 = getHeight();

		switch (paramMotionEvent.getAction()) {
		case MotionEvent.ACTION_DOWN:

			int status = 0;
			if (f1 > f3 / 2.0f) {
				setImageResource(R.drawable.yinliangadd);
				status=1;
			} else {
				setImageResource(R.drawable.yinliangreduce);
				status=2;
			}
			if (onDirectionListening != null) {
				onDirectionListening.onDirectionListening(status);
			}

			break;
		case MotionEvent.ACTION_UP:
			removeCallbacks(runnable);
			post(runnable);
			break;
		case MotionEvent.ACTION_CANCEL:
			removeCallbacks(runnable);
			post(runnable);
			break;
		}

		return super.onTouchEvent(paramMotionEvent);
	}

	private Runnable runnable = new Runnable() {
		@Override
		public void run() {
			setImageResource(R.drawable.yinliang);
		}
	};

}
