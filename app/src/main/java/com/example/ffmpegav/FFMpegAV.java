package com.example.ffmpegav;

public class FFMpegAV {

	public native void readThread();

	public native void audioThread();

	public native void videoThread();

	public void startreadThread() {
		new Thread() {
			public void run() {
				readThread();
			};
		}.start();
	}

}
