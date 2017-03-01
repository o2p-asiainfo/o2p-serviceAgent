<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html>
<html lang="en" class="no-js">
<head>
<meta charset="utf-8">
<meta content="width=device-width, initial-scale=1.0,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no" name="viewport" />
<meta content="black" name="apple-mobile-web-app-status-bar-style" />
<link rel="stylesheet" type="text/css" href="../resource/orange/css/auth_phone.css" />
<title>能力开放平台</title>
</head>

<body style="background-color:#F9F9F9" >
<div id="header" class="headerStyle">
  <div>
  <div style="height:36px">
    <div class="top_left">
      <a href="javascript:window.opener=null;window.open('','_self');window.close();"><img src="../resource/orange/images/un/turnback.jpg" /></a> 
    </div>
    <div class="top_center">
      <span id="appName">${app.appName}</span>
    </div >
    <div class="top_right"> 
    </div>
  </div>
  
  <div class="logo" style="text-align:center; background-color:#F0EFEE">
    <br/>
    <img src="../resource/orange/images/un/logo_dianxin.jpg" width="80" height="80px"/>
    <h3 id="systemName" align="center">广东能力开放平台</h3>
  </div>
  
  <div class="wrap" style="overflow-y:auto;height:auto!important;max-height:200px">
    <form method="post" name="authform" id="authform" action="access_token">
    <input type="hidden" value="${param.client_id }" name="client_id" />
    <input type="hidden" value="${param.client_secret }" name="client_secret" />
    <input type="hidden" value="${param.grant_type }" name="grant_type" />
    <input type="hidden" value="${param.code }" name="code" />
    <input type="hidden" value="${param.redirect_uri }" name="redirect_uri" />
    <div class="content_middle" style="overflow-y:auto;height:auto!important;">
    <h4>&nbsp;&nbsp;请选择您需要授权的能力</h4>
    <div class="main">
    <ul id="ul_api_name_list">
        <c:forEach items="${apiList }" var="api" >
            <li>
              <input type="checkbox" id="${api.apiMethod }" value="${api.apiMethod }" checked="checked" name="scope" /><label for="${api.apiMethod }">&nbsp;&nbsp;${api.apiName }</label>      
            </li>	
        </c:forEach>                                                                                          
     </ul>
     </div>
  </div>
  </div>
  <div class="footer" style="text-align: center">
    <input type="submit" class="Btn" id="userauth"  style="text-align:center;" onclick="" value="授  权" />
  </div>
  
</div>

</body>
