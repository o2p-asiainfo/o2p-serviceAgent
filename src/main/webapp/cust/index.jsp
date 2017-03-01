<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page import="java.util.*" %>
<%@ include file="/common/taglibs.jsp"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title><s:property value="%{getText('eaap.op2.portal.auth.custcenter')}" /></title>
<meta content="text/html; charset=utf-8" http-equiv="Content-Type" />

<link rel="stylesheet" type="text/css" href="${contextPath}/resource/${contextStyleTheme}/css/platform.css" />
<link rel="stylesheet" type="text/css" href="${contextPath}/resource/${contextStyleTheme}/css/manage.css" />
<link rel="stylesheet" type="text/css" href="${contextPath}/resource/${contextStyleTheme}/css/auth.css" />
<script type="text/javascript" src="${contextPath}/resource/comm/js/jquery_min.js"></script>
<script type="text/javascript" src="${contextPath}/resource/comm/js/jquery.js"></script>

<script type="text/javascript">
function checkLoginSubmit( appId){
	if(confirm('<s:property value="%{getText('eaap.op2.portal.auth.custcenter.userConfirm')}" />')){
		
		
        var url = 'cancelAuth.shtml';
        var params = {
                cancelAppId:appId
        };
        jQuery.post(url, params, callbackFun, 'json');
        
        var oDiv = document.getElementById(appId);
        document.getElementById("mainTop").removeChild(oDiv);
        
	}
	
}

function callbackFun(data)
{
               

          
}

</script>
</head>
<body>
  <div id="header" class="headerStyle">
    <div class="logoLogin"><a href="<s:property value="%{getText('eaap.op2.portal.auth.uri')}" />"><img 
src="${contextPath}/resource/${contextStyleTheme}/images/${contextStyleSpecial}/logo${localeName}.png" /></a> </div>
  </div>
  
<!--body start -->
<div class="sectionMain dokuwiki clearfix">
  <div class="leftSide">
    <div class="sideNav">
      <div class="itemSearch"> ${appUserInfo.userInfo.productNbr} </div>
      <dl class="itemNav">
        <dt><s:property value="%{getText('eaap.op2.portal.auth.custcenter.menu')}" />ƒ</dt>
        <dd  class="cur"><a class="sl2" 
  href="${contextPath}/auth/custAction.shtml"><s:property value="%{getText('eaap.op2.portal.auth.custcenter.authlist')}" />ƒ</a></dd>
        <dd><a class="sl2" 
  href=""><s:property value="%{getText('eaap.op2.portal.auth.custcenter.usermsg')}" /></a></dd>
      </dl>
      <div class="markline"></div>
    </div>
  </div>
  <div class="contentWarp" id= "mainTop">
    <h1 class="tilItem"><s:property value="%{getText('eaap.op2.portal.auth.custcenter.authlist')}" /></h1>
    <s:iterator value="%{appUserInfo.appList}" id="appList">
    	<!-- wikipage start --><!-- TOC START -->
    
	    <div class="toc" id="<s:property value="%{#appList.appId}"/>">
	      <div id="toc__header" class="tocheader toctoggle">${appList.appName}</div>
	      <div id="toc__inside">
	        <ul class="toc">
	          <li class="clear">
	            <ul class="toc">
	              <li class="level2">
	                <DIV class=li>
	                <SPAN class=li> <a class="Round60Img" 
	href="http://msoft.taobao.com/app_detail.htm?spm=1.381593.305567.3&amp;appId=29&amp;brandId=0&amp;modelId=0" 
	target="_blank"><span class="png60"></span><img alt="$img.text" 
	src="${contextPath}/resource/comm/images/ico/zfb.jpg" width="60" 
	height="60" /></a>
	                <div>
	                  <p>${appList.appDesc}<br/>
	                    <br/>
	                  </p>
	                </div>
	                <div class="icon-addit">
	                  <p><b><s:property value="%{getText('eaap.op2.portal.auth.custcenter.authscope')}" />š</b> 
	                  
	                  <s:iterator value="#appList.oauthApiList" id="oauthApiList">
	                  	
	                  	 <s:iterator value="#oauthApiList.priDefines" id="priDefineList">
	                  	 
	                  		${priDefineList.priDefineName}
	                  	</s:iterator>
	                  </s:iterator>
	                  
	                   </p>
	                  <p style=" text-align:right; "><s:property value="%{getText('eaap.op2.portal.auth.custcenter.wantcancel')}" />
	                    <input type="button" class="Btn" id="<s:property value="%{#appList.appId}"/>" onclick="checkLoginSubmit(<s:property value="%{#appList.appId}"/>)"  value=<s:property value="%{getText('eaap.op2.portal.auth.cancelbutton')}"/> />
	                 	
	                  </p>
	                </div>
	              </li>
	            </ul>
	          </li>
	        </ul>
	      </div>
	    </div>
	    <!-- TOC END -->
    </s:iterator>
    
    

<!--body end -->

<!--foot start -->
<jsp:include page="/common/footer.jsp" />
<!--foot end -->
</body>
</html>
