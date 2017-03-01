package com.ailk.eaap.op2.serviceagent.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.ailk.eaap.op2.common.CommonUtil;
import com.ailk.eaap.op2.serviceagent.common.MessageBO;
import com.asiainfo.foundation.log.Logger;

import net.sf.json.JSONObject;
import net.sf.json.xml.XMLSerializer;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Node;
import org.dom4j.XPath;

/**
 * Created by gongxingfa on 14/11/13.
 * 配置O2P协议适配过程中常用到的一些操作
 */
public final class AdapterTools {
	private static final Logger LOG = Logger.getLog(AdapterTools.class);
	
	private AdapterTools(){}
    /**
     * 对象路径转化为XPath路径
     *
     * @param objPath
     * @return
     */
    private static String objPath2XPath(String objPath) {
        StringBuffer xpath = new StringBuffer("//*");
        String[] pathInfo = objPath.split("\\.");
        for (String p : pathInfo) {
            xpath.append("[local-name()='" + p + "']/*");
        }
        return xpath.toString().substring(0, xpath.toString().lastIndexOf("/*"));
    }

    /**
     * 以对象的方式获取节点
     *
     * @param doc
     * @param objPath
     * @return
     */
    public static List<?> getNodes(Document doc, String objPath) {
        String xPathStr = objPath2XPath(objPath);
        XPath xPath = doc.createXPath(xPathStr);
        return xPath.selectNodes(doc);
    }

    /**
     * 以对象路径的方式获取XML节点的文本值
     *
     * @param doc
     * @param objPath
     * @return
     */
    @SuppressWarnings("rawtypes")
    public static String getXmlNodeText(Document doc, String objPath) {
        List list = getNodes(doc, objPath);
        if (list != null && list.size() > 0) {
            Node node = (Node) list.get(0);
            return node.getText();
        }
        return "";
    }

    /**
     * 以对象路径的方式设置XML节点的值
     *
     * @param doc
     * @param objPath
     * @param text
     */
    @SuppressWarnings("rawtypes")
    public static void setXmlNodeText(Document doc, String objPath, String text) {
        String xPathStr = objPath2XPath(objPath);
        XPath xPath = doc.createXPath(xPathStr);
        List list = xPath.selectNodes(doc);
        if (list != null && list.size() > 0) {
            Node node = (Node) list.get(0);
            node.setText(text);
        }
    }

    /**
     * 以对象路径的方式获取JSON对象的值
     *
     * @param root
     * @param objPath
     * @return
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    public static String getJsonValue(JSONObject root, String objPath) {
        if (!objPath.contains(".")) {
            return root.getString(objPath);
        }
        String[] pathinfo = objPath.split("\\.");
        List pathList = new ArrayList();
        for (String p : pathinfo) {
            pathList.add(p);
        }
        JSONObject tempJson = root;
        String key;
        while (pathList.size() != 1) {
            key = (String) pathList.remove(0);
            tempJson = tempJson.getJSONObject(key);
        }
        key = (String) pathList.remove(0);
        return tempJson.getString(key);
    }

    /**
     * 以对象的方式设置JSON对象的值
     *
     * @param root
     * @param objPath
     * @param value
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    public static void setJsonValue(JSONObject root, String objPath,
                                    Object value) {
        if (!objPath.contains(".")) {
            root.put(objPath, value);
            return;
        }
        String[] pathinfo = objPath.split("\\.");
        List pathList = new ArrayList();
        for (String p : pathinfo) {
            pathList.add(p);
        }
        JSONObject tempJson = root;
        String key;
        while (pathList.size() != 1) {
            key = (String) pathList.remove(0);
            tempJson = tempJson.getJSONObject(key);
        }
        key = (String) pathList.remove(0);
        tempJson.put(key, value);
    }

    /**
     * 在JSON对象里面设置通用Header
     *
     * @param messageBO
     * @param jsonObject
     */
    public static void setCommonHeader(MessageBO<?> messageBO, JSONObject jsonObject) {
        HashMap<String, String> statusMap = new HashMap<String, String>();
        statusMap.put("OK", "ok.");
        statusMap.put("USERNAME_NOT_VALID", "Fix the username and try again.");
        statusMap.put("EMAIL_NOT_VALID", "Fix the email and try again");
        statusMap.put("NOT_ALLOWED_TO_REGISTER_USER", "The user belong to another partner and can not be ported.");
        statusMap.put("USER_ALREADY_REGISTERED", "User already registered.");
        statusMap.put("PARTNERKEY_NOT_VALID", "partnerkey not valid.");
        statusMap.put("UNKNOWN_ERROR", "unknown error.");
        String status = (String) jsonObject.get("responseStatus");
        String statusTxt = (String) statusMap.get(status);
        if (statusTxt == null) {
            statusTxt = "unknown error";
        }
        jsonObject.put("responseStatusTxt", statusTxt);

        String rspType = "0";
        String rspDesc = statusTxt;
        //未知错误的时候需要抛出异常给bpm做重试处理
        if (status != null && status.endsWith("UNKNOWN_ERROR")) {
            rspType = "1";
        }
        JSONObject reqHeader = new JSONObject();
        reqHeader.put("TransactionID", messageBO.getTransactionID());
        reqHeader.put("RspTime", CommonUtil.getDefaultTimeZoneDateString());
        reqHeader.put("RspDesc", rspDesc);
        reqHeader.put("RspType", rspType);
        reqHeader.put("RspCode", "0000");
        jsonObject.put("ResponseHeader", reqHeader);
    }

    /**
     * JSON对象转换为XML Document
     *
     * @param jsonObject
     * @return
     */
    public static String json2Xml(JSONObject jsonObject) {
        XMLSerializer xmlSerializer = new XMLSerializer();
        String xml = xmlSerializer.write(jsonObject);
        Document doc = null;
            try {
				doc = DocumentHelper.parseText(xml);
			} catch (DocumentException e) {
				 LOG.info("exception", e);
			}
        return doc == null ? "" : doc.asXML();
    }
}
