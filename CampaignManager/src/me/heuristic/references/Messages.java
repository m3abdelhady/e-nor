package me.heuristic.references;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class Messages {
	public static final String UNKNOWN_ERROR = "UNKNOWN_ERROR";

	public static final String STYLE_SUCCESS = "STYLE_SUCCESS";
	public static final String STYLE_INFO = "STYLE_INFO";
	public static final String STYLE_WARNING = "STYLE_WARNING";
	public static final String STYLE_ERROR = "STYLE_ERROR";

	public static final String ACTION_CREATE = "CREATE";
	public static final String ACTION_UPDATE = "UPDATE";
	public static final String ACTION_DELETE = "DELETE";
	public static final String ACTION_COPY = "COPY";
	public static final String ACTION_DELETEAll = "DELETEAll";
	public static final String ACTION_ADD = "Add";
	public static final String ACTION_REMOVE = "Remove";

	public static final String INFO_SAVED = "SAVED";
	public static final String INFO_DELETED = "DELETED";
	public static final String INFO_IMPORTED = "IMPORTED";
	public static final String INFO_WAITING_PAYMENT_APPROVAL = "Your Payment is being processed.  This may take a couple minutes.  Thanks for your patience!";
	
	public static final String UNRECOGNIZED_USER = "UNRECOGNIZED_USER";
	public static final String UNSPECIFIED_ACCOUNT = "UNSPECIFIED_ACCOUNT";
	public static final String NO_SUCH_ACCOUNT = "NO_SUCH_ACCOUNT";
	public static final String ACCOUNT_EXPIRED = "ACCOUNT_EXPIRED";
	public static final String ACCOUNT_NEW = "ACCOUNT_NEW";

	public static final String UNSPECIFIED_CAMPAIGN = "UNSPECIFIED_CAMPAIGN";
	public static final String NO_SUCH_CAMPAIGN = "NO_SUCH_CAMPAIGN";
	public static final String UNSPECIFIED_TAGGED_URL = "UNSPECIFIED_TAGGED_URL";
	public static final String NO_SUCH_TAGGED_URL = "NO_SUCH_TAGGED_URL";
	public static final String ANAME_REQUIRED = "ANAME_REQUIRED";
	public static final String FNAME_REQUIRED = "FNAME_REQUIRED";
	public static final String LNAME_REQUIRED = "LNAME_REQUIRED";
	public static final String COMPANY_REQUIRED = "COMPANY_REQUIRED";
	public static final String ADDRESS_REQUIRED = "ADDRESS_REQUIRED";
	public static final String CITY_REQUIRED = "CITY_REQUIRED";
	public static final String STATE_REQUIRED = "STATE_REQUIRED";
	public static final String ZIP_REQUIRED = "ZIP_REQUIRED";
	public static final String COUNTRY_REQUIRED = "COUNTRY_REQUIRED";
	public static final String PHONE_REQUIRED = "PHONE_REQUIRED";
	public static final String WEBSITE_REQUIRED = "WEBSITE_REQUIRED";
	public static final String AGREEMENT_REQUIRED = "AGREEMENT_REQUIRED";

	public static final String CAMPAIGN_PARAM_REQUIRED = "CAMPAIGN_PARAM_REQUIRED";
	public static final String MEDIUM_PARAM_REQUIRED = "MEDIUM_PARAM_REQUIRED";
	public static final String SOURCE_PARAM_REQUIRED = "SOURCE_PARAM_REQUIRED";
	public static final String TERM_PARAM_REQUIRED = "TERM_PARAM_REQUIRED";
	public static final String CONTENT_PARAM_REQUIRED = "CONTENT_PARAM_REQUIRED";

	public static final String CAMPAIGN_REQUIRED = "CAMPAIGN_REQUIRED";
	public static final String MEDIUM_REQUIRED = "MEDIUM_REQUIRED";
	public static final String SOURCE_REQUIRED = "SOURCE_REQUIRED";
	public static final String URL_REQUIRED = "URL_REQUIRED";

	public static final String EMAIL_REQUIRED = "EMAIL_REQUIRED";
	public static final String REDUNDANT_EMAIL = "REDUNDANT_EMAIL";
	public static final String MALFORMATTED_EMAIL = "MALFORMATTED_EMAIL";
	public static final String AT_LEAST_1_ADMIN = "AT_LEAST_1_ADMIN";
	public static final String MALFORMATTED_URL = "MALFORMATTED_URL";
	public static final String NO_CHANNELS = "NO_CHANNELS";
	public static final String NO_CAMPAIGNS = "NO_CAMPAIGNS";
	public static final String NO_TAGGEDURLS = "NO_TAGGEDURLS";
	public static final String NO_DELIMITER = "NO_DELIMITER";
	public static final String NO_IMPORT_FILE = "NO_IMPORT_FILE";
	public static final String EMPTY_IMPORT_FILE = "EMPTY_IMPORT_FILE";

	public static final String EXCEED_MAX_FREE_ACCOUNTS = "EXCEED_MAX_FREE_ACCOUNTS";
	public static final String EXCEED_MAX_USERS = "EXCEED_MAX_USERS";
	public static final String EXCEED_MAX_CHANNELS = "EXCEED_MAX_CHANNELS";
	public static final String EXCEED_MAX_CAMPAIGNS = "EXCEED_MAX_CAMPAIGNS";
	public static final String NO_MANAGE_USERS_ALLOWED = "NO_MANAGE_USERS_ALLOWED";
	public static final String NO_IMPORT_ALLOWED = "NO_IMPORT_ALLOWED";
	public static final String NO_EXPORT_ALLOWED = "NO_EXPORT_ALLOWED";
	public static final String NO_PERMISSION = "NO_PERMISSIONS";
	
	public static final String ERR_SUBSCRIPTION_CREATION = "ERR_SUBSCRIPTION_CREATION";
	public static final String ERR_PAYMENTMETHOD = "ERR_PAYMENTMETHOD";
	public static final String ERR_BRAINTREE_CUSTOMER = "ERR_BRAINTREE_CUSTOMER";

	public static final String ORDER_ERROR = "ORDER_ERROR";
	
	public static final Map<String, String> messageDict = new HashMap<String, String>();

	static {
		messageDict.put(UNKNOWN_ERROR, "Unknown Error!");

		messageDict.put(INFO_SAVED, "Saved successfully.");
		messageDict.put(INFO_DELETED, "Deleted successfully.");
		messageDict.put(INFO_IMPORTED, "Impoterd successfully.");
		messageDict.put(INFO_WAITING_PAYMENT_APPROVAL, "Your Payment is being processed.  This may take a couple minutes.  Thanks for your patience!.");

		messageDict.put(UNRECOGNIZED_USER, "Please Click Here to Sign In.");
		messageDict.put(UNSPECIFIED_ACCOUNT, "Please specify an account!");
		messageDict.put(NO_SUCH_ACCOUNT, "Requested account not found!");
		messageDict.put(ACCOUNT_EXPIRED, "This account has expired. Please renew subscription to re-activate!");
		messageDict.put(ACCOUNT_NEW, "This account is not active yet. Click here to Activate!");

		messageDict.put(UNSPECIFIED_CAMPAIGN, "Please specify a campaign!");
		messageDict.put(NO_SUCH_CAMPAIGN, "Requested campaign not found!");
		messageDict.put(UNSPECIFIED_TAGGED_URL, "Please specify a tagged URL!");
		messageDict.put(NO_SUCH_TAGGED_URL, "Requested URL not found!");
		messageDict.put(FNAME_REQUIRED, "First Name is a required field!");
		messageDict.put(LNAME_REQUIRED, "Last Name is a required field!");
		messageDict.put(COMPANY_REQUIRED, "Company Name is a required field!");
		messageDict.put(ANAME_REQUIRED, "Account Name is a required field!");
		messageDict.put(ADDRESS_REQUIRED, "Address Name is a required field!");
		messageDict.put(CITY_REQUIRED, "City is a required field!");
		messageDict.put(STATE_REQUIRED, "State is a required field!");
		messageDict.put(ZIP_REQUIRED, "Zip/ Postal Code is a required field!");
		messageDict.put(COUNTRY_REQUIRED, "Country is a required field!");
		messageDict.put(PHONE_REQUIRED, "Phone is a required field!");
		messageDict.put(WEBSITE_REQUIRED, "Website is a required field!");
		messageDict.put(AGREEMENT_REQUIRED, "To proceed, you must agree to the Terms of Use and Privacy Policy!");

		messageDict.put(CAMPAIGN_PARAM_REQUIRED, "Campaign parameter is a required field!");
		messageDict.put(MEDIUM_PARAM_REQUIRED, "Medium parameter is a required field!");
		messageDict.put(SOURCE_PARAM_REQUIRED, "Source parameter is a required field!");
		messageDict.put(TERM_PARAM_REQUIRED, "Term parameter is a required field!");
		messageDict.put(CONTENT_PARAM_REQUIRED, "Content parameter is a required field!");

		messageDict.put(CAMPAIGN_REQUIRED, "Campaign is a required field!");
		messageDict.put(MEDIUM_REQUIRED, "Medium is a required field!");
		messageDict.put(SOURCE_REQUIRED, "Source is a required field!");
		messageDict.put(URL_REQUIRED, "URL is a required field!");

		messageDict.put(EMAIL_REQUIRED, "User E-mail is a required field!");
		messageDict.put(MALFORMATTED_EMAIL, "This e-mail address is not properly formatted!");
		messageDict.put(MALFORMATTED_URL, "This Internet address is not properly formatted!");
		messageDict.put(REDUNDANT_EMAIL, "A user with this email address already exists!");
		messageDict.put(AT_LEAST_1_ADMIN,"There must be at least one Administrator per account!");

 		messageDict.put(EXCEED_MAX_FREE_ACCOUNTS, "You have reached the maximim number of FREE accounts!<br/>");
		messageDict.put(EXCEED_MAX_USERS, "You have reached the maximim number of users for this account type!<br/>\r\n" +
				" You may remove a user or, better, upgrade your account!");
		messageDict.put(EXCEED_MAX_CHANNELS, "You have reached the maximim number of channels!");
		messageDict.put(EXCEED_MAX_CAMPAIGNS, "You have reached the maximim number of campaigns for this account type!<br/>\r\n" +
				" You may remove a campaign or, better, upgrade your account!");
		
		messageDict.put(NO_MANAGE_USERS_ALLOWED, "Editing Users is not available for your current plan.<br/>\r\nTo use this feature, you need to upgrade your account plan.");
		messageDict.put(NO_IMPORT_ALLOWED, "Importing Campaigns is not available for your current plan.<br/>\r\nTo use this feature, you need to upgrade your account plan.");
		messageDict.put(NO_EXPORT_ALLOWED, "Exporting Campaigns is not available for your current plan.<br/>\r\nTo use this feature, you need to upgrade your account plan.");
		messageDict.put(NO_PERMISSION,  "You do not have authorization to perform this action!<br/>\r\nPlease contact the Account Administrator.");

		messageDict.put(NO_CHANNELS, "You must select/ enter at least one channel!");
		messageDict.put(NO_CAMPAIGNS, "You must select at least one campaign!");
		messageDict.put(NO_TAGGEDURLS, "You must select at least one Tagged URL!");
		messageDict.put(NO_DELIMITER, "No delimiter was specified!");
		messageDict.put(NO_IMPORT_FILE, "No file was specified!");
		messageDict.put(EMPTY_IMPORT_FILE, "Import file was found empty!");
		messageDict.put(ORDER_ERROR, "Either the order number doesn't exist or it has been used with another account!");
		messageDict.put(ERR_SUBSCRIPTION_CREATION, "Error in Subscription Creation!");
		messageDict.put(ERR_PAYMENTMETHOD, "Payment Information is not valid!");
		messageDict.put(ERR_BRAINTREE_CUSTOMER, "Payment Information is not valid!");
	}

	public static String renderErrorMessage(String error) {
		StringBuilder sb = new StringBuilder();

		String message = messageDict.get(error);
		String backgroundcolor = "";
		String color = "";
		//String longAccountId = "";
		if ("UNRECOGNIZED_USER" == error || "ACCOUNT_NEW" ==error){
			backgroundcolor= "#17C4BB";
			color = "#ffffff";
			
		}
		sb.append("<div style=\"background-color:"+backgroundcolor+";width:100%; height:50px; padding:15px 8px 0px 45px;color:"+color+"\" class=\"note note-danger\">\r\n");

		
		if (null!=message && "UNRECOGNIZED_USER" == error) {
			sb.append("<a style='color:#fff; font-size:15px;display: inline-block;width: 100%;height: 100%;' id='messageLogin' href=''>"+message+"</a>");
		} 
		
		else if(null!=message && "ACCOUNT_NEW" ==error){
			sb.append("<a style='color:#fff; font-size:15px;display: inline-block;width: 100%;height: 100%;' id='activationurl'>"+message+"</a>");
		}
		else {
			sb.append(error);
		}
		sb.append("<br/><br/>\r\n");
		//sb.append("<a style='color:#fff; font-size:15px;' id='messageLogin' href=''>sign In Here</> </a>");
		sb.append("<script type='text/javascript'>"
				+ "$('#messageLogin').attr('href', $('#signin').attr('href')) ;"
				+ "$('#activationurl').attr('href', '/EditAccountPlan.jsp?aid='+$('#aaid').val())"
				
				+ "</script>");
				
		sb.append("</div>\r\n");
		
		
		

		return sb.toString();
	}

	public static String renderMessages(Collection<String> messageCodes, String messageStyle) {
		StringBuilder sb = new StringBuilder();

		if (STYLE_ERROR.equalsIgnoreCase(messageStyle))
			sb.append("<div class=\"note note-danger\"><ul>\r\n");
		else if (STYLE_WARNING.equalsIgnoreCase(messageStyle))
			sb.append("<div class=\"mws-form-message warning\"><ul>\r\n");
		else if (STYLE_SUCCESS.equalsIgnoreCase(messageStyle))
			sb.append("<div class=\"note note-success\"><ul>\r\n");
		else
			sb.append("<div class=\"note note-danger\"><ul>\r\n");

		for(String messageCode: messageCodes) {
			
			if (null!=messageDict.get(messageCode)) {
				sb.append("<li>" + messageDict.get(messageCode) + "</li>\r\n");
			} else {
				sb.append("<li>" + messageCode + "</li>\r\n");
			}
		}
		sb.append("</ul></div>\r\n");

		return sb.toString();
	}
}
