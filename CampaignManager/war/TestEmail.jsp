<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.Arrays" %>
<%@ page import="me.heuristic.util.HtmlUtil" %>
<%@ page import="me.heuristic.references.Messages" %>
<%@ page import="me.heuristic.references.UserType" %>
<%@ page import="me.heuristic.services.AccountService" %>
<%@ page import="com.google.appengine.api.users.User" %>
<%@ page import="com.google.appengine.api.users.UserService" %>
<%@ page import="com.google.appengine.api.users.UserServiceFactory" %>
<%@ page import="com.google.appengine.api.datastore.Entity" %>
<%@ page import="com.google.appengine.api.oauth.OAuthServiceFactory"%>
<%@ page import="com.google.appengine.api.oauth.OAuthService"%>
<%@ page import="com.google.api.client.auth.openidconnect.IdTokenResponse"%>
<%	String title = "Add/ Edit Account"; %>
<%@ include file="/includes/_header.inc" %>
<h1><%= title %></h1>

<div class="mws-panel grid_8">
	<div class="mws-panel-header">
	   	<span class="mws-i-24 i-plus">Test Sending Emails by API</span>
	</div>
	<div class="mws-panel-body">	
	<script>alert("<%session.getAttribute("email"); %>")</script>

	
	
<%-- <%
	// if not signed in
	if ("".equals(myEmail)) {
%>
<%= Messages.renderErrorMessage(Messages.UNRECOGNIZED_USER) %>
<%
	// if no correct account
	} else if (0!=longAccountId && null==myAccount) {
%>
<%= Messages.renderErrorMessage(Messages.NO_SUCH_ACCOUNT) %>
<%
	// check expiration
	} else if (0!=longAccountId && AccountService.hasExpired(myAccount)) {
%>
<%= Messages.renderErrorMessage(Messages.ACCOUNT_EXPIRED) %>
<%
	// check permission
	} else if (0!=longAccountId && !UserType.hasPermission(myRole, UserType.PERMISSION_MANAGE_ACCOUNT)) {
%>
<%= Messages.renderErrorMessage(Messages.NO_PERMISSION) %>
<%
	} else {
		// prepare fields for form
		String fromAddress = "";
		String fromName = "";
		String toAddress = "";
		String toName = "";
		String emailSubject = "";
		String emailBody = "";
		
		String[] errors = request.getParameterValues("err");
		
		// if errors exit
		if (null!=errors) {
			//reload submitted data
			fromAddress = HtmlUtil.handleNullWhiteSpaces(request.getParameter("fromAddress"));
			fromName = HtmlUtil.handleNullWhiteSpaces(request.getParameter("fromName"));
			toAddress = HtmlUtil.handleNullWhiteSpaces(request.getParameter("toAddress"));
			toName = HtmlUtil.handleNullWhiteSpaces(request.getParameter("toName"));
			emailSubject = HtmlUtil.handleNullWhiteSpaces(request.getParameter("emailSubject"));
			emailBody = HtmlUtil.handleNullWhiteSpaces(request.getParameter("emailBody"));
%>
<%= Messages.renderMessages(Arrays.asList(errors), Messages.STYLE_ERROR) %>
<%
// 		} else if (0!=longAccountId) {
// 			// retrieve entity data
// 			emailAddress = (String) myAccount.getProperty("email");
// 			firstName = (String) myAccount.getProperty("fname");
// 			lastName = (String) myAccount.getProperty("lname");
// 			company = (String) myAccount.getProperty("company");
// 		    accountName = (String) myAccount.getProperty("name");
// 			address = (String) myAccount.getProperty("address");
// 			city = (String) myAccount.getProperty("city");
// 			state = (String) myAccount.getProperty("state");
// 			zip = (String) myAccount.getProperty("zip");
// 			country = (String) myAccount.getProperty("country");
// 			phone = (String) myAccount.getProperty("phone");
// 			website = (String) myAccount.getProperty("website");
// 			agreement = (String) myAccount.getProperty("agreement");
		}
%>
<%
	}
%> --%>
	</div>
</div>
<%= HtmlUtil.renderFooter() %>
