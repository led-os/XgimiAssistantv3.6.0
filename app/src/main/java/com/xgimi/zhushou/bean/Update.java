package com.xgimi.zhushou.bean;

import java.io.Serializable;

/**
 * app更新类(通过get/set读写)
 * 
 * @author liuyang
 *
 */
public class Update implements Serializable {


	

	public DataBean data;


	public int code;
	public String message;

	

	public static class DataBean {
		public String package_name;
		public String version_code;
		public String download_url;
		public String gimi_device;
		public String file_md5;
		public String version_name;
		public String desc;

	}
}
