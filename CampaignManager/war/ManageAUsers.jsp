<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.Arrays" %>
<%@ page import="me.heuristic.util.HtmlUtil" %>
<%@ page import="me.heuristic.references.Messages" %>
<%@ page import="me.heuristic.references.AccountType" %>
<%@ page import="me.heuristic.references.UserType" %>
<%@ page import="me.heuristic.services.AUserService" %>
<%@ page import="me.heuristic.services.AccountService" %>
<%@ page import="com.google.appengine.api.users.User" %>
<%@ page import="com.google.appengine.api.users.UserService" %>
<%@ page import="com.google.appengine.api.users.UserServiceFactory" %>
<%@ page import="com.google.appengine.api.datastore.Entity" %>
<%	String title = "User Manager"; %>
<%@ include file="/includes/_header.inc" %>
<h3 style="display:none;"><%= title %></h3>

<div class="row">
<div class="col-md-12">
<div class="portlet light ">

	<div class="portlet-title">
	<div class="caption">
	   	<span class="caption-subject font-green bold uppercase"><%= title %></span>
	</div>
	</div>
	<div class="portletl-body">
<%
	String oldUserEmail = HtmlUtil.handleNullWhiteSpaces(request.getParameter("oldUserEmail"));

	// if not signed in
	if ("".equals(myEmail)) {
%>
<% response.sendRedirect("/signin.jsp"); %>
<%-- <%= Messages.renderErrorMessage(Messages.UNRECOGNIZED_USER) %> --%>
<%
	// if no correct account
	} else if (null==myAccount) {
%>
<%= Messages.renderErrorMessage(Messages.NO_SUCH_ACCOUNT) %>
<%
	// check new account
	} else if (AccountService.isNewAccount(myAccount)) {
%>
<%= Messages.renderErrorMessage(Messages.ACCOUNT_NEW) %>
<%
	// check expiration
	} else if (AccountService.hasExpired(myAccount)) {
%>
<%= Messages.renderErrorMessage(Messages.ACCOUNT_EXPIRED) %>
<%
	// check account type permissions
	} else if (!AccountType.allowsManageUsers((String) myAccount.getProperty("type"))) {
%>
<%= Messages.renderErrorMessage(Messages.NO_MANAGE_USERS_ALLOWED) %>
<%
	// check permission
	} else if (!UserType.hasPermission(myRole, UserType.PERMISSION_VIEW_USERS)) {
%>
<%= Messages.renderErrorMessage(Messages.NO_PERMISSION) %>
<%
	} else {
		String newUserEmail = "";
		String newUserRole = "";
	
		String[] errors = request.getParameterValues("err");
		
		if (null!=errors) {
			newUserEmail = HtmlUtil.handleNullWhiteSpaces(request.getParameter("newUserEmail"));
			newUserRole = HtmlUtil.handleNullWhiteSpaces(request.getParameter("newUserRole"));
%>
<%= Messages.renderMessages(Arrays.asList(errors), Messages.STYLE_ERROR) %>
<%
		}

		String[] infos = request.getParameterValues("info");
		if (null!=infos) {
%>
<%=	Messages.renderMessages(Arrays.asList(infos), Messages.STYLE_INFO) %>
<%
		}
%>
<%= AUserService.renderAccountUsersListForm(myRole, myAccount, oldUserEmail, newUserEmail, newUserRole) %>
<%
	}
%>
	</div>
</div>
</div>
</div>
<%= HtmlUtil.renderFooter() %>
<script type="text/javascript">
$(".note").click(function(){
	$(this).animate({opacity:0}, function() {
		$(this).slideUp("medium", function() {
			$(this).css("opacity", '');
		});
	});
	
});
</script>
