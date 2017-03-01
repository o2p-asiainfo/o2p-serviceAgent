package com.ailk.eaap.op2.serviceagent.servlet;

import java.util.ArrayList;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;

import com.asiainfo.foundation.log.Logger;

public final class ServletHelper {

	public static List<String> headers = new ArrayList<String>();
	public static final Logger LOG = Logger.getLog(ServletHelper.class);

	
	 public enum State {
		  ON, OFF
	 };

	 private ServletHelper(){}
	 
	

	/**
	 * 
	 * getMsgDom:(这里用一句话描述这个方法的作用).  
	 * TODO(这里描述这个方法适用条件 – 可选). 
	 * TODO(这里描述这个方法的执行流程 – 可选). 
	 * TODO(这里描述这个方法的使用方法 – 可选). 
	 * TODO(这里描述这个方法的注意事项 – 可选). 
	 * 
	 * @author wuwz 
	 * @param msgBody
	 * @return 
	 * @since JDK 1.6
	 */
	public static Document getMsgDom(Object msgBody) {

		Document rspMsg = null;
		if(msgBody instanceof Document) {
			rspMsg = (Document) msgBody;
		} else if(msgBody instanceof String){
			rspMsg = getDocument((String)msgBody);
		}
		
		return rspMsg;
	}

	public static Document getDocument(String reqXml)  {
		Document xmldoc = null;
			
		try {
			xmldoc = DocumentHelper.parseText(reqXml);
		} catch (DocumentException e) {
			LOG.error("xml parse error", e);
		}
		
		return xmldoc;
	}
}
