package com.xgimi.zhushou.bean;

/**
 * 头像查询请求结果
 * 
 * @author liuyang
 *
 */
public class AvatarshowMsg {

	public AvatarData data;
	public int code; // 状态码
	public String message; // 消息

	public static class AvatarData {
		public String avatar;
	}

}
