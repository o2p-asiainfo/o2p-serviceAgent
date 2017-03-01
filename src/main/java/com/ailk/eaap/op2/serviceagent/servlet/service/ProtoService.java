/** 
 * Project Name:o2p-serviceAgent 
 * File Name:ProtoService.java 
 * Package Name:com.ailk.eaap.op2.serviceagent.servlet.service 
 * Date:2015年4月14日下午2:32:57 
 * Copyright (c) 2015, www.asiainfo.com All Rights Reserved. 
 * 
*/  
  
package com.ailk.eaap.op2.serviceagent.servlet.service;  

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ailk.eaap.o2p.common.spring.config.O2pPropertyPlaceholderConfigurer;
import com.ailk.eaap.o2p.common.spring.config.ZKCfgCacheHolder;
import com.ailk.eaap.o2p.common.util.Constant;
import com.ailk.eaap.op2.common.EAAPConstants;
import com.ailk.eaap.op2.common.InType;
import com.ailk.eaap.op2.common.OutType;
import com.ailk.eaap.op2.common.StatisticData;
import com.ailk.eaap.op2.serviceagent.common.EOPDomain;
import com.ailk.eaap.op2.serviceagent.common.MessageBO;
import com.ailk.eaap.op2.serviceagent.common.RequestDispatchUtil;
import com.ailk.eaap.op2.serviceagent.protocol.ProtocolController;
import com.asiainfo.foundation.log.Logger;

/** 
 * ClassName:ProtoService  
 * Function: TODO ADD FUNCTION.  
 * Reason:   TODO ADD REASON.  
 * Date:     2015年4月14日 下午2:32:57  
 * @author   wuwz 
 * @version   
 * @since    JDK 1.6 
 *    
 */
@Service
public class ProtoService implements Serializable {

	private static final long serialVersionUID = 1L;

	private static final Logger LOG = Logger.getLog(ProtoService.class);
	@Autowired
	private ProtocolController protocolController;
	private String serviceAgentMode = O2pPropertyPlaceholderConfigurer.globalProperties.getProperty("serviceAgent.mode");
	
	/**
	 * 
	 * initUrlParam:(加载url参数).  
	 * TODO(这里描述这个方法适用条件 – 可选). 
	 * TODO(这里描述这个方法的执行流程 – 可选). 
	 * TODO(这里描述这个方法的使用方法 – 可选). 
	 * TODO(这里描述这个方法的注意事项 – 可选). 
	 * 
	 * @author wuwz 
	 * @param request
	 * @param messageBo 
	 * @since JDK 1.6
	 */
	public void initUrlParam(HttpServletRequest request, MessageBO messageBo) {
		/*put real ip address*/
		putRealIpAddress(messageBo, request);
				
		Map readOnlyMap = request.getParameterMap();

		Map<String, Object> writeAbleMap = new HashMap<String, Object>();
		
		StringBuffer queryString = new StringBuffer(request.getRequestURL()!=null?request.getRequestURL():"");  

		if (readOnlyMap != null && !readOnlyMap.isEmpty()) {
			queryString.append("?");
			Set<Map.Entry<String, String[]>> entry = readOnlyMap.entrySet();
			for (Map.Entry<String, String[]> kv : entry) {
				String key = kv.getKey();
				String[] value = (String[]) readOnlyMap.get(key);
				if(value.length>1) {
					writeAbleMap.put(key, value);
					for(int j =0 ;j<value.length;j++){
						queryString.append(key);
						queryString.append("=");
						queryString.append(value[j]);
						queryString.append("&");						
					}
								
				} else {
					if("tenant".equals(key)) {

						messageBo.getTenant().setTenantId(Integer.parseInt(value[0]));
						LOG.debug("=========> tenant:"+value[0]);
					} else {
						
						writeAbleMap.put(key, value[0]);
						queryString.append(key);
						queryString.append("=");
						queryString.append(value[0]);
						queryString.append("&"); 
					}
				}
			}
		}

		messageBo.setMessageMap(writeAbleMap);
		
		/*put requst url*/
		if (readOnlyMap != null && !readOnlyMap.isEmpty()){
			messageBo.setRequestURL(queryString.toString().substring(0, queryString.toString().length() - 1));
		}else{
			messageBo.setRequestURL(queryString.toString());
		}
		LOG.info("request url is " + messageBo.getRequestURL());
	}
	
	public void initHeadParam(HttpServletRequest request, MessageBO messageBo) {
		 
		Enumeration reqHeaderEnum = request.getHeaderNames();
		
		while( reqHeaderEnum.hasMoreElements() ) {
		    String headerName = (String)reqHeaderEnum.nextElement();
		    if(!InType.headers.contains(headerName.toLowerCase())) {
		    	messageBo.getMsgHead().put(headerName, request.getHeader(headerName));
		    } 
		}
	}
	
	public void initBodyParam(HttpServletRequest request, MessageBO messageBo, String action) throws ServletException, IOException {
		
		if (!InType.get.equals(action) && !InType.delete.equals(action)) {
			
			String reqXml = getInputParameter(request);
			messageBo.setMsgBody(reqXml);
		}
	}
	
	public void initJavaField(HttpServletRequest request, MessageBO messageBo, String action) {

		//String ip = request.getRemoteAddr();
		messageBo.setInType(action);
		//messageBo.setIp(ip);
		messageBo.setReqorrsp(EOPDomain.REQ_FLAG);
		
		Map<String,String> mapUrl = messageBo.getMessageMap();
		Map<String,Object> mapHead = messageBo.getMsgHead();
		Map<String,Object> map = new HashMap<String, Object>();
		map.putAll(mapUrl);
		map.putAll(mapHead);
		
		if(map.get(InType.ReqTime) != null) {
			messageBo.setSrcReqTime(map.get(InType.ReqTime).toString());
		} 
		if(map.get(InType.DstSysID) != null) {
			messageBo.setDstSyscode(map.get(InType.DstSysID).toString());
		} 
		if(map.get(InType.Format) != null) {
			messageBo.setFormat(map.get(InType.Format).toString());
		} 
		if(map.get(InType.TRANSID) != null) {
			messageBo.setTransactionID(map.get(InType.TRANSID).toString());
		} 
		if(map.get(InType.Method) != null) {
			messageBo.setApiname(map.get(InType.Method).toString());
		} 
		if(map.get(InType.BusiCode) != null) {
			messageBo.setApiname(map.get(InType.BusiCode).toString());
		} 
		if(map.get(InType.ServiceContractVer) != null) {
			messageBo.setContractVer(map.get(InType.ServiceContractVer).toString());
		} 
		if(map.get(InType.IP) != null) {
			messageBo.setIp(map.get(InType.IP).toString());
		} 
		if(map.get(InType.Sign) != null) {
			messageBo.setSign(map.get(InType.Sign).toString());
		} 
		if(map.get(InType.AccessToken) != null) {
			messageBo.setAccessToken(map.get(InType.AccessToken).toString());
		} 
		if(map.get(InType.SrcSysCode) != null) {
			messageBo.setSrcsyscode(map.get(InType.SrcSysCode).toString());
		} 
		if(map.get(InType.APPKEY) != null) {
			messageBo.setAppKey(map.get(InType.APPKEY).toString());
		}

		//小写的key
		if(map.get(InType.ReqTime.toLowerCase()) != null) {
			messageBo.setSrcReqTime(map.get(InType.ReqTime.toLowerCase()).toString());
		} 
		if(map.get(InType.DstSysID.toLowerCase()) != null) {
			messageBo.setDstSyscode(map.get(InType.DstSysID.toLowerCase()).toString());
		} 
		if(map.get(InType.Format.toLowerCase()) != null) {
			messageBo.setFormat(map.get(InType.Format.toLowerCase()).toString());
		} 
		if(map.get(InType.TRANSID.toLowerCase()) != null) {
			messageBo.setTransactionID(map.get(InType.TRANSID.toLowerCase()).toString());
		} 
		if(map.get(InType.Method.toLowerCase()) != null) {
			messageBo.setApiname(map.get(InType.Method.toLowerCase()).toString());
		} 
		if(map.get(InType.BusiCode.toLowerCase()) != null) {
			messageBo.setApiname(map.get(InType.BusiCode.toLowerCase()).toString());
		} 
		if(map.get(InType.ServiceContractVer.toLowerCase()) != null) {
			messageBo.setContractVer(map.get(InType.ServiceContractVer.toLowerCase()).toString());
		} 
		if(map.get(InType.IP.toLowerCase()) != null) {
			messageBo.setIp(map.get(InType.IP.toLowerCase()).toString());
		} 
		if(map.get(InType.Sign.toLowerCase()) != null) {
			messageBo.setSign(map.get(InType.Sign.toLowerCase()).toString());
		} 
		if(map.get(InType.AccessToken.toLowerCase()) != null) {
			messageBo.setAccessToken(map.get(InType.AccessToken.toLowerCase()).toString());
		} 
		if(map.get(InType.SrcSysCode.toLowerCase()) != null) {
			messageBo.setSrcsyscode(map.get(InType.SrcSysCode.toLowerCase()).toString());
		} 
		if(map.get(InType.APPKEY.toLowerCase()) != null) {
			messageBo.setAppKey(map.get(InType.APPKEY.toLowerCase()).toString());
		}
	}
	
	
	public void response(HttpServletResponse response, MessageBO messageBo) throws IOException {
		
		response.setCharacterEncoding("UTF-8");
		
		if(messageBo.getMsgHead().get(OutType.httpStatusCode) != null 
				&& !StringUtils.isEmpty(messageBo.getMsgHead().get(OutType.httpStatusCode).toString())) {
			response.setStatus(Integer.parseInt(messageBo.getMsgHead().get(OutType.httpStatusCode).toString()));
			messageBo.getMsgHead().remove(OutType.httpStatusCode);
		}
		
		Map<String, Object> msgHead = messageBo.getMsgHead();
		Set<Map.Entry<String, Object>> entry = msgHead.entrySet();
		for(Map.Entry<String, Object> kv : entry) {
			response.setHeader(kv.getKey(), (String)kv.getValue());
		}
		
		response.getWriter().write(messageBo.toString());
		response.getWriter().flush();
		response.getWriter().close();
	}
	

	public void changeMsgBodyType(MessageBO messageBo, String msgBody) {
			
		protocolController.changeMsgBodyType(messageBo, msgBody);
	}

	public void setProtocolController(ProtocolController protocolController) {
		this.protocolController = protocolController;
	}

	public boolean hasAuthCode(HttpServletRequest request, HttpServletResponse response, String method) throws IOException {

		String authCode = request.getHeader(EAAPConstants.AUTH_CODE);
		if(authCode!=null){
			if(LOG.isDebugEnabled()){
				if(method!=null && method.equals("http")){
					synchronized(this){
						StatisticData.httpReceivecount++;
					}
				}else if(method!=null && method.equals("webservice")){
					synchronized(this){
						StatisticData.webserviceReceivecount++;
					}
				}
			}
			Long reqTime = request.getHeader(EAAPConstants.REQ_TIME)==null?null:Long.valueOf(request.getHeader(EAAPConstants.REQ_TIME));
			String sourceCode = request.getHeader(EAAPConstants.SOURCE_CODE);
			if(!isRequestValid(reqTime, sourceCode, authCode)){
				response.getWriter().write("Invalid request!");
				response.getWriter().flush();
				response.getWriter().close();
				return true;
			}
		}
		
		return false;
	}

	public void initAuth(MessageBO messageBo, HttpServletRequest request) {

		Long reqTime = request.getHeader(EAAPConstants.REQ_TIME)==null?null:Long.valueOf(request.getHeader(EAAPConstants.REQ_TIME));
		String sourceCode = request.getHeader(EAAPConstants.SOURCE_CODE);
		String authCode = request.getHeader(EAAPConstants.AUTH_CODE);
		messageBo.setAuthCode(authCode);
		messageBo.setSourceCode(sourceCode);
		messageBo.setReqTime(reqTime);
	}

	public void callService(MessageBO messageBo, HttpServletRequest request,
			HttpServletResponse response, String action) throws IOException {

		if(messageBo.getAuthCode()!=null && !messageBo.getAuthCode().equals("error")){
			Map<String, String> reqHeads = new HashMap<String, String>();
			Enumeration<String> e = request.getHeaderNames();
			 while (e.hasMoreElements()) {
	            String name = (String)e.nextElement();
	            reqHeads.put(name, request.getHeader(name));
		    }
			 reqHeads.put(EAAPConstants.AUTH_CODE, messageBo.getAuthCode());
			 reqHeads.put(EAAPConstants.REQ_TIME, String.valueOf(messageBo.getReqTime()));
			 reqHeads.put(EAAPConstants.SOURCE_CODE, messageBo.getSourceCode());
			 reqHeads.put(EAAPConstants.REAL_IP_ADDRESS, messageBo.getIp());
			 String returnStr = RequestDispatchUtil.sendRequest(response, messageBo, messageBo.getServiceAgentServerAddress()+request.getRequestURI().substring(1).substring(request.getRequestURI().substring(1).indexOf("/"))+ (request.getQueryString()==null?"":("?"+request.getQueryString())), reqHeads, messageBo.getSrcPlayload(), "", action, RequestDispatchUtil.TIME_OUT, "", "", "", "","",null,null);
			 response.getWriter().write(returnStr);
			 
		}else{
			response.getWriter().write("There is error for generate auth-code, please try again!");
		}
		
		response.getWriter().flush();
		response.getWriter().close();
	}
	
	public void putRealIpAddress(MessageBO<?> messageBo, HttpServletRequest request){
		String realIpAddress = null;
		if("client".equals(serviceAgentMode)){
			realIpAddress = getIpAddr(request);
		}else{
			String authCode = request.getHeader(EAAPConstants.AUTH_CODE);
			if(authCode==null){/*access direct*/
				realIpAddress = getIpAddr(request);
			}else{/*access by proxy*/
				realIpAddress = request.getHeader(EAAPConstants.REAL_IP_ADDRESS);
			}
		}
		String[] iList = realIpAddress.split("\\.");
		if(iList.length==4){
			LOG.info("real ip address is " + iList[0] + ".*.*." + iList[3]);
		}else{
			LOG.info("real ip address is " + realIpAddress);
		}
		messageBo.setIp(realIpAddress);
	}
	
	public static String getIpAddr(HttpServletRequest request) {  
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
            ip = request.getHeader("Proxy-Client-IP");  
        }  
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
            ip = request.getHeader("WL-Proxy-Client-IP");  
        }  
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
            ip = request.getHeader("HTTP_CLIENT_IP");  
        }  
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");  
        }  
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
            ip = request.getRemoteAddr();  
        }  
        
        return ip;  
    }

	public static String getInputParameter(HttpServletRequest request)
			throws ServletException, IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream(), "UTF-8"));
		String line = null;
		StringBuffer sb = new StringBuffer();
		while ((line = br.readLine()) != null) {
			sb.append(line).append("\n");
		}
		
		int index = sb.lastIndexOf("\n");
		return sb.substring(0, index<0?0:index);
	}
	
	public static boolean isRequestValid(Long reqTime, String sourceCode, String authCode) {
		String serviceAgentMode = ZKCfgCacheHolder.PROP_ITEMS.get("serviceAgent.mode").toString();
		if("client".equals(serviceAgentMode)){
			return true;
		}
		long nowTime = System.currentTimeMillis();
		if(StringUtils.isEmpty(sourceCode) || StringUtils.isEmpty(authCode)){
			return false;
		}
		long reqValidTime = Long.valueOf(ZKCfgCacheHolder.PROP_ITEMS.get("serviceAgent.anthCode.validTime").toString());
		if(nowTime > reqTime + reqValidTime*1000){
			LOG.debug("Auth code is out of time!");
			return false;
		}
        if(!authCode.equals(strEncrypt(sourceCode, EAAPConstants.ENCODE_TYPE))){
        	LOG.debug("Invalid request!");
        	return false;
        }
		return true;
	}
	
	public static String strEncrypt(String sourceStr, String encodeType){
		try{
			MessageDigest md = MessageDigest.getInstance(encodeType);
	        byte[] b;
			try {
				b = md.digest(sourceStr.getBytes(Constant.UTF8));
			} catch (UnsupportedEncodingException e) {
				LOG.error("UnsupportedEncodingException!", e);
				return null;
			}
	        //Base64编码
	        return DigestUtils.md5Hex(Base64.encodeBase64String(b));
		}catch(NoSuchAlgorithmException e){
			LOG.error("Generate auth-code error!", e);
			return null;
		}
	}
}
