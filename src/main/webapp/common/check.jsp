<%@taglib prefix="s" uri="/struts-tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}"/> 
<%
String navBarPageId = request.getParameter("navBarPageId");
request.setAttribute("navBarPageId",navBarPageId);
%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<script type="text/javascript" src="${contextPath}/resource/comm/js/jquery.js"></script>
<script>
function  setshowmessage(){
alert(111);
}

</script>
</head>
<!--head start -->
<body onload="setshowmessage()">

</body>
</html>
<!--head end -->
