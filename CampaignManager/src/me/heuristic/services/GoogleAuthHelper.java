package me.heuristic.services;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.auth.oauth2.TokenResponseException;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeRequestUrl;
/*import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;*/
import com.google.api.client.googleapis.auth.oauth2.GoogleRefreshTokenRequest;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
/*import com.google.api.client.json.jackson.JacksonFactory;*/
import com.google.appengine.api.oauth.OAuthRequestException;
/*import com.google.appengine.api.oauth.OAuthServiceFactory;
import com.google.appengine.api.users.UserServiceFactory;*/
import com.google.appengine.labs.repackaged.org.json.JSONArray;
import com.google.appengine.labs.repackaged.org.json.JSONException;
import com.google.appengine.labs.repackaged.org.json.JSONObject;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.Arrays;

/**
 * A helper class for Google's OAuth2 authentication API.
 * @version 20130224
 * @author Matyas Danter
 */
public final class GoogleAuthHelper {

	/**
	 * Please provide a value for the CLIENT_ID constant before proceeding, set this up at https://code.google.com/apis/console/
	 */
	private static final String CLIENT_ID = "458758013131-ib24rrpblt0ltk24enis2ggk189cq2o5.apps.googleusercontent.com";
	/**
	 * Please provide a value for the CLIENT_SECRET constant before proceeding, set this up at https://code.google.com/apis/console/
	 */
	private static final String CLIENT_SECRET = "ZTaqdVxfxzaZLVh5D_dRztyW";

	/**
	 * Callback URI that google will redirect to after successful authentication
	 */
	private static final String CALLBACK_URI = "http://localhost:8888/auth.jsp";
	
	// start google authentication constants
	private static final Iterable<String> SCOPE = Arrays.asList("https://www.googleapis.com/auth/userinfo.profile;https://www.googleapis.com/auth/userinfo.email".split(";"));
	private static final String USER_INFO_URL = "https://www.googleapis.com/oauth2/v1/userinfo";
	/*private static final JsonFactory JSON_FACTORY = new JacksonFactory();*/
	private static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
	// end google authentication constants
	
	private String stateToken;
	
	/*private final GoogleAuthorizationCodeFlow flow;*/
	
	/**
	 * Constructor initializes the Google Authorization Code Flow with CLIENT ID, SECRET, and SCOPE 
	 * @throws IOException 
	 */
	/*public GoogleAuthHelper() throws IOException {
		flow = new GoogleAuthorizationCodeFlow.Builder(HTTP_TRANSPORT,
				JSON_FACTORY, CLIENT_ID, CLIENT_SECRET, SCOPE).build();
		
		generateStateToken();
		refreshAccessToken(getStateToken(), CLIENT_ID, CLIENT_SECRET);
	}*/

	/**
	 * Builds a login URL based on client ID, secret, callback URI, and scope 
	 */
	/*public String buildLoginUrl() {
		
		final GoogleAuthorizationCodeRequestUrl url = flow.newAuthorizationUrl();
		
		return url.setRedirectUri(CALLBACK_URI).setState(stateToken).build();
	}*/
	
	/**
	 * Generates a secure state token 
	 */
	private void generateStateToken(){
		
		SecureRandom sr1 = new SecureRandom();
		
		stateToken = "google;"+sr1.nextInt();
		
	}
	
	/**
	 * Accessor for state token
	 */
	public String getStateToken(){
		return stateToken;
	}
	
	/**
	 * Expects an Authentication Code, and makes an authenticated request for the user's profile information
	 * @return JSON formatted user profile information
	 * @param authCode authentication code provided by google
	 * @throws OAuthRequestException 
	 * @throws JSONException 
	 */
	/*public String getUserInfoJson(final String authCode) throws IOException, OAuthRequestException, JSONException {

		final GoogleTokenResponse response = flow.newTokenRequest(authCode).setRedirectUri(CALLBACK_URI).execute();
		final Credential credential = flow.createAndStoreCredential(response, null);
		final HttpRequestFactory requestFactory = HTTP_TRANSPORT.createRequestFactory(credential);
		// Make an authenticated request
		final GenericUrl url = new GenericUrl(USER_INFO_URL);
		final HttpRequest request = requestFactory.buildGetRequest(url);
		request.getHeaders().setContentType("application/json");
		final String jsonIdentity = "["+request.execute().parseAsString()+"]";
		
		return jsonIdentity;

	}
	*/
	
	/*private String refreshAccessToken(String refreshToken, String CLIENT_ID, String CLIENT_SECRET) throws IOException {
	    try {
	        TokenResponse response =
	                new GoogleRefreshTokenRequest(new NetHttpTransport(), new JacksonFactory(),
	                        refreshToken, CLIENT_ID, CLIENT_SECRET).execute();
	        return response.getAccessToken();
	    } catch (TokenResponseException e) {
	        return null;
	    }
	}*/
	
	public String getUserEmail(String getUserInfoJson) throws JSONException{
		
		JSONArray jsonArray =  new JSONArray(getUserInfoJson);
		JSONObject explrObject = null;

	   // for (int i = 0; i < jsonArray.length(); i++) {
	        explrObject = jsonArray.getJSONObject(0);
	        explrObject.getString("email");
	        System.out.println( explrObject.getString("email"));
	//}
	    return explrObject.getString("email");
	    
	}
	

	public String getUsergivenName(String getUserInfoJson) throws JSONException{
		
		JSONArray jsonArray =  new JSONArray(getUserInfoJson);
		JSONObject explrObject = null;

	   // for (int i = 0; i < jsonArray.length(); i++) {
	        explrObject = jsonArray.getJSONObject(0);
	        explrObject.getString("given_name");
	        System.out.println( explrObject.getString("given_name"));
	//}
	    return explrObject.getString("given_name");
	    
	}
	
public String getUserPhoto(String getUserInfoJson) throws JSONException{
		
		JSONArray jsonArray =  new JSONArray(getUserInfoJson);
		JSONObject explrObject = null;

	   // for (int i = 0; i < jsonArray.length(); i++) {
	        explrObject = jsonArray.getJSONObject(0);
	        explrObject.getString("picture");
	        System.out.println( explrObject.getString("picture"));
	//}
	    return explrObject.getString("picture");
	    
	}
	
	

}
