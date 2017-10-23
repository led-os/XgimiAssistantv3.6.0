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
public class DirectionView extends ImageButton {

	private OnDirectionListening onDirectionListening;

	private int mIsUp = 1;
	private int mIsDown = 2;
	private int mIsLeft = 3;
	private int mIsRight = 4;
	private int mIsCenter = 5;
	int status = 0;
	public DirectionView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public interface OnDirectionListening {
		void onDirectionListening(int direction);
		void onDirectionCancel(int direction);
	}

	/**
	 * 方向键盘的监听 1 上 2 下 3左 4右 5ok
	 * 
	 * @param listening
	 */
	public void setOnDirectionListening(OnDirectionListening listening) {
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
			
			if ((f1 > f3 / 3.0F) && (f1 < 2.0F * f3 / 3.0F) && (f2 > f4 / 3.0F) && (f2 < f4 * 2.0F / 3.0F)) {
				status = mIsCenter;
				setImageResource(R.drawable.four_dimen_navigation_press_center);

			} else {
				if (f2 < (f4 / 2)) {

					if (f1 < f3 / 3.0F) {
						status = mIsLeft;
						setImageResource(R.drawable.four_dimen_navigation_press_left);
					} else if (f1 > (f3 / 3.0F) * 2.0F) {
						status = mIsRight;
						setImageResource(R.drawable.four_dimen_navigation_press_right);
					} else {
						status = mIsUp;
						setImageResource(R.drawable.four_dimen_navigation_press_up);
					}

				} else {
					if (f1 < f3 / 3.0F) {
						status = mIsLeft;
						setImageResource(R.drawable.four_dimen_navigation_press_left);
					} else if (f1 > (f3 / 3.0F) * 2.0F) {
						status = mIsRight;
						setImageResource(R.drawable.four_dimen_navigation_press_right);
					} else {
						status = mIsDown;
						setImageResource(R.drawable.four_dimen_navigation_press_down);
					}
				}

			}

			if (onDirectionListening != null) {
				onDirectionListening.onDirectionListening(status);
			}
			break;
		case MotionEvent.ACTION_UP:
			if (onDirectionListening != null) {
				onDirectionListening.onDirectionCancel(status);
			}
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
			setImageResource(R.drawable.four_dimen_navigation_normal);
		}
	};

}
