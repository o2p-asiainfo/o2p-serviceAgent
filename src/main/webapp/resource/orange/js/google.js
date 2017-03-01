/**
 * @author chenxin0524@gmail.com
 */
this.mall_cookie_utils||(this.mall_cookie_utils={},function(){mall_cookie_utils.getMallCookie=function(a){var b=document.cookie.split(";"),c="",d="",e="",f=!1,g="";for(g=0;g<b.length;g++){c=b[g].split("="),d=c[0].replace(/^\s+|\s+$/g,"");if(d==a)return f=!0,c.length>1&&(e=unescape(c[1].replace(/^\s+|\s+$/g,""))),e;c=null,d=""}if(!f)return null},mall_cookie_utils.isEmpty=function(a){return typeof a=="undefined"||a==="undefined|undefined"||a===null||a===""||a==="|"},mall_cookie_utils.getMallProvCode=function(){var a=mall_cookie_utils.getMallCookie("mallcity");return mall_cookie_utils.isEmpty(a)?"011":"0"+a.split("|")[0]},mall_cookie_utils.getMallCityCode=function(){var a=mall_cookie_utils.getMallCookie("mallcity");return mall_cookie_utils.isEmpty(a)?"110":a.split("|")[1]}}());

var provincecode = mall_cookie_utils.getMallProvCode();
var provincename="";
switch (provincecode) { 
case "010": provincename = "Nei Monggol"; break;	
case "011": provincename = "Beijing"; break;   		
case "013": provincename = "Tianjin"; break;   		
case "017": provincename = "Shandong"; break;   	
case "018": provincename = "Hebei"; break;   			
case "019": provincename = "Shanxi"; break;   		
case "030": provincename = "Anhui"; break;   			
case "031": provincename = "Shanghai"; break; 	 
case "034": provincename = "Jiangsu"; break;  	 
case "036": provincename = "Zhejiang"; break; 	 
case "038": provincename = "Fujian"; break;   		
case "050": provincename = "Hainan"; break;   		
case "051": provincename = "Guangdong"; break;  	
case "059": provincename = "Guangxi"; break;  		
case "070": provincename = "Qinghai"; break;  	 
case "071": provincename = "Hubei"; break;   			
case "074": provincename = "Hunan"; break;   			
case "075": provincename = "Jiangxi"; break; 			
case "076": provincename = "Henan"; break;   			
case "079": provincename = "Xizang"; break;  			
case "081": provincename = "Sichuan"; break;  		
case "083": provincename = "Chongqing"; break; 	 
case "084": provincename = "Shaanxi"; break;   		
case "085": provincename = "Guizhou"; break;   		
case "086": provincename = "Yunnan"; break;   		
case "087": provincename = "Gansu"; break;   			
case "088": provincename = "Ningxia"; break; 		 
case "089": provincename = "Xinjiang"; break;  	 
case "090": provincename = "Jilin"; break;   			
case "091": provincename = "Liaoning"; break;  	 
case "097": provincename = "Heilongjiang"; break;
case "098": provincename = "Headquarters"; break;
default:    provincename = "(not set)"; break;
} 

  var _gaq = _gaq || [];
  _gaq.push(['_setAccount', 'UA-27681312-1']);
  _gaq.push(['_setDomainName', '10010.com']);
  _gaq.push(['_setVar', provincename]); 
  _gaq.push(['_trackPageview']);

  function loadjajs() {
    var ga = document.createElement('script'); ga.type = 'text/javascript'; ga.async = true;
    ga.src = ('https:' == document.location.protocol ? 'https://ssl' : 'http://www') + '.google-analytics.com/ga.js';
    var s = document.getElementsByTagName('script')[0]; s.parentNode.insertBefore(ga, s);
  }