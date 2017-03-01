package com.ailk.eaap.op2.serviceagent.servlet;

import org.restlet.Application;
import org.restlet.Restlet;
import org.restlet.routing.Router;
/**
 * 
 * REST接入管理类<br>
 * REST接入管理，截取/下面的api名称
 * <p>
 * @version 1.0
 * @author litieyang Feb 19, 2013
 * <hr>
 * 修改记录
 * <hr>
 * 1、修改人员:    修改时间:<br>       
 *    修改内容:
 * <hr>
 */
public class ComponentApplication extends Application {

	public synchronized Restlet createInboundRoot() { 
		Router router = new Router(getContext()); 	
		
		router.attach("/api", ComponentResource.class);    //获取上下文/路径下的api名称
		
		return router;    
	}
}
