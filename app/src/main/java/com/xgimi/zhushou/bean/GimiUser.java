package com.xgimi.zhushou.bean;

/**
 * 用户解析类,附加了状态码和消息
 * 
 * @author liuyang
 *
 */
public class GimiUser {
	public info data=new info();
	public int code;
	public String message;

	public static class info {
		public String uid; // 用户id
		public String username; // 用户名
		public String email; // 绑定邮箱
		public String avatar; // 头像地址
		public String token; // 凭证
		public String picturelist; // 图片
		public String tel;

		public String getUid() {
			return uid;
		}

		public void setUid(String uid) {
			this.uid = uid;
		}

		public String getUsername() {
			return username;
		}

		public void setUsername(String username) {
			this.username = username;
		}

		public String getEmail() {
			return email;
		}

		public void setEmail(String email) {
			this.email = email;
		}

		public String getAvatar() {
			return avatar;
		}

		public void setAvatar(String avatar) {
			this.avatar = avatar;
		}

		public String getToken() {
			return token;
		}

		public void setToken(String token) {
			this.token = token;
		}

		public String getPicturelist() {
			return picturelist;
		}

		public void setPicturelist(String picturelist) {
			this.picturelist = picturelist;
		}

	}

	@Override
	public String toString() {
		return "uid:" + data.uid + "\n" + "username:" + data.username + "\n" + "email:" + data.email + "\n" + "avatar:" + data.avatar;
	}

}
