package com.xgimi.zhushou.bean;

public class BaiBao {

	public String name;
	public int id;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public BaiBao(String name, int id) {
		this.name = name;
		this.id = id;
	}


}
