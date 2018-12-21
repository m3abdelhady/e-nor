<%@page import="com.braintreegateway.org.apache.commons.codec.binary.StringUtils"%>
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ page import="java.util.Arrays"%>
<%@ page import="me.heuristic.util.HtmlUtil"%>
<%@ page import="me.heuristic.references.Messages"%>
<%@ page import="me.heuristic.references.UserType"%>
<%@ page import="me.heuristic.services.AccountService"%>
<%-- <%@ page import="com.google.appengine.api.users.User"%>
<%@ page import="com.google.appengine.api.users.UserService"%>
<%@ page import="com.google.appengine.api.users.UserServiceFactory"%> --%>
<%@ page import="com.google.appengine.api.datastore.Entity"%>
<%@ page import="me.heuristic.braintree.engine.BrainTreeEngine"%>

<style>
.page-bar{display:none;}
</style>

<%
String plan = request.getParameter("pid");
String revenue = request.getParameter("revenue");
String[] m = revenue.split("\\.");
//String n = monthlyPay.substring(0,2);
int anuualAmount = Integer.parseInt(m[0]);
float  price= anuualAmount/12;  


String sku = "";
if(plan.contains("Basic") || plan.contains("basic")){
sku = "P0001";
}
else if(plan.contains("Professional") || plan.contains("professional")){
	sku = "P0002";
}
else{sku="";}
	
	


//price = revenue/12;
String title = "Confirmation";
String accountId = request.getParameter("aid");
%>

<%@ include file="/includes/_header.inc"%>

<div class="row">	
<div class="col-md-6">
<div class="portlet light ">
	<div class="portlet-title">
		<div class="caption">
			<span class="caption-subject font-green bold uppercase">Thank You</span>
		</div>
	</div>
	<div class="mws-panel-body" style='padding-top:20px;'>
<p>
Congratulations on your new CampaignAlyzer account!
<p>
<p>Setting up and using CampaignAlyzer takes few simple steps. <br>
We've brought together the essential information you'll need to get started in this video. For more information, <a href="https://www.campaignalyzer.com/support/">please visit our support center</a>
</p>
<p>Getting Started with CampaignAlyzer</p>

<iframe width="560" height="315" src="https://www.youtube.com/embed/9ltoCDI72gE?rel=0" frameborder="0" allowfullscreen></iframe>

<p>Thanks for your business!</p>
</div></div></div></div>
<script type="text/javascript">
dataLayer.push({
	'event': 'eventTracker', 'eventCat': 'Ecommerce', 'eventAct':'Purchase', 'eventLbl': 'Completed', 'eventVal':0,
	'ecommerce': {
	   'purchase': {
      	'actionField': {
        	'id': '<%=accountId%>',
		'affiliation': 'CampaignAlyzer Online Store',
        	'revenue': '<%=Float.parseFloat(revenue)%>',
        	'tax': '0.00',
        	'shipping': '0.00',
        	'coupon': ''
      		},
	  	'products': [{
 			'name': '<%=plan %>',
       		'id': '<%=sku %>',
      		'price': '<%=price%>',
       		'brand': 'CampaignAlyzer',
       		'category': 'Plan Subscription',
       		'variant': 'annual',
			'quantity': 12,
			'coupon': '' 
		}]
	  }
     },
		'eventCallback': function() {
		dataLayer.push({'ecommerce': undefined});
		}
  });
  console.log("<%=plan%>");
</script>


<%=HtmlUtil.renderFooter()%>