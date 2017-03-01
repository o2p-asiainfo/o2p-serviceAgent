package com.ailk.eaap.op2.serviceagent.servlet;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javassist.ClassPool;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jms.listener.DefaultMessageListenerContainer;
import org.springframework.util.StringUtils;

import com.ailk.eaap.integration.o2p.transformer.ScriptExecutorTransformer;
import com.ailk.eaap.o2p.common.cache.CacheFactory;
import com.ailk.eaap.o2p.common.cache.CacheKey;
import com.ailk.eaap.o2p.common.spring.config.ZKCfgCacheHolder;
import com.ailk.eaap.o2p.common.util.CacheUtil;
import com.ailk.eaap.o2p.common.util.LocalUtils;
import com.ailk.eaap.o2p.common.util.zookeeperUtil.DistributedConsistencyHelper;
import com.ailk.eaap.op2.common.IThreadManagerService;
import com.ailk.eaap.op2.serviceagent.taskflag.ITaskCacheService;
import com.asiainfo.foundation.spring.SpringContextHolder;
import com.ailk.eaap.op2.bo.TransformerRule;
import com.asiainfo.integretion.o2p.serviceagent.cache.CacheServiceImpl;
import com.asiainfo.integretion.o2p.serviceagent.cache.ReloadCache;


public class CacheLoadContextListener  implements ServletContextListener{
	private final static Log LOG = LogFactory.getLog(CacheLoadContextListener.class);
	
	private ReloadCache reloadService;
	
	private transient IThreadManagerService threadManagerService;
	
	private DefaultMessageListenerContainer jmsContainer4FV2;
	
	private DefaultMessageListenerContainer jmsContainer4Fe;
	
	public void contextDestroyed(ServletContextEvent arg0) {
		if(jmsContainer4FV2!=null&&jmsContainer4FV2.isActive()){
			jmsContainer4FV2.stop();
			LOG.info("jmsContainer4FV2 has stoped successfuly!");
		}	
		if(jmsContainer4Fe!=null&&jmsContainer4Fe.isActive()){
			jmsContainer4Fe.stop();
			LOG.info("jmsContainer4Fe has stoped successfuly!");
		}
		
		threadManagerService.stop();
	}

	public void contextInitialized(ServletContextEvent event){
		long beginTime = System.currentTimeMillis();
		String enableFileTaskScan = "N";
		LOG.info("begin to load configure data from center cache---------");
		reloadService = SpringContextHolder.getBean("reloadCache");
		reloadService.reloadCache();
		long endTime = System.currentTimeMillis();
		LOG.info("end to load configure data from center cache,and consume:"+(endTime-beginTime)+"ms");
		String enableTaskScanFromCfg = ZKCfgCacheHolder.PROP_ITEMS.getProperty("FX.enableTaskScan");
		if(StringUtils.hasText(enableTaskScanFromCfg)){
			enableFileTaskScan = enableTaskScanFromCfg;
		}
		if("Y".equalsIgnoreCase(enableFileTaskScan)){
			regServletToZk();
			jmsContainer4FV2 = SpringContextHolder.getBean("jms.FV2.ListenerContainer");
			jmsContainer4Fe = SpringContextHolder.getBean("FE.ListenerContainer");
			jmsContainer4FV2.start();
			LOG.info("jmsContainer4FV2 start success!---------");
			jmsContainer4Fe.start();
			LOG.info("jmsContainer4Fe start success!---------");			
		}
		//启动监听线程
		threadManagerService = (IThreadManagerService)SpringContextHolder.getBean("threadManagerServices");
		threadManagerService.start();
		//启动javassist重载线程
		Thread thread = new Thread(new JavassistReloadThread());
		thread.setDaemon(true);
		thread.start();
	}

	private void regServletToZk() {
        ITaskCacheService  taskCacheService = (ITaskCacheService)SpringContextHolder.getBean("taskCacheService");	
        //注册server到ZK
        String ip = LocalUtils.getLocalRealIp();
        String port = CacheUtil.getPort();
        DistributedConsistencyHelper dh = new DistributedConsistencyHelper();
        String identify =  dh.registerServerToZK(ip, port, DistributedConsistencyHelper.DEFAULT_AGENT_NAME);
        taskCacheService.setServerIdentifyUUID(identify);
	}

	class JavassistReloadThread implements Runnable {
        @SuppressWarnings({ "rawtypes", "unchecked" })
		@Override
        public void run() {
        	while(true){
	        	CacheFactory cacheFactory = SpringContextHolder.getBean("cacheFactory");
	        	List<Integer> javassistErrorList = new ArrayList<Integer>();
	        	if("1".equals(cacheFactory.getLocalCacheClient().get("JAVASSIST_RELOAD_STATUS"))){
	    			if(!CacheServiceImpl.reCompliJavaAssitMap.isEmpty()){
	    				ScriptExecutorTransformer st = SpringContextHolder.getBean("scriptExecutorTransformer");
	    				Map<String,TransformerRule> repliMap = CacheServiceImpl.reCompliJavaAssitMap;
//	    				String moduleVersion = String.valueOf(cacheFactory.getLocalCacheClient().get(CacheKey.MODULE_TRANSFORMER_RULE));
	    				String lastVersion = null;
	    				boolean reloadStatus = true;
	    				boolean reloadflag = false;
	    				for(String key : repliMap.keySet()){
	    					TransformerRule transformerRule = (TransformerRule)repliMap.get(key);
	    					Integer ruleId = transformerRule.getId();
	    					try{
	    						lastVersion = String.valueOf(cacheFactory.getLocalCacheClient().get("JAVASSIST_VERSION" + ruleId));
	    						if((CacheServiceImpl.TRANSFORMER_TYPE_JAVASSIST== transformerRule.getTransformerType().intValue() || CacheServiceImpl.TRANSFORMER_TYPE_JAVASSISTCLASS== transformerRule.getTransformerType().intValue())) {
	    							ClassPool pool = ClassPool.getDefault();
	    							st.reloadJavassist(transformerRule, pool);
	    							cacheFactory.getLocalCacheClient().remove("JAVASSIST_RELOAD_EXCEPTION" + ruleId);
	    							cacheFactory.getLocalCacheClient().put("JAVASSIST_VERSION" + ruleId, lastVersion);
	    							reloadflag=true;
	    							LOG.debug("javassist reload success ruleid:"+ruleId);
	    						}
	    					}catch(Exception e){
	    						reloadStatus = false;
	    						javassistErrorList.add(ruleId);
	    						cacheFactory.getLocalCacheClient().put("JAVASSIST_RELOAD_EXCEPTION" + ruleId, e.getCause());
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
	    				}
	    			}
	    		}
	        	try {
					Thread.sleep(3000);
				} catch (InterruptedException e) {
					LOG.error(e);
				}
        	}
        }
    }
}
