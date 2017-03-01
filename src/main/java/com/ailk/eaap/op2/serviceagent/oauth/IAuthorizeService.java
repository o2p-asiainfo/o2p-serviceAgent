package com.ailk.eaap.op2.serviceagent.oauth;

public interface IAuthorizeService {

	String getAssertion(String ticket, String redirectUri, String state)  throws Exception;
	
	String authorize(String clientId,String redirectUri,String state,String accountId);

}
