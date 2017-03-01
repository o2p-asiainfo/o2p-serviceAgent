<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page import="java.util.*" %>
<%@ include file="/common/taglibs.jsp"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title><s:property value="%{getText('eaap.op2.portal.auth.authorize')}" /></title>
<meta content="text/html; charset=utf-8" http-equiv="Content-Type" />
<link rel="stylesheet" type="text/css" href="${contextPath}/resource/${contextStyleTheme}/css/platform.css" />
<link rel="stylesheet" type="text/css" href="${contextPath}/resource/${contextStyleTheme}/css/manage.css" />
<link rel="stylesheet" type="text/css" href="${contextPath}/resource/${contextStyleTheme}/css/auth.css" />
<script type="text/javascript" src="${contextPath}/resource/comm/js/jquery_min.js"></script>
<script>
function closeBrower(){
	var browserName=navigator.appName; 
	if (browserName=="Netscape"){ 
		window.open('','_parent',''); 
		window.close(); 
	}else if (browserName=="Microsoft Internet Explorer") {
		window.opener = "whocares"; 
		window.close(); 
	}
}
function loginCheck()
{


	return true;
}

</script>
</head>
<body>

  <div id="header" class="headerStyle">
    <div class="logoLogin"><a href="<s:property value="%{getText('eaap.op2.portal.auth.uri')}" />"><img 
src="${contextPath}/resource/${contextStyleTheme}/images/${contextStyleSpecial}/logo.png" /></a> </div>
  </div>

<div class="divide center980"></div>

<!--body start -->
<div class="sectionMain dokuwiki clearfix">
  <div class="contentWarpC">
  <form method="post"   name="authform" id="authform"     action="AuthAction.shtml">
    <div class="toc">
      <div id="toc__inside">
       
        <div class="left">
          <ul>
            &nbsp;<s:property value="%{getText('eaap.op2.portal.auth.authoprompt')}" /> ${app.appName}<s:property value="%{getText('eaap.op2.portal.auth.pridefind')}" />
          </ul>
          <dl>
          	<s:iterator value="%{app.apis}" id="apiList">
          	
          		<s:checkbox key="#apiList.apiName" value="true" fieldValue="%{#apiList.apiMethod}" name="apiMethod" id="%{#apiList.apiId}"></s:checkbox><br/>
          		
          		
          		<s:iterator value="#apiList.priDefines" id="priDefineList">
          			&nbsp;&nbsp;&nbsp;&nbsp;<font style="color: #FF0000;">${priDefineList.priDefineName}</font><br/>
          		</s:iterator>
          		
          	</s:iterator>
          	
            <!-- <dt> <img class="rowicon" src="${contextPath}/resource/${contextStyleTheme}/images/api-default.gif" /> <span class="rowtext"> ddd</span> </dt>
            <dt> <img class="rowicon"  src="${contextPath}/resource/${contextStyleTheme}/images/api-default.gif" /> <span class="rowtext"> ttt</span> </dt>
            <dt> <img class="rowicon"  src="${contextPath}/resource/${contextStyleTheme}/images/api-default.gif" /> <span class="rowtext"> ccc</span> </dt>
          	-->
          </dl>
        </div>
        <div class="midst"> <span><img src="${contextPath}/resource/${contextStyleTheme}/images/arrow-right.gif" /></span> <br />
          <s:property value="%{getText('eaap.op2.portal.auth.authofor')}" /> </div>
        
        <div class="right"> <span class="li"> 
        <a class="Round60Img" href="javascript:null;" target="_blank"> 
        <span class="png60"></span>
        
        
        <img alt="$img.text" src="${contextPath}/resource/comm/images/ico/zfb.jpg" width="60" height="60" />
        
        </a> </span> </div>
        <div class="kong">&nbsp;</div>
        <div class="guanl"><s:property value="%{getText('eaap.op2.portal.auth.youcan')}" /><a href="${contextPath}/oauth2/custAction" target="_blank"><s:property value="%{getText('eaap.op2.portal.auth.usercenter')}" /></a><s:property value="%{getText('eaap.op2.portal.auth.usercancel')}" /> </div>
      </div>
    </div>
    <!-- TOC END -->
    
    <div class="contentWarpB">
      <input type="submit" class="Btn" id="userLogin"  value="<s:property value="%{getText('eaap.op2.portal.auth.authbutton')}" />" />
      <input type="button" class="Btn" id="userLogin" onclick="closeBrower()" value="<s:property value="%{getText('eaap.op2.portal.auth.cancelbutton')}" />" />
    </div>
    </form>
  </div>
</div>

<!--body end --> 

<!--foot start -->
<jsp:include page="/common/footer.jsp"/>
<!--foot end -->
</body>
</html>
