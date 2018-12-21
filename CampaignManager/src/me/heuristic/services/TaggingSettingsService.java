package me.heuristic.services;

import java.util.Date;

import me.heuristic.references.TaggingSettings;
import me.heuristic.util.PMUtil;

import com.google.appengine.api.datastore.Entity;

public class TaggingSettingsService {
	public static Entity updateTaggingSettings(String myEmail, Entity account,
			String conAttribution, String parSeparator, String urlShortener, String caseSensetive) {
        // creating key and account entity

    	if (!TaggingSettings.NONDEFAULT_CONVERSION_ATTRIBUTION.equalsIgnoreCase(conAttribution))
    		conAttribution = TaggingSettings.DEFAULT_CONVERSION_ATTRIBUTION;
    	account.setProperty("conAttribution", conAttribution);

		if (!TaggingSettings.NONDEFAULT_PARAMETER_SEPARATOR.equalsIgnoreCase(parSeparator))
			parSeparator = TaggingSettings.DEFAULT_PARAMETER_SEPARATOR;
    	account.setProperty("parSeparator", parSeparator);

    	if (!TaggingSettings.NONDEFAULT_URL_SHORTENER.equalsIgnoreCase(urlShortener))
    		urlShortener = TaggingSettings.DEFAULT_URL_SHORTENER;
    	account.setProperty("urlShortener", urlShortener);

    	if (!TaggingSettings.NONDEFAULT_PARAMETER_CASE.equalsIgnoreCase(caseSensetive))
    		caseSensetive = TaggingSettings.DEFAULT_PARAMETER_CASE;
    	account.setProperty("caseSensetive", caseSensetive);

        Date date = new Date();
        account.setProperty("modifiedBy", myEmail);
        account.setProperty("modifiedAt", date);

        // saving entity
        PMUtil.persistEntity(account);
        return account;
	}

	public static void setDefaultTaggingSettings(Entity account) {
		account.setProperty("conAttribution", TaggingSettings.DEFAULT_CONVERSION_ATTRIBUTION);
    	account.setProperty("parSeparator", TaggingSettings.DEFAULT_PARAMETER_SEPARATOR);
    	account.setProperty("urlShortener", TaggingSettings.DEFAULT_URL_SHORTENER);
    	account.setProperty("caseSensetive", TaggingSettings.DEFAULT_PARAMETER_CASE);
	}

	public static String renderTaggingSettingsForm(long longAccountId,
			String conAttribution, String parSeparator, String urlShortener, String caseSensetive) {
	    StringBuilder sb = new StringBuilder();

    	sb.append("<form class=\"mws-form\" action=\"/edittaggingsettings\" method=\"post\">\r\n");
    	sb.append("<div class=\"mws-form-inline\">\r\n");

    	//new html
    	
    	// conversion attribution
    	sb.append("<div class=\"form-group\">"
    			+ "<label>Conversion Attribution:</label>"
    			+ "<div class=\"radio-list\">"
    			+ "<label class=\"radio-inline\">"
    			+ "<input type=\"radio\" name=\"conAttribution\" value=\"" + TaggingSettings.DEFAULT_CONVERSION_ATTRIBUTION + "\"");
    			if (!TaggingSettings.NONDEFAULT_CONVERSION_ATTRIBUTION.equalsIgnoreCase(conAttribution)) sb.append(" checked");
    			sb.append("/>Last Referral </label>"
    			
    			+ "<label class=\"radio-inline\">"
    			+ "<input type=\"radio\" name=\"conAttribution\" value=\"" + TaggingSettings.NONDEFAULT_CONVERSION_ATTRIBUTION + "\"");
    			if (TaggingSettings.NONDEFAULT_CONVERSION_ATTRIBUTION.equalsIgnoreCase(conAttribution)) sb.append(" checked");
    			sb.append("/>First Referral </label>"
    			+"</div></div>");
    	// end new html   	
    	
    	
    	// parameter separator
    			
		sb.append("<div class=\"form-group\">"
    			+ "<label>URL Parameter Separator:</label>"
    			+ "<div class=\"radio-list\">"
    			+ "<label class=\"radio-inline\">"
    			+ "<input type=\"radio\" name=\"parSeparator\" value=\""+ TaggingSettings.DEFAULT_PARAMETER_SEPARATOR+ "\"");
				if (!TaggingSettings.NONDEFAULT_PARAMETER_SEPARATOR.equalsIgnoreCase(parSeparator)) sb.append(" checked");
    			sb.append("/>? </label>"
    			
    			+ "<label class=\"radio-inline\">"
    			+ "<input type=\"radio\" name=\"parSeparator\" value=\"" + TaggingSettings.NONDEFAULT_PARAMETER_SEPARATOR + "\"");
    			if (TaggingSettings.NONDEFAULT_PARAMETER_SEPARATOR.equalsIgnoreCase(parSeparator)) sb.append(" checked");
    			sb.append("/># </label>"
    			+"</div></div>");		
    			
    	

    	// case sensetive
    			
		sb.append("<div class=\"form-group\">"
    			+ "<label>Tagged URL Case:</label>"
    			+ "<div class=\"radio-list\">"
    			+ "<label class=\"radio-inline\">"
    			+ "<input type=\"radio\" name=\"caseSensetive\" value=\"" + TaggingSettings.DEFAULT_PARAMETER_CASE + "\"");
				if (!TaggingSettings.NONDEFAULT_PARAMETER_CASE.equalsIgnoreCase(caseSensetive)) sb.append(" checked");
    			sb.append("/>As Entered </label>"
    			
    			+ "<label class=\"radio-inline\">"
    			+ "<input type=\"radio\" name=\"caseSensetive\" value=\"" + TaggingSettings.NONDEFAULT_PARAMETER_CASE + "\"");
    			if (TaggingSettings.NONDEFAULT_PARAMETER_CASE.equalsIgnoreCase(caseSensetive)) sb.append(" checked");
    			sb.append("/>Lower Case </label>"
    			+"</div></div>");  			
    			
    	

    	// submit row
    	sb.append("<div class=\"mws-button-row\">\r\n");
		sb.append("\t<input  type=\"submit\" value=\" Save \" class=\"btn green\" />\r\n");
		sb.append("\t<a href='/'class=\"btn red\">Cancel</a>\r\n");
		sb.append("</div>\r\n\r\n");

		sb.append("<input type=\"hidden\" name=\"aid\" value=\"" + longAccountId +"\"/>\r\n");
		sb.append("</div>\r\n");
		sb.append("</form>\r\n");

		return sb.toString();
	}
}
