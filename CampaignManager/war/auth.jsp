<%@page import="com.google.gson.JsonArray"%>
<%@page import="com.google.gson.JsonElement"%>
<%@page import="me.heuristic.services.GoogleAuthHelper" %>
<%@ page import="java.util.Arrays" %>
<%@ page import="me.heuristic.util.HtmlUtil" %>
<%@ page import="me.heuristic.references.Messages" %>
<%@ page import="me.heuristic.references.UserType" %>
<%@ page import="me.heuristic.services.AccountService" %>
<%-- <%@ page import="com.google.appengine.api.users.User" %>
<%@ page import="com.google.appengine.api.users.UserService" %>
<%@ page import="com.google.appengine.api.users.UserServiceFactory" %>
<%@ page import="com.google.appengine.api.datastore.Entity" %>
<%@ page import="com.google.appengine.api.oauth.OAuthServiceFactory"%>
<%@ page import="com.google.appengine.api.oauth.OAuthService"%>
<%@ page import="com.google.api.client.auth.openidconnect.IdTokenResponse"%> --%>

<%	String title = "Basecamp OAuth 2.0 v1 Demo"; %>
<%-- <%@ include file="/includes/_header.inc" %> --%>
<%-- <%myEmail = session.getAttribute("email").toString(); %> --%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

<title>title</title>
<style>
body {
	font-family: Sans-Serif;
	margin: 1em;
}

.oauthDemo a {
	display: block;
	border-style: solid;
	border-color: #bbb #888 #666 #aaa;
	border-width: 1px 2px 2px 1px;
	background: #ccc;
	color: #333;
	line-height: 2;
	text-align: center;
	text-decoration: none;
	font-weight: 900;
	width: 13em;
}

.oauthDemo pre {
	background: #ccc;
}

.oauthDemo a:active {
	border-color: #666 #aaa #bbb #888;
	border-width: 2px 1px 1px 2px;
	color: #000;
}

.readme {
	padding: .5em;
	background-color: #F9AD81;
	color: #333;
}
</style>
</head>
<body>
	<div><a href="https://launchpad.37signals.com/authorization/new?type=web_server&client_id=44a552371b82c92ce85cd99bb13fbc3b5ea83220&redirect_uri=http://e-norlaunchpad.com/mikeanderson/basecamp.html">Basecamp</a></div>
</body>
</html>
