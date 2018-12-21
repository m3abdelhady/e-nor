<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.Arrays" %>
<%@ page import="me.heuristic.util.HtmlUtil" %>
<%@ page import="me.heuristic.references.Messages" %>
<%@ page import="me.heuristic.references.UserType" %>
<%@ page import="me.heuristic.services.AccountService" %>
<%@ page import="me.heuristic.services.MobileCampaignService" %>
<%-- <%@ page import="com.google.appengine.api.users.User" %>
<%@ page import="com.google.appengine.api.users.UserService" %>
<%@ page import="com.google.appengine.api.users.UserServiceFactory" %> --%>
<%@ page import="com.google.appengine.api.datastore.Entity" %>
<%	String title = ""; %>
<%@ include file="/includes/_header.inc" %>
<!-- <style>
.select2-results__options li:first-child {height:40px; line-height:30px; Border:solid 1px #ccc; background:#eee; font-weight:bold;}
</style> -->
<%
	//long longCampaignId = HtmlUtil.handleNullforLongNumbers(request.getParameter("cid"));

	// if new campaign
	if (0==longCampaignId) { title = "Create New Mobile Campaign";
%>

<%
} else {title = "Edit Mobile Campaign";}
%>
<h3 style="display:none;"><%= title %></h3>

<div class="row">
<div class="col-md-6">
<div class="portlet light ">
	<div class="portlet-title" style="margin-bottom:0px;">
	<div class="caption">
<span class="caption-subject font-green bold uppercase"><%= title %></span>
</div>
	</div>
	<div class="mws-panel-body">
<%
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
	// check permission
	} else if (!UserType.hasPermission(myRole, UserType.PERMISSION_MANAGE_CAMPAIGNS)) {
%>
<%= Messages.renderErrorMessage(Messages.NO_PERMISSION) %>
<%
	} else {
		Entity myMobileCampaign = null;

		String packageName = "";
		String utm_campaign = "";
		String utm_medium = "";
		String utm_source = "";
		String utm_notes = "";
	
		String[] errors = request.getParameterValues("err");
		
		// if errors exist
		if (null!=errors) {
			//reload submitted data
			packageName = HtmlUtil.handleNullWhiteSpaces(request.getParameter("packageName"));
			utm_campaign = HtmlUtil.handleNullWhiteSpaces(request.getParameter("utm_campaign"));
			utm_medium = HtmlUtil.handleNullWhiteSpaces(request.getParameter("utm_medium"));
			utm_source = HtmlUtil.handleNullWhiteSpaces(request.getParameter("utm_source"));
			utm_notes = HtmlUtil.handleNullWhiteSpaces(request.getParameter("utm_notes"));
%>
<%= Messages.renderMessages(Arrays.asList(errors), Messages.STYLE_ERROR) %>
<%
		} else if (0!=longCampaignId) {
			// retrieve entity data
			myMobileCampaign = MobileCampaignService.getSingleMobileCampaign(longAccountId, longCampaignId);
			packageName = (String) myMobileCampaign.getProperty("packageName");
			utm_campaign = (String) myMobileCampaign.getProperty("utm_campaign");
			utm_medium = (String) myMobileCampaign.getProperty("utm_medium");
			utm_source = (String) myMobileCampaign.getProperty("utm_source");
			utm_notes = (String) myMobileCampaign.getProperty("utm_notes");
		}
		
		else{
		
		packageName = request.getParameter("packageName");
		utm_campaign = request.getParameter("utm_campaign") + " copy";
		utm_medium = request.getParameter("utm_medium");
		utm_source = request.getParameter("utm_source");
		utm_notes = request.getParameter("utm_notes");
		
		if(null == packageName){
		packageName = "";
		}
		
		if(null== request.getParameter("utm_campaign")){
		utm_campaign = "";
		}	
		
		if(null== utm_notes){
		utm_notes = "";
		}		
		if(null== utm_source){
		utm_source = "";
		}
		
		}
%>
<%= MobileCampaignService.renderMobileCampaignForm(myAccount, longCampaignId, packageName, utm_campaign, utm_medium, utm_source, utm_notes) %>
<%
	}
%>
	</div>
</div>
</div>
</div>
<%= HtmlUtil.renderFooter() %>
<script type="text/javascript">
var campaignSelectBox = $("#utm_campaign");
$(".select2").change(function(){
	if($(".select2").val().indexOf("Create New Campaign") == 0){
		
	$('select.select2').replaceWith('<input class="form-control sCampaignBox" type="text" name="utm_campaign" id="utm_campaign" autofocus>');
	$('.select2').hide();
	}
	});

$(".scampaign").click(function(){
	$('.sCampaignBox').replaceWith($('select.select2'));
	
});

$("#eutm_campaign").change(function(){
	$("#utm_campaign").val($("#eutm_campaign").val())
	});
</script>
