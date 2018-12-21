<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.Arrays" %>
<%@ page import="me.heuristic.util.HtmlUtil" %>
<%@ page import="me.heuristic.references.Messages" %>
<%@ page import="me.heuristic.references.UserType" %>
<%@ page import="me.heuristic.services.AccountService" %>
<%@ page import="me.heuristic.services.TaggingSettingsService" %>
<%@ page import="me.heuristic.services.AUserService" %>
<%-- <%@ page import="com.google.appengine.api.users.User" %> --%>
<%@ page import="com.google.appengine.api.users.UserService" %>
<%@ page import="com.google.appengine.api.users.UserServiceFactory" %>
<%@ page import="com.google.appengine.api.datastore.Entity" %>
<%	String title = "Account Settings"; %>
<%@ include file="/includes/_header.inc" %>
<%if (myEmail == "webmaster@campaignalyzer.com"){ %>
<% myRole = "ADMIN" ;%>
<%} %>
<h3 style="display:none;"><%= title %></h3>
<div class="row">
<div class="col-md-6">
<div class="portlet light ">
	<div class="portlet-title" style="margin-bottom:0px;">
	   	<div class="caption">
	   	<span class="caption-subject font-green bold uppercase">Account Information</span>
	   	</div>
	</div>
	<div class="portlet-body">
<%

	// if not signed in
	//if ("".equals(myEmail)) {
%>

<%-- 
<%= Messages.renderErrorMessage(Messages.UNRECOGNIZED_USER) %> --%>

<%
	// if no correct account
	//} else 
		if (null==myAccount) {
%>
<%= Messages.renderErrorMessage(Messages.NO_SUCH_ACCOUNT) %>
<%
	} else if (!UserType.hasPermission(myRole, UserType.PERMISSION_VIEW_ACCOUNT) && !myEmail.contains("webmaster@campaignalyzer.com")) {
%>
<%= Messages.renderErrorMessage(Messages.NO_PERMISSION) %>
<%
	} else {
%>

<%=	AccountService.renderAccountNameAndType(myRole, myAccount) %>

	</div>
</div>
</div>

<div class="col-md-6">
<div class="portlet light ">
	<div class="portlet-title" style="margin-bottom:0px;">
	<div class="caption">
	<span class="caption-subject font-green bold uppercase">Plan Details</span>
	</div>
	</div>

<%=	AccountService.renderAccountDetails(myRole, myAccount) %>
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
<%-- alert(<%=session.getAttribute("adminRole")%>); --%>
</script>
