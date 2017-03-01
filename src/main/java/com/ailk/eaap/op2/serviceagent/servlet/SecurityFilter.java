package com.ailk.eaap.op2.serviceagent.servlet;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

import com.ailk.eaap.o2p.common.spring.config.ZKCfgCacheHolder;

/**
 * Servlet Filter implementation class SecurityFilter
 */
public class SecurityFilter implements Filter {

    /**
     * Default constructor. 
     */
    public SecurityFilter() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
		// TODO Auto-generated method stub
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		Object serviceAgentMode = ZKCfgCacheHolder.PROP_ITEMS.get("serviceAgent.mode");
		if(serviceAgentMode==null || "client".equals(serviceAgentMode)){
			HttpServletResponse servletResponse = (HttpServletResponse) response;
			servletResponse.setStatus(401);
			return;
		}
		
		// pass the request along the filter chain
		chain.doFilter(request, response);
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		// TODO Auto-generated method stub
	}

}
