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
import org.springframework.web.bind.annotation.RestController;

import com.ailk.eaap.integration.o2p.contractbody.util.LocalService;
import com.ailk.eaap.o2p.common.util.Constant;
import com.ailk.eaap.op2.serviceagent.common.MessageBO;
import com.ailk.eaap.op2.serviceagent.protocol.HttpController;
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
@RestController
public class HttpControllerAction {

		@Autowired
		private ProtoService protoService;
		@Autowired
		private HttpController httpController;
		@Autowired
		private LocalService localService;
	
		@RequestMapping(value = "/{tenantCode:\\S*?|\\S*?\\.\\S*?}/http/{serviceCode:\\S*?|\\S*?\\.\\S*?}")
	    public void tenantHttp(@PathVariable String tenantCode,@PathVariable String serviceCode,
	    		HttpServletRequest request,HttpServletResponse response) throws Exception {
	   
			doService(tenantCode, "", serviceCode, request, response, request.getMethod().toLowerCase());
	    }
		
		@RequestMapping(value = "/{tenantCode:\\S*?|\\S*?\\.\\S*?}/http/{baseContract:\\S*?|\\S*?\\.\\S*?}/{serviceCode:\\S*?|\\S*?\\.\\S*?}")
	    public void baseTenantHttp(@PathVariable String tenantCode, @PathVariable String baseContract,
	    		@PathVariable String serviceCode,
	    		HttpServletRequest request,HttpServletResponse response) throws Exception {
	   
			doService(tenantCode, baseContract, serviceCode, request, response, request.getMethod().toLowerCase());
	    }

		
		@RequestMapping(value = "/http/{serviceCode:\\S*?|\\S*?\\.\\S*?}")
	    public void http(@PathVariable String serviceCode,
	    		HttpServletRequest request,HttpServletResponse response) throws Exception {
	   
			doService("", "", serviceCode, request, response, request.getMethod().toLowerCase());
	    }
		

		@RequestMapping(value = "/http/{baseContract:\\S*?|\\S*?\\.\\S*?}/{serviceCode:\\S*?|\\S*?\\.\\S*?}")
	    public void BaseHttp(@PathVariable String baseContract,
	    		@PathVariable String serviceCode,HttpServletRequest request,HttpServletResponse response) throws Exception {
	   
			doService("", baseContract, serviceCode, request, response, request.getMethod().toLowerCase());
	    }
		

		public void doService(String tenantCode, String baseContract, String serviceCode,HttpServletRequest request,
				HttpServletResponse response, String action) throws IOException, ServletException {
			
			if(protoService.hasAuthCode(request, response, "http")) {
				return;
			}

			MessageBO messageBo = new MessageBO();
			if(!StringUtils.isEmpty(tenantCode)) {
				
				messageBo.getTenant().setTenantId(Integer.parseInt(tenantCode));
			}
			messageBo.setHttpMethod(action);
			messageBo.setServiceStyle(Constant.SERVICE_STYLE_HTTP);
			
			protoService.initUrlParam(request, messageBo);

			protoService.initHeadParam(request, messageBo);
			
			protoService.initBodyParam(request, messageBo, action);
			
			protoService.initJavaField(request, messageBo, action);
			
			this.invoke(request, response, action, messageBo, serviceCode);
		}


		protected void invoke(HttpServletRequest request,
				HttpServletResponse response, String action, MessageBO messageBo, String serviceCode) throws IOException {
		
			protoService.initAuth(messageBo, request);
			localService.setLocalInfo(request.getLocalPort());
			httpController.exchange(messageBo, serviceCode);
			//process by remote SA-server
			if(messageBo.getServiceAgentServerAddress()!=null && messageBo.getServiceAgentMode().equals("client") && messageBo.getAuthCode()!=null){
				
				protoService.callService(messageBo, request, response, action);
				
			}else{
				
				protoService.response(response, messageBo);
				
			}
		}
}
