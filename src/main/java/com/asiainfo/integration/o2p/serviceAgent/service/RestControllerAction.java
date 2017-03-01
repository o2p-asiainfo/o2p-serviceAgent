/** 
 * Project Name:o2p-serviceAgent 
 * File Name:Controller.java 
 * Package Name:com.asiainfo.integration.o2p.serviceAgent.service 
 * Date:2015年11月24日下午5:41:15 
 * Copyright (c) 2015, www.asiainfo.com All Rights Reserved. 
 * 
*/  
  
package com.asiainfo.integration.o2p.serviceAgent.service;  

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.ailk.eaap.integration.o2p.contractbody.util.LocalService;
import com.ailk.eaap.o2p.common.util.Constant;
import com.ailk.eaap.op2.serviceagent.common.MessageBO;
import com.ailk.eaap.op2.serviceagent.protocol.RestController;
import com.ailk.eaap.op2.serviceagent.servlet.service.ProtoService;

/** 
 * ClassName:Controller  
 * Function: TODO ADD FUNCTION.  
 * Reason:   TODO ADD REASON.  
 * Date:     2015年11月24日 下午5:41:15  
 * @author   wuwz 
 * @version   
 * @since    JDK 1.6 
 *        
 */
@org.springframework.web.bind.annotation.RestController
public class RestControllerAction {

		@Autowired
		private ProtoService protoService;
		@Autowired
		private RestController restController;
		@Autowired
		private LocalService localService;
		
		/**
		 * http://url/{租户Code}/rest/{资源}
		 */
		@RequestMapping(value = "/{tenantCode:\\S*?|\\S*?\\.\\S*?}/rest/**")
	    public void tenantRest(@PathVariable String tenantCode,
	    		HttpServletRequest request,HttpServletResponse response) throws Exception {
	   
			String action = request.getMethod();
			doService(tenantCode, request, response, action.toLowerCase());
	    }
	
		/**
		 * http://url/rest/{资源}
		 */
		@RequestMapping(value = "/rest/**")
	    public void rest(HttpServletRequest request,HttpServletResponse response) throws Exception {
	   
			String action = request.getMethod();
			doService("", request, response, action.toLowerCase());
	    }
		
		private void doService(String tenantCode, HttpServletRequest request, HttpServletResponse response, String action) throws IOException, ServletException  {
			
			if(protoService.hasAuthCode(request, response, "rest")) {
				return;
			}
			
			String pathInfo = request.getServletPath();
		
			if (pathInfo == null) {
				
				return;
			}
			
			String restResource = pathInfo.substring(pathInfo.indexOf("rest")+4, pathInfo.length());
			
			MessageBO messageBo = new MessageBO();
			if(!StringUtils.isEmpty(tenantCode)) {
				
				messageBo.getTenant().setTenantId(Integer.parseInt(tenantCode));
			}
			messageBo.setHttpMethod(action);
			messageBo.setServiceStyle(Constant.SERVICE_STYLE_RESTFUL);
			
			protoService.initUrlParam(request, messageBo);

			protoService.initHeadParam(request, messageBo);
			
			protoService.initBodyParam(request, messageBo, action);
			
			protoService.initJavaField(request, messageBo, action);
			
			this.invoke(request, response, action, restResource, messageBo);
				
		}
		


		protected void invoke(HttpServletRequest request,
				HttpServletResponse response, 
				String action, String restResource, 
				MessageBO<?> messageBo) throws IOException  {

			protoService.initAuth(messageBo, request);
			localService.setLocalInfo(request.getLocalPort());
			restController.exchange(messageBo, restResource);
			//process by remote SA-server
			if(messageBo.getServiceAgentServerAddress()!=null && messageBo.getServiceAgentMode().equals("client") && messageBo.getAuthCode()!=null){
				
				protoService.callService(messageBo, request, response, action);
			}else{
				
				protoService.response(response, messageBo);
			}
		}
}
