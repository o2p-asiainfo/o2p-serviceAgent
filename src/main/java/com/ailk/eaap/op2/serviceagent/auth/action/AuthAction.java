package com.ailk.eaap.op2.serviceagent.auth.action;

import org.apache.commons.lang.ArrayUtils;
import org.apache.struts2.ServletActionContext;

import com.ailk.eaap.op2.serviceagent.auth.bo.User;
import com.ailk.eaap.op2.serviceagent.auth.service.ServerOAuth;
import com.ailk.eaap.op2.bo.App;
import com.linkage.rainbow.ui.struts.BaseAction;

public class AuthAction extends BaseAction {

	private static final long serialVersionUID = 1L;
	private String[] apiMethod;
	private String redirectUrl;
	private App app;
	private User user;

	public String execute() throws Exception {
		Object appObj = ServletActionContext.getContext().getSession()
				.get("clientApp");
		Object userObj = ServletActionContext.getContext().getSession()
				.get("user");

		if (appObj != null) {
			app = (App) appObj;
		}
		if (userObj != null) {
			user = (User) userObj;
		}
		if (userObj == null && appObj == null) {
			return "login";
		}
		if (userObj == null && appObj != null) {
			return "login";
		}
		if (userObj != null && appObj == null) {
			return "custCenter";
		}

		app.setPhoneNum(user.getPhoneNumbetr());
		if (apiMethod == null || apiMethod.length == 0) {
			addActionError(getText("eaap.op2.portal.auth.noapiselected"));
			return ERROR;
		}

		redirectUrl = app.getAppCallbackUrl();
		String code = ServerOAuth.genericCode(app.getAppkey());
		redirectUrl = redirectUrl + "?code=" + code;
		app.setOauthCode(code);
		app.setApiMethod(apiMethod);

		return SUCCESS;
	}

	public String getRedirectUrl() {
		return redirectUrl;
	}

	public void setRedirectUrl(String redirectUrl) {
		this.redirectUrl = redirectUrl;
	}

	public void setApiMethod(String[] apiMethod) {
		if(ArrayUtils.isEmpty(apiMethod)){
			this.apiMethod =  (String[]) ArrayUtils.clone(apiMethod);
		}
	}

	public App getApp() {
		return app;
	}

	public void setApp(App app) {
		this.app = app;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
}
