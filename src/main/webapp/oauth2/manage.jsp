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
  <meta charset="utf-8" />
  <title>Telenor Open Operational Platform</title>
  <meta content="width=device-width, initial-scale=1.0" name="viewport" />
  <meta content="" name="description" />
  <meta content="" name="author" />
  <!-- BEGIN GLOBAL MANDATORY STYLES -->
  <%@ include file="../resource/includes/global-css1.shtml"%>
  <!-- END GLOBAL MANDATORY STYLES -->

  <!-- BEGIN PAGE LEVEL PLUGIN STYLES -->
  <link href="../resource/plugins/bootstrap-datepicker/datepicker.css" rel="stylesheet" />
  <link href="../resource/plugins/data-tables/DT_bootstrap.css" rel="stylesheet" />
  <link href="../resource/plugins/select2/select2.min.css" rel="stylesheet" />
  <!-- END PAGE LEVEL PLUGIN STYLES -->

  <!-- BEGIN THEME STYLES -->
  <%@ include file="../resource/includes/global-css2.shtml"%>
  <!-- END THEME STYLES -->
 
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
<div class="row breadcrumbs">
    <div class="container">
      <div class="col-md-10 col-sm-10">
        <h2>Service Management Center</h2>
      </div>
      <div class="col-md-2 col-sm-2 text-right" style="padding-top:30px">
        <a href="exit.shtml?tenantId=<%=request.getSession().getAttribute("tenantId") %>&appkey=<%=request.getSession().getAttribute("appkey") %>&redirect_uri=<%=request.getSession().getAttribute("redirect_uri") %>"  >Exit</a>
      </div>
    </div>
  </div>
<!-- END HEADER --> 

<!-- BEGIN PAGE CONTAINER -->
<div class="page-container margin-top-10"> 
  <div class="container margin-bottom-40" style="border:1px solid #ccc; padding:0" >
  	<div class="col-md-2 navside">
    	<ul>
        	<li style="padding-left:10px"><span class="glyphicon glyphicon-transfer margin-right-10"></span>Management</a></li>
            <li><a href="###"  id="granted">Authorized</a></li>
            <li><a href="###"  id="unauthorized">Unauthorized</a></li>
        </ul>
    
    </div>
    <div class="col-md-10">
    	<div style="padding:15px; height:40px">
        	<div class="authorityTit">Authorized Services<span class="red">(<a id="authCount"></a>)</span></div>
            <div class="mt10">
            	<p class="text-right"><a href="###">授权须知</a></p>
            	<table class="table table-sl table-bordered table-nowrap text-middle" id="dt">
                	<thead>
                   		<tr class="text-center" style="background:#f5f5f9">
							<th width="8%"></th>
							<th width="55%">Service Code</th>
                            <th width="20%">Authorize Status</th>
                            <th width="17%">Operation</th>
						</tr>
                    </thead>
                    <tbody></tbody>
                	<footer>
            			<tr>
                        	<th><input id="batchCheck" type="checkbox" class="group-checkable" data-set="#dt .checkboxes"></th>
                            <th colspan="3" align="right">
                            	<input class="theme-btn " type="button" id="cancelAll"  value="Cancel">
                            	<input class="theme-btn " type="button" id="AuthorizeAll"  value="Authorize">
                            </th>
                        </tr>
          			</footer>
                </table>
            </div>
        </div>
    </div>
    
  </div>
</div>

<!-- END PAGE CONTAINER --> 

<div class="modal" id="authorityProtocol"  style="width:100%;z-index:9999">
   <div class="modal-dialog"  style="max-width:560px;width:80%">
      <div class="modal-content">
         <div class="modal-header">
            <h4 class="modal-title" id="myModalLabel2">
              Authorize
            </h4>
         </div>
         <div class="modal-body">
         	<!--
            <ul class="authoritylist">
            	<li><label class="label"><input type="checkbox">授权内容内容内容内之余</label></li>
                <li><label class="label"><input type="checkbox">授权内容内容内容内之余</label></li>
                <li><label class="label"><input type="checkbox">授权内容内容内容内之余</label></li>
                <li><label class="label"><input type="checkbox">授权内容内容内容内之余</label></li>
            	<li><label class="label"><input type="checkbox">授权内容内容内容内之余</label></li>
            </ul>
            -->
            <table class="table">
                	<thead>
                   		<tr class="text-center" style="background:#f5f5f9">
							<th width="20%"></th>
							<th width="80%">应用名称</th>
						</tr>
                    </thead>
                    <tbody></tbody>
                	
                </table>
         </div> 
         <div style="margin-top:10px; text-align:center" class="modal-footer clearfix;"> 
         	 
         	<button id="protocolOk" class="btn btn-primary" type="button">Authorize</button>
            <button data-dismiss="modal" class="btn btn-default" type="button">Cancel</button>
         </div>  	
      </div>
   </div>
                <!-- /.modal-content -->
</div>
<div class="modal fade" id="authoritySet"   style="width:100%;z-index:9999">
   <div class="">
      <div class="modal-content">
         <div class="modal-header">
            <h4 class="modal-title" id="myModalLabel2">
               Authorize
            </h4>
         </div>
         <div class="modal-body clearfix">
         	<label  class="control-label"  style="float:left; height:36px; line-height:36px">Valid Time：</label><input type="text" id="time" value="10" class="form-control margin-left-10" style="float:left;width:150px">
            
            <select id="timeUnit" class="form-control margin-left-10" placeholder="name" style="width:150px;float:left">
	            <option value="86400" selected="selected">Day</option>
	            <option value="3600">Hour</option>
	            <option value="60">Minute</option>
	            <option value="1">Second</option>
	          </select>
            <input type="hidden" id="ids"  value=""/>
         </div> 
         <div style="margin-top:10px; text-align:center" class="modal-footer clearfix;"> 
         	 
         	<button id="submitAuthorize" class="btn btn-primary" type="button">Authorize</button>
         	<button id="cancelAuthorize" class="btn btn-primary" type="button">Cancel</button>
        
         </div>  	
      </div>
   </div>
                <!-- /.modal-content -->
</div>
<!-- Load javascripts at bottom, this will reduce page load time --> 
<!-- BEGIN CORE PLUGINS(REQUIRED FOR ALL PAGES) --> 
<!--[if lt IE 9]>
<script src="../resource/plugins/respond.min.js"></script>
<script src="../resource/plugins/excanvas.min.js"></script> 
<![endif]--> 
   <%@ include file="../resource/includes/global-js.shtml"%>
  <!-- END CORE PLUGINS -->
  <!-- BEGIN PAGE LEVEL JAVASCRIPTS(REQUIRED ONLY FOR CURRENT PAGE) -->
  <script src="../resource/plugins/bootstrap-datepicker/bootstrap-datepicker.js"></script>
  <script src="../resource/plugins/data-tables/jquery.dataTables.min.js"></script>
  <script src="../resource/plugins/data-tables/DT_bootstrap.js"></script>
  <script src="../resource/plugins/select2/select2.min.js"></script>
  <script src="../resource/scripts/app.js"></script> 
  <script src="../resource/scripts/oauthIinstall.js"></script>

<script type="text/javascript">
 jQuery(document).ready(function() {
  App.init();
  oauthInstall.init('<%=request.getParameter("tenantId") %>', '<%=request.getParameter("appkey") %>', '<%=request.getParameter("redirect_uri") %>');
  //$("#authorityProtocol").modal("show");
 });
 </script> 
<!-- END PAGE LEVEL JAVASCRIPTS -->
</body>
<!-- END BODY -->

</html>
