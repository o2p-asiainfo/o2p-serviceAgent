<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page import="java.util.*" %>
<%@ include file="/common/taglibs.jsp"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
 <title><s:property value="%{getText('eaap.op2.portal.auth.login')}" /></title>
<meta content="text/html; charset=utf-8" http-equiv="Content-Type" />

<link rel="icon" type="image/x-icon" href="${contextPath}/resource/${contextStyleTheme}/images/${contextStyleSpecial}/favicon.ico" mce_href="${contextPath}/resource/${contextStyleTheme}/images/${contextStyleSpecial}/favicon.ico" />
<link rel="shortcut icon" type="image/x-icon" href="${contextPath}/resource/${contextStyleTheme}/images/${contextStyleSpecial}/favicon.ico" mce_href="${contextPath}/resource/${contextStyleTheme}/images/${contextStyleSpecial}/favicon.ico" />
<link rel="stylesheet" type="text/css" href="${contextPath}/resource/${contextStyleTheme}/css/login.css" />
<link rel="stylesheet" type="text/css" href="${contextPath}/resource/${contextStyleTheme}/css/platform.css" />
<script type="text/javascript" src="${contextPath}/resource/comm/js/jquery.js"></script>
<script type="text/javascript" src="${contextPath}/resource/${contextStyleTheme}/js/auth.js"></script>
<script>
function refreshimg()
	{  
		document.getElementById("authImg").src='${contextPath}/authImg?now=' + new Date();
	};
function loginCheck()
{
  
 if(document.getElementById("username").value=="")
 {
 document.getElementById("username").focus();
 return false;
 }else if(document.getElementById("password").value=="")
  {
  document.getElementById("password").focus();
 return false;
  }else if(document.getElementById("vercode").value==""){
    document.getElementById("vercode").focus();
 return false;
 }
return true ;
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
<div class="login_allcent"><br/>
  <p><img src="${contextPath}/resource/${contextStyleTheme}/images/${contextStyleSpecial}/loginBanner.gif" /></p>
  <div class="passAcc"> 
    <div class="shadow"></div>
    <div class="passAccInfo">
      <div class="passAccInfoTitle"><s:property value="%{getText('eaap.op2.portal.auth.login')}" /></div>
      <div class="passAccForm">
         <form method="post"   name="loginform" id="loginform"  onsubmit="return loginCheck()"   action="login.shtml">
          <div class="login_inputcont">
            <div class="login_left_contname"><s:property value="%{getText('eaap.op2.portal.auth.account')}"/></div>
            <div class="login_left_continput">
              <input type="text" size="26" class="login_value" onkeyup="value=value.replace(/[^\w\.\/\/]/ig,'')"  value="<s:property value="%{getText('eaap.op2.portal.auth.login.msg')}" />" id="username" name="user.phoneNumbetr" autocomplete="off" />
            </div>
          </div>
          <div class="login_inputcont">
            <div class="login_left_contname"><s:property value="%{getText('eaap.op2.portal.auth.accountpassword')}" /></div>
            <div class="login_left_continput">
              <input type="password" size="26" class="login_value" onkeyup="value=value.replace(/[^\w\.\/]/ig,'')"  id="password" name="user.password" />
            </div>
          </div>
          <div class="login_inputcont">
            <div class="login_left_contname"><s:property value="%{getText('eaap.op2.portal.auth.regVerifiactionCode')}"/></div>
            <div class="login_left_continput yzm">
              
              <input type="text" id="vercode"  onkeyup="value=value.replace(/[^\w\.\/]/ig,'')"   name="vercode" />
				 <a href="#" onClick="refreshimg()"><img src="${contextPath}/authImg" id="authImg" width="100" height="24"/></a>
                 
              </div>
         
            <font color="red"><s:actionerror/></font>
          <div class="login_allcent_contbutton">
            <input type="submit" class="loginBtn" id="userLogin"  value=<s:property value="%{getText('eaap.op2.portal.auth.toLogin')}"/> tabindex="6"/>
           
            <!-- <div class="loginAth"><a href="../reg/index.jsp" class="fontC36cS12"><s:property value="%{getText('eaap.op2.portal.index.reg')}"/></a>|<a href="javascript:void(0)" id="resetPwd" onclick="resetPwd();"  target="_blank" class="fontC36cS12"><s:property value="%{getText('eaap.op2.portal.login.forgotPassword')}"/></a></div>-->
          </div>
         </form>
      </div>

    </div>
  </div>

</div>
</div>
<br/>
<jsp:include page="/common/footer.jsp"/>
</body>
</html>
<script language="JavaScript" type="text/javascript">
			function addListener(element,e,fn){    
    	 		if(element.addEventListener){    
          			element.addEventListener(e,fn,false);    
     			 } else {    
         	 		element.attachEvent("on" + e,fn);    
     	 		 }    
			}
			var myinput = document.getElementById("username");
			addListener(myinput,"click",function(){
			    if("<s:property value="%{getText('eaap.op2.portal.auth.login.msg')}"/>"==myinput.value){
			      myinput.value = "";
			    }
			})
			addListener(myinput,"blur",function(){
			    if(""==myinput.value){
			       myinput.value = "<s:property value="%{getText('eaap.op2.portal.auth.login.msg')}" />";
			    }
				
			})

</script>
