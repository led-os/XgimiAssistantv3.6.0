package com.xgimi.zhushou.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.AbsoluteLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.xgimi.device.device.GMDeviceController;
import com.xgimi.device.device.GMKeyCommand;
import com.xgimi.zhushou.R;

import java.util.Timer;
import java.util.TimerTask;

public class GameActivity extends Activity implements OnTouchListener,
		OnGestureListener ,OnClickListener{

	private ImageView direction;
	GestureDetector mGestureDetector = new GestureDetector(this);
	private AbsoluteLayout abs;
	private int heght;
	private int with;
	private GMKeyCommand gmKeyCommand;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game);
		initView();
	}

	private void initView() {
		direction = (ImageView) findViewById(R.id.direction);
		abs = (AbsoluteLayout) findViewById(R.id.abs);
		float x = direction.getX();
		float y = direction.getY();
		float x1 = abs.getX();
		float y1 = abs.getY();
		abs.setOnTouchListener(this);
		gmKeyCommand = new GMKeyCommand();
		ImageView fanhui= (ImageView) findViewById(R.id.fanhui);
		fanhui.setOnClickListener(this);
		ImageView game_y = (ImageView) findViewById(R.id.y);
		ImageView game_x = (ImageView) findViewById(R.id.x);
		ImageView game_a = (ImageView) findViewById(R.id.a);
		ImageView game_b = (ImageView) findViewById(R.id.b);
		ImageView game_l = (ImageView) findViewById(R.id.l);
		ImageView game_r = (ImageView) findViewById(R.id.r);
		ImageView game_ment = (ImageView) findViewById(R.id.menu);
		ImageView game_fanhui = (ImageView) findViewById(R.id.back);
		game_y.setOnClickListener(this);
		game_x.setOnClickListener(this);
		game_a.setOnClickListener(this);
		game_b.setOnClickListener(this);
		game_l.setOnClickListener(this);
		game_r.setOnClickListener(this);
		game_ment.setOnClickListener(this);
		game_fanhui.setOnClickListener(this);
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		
		if(event.getAction()==MotionEvent.ACTION_CANCEL||event.getAction()==MotionEvent.ACTION_UP)
		{
			direction.setX(zuobiaox - with / 2);
			direction.setY(zuobiaoy - heght / 2);
			stopTimer();
		}
		return mGestureDetector.onTouchEvent(event);
	}

	@Override
	public boolean onDown(MotionEvent e) {
		// TODO Auto-generated method stub
		statrTimer();
		return true;
	}


	@Override
	public void onShowPress(MotionEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		// TODO Auto-generated method stub
		Log.i("info", "onSingleTapUp");
		sb = null;
		
		stopTimer();
		return true;
	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		// TODO Auto-generated method stub
		float x1 = e2.getX();
		float y1 = e2.getY();
		Log.i("info", distanceX + "----" + distanceY);
		Log.i("info", "onDown");
		if (Math.sqrt((y1 - zuobiaoy) * (y1 - zuobiaoy) + (x1 - zuobiaox)
				* (x1 - zuobiaox)) < abs.getWidth() / 2 - direction.getWidth()
				/ 2) {
			direction.setX(x1 - direction.getWidth() / 2);
			direction.setY(y1 - direction.getHeight() / 2);
		} else {
			double c = Math.sqrt((y1 - zuobiaoy) * (y1 - zuobiaoy)
					+ (x1 - zuobiaox) * (x1 - zuobiaox));
			float d = (float) (zuobiaox + (x1 - zuobiaox)
					* ((abs.getWidth() / 2 - direction.getWidth() / 2) / c));
			float f = (float) (zuobiaoy + (y1 - zuobiaoy)
					* ((abs.getWidth() / 2 - direction.getWidth() / 2) / c));
			direction.setX(d - direction.getWidth() / 2);
			direction.setY(f - direction.getHeight() / 2);
		}

		float tany = e2.getY() - zuobiaoy;
		float tanx = e2.getX() - zuobiaox;
		double radios = Math.atan(tany / tanx);
		if (e2.getX() > zuobiaox) {
			if(-Math.PI/2<radios&&-Math.PI/4>radios)
				gmKeyCommand.key = GMKeyCommand.GMKeyGameleft;
			else if(-Math.PI/4<radios&&Math.PI/4>radios)
				gmKeyCommand.key = GMKeyCommand.GMKeyGameright;
			else{
				gmKeyCommand.key = GMKeyCommand.GMKeyGamedown;
			}
		} else if (e2.getX() < zuobiaox) {
			if ((Math.PI / 4 > radios && -Math.PI / 4 < radios)) {
				gmKeyCommand.key = GMKeyCommand.GMKeyGameleft;
			} else if ((-Math.PI / 4 > radios && -Math.PI / 2 < radios)) {
				gmKeyCommand.key = GMKeyCommand.GMKeyGamedown;
			} else if (Math.PI / 4 < radios && Math.PI / 2 > radios) {
				gmKeyCommand.key = GMKeyCommand.GMKeyGameup;
			}
		}
		// if(e2.getX()<zuobiaox&&Math.abs(e2.getY()-zuobiaoy)<20){
		// gmKeyCommand.key=GMKeyCommand.GMKeyEventLeft;
		// }else if(e2.getX()>zuobiaox&&Math.abs(e2.getY()-zuobiaoy)<20){
		// gmKeyCommand.key=GMKeyCommand.GMKeyEventRight;
		//
		// }else if(e2.getY()>zuobiaoy&&Math.abs(e2.getX()-zuobiaox)>20){
		// gmKeyCommand.key=GMKeyCommand.GMKeyEventUp;//发送向上的命令
		// }else if(e2.getY()<zuobiaoy&&Math.abs(e2.getX()-zuobiaox)>20){
		// gmKeyCommand.key=GMKeyCommand.GMKeyEventDown;
		// }
		
		if(e2.getAction()==MotionEvent.ACTION_UP||e1.getAction()==MotionEvent.ACTION_UP){
			stopTimer();
			

		}
		return true;
	}

	@Override
	public void onLongPress(MotionEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		// TODO Auto-generated method stub
		Log.i("info", "onFling");

		return false;
	}

	private int zuobiaox;
	private int zuobiaoy;
	private TextView tv;
	private StringBuffer sb;
	private Timer timer;
	private TimerTask task;

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		// TODO Auto-generated method stub
		super.onWindowFocusChanged(hasFocus);
		heght = direction.getHeight();
		with = direction.getWidth();
		zuobiaox = (abs.getRight() - abs.getLeft()) / 2;
		zuobiaoy = (abs.getBottom() - abs.getTop()) / 2;
		direction.setX(zuobiaox - with / 2);
		direction.setY(zuobiaoy - heght / 2);
	}
	
	
	public void statrTimer(){
		if(timer==null)
		timer = new Timer();
		if(task==null){
			task=new TimerTask() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
		        	GMDeviceController.getInstance().sendCommand(gmKeyCommand);

				}
			};
			
			if(timer!=null&&task!=null){
				timer.schedule(task, 0, 200);
			}
		}
	}
	
	public void stopTimer(){
		if(timer!=null){
			timer.cancel();
			timer=null;
		}
		if(task!=null){
			task.cancel();
			task=null;
		}
		Log.e("info", "停止了");
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.fanhui:
			finish();
			break;
		case R.id.l:
			gmKeyCommand.setKey(GMKeyCommand.GMKeyGamel);
			break;
		case R.id.r:
			gmKeyCommand.setKey(GMKeyCommand.GMKeyGamer);
			break;
		case R.id.menu:
			gmKeyCommand.setKey(GMKeyCommand.GMKeyGameSelecter);
			break;
		case R.id.back:
			gmKeyCommand.setKey(GMKeyCommand.GMKeyEventBack);
			break;
		case R.id.y:
			gmKeyCommand.setKey(GMKeyCommand.GMKeyGameY);
			break;
		case R.id.x:
			gmKeyCommand.setKey(GMKeyCommand.GMKeyGamex);

			break;
		case R.id.a:
			gmKeyCommand.setKey(GMKeyCommand.GMKeyGamea);

			break;
		case R.id.b:
			gmKeyCommand.setKey(GMKeyCommand.GMKeyGameb);

			break;
				
		}
    	GMDeviceController.getInstance().sendCommand(gmKeyCommand);
	}
}
