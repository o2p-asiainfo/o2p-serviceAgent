<%--
<%@ include file="/common/taglibs.jsp"%>
<script type="text/javascript"
	src="${contextPath}/resource/comm/js/jquery.js"></script>
<script src="http://maps.googleapis.com/maps/api/js?sensor=false"
	type="text/javascript"></script>
<script src="${contextPath}/resource/comm/js/gmap3.js"></script>
<link rel="stylesheet" type="text/css"
	href="${contextPath}/resource/${contextStyleTheme}/css/platform.css" />
<link rel="stylesheet" type="text/css" href="${contextPath}/resource/${contextStyleTheme}/css/platform.css" />
<link rel="stylesheet" type="text/css"
	href="${contextPath}/resource/${contextStyleTheme}/css/platform.css" />
<link rel="stylesheet" type="text/css" href="${contextPath}/resource/${contextStyleTheme}/css/manage.css" />
<link rel="stylesheet" type="text/css"
	href="${contextPath}/resource/${contextStyleTheme}/css/register.css" />

<style>
.gmap3 {
 
	border: 1px dashed #C0C0C0;
	width: 820px;
	height: 510px;
}
</style>


<html>
	<body style="">
		<s:fielderror />
		<s:form name="gmapForm" id="gmapForm"  action="" namespace="/fileShare" method="post" enctype="multipart/form-data" theme="simple">
		<div id="map3" class="gmap3"></div>
		<table>
			<tr>
				<td><input type="hidden" id="lat-north" name="north" /></td>
				<td><input type="hidden" id="lng-east" name="east" /></td>
			</tr>
			<tr>
					<td style="text-align: center;">
						<table >
							<tr>
								<td style="text-align: center;">
								    <input id="buttonadddept" type="button" class="Btn" onclick="parent.$('#dialog').dialog('close');" value=<s:property value="%{getText('eaap.op2.portal.devmgr.verify.appRgeOver')}"/> />
								 </td>
							</tr>
						</table>
						<input type="hidden"  id="north_east" name="coordinate" />
						<input type="hidden"  id="defaultId" name="defaultId" value="<%=request.getParameter("defaultId")%>" />
					</td>
				</tr>
				
		</table>
       </s:form>
	</body>
	
</html>

<script type="text/javascript">
 var tmpid = document.getElementById("defaultId").value ;
 $(function(){
 $("#map3").gmap3({
      marker:{
         latLng: [tmpid.split(',')[0],tmpid.split(',')[1]],
         options:{
          draggable:true
        },
        events:{
          dragend: function(marker){
            $(this).gmap3({
            	
              getaddress:{
                latLng:marker.getPosition(),
                callback:function(results){
                  var map = $(this).gmap3("get"),
                    infowindow = $(this).gmap3({get:"infowindow"}),
                    content = results && results[1] ? results && results[1].formatted_address : "no address";
                  if (infowindow){
                    infowindow.open(map, marker);
                    infowindow.setContent(content);
                  } else {
                    $(this).gmap3({
                      infowindow:{
                        anchor:marker, 
                        options:{content: content}
                      }
                    });
                  }
              
                }
              }
            });
           				  var r = marker.getPosition();
            			  $("#lat-north").val(r.lat());
	                  	  $("#lng-east").val(r.lng());
	                  	  $("#north_east").val(r.lat()+","+r.lng());
            
          }
        }
      },
      map:{
        options:{
          zoom: 16
        }
      }
    });
  });


function onSubmit(){
 
	 parent.document.getElementById('north_east').value=document.getElementById('north_east').value;
	 
	parent.$('#dialog').dialog('close');
}

</script>
 --%>