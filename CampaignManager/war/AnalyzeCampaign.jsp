<%String title="Campaigns" ;%>
<%@ include file="/includes/_header.inc" %>
<style>
.google-visualization-table-table td{text-align: left !important;}
table.dataTable{margin-bottom:0px !important}
</style> 
<%String myToken = request.getParameter("token"); %>
<%String campaignName = request.getParameter("cname"); %>
<%String medium = request.getParameter("medium"); %>
<%String source = request.getParameter("source"); %>
<%String storedRefreshToken = (String) myAccount.getProperty("refreshToken"); %>
<%String storedAcessToken= (String) myAccount.getProperty("accessToken"); %>

<%String accessTokenFromRefreshToken = ""; %>


<%
String gaViewId = (String) myAccount.getProperty("gaViewId");
String gaViewName = (String) myAccount.getProperty("gaViewName");
String gaAccount = (String) myAccount.getProperty("gaAccount");
String gaProperty = (String) myAccount.getProperty("gaProperty");

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

<script>
(function(w,d,s,g,js,fs){
  g=w.gapi||(w.gapi={});g.analytics={q:[],ready:function(f){this.q.push(f);}};
  js=d.createElement(s);fs=d.getElementsByTagName(s)[0];
  js.src='https://apis.google.com/js/platform.js';
  fs.parentNode.insertBefore(js,fs);js.onload=function(){g.load('analytics');};
}(window,document,'script'));
</script>
<script src="/ga-dev-tools-master/build/javascript/embed-api/components/date-range-selector.js"></script> 
 <link href="/ga-dev-tools-master/src/css/index.css" rel="stylesheet" type="text/css"/>



<div class="row">
<div class="col-md-12">
<div class="portlet light">
	<div class="portlet-title" style="margin-bottom:0px;">
	<div class="caption">
	   	<span class="caption-subject font-green bold uppercase"><%= title %></span>
	</div>   	
	</div>
	<div class="mws-panel-body">
<div class="row">
<div class="col-md-8">


<!-- <h3 class="caption-subject font-red bold uppercase">GA Account Information:</h3> -->
	<p class="btn small" style="text-align:left; margin-bottom:5px; margin-top:10px">	
	<span class="font-blue bold">Account Name:</span> <span ><%=gaAccount%></span></p>
	<p class="btn small" style="text-align:left; margin-bottom:5px; margin-top:10px; background: #fafafa;"><span class="font-blue bold">Property Name: </span><span><%=gaProperty %></span></p>
	<p class="btn small" style="text-align:left; margin-bottom:5px; margin-top:10px; background: #fafafa;"><span class="font-blue bold">View: </span><span><%=gaViewName %></span></p>
</div>

<div class="col-md-4" style="padding-top:10px;">
<div id="date-range-selector-1-container"></div>
</div>
 </div></div></div></div></div>
 

<div class="row">
<div class="col-md-12">
<div class="portlet light">
	<div class="mws-panel-body">

<!-- <div><a href="https://accounts.google.com/AccountChooser?continue=https://accounts.google.com/o/oauth2/v2/auth?access_type%3Doffline%26scope%3Dhttps://www.googleapis.com/auth/admin.reports.usage.readonly%2Bhttps://www.googleapis.com/auth/analytics.readonly%26response_type%3Dcode%26redirect_uri%3Dhttp://localhost:8888/AnalyticsCallBack%26client_id%3D458758013131-ib24rrpblt0ltk24enis2ggk189cq2o5.apps.googleusercontent.com%26from_login%3D1%26as%3D6b7f8dbe90f05f73%26prompt%3Dconsent%26btmpl=authsub&scc=1&oauth=1">Authroize</a></div> -->
<div id="embed-api-auth-container"></div>


<div id="chart-1-container"></div>
 </div></div></div></div>
<script>

gapi.analytics.ready(function() {

  /**
   * Authorize the user with an access token obtained server side.
   */
   gapi.analytics.auth.authorize({
	    'serverAuth': {
	      'access_token': '<%=storedAcessToken%>',      
	    }, 
	    container: 'embed-api-auth-container',
	    clientid: '458758013131-ib24rrpblt0ltk24enis2ggk189cq2o5.apps.googleusercontent.com',
	    'immediate': true
	  });


  /**
   * Creates a new DataChart instance showing sessions over the past 30 days.
   * It will be rendered inside an element with the id "chart-1-container".
   */
   var dateRange1 = {
		    'start-date': '14daysAgo',
		    'end-date': '8daysAgo'
		  };
  
  var dataChart1 = new gapi.analytics.googleCharts.DataChart({
    query: {
      'ids': '<%=gaViewId%>', // <-- Replace with the ids value for your view.
      'metrics': 'ga:sessions,ga:percentNewSessions,ga:newUsers,ga:bounceRate,ga:pageviewsPerSession,ga:avgSessionDuration,ga:transactionRevenue',
      'dimensions': 'ga:campaign',
      /* 'filters': 'ga:campaign==BTSpinsSearchS1006,,ga:campaign==BTSpinsSearchS1010,ga:campaign==BTSpinsSearch880' */
      'filters': 'ga:campaign==<%=campaignName.replace("'", "\\\'")%>'
    },
    chart: {
      'container': 'chart-1-container',
      'type': 'TABLE',
      'output':'dataTable',
      'options': {
        'width': '100%'
      }
    }
  }).set({query: dateRange1});  
 
  
  dataChart1.on('success', function(response) {
	  console.log(response);
	  $(".google-visualization-table-table").dataTable({ });
	
	  $(".gapi-analytics-data-chart div").css("overflow", "hidden");
	  $(".DateRangeSelector-item input").addClass("font-green bold");
	  $(".google-visualization-table-sortind").hide();
	  $('.dataTable tr td:first-child').each(function() {
		    var $this = $(this);
		    $this.html("<a href='AnalyzSourceMedium.jsp?cname="+encodeURIComponent($this.text())+"&aid=<%=longAccountId%>'>"+$this.text()+"</a>");
		});
	  
	  $( "input[type='date']" ).keydown(function(e) {
	  	   e.preventDefault();
	  	   return false;
	  	});
	  
	});
  
  
  
  var dateRangeSelector1 = new gapi.analytics.ext.DateRangeSelector({
	    container: 'date-range-selector-1-container'
	  })
	  .set(dateRange1)
	  .execute();
  
dateRangeSelector1.on('change', function(data) {
	    dataChart1.set({query: data}).execute();
	    
	  });

dataChart1.execute();
});



$(".pagination>li>a").click(function(){
	
	$('.dataTable tr td:first-child').each(function() {
	    var $this = $(this);
	    $this.html("<a href='AnalyzSourceMedium.jsp?cname="+$this.text()+"&aid=<%=longAccountId%>&token=<%=myToken%>'>"+$this.text()+"</a>");
	});
});



</script>
 <%= HtmlUtil.renderFooter() %>