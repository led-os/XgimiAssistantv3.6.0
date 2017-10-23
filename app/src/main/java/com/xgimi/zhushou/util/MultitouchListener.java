package com.xgimi.zhushou.util;

import java.util.List;


public interface MultitouchListener {
	
	public void touchEvent(List<Point> point, TYPE type);
	
	public enum TYPE{
		SINGLE_PRESS  ("SingleTouchPress"),
		SINGLE_RELEASE("SingleTouchRelease"),
		SINGLE_MOTION ("SingleTouchMotion"),
		MULTITOUCH    ("MultiTouch"),
		NULL          ("null");
		
		private TYPE( String name){
			this.name = name;
		}
		
		private String name;
		
		public String getTypeName(){
			return name;
		}
	}
	public class Point{
		private float x;
		private float y;
		
		public Point(float x, float y){
			this.x = x;
			this.y = y;
		}
		
		public float x(){
			return x;
		}
		
		public float y(){
			return y;
		}
	}
}
