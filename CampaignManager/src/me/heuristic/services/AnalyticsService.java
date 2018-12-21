package me.heuristic.services;



import me.heuristic.util.PMUtil;
import com.google.appengine.api.datastore.Entity;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import java.io.IOException;
import com.google.api.client.auth.oauth2.TokenResponseException;

/*import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import me.heuristic.references.AccountType;
import me.heuristic.references.Messages;
import me.heuristic.references.Porting;
import me.heuristic.references.UserType;
import com.google.appengine.api.oauth.OAuthService;
import com.google.appengine.api.oauth.OAuthServiceFactory;
import com.google.appengine.api.users.User;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import java.io.File;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import com.google.api.client.auth.oauth2.Credential;

import com.google.api.client.googleapis.extensions.appengine.auth.oauth2.AppIdentityCredential;
import com.google.appengine.api.datastore.Cursor;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.QueryResultList;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.oauth.OAuthRequestException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;*/

/*import com.google.api.services.analyticsreporting.v4.AnalyticsReporting;
import com.google.api.services.analyticsreporting.v4.AnalyticsReportingScopes;
import com.google.api.services.analyticsreporting.v4.model.ColumnHeader;
import com.google.api.services.analyticsreporting.v4.model.DateRange;
import com.google.api.services.analyticsreporting.v4.model.DateRangeValues;
import com.google.api.services.analyticsreporting.v4.model.Dimension;
import com.google.api.services.analyticsreporting.v4.model.GetReportsRequest;
import com.google.api.services.analyticsreporting.v4.model.GetReportsResponse;
import com.google.api.services.analyticsreporting.v4.model.Metric;
import com.google.api.services.analyticsreporting.v4.model.MetricHeaderEntry;
import com.google.api.services.analyticsreporting.v4.model.Report;
import com.google.api.services.analyticsreporting.v4.model.ReportRequest;
import com.google.api.services.analyticsreporting.v4.model.ReportRow;
*/


public class AnalyticsService{
	
		private static final String CLIENT_SECRET_JSON_RESOURCE = "/resources/campaignalyzer-9bc2ec5862d4.json";
		private HttpTransport httpTransport;
		
		/*public static AppIdentityCredential getAccessTokenFromServiceAccount(){
			AppIdentityCredential credential =
				    new AppIdentityCredential(CLIENT_SECRET_JSON_RESOURCE, AnalyticsReportingScopes.ANALYTICS_READONLY);
			
			return credential;
			
			
		}*/
	
	/*AnalyticsReporting coreReport =
		    new AnalyticsReporting.Builder(httpTransport, CLIENT_SECRET_JSON_RESOURCE, credential).build();*/
	
	
/*	
$client = new Google_Client();
$client->setAuthConfig('client_secrets.json');
$client->addScope(Google_Service_Drive::DRIVE_METADATA_READONLY);

if (isset($_SESSION['access_token']) && $_SESSION['access_token']) {
  $client->setAccessToken($_SESSION['access_token']);
  $drive_service = new Google_Service_Drive($client);
  $files_list = $drive_service->files->listFiles(array())->getItems();
  echo json_encode($files_list);
} else {
  $redirect_uri = 'http://' . $_SERVER['HTTP_HOST'] . '/oauth2callback.php';
  header('Location: ' . filter_var($redirect_uri, FILTER_SANITIZE_URL));
}*/
	
	public static void SaveViewId(long longAccountId, String gaViewId, String gaViewName, String gaAccount, String gaProperty){
		
		//Entity myAccount = null;
		if (longAccountId != 0){
			Entity myAccount = AccountService.getSingleAccount(longAccountId);
			myAccount.setProperty("gaViewId", gaViewId);
			myAccount.setProperty("gaViewName", gaViewName);
			myAccount.setProperty("gaAccount", gaAccount);
			myAccount.setProperty("gaProperty", gaProperty);
			PMUtil.persistEntity(myAccount);
		}
	}
	
	public static String GetViewId(long longAccountId){
		
		Entity myAccount = null;
		String gaViewId="";
		if (longAccountId != 0){
			myAccount = AccountService.getSingleAccount(longAccountId);
			gaViewId = (String) myAccount.getProperty("gaViewId");
			
		}
		return gaViewId;
	}
	
	public static String GetAccessKey(long longAccountId){
		
		Entity myAccount = null;
		String accessKey="";
		if (longAccountId != 0){
			myAccount = AccountService.getSingleAccount(longAccountId);
			accessKey = (String) myAccount.getProperty("accessToken");
			
		}
		return accessKey;
	}
	
	public static String GetRefreshKey(long longAccountId){
			
			Entity myAccount = null;
			String refreshKey="";
			if (longAccountId != 0){
				myAccount = AccountService.getSingleAccount(longAccountId);
				refreshKey = (String) myAccount.getProperty("refreshToken");
				
			}
			return refreshKey;
		}
		

	public static void requestAccessToken(NetHttpTransport netHttpTransport,
			com.google.api.client.json.jackson2.JacksonFactory jacksonFactory,
			String cLIENT_ID, String cLIENT_SECRET,String authcode, String redirectUri,
			Entity myAccount) throws IOException {	
	
		 try {
	        	/*longAccountId = HtmlUtil.handleNullforLongNumbers(req.getSession().getAttribute("aid").toString());*/
	        	
	            GoogleTokenResponse response =
	                new GoogleAuthorizationCodeTokenRequest(new NetHttpTransport(), new JacksonFactory(),
	                		cLIENT_ID, cLIENT_SECRET, authcode, redirectUri).execute();
	            
	            
	            System.out.println("Access token: " + response.getAccessToken());
	            String accessToken = response.getAccessToken();
                String refreshToken = response.getRefreshToken();
                //resp.getWriter().println("Access token: " + accessToken);
                //resp.getWriter().println("Refresh token: " + refreshToken);

                myAccount.setProperty("accessToken", accessToken);
    			myAccount.setProperty("refreshToken", refreshToken);
    			PMUtil.persistEntity(myAccount);
	            
	          } catch (TokenResponseException e) {
	            if (e.getDetails() != null) {
	              System.err.println("Error: " + e.getDetails().getError());
	              if (e.getDetails().getErrorDescription() != null) {
	                System.err.println(e.getDetails().getErrorDescription());
	              }
	              if (e.getDetails().getErrorUri() != null) {
	                System.err.println(e.getDetails().getErrorUri());
	              }
	            } else {
	              System.err.println(e.getMessage());
	            }
	          }
	  }
	
	
	
	public static void requestAccessTokenUsingRefreshToken(NetHttpTransport netHttpTransport,
			com.google.api.client.json.jackson2.JacksonFactory jacksonFactory,
			String cLIENT_ID, String cLIENT_SECRET,String refreshToken, String redirectUri,
			Entity myAccount) throws IOException {	
	
		 try {
	        	/*longAccountId = HtmlUtil.handleNullforLongNumbers(req.getSession().getAttribute("aid").toString());*/
	        	
	            GoogleTokenResponse response =
	                new GoogleAuthorizationCodeTokenRequest(new NetHttpTransport(), new JacksonFactory(),
	                		cLIENT_ID, cLIENT_SECRET, refreshToken, redirectUri).execute();
	            
	            
	            System.out.println("Access token: " + response.getAccessToken());
	            String accessToken = response.getAccessToken();
                //String refreshToken = response.getRefreshToken();
                //resp.getWriter().println("Access token: " + accessToken);
                //resp.getWriter().println("Refresh token: " + refreshToken);

                myAccount.setProperty("accessToken", accessToken);
    			//myAccount.setProperty("refreshToken", refreshToken);
    			PMUtil.persistEntity(myAccount);
	            
	          } catch (TokenResponseException e) {
	            if (e.getDetails() != null) {
	              System.err.println("Error: " + e.getDetails().getError());
	              if (e.getDetails().getErrorDescription() != null) {
	                System.err.println(e.getDetails().getErrorDescription());
	              }
	              if (e.getDetails().getErrorUri() != null) {
	                System.err.println(e.getDetails().getErrorUri());
	              }
	            } else {
	              System.err.println(e.getMessage());
	            }
	          }
	  }
	
	public static void getAccessTokenFromRefreshToken(NetHttpTransport netHttpTransport,
			com.google.api.client.json.jackson2.JacksonFactory jacksonFactory,
			String cLIENT_ID, String cLIENT_SECRET, String REFRESH_TOKEN, String redirectUri,
			Entity myAccount) throws IOException {
	    GoogleCredential credentials = new GoogleCredential.Builder()
	        .setClientSecrets(cLIENT_ID, cLIENT_SECRET)
	        .setJsonFactory(jacksonFactory)
	        .setTransport(netHttpTransport).build()	        
	        .setRefreshToken(REFRESH_TOKEN);

	    String accessToken = credentials.getAccessToken();
	    System.out.println("Access token before: " + accessToken);
	    System.out.println("Refresh token: " + REFRESH_TOKEN);
	    credentials.refreshToken();
	   
	    
	    
	    accessToken = credentials.getAccessToken();
	    System.out.println("Access token after: " + accessToken);
	    System.out.println("Refresh token: " + REFRESH_TOKEN);
	    
	    myAccount.setProperty("accessToken", accessToken);
		//myAccount.setProperty("refreshToken", refreshToken);
		PMUtil.persistEntity(myAccount);
	}

	
	public static void disconnectGA(Entity myAccount){		
		myAccount.setProperty("refreshToken", null);
		myAccount.setProperty("accessToken", null);
		myAccount.setProperty("gaAccount", null);
		myAccount.setProperty("gaProperty", null);
		myAccount.setProperty("gaViewId", null);		
		myAccount.setProperty("gaViewName", null);
		
		PMUtil.persistEntity(myAccount);
	}
		

}