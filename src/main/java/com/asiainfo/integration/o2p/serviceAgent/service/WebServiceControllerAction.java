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
import com.ailk.eaap.op2.common.InType;
import com.ailk.eaap.op2.serviceagent.common.MessageBO;
import com.ailk.eaap.op2.serviceagent.protocol.WebServiceController;
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
public class WebServiceControllerAction {

	@Autowired
	private ProtoService protoService;
	@Autowired
	private WebServiceController webServiceController;
	@Autowired
	private LocalService localService;
	
	@RequestMapping(value = "/webservice/{resourceAliss:\\S*?|\\S*?\\.\\S*?}")
    public void tenantWs1(@PathVariable String resourceAliss,
    		HttpServletRequest request,HttpServletResponse response) throws Exception {
		
		doService("", resourceAliss, request, response);
    }
	
	@RequestMapping(value = "/webservice/{baseContract:\\S*?|\\S*?\\.\\S*?}/{resourceAliss:\\S*?|\\S*?\\.\\S*?}")
    public void tenantWs2(@PathVariable String resourceAliss,
    		HttpServletRequest request,HttpServletResponse response) throws Exception {
   
		doService("", resourceAliss, request, response);
    }
	
	@RequestMapping(value = "/webservice/{baseContract:\\S*?|\\S*?\\.\\S*?}/{wsdlDoc:\\S*?|\\S*?\\.\\S*?}/{resourceAliss:\\S*?|\\S*?\\.\\S*?}")
    public void tenantWs3(@PathVariable String resourceAliss,
    		HttpServletRequest request,HttpServletResponse response) throws Exception {
   
		doService("", resourceAliss, request, response);
    }
	
	@RequestMapping(value = "/{tenantCode:\\S*?|\\S*?\\.\\S*?}/webservice/{resourceAliss:\\S*?|\\S*?\\.\\S*?}")
    public void tenantWs4(@PathVariable String tenantCode,@PathVariable String resourceAliss,
    		HttpServletRequest request,HttpServletResponse response) throws Exception {
   
		doService(tenantCode, resourceAliss, request, response);
    }
	
	@RequestMapping(value = "/{tenantCode:\\S*?|\\S*?\\.\\S*?}/webservice/{baseContract:\\S*?|\\S*?\\.\\S*?}/{resourceAliss:\\S*?|\\S*?\\.\\S*?}")
    public void tenantWs5(@PathVariable String tenantCode,@PathVariable String resourceAliss,
    		HttpServletRequest request,HttpServletResponse response) throws Exception {
   
		doService(tenantCode, resourceAliss, request, response);
    }
	
	@RequestMapping(value = "/{tenantCode:\\S*?|\\S*?\\.\\S*?}/webservice/{baseContract:\\S*?|\\S*?\\.\\S*?}/{wsdlDoc:\\S*?|\\S*?\\.\\S*?}/{resourceAliss:\\S*?|\\S*?\\.\\S*?}")
    public void tenantWs6(@PathVariable String tenantCode,@PathVariable String resourceAliss,
    		HttpServletRequest request,HttpServletResponse response) throws Exception {
   
		doService(tenantCode, resourceAliss, request, response);
    }
	
	private void doService(String tentantCode, String resourceAliss, HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException{
		
		if(protoService.hasAuthCode(request, response, "webservice")) {
			
			return;
		}

		if(InType.get.equals(request.getMethod().toLowerCase())) {
			
			doGet(tentantCode, resourceAliss, request, response);
		} else if(InType.post.equals(request.getMethod().toLowerCase())) {
			
			doPost(tentantCode, resourceAliss, request, response);
		}
	}

	private void doGet(String tentantCode, String resourceAliss, HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
	
		if (isWSDLRequest(request, resourceAliss)) {
			generateWSDL(response, resourceAliss,tentantCode);
		}
	}


	@SuppressWarnings("rawtypes")
	private void doPost(String tentantCode, String resourceAliss,
			HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		MessageBO messageBo = new MessageBO();
		if(!StringUtils.isEmpty(tentantCode)) {
			
			messageBo.getTenant().setTenantId(Integer.parseInt(tentantCode));
		}
		messageBo.setServiceStyle(Constant.SERVICE_STYLE_RESTFUL);
		
		protoService.initUrlParam(request, messageBo);
		
		protoService.initHeadParam(request, messageBo);
		
		protoService.initBodyParam(request, messageBo, InType.GeneralWebService);
			
		protoService.initJavaField(request, messageBo, InType.GeneralWebService);
		
		if (isWSDLRequest(request, resourceAliss)) {
			generateWSDL(response, resourceAliss,tentantCode);
		} else {
			this.invoke(request, response, resourceAliss, InType.GeneralWebService, messageBo);
		}
	}

	private boolean isWSDLRequest(HttpServletRequest request,
			String resourceAliss) {

		return (request.getQueryString() != null
				&& (request.getQueryString().trim().equalsIgnoreCase("wsdl")) || resourceAliss.toLowerCase()
					.endsWith(".xsd") || resourceAliss.toLowerCase()
					.endsWith(".wsdl"));
	}

	private void generateWSDL(HttpServletResponse response,
			String resourceAliss, String tentantCode) throws ServletException, IOException {
		String returnXml = getWsdlFile(resourceAliss,tentantCode);

		if ("".equals(returnXml)) {
			response.sendError(404, "No wsdl is avaiable for this service");
			return;
		}

		response.getWriter().write(returnXml);
		response.getWriter().flush();
		response.getWriter().close();
	}

	private String getWsdlFile(String resourceAliss, String tentantCode) {
		
		return webServiceController.getWsdlFile(resourceAliss,tentantCode);
	}


	@SuppressWarnings({ "rawtypes" })
	protected void invoke(HttpServletRequest request,
			HttpServletResponse response, String resourceAliss,
			String action, MessageBO messageBo) throws IOException  {

		protoService.initAuth(messageBo, request);
		localService.setLocalInfo(request.getLocalPort());
		webServiceController.exchange(messageBo, resourceAliss);
		//process by remote SA-server
		if(messageBo.getServiceAgentServerAddress()!=null && messageBo.getServiceAgentMode().equals("client") && messageBo.getAuthCode()!=null) {
			
			protoService.callService(messageBo, request, response, action);
		}else{

			protoService.response(response, messageBo);
		}
		
	}
}
