package com.ailk.eaap.op2.serviceagent.oauth.impl;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.SignatureException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import com.ailk.eaap.o2p.common.cache.CacheFactory;
import com.ailk.eaap.o2p.common.cache.CacheKey;
import com.ailk.eaap.o2p.common.util.Constant;
import com.ailk.eaap.op2.bo.EOPDomain;
import com.ailk.eaap.op2.serviceagent.auth.action.AuthorizeAction;
import com.ailk.eaap.op2.serviceagent.auth.service.ServerOAuth;
import com.ailk.eaap.op2.serviceagent.oauth.IAuthorizeService;
import com.ailk.eaap.op2.bo.App;

public class AuthorizeServiceImpl implements IAuthorizeService, Serializable {

	private static final long serialVersionUID = 1L;
	private String redirectUrl;
	private CacheFactory<?, ?> cacheFactory;
	private static final Logger log = Logger.getLogger(AuthorizeServiceImpl.class);
	private static Properties  properties = new Properties();
	private static String assertionurl;
	private static String serviceCode;
	private static String version;
	private static String actionCode;
	private static String srcSysID;
	private static String dstSysID;
	private static String oauthAppKey;
	
	static{  
		InputStream inputStream = null;
		try {
		inputStream = AuthorizeAction.class.getClassLoader().getResourceAsStream("eaap_serviceAgent.properties");
		log.info("loading eaap_serviceAgent.properties succeed");
		properties.load(inputStream);	 
		assertionurl=properties.getProperty("assertionurl");
		serviceCode=properties.getProperty("serviceCode");
		version=properties.getProperty("version");
		actionCode=properties.getProperty("actionCode");
		srcSysID=properties.getProperty("srcSysID");
		dstSysID=properties.getProperty("dstSysID");
		oauthAppKey=properties.getProperty("oauthAppKey");
		} catch (IOException e) {
			log.error (" loading eaap_serviceAgent. properties failure" , e);
		} finally {
			try {
				if(inputStream != null) {
					inputStream.close();
					inputStream = null;
				}
			} catch(IOException e) {
				log.error (" loading eaap_serviceAgent. properties failure" , e);
			}
		}
	}

    public String getAssertion(String ticket, String redirectUri, String state) throws Exception{
		
		//请求体，修改成道配置文件中读取
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
        String reqTime = df.format(new Date());
        final int offset = 938768;// offset为固定值，避免被猜到种子来源（和密码学中的加salt有点类似）  
		long seed = System.currentTimeMillis() + offset; 
		SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
		sr.setSeed(seed);
		String randomNum= String.valueOf(sr.nextInt());
        String transactionID = "20505"+reqTime + randomNum.substring(randomNum.length()-4);
        
        String soap = "<CAPRoot><SessionHeader>"
                +"<ServiceCode>"+serviceCode+"</ServiceCode>"
                +"<Version>"+version+"</Version>"
                +"<ActionCode>"+actionCode+"</ActionCode>"
                +"<TransactionID>"+transactionID+"</TransactionID>"
                +"<SrcSysID>"+srcSysID+"</SrcSysID>"
                +"<DigitalSign/>"
                +"<DstSysID>"+dstSysID+"</DstSysID>"
                +"<ReqTime>"+reqTime+"</ReqTime>"
                +"<Request><ReqType/><ReqCode/><ReqDesc/></Request></SessionHeader><SessionBody><AssertionQueryReq>"
                +"<Ticket>"+ticket+"</Ticket>"
                +"</AssertionQueryReq></SessionBody></CAPRoot>";
        
        String jksPath = this.getClass().getClassLoader().getResource("").getPath();
        byte[] bytes = sig(soap.getBytes(Constant.UTF8), "gdcsofeshore!@#", "fxOauth", jksPath+"fxOauth.jks");
        String digitalSign = toHexString(bytes);
      
        soap = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:ua=\"http://www.mbossuac.com.cn/ua\"><soapenv:Header/>"
                +"<soapenv:Body><ua:SelectAssertion><!--Optional:--><request><![CDATA[<CAPRoot><SessionHeader>"
                +"<ServiceCode>"+serviceCode+"</ServiceCode>"
                +"<Version>"+version+"</Version>"
                +"<ActionCode>"+actionCode+"</ActionCode>"
                +"<TransactionID>"+transactionID+"</TransactionID>"
                +"<SrcSysID>"+srcSysID+"</SrcSysID>"
                +"<DigitalSign>"+digitalSign+"</DigitalSign>"
                +"<DstSysID>"+dstSysID+"</DstSysID>"
                +"<ReqTime>"+reqTime+"</ReqTime>"
                +"<Request><ReqType/><ReqCode/><ReqDesc/></Request></SessionHeader><SessionBody><AssertionQueryReq>"
                +"<Ticket>"+ticket+"</Ticket>"
                +"</AssertionQueryReq></SessionBody></CAPRoot>]]></request></ua:SelectAssertion></soapenv:Body></soapenv:Envelope>";
		
		URL wsUrl = new URL(assertionurl);
        HttpURLConnection conn = (HttpURLConnection) wsUrl.openConnection();
        
        conn.setDoInput(true);
        conn.setDoOutput(true);
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "text/xml;charset=UTF-8");
        conn.setRequestProperty("appkey", oauthAppKey);
        conn.setRequestProperty("transactionid", transactionID);
        
        OutputStream os = null;
        InputStream is = null;
        StringBuffer s = new StringBuffer("");
        try {
	        os = conn.getOutputStream();
	        
	        os.write(soap.getBytes(Constant.UTF8));
	        
	        is = conn.getInputStream();
	        
	        byte[] b = new byte[1024];
	        int len = 0;
	        while((len = is.read(b)) != -1){
	            String ss = new String(b,0,len,Constant.UTF8);
	            s.append(ss);
	        }
        } catch(IOException e) {
        	throw e;
        } finally {
        	if(is != null) {
        		is.close();
        		is = null;
        	}
        	if(os != null) {
        		os.close();
        		os = null;
        	}
        }
        conn.disconnect();
        
        //断言接口返回结果
        String assertion = s.toString().replace("&lt;", "<" + "");

        //校验返回信息是否正确
		String str = checkAssertion(assertion);
		if (!"true".equals(str)) {
			redirectUrl = redirectUri + "?state=" + state + "&error=assertion_fail" + "&error_description=" + str;
			return redirectUrl;
		}
        
		//通过断言接口返回数据，获取用户信息
		String accountId = getAccountID(assertion);
        
		return accountId;
	}
	
    public String checkAssertion(String assertion){
		
    	String rspCode = null;
    	String rspDesc = null;
    	
    	Pattern pattern1=Pattern.compile("<RspCode>(\\w+)</RspCode>");
	    Matcher matcher1=pattern1.matcher(assertion);
	    while(matcher1.find()){
	    	rspCode=matcher1.group(1);
	    }
	    
	    Pattern pattern2=Pattern.compile("<RspDesc>(\\w+)</RspDesc>");
	    Matcher matcher2=pattern2.matcher(assertion);
	    while(matcher2.find()){
	    	rspDesc=matcher2.group(1);
	    }
	    
	    if("0000".equals(rspCode)){
	    	return "true";
	    }else{
	    	return rspDesc;
	    }
    	
	}
	
	public String getAccountID(String inMsg){
		
		String accountId = null;
		
		//获取accountId
		Pattern pattern=Pattern.compile("<AccountID>(\\w+)</AccountID>");
	    Matcher matcher=pattern.matcher(inMsg);
	    while(matcher.find()){
	    	accountId=matcher.group(1); 
	    }
	    
		return accountId;
	}
	
	public String authorize(String clientId,String redirectUri,String state,String accountId){
		
        App app = null;
        
        //获取app
        Object obj = cacheFactory.getXMemcachedClient().get(CacheKey.App+clientId);

			if(obj!=null){
				
				app = (App)obj;
				app.setAppCallbackUrl(redirectUri);
				
				if(app.getAppOauthType()!=null && app.getAppOauthType().equals(EOPDomain.user_auth)){
					
					//应用授权
					String code = ServerOAuth.genericCode(clientId);
					redirectUrl = redirectUri + "?state="+ state + "&code="+code;  
					app.setOauthCode(code);
					app.setPhoneNum(accountId);
					cacheFactory.getXMemcachedClient().put(CacheKey.AuthorizeCode+code, 100000, app);
					log.debug("createCode"+code);
					
					return redirectUrl;
				}
				
			}else{
				
				//clientId不存在
				redirectUrl = redirectUri +  "?state="+state+"&error=invalid_client"+ "&error_description=client_idIsNull";
				return redirectUrl;
			}
			return redirectUrl;
	}
	
	//签名
	public static byte[] sig(byte[] sigText, String KeyPassword, String KeystoreAlias, String KeyStorePath) {
		char[] kpass;
		int i;
		BufferedInputStream ksbufin = null;
		try {
			KeyStore ks = KeyStore.getInstance("JKS");
			FileInputStream ksfis = new FileInputStream(KeyStorePath);
			ksbufin = new BufferedInputStream(ksfis);
			kpass = new char[KeyPassword.length()];
			for (i = 0; i < KeyPassword.length(); i++){
				kpass[i] = KeyPassword.charAt(i);
			}
			ks.load(ksbufin, kpass);
			PrivateKey priv = (PrivateKey) ks.getKey(KeystoreAlias, kpass);

			Signature rsa = Signature.getInstance("SHA1withDSA");
			rsa.initSign(priv);
			rsa.update(sigText);
			return rsa.sign();
		} catch (KeyStoreException e) {
			log.error(e);
			return null;
		} catch (NoSuchAlgorithmException e) {
			log.error(e);
			return null;
		} catch (CertificateException e) {
			log.error(e);
			return null;
		} catch (IOException e) {
			log.error(e);
			return null;
		} catch (InvalidKeyException e) {
			log.error(e);
			return null;
		} catch (SignatureException e) {
			log.error(e);
			return null;
		} catch (UnrecoverableKeyException e) {
			log.error(e);
			return null;
		}finally{
			if(ksbufin!=null){
				try {
					ksbufin.close();
				} catch (IOException e) {
					log.error("IOException", e);
				}
			}
		}
	}
		
	public static byte[] toHex(byte[] digestByte) {
		byte[] rtChar = new byte[digestByte.length * 2];
		for (int i = 0; i < digestByte.length; i++) {
			byte b1 = (byte) (digestByte[i] >> 4 & 0x0f);
			byte b2 = (byte) (digestByte[i] & 0x0f);
			rtChar[i * 2] = (byte) (b1 < 10 ? b1 + 48 : b1 + 55);
			rtChar[i * 2 + 1] = (byte) (b2 < 10 ? b2 + 48 : b2 + 55);
		}
		return rtChar;
	}

    public static String toHexString(byte[] digestByte) throws UnsupportedEncodingException {
	    return new String(toHex(digestByte), Constant.UTF8);
	}

	public CacheFactory<?, ?> getCacheFactory() {
		return cacheFactory;
	}

	public void setCacheFactory(CacheFactory<?, ?> cacheFactory) {
		this.cacheFactory = cacheFactory;
	}
	
}
