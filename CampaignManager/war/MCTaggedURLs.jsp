<%@page import="me.heuristic.services.MobileCampaignService"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.Arrays" %>
<%@ page import="me.heuristic.util.HtmlUtil" %>
<%@ page import="me.heuristic.references.Messages" %>
<%@ page import="me.heuristic.references.UserType" %>
<%@ page import="me.heuristic.services.AccountService" %>
<%@ page import="me.heuristic.services.MobileCampaignService" %>
<%@ page import="me.heuristic.services.MCTaggedUrlService" %>
<%@ page import="com.google.appengine.api.users.User" %>
<%@ page import="com.google.appengine.api.users.UserService" %>
<%@ page import="com.google.appengine.api.users.UserServiceFactory" %>
<%@ page import="com.google.appengine.api.datastore.Entity" %>
<%	String title = "Mobile Apps Tagged URLs"; %>
<%@ include file="/includes/_header.inc" %>
<script type="text/javascript">
function copyToClipboard(element) {
	  var $temp = $("<input>");
	  $("body").append($temp);
	  $temp.val($(element).text()).select();
	  document.execCommand("copy");
	  $temp.remove();
	}
	

$(document).ready(function() {
    $('#uniform-exportAll').click(function(event) {  //on click  
			if($('#uniform-exportAll span').hasClass('checked')){ 
			$('.focus span').toggleClass('checked');	
            $('.checkboxes').attr( "checked","checked");  //select all checkboxes with class "checkbox1"
            $('.checker span').toggleClass('checked');
    }
    
			else{
			$('.checkboxes').removeAttr( "checked");  //select all checkboxes with class "checkbox1"
            $('.checker span').toggleClass('checked');
            $('.focus span').toggleClass('checked');
			}
    });
    
    $('.checked').click(function(){
    	$('.checked span').toggleClass('checked');
    	$('.checkboxes').removeAttr( "checked");
    })
    
});
</script>



<style>
.cdetails li{min-width:25%;}
td{word-break:break-all; min-width: 150px; max-width:400px;}
</style>
<%

Entity myMobileCampaign = null;
Entity taggedUrl = null;

//long longCampaignId = HtmlUtil.handleNullforLongNumbers(request.getParameter("cid"));
long longTaggedUrlId = HtmlUtil.handleNullforLongNumbers(request.getParameter("tid"));
long longAccountIdn = HtmlUtil.handleNullforLongNumbers(request.getParameter("aid"));

//long longCampaignId = HtmlUtil.handleNullforLongNumbers(request.getParameter("cid"));
if (0!=longCampaignId) {
	myMobileCampaign = MobileCampaignService.getSingleMobileCampaign(longAccountId, longCampaignId);
}

if (0!=longTaggedUrlId) {
	taggedUrl = MCTaggedUrlService.getSingleTaggedUrl(longAccountId, longCampaignId, longTaggedUrlId);
}
%>


<h3 style="float:left; width:100%;"><span style='display:none;'><%= title %>:</span> <span style='float:right' class='btn green cds'>Show Campaign Details</span> <span style='display:none; float:right' class='btn green cdh'>Hide Campaign Details</span></h3>

<div class="row">
<div class="col-md-12 cd" style='display:none;'>
<div class="portlet light ">

	<div class="portlet-title">
	<div class="caption">
	   	<span class="caption-subject font-green bold uppercase">Campaign</span>
	</div>   	
	</div>
	<div class="portlet-body">
<%	

	// if not signed in
	if ("".equals(myEmail)) {
%>
<% response.sendRedirect("/signin.jsp?continuer=MCTaggedURL.jsp"); %>
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
	} else if (null==myMobileCampaign) {
%>
<%= Messages.renderErrorMessage(Messages.NO_SUCH_CAMPAIGN) %>
<%
	// check permission
	} else if (!UserType.hasPermission(myRole, UserType.PERMISSION_VIEW_CAMPAIGNS) && !UserServiceFactory.getUserService().isUserAdmin()) {
%>
<%= Messages.renderErrorMessage(Messages.NO_PERMISSION) %>
<%
	// check permission
	} else if (!UserType.hasPermission(myRole, UserType.PERMISSION_VIEW_TAGGED_URLS) && !UserServiceFactory.getUserService().isUserAdmin()) {
%>
<%= Messages.renderErrorMessage(Messages.NO_PERMISSION) %>
<%
	} else {
%>
<%= MobileCampaignService.renderMobileCampaignDetails(myRole, myMobileCampaign) %>

	</div>
</div></div>


<div class="row">
<div class="col-md-12">
<div class="portlet light ">

	<div class="portlet-title">
	<div class="caption">
	   	<span class="caption-subject font-green bold uppercase">Campaign Tagged Urls</span>
	 </div>  	
	</div>
	<div class="portlet-body">
<%
		String packageName = "";
		String utm_url = "";
		String utm_term = "";
		String utm_content = "";
		String utm_extra = "";
	
		String[] errors = request.getParameterValues("err");
		
		if (null!=errors) {
			//packageName = HtmlUtil.handleNullWhiteSpaces(request.getParameter("packageName"));
			utm_url = HtmlUtil.handleNullWhiteSpaces(request.getParameter("utm_url"));
			utm_term = HtmlUtil.handleNullWhiteSpaces(request.getParameter("utm_term"));
			utm_content = HtmlUtil.handleNullWhiteSpaces(request.getParameter("utm_content"));
			utm_extra = HtmlUtil.handleNullWhiteSpaces(request.getParameter("utm_extra"));
			packageName = HtmlUtil.handleNullWhiteSpaces(request.getParameter("packageName"));
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
<% 
if(0!= longTaggedUrlId){
 %>
 <%= MCTaggedUrlService.renderTaggedUrlFormRow(longAccountIdn, longCampaignId, taggedUrl,longTaggedUrlId) %>
<%
	} else{
%>
<%= MCTaggedUrlService.renderNewTaggedUrlFormRow(longAccountId, longCampaignId) %>
<%= MCTaggedUrlService.renderTaggedUrlListForm(myRole, myAccount, myMobileCampaign, longTaggedUrlId, utm_url, utm_term, utm_content, utm_extra) %>
<%} %>

<%
	}
%>
	</div>
</div>
</div></div></div>
<%= HtmlUtil.renderFooter() %>
<script type="text/javascript">
$('.cds').click(function(){$( ".cd" ).toggle(100,"swing"); $( ".cdh" ).toggle(); $( ".cds" ).toggle();})
$('.cdh').click(function(){$( ".cd" ).toggle(100,"swing"); $( ".cds" ).toggle(); $( ".cdh" ).toggle();})</script>


<script type="text/javascript">
$(".note").click(function(){
	$(this).animate({opacity:0}, function() {
		$(this).slideUp("medium", function() {
			$(this).css("opacity", '');
		});
	});
	
});

$(document).ready(function() {
    $('#deletAll').click(function(event) {  //on click 
        if(this.checked) { // check select status
            $('.checkboxes').each(function() { //loop through each checkbox
                this.checked = true;  //select all checkboxes with class "checkbox1"               
            });
        }else{
            $('.checkboxes').each(function() { //loop through each checkbox
                this.checked = false; //deselect all checkboxes with class "checkbox1"                       
            });         
        }
    });
    
});
</script>
