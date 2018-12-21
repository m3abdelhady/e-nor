package me.heuristic.services;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

/*import javax.swing.text.html.HTMLDocument.Iterator;*/

import java.util.Random;
import java.util.concurrent.TimeUnit;

import me.heuristic.braintree.engine.BrainTreeEngine;
import me.heuristic.references.AccountType;
import me.heuristic.references.Messages;
import me.heuristic.references.UserType;
import me.heuristic.util.Constant;
import me.heuristic.util.MailUtil;
import me.heuristic.util.PMUtil;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
/*import com.google.appengine.api.users.User;*/
//import com.google.appengine.api.users.UserServiceFactory;
/*import com.google.appengine.api.oauth.OAuthRequestException;
import com.google.appengine.api.oauth.OAuthService;
import com.google.appengine.api.oauth.OAuthServiceFactory;*/
import com.google.appengine.api.users.UserServiceFactory;



public class AccountService {
	public static int MAX_FREE_ACCOUNTS = 50;
	static int startPOint = 500;	
	
	public static void deleteAccount(Entity account) {
		final Key accountkey = account.getKey();
		Iterable<Entity> campaigns = null;
		final List<Key> campaignKeys = new ArrayList<Key>();
		Iterable<Entity> taggedUrls = null;
		final List<Key> taggedUrlKeys = new ArrayList<Key>();
		
		try {
			// CASCADE_ON_DELETE
			campaigns = PMUtil.listChildKeys("Campaign", accountkey);

			for (Entity campaign : campaigns) {
				campaignKeys.add(campaign.getKey());

				taggedUrls = PMUtil.listChildKeys("TaggedUrl",
						campaign.getKey());
				for (Entity taggedUrl : taggedUrls) {
					taggedUrlKeys.add(taggedUrl.getKey());
				}
			}
			PMUtil.deleteEntity(taggedUrlKeys);
			PMUtil.deleteEntity(campaignKeys);
			PMUtil.deleteEntity(accountkey);
		} catch (Exception ex) {
			// TODO
			// String msg = Util.getErrorResponse(e);
		}
	}

	public static Entity createOrUpdateAccount(String myEmail,
			long longAccountId, String emailAddress, String firstName,
			String lastName, String company, String accountName,
			String address, String city, String state, String zip,
			String country, String phone, String website, String agreement) {

		// creating key and account entity
		Date now = new Date();
		Entity account;

		if (0 == longAccountId) {
			// new account
			account = new Entity("Account");

			// default channels
			ChannelService.setDefaultChannels(account);

			// default user
			AUserService.setDefaultAccountUser(account, myEmail);

			// default tagging settings
			TaggingSettingsService.setDefaultTaggingSettings(account);

			// default tagging parameters
			TaggingParametersService.setDefaultTaggingParameters(account);

			// order data
			account.setProperty("orderNumber", "");
			account.setProperty("type", AccountType.TRAIL);
			// ENV: account.setProperty("type", AccountType.TEST);
			account.setProperty("startDate", now);

			account.setProperty("createdBy", myEmail);
			account.setProperty("createdAt", now);
			
			GregorianCalendar cal = new GregorianCalendar();
			cal.setTime(now);
			cal.add(Calendar.DATE, (int) Constant.NUMBER_OF_TRAIL_DAYS);
//			cal.add(Calendar.DATE, 1);

			account.setProperty("endDate", cal.getTime());

			
			account.setProperty("caID", standardizingID());			
		} else {
			// editing existing account
			// retrieve it from datastore
			account = getSingleAccount(longAccountId);
		}

		// setting values and saving properties
		account.setProperty("email", emailAddress);
		account.setProperty("fname", firstName);
		account.setProperty("lname", lastName);
		account.setProperty("company", company);
		account.setProperty("name", accountName);
		account.setProperty("address", address);
		account.setProperty("city", city);
		account.setProperty("state", state);
		account.setProperty("zip", zip);
		account.setProperty("country", country);
		account.setProperty("phone", phone);
		account.setProperty("website", website);
		account.setProperty("agreement", agreement);

		account.setProperty("modifiedBy", myEmail);
		account.setProperty("modifiedAt", now);
		// saving entity
		PMUtil.persistEntity(account);
		MailUtil.sendAccountCreationMail(emailAddress , firstName +" "+ lastName, myEmail ,firstName +" "+ lastName);
		//MailUtil.sendAccountCreationMail(ToAddress, ToName, ToOwnerAddress, ToOwnerName);
		//MailUtil.sendMail("memo.mrsy@gmail.com", "Campaignalyzer", emailAddress, firstName+" "+lastName, "Account Creation", "Hello "+firstName+" your Campaignalyzer account has ben created");
		return account;
	}

	
	public static int getAllAccountsCountFor(String myEmail) {
		
		Iterable<Entity> campaigns = AccountService.getActiveAccountsForUser(myEmail); 	   

	    int i = 0;
		for (@SuppressWarnings("unused") Entity campaign: campaigns) {
			i++;
		}
	    return i;
	}
	
	public static int getAllMyAccountsCountFor(String myEmail) {
		
		Iterable<Entity> campaigns = AccountService.getAllAccountsForUser(myEmail); 	   

	    int i = 0;
		for (@SuppressWarnings("unused") Entity campaign: campaigns) {
			i++;
		}
	    return i;
	}
	
	public static void makeAccountFree(String myEmail, long longAccountId) {
		Entity account = getSingleAccount(longAccountId);
		String firstName = (String) account.getProperty("firstName");

		// setting properties
		Date now = new Date();
		account.setProperty("modifiedBy", myEmail);
		account.setProperty("modifiedAt", now);

		account.setProperty("orderNumber", "");
		account.setProperty("type", AccountType.FREE);
		account.setProperty("startDate", now);
		account.removeProperty("endDate"); // if we have any
		

		// saving entity
		PMUtil.persistEntity(account);
		//MailUtil.sendChangePlanMail(myEmail, firstName, "Free");
		
	}
	
	public static void makeAccountFreeTrial(String myEmail, long longAccountId, String planID) {
		Entity account = getSingleAccount(longAccountId);
		String firstName = (String) account.getProperty("firstName");

		// setting properties
		Date now = new Date();
		account.setProperty("modifiedBy", myEmail);
		account.setProperty("modifiedAt", now);

		account.setProperty("orderNumber", "");
		account.setProperty("type", planID);
		account.setProperty("startDate", now);
		
//		GregorianCalendar cal = new GregorianCalendar();
//		cal.setTime(now);
//		cal.add(Calendar.DATE, (int) Constant.YEAR_DAYS);
//		cal.add(Calendar.DATE, 1);

//		account.setProperty("endDate", cal.getTime());
		account.removeProperty("endDate"); // if we have any
		

		// saving entity
		PMUtil.persistEntity(account);
		//MailUtil.sendChangePlanMail(myEmail, firstName, "Free");
		
	}

	public static void updateAccountWithOrderData(Entity order) {
		long longAccountId = (Long) order.getProperty("accountId");
		Entity account = getSingleAccount(longAccountId);

		if (null != account) {
			account.setProperty("orderNumber", order.getProperty("orderNumber"));
			account.setProperty("type", order.getProperty("accountType"));
			account.setProperty("startDate", order.getProperty("startDate"));
			Date endDate = (Date) order.getProperty("endDate");
			if (null != endDate) {
				account.setProperty("endDate", endDate);
			}

			PMUtil.persistEntity(account);
		} else {
			Entity faccount = new Entity("Failed");
			faccount.setProperty("orderNumber",
					order.getProperty("orderNumber"));
			faccount.setProperty("type", order.getProperty("accountType"));
			faccount.setProperty("startDate", order.getProperty("startDate"));
			faccount.setProperty("endDate", order.getProperty("endDate"));
			faccount.setProperty("accountId", longAccountId);

			PMUtil.persistEntity(faccount);
		}
	}

	public static Iterable<Entity> getAllAccountsForUser(String myEmail) {
		Iterable<Entity> accounts = PMUtil.listEntities("Account", "users",
				myEmail);
		return accounts;
	}
	
	public static Iterable<Entity> getAllAccountsForAdmin() {
		Iterable<Entity> accounts = PMUtil.listEntitiesOfKind("Account");
		return accounts;
	}

	public static Iterable<Entity> getActiveAccountsForUser(String myEmail) {
		Iterable<Entity> accounts = getAllAccountsForUser(myEmail);
		List<Entity> activeAccounts = new ArrayList<Entity>();
		for (Entity account : accounts) {
			if (isActiveAccount(account)) {
				activeAccounts.add(account);
			}
		}
		return activeAccounts;
	}
	
	public static Entity getFirstActiveAccountsForUser(String myEmail) {
		Iterable<Entity> accounts = getAllAccountsForUser(myEmail);
		List<Entity> activeAccounts = new ArrayList<Entity>();		
		for (Entity account : accounts) {
			if (isActiveAccount(account)) {
				activeAccounts.add(account);
			}
		}
		return activeAccounts.get(0);
	}

	public static boolean hasUserExceededMaxFreeAccounts(String myEmail) {
		Iterable<Entity> accounts = getAllAccountsForUser(myEmail);
		int n = 0;

		for (Entity account : accounts) {
			if (AccountType.FREE.equals((String) account.getProperty("type"))) {
				n++;
			}
		}

		return (n > MAX_FREE_ACCOUNTS);
	}

	public static Entity getSingleAccount(long longAccountId) {
		Key key = KeyFactory.createKey("Account", longAccountId);
		return PMUtil.findEntity(key);
	}

	public static String renderAccountForm(String myEmail, long longAccountId, String email,
			String firstName, String lastName, String company,
			String accountName, String address, String city, String state,
			String zip, String country, String phone, String website,
			String agreement) {
		StringBuilder sb = new StringBuilder();

		String buttonText = "";
		if (0 == longAccountId)
			buttonText = " Create Account ";
		else
			buttonText = " Save ";
		sb.append("<div class='alert alert-danger display-hide'>"
				
                  +"<button class='close' data-close='alert'></button>"
                  +"<span>Please Enter All Required Fields. </span></div>");
		
		sb.append("<form class=\"mws-form\" action=\"/editaccount\" method=\"post\" name=\"accountForm\"\">\r\n");
		sb.append("<div class=\"mws-form-cols clearfix\">\r\n");
		sb.append("<input required type='hidden' name='myEmail' value='"+myEmail+"'/>");
		
		sb.append("\t<div class=\"form-group\">\r\n");
		sb.append("\t\t<h2>Enter Your Account Information</h2>\r\n");
		
		sb.append("\t\t<span style='margin-left: 12px;'>(All fields are required)<span>\r\n");
		
		sb.append("\t</div>\r\n");
	
		
		
		sb.append("<div class='row'> ");
		sb.append("\t<div class=\"form-group col-md-6\">\r\n");
		sb.append("\t\t<label>E-mail Address</label>\r\n");
		sb.append("<div class='input-group'>");
		sb.append("<span class='input-group-addon'><i class='fa fa-envelope'></i></span>");
		
		sb.append("\t\t<div class=\"mws-form-item large\">\r\n");
		sb.append("\t\t\t<input required type=\"email\" name=\"email\" value=\"" + email
				+ "\" class=\"form-control\" />\r\n");
		sb.append("</div>");
		sb.append("\t\t</div>\r\n");
		sb.append("\t</div>\r\n");
		
		sb.append("\t<div class=\"form-group col-md-6\">\r\n");
		sb.append("\t\t<label>Phone</label>\r\n");
		sb.append("\t\t<div class=\"mws-form-item large\">\r\n");
		sb.append("\t\t\t<input required type=\"tel\" name=\"phone\" value=\"" + phone
				+ "\" class=\"form-control\" />\r\n");
		sb.append("\t\t</div>\r\n");
		sb.append("\t</div>\r\n");
		sb.append("</div>");
		
		sb.append("<div class='row'> ");
		sb.append("\t<div class=\"form-group col-md-6\">\r\n");
		sb.append("\t\t<label>First Name</label>\r\n");
		sb.append("\t\t<div class=\"mws-form-item large\">\r\n");
		sb.append("\t\t\t<input required type=\"text\" name=\"fname\" value=\""
				+ firstName + "\" class=\"form-control\" />\r\n");
		sb.append("\t\t</div>\r\n");
		sb.append("\t</div>\r\n");

		sb.append("\t<div class=\"form-group col-md-6\">\r\n");
		sb.append("\t\t<label>Last Name</label>\r\n");
		sb.append("\t\t<div class=\"mws-form-item large\">\r\n");
		sb.append("\t\t\t<input required type=\"text\" name=\"lname\" value=\""
				+ lastName + "\" class=\"form-control\" />\r\n");
		sb.append("\t\t</div>\r\n");
		sb.append("\t</div>\r\n");
		
		sb.append("<div class='row'> ");
		
		
		
		sb.append("\t<div class=\"form-group col-md-4\">\r\n");
		sb.append("\t\t<label>Company Name</label>\r\n");
		sb.append("\t\t<div class=\"mws-form-item large\">\r\n");
		sb.append("\t\t\t<input required type=\"text\" name=\"company\" value=\""
				+ company + "\" class=\"form-control\" />\r\n");
		sb.append("\t\t</div>\r\n");
		sb.append("\t</div>\r\n");

		sb.append("\t<div class=\"form-group col-md-4\">\r\n");
		sb.append("\t\t<label>Account Name</label>\r\n");
		sb.append("\t\t<div class=\"mws-form-item large\">\r\n");
		sb.append("\t\t\t<input required type=\"text\" name=\"name\" value=\""
				+ accountName + "\" class=\"form-control\" />\r\n");
		sb.append("\t\t</div>\r\n");
		sb.append("\t</div>\r\n");
		
		sb.append("\t<div class=\"form-group col-md-4\">\r\n");
		sb.append("\t\t<label>Website</label>\r\n");
		sb.append("\t\t<div class=\"mws-form-item large\">\r\n");
		sb.append("\t\t\t<input required type=\"text\" name=\"website\" value=\""
				+ website + "\" class=\"form-control\" />\r\n");
		sb.append("\t\t</div>\r\n");
		sb.append("\t</div>\r\n");
		sb.append("</div>");

		sb.append("<div class='row'> ");
		sb.append("\t<div class=\"form-group col-md-12\">\r\n");
		sb.append("\t\t<label>Address</label>\r\n");
		sb.append("\t\t<div class=\"mws-form-item large\">\r\n");
		sb.append("\t\t\t<input required type=\"text\" name=\"address\" value=\""
				+ address + "\" class=\"form-control\" />\r\n");
		sb.append("\t\t</div>\r\n");
		sb.append("\t</div>\r\n");
		sb.append("</div>");
		
		sb.append("<div class='row'> ");
		
		sb.append("\t<div class=\"form-group col-md-3\">\r\n");
		sb.append("\t\t<label>Country</label>\r\n");
		sb.append("\t\t<div class=\"mws-form-item large\">\r\n");
		
		
		//sb.append("\t\t\t<input type=\"text\" name=\"country\" value=\""
		//		+ country + "\" class=\"form-control\" />\r\n");		
		
		sb.append("<select required class='form-control' onchange=\"print_state('state',this.selectedIndex);\" id='country' name ='country'>");
		
		//if(country !=""){
			sb.append( "<option>"+ country +"</option>");
			//}
		
		//else{
		//	sb.append("<option value='USA'>USA</option>");
			//}
		sb.append("</select>");
		
		sb.append("\t\t</div>\r\n");
		sb.append("\t</div>\r\n");

		
		sb.append("\t<div class=\"form-group col-md-3\">\r\n");
		sb.append("\t\t<label>State/Province</label>\r\n");
		sb.append("\t\t<div class=\"mws-form-item large StateBox\">\r\n");
		
		
		/*sb.append("\t\t\t<input type=\"text\" name=\"state\" value=\"" + state
				+ "\" class=\"form-control stateText\" />\r\n");*/
		
		sb.append("<select required class='form-control stateSelect' name ='state' id ='state'><option>"+state+"</option></select>");
		
		sb.append("\t\t</div>\r\n");
		sb.append("\t</div>\r\n");
		
		sb.append("\t<div class=\"form-group col-md-3\">\r\n");
		sb.append("\t\t<label>City</label>\r\n");
		sb.append("\t\t<div class=\"mws-form-item large\">\r\n");
		sb.append("\t\t\t<input required type=\"text\" name=\"city\" value=\"" + city
				+ "\" class=\"form-control\" />\r\n");
		sb.append("\t\t</div>\r\n");
		sb.append("\t</div>\r\n");		
		
		sb.append("\t<div class=\"form-group col-md-3\" style='white-space:nowrap'>\r\n");
		sb.append("\t\t<label>Zip/ Postal Code</label>\r\n");
		sb.append("\t\t<div class=\"mws-form-item large\">\r\n");
		sb.append("\t\t\t<input required type=\"text\" name=\"zip\" value=\"" + zip
				+ "\" class=\"form-control\" />\r\n");
		sb.append("\t\t</div>\r\n");
		sb.append("\t</div>\r\n");
		
		
		sb.append("</div>");
		
//		sb.append("<hr>");
//		sb.append("<div class='row'> ");
//		sb.append("\t<div class=\"form-group col-md-12\">\r\n");
//		sb.append("\t\t<h2>Billing Contact Information</h2><p style='padding:10px 0px'>This email will be notified about an activity associated with this account in addition to the Google account used to access Campaignalyzer</p> \r\n");
//		sb.append("\t</div>\r\n");
//		sb.append("</div>");
		
//		sb.append("<div class='row'> ");
//		sb.append("\t<div class=\"form-group col-md-6\">\r\n");
//		sb.append("\t\t<label>E-mail Address*</label>\r\n");
//		sb.append("<div class='input-group'>");
//		sb.append("<span class='input-group-addon'><i class='fa fa-envelope'></i></span>");
//		
//		sb.append("\t\t<div class=\"mws-form-item large\">\r\n");
//		sb.append("\t\t\t<input required type=\"email\" name=\"email\" value=\"" + email
//				+ "\" class=\"form-control\" />\r\n");
//		sb.append("</div>");
//		sb.append("\t\t</div>\r\n");
//		sb.append("\t</div>\r\n");
//		
//		sb.append("\t<div class=\"form-group col-md-6\">\r\n");
//		sb.append("\t\t<label>Phone*</label>\r\n");
//		sb.append("\t\t<div class=\"mws-form-item large\">\r\n");
//		sb.append("\t\t\t<input required type=\"tel\" name=\"phone\" value=\"" + phone
//				+ "\" class=\"form-control\" />\r\n");
//		sb.append("\t\t</div>\r\n");
//		sb.append("\t</div>\r\n");
//		sb.append("</div>");
//		
//		sb.append("<div class='row'> ");
//		sb.append("\t<div class=\"form-group col-md-6\">\r\n");
//		sb.append("\t\t<label>First Name*</label>\r\n");
//		sb.append("\t\t<div class=\"mws-form-item large\">\r\n");
//		sb.append("\t\t\t<input required type=\"text\" name=\"fname\" value=\""
//				+ firstName + "\" class=\"form-control\" />\r\n");
//		sb.append("\t\t</div>\r\n");
//		sb.append("\t</div>\r\n");
//
//		sb.append("\t<div class=\"form-group col-md-6\">\r\n");
//		sb.append("\t\t<label>Last Name*</label>\r\n");
//		sb.append("\t\t<div class=\"mws-form-item large\">\r\n");
//		sb.append("\t\t\t<input required type=\"text\" name=\"lname\" value=\""
//				+ lastName + "\" class=\"form-control\" />\r\n");
//		sb.append("\t\t</div>\r\n");
//		sb.append("\t</div>\r\n");

		sb.append("</div>\r\n");
		sb.append("</div>");

		sb.append("<div class=\"mws-form-inline\">\r\n <br/>");
		sb.append("\t<div class=\"form-group\">\r\n");
		//sb.append("\t\t<ul class=\"mws-form-list\">\r\n");
		if(0 ==longAccountId){		
			
			sb.append("\t\t\t<span class='privacyLabel'><input required type=\"checkbox\" name=\"agreement\"/></span> <label style='display:inline;'> I certify that I have read and agree"
					+ " to the CampaignAlyzer <a target=\"_blank\" href=\"https://www.campaignalyzer.com/terms/\">Terms of Service</a>"
					+ " and <a target=\"_blank\" href=\"https://www.campaignalyzer.com/privacy-policy/\">Privacy Policy</a>.</label>\r\n");
			
		}
		else{sb.append("\t\t\t<input required style='display:none;' class='form-control' type=\"checkbox\" name=\"agreement\" checked/> <label> I certify that I have read and agree"
			+ " to the CampaignAlyzer <a target=\"_blank\" href=\"https://www.campaignalyzer.com/terms/\">Terms of Service</a>"
			+ " and <a target=\"_blank\" href=\"https://www.campaignalyzer.com/privacy-policy/\">Privacy Policy</a>.</label>\r\n");}
		
		//sb.append("\t\t</ul>\r\n");
		sb.append("\t</div>\r\n");
		sb.append("</div>\r\n");

		sb.append("\t<div class=\"mws-button-row\">\r\n");
		sb.append("\t\t<input type=\"submit\" value=\"" + buttonText
				+ "\" class=\"btn sbold green\" />\r\n");
		
		/*if(null != getFirstActiveAccountsForUser(myEmail)){
			sb.append("\t<a href ='/dashboard.jsp?aid="+getFirstActiveAccountsForUser(myEmail).getKey().getId()+"'  class=\"btn red\" >Cancel</a>\r\n");	
		}
		else if(null != getFirstActiveAccountsForUser(myEmail) && 0 != getAllMyAccountsCountFor(myEmail)){
			sb.append("\t<a href ='/ManageAccounts.jsp' class=\"btn red\" >Cancel</a>\r\n");
		}
		else{
			
			sb.append("\t<button type='reset' class=\"btn red\" onclick='goBack()'>Cancel</button>\r\n");
		}*/
		
		if(getAllMyAccountsCountFor(myEmail) != 0){
			sb.append("<a class=\"btn red\" href='/SC_CreateAccount'>Cancel</a> ");
		}
		
		//sb.append("\t<button type='reset' class=\"btn red\" onclick='goBack()'>Cancel</button>\r\n");
		sb.append("\t</div>\r\n");

		sb.append("<input type=\"hidden\" name=\"aid\" value=\""
				+ longAccountId + "\"/>\r\n");
		sb.append("<input type='hidden' name='pid' value=''/>\r\n");
		sb.append("<input type='hidden' name ='myEmail' value='"+myEmail+"'");
		
		sb.append("</form>\r\n");
		

		return sb.toString();
	}

	public static String renderAccountNameAndType(String myRole, Entity account){
		StringBuilder sb = new StringBuilder();

		// retrieve name and type
		long longAccountId = account.getKey().getId();
		String caID = (String) account.getProperty("caID");
		String accountName = (String) account.getProperty("name");
		String accountType = (String) account.getProperty("type");
		String companyName = (String) account.getProperty("company");
		String address = (String) account.getProperty("address");
		String city = (String) account.getProperty("city");
		String state = (String) account.getProperty("state");

		if (BrainTreeEngine.getInstance().getPlusPlanId().equals(accountType)) {
			accountType = AccountType.PLUS;
		} else if (BrainTreeEngine.getInstance().getPremiumPlanId()
				.equals(accountType)) {
			accountType = AccountType.PREMIUM;
		} else if (BrainTreeEngine.getInstance().getStandardPlanId()
				.equals(accountType)) {
			accountType = AccountType.STANDARD;
		}
		else if (BrainTreeEngine.getInstance().getBasicPlanId()
				.equals(accountType)) {
			accountType = AccountType.BASIC;
		}
		else if (BrainTreeEngine.getInstance().getProfessionalPlanId()
				.equals(accountType)) {
			accountType = AccountType.PROFESSIONAL;
		}

		sb.append("<div class=\"portlet-body\" style='padding-top:3px;'>");
		//if(caID != null){
		//sb.append("\t<p><strong>Account ID:</strong> " + caID + "</p>\r\n");
		//}
		sb.append("\t<p><strong>Account Name:</strong> " + accountName + "</p>\r\n");
		sb.append("\t<p><strong>Company Name:</strong> " + companyName + "</p>\r\n");
		sb.append("\t<p><strong>Address:</strong> " + address + "</p>\r\n");
		sb.append("\t<p><strong>City:</strong> " + city + "</p>\r\n");
		sb.append("\t<p><strong>State:</strong> " + state + "</p>\r\n");
		//sb.append("\tAccount Plan: " + accountType + "\r\n");
		sb.append("</div>\r\n");

		if (UserType.hasPermission(myRole, UserType.PERMISSION_MANAGE_ACCOUNT) || myRole == null ) {
			sb.append("<form class=\"mws-form\">\r\n");
			sb.append("<div class=\"mws-form-inline\">\r\n");
			sb.append("\t<div class=\"mws-button-row\">\r\n");
			sb.append("\t\t<input type=\"button\" value=\" Edit Account Information \" class=\"btn sbold green\" onClick=\"location.href='/EditAccount.jsp?aid="
					+ longAccountId + "'\"/>\r\n");
			sb.append("\t</div>\r\n");
			sb.append("</div>\r\n");
			sb.append("</form>\r\n");
		}

		return sb.toString();
	}
	
	
	public static String renderAccountList(String myEmail){
		StringBuilder sb = new StringBuilder();
		
		
		// retrieve all accounts belonging to this user
		Iterable<Entity> accounts = null;
		//if("" != myEmail){
			accounts = getAllAccountsForUser(myEmail);	
		//}
		/*else if("" == myEmail){
			
			accounts = getAllAccountsForAdmin();
		}*/
		sb.append("<form class=\"mws-form\">\r\n");
		sb.append("<div class=\"mws-form-inline\">\r\n");
		sb.append("\t<div style='text-align:right' class=\"form-group\">\r\n");
		sb.append("\t\t<input type=\"button\" value=\" Create New Account \" class=\"btn sbold green\" onClick=\"location.href='/NewAccount.jsp'\"/>\r\n");
		sb.append("\t</div>\r\n");
		sb.append("</div>\r\n");
		sb.append("</form>\r\n");

		sb.append("<table class=\"sortable table table-striped table-bordered table-hover table-checkable order-column dataTable no-footer\">\r\n");
		sb.append("<thead><tr>\r\n");
		if ("" == myEmail){
			
			sb.append("<th>Account ID</th>\r\n");
			sb.append("<th>Account ID</th>\r\n");
		}
		sb.append("<th>Account Name</th>\r\n");
		sb.append("<th>Account Plan</th>\r\n");
		if ("" == myEmail){
			
			sb.append("<th>Address</th>\r\n");
			sb.append("<th>Email</th>\r\n");
		}
		else{
		sb.append("<th>User Type</th>\r\n");
		}
		
		sb.append("<th class='action'>Action</th>\r\n");
		
		sb.append("</tr></thead>\r\n<tbody>\r\n");

		int i = 0;
		String accountName = "";
		String accountType = "";
		String myRole = "";
		String address = "";
		String email = "";
		String caID = "";
		long longAccountId = 0;
		for (Entity account : accounts) {
			caID = (String) account.getProperty("caID");
			accountName = (String) account.getProperty("name");
			accountType = (String) account.getProperty("type");
			myRole = (String) account.getProperty(myEmail);
			address = (String) account.getProperty("address");
			email = (String) account.getProperty("email");
			
			/*if (UserServiceFactory.getUserService().isUserAdmin() && "" == myEmail){
				
				myRole = "E-nor";
			}*/
			longAccountId = account.getKey().getId();

			sb.append("<tr>\r\n");
			if ("" == myEmail){
				
				sb.append("<td>"+ longAccountId + "</td>\r\n");
				if(caID != null){
					
					sb.append("<td>"+ caID + "</td>\r\n");
				}
				else{
					
					sb.append("<td> </td>\r\n");
				}
				
			}
			sb.append("<td><a href=\"/ManageCampaignHolders.jsp?aid=" + longAccountId
					+ "\">" + accountName + "</a></td>\r\n");			
			sb.append("<td>" + accountType + "</td>\r\n");
			if ("" == myEmail){
				
				sb.append("<td>"+ address + "</td>\r\n");
				sb.append("<td>" + email + "</td>\r\n");
			}
			else{
			sb.append("<td>" + myRole + "</td>\r\n");}
			sb.append("<td class='action'>\r\n");

			if (isNewAccount(account)) {
				sb.append(renderNewAccountLinks(account));
			} else if (hasExpired(account)) {
				sb.append(renderExpiredAccountLinks(account));
			} else {
				sb.append(renderActiveAccountLinks(account));
			}

			sb.append("</td>\r\n");
			sb.append("</tr>\r\n");

			i++;
		}

		if (i == 0) {
			sb.append("<tr><td colspan=\"4\">You haven't created any account and/or no account admin gave you access to any account.</td></tr>\r\n");
		}

		sb.append("</tbody>\r\n");
		sb.append("</table>\r\n");

		sb.append("<form class=\"mws-form\">\r\n");
		sb.append("<div class=\"mws-form-inline\">\r\n");
		//sb.append("\t<div class=\"mws-button-row\">\r\n");
		//sb.append("\t\t<input type=\"button\" value=\" Create New Account \" class=\"btn sbold green\" onClick=\"location.href='/EditAccount.jsp'\"/>\r\n");
		//sb.append("\t</div>\r\n");
		sb.append("</div>\r\n");
		sb.append("</form>\r\n");

		return sb.toString();
	}

	private static String renderNewAccountLinks(Entity account) {
		StringBuilder sb = new StringBuilder();

		long longAccountId = account.getKey().getId();
		String accountName = (String) account.getProperty("name");

		sb.append("New Account: ");
		sb.append("<a href=\"/ViewAccount.jsp?aid=" + longAccountId
				+ "\">View</a>");
		sb.append("&nbsp;|&nbsp;");
		sb.append("<a href=\"/EditAccountPlan.jsp?aid=" + longAccountId
				+ "\">Activate</a>");
		sb.append("&nbsp;|&nbsp;");
		sb.append("<a href=\"javascript:confirmAccountDelete('/editaccount?aid="
				+ longAccountId
				+"&myEmail="
				+account.getProperty("createdBy")
				+ "&act="
				+ Messages.ACTION_DELETE
				+ "','"
				+ accountName + "');\">Delete</a>");

		return sb.toString();
	}

	private static String renderExpiredAccountLinks(Entity account) {
		StringBuilder sb = new StringBuilder();

		long longAccountId = account.getKey().getId();
		String accountName = (String) account.getProperty("name");
		Date endDate = (Date) account.getProperty("endDate");

		sb.append("Expired on "
				+ DateFormat.getDateTimeInstance().format(endDate) + ": ");
		sb.append("<a href=\"/ViewAccount.jsp?aid=" + longAccountId
				+ "\">View</a>");
		sb.append("&nbsp;|&nbsp;");
		sb.append("<a href=\"EditAccountPlan.jsp?aid=" + longAccountId
				+ "\">Reactivate</a>");
		sb.append("&nbsp;|&nbsp;");
		sb.append("<a href=\"javascript:confirmAccountDelete('/editaccount?aid="
				+ longAccountId
				+ "&act="
				+ Messages.ACTION_DELETE
				+ "','"
				+ accountName + "');\">Delete</a>");

		return sb.toString();
	}

	private static String renderActiveAccountLinks(Entity account) {
		StringBuilder sb = new StringBuilder();
		long longAccountId = account.getKey().getId();

		sb.append("<a href=\"/ManageCampaignHolders.jsp?aid=" + longAccountId
				+ "\">View Campaigns</a>");
		sb.append("&nbsp;|&nbsp;");
		sb.append("<a href=\"/ViewAccount.jsp?aid=" + longAccountId
				+ "\">Settings</a>");

		return sb.toString();
	}

	public static String renderAccountDetails(String myRole, Entity account) {
		StringBuilder sb = new StringBuilder();

		// defaults
		long longAccountId = account.getKey().getId();
		String accountName = (String) account.getProperty("name");
		String caID = (String) account.getProperty("caID");
		String accountType = (String) account.getProperty("type");
		Date createdAt = (Date) account.getProperty("createdAt");
		Date modifiedAt = (Date) account.getProperty("modifiedAt");

		String orderNumber = (String) account.getProperty("orderNumber");
		Date startDate = (Date) account.getProperty("startDate");
		Date endDate = (Date) account.getProperty("endDate");
		
		SimpleDateFormat dt1 = new SimpleDateFormat("MMM d, yyyy");
	

		@SuppressWarnings("unchecked")
		List<String> users = (ArrayList<String>) account.getProperty("users");
		int mobCampaignsCount = MobileCampaignHolderService
				.getAllMobileCampaignHoldersCountFor(longAccountId);
		
		int webCampaignsCount = CampaignHolderService
				.getAllCampaignHoldersCountFor(longAccountId);
		
		/*int campaignsCount = CampaignService
				.getAllCampaignsCountFor(longAccountId);*/
		
		int campaignsCount = mobCampaignsCount + webCampaignsCount;

		sb.append("<div class=\"portlet-body\" style='padding-top:0px;'>\r\n");
		sb.append("<table class='table table table-striped table-bordered table-hover table-checkable order-column dataTable no-footer' border=\"0\" cellspacing=\"0\" cellpadding=\"3\"><tbody>\r\n");
		sb.append("<tr><td style='font-weight:bold'>Account Plan:</td><td style='font-weight:bold' align=\"left\">"
						+ accountType + "</td></tr>\r\n");
		if (!isNewAccount(account)) {
			if (!AccountType.FREE.equals(accountType) && !AccountType.TRAIL.equals(accountType))
				sb.append("<tr><td>Order Number:</td><td align=\"left\">"
						+ orderNumber + "</td></tr>\r\n");
			sb.append("<tr><td >Start Date:</td><td align=\"left\">"
					//+ DateFormat.getDateTimeInstance().format(startDate)
					+ dt1.format(startDate)
					+ "</td></tr>\r\n");
			if (null != endDate)
				sb.append("<tr><td>End Date:</td><td align=\"left\">"
//						+ DateFormat.getDateTimeInstance().format(endDate)
						+ dt1.format(endDate)
						+ "</td></tr>\r\n");
			else				
				
				sb.append("<tr><td>End Date:</td><td align=\"left\">Not Set</td></tr>\r\n");
			
			if (AccountType.TRAIL.equals(accountType))
				sb.append("<tr><td colspan=\"2\">   <a class='btn sbold green' href='/EditAccountPlan.jsp?aid="+ longAccountId +"'>Manage Billing </a>     </td></tr>\r\n");

			
//			sb.append("<tr><td colspan=\"2\"><hr /></td></tr>\r\n");
			sb.append("<tr><td colspan=\"2\"></td></tr>\r\n");

			sb.append("<tr><td>Number of Users:</td><td align=\"left\">"
					+ users.size()
					+ " out of "
					+ AccountType.maxUsersMap.get(accountType)
					+ "</td></tr>\r\n");
			sb.append("<tr><td>Number of Web Campaigns:</td><td align=\"left\">"
					+ webCampaignsCount	+" Campaign"					
					+ "</td></tr>\r\n");
			
			sb.append("<tr><td>Number of Mobile Campaigns:</td><td align=\"left\">"
					+ mobCampaignsCount	+" Campaign"				
					+ "</td></tr>\r\n");
			
			sb.append("<tr><td>Total Number of Campaigns:</td><td align=\"left\">"
					+ campaignsCount
					+ " out of "
					+ AccountType.maxCampaignsMap.get(accountType)					
					+ "</td></tr>\r\n");
			sb.append("<tr><td>Export Campaigns:</td><td align=\"left\">"
					+ yesNo(AccountType.allowsExport(accountType))
					+ "</td></tr>\r\n");
			sb.append("<tr><td>Import Campaigns:</td><td align=\"left\">"
					+ yesNo(AccountType.allowsImport(accountType))
					+ "</td></tr>\r\n");
			sb.append("<tr><td>Monthly Subscription:</td><td align=\"left\">$"
					+ AccountType.priceMap.get(accountType)/12 + "<br> (Billed Anually)</td></tr>\r\n");
			sb.append("<tr><td colspan=\"2\"><hr /></td></tr>\r\n");
		}

		sb.append("<tr><td>Creation Date:</td><td align=\"left\">"
				+ DateFormat.getDateTimeInstance().format(createdAt)
				+ "</td></tr>\r\n");
		sb.append("<tr><td>Creation By:</td><td align=\"left\">"
				+ account.getProperty("createdBy") + "</td></tr>\r\n");
		sb.append("<tr><td>Last Modified Date:</td><td align=\"left\">"
				+ DateFormat.getDateTimeInstance().format(modifiedAt)
				+ "</td></tr>\r\n");
		sb.append("<tr><td>Last Modified By:</td><td align=\"left\">"
				+ account.getProperty("modifiedBy") + "</td></tr>\r\n");
		sb.append("</tbody></table>\r\n");
		sb.append("</div>\r\n");

		if (UserType.hasPermission(myRole, UserType.PERMISSION_MANAGE_ACCOUNT) || myRole == null) {
			
			sb.append("<form class=\"mws-form\" action=\"/editaccount\">\r\n");
			sb.append("<div class=\"mws-form-inline\">\r\n");
			sb.append("\t<div class=\"mws-button-row\">\r\n");
			sb.append("\t\t<input type=\"button\" value=\" Change Plan \" class=\"btn sbold green\" onClick=\"location.href='/EditAccountPlan.jsp?aid="
					+ longAccountId + "&update=1'\"/>\r\n");
			if (!AccountType.FREE.equals(accountType))
				// sb.append("\t\t<input type=\"button\" value=\" View Subscription \" class=\"mws-button red\" onClick=\"window.open('"
				// + GoogleCheckout.CHECKOUT_RECEIPT_URL + orderNumber +
				// "');\"/>\r\n");
				sb.append("\t\t<input type=\"submit\" value=\" Delete Account \" class=\"btn btn-danger\" onClick=\"return confirmAccountDeleteSubmit('"
						+ accountName + "');\"/>\r\n");
			sb.append("\t</div>\r\n");
			sb.append("<input type=\"hidden\" name=\"aid\" value=\""
					+ longAccountId + "\"/>\r\n");
			sb.append("<input type=\"hidden\" name=\"act\" value=\""
					+ Messages.ACTION_DELETE + "\"/>\r\n");
			sb.append("</div>\r\n");
			sb.append("</form>\r\n");
		}

		return sb.toString();
	}

	public static String renderAccountSubscriptionForm(Entity account, String update) {
		StringBuilder sb = new StringBuilder();
		
		
		//Basic

		sb.append(" <div class='pricing-content-1'><div class='row'>");
		sb.append("<div class='col-md-4'> <div class='price-column-container border-active'><div class='price-table-head bg-red'><h2 class='no-margin'>"
				+ AccountType.BASIC + "</h2></div><div class='arrow-down border-top-red'></div><div class='price-table-pricing'><h3><sup class='price-sign'>$</sup>"
						+ Math.round(AccountType.priceMap.get(AccountType.BASIC))/12 
						+ "<sup class='month-sign'>    /month</sup> <br> "
						+ "</h3><p>Billed Annually</p></div><div class='price-table-content'><div class='row mobile-padding'><div class='col-xs-3 text-right mobile-padding'>"
						+ "<i class='glyphicon glyphicon-user'></i></div><div class='col-xs-9 text-left mobile-padding'>"
						+ AccountType.maxUsersMap.get(AccountType.BASIC) + 
						" Users</div></div><div class='row mobile-padding'><div class='col-xs-3 text-right mobile-padding'><i class='glyphicon glyphicon-screenshot'></i></div><div class='col-xs-9 text-left mobile-padding'>"
						+ AccountType.maxCampaignsMap.get(AccountType.BASIC) + 
						" Campaigns</div></div><div class='row mobile-padding'><div class='col-xs-3 text-right mobile-padding'><i class='glyphicon glyphicon-tags'></i></div><div class='col-xs-9 text-left mobile-padding'>"
						+ "Unlimited Tagged URLs"
						+ "</div></div><div class='row mobile-padding'><div class='col-xs-3 text-right mobile-padding'><i class='glyphicon glyphicon-compressed'></i></div><div class='col-xs-9 text-left mobile-padding'>"
						+ "URL Shortener"
						+ "</div></div><div class='row mobile-padding'><div class='col-xs-3 text-right mobile-padding'><i class='glyphicon glyphicon-cloud-download'></i></div><div class='col-xs-9 text-left mobile-padding'>"
						+ "Export Campaigns"
						+ "</div></div><div class='row mobile-padding'><div class='col-xs-3 text-right mobile-padding'><i class='glyphicon glyphicon-cloud-upload'></i></div><div class='col-xs-9 text-left mobile-padding'>"
						+ "Import Campaigns"
						+ "</div></div><div class='row mobile-padding'><div class='col-xs-3 text-right mobile-padding'><i class='glyphicon glyphicon-stats'></i></div><div class='col-xs-9 text-left mobile-padding'>"
						+ "Analytics Integration"
						+ "</div></div></div><div class='arrow-down arrow-grey'></div><div class='price-table-footer'>"
						+ renderBrainTreeSubscriptionButton(AccountType.BASIC,account,update)
						+"</div></div></div>");
		
		
		sb.append("<div class='col-md-4'> <div class='price-column-container border-active'><div class='price-table-head bg-green'><h2 class='no-margin'>"
				+ AccountType.PROFESSIONAL + "</h2></div><div class='arrow-down border-top-green'></div><div class='price-table-pricing'><h3><sup class='price-sign'>$</sup>"
						+ Math.round(AccountType.priceMap.get(AccountType.PROFESSIONAL))/12 
						+ "<sup class='month-sign'>    /month</sup> <br> "
						+ " </h3><p>Billed Annually</p><div class='price-ribbon'>Popular</div></div><div class='price-table-content'><div class='row mobile-padding'><div class='col-xs-3 text-right mobile-padding'>"
						+ "<i class='glyphicon glyphicon-user'></i></div><div class='col-xs-9 text-left mobile-padding'>"
						+ AccountType.maxUsersMap.get(AccountType.PROFESSIONAL) + 
						" Users</div></div><div class='row mobile-padding'><div class='col-xs-3 text-right mobile-padding'><i class='glyphicon glyphicon-screenshot'></i></div><div class='col-xs-9 text-left mobile-padding'>"
						+ AccountType.maxCampaignsMap.get(AccountType.PROFESSIONAL) + 
						" Campaigns</div></div><div class='row mobile-padding'><div class='col-xs-3 text-right mobile-padding'><i class='glyphicon glyphicon-tags'></i></div><div class='col-xs-9 text-left mobile-padding'>"
						+ "Unlimited Tagged URLs"
						+ "</div></div><div class='row mobile-padding'><div class='col-xs-3 text-right mobile-padding'><i class='glyphicon glyphicon-compressed'></i></div><div class='col-xs-9 text-left mobile-padding'>"
						+ "URL Shortener"
						+ "</div></div><div class='row mobile-padding'><div class='col-xs-3 text-right mobile-padding'><i class='glyphicon glyphicon-cloud-download'></i></div><div class='col-xs-9 text-left mobile-padding'>"
						+ "Export Campaigns"
						+ "</div></div><div class='row mobile-padding'><div class='col-xs-3 text-right mobile-padding'><i class='glyphicon glyphicon-cloud-upload'></i></div><div class='col-xs-9 text-left mobile-padding'>"
						+ "Import Campaigns"
						+ "</div></div><div class='row mobile-padding'><div class='col-xs-3 text-right mobile-padding'><i class='glyphicon glyphicon-stats'></i></div><div class='col-xs-9 text-left mobile-padding'>"
						+ "Analytics Integration"
						+ "</div></div></div><div class='arrow-down arrow-grey'></div><div class='price-table-footer'>"
						+ renderBrainTreeSubscriptionButton(AccountType.PROFESSIONAL,account,update)
						+"</div></div></div>");
		
		sb.append("<div class='col-md-4'> <div class='price-column-container border-active'><div class='price-table-head bg-purple'><h2 class='no-margin'>"
				+ "Enterprise" + "</h2></div><div class='arrow-down border-top-purple'></div><div class='price-table-pricing'><h3>"
//						+ "<sup class='price-sign'>$</sup>"
						+ "Call us"
						+ "<br> </h3><p>1.408.988.0003</p></div><div class='price-table-content'><div class='row mobile-padding'><div class='col-xs-3 text-right mobile-padding'>"
						+ "<i class='glyphicon glyphicon-user'></i></div><div class='col-xs-9 text-left mobile-padding'>"
						+ "Custom"+ 
						" </div></div><div class='row mobile-padding'><div class='col-xs-3 text-right mobile-padding'><i class='glyphicon glyphicon-screenshot'></i></div><div class='col-xs-9 text-left mobile-padding'>"
						+ "Custom" + 
						" </div></div><div class='row mobile-padding'><div class='col-xs-3 text-right mobile-padding'><i class='glyphicon glyphicon-tags'></i></div><div class='col-xs-9 text-left mobile-padding'>"
						+ "Unlimited Tagged URLs"
						+ "</div></div><div class='row mobile-padding'><div class='col-xs-3 text-right mobile-padding'><i class='glyphicon glyphicon-compressed'></i></div><div class='col-xs-9 text-left mobile-padding'>"
						+ "URL Shortener"
						+ "</div></div><div class='row mobile-padding'><div class='col-xs-3 text-right mobile-padding'><i class='glyphicon glyphicon-cloud-download'></i></div><div class='col-xs-9 text-left mobile-padding'>"
						+ "Export Campaigns"
						+ "</div></div><div class='row mobile-padding'><div class='col-xs-3 text-right mobile-padding'><i class='glyphicon glyphicon-cloud-upload'></i></div><div class='col-xs-9 text-left mobile-padding'>"
						+ "Import Campaigns"
						+ "</div></div><div class='row mobile-padding'><div class='col-xs-3 text-right mobile-padding'><i class='glyphicon glyphicon-stats'></i></div><div class='col-xs-9 text-left mobile-padding'>"
						+ "Analytics Integration"
						+ "</div></div></div><div class='arrow-down arrow-grey'></div><div class='price-table-footer'>"
						+"<a target='_blank' href='https://www.campaignalyzer.com/contact-us/' class='btn grey-salsa btn-outline price-button sbold uppercase' style='display: inline-block; padding:5px;'>Contact Us</a>"
//						+ renderBrainTreeSubscriptionButton(AccountType.BASIC,account,update)
						+"</div></div></div>");
		
		sb.append("</div></div>");
		

		

		return sb.toString();
	}
	
//	public static String renderAccountSubscriptionForm(Entity account, String update) {
//		StringBuilder sb = new StringBuilder();
//
//		sb.append("<table class=\"table table table-striped table-bordered table-hover table-checkable order-column dataTable no-footer table-bordered\"><thead><tr>\r\n");
//		sb.append("<th style='font-weight:bold'>Packages / Features</th>");
//		//sb.append("<th>" + AccountType.PREMIUM + "</th>\r\n");
//		//sb.append("<th>" + AccountType.PLUS + "</th>\r\n");
//		//sb.append("<th>" + AccountType.STANDARD + "</th>\r\n");
//		
//		//Free
//		//sb.append("<th>" + AccountType.FREE + "</th>\r\n");
//		
//		sb.append("<th style='font-weight:bold'>" + AccountType.BASIC + "</th>\r\n");
//		sb.append("<th style='font-weight:bold'>" + AccountType.PROFESSIONAL + "</th>\r\n");
//		sb.append("<th style='font-weight:bold'>Enterprise</th>\r\n");
//		
//		
//		sb.append("</tr></thead>\r\n<tbody>\r\n");
//
//		/*sb.append("<tr><td>Monthly Subscription:</td>" + "<td>$"
//				+ AccountType.priceMap.get(AccountType.PREMIUM) + "</td>"
//				+ "<td>$" + AccountType.priceMap.get(AccountType.PLUS)
//				+ "</td>" + "<td>$"
//				+ AccountType.priceMap.get(AccountType.STANDARD) + "</td>"
//				//Free
//				+ "<td>$" + AccountType.priceMap.get(AccountType.FREE)
//				+ "</td>" + "</tr>\r\n");*/
//		
//		sb.append("<tr><td>Monthly Price (Billed Annually):</td>" + "<td>$"
//				+ Math.round(AccountType.priceMap.get(AccountType.BASIC))/12 + "<br> (Billed Anually)</td>"
//				+ "<td>$" + Math.round(AccountType.priceMap.get(AccountType.PROFESSIONAL))/12
//				+ "<br> (Billed Anually)</td>" + "<td>Custom </td>"
//				+ "</tr>\r\n");
//				
//		
//		
//		/*sb.append("<tr><td>Users:</td>" + "<td>"
//				+ AccountType.maxUsersMap.get(AccountType.PREMIUM) + "</td>"
//				+ "<td>" + AccountType.maxUsersMap.get(AccountType.PLUS)
//				+ "</td>" + "<td>"
//				+ AccountType.maxUsersMap.get(AccountType.STANDARD) + "</td>"
//				//Free
//				+ "<td>" + AccountType.maxUsersMap.get(AccountType.FREE)
//				+ "</td>" + "</tr>\r\n");*/
//		
//		
//		sb.append("<tr><td>Users:</td>" + "<td>"
//				+ AccountType.maxUsersMap.get(AccountType.BASIC) + "</td>"
//				+ "<td>" + AccountType.maxUsersMap.get(AccountType.PROFESSIONAL)
//				+ "</td>" + "<td>Custom</td>"
//				+ "</tr>\r\n");
//		
//		
//		/*sb.append("<tr><td>Campaigns:</td>" + "<td>"
//				+ AccountType.maxCampaignsMap.get(AccountType.PREMIUM)
//				+ "</td>" + "<td>"
//				+ AccountType.maxCampaignsMap.get(AccountType.PLUS) + "</td>"
//				+ "<td>"
//				+ AccountType.maxCampaignsMap.get(AccountType.STANDARD)
//				+ "</td>" 
//				//Free
//				+ "<td>"+ AccountType.maxCampaignsMap.get(AccountType.FREE) + "</td>"
//				+ "</tr>\r\n");*/
//		
//		
//		sb.append("<tr><td>Campaigns:</td>" + "<td>"
//				+ AccountType.maxCampaignsMap.get(AccountType.BASIC)
//				+ "</td>" + "<td>"
//				+ AccountType.maxCampaignsMap.get(AccountType.PROFESSIONAL) + "</td>"
//				+ "<td>Custom</td>"
//				+ "</tr>\r\n");
//				
//		
//		/*sb.append("<tr><td>Tagged URLs:</td>"
//				+ "<td>Unlimited</td><td>Unlimited</td><td>Unlimited</td>"
//				//Free
//				+"<td>Unlimited</td>"
//				+"</tr>\r\n");*/
//		
//		sb.append("<tr><td>Tagged URLs:</td>"
//				+ "<td>Unlimited</td><td>Unlimited</td><td>Custom</td>"
//				
//				+"</tr>\r\n");
//		
//		/*sb.append("<tr><td>GA Integration:</td>"
//				+ "<td>Coming Soon</td><td>Coming Soon</td><td>No</td>"
//				//Free
//				+"<td>No</td>"
//				+"</tr>\r\n");*/
//		
//			
//		/*sb.append("<tr><td>URL Shortener:</td><td>Yes</td><td>Yes</td><td>Yes</td>"
//				//Free
//				+"<td>Yes</td>"
//				+"</tr>\r\n");*/
//		
//		
//		sb.append("<tr><td>URL Shortener:</td><td>Yes</td><td>Yes</td><td>Custom</td>"
//				
//				+"</tr>\r\n");
//		
//		/*sb.append("<tr><td>Annotations:</td><td>Yes</td><td>Yes</td><td>Yes</td>"
//				//Free
//				+"<td>Yes</td>"
//				+"</tr>\r\n");*/
//		
//		sb.append("<tr><td>Annotations:</td><td>Yes</td><td>Yes</td><td>Custom</td>"
//				
//				+"</tr>\r\n");
//		
//		
//		/*sb.append("<tr><td>Export Campaigns:</td>" + "<td>"
//				+ yesNo(AccountType.allowsExport(AccountType.PREMIUM))
//				+ "</td>" + "<td>"
//				+ yesNo(AccountType.allowsExport(AccountType.PLUS)) + "</td>"
//				+ "<td>"
//				+ yesNo(AccountType.allowsExport(AccountType.STANDARD))
//				+ "</td>" 
//				//Free
//				+ "<td>"+ yesNo(AccountType.allowsExport(AccountType.FREE)) + "</td>"
//				+ "</tr>\r\n");*/
//		
//		sb.append("<tr><td>Export Campaigns:</td>" + "<td>"
//				+ yesNo(AccountType.allowsExport(AccountType.BASIC))
//				+ "</td>" + "<td>"
//				+ yesNo(AccountType.allowsExport(AccountType.PROFESSIONAL)) + "</td>"
//				+ "<td>Custom</td>" 
//				
//				+ "</tr>\r\n");
//		
//		/*sb.append("<tr><td>Import Campaigns:</td>" + "<td>"
//				+ yesNo(AccountType.allowsImport(AccountType.PREMIUM))
//				+ "</td>" + "<td>"
//				+ yesNo(AccountType.allowsImport(AccountType.PLUS)) + "</td>"
//				+ "<td>"
//				+ yesNo(AccountType.allowsImport(AccountType.STANDARD))
//				+ "</td>"
//				//Free
//				+ "<td>"+ yesNo(AccountType.allowsImport(AccountType.FREE)) + "</td>"
//				+ "</tr>\r\n");*/
//		
//		sb.append("<tr><td>Import Campaigns:</td>" + "<td>"
//				+ yesNo(AccountType.allowsImport(AccountType.BASIC))
//				+ "</td>" + "<td>"
//				+ yesNo(AccountType.allowsImport(AccountType.PROFESSIONAL)) + "</td>"
//				+ "<td>Custom</td>"
//				
//				+ "</tr>\r\n");
//
//		/*sb.append("<tr align=\"center\"><td align=\"right\">&nbsp;</td>\r\n"
//				+ "<td>"
//				+ renderBrainTreeSubscriptionButton(AccountType.PREMIUM,
//						account,update)
//				+ "</td>\r\n"
//				+ "<td>"
//				+ renderBrainTreeSubscriptionButton(AccountType.PLUS, account,update)
//				+ "</td>\r\n"
//				+ "<td>"
//				+ renderBrainTreeSubscriptionButton(AccountType.STANDARD,
//						account,update) + "</td>\r\n" 
//				//Free
//				+ "<td>"+ renderBrainTreeSubscriptionButton(AccountType.FREE, account,update) + "</td>\r\n"
//				+  "</tr>\r\n");*/
//		
//		//free trial row
//		/*sb.append("<tr align=\"center\" style='line-height:45px;'><td  style='line-height:45px;' align=\"right\">Free Trial Period</td>\r\n"
//				+ "<td>30 Days</td>"
//				+ "<td>30 Days</td>\r\n"
//				+ "<td>30 Days</td>\r\n" 				
//				+  "</tr>\r\n");*/
//		
//		
////		sb.append("<tr align=\"center\"><td  align=\"left\"><a href='/SC_CreateAccount' class='btn red'>Cancel</a></td>\r\n"
//				sb.append("<tr align=\"center\"><td  align=\"left\"></td>\r\n"
//
//				+ "<td align='left'>"
//				+ renderBrainTreeSubscriptionButton(AccountType.BASIC,account,update)
//				+ "</td>\r\n"
//				+ "<td align=\"left\">"
//				+ renderBrainTreeSubscriptionButton(AccountType.PROFESSIONAL,account,update)
//				+ "</td>\r\n"
//				+ "<td align=\"left\"><a target='_blank' href='http://campaignalyzer.com/contact.html' class='mws-button gray' style='display: inline-block; padding:5px;'>Contact Us</a></td>\r\n" 
//				
//				+  "</tr>\r\n");
//
//		sb.append("</tbody></table>\r\n");
//
//		return sb.toString();
//	}

	private static String renderBrainTreeSubscriptionButton(String newType,
			Entity account, String update) {
		StringBuilder sb = new StringBuilder();

		long longAccountId = account.getKey().getId();
		String accountType = (String) account.getProperty("type");

		if (newType.equals(accountType) && !hasExpired(account)) {
			sb.append("&nbsp;<input type='button' class='btn default' value='Current Plan'>&nbsp;");
		} else if (AccountType.FREE.equals(newType)) {
			sb.append("<form method=\"POST\" action=\"/editaccountplan\" accept-charset=\"utf-8\">\r\n");
			sb.append("\t<input type=\"hidden\" name=\"aid\" value=\""
					+ longAccountId + "\"/>\r\n");
			sb.append("\t<input type=\"submit\" value=\" Free Account \" class=\"mws-button red\"/>\r\n");
			sb.append("</form>\r\n");

		} else {
			// String itemName = "CampaignAlyzer " + newType + " account";
			// String itemDescription = newType + " Account # " + longAccountId;

			// double firstPayment = AccountType.priceMap.get(newType);

			sb.append("<form method=\"POST\" action=\"/Payment.jsp\" accept-charset=\"utf-8\">\r\n");

			// if active plan, add upgrade or downgrade parameters
			if (isActiveAccount(account)
					&& !AccountType.FREE.equals(accountType)) {
				// if upgrade
				double upgradeFees = AccountType.calculateUpgradeFees(
						accountType, newType);
				if (0 != upgradeFees) {
					sb.append("<input type=\"hidden\" name=\"update\" value=\"1\">\r\n");

				}
			}

			sb.append("<input type=\"hidden\" name=\"update\" value=\""+update+"\"/>\r\n");
			sb.append("<input type=\"hidden\" name=\"newType\" value=\""+newType+"\"/>\r\n");
			sb.append("<input type=\"hidden\" name=\"_charset_\"/>\r\n");
			sb.append("<input type=\"hidden\" name=\"pid\" value=\""
					+ AccountType.PlanMap.get(newType) + "\"/>\r\n");
			sb.append("<input type=\"hidden\" name=\"np\" value=\""
					+ AccountType.priceMap.get(newType) + "\"/>\r\n");
			sb.append("<input type=\"hidden\" name=\"aid\" value=\""
					+ longAccountId + "\"/>\r\n");

			sb.append("<input type=\"submit\"  value='Subscribe' class='btn sbold green' name=\"BrainTree Checkout\" alt=\"Fast checkout through BrainTree"
					
					+ "\" height=\"46\" width=\"180\" />\r\n");
			sb.append("</form>\r\n");
		}
		return sb.toString();
	}

	private static String yesNo(boolean c) {
		if (c)
			return "Yes";
		else
			return "No";
	}

	public static boolean hasExpired(Entity account) {
		Date now = new Date();
		Date endDate = (Date) account.getProperty("endDate");
		if (null == endDate)
			return false;
		else
			return now.after(endDate);
	}

	public static boolean isNewAccount(Entity account) {
		return (AccountType.NEW.equals(account.getProperty("type")));
	}
	
	public static boolean isTrailAccount(Entity account) {
		return (AccountType.TRAIL.equals(account.getProperty("type")));
	}
	
	public static boolean isTrailAccountId (long longAccountId) {
		Entity account = getSingleAccount(longAccountId);
		return (AccountType.TRAIL.equals(account.getProperty("type")));
	}

	public static boolean isActiveAccount(Entity account) {
		return (!isNewAccount(account) && !hasExpired(account));
	}
	
	public static String standardizingID() {
		//Date today = new Date();		
		//Random id = new Random();
		String fixedPart = "CA-2012";
		
		//int accountID = id.nextInt(100000000);
		//int lastPart = id.nextInt(10);
		startPOint++;
		String formatedID = String.format("%06d", startPOint);
		String StanderedID = fixedPart + formatedID;
		
		return StanderedID;
	}
	
	
	public static String executePost(String targetURL, String urlParameters) {
		  HttpURLConnection connection = null;

		  try {
		    //Create connection
		    URL url = new URL(targetURL);
		    connection = (HttpURLConnection) url.openConnection();
		    connection.setRequestMethod("POST");
		    connection.setRequestProperty("Content-Type", 
		        "application/x-www-form-urlencoded");
		    connection.setRequestProperty("Content-Length", 
		        Integer.toString(urlParameters.getBytes().length));
		    connection.setRequestProperty("Content-Language", "en-US");  

		    connection.setUseCaches(false);
		    connection.setDoOutput(true);

		    //Send request
		    DataOutputStream wr = new DataOutputStream (
		        connection.getOutputStream());
		    wr.writeBytes(urlParameters);
		    wr.close();

		    //Get Response  
		    InputStream is = connection.getInputStream();
		    BufferedReader rd = new BufferedReader(new InputStreamReader(is));
		    StringBuilder response = new StringBuilder(); // or StringBuffer if Java version 5+
		    String line;
		    while ((line = rd.readLine()) != null) {
		      response.append(line);
		      response.append('\r');
		    }
		    rd.close();
		    System.out.print("response=" + response.toString());
		    return "response=" + response.toString();
		  } catch (Exception e) {
		    e.printStackTrace();
		    System.out.print("response=" + null);
		    return "response =" + null;
		  } finally {
		    if (connection != null) {
		      connection.disconnect();
		    }
		  }
		}
	
	public static String getTrailInfo(long longAccountId) {
		Entity account = getSingleAccount(longAccountId);
		long days = 0;
		if (account.getProperty("type").toString().equalsIgnoreCase(AccountType.TRAIL)) {
			Date startDate = (Date) account.getProperty("startDate");
			Date now = new Date();
		    long diff = now.getTime() - startDate.getTime();

//		    System.out.println ("Days: " + TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS));
		    days = Constant.NUMBER_OF_TRAIL_DAYS - TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
		}
		return "In Free Trial: " + days + " days left </br>" + 
		"You have "+days+ " days remaining until your free trial ends. Enter your payment information to continue using CampaignAlyzer when your trial period finishes." ;
		
	}
	
	public static String getTrailInfoDays(long longAccountId) {
		Entity account = getSingleAccount(longAccountId);
		long days = 0;
		if (account.getProperty("type").toString().equalsIgnoreCase(AccountType.TRAIL)) {
			Date startDate = (Date) account.getProperty("startDate");
			Date now = new Date();
		    long diff = now.getTime() - startDate.getTime();

//		    System.out.println ("Days: " + TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS));
		    days = Constant.NUMBER_OF_TRAIL_DAYS - TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
		}
		return "" + days ;		
	}
	
}
