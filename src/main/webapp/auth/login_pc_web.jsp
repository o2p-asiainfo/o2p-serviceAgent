<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page import="java.util.*" %>
<%@ include file="/common/taglibs.jsp"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>能力开放平台</title>
<meta content="text/html; charset=utf-8" http-equiv="Content-Type" />
<meta content="width=device-width, initial-scale=1.0,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no" name="viewport" />
<meta content="black" name="apple-mobile-web-app-status-bar-style" />
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

<div align="center">
<iframe name="content" height="420" width="480" frameborder="0" scrolling="no" src="http://113.108.186.145:9001/portal/common/commonPage.jsp?sysType=20510"></iframe>
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


