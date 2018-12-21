package me.heuristic.servlets;

/*import com.google.api.client.extensions.appengine.http.UrlFetchTransport;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.auth.oauth2.RefreshTokenRequest;
import com.google.api.client.auth.oauth2.TokenErrorResponse;
import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.auth.oauth2.TokenResponseException;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.LowLevelHttpRequest;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.services.analytics.Analytics;
import com.google.api.services.analytics.AnalyticsScopes;*/
/*import com.google.api.services.analyticsreporting.v4.AnalyticsReporting;
import com.google.api.services.analyticsreporting.v4.AnalyticsReporting.Builder;*/
/*import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

import com.google.api.client.auth.oauth2.CredentialRefreshListener;

import me.heuristic.services.AUserService;
*/








/*import com.google.appengine.api.datastore.Entity;*/

import me.heuristic.services.AnalyticsService;

/*import java.io.FileInputStream;
import java.io.FileReader;

import java.io.InputStreamReader;
import java.io.InputStream;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import me.heuristic.services.AccountService;*/

import java.io.IOException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import me.heuristic.util.HtmlUtil;






/*import me.heuristic.util.PMUtil;

import javax.servlet.ServletException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.JsonGenerator;
import com.google.api.client.json.JsonParser;
import com.google.api.client.json.JsonToken;
import com.google.api.client.util.Beta;
import com.google.api.client.util.Preconditions;
import com.google.api.client.json.jackson2.JacksonFactory;*/


/*import com.google.common.collect.ImmutableMap;*/

/*@SuppressWarnings("serial")*/
public class AnalyticsInfoHandlerServlet extends HttpServlet {
	   /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
    	doPost(req, resp);
    }  
 
	 
	  public void doPost(HttpServletRequest req, HttpServletResponse resp)
	            throws IOException {
		  
		 /* final UrlFetchTransport HTTP_TRANSPORT = new UrlFetchTransport();
		  final HttpTransport transporter = new HttpTransport() {
			
			@Override
			protected LowLevelHttpRequest buildPutRequest(String url)
					throws IOException {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			protected LowLevelHttpRequest buildPostRequest(String url)
					throws IOException {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			protected LowLevelHttpRequest buildGetRequest(String url)
					throws IOException {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			protected LowLevelHttpRequest buildDeleteRequest(String url)
					throws IOException {
				// TODO Auto-generated method stub
				return null;
			}
		};*/
		  
		  /*final JacksonFactory JSON_FACTORY = new JacksonFactory();
		  final String CLIENT_ID = "458758013131-ib24rrpblt0ltk24enis2ggk189cq2o5.apps.googleusercontent.com";
		  final String CLIENT_SECRET = "ZTaqdVxfxzaZLVh5D_dRztyW";
		  final Set<String> SCOPES = Collections.singleton(AnalyticsScopes.ANALYTICS_READONLY);*/
		
		
		
		/*Entity myAccount = null;
		
		
		String CLIENT_SECRET_FILE = "http://localhost:8888/resources/client_secret.json";
*/
		
		
		//resp.sendRedirect("/AnalyticsSettings.jsp?code="+code+"&aid="+longAccountId);
		
	/*	String authcode = req.getParameter("code");
        String clientID = "458758013131-ib24rrpblt0ltk24enis2ggk189cq2o5.apps.googleusercontent.com";
        String clientSecret = "ZTaqdVxfxzaZLVh5D_dRztyW";
        String redirectUri = "http://localhost:8888/AnalyticsCallBack";
        String accessToken = "";
        String refreshToken = "";
        String act = req.getParameter("act");*/
       /* StringBuilder returnUrl = new StringBuilder();*/
        String gaViewId = req.getParameter("gaView");
        String gaViewName = req.getParameter("gaViewName");
        String gaAccount = req.getParameter("gaAccount");
        String gaProperty = req.getParameter("gaProperty");
        /*String myToken = req.getParameter("myToken");*/
        
        //GoogleCredential credentials = null;
        long longAccountId = HtmlUtil.handleNullforLongNumbers(req.getSession().getAttribute("aid").toString());


			//if(act == "saveViewId" || act.contentEquals("saveViewId")){
				
				AnalyticsService.SaveViewId(longAccountId, gaViewId, gaViewName, gaAccount, gaProperty);
				resp.sendRedirect("/AnalyticsSettings.jsp?aid="+longAccountId+"&info=View has been savedv"+gaViewId);
			//}
			//resp.sendRedirect("/AnalyticsSettings.jsp?info=View has not been saved&token="+myToken);
						
					
			
			/*resp.sendRedirect("/AnalyticsSettings.jsp?token="+accessToken);*/
				
		
	  }
	  
	  
	 
	
}