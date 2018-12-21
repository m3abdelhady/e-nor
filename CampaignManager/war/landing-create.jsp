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
session.setAttribute("userEmail", request.getParameter("myEmailc"));
session.setAttribute("userPhoto", request.getParameter("myPhotoc"));
%>

<%@ include file="/includes/_header.inc" %>

<script>
window.location.replace("/NewAccount.jsp");	
</script>