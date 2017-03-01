<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<!--[if IE 8]> <html lang="en" class="ie8 no-js"> <![endif]-->
<!--[if IE 9]> <html lang="en" class="ie9 no-js"> <![endif]-->
<!--[if !IE]><!-->
<html lang="en">
<!--<![endif]-->
<!-- BEGIN HEAD -->

<head>
<meta charset="UTF-8">
<title>Telenor Open Operational Platform</title>
<meta content="width=device-width, initial-scale=1.0" name="viewport" />
<meta content="" name="description" />
<meta content="" name="author" />

<!-- BEGIN GLOBAL MANDATORY STYLES -->
<link href="../resource/plugins/font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css" />
<link href="../resource/plugins/bootstrap/css/bootstrap.min.css" rel="stylesheet" type="text/css" />
<!-- END GLOBAL MANDATORY STYLES -->

<!-- BEGIN THEME STYLES -->

<link href="../resource/css/style.css" rel="stylesheet" type="text/css" />
<link href="../resource/css/themes/orange.css" rel="stylesheet" type="text/css" id="style_color" />
<link href="../resource/css/style-responsive.css" rel="stylesheet" type="text/css" />
<link href="../resource/css/custom.css" rel="stylesheet" type="text/css" />
<!-- END THEME STYLES -->

<link rel="shortcut icon" href="favicon.ico" />
</head>
<!-- END HEAD -->
<!-- BEGIN BODY -->

<body>
<!-- BEGIN HEADER -->
<div style="border-bottom:1px solid #ccc; box-shadow:2px 2px 6px #ccc">
<div class="container">
	<div class="header" style="box-shadow:none; z-index:0; "><a class="logo-v1" href="javascript:void(0);"> <img src="../resource/img/logo.png" id="logoimg" style="widows:100px; height:auto"> </a>
    </div>
</div>
</div>
<input type="hidden" id="" value="">
<!-- END HEADER --> 

<!-- BEGIN PAGE CONTAINER -->
<div class="page-container mt20" > 
  
  <!-- BEGIN CONTAINER -->
  <div class="container margin-bottom-40 mt30">
    <div class="row">
      <div class="col-md-6 col-md-offset-3 col-sm-6 col-sm-offset-3" style="font-size:14px">
      	
      </div>
      <div class="col-md-6 col-md-offset-3 col-sm-6 col-sm-offset-3 login-signup-page  mt10">
      	
        <form class="login-form" method="post">
          <div class="form-group">
          	<h3 class="text-center" style="color:#000">亚信授权管理服务中心</h3>
      		<p class=" margin-top-20" style="text-indent:2em">登录授权后 Iboss 可获得您的商家信息， 您也可以在授权管理 里查看和取消授权，为您提供更加优质的服务！</p>
            <div class="input-group groupinput" > <span class="input-group-addon" style="background:none;border:none"><i class="fa fa-envelope"></i></span>
              <input type="text" class="form-control" placeholder="Username" name="username"  id="username" style="border:none; z-index:0 " value="">
            </div>
          </div>
          <div class="form-group">
            <div class="input-group groupinput"> <span class="input-group-addon"  style="background:none;border:none; padding-left:15px"><i class="fa fa-lock"></i></span>
              <input type="password" class="form-control" placeholder="Password" name="password" id="password" style="border:none; z-index:0 ">
            </div>
          </div>
          <div class="form-group" style="position:relative; padding-right:110px">
            <div class="col-md-12 validateinput"> <span class="input-group-addon"><i class="glyphicon glyphicon-ok"></i></span>
              <input type="text" class="form-control" placeholder="Verification" name="verification" id="verification" style="border:none">
               </div>
               <img src="http://emcc.tenant.72.test:9500/sso/login/generateCheckCode.shtml" class="verifyImg" height="34" id="captchaimage" style="position:absolute;top:0;right:0;cursor:pointer">
          </div>
          <div class="row clearfix" style="padding:10px 15px">
              <input type="checkbox" id="authority" checked><label class="margin-left-10">我已接受<a href="###">授权须知</a><a target="_blank" href="../oauth2/manage.shtml">权限管理</a></label></div>
         
          <div class="row clearfix ">
            <div class=" text-center" style="padding:0 15px">
              <button type="submit" class="btn theme-btn col-md-12" id="loginSubmit">submit</button>
            </div>
          </div>
        </form>
      </div>
    </div>
  </div>
  <!-- END CONTAINER --> 
</div>

<!-- END CONTAINER -->

<!-- END PAGE CONTAINER --> 

<!-- Load javascripts at bottom, this will reduce page load time --> 
<!-- BEGIN CORE PLUGINS(REQUIRED FOR ALL PAGES) --> 
<!--[if lt IE 9]>
    <script src="../resource/plugins/respond.min.js"></script>  
    <![endif]--> 
<script src="../resource/plugins/jquery-1.10.2.min.js" type="text/javascript"></script> 
<script src="../resource/plugins/jquery-migrate-1.2.1.min.js" type="text/javascript"></script> 
<script src="../resource/plugins/bootstrap/js/bootstrap.min.js" type="text/javascript"></script> 
<!-- END CORE PLUGINS --> 
<!-- BEGIN PAGE LEVEL JAVASCRIPTS(REQUIRED ONLY FOR CURRENT PAGE) --> 
<script src="../resource/plugins/uniform/jquery.uniform.min.js" type="text/javascript"></script> 
<script src="../resource/plugins/jquery-validation/jquery.validate.min.js" type="text/javascript"></script>
<script src="../resource/scripts/login.js"></script> 
<script type="text/javascript">
 jQuery(document).ready(function() {
  Login.init('<%=request.getParameter("tenantId") %>', '<%=request.getParameter("appkey") %>', '<%=request.getParameter("redirect_uri") %>');
  $("#authority").change(function(){
	  if(!$(this).is(":checked")){
		  $("#loginSubmit").attr("disabled",true);
		  }
	  else{$("#loginSubmit").attr("disabled",false);}
	  });
 });
 </script> 
<!-- END PAGE LEVEL JAVASCRIPTS -->
</body>
<!-- END BODY -->

</html>
