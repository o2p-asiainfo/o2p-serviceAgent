package com.ailk.eaap.op2.serviceagent.cust.action;


import com.ailk.eaap.op2.serviceagent.auth.bo.User;
import com.linkage.rainbow.ui.struts.BaseAction;

public class CancelAuthAction extends BaseAction{

	private static final long serialVersionUID = 1L;
	
	private Integer cancelAppId;
	private User user;
	
	public String execute() throws Exception{
		
		return SUCCESS;
	}

	public Integer getCancelAppId() {
		return cancelAppId;
	}

	public void setCancelAppId(Integer cancelAppId) {
		this.cancelAppId = cancelAppId;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

}
