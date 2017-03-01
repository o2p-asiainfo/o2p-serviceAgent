package com.ailk.eaap.op2.serviceagent.auth.action;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;

import com.ailk.eaap.op2.serviceagent.auth.service.ServerOAuth;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.Interceptor;

public class ActionInterceptor implements Interceptor{

	public void destroy() {
	}

	public void init() {
	}

	public String intercept(ActionInvocation invocation) throws Exception {
		Object obj = ServletActionContext.getRequest().getSession().getAttribute("user");
		if(obj!=null){
			return invocation.invoke();
		}else{
			return "login";
		}
	}

	public void initParameters(){
		HttpServletRequest request = ServletActionContext.getRequest();
		String clientId = request.getParameter("client_id");
		String clientSecret = request.getParameter("client_secret");
		String redirectUri = request.getParameter("redirect_uri");
		ServerOAuth.getJson().authorizationRequest(clientId, clientSecret, redirectUri);
	}

}
