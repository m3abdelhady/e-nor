<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.Arrays" %>
<%@ page import="me.heuristic.util.HtmlUtil" %>
<%@ page import="me.heuristic.references.Messages" %>
<%@ page import="me.heuristic.references.AccountType" %>
<%@ page import="me.heuristic.references.UserType" %>
<%@ page import="me.heuristic.services.AccountService" %>
<%@ page import="me.heuristic.services.CampaignService" %>
<%-- <%@ page import="com.google.appengine.api.users.User" %>
<%@ page import="com.google.appengine.api.users.UserService" %>
<%@ page import="com.google.appengine.api.users.UserServiceFactory" %> --%>
<%@ page import="com.google.appengine.api.datastore.Entity" %>
<%	String title = "Import Campaigns"; %>
<%@ include file="/includes/_header.inc" %>
<h3 style='display:none;'><%= title %></h3>

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
<%-- <% response.sendRedirect("/signin.jsp"); %> --%>
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
	} else if (!AccountType.allowsImport((String) myAccount.getProperty("type"))) {
%>
<%= Messages.renderErrorMessage(Messages.NO_IMPORT_ALLOWED) %>
<%
	// check user permissions
	} else if (!UserType.hasPermission(myRole, UserType.PERMISSION_IMPORT_CAMPAIGNS)) {
%>
<%= Messages.renderErrorMessage(Messages.NO_PERMISSION) %>
<%
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
<%= CampaignService.renderCampaignImportForm(longAccountId) %>
<%
	}
%>
	</div>
</div>
</div>

<div class="col-md-6">
<div class="portlet light ">

	<div class="portlet-title">
	<div class="caption">
	   	<span class="caption-subject font-green bold uppercase">Campaign Templates Center</span>
	</div>   	
	</div>
	<div class="portlet-body">
			<P>The easiest way to import campaigns is to export a CSV file from our templates center and simply edit the exported file. Please follow the process below.</P>
			<ol>
			<li><b>Export template.</b><br/>
			<p>Click on the “Download Template” button below</p></li>
			<li><b>Include a column header row.</b><br/>
			<p>CampaignAlyzer treats the first row as a column header row. Please insure to include the following column headers: campaign, medium, source, content, term, extra parameter, url</li>
			<li><b>List each item on its own row.</b><br/>
			<p>CampaignAlyzer requires that each row in your CSV or TSV file describes a single entity within a campaign. For example, if you are sending your visitors to different landing pages within the same campaign, each URL should be listed separately.</li>
			<li><b>Save file as CSV.</b><br/>
			<p>Be sure your file is saved in one of the supported formats: CSV (Comma Separated Values), TSV (Tab Separated Values), or CSV for Excel. CampaignAlyzer will not import your file if it's saved in the wrong format or encoding; for example, CampaignAlyzer does not import Microsoft Excel (.xls, .xlsx) formatted files so if you edit it in Excel make sure to Save As csv.</li>
			<li><b>Import</b><br/>
			<p>Once you have prepared your CSV file, follow these steps to import it:<br/>
			<ul>
				<li>Select Import Campaigns from the side bar menu.</li>
				<li>Browse to load your file.</li>
				<li>Click Import.</li>
			</ul></li>
			</ol>
		</div>
	   <form class="mws-form" action="/editcampaign">
		<div class="mws-button-row">
			<input type="button" value=" Download Template " class="btn green" onClick="window.open('https://www.campaignalyzer.com/support/templates/')"/>
		</div>
		</form>
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
