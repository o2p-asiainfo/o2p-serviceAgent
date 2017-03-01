package com.ailk.eaap.op2.serviceagent.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.ailk.eaap.op2.common.InType;
import com.ailk.eaap.op2.serviceagent.common.MessageBO;
import com.ailk.eaap.op2.serviceagent.deal.service.IDIHService;

public class HttpAPIService extends HttpServlet implements javax.servlet.Servlet{
	
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		MessageBO<String> messageBo  = new MessageBO<String>();
		messageBo.setRestFormString(request.getQueryString());  // 'url/method?a=123&b=1234' get parameters 'a=123&b=1234'
		Map<String, String[]> readOnlyMap =   request.getParameterMap();
		Map<String,String> writeAbleMap = new HashMap<String, String>();
		if(readOnlyMap!=null && !readOnlyMap.isEmpty()){
			Set<Map.Entry<String, String[]>> entry = readOnlyMap.entrySet();
			for (Map.Entry<String, String[]> kv : entry) {
				String key = kv.getKey();
				String[] value = kv.getValue();
				writeAbleMap.put(key, value[0]);
			}
		}
		messageBo.setMessageMap(writeAbleMap);
		messageBo.setMsgBody(URLDecoder.decode(request.getQueryString(),"UTF-8"));
		messageBo.setInType(InType.RESTGET);
		String apiName = null;
		if(messageBo.getMessageMap().get("Method")!=null){
			apiName=messageBo.getMessageMap().get("Method").toString();
		}
		if(messageBo.getMessageMap().get("BusiCode")!=null){
			apiName=messageBo.getMessageMap().get("BusiCode").toString();
		}
		messageBo.setApiname(apiName);
		messageBo.setIp(request.getRemoteAddr());
		String retXml = dataExchange(messageBo);
		response.setContentType("text/xml; charset=UTF-8");
		response.getWriter().write(retXml);
		response.getWriter().flush();
		response.getWriter().close();
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String retXml = "";
		retXml = dataExchange(getInputParameter(request), request.getRemoteAddr());
		response.setContentType("text/xml; charset=UTF-8");
		response.getWriter().write(retXml);
		response.getWriter().flush();
		response.getWriter().close();
	}

	/**
	 * @param request
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	private String getInputParameter(HttpServletRequest request)
			throws ServletException, IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(request
				.getInputStream(), "UTF-8"));
		String line = null;
		StringBuffer sb = new StringBuffer();
		while ((line = br.readLine()) != null) {
			sb.append(line);
		}
		return sb.toString();
	}

	/**
	 * dataExchange
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	private String dataExchange(String message, String ip) {
		WebApplicationContext context = WebApplicationContextUtils
				.getWebApplicationContext(super.getServletContext());
		EAAPService service = (EAAPService) context.getBean("DEPService");
		return service.exchange(message, ip,InType.RESTPOST);
	}

	private String dataExchange(MessageBO<String> messageBo) {
		WebApplicationContext context = WebApplicationContextUtils
				.getWebApplicationContext(super.getServletContext());
		IDIHService dihService = (IDIHService) context.getBean("dihService");
		return dihService.exchange(messageBo);
	}

}
