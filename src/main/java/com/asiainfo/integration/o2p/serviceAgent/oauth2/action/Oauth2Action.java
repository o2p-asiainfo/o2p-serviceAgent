package com.asiainfo.integration.o2p.serviceAgent.oauth2.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.struts2.ServletActionContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ailk.eaap.o2p.common.cache.CacheFactory;
import com.ailk.eaap.o2p.common.cache.CacheKey;
import com.ailk.eaap.o2p.common.spring.config.ZKCfgCacheHolder;
import com.ailk.eaap.o2p.common.util.CacheUtil;
import com.ailk.eaap.op2.bo.Tenant;
import com.ailk.eaap.op2.serviceagent.common.EOPDomain;
import com.asiainfo.foundation.log.Logger;
import com.ailk.eaap.op2.bo.Api;
import com.ailk.eaap.op2.bo.App;
import com.ailk.eaap.op2.bo.SerInvokeIns;
import com.asiainfo.integretion.o2p.serviceagent.cache.IMemcacheManageServ;
import com.linkage.rainbow.ui.struts.BaseAction;

@SuppressWarnings("serial")
@Controller
@RequestMapping("/service")
public class Oauth2Action extends BaseAction{

	private static final Logger LOG = Logger.getLog(Oauth2Action.class);
	private static final String TENANT_ONLINE = "A";
	private static final String NEED_USER_AUTHORIZE = "1";
	private static final String TENANT_ID = "tenantId";
	private static final String APPKEY = "appkey";
	private static final String REDIRECT_URI = "redirect_uri";
	private static final String USER_TOKEN = "user_token";
	private static final String USERNAME = "username";
	private String partyAuthUrl;
	private IMemcacheManageServ cacheService;
	private CacheFactory<String, Object> cacheFactory;
	private int expiresTime = 10 * 24 * 60 * 60;//second, default value is 10 days
	
	public String getPartyAuthUrl() {
		return partyAuthUrl;
	}

	public void setPartyAuthUrl(String partyAuthUrl) {
		this.partyAuthUrl = partyAuthUrl;
	}
	
	public int getExpiresTime() {
		return expiresTime;
	}

	public void setExpiresTime(int expiresTime) {
		this.expiresTime = expiresTime;
	}

	public CacheFactory<String, Object> getCacheFactory() {
		return cacheFactory;
	}

	public void setCacheFactory(CacheFactory<String, Object> cacheFactory) {
		this.cacheFactory = cacheFactory;
	}

	public IMemcacheManageServ getCacheService() {
		return cacheService;
	}

	public void setCacheService(IMemcacheManageServ cacheService) {
		this.cacheService = cacheService;
	}
	
	public String exit(){
		//destroy session
		HttpServletRequest request = ServletActionContext.getRequest();
		request.getSession().invalidate();
		return SUCCESS;
	}
	
	/**
	 * @author linwf
	 * @param request
	 * @param type: 0.login check, 1.session check
	 * @return
	 */
	public JSONObject check(HttpServletRequest request, String type){
		JSONObject jo = new JSONObject();
		//check tenant
		String tenantId = request.getParameter("tenantId");
		if(tenantId==null || "".equals(tenantId)){
			jo.put("info", "no tenant info!");
			jo.put("code", "E");//code: N.normal, E.error, F.forward
			return jo;
		}
		Tenant tenant = (Tenant) cacheService.getObjByKey(CacheKey.TENANT + tenantId);
		if(tenant==null){
			jo.put("info", "the tenant[id=" + tenant + "] is not exists!");
			jo.put("code", "E");
			return jo;
		}
		if(tenant.getStatus()!=null && !TENANT_ONLINE.equals(tenant.getStatus())){
			jo.put("info", "the tenant is not online!");
			jo.put("code", "E");
			return jo;
		}
		//check component
		String appkey = request.getParameter("appkey");
		if(appkey==null || "".equals(appkey)){
			jo.put("info", "no appkey info!");
			jo.put("code", "E");
			return jo;
		}
		App app = (App) cacheService.getKey(CacheKey.App + appkey, Integer.valueOf(tenantId));
		if(app==null){
			jo.put("info", "the app is not exists!");
			jo.put("code", "E");
			return jo;
		}
		if(app.getAppState()!=null && !EOPDomain.ONLINE.equals(app.getAppState())){
			jo.put("info", "the app is not online!");
			jo.put("code", "E");
			return jo;
		}
		//check all request info is valid
		if("1".equals(type)){
			Object tenantIdObj = request.getSession().getAttribute("tenantId");
			Object appkeyObj = request.getSession().getAttribute("appkey");
			if(tenantIdObj==null || appkeyObj==null || !tenantIdObj.equals(tenantId) || !appkeyObj.equals(appkey)){
				jo.put("url", "loginPage");
				jo.put("code", "F");
				return jo;
			}
		}
		request.getSession().setAttribute("tenantId", tenantId);
		request.getSession().setAttribute("appkey", appkey);
		return null;
	}

	public void login() throws HttpException, IOException {
		HttpServletResponse response=ServletActionContext.getResponse();
	    response.setContentType("text/html;charset=utf-8");
	    PrintWriter out = response.getWriter();
	    JSONObject result = new JSONObject();
		HttpServletRequest request = ServletActionContext.getRequest();
		JSONObject jo = check(request, "0");
		if(jo!=null){
			out.println(jo.toString());  
		    out.flush();  
		    out.close();
		}
		String redirect_uri = request.getParameter("redirect_uri");
		//check post info
		String username = request.getParameter("username");
		if(username==null || "".equals(username)){
			result.put("url", "loginPage");
			result.put("code", "F");
			out.println(result.toString());  
		    out.flush();  
		    out.close();
			return;
		}
		String password = request.getParameter("password");
		if(password==null || "".equals(password)){
			result.put("url", "loginPage");
			result.put("code", "F");
			out.println(result.toString());  
		    out.flush();  
		    out.close();
			return;
		}
		if(password==null || "".equals(password)){
			result.put("url", "loginPage");
			result.put("code", "F");
			out.println(result.toString());  
		    out.flush();  
		    out.close();
			return;
		}
		String verification = request.getParameter("verification");
		if(verification==null || "".equals(verification)){
			result.put("url", "loginPage");
			result.put("code", "F");
			out.println(result.toString());  
		    out.flush();  
		    out.close();
			return;
		}
		JSONObject hubJO = new JSONObject();
		hubJO.put("userName", username);
		hubJO.put("pwd", password);
		hubJO.put("loginType", "1");
		hubJO.put("credentialType", "password");
		hubJO.put("vertifyCode", verification);
		//call authenticate service, if pass then continue, else break
		if(partyAuthUrl==null){
			partyAuthUrl = ZKCfgCacheHolder.PROP_ITEMS.getProperty("serviceAgent.oauth2.partyAuth.url");
		}
		String tenantId = request.getSession().getAttribute("tenantId").toString();
		String appkey = request.getSession().getAttribute("appkey").toString();
		String returnStr = callRemoteService(partyAuthUrl + URLEncoder.encode(hubJO.toString(), "UTF-8") + "&tenant=" + tenantId);
		if(returnStr==null){
			result.put("info", "login failed!");
			result.put("url", "loginPage");
			result.put("code", "F");
			out.println(result.toString());  
		    out.flush();  
		    out.close();
			return;
		}
		JSONObject resultObj = JSONObject.fromObject(returnStr);
		if(!"1".equals(resultObj.getString("hub_code"))){
			//show failed info
			result.put("url", "loginPage");
			result.put("info", resultObj.getString("hub_value"));
			result.put("code", "F");
			out.println(result.toString());  
		    out.flush();  
		    out.close();
			return;
		}
		request.getSession().setAttribute(USER_TOKEN, resultObj.getString("data"));
		request.getSession().setAttribute(TENANT_ID, tenantId);
		request.getSession().setAttribute(APPKEY, appkey);
		request.getSession().setAttribute(REDIRECT_URI, redirect_uri);
		request.getSession().setAttribute(USERNAME, username);
		request.getSession().setMaxInactiveInterval(30 * 60);//set session expired time(second)
		result.put("url", "manage");
		result.put("code", "N");
		out.println(result.toString());  
	    out.flush();  
	    out.close();
	}
	
	public String loginPage(){
		return "loginPage";
	}
	
	public String manage() {
		//check session
		HttpServletRequest request = ServletActionContext.getRequest();
		Object obj = request.getSession().getAttribute(USER_TOKEN);
		if(obj==null){
			return "loginPage";
		}
		return "manage";
	}

	public String authenticate() {
		
		return "manage";
	}
	
	public void authorize() throws IOException {
		HttpServletRequest request = ServletActionContext.getRequest();
		//session check
		this.check(request, "1");
		 HttpServletResponse response=ServletActionContext.getResponse();
	    response.setContentType("text/html;charset=utf-8");
	    PrintWriter out = response.getWriter();
		String idsStr = request.getParameter("ids");
		if(idsStr==null || "".equals(idsStr)){
			out.println("no data for deal!");  
		    out.flush();  
		    out.close();
		    return;
		}
		String action = request.getParameter("action");
		String username = request.getSession().getAttribute(USERNAME).toString();
		if(action==null || "".equals(action)){
			out.println("no action type data in request!");  
		    out.flush();  
		    out.close();
		    return;
		}
		String[] list = idsStr.split(",");
		String tenantId = request.getSession().getAttribute("tenantId").toString();
		for(String id : list){
			SerInvokeIns sii = (SerInvokeIns) cacheService.getKey(CacheKey.serInvokeIns + id, Integer.valueOf(tenantId));
			if(sii==null){
				LOG.warn("There is no this object in cache for id="+id);
				continue;
			}
			if("0".equals(action)){//action:0.Authorize, 1.Cancel
				Integer validTime = request.getParameter("validTime")==null?expiresTime:Integer.valueOf(request.getParameter("validTime"));
				cacheFactory.getCacheClient().put(CacheUtil.getTokenKey(sii, tenantId, username), validTime, request.getSession().getAttribute(USER_TOKEN));
			}else if("1".equals(action)){
				cacheFactory.getCacheClient().remove(CacheUtil.getTokenKey(sii, tenantId, username));
			}
		}
		//callback to synchronize token to 3rd system
		this.callRemoteService(request.getSession().getAttribute("redirect_uri").toString());
		
		out.println("success!");  
	    out.flush();  
	    out.close();
	}
	
	private String callRemoteService(String url){
		try{
			HttpClient httpClient = new HttpClient();
			HttpMethod httpMethod = new PostMethod(url);
			int status = httpClient.executeMethod(httpMethod);
			if(200==status){
				return httpMethod.getResponseBodyAsString();
			}
		}catch(Exception e){
			LOG.error("call remote service error! url=" + url, e);
		}
		return null;
	}
	
	public void write() throws IOException{
	    //check session
		HttpServletRequest request = ServletActionContext.getRequest();
		Object obj = request.getSession().getAttribute(USER_TOKEN);
		if(obj==null){
			return;
		}
	    HttpServletResponse response=ServletActionContext.getResponse();
	    response.setContentType("text/html;charset=utf-8");
	    PrintWriter out = response.getWriter();
		JSONObject jo = this.check(request, "1");
		if(jo!=null){
			out.println("invalid request!");  
		    out.flush();  
		    out.close();
		    return;
		}
		String tenantId = request.getParameter("tenantId");
		String appkey = request.getParameter("appkey");
		App app = (App) cacheService.getKey(CacheKey.App + appkey, Integer.valueOf(tenantId));
		if(app==null){
			LOG.warn("the app is not exists!");
			return;
		}
		if(app.getAppState()!=null && !EOPDomain.ONLINE.equals(app.getAppState())){
			LOG.warn("the app is not online!");
			return;
		}
	    String componentCode = app.getComponentCode();
		//get all service invoke instances which need user authorize under the component
		@SuppressWarnings("unchecked")
		List<String> keyList = (List<String>) cacheService.getKey("KEY_LIST_SER_INVOKE_INS", Integer.valueOf(tenantId));
		List<JSONObject> siis = new ArrayList<JSONObject>();
		JSONObject result = new JSONObject();
		result.put("result", "'code': 1,"+
        "'message': function() {"+
          "if (this.code != 1) {"+
           " return 'not health, no data'} }");
		String type = request.getParameter("type");
		String username = request.getSession().getAttribute(USERNAME).toString();
		if(keyList!=null && !keyList.isEmpty()){
			List<Integer> idList = new ArrayList<Integer>();
			for(String key : keyList){
				if(key.startsWith(tenantId + CacheKey.ProofValues)){
					continue;
				}
				SerInvokeIns sii = (SerInvokeIns) cacheService.getObjByKey(key);
				if(componentCode.equals(sii.getComponentCode())){
					Api api = (Api) cacheService.getKey(CacheKey.Api + sii.getServiceId(), Integer.valueOf(tenantId));
					if(api!=null && api.getIsNeedUserAuth()!=null && NEED_USER_AUTHORIZE.equals(api.getIsNeedUserAuth())){
						if(type!=null){
							if(!queryAuthorizeStatus(type, sii, tenantId, username)){
								continue;
							}
						}
						JSONObject sjo = new JSONObject();
						Integer id = sii.getSerInvokeInsId();
						sjo.put("SER_INVOKE_INS_ID", id);
						sjo.put("SERVICE_CODE", sii.getService().getServiceCode());
						sjo.put("AUTHORIZE_STATUS", cacheFactory.getCacheClient().get(CacheUtil.getTokenKey(sii, tenantId, username))==null?"0":"1");
						if(!idList.contains(id)){
							siis.add(sjo);
							idList.add(id);
						}
					}
				}
			}
		}
		result.put("recordsTotal", siis.size());
		result.put("recordsFiltered", siis.size());
		result.put("data", JSONArray.fromObject(siis));
	    out.println(result.toString());  
	    out.flush();  
	    out.close();  
	}
	
	private boolean queryAuthorizeStatus(String type, SerInvokeIns sii, String tenantId, String username){
		Object obj = cacheFactory.getCacheClient().get(CacheUtil.getTokenKey(sii, tenantId, username));
		if(("1".equals(type) && obj!=null) || ("0".equals(type) && obj==null)){
			return true;
		}else{
			return false;
		}
	}
}
