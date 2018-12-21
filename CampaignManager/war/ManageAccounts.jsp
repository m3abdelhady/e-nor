<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.Arrays" %>
<%@ page import="me.heuristic.util.HtmlUtil" %>
<%@ page import="me.heuristic.references.Messages" %>
<%@ page import="me.heuristic.services.AccountService" %>
<%@ page import="com.google.appengine.api.users.User" %>
<%@ page import="com.google.appengine.api.users.UserService" %>
<%@ page import="com.google.appengine.api.users.UserServiceFactory" %>
<%@ page import="com.google.appengine.api.datastore.Entity" %>

<%	String title = "My Accounts"; %>
<%@ include file="/includes/_header.inc" %>
<%myEmail = session.getAttribute("userEmail").toString(); %>
<style>
.page-bar{display:none;}
</style>
<h3 style="display:none"><%= title %></h3>
<div class="portlet light">
	<div class="portlet-title" >
	<div class="caption">
	   	<span class="caption-subject font-green bold uppercase"><%= title %></span>
	</div>
	</div>
	<div class="mws-panel-body">	
<%
//myEmail = session.getAttribute("email").toString();
	// if not signed in
	if ("".equals(myEmail)) {
%>

<%-- <% response.sendRedirect("/signin.jsp?continuer=ManageAccounts.jsp"); %> --%>
<%-- <%= Messages.renderErrorMessage(Messages.UNRECOGNIZED_USER) %> --%>
<%
	// if no correct account
	} else {

		String[] errors = request.getParameterValues("err");
		
		if (null!=errors) {
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

<%= AccountService.renderAccountList(myEmail) %>
<%
	}
%>
	</div>
</div>

<%= HtmlUtil.renderFooter() %>
<script type="text/javascript">
alert = function() {};
$(".note").click(function(){
	$(this).animate({opacity:0}, function() {
		$(this).slideUp("medium", function() {
			$(this).css("opacity", '');
		});
	});
	
});
</script>
