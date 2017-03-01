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

import com.ailk.eaap.op2.serviceagent.route.service.ITimeOutControlServ;

public class HttpNotifyService extends HttpServlet implements javax.servlet.Servlet{

	private static final long serialVersionUID = 1L;

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String retXml = "";
		retXml = dataExchange(getInputParameter(request));
		response.setContentType("text/xml; charset=UTF-8");
		response.getWriter().write(retXml);
		response.getWriter().flush();
		response.getWriter().close();
	}

	
	/**
	 * 
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
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	private String dataExchange(String message) {
		WebApplicationContext context = WebApplicationContextUtils
				.getWebApplicationContext(super.getServletContext());
		ITimeOutControlServ service = (ITimeOutControlServ) context.getBean("timeOutControlServ");
		service.changeTechImplState(message);
		return "SUCCESS";
	}

}
