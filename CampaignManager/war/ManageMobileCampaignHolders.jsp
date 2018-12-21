<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.Arrays" %>
<%@ page import="me.heuristic.util.HtmlUtil" %>
<%@ page import="me.heuristic.references.Messages" %>
<%@ page import="me.heuristic.references.UserType" %>
<%@ page import="me.heuristic.services.AccountService" %>
<%@ page import="me.heuristic.services.MobileCampaignHolderService" %>
<%@ page import="com.google.appengine.api.users.User" %>
<%@ page import="com.google.appengine.api.users.UserService" %>
<%@ page import="com.google.appengine.api.users.UserServiceFactory" %>
<%@ page import="com.google.appengine.api.datastore.Entity" %>
<%	String title = "Mobile Apps Campaign Management"; %>
<%@ include file="/includes/_header.inc" %>

<script type="text/javascript">


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
<h3 style="display:none;"><%= title %></h3>

<div class="row">
<div class="col-md-12">
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
<% response.sendRedirect("/signin.jsp?continuer=ManageMobileCampaigns.jsp"); %>
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
	} else if (!UserType.hasPermission(myRole, UserType.PERMISSION_VIEW_CAMPAIGNS) && !UserServiceFactory.getUserService().isUserAdmin()) {
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
<%= MobileCampaignHolderService.renderMobileCampaignHolderList(myRole, longAccountId) %>
<%
	}
%>
		</div>
</div>
</div>
</div>
<%= HtmlUtil.renderFooter() %>
<script type="text/javascript">
  $('#selecctall').click(function(event) {  //on click 
        if(this.checked) { // check select status
            $('.d1').each(function() { //loop through each checkbox
                this.checked = true;  //select all checkboxes with class "d1"  
                $('#dlabel').text('Clear') ;             
            });
        }else{
            $('.d1').each(function() { //loop through each checkbox
                this.checked = false; //deselect all checkboxes with class "d1"   
                $('#dlabel').text('Select All') ;                     
            });         
        }
    });
  alert = function() {};
  
  
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



