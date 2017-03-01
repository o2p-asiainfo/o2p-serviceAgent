package com.ailk.eaap.op2.serviceagent.auth.action;



import java.io.UnsupportedEncodingException;

import com.ailk.eaap.o2p.common.cache.ICacheFactory;
import com.ailk.eaap.op2.bo.App;
import com.asiainfo.integretion.o2p.serviceagent.cache.IMemcacheManageServ;
import com.linkage.rainbow.ui.struts.BaseAction;

/**
 * 授权
 * @author Administrator
 * @since 2015/5/4
 */

public class AccessTokenAction extends BaseAction{

	private static final long serialVersionUID = 1L;
	private String redirectUrl;
	private App app;
	private transient IMemcacheManageServ cacheService;
	private ICacheFactory<String, Object> cacheFactory;

	public String execute() throws UnsupportedEncodingException{
//		HttpServletRequest request = getRequest();
//		String client_id = request.getParameter("client_id");
//		String client_secret = request.getParameter("client_secret");
//		String[] scope = request.getParameterValues("scope"); //权限范围
//		String code = request.getParameter("code"); //授权码
//		String redirect_uri = request.getParameter("redirect_uri");
//				
//		//判断授权码是否有效
//		Object appObj = cacheFactory.getCacheClient().get(CacheKey.AuthorizeCode+code);
//		if (appObj==null || "".equals(appObj)) {
//			redirectUrl = redirect_uri + "?error="+java.net.URLEncoder.encode(getText("eaap.op2.portal.auth.AuthorizeCode.notexist"),"utf-8")+code;
//			return "redirectUrl";
//		}
//		
//		app = (App) appObj;
//		
//		//判断appkey是否正确
//		if(!client_id.equals(app.getAppkey())){
//			redirectUrl = redirect_uri + "?error="+java.net.URLEncoder.encode(getText("eaap.op2.portal.auth.noexist"),"utf-8");//client_secret,client_id is not match   ";
//			return "redirectUrl";
//		}else if(client_secret==null || !client_secret.equals(app.getAppsecure())){
//			redirectUrl = redirect_uri + "?error="+java.net.URLEncoder.encode(getText("eaap.op2.portal.auth.match.error"),"utf-8");//client_secret,client_id is not match   ";
//			return "redirectUrl";
//		} else {
//			AppAccToken aat = new AppAccToken();
//			String accessToken = ServerOAuth.generalAccessToken(client_id, app.getPhoneNum());
//			String refreshToken = ServerOAuth.generalAccessToken(client_id,"refresh_token"+app.getPhoneNum());
//			aat.setAccToken(accessToken);
//			aat.setRefToken(refreshToken);
//			aat.setAppId(app.getAppId());
//			aat.setApp(app);
//			if (app.getAppOauthType()!=null && app.getAppOauthType().equals(EOPDomain.user_auth)) {
//				UserInfo userInfo = new UserInfo();
//				userInfo.setProductNbr(app.getPhoneNum()); //app.getPhoneNum()
//				String[] apiMethods = scope;
//				List<Api> apiList = new ArrayList<Api>();
//				StringBuffer oauthApiIds = new StringBuffer("");
//				for (int i = 0; i < apiMethods.length; i++) {
//					Api api = (Api) cacheService.getKey( CacheKey.Api+apiMethods[i]);
//					apiList.add(api);
//					oauthApiIds.append(api.getApiId());
//					if(i!=apiMethods.length-1){
//						oauthApiIds.append(",");
//					}
//				}
//				aat.setApis(apiList);
//				aat.setOauthApiList(oauthApiIds.toString());
//				aat.setUserInfo(userInfo);
//				app.setOauthApiList(apiList);
//				
//
//			}
//			cacheFactory.getCacheClient().remove(CacheKey.AuthorizeCode+code);
//			cacheService.addObj(CacheKey.token+accessToken, app.getTokenEnableTime(), aat);
//			redirectUrl = redirect_uri + "?access_token="+accessToken+"&"+"expires_in="+app.getTokenEnableTime();
			return "redirectUrl";
//		}
	}
	
	public String getRedirectUrl() {
		return redirectUrl;
	}

	public void setRedirectUrl(String redirectUrl) {
		this.redirectUrl = redirectUrl;
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

	public ICacheFactory<String, Object> getCacheFactory() {
		return cacheFactory;
	}

	public void setCacheFactory(ICacheFactory<String, Object> cacheFactory) {
		this.cacheFactory = cacheFactory;
	}


	

}