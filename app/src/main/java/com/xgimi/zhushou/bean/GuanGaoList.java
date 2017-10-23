package com.xgimi.zhushou.bean;

import java.util.List;

public class GuanGaoList {

	public int code;
	public String message;
	public List<Infor> data;
	public static class Infor{
		public String id;
		public String title;
		public String singer;
	}
}
