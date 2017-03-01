package com.ailk.eaap.op2.serviceagent.servlet;

import java.io.IOException;

import org.restlet.data.Form;
import org.restlet.representation.Representation;
import org.restlet.resource.ServerResource;

import com.ailk.eaap.o2p.common.cache.CacheKey;
import com.ailk.eaap.op2.common.EAAPException;
import com.ailk.eaap.op2.common.EAAPTags;
import com.ailk.eaap.op2.common.InType;
import com.ailk.eaap.op2.serviceagent.common.EOPDomain;
import com.ailk.eaap.op2.serviceagent.common.ErrorDomain;
import com.ailk.eaap.op2.serviceagent.common.MessageBO;
import com.ailk.eaap.op2.serviceagent.deal.service.IDIHService;
import com.ailk.eaap.op2.serviceagent.log.MessageLogService;
import com.ailk.eaap.op2.serviceagent.route.service.IRouteServ;
import com.ailk.eaap.op2.serviceagent.validate.service.IFlowControlService;
import com.ailk.eaap.op2.bo.Api;
import com.ailk.eaap.op2.bo.Component;
import com.ailk.eaap.op2.bo.SerInvokeIns;
import com.asiainfo.integretion.o2p.serviceagent.cache.IMemcacheManageServ;

public class WebServiceResource extends ServerResource{

	private IDIHService dihService;
	private IMemcacheManageServ memcacheManageServ;
	private IRouteServ routeServ;
	private IFlowControlService flowControlServ;
	private MessageLogService messageLogService;

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	protected Representation post(Representation entity)
			 {
		MessageBO messageBo = new MessageBO();
		messageBo.setInType(InType.GeneralWebService);
		try{
			String reqXml = entity.getText();
			String ip = getRequest().getClientInfo().getAddress();
			messageBo.setMsgBody(reqXml);
			messageBo.setIp(ip);
			messageBo.setReqorrsp(EOPDomain.REQ_FLAG);
			String str = (String)getRequest().getAttributes().get("apiName");
			
			Form form = getRequest().getResourceRef().getQueryAsForm();
			Object srcObj  = form.getValuesMap().get(InType.SrcSysCode);
			Api api = null;
			Component srcComponent = null;
			Object apiobj = memcacheManageServ.getKey(CacheKey.Api+str, messageBo.getTenant().getTenantId());
			if(apiobj==null){
				throw new EAAPException( EAAPTags.SEG_DRAVER_SIGN,ErrorDomain.ERROR_CODE_9050,ErrorDomain.not_find_webservice);
			}else{
				api = (Api)apiobj;
			}
			
			srcComponent = validationSrcObj(messageBo, srcObj, srcComponent);
			
			if(srcComponent!=null){
				String key=CacheKey.serInvokeIns+api.getServiceId()+""+srcComponent.getCode();
				Object si = memcacheManageServ.getKey(key, messageBo.getTenant().getTenantId());
				if(si==null){
					throw new EAAPException( EAAPTags.SEG_DRAVER_SIGN,ErrorDomain.ERROR_CODE_9050,ErrorDomain.service_call_instance_not_exist);
				}
				SerInvokeIns serInvokeIns = (SerInvokeIns)si;
				messageBo.setSerInvokeIns(serInvokeIns);
				messageBo.setMsgHead();
			}
			routeServ.route(messageBo);
			flowControlServ.subtraThreadNum(messageBo.getSerInvokeIns().getSerInvokeInsId()+"");
		}catch(EAAPException e){
			messageBo.setEAAPException(e);
		}catch(IOException e){
			messageBo.setEAAPException(new EAAPException( EAAPTags.SEG_DRAVER_SIGN,ErrorDomain.ERROR_CODE_9999,ErrorDomain.system_error,e));
		}finally{
			messageLogService.sendMessageLog(messageBo);
		}
		
		return entity;
	}

	private Component validationSrcObj(MessageBO<String> messageBo, Object srcObj,
			Component srcComponent) {
		if(srcObj==null){
			throw new EAAPException( EAAPTags.SEG_DRAVER_SIGN,ErrorDomain.ERROR_CODE_9050,ErrorDomain.SrcSysID_lost);
		}else{
			Object srcComobj = memcacheManageServ.getKey(CacheKey.Component+srcObj, messageBo.getTenant().getTenantId());
			if(srcComobj==null){
				throw new EAAPException( EAAPTags.SEG_DRAVER_SIGN,ErrorDomain.ERROR_CODE_9050,ErrorDomain.component_not_register);
			}else{
				srcComponent = (Component)srcComobj;
				messageBo.setSrcComponent(srcComponent);
			}
		}
		return srcComponent;
	}

	public IDIHService getDihService() {
		return dihService;
	}

	public void setDihService(IDIHService dihService) {
		this.dihService = dihService;
	}

	public IMemcacheManageServ getMemcacheManageServ() {
		return memcacheManageServ;
	}

	public void setMemcacheManageServ(IMemcacheManageServ memcacheManageServ) {
		this.memcacheManageServ = memcacheManageServ;
	}

	public void setRouteServ(IRouteServ routeServ) {
		this.routeServ = routeServ;
	}

	public void setFlowControlServ(IFlowControlService flowControlServ) {
		this.flowControlServ = flowControlServ;
	}

	public void setMessageLogService(MessageLogService messageLogService) {
		this.messageLogService = messageLogService;
	}
}
