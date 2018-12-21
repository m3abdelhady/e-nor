
<!DOCTYPE html>
<html lang="en">
    <!--<![endif]-->
    <!-- BEGIN HEAD -->

    <head>
   
   <!--  <meta name="google-signin-scope" content="profile email https://www.googleapis.com/auth/analytics.readonly https://www.googleapis.com/auth/plus.login" > -->
    <meta name="google-signin-scope" content="email" >
    <meta name="google-signin-client_id" content="458758013131-ib24rrpblt0ltk24enis2ggk189cq2o5.apps.googleusercontent.com">
    <meta name="google-signin-button-text" content="Login">
<!--     <meta name="google-signin-prompt" content="select_account">
 -->    <script src="https://apis.google.com/js/platform.js" async defer></script>
    
        <meta charset="utf-8" />
        <title>CampaignAlyzer | User Login</title>
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta content="width=device-width, initial-scale=1" name="viewport" />
        <meta content="" name="description" />
        <meta content="" name="author" />
        <!-- BEGIN GLOBAL MANDATORY STYLES -->
        <link href="https://fonts.googleapis.com/css?family=Open+Sans:400,300,600,700&subset=all" rel="stylesheet" type="text/css" />
        <link href="assets/global/plugins/font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css" />
        <link href="assets/global/plugins/simple-line-icons/simple-line-icons.min.css" rel="stylesheet" type="text/css" />
        <link href="assets/global/css/components.min.css" rel="stylesheet" id="style_components" type="text/css" />
        <link href="assets/global/css/plugins.min.css" rel="stylesheet" type="text/css" />
        <!-- END THEME GLOBAL STYLES -->
        <!-- BEGIN PAGE LEVEL STYLES -->
        <link href="assets/pages/css/login.min.css" rel="stylesheet" type="text/css" />
        <!-- END PAGE LEVEL STYLES -->
        <!-- BEGIN THEME LAYOUT STYLES -->
        <!-- END THEME LAYOUT STYLES -->
        <link rel="shortcut icon" href="icon.png"><style type="text/css">.jqstooltip { position: absolute;left: 0px;top: 0px;visibility: hidden;background: rgb(0, 0, 0) transparent;background-color: rgba(0,0,0,0.6);filter:progid:DXImageTransform.Microsoft.gradient(startColorstr=#99000000, endColorstr=#99000000);-ms-filter: "progid:DXImageTransform.Microsoft.gradient(startColorstr=#99000000, endColorstr=#99000000)";color: white;font: 10px arial, san serif;text-align: left;white-space: nowrap;padding: 5px;border: 1px solid white;z-index: 10000;}.jqsfield { color: white;font: 10px arial, san serif;text-align: left;}</style>
       
       
        <style>
        .abcRioButtonIcon{display:none;}
        
        .abcRioButton{margin:0 auto !important; background:#32c5d2 !important; color:#fff; }
        
        .abcRioButtonContents{ font-size:16px !important; font-weight:bold !important; font-family:"Open Sans",sans-serif !important}
        
        .createNewbtn {
    position: relative;
    top: -26px;
    opacity: 0;
}
.login .content .create-account{margin:0 -40px -60px;}
a.uppercase {text-decoration:none;}
.create-account:hover a{text-decoration:underline;}
        </style>
        </head>
    <!-- END HEAD -->

    <body class=" login" style="padding-top:100px !important;'">
    <!-- Google Tag Manager -->
<noscript><iframe src="//www.googletagmanager.com/ns.html?id=GTM-MBXDLW"
height="0" width="0" style="display:none;visibility:hidden"></iframe></noscript>
<script>(function(w,d,s,l,i){w[l]=w[l]||[];w[l].push({'gtm.start':
new Date().getTime(),event:'gtm.js'});var f=d.getElementsByTagName(s)[0],
j=d.createElement(s),dl=l!='dataLayer'?'&l='+l:'';j.async=true;j.src=
'//www.googletagmanager.com/gtm.js?id='+i+dl;f.parentNode.insertBefore(j,f);
})(window,document,'script','dataLayer','GTM-MBXDLW');</script>
<!-- End Google Tag Manager -->
    
        <div class="menu-toggler sidebar-toggler"></div>
        <!-- END SIDEBAR TOGGLER BUTTON -->
        <!-- BEGIN LOGO -->
        
        <div class="logo" style="
    width: 430px;
    background: #6c7a8d;
">
        
            <a href="/">
                <img src="/images/campaignalyzer-icon-100px.png" alt="Logo" width="60"><img src="/images/logo_white_small.png" alt=""></a>
                
        </div>
        <!-- END LOGO -->
        <!-- BEGIN LOGIN -->
        <div class="content" style="margin-top:0">
            <!-- BEGIN LOGIN FORM -->
            <form class="login-form" id="login-form" action="/SecurityCheck" method="post">
            <input type="hidden" value="" name="myEmail" id="myEmail"/>
            <input type="hidden" value="" name="myPhoto" id="myPhoto"/>
            <input type="hidden" value="" name="createAction" id="createAction"/>
            <%String planID = request.getParameter("pid"); %>
            <%if(planID != null){%>
            <input type="hidden" value="<%=planID %>" name="planID" id="planID"/>
            <%} %>
               <!--  <h3 class="form-title font-green">Sign In</h3> -->
               
                <div class="form-actions" style='text-align:center'>
                  <%-- <a  href="https://accounts.google.com/ServiceLogin?service=ah&amp;passive=true&amp;continue=https://appengine.google.com/_ah/conflogin%3Fcontinue%3Dhttps://app.campaignalyzer.com/<%=continuer %>&amp;ltmpl=gm&amp;shdf=ChoLEgZhaG5hbWUaDkNhbXBhaWduQWx5emVyDBICYWgiFNBOzEWPYMt9J-rlEEb1ZDYkkFUPKAEyFGhT54VaXjWLCaFgxgKSbiztIoav" class="btn green uppercase">Login</a> --%> 
                    <!-- <a  href="https://accounts.google.com/ServiceLogin?service=ah&passive=true&continue=https%3A%2F%2Fappengine.google.com%2F_ah%2Fconflogin%3Fcontinue%3Dhttps%3A%2F%2F5-dot-m-campaignalyzer.appspot.com/landing.jsp&ltmpl=gm&shdf=ChwLEgZhaG5hbWUaEG0tY2FtcGFpZ25hbHl6ZXIMEgJhaCIUd-ISOuvWEE5IYaa69GrTUXLtv5goATIUn8vlVxl3Z69UDk_whKEwO2nMuFE" class="btn green uppercase">Login</a> -->
                    <!-- <a href="https://accounts.google.com/o/oauth2/v2/auth?client_id=369501443736-k8sv9gts04586jhhvdmd1gei52e15ipl.apps.googleusercontent.com&response_type=token&scope=openid%20email&redirect_uri=https://5-dot-m-campaignalyzer.appspot.com" class="btn green uppercase">Login</a> -->
                   <!--  <a href="https://accounts.google.com/o/oauth2/v2/auth?client_id=458758013131-ib24rrpblt0ltk24enis2ggk189cq2o5.apps.googleusercontent.com&response_type=token&scope=openid%20email&redirect_uri=http://localhost:8888/landing.jsp" class="btn green uppercase">Login</a> -->
                    <div class="g-signin2"  id ="lb" data-onsuccess="onSignIn" data-theme="dark" style="padding-top:20px;"></div>
    <p>Sign in to CampainAlyzer using your Gmail or Google Account</p>
                </div>              
                <div class="create-account">
                   <p>
                        <!-- <a href="https://accounts.google.com/ServiceLogin?service=ah&amp;passive=true&amp;continue=https://appengine.google.com/_ah/conflogin%3Fcontinue%3Dhttps://app.campaignalyzer.com/EditAccount.jsp&amp;ltmpl=gm&amp;shdf=ChoLEgZhaG5hbWUaDkNhbXBhaWduQWx5emVyDBICYWgiFJza2oou7Jn-LgYNsyidpuMF5wuZKAEyFErnjFi111XC4CoQ_mUXGnCj7Q20" class="uppercase">Create an account</a> -->
                   <a href="https://accounts.google.com/ServiceLogin?service=ah&amp;passive=true&amp;continue=https://appengine.google.com/_ah/conflogin%3Fcontinue%3Dhttps://5-dot-m-campaignalyzer.appspot.com/EditAccount.jsp&amp;ltmpl=gm&amp;shdf=ChoLEgZhaG5hbWUaDkNhbXBhaWduQWx5emVyDBICYWgiFJza2oou7Jn-LgYNsyidpuMF5wuZKAEyFErnjFi111XC4CoQ_mUXGnCj7Q20" class="uppercase">Create an account</a>
                    </p>
                    <div class="g-signin2 createNewbtn" data-onsuccess="onSignIn" data-theme="dark" onclick="$('#createAction').val('1');"></div>
                    
                </div> 
                
            </form>
                 <form id="create-form" action="/SecurityCheck">
                 <input type="hidden" value="" name="myEmail" id="myEmail">
                 <input type="hidden" value="" name="myPhotoc" id="myPhoto">
                 <input type="hidden" value="" name="action" id="action"/>
                 </form>      
            <!-- END LOGIN FORM -->     
 <script>
 
 /* function onCreateNew(googleUser) {
	 
     // Useful data for your client-side scripts:
     var profile = googleUser.getBasicProfile();
     console.log("ID: " + profile.getId()); // Don't send this directly to your server!
     console.log("Name: " + profile.getName());
     console.log("Image URL: " + profile.getImageUrl());
     console.log("Email: " + profile.getEmail());

     // The ID token you need to pass to your backend:
     var id_token = googleUser.getAuthResponse().id_token;
     console.log("ID Token: " + id_token);
     
     var jemail = profile.getEmail();
     $("#myEmailc").val(profile.getEmail());
     $("#myPhotoc").val(profile.getImageUrl());
     //$("action").val("1");
     //alert($("#myEmail").val());    
   	// $("#create-form").submit(); 
   	 //preventDefault();
   //  window.location = "/NewAccount.jsp";
     $("#login-form").submit();
   
   };  */
   
   
      function onSignIn(googleUser) {
    	 
        // Useful data for your client-side scripts:
        var profile = googleUser.getBasicProfile();
        console.log("ID: " + profile.getId()); // Don't send this directly to your server!
        console.log("Name: " + profile.getName());
        console.log("Image URL: " + profile.getImageUrl());
        console.log("Email: " + profile.getEmail());

        // The ID token you need to pass to your backend:
        var id_token = googleUser.getAuthResponse().id_token;
        console.log("ID Token: " + id_token);
        //window.location = "/TestEmail.jsp?email="+profile.getEmail();
        var jemail = profile.getEmail();
        $("#myEmail").val(profile.getEmail());
        $("#myPhoto").val(profile.getImageUrl());
        //$("action").val();
        //alert($("#myEmail").val());       
       
        $("#login-form").submit(); 
        
      };      
     
      
    </script>           
        </div>
        <div class="copyright"> &copy; Copyright 2016 CampaignAlyzer. All rights reserved  </div>
        <!--[if lt IE 9]>
<script src="assets/global/plugins/respond.min.js"></script>
<script src="assets/global/plugins/excanvas.min.js"></script> 
<![endif]-->
        <!-- BEGIN CORE PLUGINS -->
        <script src="assets/global/plugins/jquery.min.js" type="text/javascript"></script>
        <script src="assets/global/plugins/bootstrap/js/bootstrap.min.js" type="text/javascript"></script>
      
        <!-- BEGIN THEME GLOBAL SCRIPTS -->
        <script src="assets/global/scripts/app.min.js" type="text/javascript"></script>
   
    </body>

</html>