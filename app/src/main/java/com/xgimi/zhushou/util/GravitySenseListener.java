package com.xgimi.zhushou.util;

public interface GravitySenseListener {

	public void gravitySenseListener(Point point, TYPE type);

	public enum TYPE {
		ORIENTATION("Orientate", "TvOrientationInput"), ACCELEROMETER(
				"Accelerate", "TvAccelerometerInput"), NULL("null", "");

		private TYPE(String action, String flag) {
			this.action = action;
			this.flag = flag;
		}

		private String flag;
		private String action;

		public String getAction() {
			return action;
		}

		public String getFlag() {
			return flag;
		}
	}

	public static class Point {
		private float x;
		private float y;
		private float z;

		public Point(float x, float y, float z) {
			this.x = x;
			this.y = y;
			this.z = z;
		}

		public void set(float x, float y, float z) {
			this.x = x;
			this.y = y;
			this.z = z;
		}

		public float getX() {
			return x;
		}

		public float getY() {
			return y;
		}

		public float getZ() {
			return z;
		}
	}
}
