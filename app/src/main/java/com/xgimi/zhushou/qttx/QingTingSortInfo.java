package com.xgimi.zhushou.qttx;

import java.util.ArrayList;

public class QingTingSortInfo {

	public int errorno;
	public String errormsg;
	public ArrayList<Data> data;

	public class Data {
		public int id;
		public String name;
		public int sequence;
		public int section_id;
		public String type;
	}

}
