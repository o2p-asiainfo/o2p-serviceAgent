package com.ailk.eaap.op2.serviceagent.auth.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;

import com.linkage.rainbow.ui.struts.BaseAction;

public class TicketAction extends BaseAction{
	
	private String redirectUrl;
	
	public String execute() throws Exception{
		
		HttpServletRequest request = ServletActionContext.getRequest();
		String ticket = request.getParameter("UATicket");
		
		HttpSession session = request.getSession();
		redirectUrl = (String) session.getAttribute("redirectUri");
		
		if(redirectUrl == null){
			
			return "login";
		}
		
		redirectUrl = redirectUrl + "?ticket="+ticket;
		return "redirectUrl";

	}

	public String getRedirectUrl() {
		return redirectUrl;
	}

	public void setRedirectUrl(String redirectUrl) {
		this.redirectUrl = redirectUrl;
	}
	
	
}
