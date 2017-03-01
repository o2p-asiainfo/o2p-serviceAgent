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
      .gmap3{
        scrolling:no;
        border: 1px dashed #C0C0C0;
        width: 820px;
	    height: 550px;
      }
      .infow{
        width:250px;
        height:150px;
      }
    </style>



    
    <script type="text/javascript">
    
      // http://blog.mridey.com/2010/11/how-to-embed-streetview-in-infowindow.html
      
      function Panorama(){
        var p,  marker, infowindow, map;
        
        this.setMap = function(obj){
          map = obj;
        }
        
        this.setMarker = function(obj){
          marker = obj;
        }
        
        this.setInfowindow = function(obj){
          infowindow = obj;
        }
        
        this.open = function(){
          infowindow.open(map, marker);
        }
        
        this.run = function(id){
          if (!marker) {
            return;
          }
          p = new google.maps.StreetViewPanorama(
            document.getElementById(id), 
            { navigationControl: true,
              navigationControlOptions: {style: google.maps.NavigationControlStyle.ANDROID},
              enableCloseButton: false,
              addressControl: false,
              linksControl: false
            }
          );
          p.bindTo("position", marker);
          p.setVisible(true);
        }
      };
    
      function onShow(){
       var defaultId = getQueryString('defaultId');
       var arrayObj = new Array([1]);
        if(defaultId==null||defaultId=='')
        {
           for(var m=0;m<4;m++)
	        {
	        
	        if(document.getElementById('north_east'+m)!=null)
	        {
	         if(!document.getElementById('north_east'+m).disabled)
	         {
	         arrayObj[m]= new Array();
	         arrayObj[m][0]= document.getElementById('north_east'+0).value.split(",")[0];
	         arrayObj[m][1]= document.getElementById('north_east'+0).value.split(",")[1];
	         }
	        }
	       }
        }else
         {
            var tmpArray = defaultId.split(';')
            for(var m=0;m<tmpArray.length;m++)
	        {
	         arrayObj[m]= new Array();
	         arrayObj[m][0]= tmpArray[m].split(",")[0];
	         arrayObj[m][1]= tmpArray[m].split(",")[1];
	        }
          }
        var points = arrayObj;
         var map;
        
        $('#test1').gmap3({
          map:{
            options:{
              zoom: 12,
              mapTypeId: google.maps.MapTypeId.ROADMAP,
              streetViewControl: false,
              center: points[0]
            },
            callback: function(aMap){
              map = aMap;
            }
          }
        });
            
        
        $.each(points, function(i, point){
         
          var panorama = new Panorama();
          panorama.setMap(map);
        
          $("#test1").gmap3({
            marker:{
              latLng: point,
              options:{title: 'click', draggable: true},
              callback: function(marker){
                panorama.setMarker(marker);
              },
            
            events:{
                click: function(point){
          	      panorama.open();
          	     },
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
            			  $("#north_east"+i).val(r.lat()+","+r.lng()); 
	              }
               }
            },
            
            infowindow:{
              options:{
                content: "<div id='iw"+i+"' class='infow'></div>"
              },
              callback: function(infowindow){
                panorama.setInfowindow(infowindow);
              },
              
              events:{
                domready: function(){
                  panorama.run("iw"+i);
                }
          	  
              } 
             }
                          
          });
               
        });
                          
      };
      
      function getQueryString(name) {   
		 var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");    
		 var r = window.location.search.substr(1).match(reg);    
		 if (r != null) 
		 return unescape(r[2]); 
		 return null;    
	  }
	  
      function onSubmit(){
      var tmp_north_east="";
       for(var m=0;m<4;m++){ 
        if(!document.getElementById('north_east'+m).disabled)
        {
         tmp_north_east+=document.getElementById('north_east'+m).value+';';
        }
       }
       
       parent.document.getElementById(document.getElementById('defaultName').value).value=tmp_north_east.substring(0,tmp_north_east.length-1);
       parent.$('#dialog').dialog('close');
     }
   
    
    </script>
    
   <html>
	  <body onload="onShow()">
       <div id="test1" class="gmap3"></div>
		<s:form name="gmapForm" id="gmapForm"  action="" namespace="/fileShare" method="post" enctype="multipart/form-data" theme="simple">
		 <table>
		 <tr>
		 <td><s:property value="%{getText('eaap.op2.portal.pardmix.choosepointnum')}"/>
		 <select name="myaddpoint" onchange="javascript:gmapForm.action='${contextPath}/pardMix/onchangeMapPoint.shtml';gmapForm.submit();">
		  <option value="1" <c:if test="${myaddpoint=='1'}">selected</c:if>>1</option>
		  <option value="2" <c:if test="${myaddpoint=='2'}">selected</c:if>>2</option>
		  <option value="3" <c:if test="${myaddpoint=='3'}">selected</c:if>>3</option>
		  </select>
		 </tr> 
		 <tr>
		    <td style="text-align: center;">
				<table>
				  <tr>
				   <td>
				    <div id="mypoint">
				    <input type="hidden"             value="-33.88917576169259,151.2442638310547"  id="north_east0" name="point0" /> 
				    <input type="hidden" <c:if test="${myaddpoint<'2'}">disabled</c:if>    value="-33.889175761692222222258,151.2442638310547"  id="north_east1" name="point1" /> 
				    <input type="hidden" <c:if test="${myaddpoint<'3'}">disabled</c:if>    value="-33.88917576162222229258,151.2442638310547"  id="north_east2" name="point2" /> 
				    <input type="hidden" <c:if test="${myaddpoint<'4'}">disabled</c:if>    value="-33.889175761622222222229258,151.2442638310547"  id="north_east3" name="point3" /> 
				    </div>
				    </td>
				  </tr>
					<tr>
						<td style="text-align: center;">
						   <input type="hidden"  value="${defaultName}"  id="defaultName" name="defaultName" />   
							<input id="buttonadddept" type="button" class="Btn" onclick="onSubmit()" value=<s:property value="%{getText('eaap.op2.portal.devmgr.verify.appRgeOver')}"/> />
							<input id="cancel_button" class="Btn" value=<s:property value="%{getText('eaap.op2.portal.devmgr.cancel')}"/> type="button" onclick="parent.$('#dialog').dialog('close');" />
							 
						</td>
					</tr>
				</table>
			  </td>
		  </tr>
		</table>
       </s:form>
  </body>
</html>
 