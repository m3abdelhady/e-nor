<%@page import="me.heuristic.services.AnalyticsService"%>
<%@ page import="java.util.Arrays" %>
<%@ page import="me.heuristic.util.HtmlUtil" %>
<%@ page import="me.heuristic.references.Messages" %>
<%@ page import="me.heuristic.references.UserType" %>
<%@ page import="me.heuristic.services.AccountService" %>
<%@ page import="me.heuristic.services.CampaignHolderService" %>
<%@ page import="com.google.appengine.api.users.User" %>
<%@ page import="com.google.appengine.api.users.UserService" %>
<%@ page import="com.google.appengine.api.users.UserServiceFactory" %>
<%@ page import="com.google.appengine.api.datastore.Entity" %>

<%	String title = "Google Analytics Connection"; %>
<%@ include file="/includes/_header.inc" %> 
<script>
(function(w,d,s,g,js,fs){
  g=w.gapi||(w.gapi={});g.analytics={q:[],ready:function(f){this.q.push(f);}};
  js=d.createElement(s);fs=d.getElementsByTagName(s)[0];
  js.src='https://apis.google.com/js/platform.js';
  fs.parentNode.insertBefore(js,fs);js.onload=function(){g.load('analytics');};
}(window,document,'script'));
</script>
 <link href="/ga-dev-tools-master/src/css/index.css" rel="stylesheet" type="text/css"/> 
<%
String gaViewId =  HtmlUtil.handleNullWhiteSpaces((String) myAccount.getProperty("gaViewId"));
String gaViewName =  HtmlUtil.handleNullWhiteSpaces((String) myAccount.getProperty("gaViewName"));
String gaAccount =  HtmlUtil.handleNullWhiteSpaces((String) myAccount.getProperty("gaAccount"));
String gaProperty =  HtmlUtil.handleNullWhiteSpaces((String) myAccount.getProperty("gaProperty"));

if(gaViewName == null || gaViewName ==""){
	gaViewName = "Not Set";
}
if(gaAccount == null || gaAccount == ""){
	gaAccount = "Not Set";
}
if(gaProperty == null || gaProperty == ""){
	gaProperty = "Not Set";
}


%>
<style>
.ViewSelector2-item > label, .ViewSelector td:first-child{
display:inline-block !important;
width:10% !important;
}
.ViewSelector2-item > select{
width:37% !important;
}
</style>

<%String myToken = request.getParameter("token"); %>
<%String storedRefreshToken = (String) myAccount.getProperty("refreshToken"); %>
<%String storedAcessToken= (String) myAccount.getProperty("accessToken"); %>

<%String accessTokenFromRefreshToken = ""; %>
<%String campaignName = request.getParameter("cname"); %>
<%String viewId =  request.getParameter("gaView"); %>


<!-- <div id="data-chart-2-container"></div>
<div id="date-range-selector-2-container"></div> -->

<!-- Include the ViewSelector2 component script. -->
 <script src="/ga-dev-tools-master/build/javascript/embed-api/components/view-selector2.js"></script> 

<!-- Include the DateRangeSelector component script. -->
 <script src="/ga-dev-tools-master/build/javascript/embed-api/components/date-range-selector.js"></script> 

<div class="row">
<div class="col-md-8">
<div class="portlet light">
	<div class="portlet-title" style="margin-bottom:0px;">
	<div class="caption">
	   	<span class="caption-subject font-green bold uppercase"><%= title %></span>
	   
	</div>   	
	</div>
	<div class="mws-panel-body">
	
	<%
if(storedRefreshToken == null){%>


	<div id='authBlock' class="authBlock"><p>Click Below to authenticate to your Google Analytics account, once connected, select the account, property and view you wish to analyze with campaign data.</p>

	
   <!--  <a class="btn green" href="https://accounts.google.com/AccountChooser?continue=https://accounts.google.com/o/oauth2/v2/auth?access_type%3Doffline%26scope%3Dhttps://www.googleapis.com/auth/admin.reports.usage.readonly%2Bhttps://www.googleapis.com/auth/analytics.readonly%26response_type%3Dcode%26redirect_uri%3Dhttp://localhost:8888/AnalyticsCallBack%26client_id%3D458758013131-ib24rrpblt0ltk24enis2ggk189cq2o5.apps.googleusercontent.com%26from_login%3D1%26as%3D6b7f8dbe90f05f73%26prompt%3Dconsent%26btmpl=authsub&scc=1&oauth=1">Click Authorize Connection</a> -->
	
	<!-- <a class="btn green" href="https://accounts.google.com/AccountChooser?continue=https://accounts.google.com/o/oauth2/v2/auth?access_type%3Doffline%26scope%3Dhttps://www.googleapis.com/auth/admin.reports.usage.readonly%2Bhttps://www.googleapis.com/auth/analytics.readonly%26response_type%3Dcode%26redirect_uri%3Dhttps://4-0-dot-campaignalyzer.appspot.com/AnalyticsCallBack%26client_id%3D458758013131-ib24rrpblt0ltk24enis2ggk189cq2o5.apps.googleusercontent.com%26from_login%3D1%26as%3D6b7f8dbe90f05f73%26prompt%3Dconsent%26btmpl=authsub&scc=1&oauth=1">Click Authorize Connection</a> -->
	
	<a class="btn green" href="https://accounts.google.com/AccountChooser?continue=https://accounts.google.com/o/oauth2/v2/auth?access_type%3Doffline%26scope%3Dhttps://www.googleapis.com/auth/admin.reports.usage.readonly%2Bhttps://www.googleapis.com/auth/analytics.readonly%26response_type%3Dcode%26redirect_uri%3Dhttps://app.campaignalyzer.com/AnalyticsCallBack%26client_id%3D458758013131-ib24rrpblt0ltk24enis2ggk189cq2o5.apps.googleusercontent.com%26from_login%3D1%26as%3D6b7f8dbe90f05f73%26prompt%3Dconsent%26btmpl=authsub&scc=1&oauth=1">Click Authorize Connection</a>
	
	<!-- <a class="btn green" href="https://accounts.google.com/AccountChooser?continue=https://accounts.google.com/o/oauth2/v2/auth?access_type%3Doffline%26scope%3Dhttps://www.googleapis.com/auth/admin.reports.usage.readonly%2Bhttps://www.googleapis.com/auth/analytics.readonly%26response_type%3Dcode%26redirect_uri%3Dhttps://4-0-dot-campaignalyzer.appspot.com/AnalyticsSettings.jsp%26client_id%3D458758013131-ib24rrpblt0ltk24enis2ggk189cq2o5.apps.googleusercontent.com%26from_login%3D1%26as%3D6b7f8dbe90f05f73%26prompt%3Dconsent%26btmpl=authsub&scc=1&oauth=1">Click Here to Authroize</a> -->
	
	<!-- <a class="btn green" href="https://accounts.google.com/AccountChooser?continue=https://accounts.google.com/o/oauth2/v2/auth?access_type%3Doffline%26scope%3Dhttps://www.googleapis.com/auth/admin.reports.usage.readonly%2Bhttps://www.googleapis.com/auth/analytics.readonly%26response_type%3Dcode%26redirect_uri%3Dhttps://4-0-dot-campaignalyzer.appspot.com/defaultEmbedApi.jsp%26client_id%3D458758013131-ib24rrpblt0ltk24enis2ggk189cq2o5.apps.googleusercontent.com%26from_login%3D1%26as%3D6b7f8dbe90f05f73%26prompt%3Dconsent%26btmpl=authsub&scc=1&oauth=1">Click Here to Authroize</a> -->
	
		

	</div>
	<%}else{%>
	<div id='disconnectBlock' class="disconnectBlock"><p>Click Below to disconnect Google Analytics from CampaignAlyzer.</p>
	<a class="btn red" href="/AnalyticsCallBack?act=disconnect&aid=<%=longAccountId%>"><i class="fa fa-unlink" style="
    margin-right: 3px;
"></i> Disconnect</a>
	
	<p>Select the account, property and view you wish to analyze with campaign data.</p>
	<div id="view-selector-container" style="padding-top:15px;"></div>
	
	<form action="/AnalyticsInfoHandler" method="post">
<input type="hidden" name="act" id="act" value="saveViewId"/>
<input type="hidden" name="caAccount" id="caAccount"/>
<input type="hidden" name="gaAccount" id="gaAccount"/>
<input type="hidden" name="gaProperty" id="gaProperty"/>
<input type="hidden" name="gaView" id="gaView" />
<input type="hidden" name="gaViewName" id="gaViewName" />
<input type="hidden" name="myToken" id="myToken" value=<%=storedAcessToken %> />
<input type="hidden" name="aids" id="aids" value=<%=longAccountId %> />
<input type="submit" class="btn green saveView"  value="Save"/>
</form>
	</div>
	</div>
	<%} %>
	<div style="display:none;" class="authBlockMessage">
	<%-- <h3 class="caption-subject font-red bold uppercase">GA Account Information:</h3>
	<p class="btn  small" style="text-align:left; margin-bottom:5px; background: #fafafa;"">	
	<span class="font-blue bold">Account Name:</span> <span ><%=gaAccount%></span></p>
	<p class="btn small" style="text-align:left; margin-bottom:5px; background: #fafafa;"">	
	<span class="font-blue bold">Property Name: </span><span><%=gaProperty %></span></p>
	<p class="btn small" style="text-align:left; margin-bottom:5px; background: #fafafa;"">	
	<span class="font-blue bold">View: </span><span><%=gaViewName %></span></p> --%>
	
	
	<div id="embed-api-auth-container"></div>
	
	

	<div id="date-range-selector-1-container"></div>

	<div id="data-chart-1-container"></div>
	
	</div>	


</div>
</div>
<script>
var flag= true;
var propertySelection = true;
gapi.analytics.ready(function() { 
	  /**
	   * Authorize the user immediately if the user has already granted access.
	   * If no access has been created, render an authorize button inside the
	   * element with the ID "embed-api-auth-container".
	   */
	   
	   gapi.analytics.auth.authorize({
		    'serverAuth': {
		      'access_token': '<%=storedAcessToken%>',      
		    }, 
		    'container': 'embed-api-auth-container',
		    'clientid': '458758013131-ib24rrpblt0ltk24enis2ggk189cq2o5.apps.googleusercontent.com',
		    'immediate': true
		  });

	  /*  gapi.analytics.auth.isAuthorized(){
		  
	   } */
	  /**
	   * Store a set of common DataChart config options since they're shared by
	   * both of the charts we're about to make.
	   */
	  var commonConfig = {		
	    query: {
	      'ids':'<%=gaViewId%>',
	      'metrics': 'ga:sessions,ga:percentNewSessions,ga:newUsers,ga:bounceRate,ga:avgSessionDuration,ga:transactionRevenue',
	      'dimensions': 'ga:campaign',
	      'startIndex':1	      
	    },
	    chart: {
	      type: 'TABLE',
	      options: {
	        width: '100%'        
	        
	      }
	    }
	  };


	  /**
	   * Query params representing the first chart's date range.
	   */
	  var dateRange1 = {
	    'start-date': '14daysAgo',
	    'end-date': '8daysAgo'
	  };
	  var ids={
		'ids':'<%=gaViewName%>'
	  }


	  /**
	   * Query params representing the second chart's date range.
	   */
	  var dateRange2 = {
	    'start-date': '7daysAgo',
	    'end-date': 'yesterday'
	  };


	  /**
	   * Create a new ViewSelector2 instance to be rendered inside of an
	   * element with the id "view-selector-container".
	   */
	  var viewSelector = new gapi.analytics.ext.ViewSelector2({
	    container: 'view-selector-container',
	    ids:'<%=gaViewId%>'
	  })
	  /* .set(ids) */
	  .execute();


	  /**
	   * Create a new DateRangeSelector instance to be rendered inside of an
	   * element with the id "date-range-selector-1-container", set its date range
	   * and then render it to the page.
	   */
	  var dateRangeSelector1 = new gapi.analytics.ext.DateRangeSelector({
	    container: 'date-range-selector-1-container'
	  })
	  .set(dateRange1)
	  .execute();


	  /**
	   * Create a new DateRangeSelector instance to be rendered inside of an
	   * element with the id "date-range-selector-2-container", set its date range
	   * and then render it to the page.
	   */
	  /* var dateRangeSelector2 = new gapi.analytics.ext.DateRangeSelector({
	    container: 'date-range-selector-2-container'
	  })
	  .set(dateRange2)
	  .execute(); */


	  /**
	   * Create a new DataChart instance with the given query parameters
	   * and Google chart options. It will be rendered inside an element
	   * with the id "data-chart-1-container".
	   */
	  var dataChart1 = new gapi.analytics.googleCharts.DataChart(commonConfig)
	      .set({query: dateRange1})
	      .set({chart: {container: 'data-chart-1-container'}});


	  /**
	   * Create a new DataChart instance with the given query parameters
	   * and Google chart options. It will be rendered inside an element
	   * with the id "data-chart-2-container".
	   */
	  /* var dataChart2 = new gapi.analytics.googleCharts.DataChart(commonConfig)
	      .set({query: dateRange2})
	      .set({chart: {container: 'data-chart-2-container'}}); */


	  /**
	   * Register a handler to run whenever the user changes the view.
	   * The handler will update both dataCharts as well as updating the title
	   * of the dashboard.
	   */
	  viewSelector.on('viewChange', function(data) {
	    dataChart1.set({query: {ids: data.ids}}).execute();
	   // dataChart2.set({query: {ids: data.ids}}).execute();
/*  var title = document.getElementById('view-name');
	    title.innerHTML = data.property.name + ' (' + data.view.name + ')'; */
	    
	  });	      
	 
	  /**
	   * Register a handler to run whenever the user changes the date range from
	   * the first datepicker. The handler will update the first dataChart
	   * instance as well as change the dashboard subtitle to reflect the range.
	   */
	  dateRangeSelector1.on('change', function(data) {
	    dataChart1.set({query: data}).execute();

	    // Update the "from" dates text.
	    var datefield = document.getElementById('from-dates');
	    datefield.innerHTML = data['start-date'] + '&mdash;' + data['end-date'];
	  });


	  /**
	   * Register a handler to run whenever the user changes the date range from
	   * the second datepicker. The handler will update the second dataChart
	   * instance as well as change the dashboard subtitle to reflect the range.
	   */
	 /*  dateRangeSelector2.on('change', function(data) {
	    dataChart2.set({query: data}).execute();

	    // Update the "to" dates text.
	    var datefield = document.getElementById('to-dates');
	    datefield.innerHTML = data['start-date'] + '&mdash;' + data['end-date'];
	  }); */
	  
	  dataChart1.on('success', function(response) {
		  var accountName = $(".gaAccount option:selected").text();
		   var PropertyName = $(".gaProperty option:selected").text();
		   var viewName = $(".gaView option:selected").text();
		   $("#gaViewName").val(viewName);
		   $("#gaAccount").val(accountName);
		   $("#gaProperty").val(PropertyName);
		   $("#gaView").val($(".gaView").val());
	    
		});

	 /*  gapi.analytics.auth.on('sucsess', function(response) {
			 
		    //hide the auth-button
		   alert("beeb");
		
		    dataChart1.execute();
		    
		  }); */
	  
	 
	  
	});
	 
</script>
</div></div>

<%= HtmlUtil.renderFooter() %>
<script type="text/javascript">
var gaView = $("#view-selector-container select").last().val();
$("#view-selector-container select").last().change(function() {
	$("#gaView").val(gaView);
});


alert = function() {};
$(".note").click(function(){
	$(this).animate({opacity:0}, function() {
		$(this).slideUp("medium", function() {
			$(this).css("opacity", '');
		});
	});
	
});
/* $("#data-chart-1-container").hide(); */
/* $("#date-range-selector-1-container").hide(); */

	
/* $('#data-chart-1-container .google-visualization-table-table').dataTable({ 
	 "order": [[7, "desc"]]   
}); */


</script>


