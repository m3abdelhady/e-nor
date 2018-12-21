package me.heuristic.services;

import java.util.Date;

import me.heuristic.references.TaggingParameters;
import me.heuristic.util.PMUtil;
import com.google.appengine.api.datastore.Entity;

public class TaggingParametersService {
	public static Entity updateTaggingParameters(String myEmail, Entity account,
			String campaignParam, String mediumParam, String sourceParam,
			String termParam, String contentParam, String queryParam) {
        // creating key and account entity

    	account.setProperty("campaignParam", campaignParam);
    	account.setProperty("mediumParam", mediumParam);
    	account.setProperty("sourceParam", sourceParam);
    	account.setProperty("termParam", termParam);
    	account.setProperty("contentParam", contentParam);
    	account.setProperty("queryParam", queryParam);

        Date date = new Date();
        account.setProperty("modifiedBy", myEmail);
        account.setProperty("modifiedAt", date);

        // saving entity
        PMUtil.persistEntity(account);
        return account;
	}

	public static void setDefaultTaggingParameters(Entity account) {
    	account.setProperty("campaignParam", TaggingParameters.DEFAULT_CAMPAIGN_PARAMETER);
    	account.setProperty("mediumParam", TaggingParameters.DEFAULT_MEDIUM_PARAMETER);
    	account.setProperty("sourceParam", TaggingParameters.DEFAULT_SOURCE_PARAMETER);
    	account.setProperty("termParam", TaggingParameters.DEFAULT_TERM_PARAMETER);
    	account.setProperty("contentParam", TaggingParameters.DEFAULT_CONTENT_PARAMETER);
    	account.setProperty("queryParam", TaggingParameters.DEFAULT_QUERY_PARAMETER);
	}

	public static String renderTaggingParametersForm(long longAccountId,
			String campaignParam, String mediumParam, String sourceParam,
			String termParam, String contentParam, String queryParam) {
	    StringBuilder sb = new StringBuilder();

    	sb.append("<form name=\"taggingParametersForm\" class=\"mws-form\" action=\"/edittaggingparameters\" method=\"post\" onSubmit=\"return validateTaggingParametersForm();\">\r\n");
    	sb.append("<div class=\"mws-form-inline\">\r\n");

    	// parameters
    	sb.append("\t<div class=\"form-group\">\r\n");
    	sb.append("\t\t<p>If your organization is using multiple web analytics tools to track your web traffic, then it is more than likely that the five default Google Analytics UTM parameters are not sufficient, and you need extra parameters to pass campaign information to the additional analytics tool.</p>\r\n" +
    			"<p>CampaignAlyzer offers this option and allows you to specify a sixth query parameter to use to push campaign information to your desired analytics tool. You may opt to leave blank if not needed.</p>\r\n");
    	
    	sb.append("\t\t<label>Query Parameter:</label>\r\n");
    	sb.append("\t\t<div class=\"mws-form-item small\">\r\n");
		sb.append("\t\t\t<input type=\"text\" name=\"queryParam\" value=\"" + queryParam +"\" class=\"form-control\" />\r\n");
    	sb.append("\t\t</div>\r\n");    	
    	
    	sb.append("\t</div>\r\n");    	

    	sb.append("\t<hr width=\"75%\">\r\n");

    	sb.append("\t<div class=\"form-group\">\r\n");    	
    	
    	sb.append("\t\t<p>If your organization is already using different tracking variables than the default Google Analytics parameters to tag your online links, you can map these existing variables to Google Analytics’ campaign tracking variables.</p>\r\n" +
    			"<p>CampaignAlyzer has the flexibility to use any campaign tracking variables you prefer. For example, your campaign tracking variables may use “keyword” instead of “utm_term” to identify the keyword or use “cid” instead of “utm_campaign” to identify the campaign name.</p>\r\n");
    	sb.append("\t\t<label>Campaign:</label>\r\n");
    	sb.append("\t\t<div class=\"mws-form-item small\">\r\n");
		sb.append("\t\t\t<input type=\"text\" name=\"campaignParam\" value=\"" + campaignParam +"\" class=\"form-control\" />\r\n");
    	sb.append("\t\t</div>\r\n");
    	sb.append("\t</div>\r\n");
    	
    	sb.append("\t<div class=\"form-group\">\r\n");
    	sb.append("\t\t<label>Medium:</label>\r\n");
    	sb.append("\t\t<div class=\"mws-form-item small\">\r\n");
		sb.append("\t\t\t<input type=\"text\" name=\"mediumParam\" value=\"" + mediumParam +"\" class=\"form-control\" />\r\n");
    	sb.append("\t\t</div>\r\n");
    	sb.append("\t</div>\r\n");

    	sb.append("\t<div class=\"form-group\">\r\n");
    	sb.append("\t\t<label>Source:</label>\r\n");
    	sb.append("\t\t<div class=\"mws-form-item small\">\r\n");
		sb.append("\t\t\t<input type=\"text\" name=\"sourceParam\" value=\"" + sourceParam +"\" class=\"form-control\" />\r\n");
    	sb.append("\t\t</div>\r\n");
    	sb.append("\t</div>\r\n");

    	sb.append("\t<div class=\"form-group\">\r\n");
    	sb.append("\t\t<label>Term:</label>\r\n");
    	sb.append("\t\t<div class=\"mws-form-item small\">\r\n");
		sb.append("\t\t\t<input type=\"text\" name=\"termParam\" value=\"" + termParam +"\" class=\"form-control\" />\r\n");
    	sb.append("\t\t</div>\r\n");
    	sb.append("\t</div>\r\n");

    	sb.append("\t<div class=\"form-group\">\r\n");
    	sb.append("\t\t<label>Content:</label>\r\n");
    	sb.append("\t\t<div class=\"mws-form-item small\">\r\n");
		sb.append("\t\t\t<input type=\"text\" name=\"contentParam\" value=\"" + contentParam +"\" class=\"form-control\" />\r\n");
    	sb.append("\t\t</div>\r\n");
    	sb.append("\t</div>\r\n");
    	
    	// submit row
    	sb.append("<div class=\"mws-button-row\">\r\n");
		sb.append("\t<input type=\"submit\" value=\" Save \" class=\"btn green\"/>\r\n");
		sb.append("\t<input type=\"button\" value=\" Reset To Default Tags \" class=\"btn default\" onClick=\"setDefaultParams();\"/>\r\n");
		sb.append("\t<a href='/'class=\"btn red\">Cancel</a>\r\n");
		sb.append("</div>\r\n\r\n");

		sb.append("<input type=\"hidden\" name=\"aid\" value=\"" + longAccountId +"\"/>\r\n");
		
		sb.append("</div>\r\n");
		sb.append("</form>\r\n");

		return sb.toString();
	}
}
