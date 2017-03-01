package com.ailk.eaap.op2.serviceagent.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.UUID;

import javassist.ClassClassPath;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.CtNewMethod;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.ailk.eaap.integration.o2p.transformer.ScriptExecutorTransformer;
import com.ailk.eaap.integration.o2p.transformer.common.IScriptDoMsg;
import com.ailk.eaap.integration.o2p.transformer.common.JavaJdtUtil;
import com.ailk.eaap.o2p.common.cache.CacheFactory;
import com.ailk.eaap.o2p.common.cache.CacheKey;
import com.asiainfo.foundation.spring.SpringContextHolder;
import com.ailk.eaap.op2.bo.TransformerRule;
import com.asiainfo.integretion.o2p.serviceagent.cache.CacheServiceImpl;
import com.asiainfo.integretion.o2p.serviceagent.cache.IMemcacheManageServ;
 
public class JavassistServlet extends HttpServlet implements javax.servlet.Servlet{

	private static final long serialVersionUID = 1L;
	private static final Log LOG = LogFactory.getLog(JavassistServlet.class);
	
	public static void main(String[] args) {
		System.out.println("\n");
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		PrintWriter out = response.getWriter();

		WebApplicationContext context = WebApplicationContextUtils.getWebApplicationContext(super.getServletContext());
		IMemcacheManageServ cacheService =(IMemcacheManageServ)context.getBean("cacheService");
		CacheFactory cacheFactory = (CacheFactory) context.getBean("cacheFactory");
		List<Integer> errorList = (List<Integer>)cacheService.getKey("JAVASSIST_ERROR_LIST");
//		out.println(new Date());
		Object ruleId = request.getParameter("ruleId");
		Object action = request.getParameter("action");

		out.println("ruleId:<input type='text' value='" + (ruleId==null?"":ruleId) + "' id='ruleId' name='ruleId'/>");
		out.println("<input type='button' value='check'  onClick='javascript:javasistValidate(1)'/>");
		
		if(ruleId==null || ruleId.equals("")){
			
		}else{
			if("1".equals(action)){//validate
				TransformerRule transformerRule = (TransformerRule)cacheService.getKey("transformerRule"+ruleId);
				if(transformerRule==null || (16!=transformerRule.getTransformerType() && 17!=transformerRule.getTransformerType())){
					out.println("<script>alert('the rule is not exist or transformer type is not javassist!')</script>");
				}else{
					try{
						if(16==transformerRule.getTransformerType() && transformerRule.getScriptContent()!=null){
							String className = "com.ailk.eaap.integration.o2p.transformer.common.ScriptDoMsg" + UUID.randomUUID();
							ClassPool pool = ClassPool.getDefault();
							pool.insertClassPath(new ClassClassPath(IScriptDoMsg.class));
							pool.importPackage("com.ailk.eaap.integration.o2p.transformer.common.IScriptDoMsg");
							pool.importPackage("com.ailk.eaap.integration.o2p.script.jsr223");
							pool.importPackage("com.ailk.eaap.op2.serviceagent.common");
							pool.importPackage("org.apache.log4j");
							pool.importPackage("com.ailk.eaap.o2p.common.cache.ICache");
							pool.importPackage("com.ailk.eaap.op2.serviceagent.deal.service.IMessageBoHisService");
							String script = transformerRule.getScriptContent().replaceAll("return;", "return null;");
							List<String> list = new ArrayList<String>();
							while(true){
								Map<String, String> map = ScriptExecutorTransformer.getSubStr(script);
								if(map==null){
									break;
								}else{
									script = map.get("script");
									list.add(map.get("method"));
								}
							}
							
							String[] codeLines = script.split("\n");
							StringBuffer sbMethod = new StringBuffer();

							sbMethod.append("public Object doMessage(MessageBO messageBO,com.ailk.eaap.o2p.common.cache.ICache cacheClient ,com.ailk.eaap.op2.serviceagent.deal.service.IMessageBoHisService messageBoHisSer ) {");

							for(String s:codeLines){
								if(null==s||"".equals(s.trim()))
									continue;

								s=s.trim();
								if(s.indexOf("import")>=0){
									String pk = s.substring(s.indexOf("import")+7).replace(";","");
									pk = pk.substring(0, pk.lastIndexOf("."));
									pool.importPackage(pk);
								}
								else sbMethod.append(s+"\n");
							}
							sbMethod.append("\n}");					
							CtClass cc = pool.makeClass(className);
							
							CtClass its[] = {pool.get("com.ailk.eaap.integration.o2p.transformer.common.IScriptDoMsg")};
							cc.setInterfaces(its);
							if(list!=null && !list.isEmpty()){
								for(String md : list){
									CtMethod method = CtNewMethod.make(md,cc);
									cc.addMethod(method);
								}
							}
							CtMethod md = CtNewMethod.make(sbMethod.toString(),cc);
							cc.addMethod(md);
							IScriptDoMsg doMsg = (IScriptDoMsg) cc.toClass().newInstance();
						}else if(17==transformerRule.getTransformerType() && transformerRule.getScriptContent()!=null){
							JavaJdtUtil.parserJava(transformerRule.getScriptContent(), transformerRule.getId());
						}
						out.println("<script>alert('the script is pass compile by javassist which ruleId=" + transformerRule.getId() + "!')</script>");
						out.println("<input type='button' value='reload'  onClick='javascript:javasistValidate(2)'/>");
					}catch(Exception e){
						out.println("<script>alert('ruleId=" + transformerRule.getId() + " compile error! " + e + "')</script>");
					}
				}
			}else if("2".equals(action)){//reload
				ScriptExecutorTransformer st = (ScriptExecutorTransformer) context.getBean("scriptExecutorTransformer");
				TransformerRule transformerRule = (TransformerRule)cacheService.getKey("transformerRule"+ruleId);
				ClassPool pool = ClassPool.getDefault();
				st.reloadJavassist(transformerRule, pool);
				cacheFactory.getLocalCacheClient().remove("JAVASSIST_RELOAD_EXCEPTION" + ruleId);
				out.println("<script>alert('reload success! ruleId=" + transformerRule.getId() + "!')</script>");
			}
		}
		out.println("</br></br>");
		if(errorList!=null && !errorList.isEmpty()){
			out.println("<table border='1'>");
			out.println("<tr><td>contract id</td><td>error infomation</td></tr>");
			for(Integer rid : errorList){
				if(cacheService.getKey("JAVASSIST_RELOAD_EXCEPTION"+rid)!=null)
					out.println("<tr><td>" + rid + "</td><td>" + cacheService.getKey("JAVASSIST_RELOAD_EXCEPTION"+rid) + "</td></tr>");
			}
			out.println("</table>");
		}else{
			out.println("There is no exception occurd for javassist!");
		}
		out.println("<script type='text/javascript'>function javasistValidate(actionType){var ruleId = document.getElementById('ruleId').value;if(ruleId==null || ruleId==''){alert('ruleId can not be null!');return;}window.location.href='JavassistServlet?action=' + actionType + '&ruleId='+ruleId;}</script>");
		response.setContentType("text/html; charset=UTF-8");
//		response.getWriter().write(out);
		response.getWriter().flush();
		response.getWriter().close();
	}
		
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		PrintWriter out = resp.getWriter();

		CacheFactory cacheFactory = SpringContextHolder.getBean("cacheFactory");
    	List<Integer> javassistErrorList = new ArrayList<Integer>();
    	if("1".equals(cacheFactory.getLocalCacheClient().get("JAVASSIST_RELOAD_STATUS"))){
			if(!CacheServiceImpl.reCompliJavaAssitMap.isEmpty()){
				ScriptExecutorTransformer st = SpringContextHolder.getBean("scriptExecutorTransformer");
				Map<String,TransformerRule> repliMap = CacheServiceImpl.reCompliJavaAssitMap;
				String moduleVersion = String.valueOf(cacheFactory.getLocalCacheClient().get(CacheKey.MODULE_TRANSFORMER_RULE));
				String lastVersion = null;
				boolean reloadStatus = true;
				boolean reloadflag = false;
				for(String key : repliMap.keySet()){
					TransformerRule transformerRule = (TransformerRule)repliMap.get(key);
					Integer ruleId = transformerRule.getId();
					try{
						lastVersion = String.valueOf(cacheFactory.getLocalCacheClient().get("JAVASSIST_VERSION" + ruleId));
						if((CacheServiceImpl.TRANSFORMER_TYPE_JAVASSIST== transformerRule.getTransformerType().intValue() || CacheServiceImpl.TRANSFORMER_TYPE_JAVASSISTCLASS== transformerRule.getTransformerType().intValue()) && (lastVersion==null || !moduleVersion.equals(lastVersion))) {
							ClassPool pool = ClassPool.getDefault();
							st.reloadJavassist(transformerRule, pool);
							cacheFactory.getLocalCacheClient().remove("JAVASSIST_RELOAD_EXCEPTION" + ruleId);
							cacheFactory.getLocalCacheClient().put("JAVASSIST_VERSION" + ruleId, lastVersion);
							reloadflag=true;
							out.println("javassist reload success ruleid:"+ruleId+"\n");
							LOG.debug("javassist reload success ruleid:"+ruleId);
						}
					}catch(Exception e){
						reloadStatus = false;
						javassistErrorList.add(ruleId);
						cacheFactory.getLocalCacheClient().put("JAVASSIST_RELOAD_EXCEPTION" + ruleId, e.getCause());
						out.println("ruleId:"+ruleId+ "\n"+ e.toString());
						LOG.error("javassist reload exception ruleid:"+ruleId+" \n"+e.toString());
					}
				}
				if(reloadStatus){
					cacheFactory.getLocalCacheClient().put("JAVASSIST_RELOAD_STATUS", "0");
				}else{
					cacheFactory.getLocalCacheClient().put("JAVASSIST_ERROR_LIST", javassistErrorList);
				}
				if(reloadflag){
					LOG.info("--------reload javassist finish---------");
					out.println("reload javassist finish");
				}
			}
		}
		resp.setContentType("text/html; charset=UTF-8");
		resp.getWriter().flush();
		resp.getWriter().close();
	}
	
	

}
