package com.ailk.eaap.op2.serviceagent.servlet;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.xml.ws.WebServiceContext;

@WebService(targetNamespace="http://www.chinatelecom.hub.com")
public interface IDEPService {
	WebServiceContext context = new   org.apache.cxf.jaxws.context.WebServiceContextImpl(); 
	
	@WebMethod
	String exchange(@WebParam(name="in0",  targetNamespace = "http://www.chinatelecom.hub.com")String message);
	@WebMethod
	String apiexchange(@WebParam(name="in0",  targetNamespace = "http://www.chinatelecom.hub.com")String message);
}