<%@ page import="me.heuristic.services.CampaignService" %>
<%@ page import="com.google.appengine.api.oauth.OAuthService"%>
<%@ page import="com.google.appengine.api.oauth.OAuthServiceFactory"%>
<%@ page import="me.heuristic.services.GoogleAuthHelper" %>
<%@ page import="me.heuristic.util.HtmlUtil" %>
<%@ page import="com.google.appengine.api.datastore.Entity" %>
<%@ page import="me.heuristic.services.AccountService" %>

	<%
	
	String myEmail = HtmlUtil.handleNullWhiteSpaces(session.getAttribute("userEmail").toString());
	String myPhoto = HtmlUtil.handleNullWhiteSpaces(session.getAttribute("userPhoto").toString());
	
		
	String myRole = "";
	Entity myAccount = null;	
	long longAccountId = HtmlUtil.handleNullforLongNumbers(request.getParameter("aid"));
	session.setAttribute("aid", longAccountId);
	long longCampaignId = HtmlUtil.handleNullforLongNumbers(request.getParameter("cid"));
		
	String cName = request.getParameter("cname");
	String newType= request.getParameter("newType");
	String ms = request.getParameter("ms");
	
	//UserService userService = UserServiceFactory.getUserService();
	//User user = userService.getCurrentUser();
	
	if (""!=myEmail) {
		if (0!=longAccountId) {
		    myAccount = AccountService.getSingleAccount(longAccountId);
		    if (null!=myAccount) {
		    	myRole = (String) myAccount.getProperty(myEmail);
		    }
		}	
		}		
		%>
<!DOCTYPE html>
<html>
<head>

<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="google-signin-scope" content="profile email https://www.googleapis.com/auth/analytics https://www.googleapis.com/auth/plus.login" >
<meta name="google-signin-client_id" content="458758013131-ib24rrpblt0ltk24enis2ggk189cq2o5.apps.googleusercontent.com">

<!-- Apple iOS and Android stuff (do not remove) -->
<meta name="apple-mobile-web-app-capable" content="no" />
<meta name="apple-mobile-web-app-status-bar-style" content="black" />

<meta name="viewport" content="width=device-width,initial-scale=1,user-scalable=no,maximum-scale=1" />

	<%=	HtmlUtil.renderTitle(title) %>
	
	<!-- implement Data Table by Morsi -->
	<script type="text/javascript" language="javascript" src="/assets/global/scripts/jquery.js"></script>
	<script type="text/javascript" language="javascript" src="/assets/global/scripts/dataTable/jquery.dataTables.js"></script>
	<script type="text/javascript" language="javascript" src="/resources/syntax/shCore.js"></script>
	<script type="text/javascript" language="javascript" src="/resources/demo.js"></script>
	<script type="text/javascript" language="javascript" class="init">
	

$(document).ready(function() {
	 $('.sortable').dataTable({    
});

	 $('.modified').dataTable({ 
	 "order": [[7, "desc"]]   
});

$('.modified4').dataTable({ 
	 "order": [[6, "desc"]]   
});

	 
	 
} );

	    
function goBack() {
    window.history.back();
}
 

	</script>
	
	
	

<!-- New Template Head Tags-->

 <link href="https://fonts.googleapis.com/css?family=Open+Sans:400,300,600,700&amp;subset=all" rel="stylesheet" type="text/css"/>
 <link href="/assets/global/css/campaignalyzer.css" rel="stylesheet" type="text/css"/>
        <link href="/assets/global/plugins/font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css"/>
        <link href="/assets/global/plugins/simple-line-icons/simple-line-icons.min.css" rel="stylesheet" type="text/css"/>
        <link href="/assets/global/plugins/bootstrap/css/bootstrap.min.css" rel="stylesheet" type="text/css"/>  
        <link href="../assets/global/plugins/bootstrap-fileinput/bootstrap-fileinput.css" rel="stylesheet" type="text/css"/>     
         <link href="/assets/global/css/pricing.min.css" rel="stylesheet" type="text/css"/>
         
        <!-- END GLOBAL MANDATORY STYLES -->
       
        <!-- BEGIN THEME GLOBAL STYLES -->
        <link href="/assets/global/css/components.min.css" rel="stylesheet" id="style_components" type="text/css"/>
        <link href="/assets/global/css/plugins.min.css" rel="stylesheet" type="text/css"/>
        <!-- END THEME GLOBAL STYLES -->
        <!-- BEGIN THEME LAYOUT STYLES -->
        <link href="/assets/layouts/layout2/css/layout.min.css" rel="stylesheet" type="text/css"/>
        <link href="/assets/layouts/layout2/css/themes/blue.min.css" rel="stylesheet" type="text/css" id="style_color"/>
        <link href="/assets/layouts/layout2/css/custom.min.css" rel="stylesheet" type="text/css"/>
        
        
       
       
        <link rel="shortcut icon" href="icon.png"><style type="text/css">.jqstooltip { position: absolute;left: 0px;top: 0px;visibility: hidden;background: rgb(0, 0, 0) transparent;background-color: rgba(0,0,0,0.6);filter:progid:DXImageTransform.Microsoft.gradient(startColorstr=#99000000, endColorstr=#99000000);-ms-filter: "progid:DXImageTransform.Microsoft.gradient(startColorstr=#99000000, endColorstr=#99000000)";color: white;font: 10px arial, san serif;text-align: left;white-space: nowrap;padding: 5px;border: 1px solid white;z-index: 10000;}.jqsfield { color: white;font: 10px arial, san serif;text-align: left;}</style>
		
		<link href="/assets/global/plugins/select2/css/select2.min.css" rel="stylesheet" type="text/css" />
        <link href="/assets/global/plugins/select2/css/select2-bootstrap.min.css" rel="stylesheet" type="text/css" />
        <link href="../assets/global/plugins/bootstrap-daterangepicker/daterangepicker.min.css" rel="stylesheet" type="text/css" />
        <link href="../assets/global/plugins/bootstrap-datepicker/css/bootstrap-datepicker3.min.css" rel="stylesheet" type="text/css" />
		
		<!-- implement Data Table by Morsi -->
	

		<!--Scripts -->
		
		<script src="../assets/global/plugins/jquery.min.js" type="text/javascript"></script>
        <script src="../assets/global/plugins/bootstrap/js/bootstrap.min.js" type="text/javascript"></script>
       
        <!-- END CORE PLUGINS -->
        <!-- BEGIN PAGE LEVEL PLUGINS -->
        <script src="../assets/global/scripts/datatable.js" type="text/javascript"></script>
        <script src="../assets/global/plugins/datatables/datatables.min.js" type="text/javascript"></script>
        <script src="../assets/global/plugins/datatables/plugins/bootstrap/datatables.bootstrap.js" type="text/javascript"></script>
        <!-- END PAGE LEVEL PLUGINS -->
        <!-- BEGIN THEME GLOBAL SCRIPTS -->
        <script src="../assets/global/scripts/app.min.js" type="text/javascript"></script>
        <!-- END THEME GLOBAL SCRIPTS -->
        <!-- BEGIN PAGE LEVEL SCRIPTS -->
        <script src="../assets/pages/scripts/table-datatables-managed.min.js" type="text/javascript"></script>
        <!-- BEGIN PAGE LEVEL PLUGINS -->
        <link href="../assets/global/plugins/datatables/datatables.min.css" rel="stylesheet" type="text/css" />
        <link href="../assets/global/plugins/datatables/plugins/bootstrap/datatables.bootstrap.css" rel="stylesheet" type="text/css" />
        <!-- END PAGE LEVEL PLUGINS -->
        <script src="../assets/layouts/layout2/scripts/layout.min.js" type="text/javascript"></script>
        <script src="../assets/layouts/layout2/scripts/demo.min.js" type="text/javascript"></script>
        <script src="../assets/layouts/global/scripts/quick-sidebar.min.js" type="text/javascript"></script>
        <script src="../assets/global/plugins/bootstrap-fileinput/bootstrap-fileinput.js" type="text/javascript"></script>       
        <script src="../assets/global/scripts/campaignalyzer.js" type="text/javascript"></script>
        <script src = "../assets/global/scripts/countries3.js" type= "text/javascript"></script>
        <script src="../assets/global/plugins/select2/js/select2.full.min.js" type="text/javascript"></script>
        <script src="../assets/pages/scripts/components-select2.min.js" type="text/javascript"></script>   
           
              
        <!--<script src="../assets/global/plugins/backstretch/jquery.backstretch.min.js" type="text/javascript"></script> -->
        <!--<script src="../assets/pages/scripts/form-validation.min.js" type="text/javascript"></script> -->
     	<!--<script src="../assets/global/plugins/jquery-validation/js/jquery.validate.min.js" type="text/javascript"></script> -->
     	
     	 <script src="../assets/global/plugins/moment.min.js" type="text/javascript"></script>
        <script src="../assets/global/plugins/bootstrap-daterangepicker/daterangepicker.min.js" type="text/javascript"></script>
        <script src="../assets/global/plugins/bootstrap-datepicker/js/bootstrap-datepicker.min.js" type="text/javascript"></script>
     	
<script src="https://cdnjs.cloudflare.com/ajax/libs/modernizr/2.8.2/modernizr.js"></script>
<script>
//paste this code under the head tag or in a separate js file.
	// Wait for window load
	$(window).load(function() {
		// Animate loader off screen
		$(".se-pre-con").fadeOut("slow");
	});
		 
		
		<!-- End Scripts -->
		</script>
		<script>
  function signOut() {
    var auth2 = gapi.auth2.getAuthInstance();
    auth2.signOut().then(function () {
      console.log('User signed out.');
      
      window.location.replace("/signin.jsp");
      
    });
  }
  function onLoad() {
      gapi.load('auth2', function() {
        gapi.auth2.init();
      });
    }
    ;

</script>

<!--GA-4.0 Tags-->

   <script src="https://apis.google.com/js/platform.js?onload=renderButton" async defer></script>              
   <!-- BEGIN Pre-requisites -->

  </script>
  <script src="https://apis.google.com/js/client:platform.js?onload=start" async defer>
  </script>
  
   <script>
    function start() {
      gapi.load('auth2', function() {
        auth2 = gapi.auth2.init({
          client_id: '458758013131-ib24rrpblt0ltk24enis2ggk189cq2o5.apps.googleusercontent.com',
          // Scopes to request in addition to 'profile' and 'email'
          //scope: 'https://www.googleapis.com/auth/analytics.readonly'
        });
      });
    }
  </script>  



<!--End GA-4.0 Tags -->

<!-- End New Template Head Tags-->



<!-- Include Required Prerequisites -->

<script type="text/javascript" src="//cdn.jsdelivr.net/momentjs/latest/moment.min.js"></script>

 
<!-- Include Date Range Picker -->
<script type="text/javascript" src="//cdn.jsdelivr.net/bootstrap.daterangepicker/2/daterangepicker.js"></script>
<link rel="stylesheet" type="text/css" href="//cdn.jsdelivr.net/bootstrap.daterangepicker/2/daterangepicker.css" />


</head>
<body class="page-header-fixed page-sidebar-closed-hide-logo page-container-bg-solid">
<!-- Google Tag Manager -->
<noscript><iframe src="//www.googletagmanager.com/ns.html?id=GTM-MBXDLW"
height="0" width="0" style="display:none;visibility:hidden"></iframe></noscript>
<script>(function(w,d,s,l,i){w[l]=w[l]||[];w[l].push({'gtm.start':
new Date().getTime(),event:'gtm.js'});var f=d.getElementsByTagName(s)[0],
j=d.createElement(s),dl=l!='dataLayer'?'&l='+l:'';j.async=true;j.src=
'//www.googletagmanager.com/gtm.js?id='+i+dl;f.parentNode.insertBefore(j,f);
})(window,document,'script','dataLayer','GTM-MBXDLW');</script>
<!-- End Google Tag Manager -->
<input type='hidden' value="<%=longAccountId%>" id="aaid"/>
<!-- New Tamplate Header-->

<div class="se-pre-con"></div>

<div class="page-header navbar navbar-fixed-top">
            <!-- BEGIN HEADER INNER -->
            <div class="page-header-inner ">
                <!-- BEGIN LOGO -->
                <div class="page-logo" style='padding-left:0px;'>
                    <a href="/SC_CreateAccount">
                       <img src="/images/logo-small.png" alt="campaignalyzer" class="logo-default" width="155" style="margin-top:19px;"/> </a>
                    <div class="menu-toggler sidebar-toggler">
                        <!-- DOC: Remove the above "hide" to enable the sidebar toggler button on header -->
                    </div>
                </div>
                <!-- END LOGO -->
                <!-- BEGIN RESPONSIVE MENU TOGGLER -->
                <a href="javascript:;" class="menu-toggler responsive-toggler" data-toggle="collapse" data-target=".navbar-collapse"> </a>
                <!-- END RESPONSIVE MENU TOGGLER -->
                <!-- BEGIN PAGE ACTIONS -->
                <!-- DOC: Remove "hide" class to enable the page header actions -->
         
                <!-- BEGIN PAGE TOP -->
                <div class="page-top">
                   <!-- BEGIN TOP NAVIGATION MENU -->
                   <div style='float:left; margin:17px;'>
                   <%if(AccountService.getAllAccountsCountFor(myEmail) != 0 ){%>
        					<%= HtmlUtil.renderAccountSwitcher(myEmail, longAccountId) %>           
                   <%}%>
                   </div>
                   
                    <div class="top-menu">
                        <ul class="nav navbar-nav pull-right">                            
                            <!-- BEGIN USER LOGIN DROPDOWN -->
                            <%if(0!=longAccountId ){%> 
	                   			<%if(AccountService.isTrailAccountId(longAccountId) == true ){%>
	                   
		                            <li class="dropdown dropdown-extended dropdown-notification" id="header_notification_bar">
		                                <a href="javascript:;" class="dropdown-toggle" data-toggle="dropdown" data-hover="dropdown" data-close-others="true">
		                                    <i class="icon-bell"></i>
		                                    <span class="badge badge-default"> 1 </span>
		                                </a>
		                                <ul class="dropdown-menu">
		                                    <li class="external">
		                                        <h3>
		                                            <span class="bold">Free Trial: </span> <%= AccountService.getTrailInfoDays(longAccountId) %> days left</h3>
		                                        <a href="/EditAccountPlan.jsp?aid=<%=longAccountId%>">pay now </a>
		                                    </li>
		                                    <li>
		                                        <div class="slimScrollDiv" style="position: relative; overflow: hidden; width: auto; height: 152px;"><ul class="dropdown-menu-list scroller" style="height: 250px; overflow: hidden; width: auto;" data-handle-color="#637283" data-initialized="1">
		                                            <li>
		                                                <a href="/EditAccountPlan.jsp?aid=<%=longAccountId%>" >
		                                                    <span class="time"><%= AccountService.getTrailInfoDays(longAccountId) %> days</span>
		                                                    <span class="details">
		                                                        <span class="">
		                                                            <!--<i class="fa fa-plus"></i>-->
		                                                        </span> You have  <%= AccountService.getTrailInfoDays(longAccountId) %> days remaining until your free trial ends. Enter your payment information to continue using CampaignAlyzer when your trial period finishes.</span>
		                                                </a>
		                                            </li>
		                                            
		                                        </ul><div class="slimScrollBar" style="background: rgb(99, 114, 131); width: 7px; position: absolute; top: 0px; opacity: 0.4; display: block; border-radius: 7px; z-index: 99; right: 1px;"></div><div class="slimScrollRail" style="width: 7px; height: 100%; position: absolute; top: 0px; display: none; border-radius: 7px; background: rgb(234, 234, 234); opacity: 0.2; z-index: 90; right: 1px;"></div></div>
		                                    </li>
		                                </ul>
		                            </li>
                          		 <%}%>	
                            <%}%>
                            <!-- DOC: Apply "dropdown-dark" class after below "dropdown-extended" to change the dropdown styte -->
                             <%if(myPhoto == null || myPhoto == ""){%>
                                 
                             <li style="float:left; padding:15px 0">
                             <img src="/images/default_avatar.png" style="width:39px; border-radius:100% !important" id="defaultAvatar"></li>
                            <%}else{%>                                                        
                            <li style="float:left; padding:15px 0">
                             
                              <img src="<%=myPhoto%>" style="width:39px; border-radius:100% !important" id="profilePhoto"></li>
                             <%}%>
                             <li style="float:left" class="dropdown dropdown-user">                            
                               <%=HtmlUtil.renderWelcomeBox(myEmail, myRole," # onclick='signOut();'","/signin.jsp")%>                                
                            </li>
                            
                            
                             
                            
                            
                         
                            <!-- END USER LOGIN DROPDOWN -->
                            
                            <!-- BEGIN QUICK SIDEBAR TOGGLER -->
                            <!-- DOC: Apply "dropdown-dark" class after below "dropdown-extended" to change the dropdown styte -->
                           
                            <!-- END QUICK SIDEBAR TOGGLER -->
                           <li style="float:left; padding:15px 0" title="Logout"><a class="" href="#" onclick="signOut();" style="
    padding: 10px;
"><i class="icon-logout"></i></a></li>
                        </ul>
                    </div>
                    <!-- END TOP NAVIGATION MENU -->
                </div>
                <!-- END PAGE TOP -->
            </div>
            <!-- END HEADER INNER -->
        </div>
        <!-- END HEADER -->
        <!-- BEGIN HEADER & CONTENT DIVIDER -->
        <div class="clearfix"> </div>
<!-- End New Template Header-->
                    </div>
                </div>
            </div>
        </div>
    </div>

    <!-- Start Main Wrapper -->
    <div class="page-container">
            <!-- BEGIN SIDEBAR -->
            <div class="page-sidebar-wrapper">
                <!-- END SIDEBAR -->
                <!-- DOC: Set data-auto-scroll="false" to disable the sidebar from auto scrolling/focusing -->
                <!-- DOC: Change data-auto-speed="200" to adjust the sub menu slide up/down speed -->
                <div class="page-sidebar navbar-collapse collapse">
                    <!-- BEGIN SIDEBAR MENU -->
                    <!-- DOC: Apply "page-sidebar-menu-light" class right after "page-sidebar-menu" to enable light sidebar menu style(without borders) -->
                    <!-- DOC: Apply "page-sidebar-menu-hover-submenu" class right after "page-sidebar-menu" to enable hoverable(hover vs accordion) sub menu mode -->
                    <!-- DOC: Apply "page-sidebar-menu-closed" class right after "page-sidebar-menu" to collapse("page-sidebar-closed" class must be applied to the body element) the sidebar sub menu mode -->
                    <!-- DOC: Set data-auto-scroll="false" to disable the sidebar from auto scrolling/focusing -->
                    <!-- DOC: Set data-keep-expand="true" to keep the submenues expanded -->
                    <!-- DOC: Set data-auto-speed="200" to adjust the sub menu slide up/down speed -->
        

<%= HtmlUtil.renderNavigationLinks(myEmail, myRole, longAccountId) %>

       </div>
                <!-- END SIDEBAR -->
            
            <!-- END SIDEBAR -->
            <!-- BEGIN CONTENT -->
            <div class="page-content-wrapper">
                <!-- BEGIN CONTENT BODY -->
                <div class="page-content">
                    <!-- BEGIN PAGE HEADER-->
                    <!-- BEGIN THEME PANEL -->                   	
                    <div class="page-bar">
                        <ul class="page-breadcrumb"> 
                        <%= HtmlUtil.renderBreadcrumbs(myEmail, myRole, longAccountId, cName, ms, longCampaignId , newType, title)%></ul>
                                                     
                     </div>
                        
       
	