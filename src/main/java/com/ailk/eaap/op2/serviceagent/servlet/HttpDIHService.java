package com.ailk.eaap.op2.serviceagent.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.ailk.eaap.op2.common.InType;


public class HttpDIHService extends HttpServlet implements javax.servlet.Servlet{

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub

		resp.setContentType("text/xml; charset=UTF-8");
		resp.getWriter().write("<reponse>not support Http Get mehtod ,please use POST method</reponse>");
		resp.getWriter().flush();
		resp.getWriter().close();
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String retXml="";
		retXml = dataExchange(getInputParameter(request), request.getRemoteAddr());
		response.setContentType("text/xml; charset=UTF-8");
		response.getWriter().write(retXml);
		response.getWriter().flush();
		response.getWriter().close();
	}

	/**
	 * ȡcom.asiainfo.integretion.o2p.serviceagent.cache.IMemcacheManageServcom.asiainfo.integretion.o2p.serviceagent.cache.IMemcacheManageServ�
	 * 
	 * @param request
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	private String getInputParameter(HttpServletRequest request)
			throws ServletException, IOException {
		// ͳһ��UTF-8�ıcom.asiainfo.integretion.o2p.serviceagent.cache.IMemcacheManageServʽ
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
	 * ���ú�̨spring�ṩ��beancom.asiainfo.integretion.o2p.serviceagent.cache.IMemcacheManageServ
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	private String dataExchange(String message, String ip) {
		WebApplicationContext context = WebApplicationContextUtils
				.getWebApplicationContext(super.getServletContext());
		EAAPService service = (EAAPService) context.getBean("DEPService");
		return service.exchange(message, ip,InType.CEPHTTP);
	}

}
