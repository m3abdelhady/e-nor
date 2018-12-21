<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.Arrays" %>
<%@ page import=" java.net.URLDecoder" %>
<%@ page import="me.heuristic.util.HtmlUtil" %>
<%@ page import="me.heuristic.references.Messages" %>
<%@ page import="me.heuristic.references.UserType" %>
<%@ page import="me.heuristic.services.AccountService" %>
<%@ page import="me.heuristic.services.CampaignService" %>
<%@ page import="me.heuristic.services.TaggedUrlService" %>
<%@ page import="me.heuristic.services.GShortenerService" %>
<%-- <%@ page import="com.google.appengine.api.users.User" %>
<%@ page import="com.google.appengine.api.users.UserService" %>
<%@ page import="com.google.appengine.api.users.UserServiceFactory" %> --%>
<%@ page import="com.google.appengine.api.datastore.Entity" %>
<%	String title = "Share this link"; %>
<%
	String myEmail = "";
	String myRole = "";
	Entity myAccount = null;
	long longAccountId = HtmlUtil.handleNullforLongNumbers(request.getParameter("aid"));

	/* UserService userService = UserServiceFactory.getUserService();
	User user = userService.getCurrentUser(); */
	/* if (null!=user) {
		myEmail = user.getEmail();
	
		if (0!=longAccountId) {
		    myAccount = AccountService.getSingleAccount(longAccountId);
		    if (null!=myAccount) {
		    	myRole = (String) myAccount.getProperty(myEmail);
		    }
		}
	} */
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />

<!-- Apple iOS and Android stuff (do not remove) -->
<meta name="apple-mobile-web-app-capable" content="no" />
<meta name="apple-mobile-web-app-status-bar-style" content="black" />
<meta name="viewport" content="width=device-width,initial-scale=1,user-scalable=no,maximum-scale=1" />

<!-- Required Stylesheets -->
<link rel="stylesheet" type="text/css" href="css/reset.css" media="screen" />
<link rel="stylesheet" type="text/css" href="css/text.css" media="screen" />
<link rel="stylesheet" type="text/css" href="css/fonts/ptsans/stylesheet.css" media="screen" />
<link rel="stylesheet" type="text/css" href="css/fluid.css" media="screen" />

<link rel="stylesheet" type="text/css" href="css/mws.style.css" media="screen" />
<link rel="stylesheet" type="text/css" href="css/icons/16x16.css" media="screen" />
<link rel="stylesheet" type="text/css" href="css/icons/24x24.css" media="screen" />
<link rel="stylesheet" type="text/css" href="css/icons/32x32.css" media="screen" />

<!-- Theme Stylesheet -->
<link rel="stylesheet" type="text/css" href="css/mws.theme.css" media="screen" />

<!-- JavaScript Plugins -->
<script type="text/javascript" src="js/jquery-1.7.1.min.js"></script>
<script type="text/javascript" src="js/jquery.mousewheel-min.js"></script>

<!-- Core Script -->
<script type="text/javascript" src="js/mws.js"></script>

	<script type="text/javascript" src="/js/campaignalyzer.js"></script>

<!-- GA Code -->
<script type="text/javascript">
  var _gaq = _gaq || [];
  _gaq.push(['_setAccount', 'UA-31262672-1']);
  _gaq.push(['_setDomainName', 'campaignalyzer.com']);
  _gaq.push(['_trackPageview']);
 
  (function() {
    var ga = document.createElement('script'); ga.type = 'text/javascript'; ga.async = true;
    ga.src = ('https:' == document.location.protocol ? 'https://ssl' : 'http://www') + '.google-analytics.com/ga.js';
    var s = document.getElementsByTagName('script')[0]; s.parentNode.insertBefore(ga, s);
  })();  
  
  //Copy URL By Morsi  
	//var copyTextareaBtn = document.querySelector('.copyURL');

 function copyToClipboard(element) {
    $("body").append("<input type='text' id='temp' style='position:absolute;opacity:0;'>");
    $("#temp").val($(element).val()).select();
    document.execCommand("copy");
    $("#temp").remove();
}

</script>
<!-- GA Code -->

	<%=	HtmlUtil.renderTitle(title) %>
</head>
<body>

    <!-- Start Main Wrapper -->
    <div id="mws-wrapper">
    
       	<!-- Inner Container Start -->
        <div class="container">
			<h1><%= title %></h1>
			
			<div class="mws-panel grid_8">
				<div class="mws-panel-header">
				   	<span class="mws-i-24 i-table-1"><%= title %></span>
				</div>
				<div class="mws-panel-body">
<%
	String completeUrl = HtmlUtil.handleNullWhiteSpaces(request.getParameter("url"));
	completeUrl = URLDecoder.decode(completeUrl, "UTF-8");
	String shortUrl = GShortenerService.shorten(completeUrl);
%>
<%=	TaggedUrlService.renderTaggedUrlShareForm(shortUrl, completeUrl) %>
				</div>
			</div>
		</div>
<!-- Main Container End -->	</div>
	
	
</body>
</html>
