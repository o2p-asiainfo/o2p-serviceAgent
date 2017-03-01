<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page import="java.util.*" %>
<%@ include file="/common/taglibs.jsp"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>能力开放平台</title>
<meta content="text/html; charset=utf-8" http-equiv="Content-Type" />
<link rel="stylesheet" type="text/css" href="../resource/orange/css/platform.css" />
<link rel="stylesheet" type="text/css" href="../resource/orange/css/manage.css" />
<link rel="stylesheet" type="text/css" href="../resource/orange/css/auth.css" />
<script type="text/javascript" src="../resource/comm/js/jquery-1.7.2.min.js"></script>

</head>
<body style="background-color:#F9F9F9" >
<!--header--><!--头部-->
<div class="headLoadLogin" style="background-color:#F99A00">
  <div id="header" class="headerStyle">
    <div class="logoLogin_1" ><a href="http://www.189.cn/"><img 
src="../resource/orange/images/tel/logo-orange.png" /></a> </div>
  </div>
</div>
<!--头部--><!--分割-->

<div class="divide_1 center980"></div>

<!--body start -->
<div class="sectionMain dokuwiki clearfix">
  <div class="content">
    <div class="content_top">
      <div id="toc_inside_left">
	  	<div id="toc_inside_left1">
      		<a href="javascript:void(0)"><span><img src="${contextPath}/resource/comm/images/ico/app.png" width="60" height="60"/></span></a>
		</div>
        <div id="toc_inside_left2">
        	<span class="rowtext">${app.appName }</span>
       	</div>

      </div>

      <div id="toc_inside_middle">
      	<div id="toc_inside_middle1"><span><p style="color:#FE8600;font-size:14px;font-weight:bold">申请授权</p></span></div>
        <div id="toc_inside_middle2"><img src="../resource/orange/images/arrow-right.png"/></div>
      </div>      
      <div id="toc_inside_right">
	  	<div id="toc_inside_right1">
      		<a href="javascript:void(0)"><span><img src="../resource/comm/images/ico/tel.png" width="60" height="60"/></span></a>
		</div>
        <div id="toc_inside_right2">
        	<span class="rowtext">能力开放平台</span>
       	</div>      
      </div>      
    </div>
    <form method="post" name="authform" id="authform" action="access_token">
    <input type="hidden" value="${param.client_id }" name="client_id" />
    <input type="hidden" value="${param.client_secret }" name="client_secret" />
    <input type="hidden" value="${param.grant_type }" name="grant_type" />
    <input type="hidden" value="${param.code }" name="code" />
    <input type="hidden" value="${param.redirect_uri }" name="redirect_uri" />
    <div class="content_middle" style="overflow-y:auto;height:auto!important;max-height:320px">
    	<br/><br/>
        <p style="font-size:14px;font-weight:bold;margin-bottom:10px">请选择你需要授权的能力</p>
        <ul id="ul_api_name_list">
        <c:forEach items="${apiList }" var="api" >
            <li>
            	<input type="checkbox" id="${api.apiMethod }" value="${api.apiMethod }" checked="checked" name="scope" /><label for="${api.apiMethod }">&nbsp;&nbsp;${api.apiName }</label>
            </li>
        	
        </c:forEach>                                                                             
        </ul>
    </div>
    <!-- TOC END -->
	<div class="divide30 center900"></div>    
    <div style="width:400px;margin:0px auto;text-align:center">
      <input type="submit" class="Btn" id="userauth" onclick="" value="授  权" />&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
      <input type="button" class="Btn" id="cancel" onclick="CloseWebPage()" value="取  消" />
    </div>
    </form>
  </div>
</div>

<!--body end --> 

<!--foot start -->
<div  id="bottom">
  <div class="footer_c"  > Copyright © 2012 - 2013 中国电信 All Rights Reserved </div>
</div>
<!--foot end -->
</body>
</html>
<script type="text/javascript">
function CloseWebPage(){
	 if (navigator.userAgent.indexOf("MSIE") > 0) {
	  if (navigator.userAgent.indexOf("MSIE 6.0") > 0) {
	   window.opener = null;
	   window.close();
	  } else {
	   window.open('', '_top');
	   window.top.close();
	  }
	 }
	 else if (navigator.userAgent.indexOf("Firefox") > 0) {
	  window.location.href = 'about:blank ';
	 } else {
	  window.opener = null;
	  window.open('', '_self', '');
	  window.close();
	 }
	}
$(function(){
/* 	$('a[name="a_scope"]').click(function(){
		alert(this.name);
		alert(11);
	}) */
/*  	$.ajax({
		url:"${contextPath}/oauth2/getNeedUserAuthApi",
		dataType:"json",
		type:"get",
		success:function(data){
			var str = '';
			$.each(data,function(i,row){
				str += '<li><label><input type="checkbox" value="'+row.apiMethod+'" checked="checked" name="scope" /><a name="a_scope" >&nbsp;&nbsp;'+row.apiName+'</a></label></li>'
			})
			$('#ul_api_name_list').html(str);
		} */
//	}) */
/* 	$('#userauth').click(function(){
		var scope = '';
		$("[name='scope'][checked]").each(function(){
			scope += $(this).val()+",";
		})
		
		alert(scope);
	}) */
})
</script>


