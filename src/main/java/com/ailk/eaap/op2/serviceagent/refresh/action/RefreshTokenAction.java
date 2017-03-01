package com.ailk.eaap.op2.serviceagent.refresh.action;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.asiainfo.integretion.o2p.serviceagent.cache.IMemcacheManageServ;
import com.linkage.rainbow.ui.struts.BaseAction;

public class RefreshTokenAction extends BaseAction{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public String execute() throws Exception{
		
		WebApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(this.getSession().getServletContext());
		IMemcacheManageServ memcacheManageServ = (IMemcacheManageServ)ctx.getBean("memcacheManageServ");
		memcacheManageServ.addToken();
		return SUCCESS;
	}
}
