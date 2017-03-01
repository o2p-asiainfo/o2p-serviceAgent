package com.ailk.eaap.op2.serviceagent.servlet;

import javax.servlet.http.HttpServletRequest;

import org.apache.cxf.transport.http.AbstractHTTPDestination;

import net.sf.json.JSONObject;




import com.ailk.eaap.op2.common.InType;
import com.ailk.eaap.op2.serviceagent.common.MessageBO;
import com.ailk.eaap.op2.serviceagent.deal.service.IDIHService;

public class EAAPService implements IDEPService{
	
	private IDIHService dihService;
	public String exchange(String message) {
		javax.xml.ws.handler.MessageContext ctx = context.getMessageContext();
		HttpServletRequest request =(HttpServletRequest)ctx.get(AbstractHTTPDestination.HTTP_REQUEST);
		String ip = request.getRemoteAddr();
		return exchange(message,ip,InType.CEPWEBSERVICE);
	}
	
	public String apiexchange(String message) {
		javax.xml.ws.handler.MessageContext ctx = context.getMessageContext();
		HttpServletRequest request =(HttpServletRequest)ctx.get(AbstractHTTPDestination.HTTP_REQUEST);
		String ip = request.getRemoteAddr();
		return exchange(message,ip,InType.RESTPOST);
	}
	
	@SuppressWarnings("rawtypes")
	public String exchange(String message,String ip,String intype){
		MessageBO messageBo = new MessageBO();
		messageBo.setInType(intype);
		messageBo.setIp(ip);
		initMsgBody(messageBo, message);
		return dihService.exchange(messageBo);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void initMsgBody(MessageBO messageBo, String message) {
		if(message.startsWith("<")) {
			messageBo.setMsgBody(ServletHelper.getDocument(message));
		} else if(message.startsWith("{")){
			messageBo.setMsgBody(JSONObject.fromObject(message));
		} else {
			messageBo.setMsgBody(message);
		}
		
	}

	public IDIHService getDihService() {
		return dihService;
	}
	
	public void setDihService(IDIHService dihService) {
		this.dihService = dihService;
	}

}
