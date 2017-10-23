package com.xgimi.zhushou.bean;

public class ImageInfo {
	public static final int TYPE_CHECKED = 1;
	public static final int TYPE_NOCHECKED = 0;
	public int type;
	public boolean isCance;
	public long id;
	public String url;
	public boolean isUpload;
	public String time;

	public void setTime(String time){
		this.time=time;
	}
	public String getTime(){
		return time;
	}

	public ImageInfo() {
		isUpload = false;
	}
	public void setType(int type){
		this.type=type;
	}

	public ImageInfo(long id, String url) {
		super();

		this.id = id;
		this.url = url;
		isUpload = false;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public boolean isUpload() {
		return isUpload;
	}

	public void setUpload(boolean isUpload) {
		this.isUpload = isUpload;
	}


}