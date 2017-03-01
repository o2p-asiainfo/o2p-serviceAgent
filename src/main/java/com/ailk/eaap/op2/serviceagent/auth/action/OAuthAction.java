package com.ailk.eaap.op2.serviceagent.auth.action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.ailk.eaap.o2p.common.cache.CacheKey;
import com.ailk.eaap.o2p.common.cache.ICacheFactory;
import com.ailk.eaap.op2.bo.Api;
import com.ailk.eaap.op2.bo.App;
import com.asiainfo.integretion.o2p.serviceagent.cache.IMemcacheManageServ;
import com.linkage.rainbow.ui.struts.BaseAction;

public class OAuthAction extends BaseAction{

	private static final long serialVersionUID = 1L;
	private App app;
	private String redirectUrl;
	private transient IMemcacheManageServ cacheService;
	@SuppressWarnings("rawtypes")
	private ICacheFactory cacheFactory;
	private List<Api> apiList;
	
	
	@SuppressWarnings("unchecked")
	public String auth() throws Exception {
		apiList = (List<Api>) cacheFactory.getLocalCacheClient().get(CacheKey.AUTH_API_LIST); //获取授权列表
		HttpServletRequest request = getRequest();
		String clientId = request.getParameter("client_id");
		String code = request.getParameter("code"); //授权码
		String redirectUri = request.getParameter("redirect_uri");
		String display = request.getParameter("display")==null?"page":request.getParameter("display");
		//		
/*		Object object = cacheService.getKey(CacheKey.App+client_id);
		App appTemp = (App) object;
		appTemp.setAppCallbackUrl("http://www.baidu.com");
		appTemp.setAppsecure("1005");
		cacheService.addObj(CacheKey.AuthorizeCode+code, 100, appTemp);*/
		//
		
		//判断授权码是否有效
		Object appObj = cacheFactory.getCacheClient().get(CacheKey.AuthorizeCode+code);
		if (appObj==null || "".equals(appObj)) {
			redirectUrl = redirectUri + "?error="+java.net.URLEncoder.encode(getText("eaap.op2.portal.auth.AuthorizeCode.notexist"),"utf-8")+code;
			return "redirectUrl";
		}
		app = (App) appObj;
		//判断appkey是否正确
		if(!clientId.equals(app.getAppkey())){
			redirectUrl = redirectUri + "?error="+java.net.URLEncoder.encode(getText("eaap.op2.portal.auth.noexist"),"utf-8");//client_secret,client_id is not match   ";
			return "redirectUrl";
		}
		if ("page".equals(display) || "mobile".equals(display) || "touch".equals(display)) {
			return display;
		} else {
			redirectUrl = redirectUri + "?error="+java.net.URLEncoder.encode(getText("eaap.op2.portal.auth.dispalynosupport"),"utf-8");//client_secret,client_id is not match   ";
			return "redirectUrl";			
		}
	}


	public String getRedirectUrl() {
		return redirectUrl;
	}

	public void setRedirectUrl(String redirectUrl) {
		this.redirectUrl = redirectUrl;
	}

	public ICacheFactory<?, ?> getCacheFactory() {
		return cacheFactory;
	}

	public void setCacheFactory(ICacheFactory<?, ?> cacheFactory) {
		this.cacheFactory = cacheFactory;
	}

	public App getApp() {
		return app;
	}

	public void setApp(App app) {
		this.app = app;
	}

	public IMemcacheManageServ getCacheService() {
		return cacheService;
	}

	public void setCacheService(IMemcacheManageServ cacheService) {
		this.cacheService = cacheService;
	}

	public List<Api> getApiList() {
		return apiList;
	}

	public void setApiList(List<Api> apiList) {
		this.apiList = apiList;
	}


	
	
}
