<!DOCTYPE html>
<html>
<head>
    <title>Embed API Demo</title>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap.min.css">
    <script src="https://code.jquery.com/jquery-2.1.4.min.js"></script>
    <!-- <script src="moment.min.js"></script> -->
</head>
<body>
<%String myToken = request.getParameter("token"); %>

<div class="container">
    <div><a href="https://accounts.google.com/AccountChooser?continue=https://accounts.google.com/o/oauth2/v2/auth?access_type%3Doffline%26scope%3Dhttps://www.googleapis.com/auth/admin.reports.usage.readonly%2Bhttps://www.googleapis.com/auth/analytics.readonly%26response_type%3Dcode%26redirect_uri%3Dhttp://localhost:8888/AnalyticsCallBack%26client_id%3D458758013131-ib24rrpblt0ltk24enis2ggk189cq2o5.apps.googleusercontent.com%26from_login%3D1%26as%3D6b7f8dbe90f05f73%26prompt%3Dconsent%26btmpl=authsub&scc=1&oauth=1">Authroize</a></div>
    <section id="auth-button"></section>
    
    <h2>RaymondCamden.com</h2>
    <div id="status"></div>
    <table id="dataTable" class="table">
        <thead><tr><th>Date</th><th>Sessions</th><th>Avg. Session Duration</th></tr></thread>
        <tbody></tbody>
    </table>

</div>

<script>
(function(w,d,s,g,js,fjs){
  g=w.gapi||(w.gapi={});g.analytics={q:[],ready:function(cb){this.q.push(cb)}};
  js=d.createElement(s);fjs=d.getElementsByTagName(s)[0];
  js.src='https://apis.google.com/js/platform.js';
  fjs.parentNode.insertBefore(js,fjs);js.onload=function(){g.load('analytics')};
}(window,document,'script'));
</script>

<script>
var $tbody;
var $status;
var intl = false;

$(document).ready(function() {
    
    if(window.Intl) intl = true;
    
    $tbody = $("#dataTable tbody");
    $status = $("#status");
    $status.html("<i>Please stand by - loading data...</i>");

    gapi.analytics.ready(function() {
    
    
        var CLIENT_ID = '369501443736-k8sv9gts04586jhhvdmd1gei52e15ipl.apps.googleusercontent.com';
        
        gapi.analytics.auth.authorize({
        	
        	 
            container: 'auth-button',
            clientid: '369501443736-k8sv9gts04586jhhvdmd1gei52e15ipl.apps.googleusercontent.com',
            userInfoLabel:""
        });
       
        gapi.analytics.auth.on('success', function(response) {
            //hide the auth-button
            /* document.querySelector("#auth-button").style.display='none'; */ 
            
            gapi.client.analytics.data.ga.get({
                'ids': 'ga:8292058',
                'metrics': 'ga:sessions,ga:percentNewSessions,ga:newUsers,ga:bounceRate,ga:avgSessionDuration,ga:transactionRevenue',
                'dimensions': 'ga:campaign',
                
                
            }).execute(function(results) {
                $status.html("");
                renderData(results.rows);
            });
        
    
        });
    
    });
    
});


function formatDate(str) {
    var year = str.substring(0,4);
    var month = str.substring(4,6);
    var day = str.substring(6,8);
    if(intl) {
        var d = new Date(year,month-1,day); 
        return new Intl.DateTimeFormat().format(d);
    } 
    return month + "/" + day + "/" + year;
}

function formatNumber(str) {
    if(intl) return new Intl.NumberFormat().format(str);
    return str;
}

/* function formatTime(str) {
    var dur = moment.duration(parseInt(str,10), 'seconds');
    var minutes = dur.minutes();
    var seconds = dur.seconds();
    return minutes + " minutes and " + seconds + " seconds";
} */

function renderData(data) {
    var s = "";
    for(var i=0;i<data.length;i++) {
        s += "<tr><td>" + formatDate(data[i][0]) + "</td>";
        s += "<td>" + formatNumber(data[i][1]) + "</td>";
        //s += "<td>" + formatTime(data[i][2]) + "</td></tr>";
    }
    $tbody.html(s);
}
</script>
</body>
</html>