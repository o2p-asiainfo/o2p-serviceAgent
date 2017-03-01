package com.ailk.eaap.op2.serviceagent.cust.action;

import com.ailk.eaap.op2.serviceagent.auth.bo.User;
import com.ailk.eaap.op2.bo.AppUserInfo;
import com.linkage.rainbow.ui.struts.BaseAction;

public class CustAction extends BaseAction{

	private static final long serialVersionUID = 1L;
	
	private User user;
	private AppUserInfo appUserInfo;
	public String execute() throws Exception{
		return "";
	}
	
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public AppUserInfo getAppUserInfo() {
		return appUserInfo;
	}

	public void setAppUserInfo(AppUserInfo appUserInfo) {
		this.appUserInfo = appUserInfo;
	}
	
}
