package com.sun3d.culturalShanghai.object;

public class WeiXinInfo {
	private String icon = "";
	private String token = "";
	private String nickname = "";
	private String expiresTime = "";
	private String expiresIn = "";
	private String unionid = "";
	private String province = "";
	private String openid = "";
	private String weibo = "";

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getExpiresTime() {
		return expiresTime;
	}

	public void setExpiresTime(String expiresTime) {
		this.expiresTime = expiresTime;
	}

	public String getExpiresIn() {
		return expiresIn;
	}

	public void setExpiresIn(String expiresIn) {
		this.expiresIn = expiresIn;
	}

	public String getUnionid() {
		return unionid;
	}

	public void setUnionid(String unionid) {
		this.unionid = unionid;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getOpenid() {
		return openid;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}

	public String getWeibo() {
		return weibo;
	}

	public void setWeibo(String weibo) {
		this.weibo = weibo;
	}

	@Override
	public String toString() {
		return "WeiXinInfo [icon=" + icon + ", token=" + token + ", nickname=" + nickname
				+ ", expiresTime=" + expiresTime + ", expiresIn=" + expiresIn + ", unionid="
				+ unionid + ", province=" + province + ", openid=" + openid + ", weibo=" + weibo
				+ "]";
	}

}
