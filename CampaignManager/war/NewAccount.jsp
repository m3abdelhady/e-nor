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
<%	String title = ""; %>

<%@ include file="/includes/_header.inc" %>
<style>
.backstretch img {
    top: 0 !important;
}
.backstretch {
    /* height: 800px !important; */
}
html, body{height:100%; background:#fff;}
.login-bg{height:100vh;}
.page-footer{position:absolute; bottom:10px}

.page-bar,.page-sidebar-wrapper, .page-header.navbar .page-top,.page-header.navbar,.portlet-title{display:none;}
.page-content-wrapper .page-content, .row, .col-md-12 h2,.portlet.light{padding:0px 0px; margin:0; background:#fff;}
.page-header-fixed .page-container{margin-top:0}

.mws-button-row, .mws-form-inline{padding-left:15px;}
.blackLogo {
    /* padding: 0px 12px 50px; */
   padding: 0px 12px 0px;
}
/* hr{margin:10px; !important} */
h2{margin-left:12px;}
p{padding: 10px 0px 0px 0px !important;
    margin: 0px 0 10px 0px !important;}

</style>
<% myEmail = HtmlUtil.handleNullWhiteSpaces(session.getAttribute("userEmail").toString()); %>
<% myPhoto = HtmlUtil.handleNullWhiteSpaces(session.getAttribute("userPhoto").toString());%>
<%
	//long longCampaignId = HtmlUtil.handleNullforLongNumbers(request.getParameter("aid"));

	// if new campaign
	if (0==longCampaignId) { title = "Create New Account";
%>

<%
} else {title = "Edit Account";}
%>

<h3 style="display:none"><%= title %></h3>
<div class="row">
<div class="col-md-6">
<div class="top-menu">
                        <ul class="nav navbar-nav pull-right">                            
                            <!-- BEGIN USER LOGIN DROPDOWN -->
                            <!-- DOC: Apply "dropdown-dark" class after below "dropdown-extended" to change the dropdown styte -->
                             <%if(myPhoto == null || myPhoto == ""){%>
                                 
                             <li style="float:left; padding:15px 0">
                             <img src="/images/default_avatar.png" style="width:39px; border-radius:100% !important" id="defaultAvatar"></li>
                            <%}else{%>                                                        
                            <li style="float:left; padding:15px 0">
                             
                              <img src="<%=myPhoto%>" style="width:39px; border-radius:100% !important" id="profilePhoton"></li>
                             <%}%>
                             <li style="float:left" class="dropdown dropdown-user">                            
                               <%=HtmlUtil.renderWelcomeBox(myEmail, myRole," # onclick='signOut();'","/signin.jsp")%>                                
                            </li>
                            <li style="float:left; padding:15px 0" title="Logout"><a class="" href="#" onclick="signOut();" style="
    padding: 10px;
"><i class="icon-logout"></i></a></li>
                            
                         
                            <!-- END USER LOGIN DROPDOWN -->
                            
                            <!-- BEGIN QUICK SIDEBAR TOGGLER -->
                            <!-- DOC: Apply "dropdown-dark" class after below "dropdown-extended" to change the dropdown styte -->
                           
                            <!-- END QUICK SIDEBAR TOGGLER -->
                        </ul>
                    </div>
<div class="portlet light ">
	<div class="portlet-title" style="margin-bottom:0px;">
<div class="caption">
	<span class="caption-subject font-green bold uppercase"><%= title %></span>
	</div>
	</div>
	<div class="portlet-body">
<div class="blackLogo"><img src="/images/logo-black.png"/></div>
<%
	// if not signed in
	if ("".equals(myEmail)) {
%>
<% response.sendRedirect("/signin.jsp?continuer=EditAccount"); %>
<%-- <%= Messages.renderErrorMessage(Messages.UNRECOGNIZED_USER) %> --%>
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
	} else if (0!=longAccountId && !UserType.hasPermission(myRole, UserType.PERMISSION_MANAGE_ACCOUNT) && !UserServiceFactory.getUserService().isUserAdmin()) {
%>
<%= Messages.renderErrorMessage(Messages.NO_PERMISSION) %>
<%
	} else {
		// prepare fields for form
		String emailAddress = "";
		String firstName = "";
		String lastName = "";
		String company = "";
		String accountName = "";
		String address = "";
		String city = "";
		String state = "";
		String zip = "";
		String country = "";
		String phone = "";
		String website = "";
		String agreement = "";
		
		
		String[] errors = request.getParameterValues("err");
		
		// if errors exit
		if (null!=errors) {
			//reload submitted data
			emailAddress = HtmlUtil.handleNullWhiteSpaces(request.getParameter("email"));
			firstName = HtmlUtil.handleNullWhiteSpaces(request.getParameter("fname"));
			lastName = HtmlUtil.handleNullWhiteSpaces(request.getParameter("lname"));
			company = HtmlUtil.handleNullWhiteSpaces(request.getParameter("company"));
			accountName = HtmlUtil.handleNullWhiteSpaces(request.getParameter("name"));
			address = HtmlUtil.handleNullWhiteSpaces(request.getParameter("address"));
			city = HtmlUtil.handleNullWhiteSpaces(request.getParameter("city"));
			state = HtmlUtil.handleNullWhiteSpaces(request.getParameter("state"));
			zip = HtmlUtil.handleNullWhiteSpaces(request.getParameter("zip"));
			country = HtmlUtil.handleNullWhiteSpaces(request.getParameter("country"));
			phone = HtmlUtil.handleNullWhiteSpaces(request.getParameter("phone"));
			website = HtmlUtil.handleNullWhiteSpaces(request.getParameter("website"));
			agreement = HtmlUtil.handleNullWhiteSpaces(request.getParameter("agreement"));
			
%>
<%= Messages.renderMessages(Arrays.asList(errors), Messages.STYLE_ERROR) %>
<%
		} else if (0!=longAccountId) {
			// retrieve entity data
			emailAddress = (String) myAccount.getProperty("email");
			firstName = (String) myAccount.getProperty("fname");
			lastName = (String) myAccount.getProperty("lname");
			company = (String) myAccount.getProperty("company");
		    accountName = (String) myAccount.getProperty("name");
			address = (String) myAccount.getProperty("address");
			city = (String) myAccount.getProperty("city");
			state = (String) myAccount.getProperty("state");
			zip = (String) myAccount.getProperty("zip");
			country = (String) myAccount.getProperty("country");
			phone = (String) myAccount.getProperty("phone");
			website = (String) myAccount.getProperty("website");
			agreement = (String) myAccount.getProperty("agreement");
		}
%>
<%= AccountService.renderAccountForm(myEmail, longAccountId, emailAddress, firstName, lastName, company, accountName, address, city, state, zip, country, phone, website, agreement) %>
<%
	}
%>
	</div>
</div>
<!-- Start Footer -->
<div class="page-footer" style="position:relative; bottom:0"><div class="page-footer-inner">© Copyright 2017 CampaignAlyzer. All rights reserved. | <a target="_blank" href="https://campaignalyzer.com/">Home</a> | <a target="_blank" href="https://campaignalyzer.com/terms">Terms Of Use</a> | <a target="_blank" href="https://campaignalyzer.com/privacy-policy">Privacy Policy</a> | <a target="_blank" href="https://campaignalyzer.com/contact-us">Contact Us</a>
	</div>
</div>
<!-- End Footer -->
</div>
<div class="col-md-6" style="padding:0; margin:0">
                    <div style="position:relative;height:100vh;background: url('/assets/pages/img/login/banner.png') no-repeat; background-size:cover;"> </div>
                </div>
</div>

<script>
if(getParameterByName("pid") != null){
$("input[name='pid']").val(getParameterByName("pid"));
}</script>

<script type="text/javascript">
$(".note").click(function(){
	$(this).animate({opacity:0}, function() {
		$(this).slideUp("medium", function() {
			$(this).css("opacity", '');
		});
	});
	
});

if(!$("#aid").length){$("#dashboardlink").hide()}
$(".has-error").change(function(){
	$(this).removeClass('has-error');
	});
	
	
<!-- To Be Deleated -->

<!-- To Be Deleated -->
	
</script>
<script type='text/javascript'>print_country('country')</script>
<Script>$("#country").val("USA").change();</Script>
<script src='https://apis.google.com/js/platform.js?onload=onLoad' async defer></script>
    	
<script type='text/javascript'>defautAvatar();</script>
<iframe name='caResponse' width='300' height='200' style='display:none'></iframe>
<iframe name='actonResponse' width='300' height='200' style='display:none'></iframe>
</body></html>
