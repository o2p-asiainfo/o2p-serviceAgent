package com.ailk.eaap.op2.serviceagent.servlet;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.ailk.eaap.op2.serviceagent.common.ReadProperties;
import com.asiainfo.integretion.o2p.serviceagent.cache.IMemcacheManageServ;
import com.linkage.rainbow.util.spring.ContextHolder;


public class EAAPServiceContextListener  implements ServletContextListener{

	private IMemcacheManageServ memcacheServ;
	public static final String INTERNATION_FILE = "internationFile";
	
	public void contextDestroyed(ServletContextEvent arg0) {
		
	}

	public void contextInitialized(ServletContextEvent event) {
		// TODO Auto-generated method stub
		ApplicationContext context = WebApplicationContextUtils.getRequiredWebApplicationContext(event.getServletContext());
		ContextHolder.setApplicationContext(context);
		String fileName = event.getServletContext().getInitParameter(INTERNATION_FILE);
		ReadProperties.setFileName(fileName);
		memcacheServ = (IMemcacheManageServ)context.getBean("memcacheManageServ");
		memcacheServ.addAll(null);
		
	}

}
