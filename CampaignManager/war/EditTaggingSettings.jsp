<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.Arrays" %>
<%@ page import="me.heuristic.util.HtmlUtil" %>
<%@ page import="me.heuristic.references.Messages" %>
<%@ page import="me.heuristic.references.UserType" %>
<%@ page import="me.heuristic.references.TaggingParameters" %>
<%@ page import="me.heuristic.services.AccountService" %>
<%@ page import="me.heuristic.services.TaggingSettingsService" %>
<%@ page import="me.heuristic.services.TaggingParametersService" %>
<%-- <%@ page import="com.google.appengine.api.users.User" %>
<%@ page import="com.google.appengine.api.users.UserService" %>
<%@ page import="com.google.appengine.api.users.UserServiceFactory" %> --%>
<%@ page import="com.google.appengine.api.datastore.Entity" %>
<%	String title = "Tagging Settings"; %>
<%@ include file="/includes/_header.inc" %>
<h3 style="display:none;"><%= title %></h3>
<div class="row">
<div class="col-md-6">
<div class="portlet light ">
	<div class="portlet-title">
	<div class="caption">
	   	<span class="caption-subject font-green bold uppercase"><%= title %></span>
	</div>   	
	</div>
	<div class="portlet-body">
<%
	// if not signed in
	if ("".equals(myEmail)) {
%>
<% response.sendRedirect("/signin.jsp"); %>
<%-- <%= Messages.renderErrorMessage(Messages.UNRECOGNIZED_USER) %> --%>
<%
	// if no correct account
	} else	if (null==myAccount) {
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
	// check permission
	} else if (!UserType.hasPermission(myRole, UserType.PERMISSION_EDIT_TAGGING_SETTINGS)) {
%>
<%= Messages.renderErrorMessage(Messages.NO_PERMISSION) %>
<%
	} else {
		String conAttribution = "";
		String parSeparator = "";
		String urlShortener = "";
		String caseSensetive = "";
		
		String[] errors = request.getParameterValues("err");
		
		if (null!=errors) {
			// reload submitted data
			conAttribution = HtmlUtil.handleNullWhiteSpaces(request.getParameter("conAttribution"));
			parSeparator = HtmlUtil.handleNullWhiteSpaces(request.getParameter("parSeparator"));
			urlShortener = HtmlUtil.handleNullWhiteSpaces(request.getParameter("urlShortener"));
			caseSensetive = HtmlUtil.handleNullWhiteSpaces(request.getParameter("caseSensetive"));
		} else {
			// retrieve entity data
			conAttribution = (String) myAccount.getProperty("conAttribution");
			parSeparator = (String) myAccount.getProperty("parSeparator");
			urlShortener = (String) myAccount.getProperty("urlShortener");
			caseSensetive = (String) myAccount.getProperty("caseSensetive");
		}
%>
<%= TaggingSettingsService.renderTaggingSettingsForm(longAccountId, conAttribution, parSeparator, urlShortener, caseSensetive)  %>
	</div>
</div>
</div>


<div class="col-md-6">
<div class="portlet light ">

	<div class="portlet-title">
	<div class="caption">
	   	<span class="caption-subject font-green bold uppercase">Campaign Tracking Variables</span>
	</div>
	</div>
	<div class="portlet-body">
<%
		String campaignParam = "";
		String mediumParam = "";
		String sourceParam = "";
		String termParam = "";
		String contentParam = "";
		String queryParam = "";
		
		if (null!=errors) {
			campaignParam = HtmlUtil.handleNullWhiteSpaces(request.getParameter("campaignParam"));
			mediumParam = HtmlUtil.handleNullWhiteSpaces(request.getParameter("mediumParam"));
			sourceParam = HtmlUtil.handleNullWhiteSpaces(request.getParameter("sourceParam"));
			termParam = HtmlUtil.handleNullWhiteSpaces(request.getParameter("termParam"));
			contentParam = HtmlUtil.handleNullWhiteSpaces(request.getParameter("contentParam"));
			queryParam = HtmlUtil.handleNullWhiteSpaces(request.getParameter("queryParam"));
%>
<%= Messages.renderMessages(Arrays.asList(errors), Messages.STYLE_ERROR) %>
<%
		} else {
			campaignParam = (String) myAccount.getProperty("campaignParam");
			if (null==campaignParam) campaignParam = TaggingParameters.DEFAULT_CAMPAIGN_PARAMETER;

			mediumParam = (String) myAccount.getProperty("mediumParam");
			if (null==mediumParam) mediumParam = TaggingParameters.DEFAULT_MEDIUM_PARAMETER;

			sourceParam = (String) myAccount.getProperty("sourceParam");
			if (null==sourceParam) sourceParam = TaggingParameters.DEFAULT_SOURCE_PARAMETER;

			termParam = (String) myAccount.getProperty("termParam");
			if (null==termParam) termParam = TaggingParameters.DEFAULT_TERM_PARAMETER;

			contentParam = (String) myAccount.getProperty("contentParam");
			if (null==contentParam) contentParam = TaggingParameters.DEFAULT_CONTENT_PARAMETER;

			queryParam = (String) myAccount.getProperty("queryParam");
			if (null==queryParam) queryParam = TaggingParameters.DEFAULT_QUERY_PARAMETER;
		}

		String[] infos = request.getParameterValues("info");
		if (null!=infos) {
%>
<%=	Messages.renderMessages(Arrays.asList(infos), Messages.STYLE_INFO) %>
<%
		}
%>
<%= TaggingParametersService.renderTaggingParametersForm(longAccountId,
		campaignParam, mediumParam, sourceParam,
		termParam, contentParam, queryParam)  %>
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
