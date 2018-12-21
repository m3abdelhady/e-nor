<%@page import="java.util.Iterator"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.Arrays" %>
<%@ page import="me.heuristic.util.HtmlUtil" %>
<%@ page import="me.heuristic.references.Messages" %>
<%@ page import="me.heuristic.services.AccountService" %>
<%-- <%@ page import="com.google.appengine.api.users.User" %>
<%@ page import="com.google.appengine.api.users.UserService" %>
<%@ page import="com.google.appengine.api.users.UserServiceFactory" %> --%>
<%@ page import="com.google.appengine.api.datastore.Entity" %>
<%	String title = "Landing"; %>

<%

session.setAttribute("userEmail", request.getParameter("myEmail"));
session.setAttribute("userPhoto", request.getParameter("myPhoto"));
%>


<%@ include file="/includes/_header.inc" %>
<script>
var aid= $('#aid').val();

</script>
<%if(AccountService.getAllAccountsCountFor(request.getParameter("myEmail")) == 0 || (request.getParameter("action") == "create")){%>
<script>window.location="/NewAccount.jsp";	</script>
<%} else{ %>

<script>window.location="/dashboard.jsp?aid=" + aid;</script>
<%} %>
