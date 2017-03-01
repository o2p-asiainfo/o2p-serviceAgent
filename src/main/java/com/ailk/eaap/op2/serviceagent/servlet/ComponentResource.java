package com.ailk.eaap.op2.serviceagent.servlet;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.restlet.data.Form;
import org.restlet.data.MediaType;
import org.restlet.data.Reference;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;

import com.ailk.eaap.op2.common.CommonUtil;
import com.ailk.eaap.op2.common.InType;
import com.ailk.eaap.op2.serviceagent.common.MessageBO;
import com.ailk.eaap.op2.serviceagent.deal.service.IDIHService;
/**
 * 
 * rest接入资料管理类<br>
 * 获得rest接入的请求信息，并调用后端的处理逻辑
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
public class ComponentResource extends ServerResource {
	private IDIHService dihService;
	private static final Logger log = Logger.getLogger(ComponentResource.class);

	public IDIHService getDihService() {
		return dihService;
	}

	public void setDihService(IDIHService dihService) {
		this.dihService = dihService;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	protected Representation post(Representation entity)
			 {
		try{
			String reqXml = entity.getText();
			String ip = getRequest().getClientInfo().getAddress();
			MessageBO messageBo  = new MessageBO();
			messageBo.setInType(InType.RESTPOST);
			messageBo.setMsgBody(reqXml);
			messageBo.setIp(ip);
			
			StringRepresentation entityRsp=new StringRepresentation(dihService.exchange(messageBo), MediaType.TEXT_XML);
			
			return entityRsp;
		}catch(IOException e){
			log.error(e.getStackTrace());
		}
		return null;
		
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	protected Representation get() {
		StringRepresentation entityRsp = null;
			
		MessageBO messageBo  = new MessageBO();
		Reference reference = getRequest().getResourceRef();
		String segment = reference.getBaseRef().getLastSegment();
		
		if("alipaycallback".equals(segment)){
			setAlipayParam(reference);
		}
		Form form = getRequest().getResourceRef().getQueryAsForm();//获得urlget参数
		messageBo.setRestFormString(form.getQueryString());
		log.debug(form.getQueryString());
		 messageBo.setMessageMap(form.getValuesMap());//把urlget参数转化成map对象
		 messageBo.setInType(InType.RESTGET);
		 try {
			if("alipaycallback".equals(segment)){
				messageBo.setMsgBody(URLDecoder.decode(getRequest().getResourceRef().toString(),"GBK"));
			}
			else{
				messageBo.setMsgBody(URLDecoder.decode(getRequest().getResourceRef().toString(),"UTF-8"));
			}
		} catch (UnsupportedEncodingException e) {
			log.error(e.getStackTrace());
			return null;
		}
		 
		String apiName = null;
		if(getRequest().getAttributes().get("apiName")!=null){
			apiName = (String) getRequest().getAttributes().get("apiName");
		}
		
		if(messageBo.getMessageMap().get("Method")!=null){//如果服务使用方采用Method参数传递api名称，则替换/路径的api名称
			apiName=messageBo.getMessageMap().get("Method").toString();
		}
		
		if("rechargeIssuedTest".equals(apiName)){
			return rechargeIssuedTest(messageBo);
		}
		if(messageBo.getMessageMap().get("BusiCode")!=null){//如果服务使用方采用Method参数传递api名称，则替换/路径的api名称
			apiName=messageBo.getMessageMap().get("BusiCode").toString();
		}
		log.debug(apiName);
		
		messageBo.setApiname(apiName);
		
		messageBo.setIp(getRequest().getClientInfo().getAddress());//获得客户端IP
		entityRsp=new StringRepresentation(dihService.exchange(messageBo), MediaType.TEXT_XML);
		return entityRsp;
	}
	
	private void setAlipayParam(Reference reference){
		reference.addQueryParameter("Method", "dutCallBack");
		reference.addQueryParameter("AppKey", "d459305530be25a8c18e23288cce974e");
		reference.addQueryParameter("TransactionID", "agentFeeDao.getAlipayTransactionID()");//改为从缓存获取数据
		reference.addQueryParameter("ReqTime", CommonUtil.getNow());
	}

	private StringRepresentation rechargeIssuedTest(MessageBO<String> messageBO) {
		try{
			Map<?, ?> paramMap = messageBO.getMessageMap();
			
			Document rspDocument = DocumentHelper.parseText("<ContractRoot></ContractRoot>");
			Element contractRootElement = (Element) rspDocument.selectSingleNode("/ContractRoot");
			Element tcpContElement = contractRootElement.addElement("TcpCont");
			Element svcContElement = contractRootElement.addElement("SvcCont");
			
			Element transactionIDElement = tcpContElement.addElement("TransactionID");
			Element actionCodeElement = tcpContElement.addElement("ActionCode");
			Element rspTimeElement = tcpContElement.addElement("RspTime");
			Element responseElement = tcpContElement.addElement("Response");
			transactionIDElement.setText((String)paramMap.get("TransactionID"));
			actionCodeElement.setText("1");
			rspTimeElement.setText(new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
			
			Element rspTypeElement = responseElement.addElement("RspType");
			Element rspCodeElement = responseElement.addElement("RspCode");
			Element rspDescElement = responseElement.addElement("RspDesc");
			rspTypeElement.setText("0");
			rspCodeElement.setText("0000");
			rspDescElement.setText("success");
			
			Element currDateElement = svcContElement.addElement("CurrDate");
			Element staffIdElement = svcContElement.addElement("StaffId");
			Element acctIdElement = svcContElement.addElement("AcctId");
			Element paySerialNbrElement = svcContElement.addElement("PaySerialNbr");
			currDateElement.setText(new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
			staffIdElement.setText("1100260");
			acctIdElement.setText("115582474");
			SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
			paySerialNbrElement.setText((int)(random.nextInt()*(999999-100000)+100000) + new SimpleDateFormat("yyMMddHHmmss").format(new Date()));
			
			return new StringRepresentation(rspDocument.asXML(),MediaType.TEXT_XML);
		}catch(DocumentException e){
			log.error(e.getStackTrace());
			return null;
		}catch(NoSuchAlgorithmException e){
			log.error(e.getStackTrace());
			return null;
		}
	}
}
