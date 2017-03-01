package com.ailk.eaap.op2.serviceagent.auth.action;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts2.ServletActionContext;
import com.ailk.eaap.o2p.common.cache.CacheFactory;
import com.ailk.eaap.op2.serviceagent.oauth.IAuthorizeService;
import com.linkage.rainbow.ui.struts.BaseAction;

public class AuthorizeAction extends BaseAction {
	
	private static final long serialVersionUID = 1L;
	
	private String redirectUrl;
	private CacheFactory<?, ?> cacheFactory;
	private IAuthorizeService authorizeService;
	
	public String getRedirectUrl() {
		return redirectUrl;
	}

	public void setRedirectUrl(String redirectUrl) {
		this.redirectUrl = redirectUrl;
	}
	
	public CacheFactory<?, ?> getCacheFactory() {
		return cacheFactory;
	}

	public void setCacheFactory(CacheFactory<?, ?> cacheFactory) {
		this.cacheFactory = cacheFactory;
	}
	
	public IAuthorizeService getAuthorizeService() {
		return authorizeService;
	}

	public void setAuthorizeService(IAuthorizeService authorizeService) {
		this.authorizeService = authorizeService;
	}
	
	public String execute() throws Exception{
		
		HttpServletRequest request = ServletActionContext.getRequest();
		
		String ticket = request.getParameter("ticket");
		String clientId = request.getParameter("client_id");
		String responseType = request.getParameter("response_type");
		String redirectUri = request.getParameter("redirect_uri");
		String state = request.getParameter("state");
     
		if(redirectUri == null){
			redirectUrl = "http://www.baidu.com ?state="+state+ "&error=invalid_redirect_uri "+"&error_description= redirect_uriIsNull ";
			return "redirectUrl";
		}
      
		
		if(clientId == null){
			redirectUrl = redirectUri + "?state=" +state+ "&error=invalid _client" +"&error_description=client _idIsNull" ;
			return "redirectUrl";
		}
		
        if(responseType == null){
        	redirectUrl = redirectUri + "?state=" +state+ "&error=unsupported _response_ type" +"&error_description= response_typeIsNull ";
			return "redirectUrl";
		}
		
		if(ticket == null){
			redirectUrl = redirectUri + "?state=" +state+ "&error=invalid_ticket" + "&error_description= ticketIsNull" ;
			return "redirectUrl";
		}
		
		//通过ticket调用UAM断言接口获取用户信息
		String result = authorizeService.getAssertion(ticket, redirectUri, state);
		
		if(result.contains(redirectUri)){
			redirectUrl = result;
			return "redirectUrl";
		}
		
		//获取授权码
		if("code".equals(responseType)){	
			redirectUrl = authorizeService.authorize(clientId, redirectUri, state, result);
			return "redirectUrl";
		}else{
			redirectUrl = redirectUri + "?state=" +state+"&error=unsupported_response_type" + "&error_description=response_ typeIsError" ;
			return "redirectUrl";
		}
		
	}
}
