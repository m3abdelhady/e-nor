<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.Arrays" %>
<%@ page import="me.heuristic.util.HtmlUtil" %>
<%@ page import="me.heuristic.references.Messages" %>
<%@ page import="me.heuristic.services.AccountService" %>
<%@ page import="com.google.appengine.api.users.User" %>
<%@ page import="com.google.appengine.api.users.UserService" %>
<%@ page import="com.google.appengine.api.users.UserServiceFactory" %>
<%@ page import="com.google.appengine.api.datastore.Entity" %>
<%	String title = "All Accounts"; %>
<%@ include file="/includes/_header.inc" %>
<style>
.page-bar{display:none;}
</style>
<div class="portlet light">
	<div class="portlet-title" >
	<div class="caption">
	   	<span class="caption-subject font-green bold uppercase"><%= title %></span>
	</div>
	</div>
	<div class="mws-panel-body">	
 <%if(myEmail.contains("webmaster@campaignalyzer.com")){ %>
<%= AccountService.renderAccountList("") %>
<%}else{%>
<%response.sendRedirect("/"); %>
<%} %>
	</div>
</div>
<%= HtmlUtil.renderFooter() %>
