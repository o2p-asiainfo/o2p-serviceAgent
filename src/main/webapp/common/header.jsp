<%@taglib prefix="s" uri="/struts-tags" %>
<%@page import="com.linkage.rainbow.ui.struts.BaseAction"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}"/> 
<%
String navBarPageId = request.getParameter("navBarPageId");
request.setAttribute("navBarPageId",navBarPageId);
 
BaseAction baseAction = new BaseAction();
String localeName = baseAction.getLocaleName();
request.setAttribute("localeName",localeName);
%>
<script>
 $(function() {
var Timer=new Date() ; 
var hours=Timer.getHours() ;
if(hours<=12)
{
$("#showmessage").text("<s:property value="%{getText('eaap.op2.portal.index.googmorning')}"/>"+",");
}else if(hours>12)
{  
$("#showmessage").text("<s:property value="%{getText('eaap.op2.portal.index.goodafternoon')}"/>"+",");
}
 });
 

</script> 
<!--head start --> 
<div class="sectionHead">
 
  <div class="head"><a class="logo" href="${contextPath}/index.jsp"><img src="${contextPath}/resource/${contextStyleTheme}/images/${contextStyleSpecial}/logo${localeName}.png" /></a>
   <c:choose>
    <c:when test="${userBean.name!=null and userBean.name!=''}">
        <div class="topLogin">
                 <span id='showmessage'></span>
                 ${userBean.name}<span class="cd"><s:property value="%{getText('eaap.op2.portal.index.spit')}" /></span>
        	    <a href="${contextPath}/login/logout.shtml"><s:property value="%{getText('eaap.op2.portal.index.logout')}" /></a> 
        </div>
    
    </c:when>
    <c:otherwise>
     <div class="topLogin">
   				<a href="${contextPath}/login/preLogin.shtml"><s:property value="%{getText('eaap.op2.portal.index.login')}" /></a><span class="cd"><s:property value="%{getText('eaap.op2.portal.index.spit')}" /></span>
        	    <a href="${contextPath}/reg/index.jsp"><s:property value="%{getText('eaap.op2.portal.index.reg')}" /></a> 
     </div>
    
    </c:otherwise>
   </c:choose>
    
    
  </div>
  <div class="navBar">
  
   <c:choose>
    <c:when test="${userBean.name!=null and userBean.name!=''}">
     <ul>
      <li id="homepage" <s:if test="#attr['navBarPageId']=='homepage'">class="cur"</s:if>><a href="${contextPath}/index.jsp"><span><s:property value="%{getText('eaap.op2.portal.index.index')}"/></span></a></li>
      <li id="devpage"  <s:if test="#attr['navBarPageId']=='devpage'">class="cur"</s:if>><a href="${contextPath}/login/userConter.shtml"><span><s:property value="%{getText('eaap.op2.portal.index.devIndex')}"/></span></a></li>
      <li id="provpage" <s:if test="#attr['navBarPageId']=='provpage'">class="cur"</s:if>><a href="${contextPath}/prov/provMain.shtml"><span><s:property value="%{getText('eaap.op2.portal.index.provIndex')}"/></span></a></li>
      <li id="pardpage" <s:if test="#attr['navBarPageId']=='pardpage'">class="cur"</s:if>><a href="${contextPath}/pard/pardIndex.shtml"><span><s:property value="%{getText('eaap.op2.portal.index.pradIndex')}"/></span></a></li>
      
      <li id="docpage" <s:if test="#attr['navBarPageId']=='docpage'">class="cur"</s:if>><a href="${contextPath}/doc/findPlatformView.shtml"><span><s:property value="%{getText('eaap.op2.portal.index.docIndex')}"/></span></a></li>
      <li id="supportpage"  <s:if test="#attr['navBarPageId']=='supportpage'">class="cur"</s:if>><a href="${contextPath}/support/findSupport.shtml"><span><s:property value="%{getText('eaap.op2.portal.index.supportIndex')}"/></span></a></li>
     </ul>
    </c:when>
    <c:otherwise>
     <ul>
      <li id="homepage" <s:if test="#attr['navBarPageId']=='homepage'">class="cur"</s:if>><a href="${contextPath}/index.jsp"><span><s:property value="%{getText('eaap.op2.portal.index.index')}"/></span></a></li>
      <li id="devpage"  <s:if test="#attr['navBarPageId']=='devpage'">class="cur"</s:if>><a href="${contextPath}/dev/index.jsp"><span><s:property value="%{getText('eaap.op2.portal.index.devIndex')}"/></span></a></li>
      <li id="provpage" <s:if test="#attr['navBarPageId']=='provpage'">class="cur"</s:if>><a href="${contextPath}/prov/index.jsp"><span><s:property value="%{getText('eaap.op2.portal.index.provIndex')}"/></span></a></li>
      <li id="pardpage" <s:if test="#attr['navBarPageId']=='pardpage'">class="cur"</s:if>><a href="${contextPath}/pard/index.jsp"><span><s:property value="%{getText('eaap.op2.portal.index.pradIndex')}"/></span></a></li>
      
      <li id="docpage" <s:if test="#attr['navBarPageId']=='docpage'">class="cur"</s:if>><a href="${contextPath}/doc/findPlatformView.shtml"><span><s:property value="%{getText('eaap.op2.portal.index.docIndex')}"/></span></a></li>
      <li id="supportpage"  <s:if test="#attr['navBarPageId']=='supportpage'">class="cur"</s:if>><a href="${contextPath}/support/findSupport.shtml"><span><s:property value="%{getText('eaap.op2.portal.index.supportIndex')}"/></span></a></li>
     </ul>
    </c:otherwise>
   </c:choose>
  
   
  </div>
</div>
 
<!--head end -->
