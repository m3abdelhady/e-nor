<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.Arrays" %>
<%@ page import="me.heuristic.util.HtmlUtil" %>
<%@ page import="me.heuristic.references.Messages" %>
<%@ page import="me.heuristic.references.UserType" %>
<%@ page import="me.heuristic.services.AccountService" %>
<%-- <%@ page import="com.google.appengine.api.users.User" %> --%>
<%@ page import="com.google.appengine.api.users.UserService" %>
<%@ page import="com.google.appengine.api.users.UserServiceFactory" %>
<%@ page import="com.google.appengine.api.datastore.Entity" %>
<%	String title = ""; %>
<%@ include file="/includes/_header.inc" %>
<%myEmail = session.getAttribute("userEmail").toString(); %>
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
<div class="portlet light ">
	<div class="portlet-title" style="margin-bottom:0px;">
<div class="caption">
	<span class="caption-subject font-green bold uppercase"><%= title %></span>
	</div>
	</div>
	<div class="portlet-body">

<%
	// if not signed in
	if ("".equals(myEmail)) {
%>
<%-- <% response.sendRedirect("/signin.jsp?continuer=EditAccount"); %> --%>
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
	} else if (0!=longAccountId && !UserType.hasPermission(myRole, UserType.PERMISSION_MANAGE_ACCOUNT) && myRole==("ADMIN")) {
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
		String caID ="";
		
		
		
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
			caID = (String) myAccount.getProperty("CAID");
		}
%>
<%= AccountService.renderAccountForm(myEmail,longAccountId, emailAddress, firstName, lastName, company, accountName, address, city, state, zip, country, phone, website, agreement) %>
<%
	}
%>
	</div>
</div>
</div>
</div>

<script>
if(getParameterByName("pid") != null){
$("input[name='pid']").val(getParameterByName("pid"));
}</script>

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
<script type='text/javascript'>print_country('country')</script>
<script>
if($("#country").val() == "USA" || $("#country").val() == "Canada"){
	print_state_list("state");
}
else{
	var stateVal = $("#state").val();
	$("#state").replaceWith("<input type='text' name='state' value="+stateVal+" class='form-control stateText'/>");
	
	
}
</script>
<!-- <script>
print_state("state", 239)
</script> -->
<!-- <script>
if($("#country").val() == "USA" || $("#country").val() == "Canada"){
	$(".stateText").attr("id","none");
	$(".stateText").hide();
	$(".stateSelect").attr("id","state");
	
}
else{
	$(".stateSelect").attr("id","none");
	$(".stateSelect").hide();
	$(".stateText").attr("id","state");
	
}
</script> -->