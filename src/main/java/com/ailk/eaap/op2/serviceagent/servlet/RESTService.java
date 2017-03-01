package com.ailk.eaap.op2.serviceagent.servlet;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.cxf.transport.http.AbstractHTTPDestination;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Node;

import com.ailk.eaap.op2.common.CommonUtil;
import com.ailk.eaap.op2.common.InType;
import com.ailk.eaap.op2.serviceagent.common.MessageBO;
import com.ailk.eaap.op2.serviceagent.deal.service.IDIHService;
import com.asiainfo.foundation.log.LogModel;
import com.asiainfo.foundation.log.Logger;

public class RESTService implements IDEPService{
	
	private static final Logger LOG = Logger.getLog(RESTService.class);
	private IDIHService dihService;
	
	public String exchange(String message) {
		javax.xml.ws.handler.MessageContext ctx = context.getMessageContext();
		HttpServletRequest request =(HttpServletRequest)ctx.get(AbstractHTTPDestination.HTTP_REQUEST);
		String ip = request.getRemoteAddr();
		String  apiName = CommonUtil.getNodeValue(message, InType.Method);
		if("detectTest".equals(apiName)){
			return detectTest(message);
		}
		return exchange(message,ip,InType.RESTPOST);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String exchange(String message,String ip,String intype){
		MessageBO messageBo = new MessageBO();
		messageBo.setInType(intype);
		messageBo.setIp(ip);
		messageBo.setMsgBody(message);
		return dihService.exchange(messageBo);
	}
	
	private String detectTest(String message){
		Document xmlDoc = null;
		try {
			xmlDoc = DocumentHelper.parseText(message);
		} catch (DocumentException e) {
			LOG.error(LogModel.EVENT_APP_EXCPT, e.getCause());
		}
		String rspTime = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
		
		String transactionID = null;
		if(xmlDoc != null) {
			Node transNode = xmlDoc.selectSingleNode("/ContractRoot/TcpCont/TransactionID");
			if(transNode != null) {
				transactionID = transNode.getText();
			}
		}
		
		StringBuffer result = new StringBuffer("<?xml version=\"1.0\" encoding=\"UTF-8\"?><ContractRoot>");
		result.append("<TcpCont>");
		result.append("<TransactionID>");
		result.append(transactionID);
		result.append("</TransactionID>");
		result.append("<Method>detectTest</Method>");
		result.append("<RspTime>");
		result.append(rspTime);
		result.append("</RspTime>");
		result.append("<ActionCode>1</ActionCode");
		result.append("</TcpCont>");
		result.append("<SvcCont><RspCode>0000</RspCode></SvcCont>");
		return result.toString();
	}
	
	public IDIHService getDihService() {
		return dihService;
	}
	public void setDihService(IDIHService dihService) {
		this.dihService = dihService;
	}

	public String apiexchange(String message) {
		return null;
	}
}
