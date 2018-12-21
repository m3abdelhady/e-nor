package me.heuristic.servlets;

/*import com.google.api.client.googleapis.auth.oauth2.*;
import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.auth.oauth2.AuthorizationCodeTokenRequest;
import com.google.api.client.auth.oauth2.TokenResponseException;
import com.google.api.client.http.BasicAuthentication;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;*/
import com.google.api.client.http.javanet.NetHttpTransport;
/*import com.google.api.services.analytics.AnalyticsScopes;*/
import com.google.appengine.api.datastore.Entity;
/*import com.google.api.client.json.JsonFactory;
import com.google.api.services.analytics.Analytics;*/
/*import com.google.api.services.analyticsreporting.v4.*;*/
/*import com.google.api.client.util.GenericData;
import com.google.api.client.auth.oauth2.TokenRequest;
import com.google.api.client.auth.oauth2.AuthorizationCodeTokenRequest;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest;*/

import java.io.IOException;
/*import java.util.Collections;
import java.util.Set;*/

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import me.heuristic.services.AccountService;
import me.heuristic.services.AnalyticsService;
import me.heuristic.util.HtmlUtil;
/*import me.heuristic.util.PMUtil;*/

import com.google.api.client.json.jackson2.JacksonFactory;
/*import com.google.api.client.json.*;
import com.sun.corba.se.impl.oa.poa.AOMEntry;*/


@SuppressWarnings("serial")
public class AnalyticsCallBackServlet extends HttpServlet {
    public void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
    	doPost(req, resp);
    }

    public void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
    	//HttpSession session = req.getSession(true);
    	 // final JacksonFactory JSON_FACTORY = new JacksonFactory();
		  final String CLIENT_ID = "458758013131-ib24rrpblt0ltk24enis2ggk189cq2o5.apps.googleusercontent.com";
		 
		  final String CLIENT_SECRET = "ZTaqdVxfxzaZLVh5D_dRztyW";		
		
		  Entity myAccount = null;
				
			/*String CLIENT_SECRET_FILE = "http://localhost:8888/resources/client_secret.json";*/
			
			//String CLIENT_SECRET_FILE = "https://4-0-dot-campaignalyzer.appspot.com/resources/client_secret.json";
	
			
			//resp.sendRedirect("/AnalyticsSettings.jsp?code="+code+"&aid="+longAccountId);
			
			String authcode = req.getParameter("code");
			String distination = HtmlUtil.handleNullWhiteSpaces(req.getParameter("dis"));
			String act = HtmlUtil.handleNullWhiteSpaces(req.getParameter("act"));
	        //String clientID = "458758013131-ib24rrpblt0ltk24enis2ggk189cq2o5.apps.googleusercontent.com";
	       // String clientSecret = "ZTaqdVxfxzaZLVh5D_dRztyW";
			
	       
			/*String redirectUri = "http://localhost:8888/AnalyticsSettings.jsp";*/
	      
			/*String redirectUri = "https://4-0-dot-campaignalyzer.appspot.com/AnalyticsCallBack";*/
			
			String redirectUri = "https://app.campaignalyzer.com/AnalyticsCallBack";
	       
			/*String redirectUri = "http://localhost:8888/AnalyticsSettings.jsp";*/
			
			/*String redirectUri = "/defaultEmbedApi.jsp";*/
			
			/*String redirectUri = "http://localhost:8888/AnalyticsCallBack";*/
			
			/*String tokenServerURL = "https://accounts.google.com/AccountChooser?continue=https://accounts.google.com/o/oauth2/v2/auth?access_type%3Doffline%26scope%3Dhttps://www.googleapis.com/auth/admin.reports.usage.readonly%2Bhttps://www.googleapis.com/auth/analytics.readonly%26response_type%3Dcode%26redirect_uri%3Dhttps://4-0-dot-campaignalyzer.appspot.com/AnalyticsCallBack%26client_id%3D458758013131-ib24rrpblt0ltk24enis2ggk189cq2o5.apps.googleusercontent.com%26from_login%3D1%26as%3D6b7f8dbe90f05f73%26prompt%3Dconsent%26btmpl=authsub&scc=1&oauth=1";
	       	String accessToken = "";
	        String refreshToken = "";*/
	       // String act = req.getParameter("act");
	       // String viewId = req.getParameter("gaView");
	        
	        /*long longAccountId = 0;*/
	        
	        
	        long longAccountId = HtmlUtil.handleNullforLongNumbers(req.getSession().getAttribute("aid").toString());
	      
	        myAccount = AccountService.getSingleAccount(longAccountId);
	        
	        final String REFRESH_TOKEN = (String)myAccount.getProperty("refreshToken");
	        /*String refreshToken = (String)myAccount.getProperty("refreshToken");*/
	        
	      if(authcode == null){
	    	  
	       
	       if(act.contains("disconnect")){
	        	
	        	AnalyticsService.disconnectGA(myAccount);
	        	resp.sendRedirect("/AnalyticsSettings.jsp?act=disconnected&aid="+longAccountId);
	        }
	       
	       else{
	    	   
	    	   
	    	   if(REFRESH_TOKEN != null || REFRESH_TOKEN !=""){
		        	//refreshToken = (String)myAccount.getProperty("refreshToken");
		        	AnalyticsService.getAccessTokenFromRefreshToken(new NetHttpTransport(), new JacksonFactory(), CLIENT_ID, CLIENT_SECRET, REFRESH_TOKEN, redirectUri, myAccount);
		        	
		        	if(distination.contentEquals("analyticsReports")){
		        		resp.sendRedirect("/AnalyticsReports.jsp?aid="+longAccountId);
		        		
		        	}
		        	

		        	else if(distination.contentEquals("AnalyzMediumSource")){
		        		String cname = req.getParameter("cname");
		        		resp.sendRedirect("/AnalyzSourceMedium.jsp?cname="+cname+"&aid="+longAccountId);
		        		
		        	}
		        	
		        	else if(distination.contentEquals("AnalyzLanding")){
		        		String cname = req.getParameter("cname");
		        		String ms = req.getParameter("ms");
		        		resp.sendRedirect("/AnalyzLanding.jsp?ms="+ms+"&cname="+cname+"&aid="+longAccountId);
		        	}
		        	
		        	
		        	else{
		        		resp.sendRedirect("/AnalyticsSettings.jsp?dis="+distination+"&aid="+longAccountId);
		        	}
		        
		        }	 
	    	  
	    	   
		       
	       }
	       // AnalyticsService.requestAccessTokenUsingRefreshToken(new NetHttpTransport(), new JacksonFactory(), CLIENT_ID, CLIENT_SECRET, REFRESH_TOKEN, redirectUri, myAccount);
	        
	      }
	      
	      else{		        	
	        	AnalyticsService.requestAccessToken(new NetHttpTransport(), new JacksonFactory(), CLIENT_ID, CLIENT_SECRET, authcode, redirectUri, myAccount);
	        	
	        		resp.sendRedirect("/AnalyticsSettings.jsp?aid="+longAccountId);		        		
	        	
	        }
	        			
	        			
	        			   
	        			  

	        	  
	        	  
		
    }
    
}       
