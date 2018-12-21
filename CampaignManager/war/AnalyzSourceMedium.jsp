<%String title="Sources / Medium" ;%>
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
	   	<span style='padding-left: 2px; color: #dd7127; font-size: 9px; font-weight: bold; position: relative; text-transform: uppercase; top: -3px;'>BETA</span>
	</div>   	
	</div>
	<div class="mws-panel-body">
<div class="row">
<div class="col-md-8">

<!-- <h3 class="caption-subject font-red bold uppercase">GA Account Information:</h3> -->
	<%-- <p class="btn  small" style="text-align:left; margin-bottom:5px; margin-top:10px; background: #fafafa; cursor: default;">	
	<span class="font-blue bold">Account Name:</span> <span ><%=gaAccount%></span></p>
	<p class="btn  small" style="text-align:left; margin-bottom:5px; margin-top:10px; background: #fafafa; cursor: default;"><span class="font-blue bold">Property Name: </span><span><%=gaProperty %></span></p>
	<p class="btn  small" style="text-align:left; margin-bottom:5px; margin-top:10px;background: #fafafa; cursor: default;"><span class="font-blue bold">View: </span><span><%=gaViewName %></span></p> --%>

<div style="padding-top:25px">
<span class="font-blue bold">Seletced View:</span> <span class="font-black small"><%=gaAccount%> > </span><span class="font-black small"><%=gaProperty %> > </span> <span class="font-black small"><%=gaViewName %></span>
</div>
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

<div style="text-align:center;hight: 25px;"><img id="loadingSpinner" src="/images/gaLoader.gif"></div>
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
		    'start-date': '30daysAgo',
		    'end-date': 'yesterday'
		  };
  
  var dataChart1 = new gapi.analytics.googleCharts.DataChart({
    query: {
      'ids': 'ga:<%=gaViewId%>', // <-- Replace with the ids value for your view.
      'metrics': 'ga:sessions,ga:percentNewSessions,ga:newUsers,ga:bounceRate,ga:pageviewsPerSession,ga:avgSessionDuration,ga:transactionRevenue',
      'dimensions': 'ga:medium,ga:source',
      /* 'filters': 'ga:campaign==BTSpinsSearchS1006,,ga:campaign==BTSpinsSearchS1010,ga:campaign==BTSpinsSearch880' */
      'filters': 'ga:campaign==<%=campaignName.replace("'", "\\\'")%>',
      'max-results':10000,
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
	$('.google-visualization-table-table thead tr th:first-child').before("<th></th>");	    	
	    
	  
	  $('.google-visualization-table-table tbody tr td:first-child').each(function() {	    	
			 var $this = $(this);
			 $this.before("<td></td>");
	    	 	    	
	    });
	  
	 $('.google-visualization-table-table tr td:nth-child(2)').each(function() {
		    var $this = $(this);
		    //var $source = $('.google-visualization-table-table tr td:nth-child(2)').text();
		    var s= $this.next("td").text();
		   
		 // $this.html("<a href='AnalyzLanding.jsp?ms="+$this.text()+"/"+s+"&cname=<%=campaignName.replace("'", "\\\'")%>&aid=<%=longAccountId%>'>"+$this.text()+"</a>");
			   // $this.html("<a href='AnalyzLanding.jsp?cname="+encodeURIComponent("<%=campaignName%>")+"&aid=<%=longAccountId%>&ms="+$this.text()+"/"+s+"'>"+$this.text()+"</a>");	
	 }); 
		    
		    $('.google-visualization-table-table tr td:nth-child(5)').each(function() {	    	
				 var $this = $(this);
				 $this.text(parseFloat($this.text()).toFixed(2));
		    	 $this.text($this.text() + "%");	    	
		    });
		  
		  $('.google-visualization-table-table tr td:nth-child(7)').each(function() {	    	
				 var $this = $(this);
				 $this.text(parseFloat($this.text()).toFixed(2));
		    	 $this.text($this.text() + "%");	    	
		    });
		  
		  $('.google-visualization-table-table tr td:nth-child(8)').each(function() {	    	
				 var $this = $(this);
				 $this.text(parseFloat($this.text()).toFixed(2));
		    	     	
		    });
		  
		$('.google-visualization-table-table tr td:nth-child(9)').each(function() {	    	
			 var $this = $(this);
			 var $formatedValue = secondstotime($this.text());
			 if ($formatedValue== "Invalid "){			   	
				 $this.text("00:00:00");
			   	}
			 else{
	    	 $this.text(secondstotime($this.text()));
			 }    	
		    });
		 
		$('.google-visualization-table-table tr td:nth-child(10)').each(function() {	    	
			 var $this = $(this);	  
			 $this.text(parseFloat($this.text()).toFixed(2));
	   	 $this.text("$" + $this.text());	    	
	   });
		
		 
		  $(".google-visualization-table-table").addClass("table table-striped table-bordered table-hover table-checkable order-column dataTable no-footer");
		  $(".google-visualization-table-table tr").removeAttr("class");
		  $(".google-visualization-table-table tr td").removeAttr("class");
		  $(".google-visualization-table-table").removeClass("google-visualization-table-table");
	  
	  
	  var t = $('.table').DataTable( {
		  colReorder: true,
	        "columnDefs": [ {
	           
	        	"targets": 0,
	            "orderable": false
	        } ],
	        "order": [[ 3, 'desc' ]]
	    } );
	 
	    t.on( 'order.dt search.dt', function () {
	        t.column(0, {search:'applied', order:'applied'}).nodes().each( function (cell, i) {
	            cell.innerHTML = i+1;
	        } );
	    } ).draw();
	    
	   
	    $(function() {
	        jQuery.each($(".table tr"), function() { 
	            $(this).children(":eq(2)").after($(this).children(":eq(1)"));
	            /* $(this).children(":eq(1)").html($(this).children(":eq(1)").text() + "/" + $(this).children(":eq(2)").text() ); */
	            $(this).children(":eq(1)").html($(this).children(":eq(1)").text() + "/" + $(this).children(":eq(2)").text() );
	            var $m = $(this).children(":eq(2)").text();
	            var $s = $(this).children( ":eq(1)").text();	
	            var $sm = $m + "/" + $s;
	            $(this).children(":eq(2)").remove();
	          
	            $(this).children(":eq(1)").html("<a href='AnalyzLanding.jsp?cname="+encodeURIComponent("<%=campaignName%>")+"&aid=<%=longAccountId%>&ms="+$s+"'>"+$s+"</a>");
            	
            	$("th a").contents().unwrap();
            	$(this).children("td:eq(1)").css("text-align", "left");
	        });
	    });
	    
	   
	
	  
	
	  $(".gapi-analytics-data-chart div").css("overflow", "hidden");
	  $(".DateRangeSelector-item input").addClass("font-green bold");
	  $(".google-visualization-table-sortind").hide();	 
	  
	  $( "input[type='date']" ).keydown(function(e) {
	  	   e.preventDefault();
	  	   return false;
	  	});
	  
	  
	  $('#loadingSpinner').hide();
	 
	});
  
  
	 dataChart1.on('error', function() {
		   window.location = "/AnalyticsSettings.jsp?aid=<%=longAccountId%>";
		 });
  
  
  var dateRangeSelector1 = new gapi.analytics.ext.DateRangeSelector({
	    container: 'date-range-selector-1-container'
	  })
	  .set(dateRange1)
	  .execute();
  
dateRangeSelector1.on('change', function(data) {
	$('#loadingSpinner').show();
	    dataChart1.set({query: data}).execute();
	    
	  });

dataChart1.execute();
});


$("li[title='Go To Campaign Page'] a").click(function(){
	
	goBack();
});


$( "input[type='date']" ).keydown(function(e) {
	   e.preventDefault();
	   return false;
	});
</script>
 <%= HtmlUtil.renderFooter() %>