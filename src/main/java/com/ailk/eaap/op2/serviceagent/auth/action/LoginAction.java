package com.ailk.eaap.op2.serviceagent.auth.action;



import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;

import com.ailk.eaap.op2.serviceagent.auth.bo.User;
import com.ailk.eaap.op2.bo.App;
import com.ailk.eaap.op2.bo.AppUserInfo;
import com.linkage.rainbow.ui.struts.BaseAction;

public class LoginAction extends BaseAction{

	private String vercode;
	private User user = new User();
	private App app;
	private AppUserInfo appUserInfo;
	private String redirectUrl;
//	private static String assertionurl;
	
	public String execute() throws Exception{
		
		HttpServletRequest request = ServletActionContext.getRequest();
		String redirectUri = request.getParameter("redirectUri");
		if(redirectUri == null){
			redirectUrl = "http://www.baidu.com?error=redirectUriIsNull";
			return "redirectUrl";
		}
		
		HttpSession session = request.getSession();
		session.setAttribute("redirectUri", redirectUri);
		
		String loginPageType = request.getParameter("loginPageType");
		String display = request.getParameter("display");
		if(display == null){
			redirectUrl = redirectUri +"?error=displayIsNull";
			return "redirectUrl";
		}
		
		if(loginPageType == null){
			redirectUrl = redirectUri +"?error=loginPageTypeIsNull";
			return "redirectUrl";
		}
		
		if("1".equals(loginPageType)){
			return "login1";
		}else if("2".equals(loginPageType)){
			if("touch".equals(display)){
				return "loginTouch";
			}else if("page".equals(display)){
				return "loginPage";
			}
		}
		
		return "login1";
	}
	
	public String getVercode() {
		return vercode;
	}
	
	public void setVercode(String vercode) {
		this.vercode = vercode;
	}
	
	public User getUser() {
		return user;
	}
	
	public void setUser(User user) {
		this.user = user;
	}
	
	public App getApp() {
		return app;
	}
	
	public void setApp(App app) {
		this.app = app;
	}
	
	public AppUserInfo getAppUserInfo() {
		return appUserInfo;
	}
	
	public void setAppUserInfo(AppUserInfo appUserInfo) {
		this.appUserInfo = appUserInfo;
	}

	public String getRedirectUrl() {
		return redirectUrl;
	}

	public void setRedirectUrl(String redirectUrl) {
		this.redirectUrl = redirectUrl;
	}

}
