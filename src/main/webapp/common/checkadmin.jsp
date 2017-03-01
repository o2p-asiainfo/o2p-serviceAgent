<%@ include file="/common/taglibs.jsp"%> 
<%
 String userId ="" ;
 if(session.getAttribute("userId")!=null){
   userId= String.valueOf(session.getAttribute("userId"));
 }  
 if (userId.equals("")){
   out.print("<script>alert('Login Please!');parent.location='"+request.getContextPath()+"/login/index.jsp';</script>") ;
 }
%>

