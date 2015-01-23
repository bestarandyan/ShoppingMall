package com.manyi.mall.cachebean.user;
/*
 * @author zxc
 *
 */
public class ModifyLoginPwdRequest {

	private int uid;
	private String oldPwd;
	private String newPwd;
	private String confPwd;

	public int getUid() {
		return uid;
	}

	public void setUid(int uid) {
		this.uid = uid;
	}

	public String getOldPwd() {
		return oldPwd;
	}
	
	public String getConfPwd() {
		return confPwd;
	}

	public void setConfPwd(String confPwd) {
		this.confPwd = confPwd;
	}

	public void setOldPwd(String oldPwd) {
		this.oldPwd = oldPwd;
	}
	public String getNewPwd() {
		return newPwd;
	}
	public void setNewPwd(String newPwd) {
		this.newPwd = newPwd;
	}
}
