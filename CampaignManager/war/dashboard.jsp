<%@page import="me.heuristic.services.MobileCampaignHolderService"%>
<%@page import="java.util.Iterator"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.Arrays" %>
<%@ page import="me.heuristic.util.HtmlUtil" %>
<%@ page import="me.heuristic.references.Messages" %>
<%@ page import="me.heuristic.services.AccountService" %>
<%@ page import="me.heuristic.services.CampaignHolderService" %>
<%-- <%@ page import="com.google.appengine.api.users.User" %>
<%@ page import="com.google.appengine.api.users.UserService" %>
<%@ page import="com.google.appengine.api.users.UserServiceFactory" %>
<%@ page import="com.google.appengine.api.oauth.OAuthServiceFactory" %> 
<%@ page import="com.google.appengine.api.datastore.Entity" %>--%>
<%	String title = "Dashboard"; %>
<%@ include file="/includes/_header.inc" %>
<style>
.page-bar, .action, .mws-form, .dataTables_wrapper .row,.form-group,.chekboxTD{display:none;}
.no-footer{border:none !important;}
.table-scrollable{border-right:none; !important;}

/* .sorting_asc, .sorting, .sorting_desc{background:none !important;} */
</style>
<h3 style="display:none"><%= title %></h3>
<div class="portlet light">
	<div class="portlet-title" >
	<div class="caption">
	   	<span class="caption-subject font-green bold uppercase"><%= title %></span>
	</div>
	<div class="theme-panel">
	
<%-- 	<div class=" tooltips" data-container="body" data-placement="left" data-html="true" data-original-title="<%= AccountService.getTrailInfo(longAccountId) %>" style="top: 15px;
    right: 42px;
    height: 40px;
    width: 40px;
    border-radius: 50%!important;
    cursor: pointer;
    position: absolute;
    text-align: center;
    background-color: #fff;display: block;">
	
	<i class="icon-settings"> </i>
	</div> --%>
	                                                           
	                                                           
	       
	
                        <div class="settingsToggler tooltips" data-container="body" data-placement="left" data-html="true" data-original-title="Account Settings" style="display: block;">                       
                          <i class="icon-settings"><a href="/ViewAccount.jsp?aid=<%= longAccountId%>" style="
    position: absolute;
    top: 0;
    display: block;
    height: 40px;
    width: 40px;
    left: 0;"></a></i>
                        </div>                    
                       
                    </div>
	</div>
	<div class="mws-panel-body">
	
	<%long longAccountIds = HtmlUtil.handleNullforLongNumbers(request.getParameter("aid"));%>
<%

	// if not signed in
	if ("".equals(myEmail)) {
%>


 <% response.sendRedirect("/signin.jsp"); %>

<%= Messages.renderErrorMessage(Messages.UNRECOGNIZED_USER) %>

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
	
<div class="row">
                        <div class="col-lg-3 col-md-3 col-sm-6 col-xs-12">
                            <div class="dashboard-stat blue">
                                <div class="visual">
                                    <i class="fa fa fa-folder-open"></i>
                                </div>
                                <div class="details">
                                <div class="number"> Active Accounts </div>
                                    <div class="desc">
                                        <span data-counter="counterup" data-value="1349"><%= AccountService.getAllAccountsCountFor(myEmail) %></span>
                                    </div>
                                </div>
                                <a class="more" href="\ManageAccounts.jsp"> View more
                                    <i class="m-icon-swapright m-icon-white"></i>
                                </a>
                            </div>
                        </div>
                        <div class="col-lg-3 col-md-3 col-sm-6 col-xs-12">
                            <div class="dashboard-stat red">
                                <div class="visual">
                                    <i class="fa fa-bar-chart-o"></i>
                                </div>
                                <div class="details">
                                 <div class="number">Campaigns</div>
                                    <div class="desc">
                                        <%= CampaignHolderService.getAllCampaignHoldersCountFor(longAccountId) + MobileCampaignHolderService.getAllMobileCampaignHoldersCountFor(longAccountId) %> </div>
                                   
                                </div>
                                <a class="more" href="/ManageCampaignHolders.jsp?aid=<%=longAccountIds%>"> View more
                                    <i class="m-icon-swapright m-icon-white"></i>
                                </a>
                            </div>
                        </div>
                        <div class="col-lg-3 col-md-3 col-sm-6 col-xs-12">
                            <div class="dashboard-stat green">
                                <div class="visual">
                                    <i class="fa fa-tag"></i>
                                </div>
                                <div class="details">
                                    
                                    <div class="number"> <span data-counter="counterup">+ New Campaign </span></div>
                                </div>
                                <a class="more" href="/EditCampaign.jsp?aid=<%=longAccountIds%>"> Add Now
                                    <i class="m-icon-swapright m-icon-white"></i>
                                </a>
                            </div>
                        </div>
                        <div class="col-lg-3 col-md-3 col-sm-6 col-xs-12">
                            <div class="dashboard-stat purple">
                                <div class="visual">
                                    <i class="fa fa-info-circle"></i>
                                </div>
                                <div class="details">
                                    <div class="number"> 
                                        <span data-counter="counterup">Support</span></div>
                                    <div class="desc"> Help Center </div>
                                </div>
                                <a class="more" href="https://campaignalyzer.com/support" target="_blank"> View more
                                    <i class="m-icon-swapright m-icon-white"></i>
                                </a>
                            </div>
                        </div>
                    </div>


<div class='row'>
<div class='col-md-12'><hr></div>
</div>

<div class='row'>
<div class='col-md-6'>
<div class="portlet light bordered">
	<div class="portlet-title" >
	<div class="caption">
	<i class="icon-bar-chart font-green-haze"></i>
	<span class="caption-subject bold uppercase font-green-haze">Recent Campaigns</span>
	</div>
<div class="actions">
<a href="/EditCampaign.jsp?aid=<%=longAccountIds%>" class="btn btn-transparent dark btn-circle btn-sm red">New Campaign</a>
<a href="/ManageCampaignHolders.jsp?aid=<%=longAccountIds%>" class="btn btn-transparent dark btn-outline btn-circle btn-sm red">View More</a></div>
</div>
<%= CampaignHolderService.renderCampaignHolderList(myRole, longAccountIds)%>
</div>
</div>




<div class='col-md-6'>
<div class="portlet light bordered">
	<div class="portlet-title" >
	<div class="caption">
	<i class="icon-briefcase font-red-haze"></i>
	<span class="caption-subject font-red bold uppercase">Accounts</span>
	</div>
	<div class="actions">
<a href="/ManageAccounts.jsp" class="btn btn-transparent dark btn-outline btn-circle btn-sm red">View More</a>
</div></div>
<%= AccountService.renderAccountList(myEmail) %>

</div></div></div>
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
var aid= $('#aid').val();



</script>
