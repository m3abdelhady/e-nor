<%@ page import="me.heuristic.services.OrderService"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.Arrays" %>
<%@ page import="me.heuristic.util.HtmlUtil" %>
<%@ page import="me.heuristic.references.Messages" %>
<%@ page import="me.heuristic.references.AccountType" %>
<%@ page import="me.heuristic.references.UserType" %>
<%@ page import="me.heuristic.services.AccountService" %>
<%-- <%@ page import="com.google.appengine.api.users.User" %>
<%@ page import="com.google.appengine.api.users.UserService" %>
<%@ page import="com.google.appengine.api.users.UserServiceFactory" %> --%>
<%@ page import="com.google.appengine.api.datastore.Entity" %>
<%@ page import="me.heuristic.braintree.engine.BrainTreeEngine"%>
<%	String title = "Account Plan"; %>
<%@ include file="/includes/_header.inc" %>
<style>
.breadcrumbs{display:none;}
</style>
<h3 style="display:none;"><%=title %></h3>

<script>
var annualAmount;
var monthlyAmount;
if(getParameterByName("pid") == "Basic"){
	annualAmount = 1188.0;	
}
else if (getParameterByName("pid") == "Professional"){
annualAmount = 5988.0;	
	
}
monthlyAmount = annualAmount/12;
</script>

<% String accountId = request.getParameter("aid");
	   long maccountId = Long.parseLong(accountId);
	  // String monthlyPay = request.getParameter("np");	  
	   //String monthlyPay=;
	  
	  // String[] m = monthlyPay.split("\\.");
	   //String n = monthlyPay.substring(0,2);
	   //int anuualAmount = Integer.parseInt(m[0]);
	  // int monthllyAmount = anuualAmount/12;
	    
	   
	  	%>
	<% Entity currentAccount = AccountService.getSingleAccount(maccountId);	%>

<%String activateMessage = "Your account has been created.  To activate account, please choose a plan:" ; %>
<%if(null ==request.getParameter("update")){%>
<p style="margin-left:1%; font-family:verdana; display:none;"><%=activateMessage %></p>
<%} %>


<%if(null == request.getParameter("pid") || "" == request.getParameter("pid")){%>
<style>
.checkoutTble, .subscripionDetailsTable{display:none;}
</style>
<%} else{ %>
<style>
.AccountPlanTable, .NotesTable{display:none;}
</style>
<script>

$("li[title='Account']").text("Payment Details");
$("li[title='Account']").before("<li title='Selected Plan'><%=request.getParameter("pid")%><i class='fa fa-angle-right'></i></li>");
</script>
<%} %>

<div class="row">
<div class="col-md-8 AccountPlanTable">
<div class="portlet light ">
	<div class="portlet-title">
		<div class="caption">
		<span class="caption-subject font-green bold uppercase"><%=title %></span>
		</div>
	</div>
	<div class="portlet-body">
<%
	String update =request.getParameter("update");
	// if not signed in
	if ("".equals(myEmail)) {
%>
<% response.sendRedirect("/signin.jsp?continuer=EditAccountPlan.jsp"); %>
<%-- <%= Messages.renderErrorMessage(Messages.UNRECOGNIZED_USER) %> --%>
<%
	// if no correct account
	} else if (null==myAccount) {
%>
<%= Messages.renderErrorMessage(Messages.NO_SUCH_ACCOUNT) %>
<%
	// check permission
	} else if (!UserType.hasPermission(myRole, UserType.PERMISSION_VIEW_ACCOUNT) && !myEmail.contains("webmaster@campaignalyzer.com")) {
%>
<%= Messages.renderErrorMessage(Messages.NO_PERMISSION) %>
<%
	} else {
		String[] errors = request.getParameterValues("err");
		
		// if errors exist
		if (null!=errors) {
%>
<%= Messages.renderMessages(Arrays.asList(errors), Messages.STYLE_ERROR) %>
<%
		}
%>
<%= AccountService.renderAccountSubscriptionForm(myAccount,update)  %>
	</div>
</div>
</div>
<div class="col-md-4 NotesTable">
<div class="portlet light ">
	<div class="portlet-title">
	   	<span class="caption-subject font-green bold uppercase">Notes</span>
	</div>
	<div class="portlet-body">
		<div class="mws-panel-content">
<%
		if (AccountService.isActiveAccount(myAccount)) {
%>			
			
			<h3>Upgrade Account</h3>
			<p>If you choose this option, your upgrade will take effect immediately and you will only be charged the difference between the two subscriptions.</p>
			
			<h6 style="display:none">To upgrade your plan:</h6>
			<ol style="display:none">
			<li>Select <strong>Settings &gt; Account</strong> Settings from the side bar menu.</li>
			<li>Click <strong>Change Plan</strong>.</li>
			<li>Subscribe to a new desired plan.</li>
			</ol>
			
			<h3>Downgrade Account</h3>
			<p>If you choose this option, your downgrade will take effect immediately. However, payment downgrades will occur at the end of the current payment cycle (a month from the last payment).</p>
			
			<h6 style="display:none">To downgrade your plan:</h6>
			<ol style="display:none">
			<li>Select <strong>Settings &gt; Account</strong> Settings from the side bar menu.</li>
			<li>Click <strong>Change Plan</strong>.</li>
			<li>Subscribe to a new desired plan.</li>
			</ol>
<%
		} else {
%>			
			<h3>No commitment</h3>
			<p>All payments are billed annually from the start of your subscription period. There is no minimum contract and you can easily cancel payment at any time and will be creditted for unused periods.</p>
			
			<h3>Security</h3>
			<p>All payments are made through BraintreePayment.</p>
			
			<h3>Note:</h3>
			<p>If you've just placed your order, allow a few minutes for the order to be processed and reload this page.</p>
<%
		}
%>
		</div>
<%
	}
%>
	</div>
	
</div>
</div></div>

<div class="row">	
<div class="col-md-6 checkoutTble">
<div class="portlet light ">
	<div class="portlet-title">
		<div class="caption">
			<span class="caption-subject font-green bold uppercase">Payment Method</span>
		</div>
	</div>
	<div class="mws-panel-body" style='padding-top:20px;'>
		<table width="100%" >
			<tr>
				<!-- <td style="width:50%; border-right:solid 1px #ccc"> -->
				<td style="width:50%;">
					<table style="width:90%; margin:0 auto;">
						<tr>
							<td style='padding-bottom:20px;'><span>
							<img src="/images/checkout_logos.png"/>
							</span>
							</td>
						</tr>
						<tr>
							<td>
								<form id="checkout" method="post" action="/braintreeservlet">
									<!--  <div id="dropin"></div>-->
									<table class="table" style="width: 100%">
										<tr>
											<td align="left">Card Number</td>
											<td><input maxlength="16" id='bt_number' class='form-control' required data-braintree-name="number"></td>
										</tr>
										<tr>
											<td align="left">Expiration Date (MM/YY)</td>
											<td><!-- <div style="display:none;width:100% !important;" class="input-group input-medium date date-picker" data-date-format="mm/yyyy" data-date-viewmode="years" data-date-minviewmode="months">
                                                        <input id='bt_exd' class='form-control' required data-braintree-name="expiration_date" readonly>
                                                        <span class="input-group-btn">
                                                            <button class="btn default" type="button" style='height:34px;'>
                                                                <i class="fa fa-calendar"></i>
                                                            </button>
                                                        </span>
                                                    </div> -->
                                                    <div class="row">
                                            <div class="col-md-6"><select class='form-control' id="monthList" required>											
										    <option selected value='01'>01</option>
										    <option value='02'>02</option>
										    <option value='03'>03</option>
										    <option value='04'>04</option>
										    <option value='05'>05</option>
										    <option value='06'>06</option>
										    <option value='07'>07</option>
										    <option value='08'>08</option>
										    <option value='09'>09</option>
										    <option value='10'>10</option>
										    <option value='11'>11</option>
										    <option value='12'>12</option>
											</select>
											
											</div>
                                                    <div class="col-md-6">
                                                    <select class='form-control' id="yearList" required></select></div>
                                                    </div>
                                                    
											<input type="hidden" id="exdate" class='form-control' required data-braintree-name="expiration_date" 	required>											
											
											
											</td>
										</tr>
										<tr>
											<td align="left">Card Holder Name</td>
											<td><input class='form-control' required data-braintree-name="cardholder_name"></td>
										</tr>
										<tr>
											<td align="left">CVV</td>
											<td><input class='form-control' required data-braintree-name="cvv"></td>
										</tr>
										<tr>
											<td align="left">Postal Code</td>
											<td><input class='form-control' required data-braintree-name="postal_code"></td>
										</tr>
										<tr>
											<td colspan="2" align="left"><input class="btn sbold green" type="submit"
												id="submit" value="Pay with Credit"></td>
										</tr>
									</table>

									<input type="hidden" name="pid"
										value="<%=request.getParameter("pid")%>" /> 
										<input type="hidden" name="aid"	value="<%=request.getParameter("aid")%>" /> 
										<input type="hidden" name="update" value="<%=request.getParameter("update")%>" />
										<input type="hidden" name="np" id="np" value="">
								</form>
							</td>
						</tr>
					</table>
				</td>
				<td style="width: 10%; display:none;" >
				</td>
				<td style="width:40%; height:0px; display:none;" >
					<table style="width:90%; margin:0 auto; height:90%">
						<tr style="display:none">
							<td><span class="mws-i-24 i-suitcase-1"></span></td>
						</tr>
						<tr>
							<td style='vertical-align:top;'>
								<form id="merchant-form" action="/braintreeservlet"
									method="post">
									<table>
										<tr>
											<td><div id="paypal-container"></div></td>
										</tr>
										<tr>
											<td style="height: 20pt;"></td>
										</tr>
										<tr>
											<td><input class="btn sbold green" type="submit" value="Pay with Paypal" /></td>
										</tr>
									</table>

									<input type="hidden" name="pid"
										value="<%=request.getParameter("pid")%>" /> <input
										type="hidden" name="aid"
										value="<%=request.getParameter("aid")%>" /> <input
										type="hidden" name="update"
										value="<%=request.getParameter("update")%>" />
								</form>
								</td>
						</tr>
						
					</table> 
					<!-- <button type='reset' class="cancelButton" onclick='goBack()'>Cancel</button> -->
					<a style="float:right; margin-right:10%; text-decoration: none;" onclick='goBack()'>Cancel</a>
					
					<script src="https://js.braintreegateway.com/v2/braintree.js"></script>

		<script>
  
    braintree.setup("<%=BrainTreeEngine.getInstance().GetClientToken()%>",
					"custom", {
						id : "checkout"
					});
  		</script>
  		
					<script src="https://js.braintreegateway.com/v2/braintree.js"></script>
					<script>braintree.setup("<%=BrainTreeEngine.getInstance().GetClientToken()%>", "paypal", {
      container: "paypal-container",
      singleUse: false,
      onPaymentMethodReceived: function (obj) {
        doSomethingWithTheNonce(obj.nonce);
      }
    });
					
					</script>
    
				</td>
			</tr>
		</table>	</div>
</div>
</div>

<div class="col-md-6 subscripionDetailsTable">
<div class="portlet light ">
	<div class="portlet-title">
		<div class="caption">
		<span class="caption-subject font-green bold uppercase">Subscription Details</span>
	</div>
	</div>
	<div class="mws-panel-body" style='padding-top:20px;'>
	<ol class="progtrckr" data-progtrckr-steps="5">
    <li class="progtrckr-done">Selected Plan</li>
    <li class="progtrckr-done">Account</li>
    <li class="progtrckr-done">Date</li>
    <li class="progtrckr-done">Amount</li>
    
</ol>

<ol class="progtrckr" data-progtrckr-steps="5" style='border-bottom:none;'>
    <li class="progtrckr-done"><%= request.getParameter("pid") %></li>
    <li class="progtrckr-done"><%= currentAccount.getProperty("name") %></li>
    <li class="progtrckr-done"><script type="text/javascript">fullDate = new Date(); document.write(fullDate.toDateString());</script></li>
    <li class="progtrckr-todo" style="line-height:1.5em">$<script type="text/javascript">document.write(monthlyAmount)</script>/mo<br>(Billed Annually)</li>
</ol>

<div style="
    height: 50px;
    /* float: right; */
    text-align: right;
    width: 97%;
    margin: 30px 0px;
    font-family: verdana;
    font-weight: bold;
">Total: $<script type="text/javascript">document.write(annualAmount)</script></div>
	</div> 
	</div>
	</div></div></div>
		


<%= HtmlUtil.renderFooter() %>
<script type="text/javascript">
$(".note").click(function(){
	$(this).animate({opacity:0}, function() {
		$(this).slideUp("medium", function() {
			$(this).css("opacity", '');
		});
	});
	
});

$(".date-picker").datepicker({
	
	changeYear:true,
	yearRange: "2005:2015"
});

var d = new Date();
for (var i = 0; i <= 12; i++) {
    var option = "<option value=" + parseInt(d.getFullYear() + i) + ">" + parseInt(d.getFullYear() + i) + "</option>"
    $('#yearList').append(option);
}
$("#yearList").change(function(){
	$("#exdate").val($("#monthList").val()+"/"+ $("#yearList").val());
	
});

$("#monthList").change(function(){
	$("#exdate").val($("#monthList").val()+"/"+ $("#yearList").val());
	
});
$("#np").val(annualAmount);
</script>