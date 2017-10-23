package com.xgimi.zhushou.bean;

public class PlayHostory {

	public String id;
	public String icon;
	public String title;
	public String time;
	public String kind;

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public PlayHostory(String id, String icon, String title, String time,String kind) {
		this.id = id;
		this.icon = icon;
		this.title = title;
		this.time = time;
		this.kind=kind;
	}
	
	
}
