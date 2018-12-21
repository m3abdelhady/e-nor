package me.heuristic.services;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.util.Date;
//import org.apache.tools.ant.taskdefs.Javadoc.PackageName;







import java.util.Iterator;

import me.heuristic.util.HtmlUtil;
import me.heuristic.util.PMUtil;
import me.heuristic.references.Porting;
import me.heuristic.references.Messages;
import me.heuristic.references.TaggingSettings;
import me.heuristic.references.TaggingParameters;
import me.heuristic.references.UserType;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
/*import com.google.appengine.api.users.UserServiceFactory;*/

public class MCTaggedUrlService {
	public static void deleteTaggedUrl(Entity mobiletaggedUrl) {
    	Key mobiletaggedUrlKey = mobiletaggedUrl.getKey();
    	PMUtil.deleteEntity(mobiletaggedUrlKey);
	}

	public static Entity createOrUpdateTaggedUrl(String myEmail,
			long longAccountId, long longCampaignId, long longTaggedUrlId,
			String utm_term, String utm_content, String utm_extra) {

		// creating key and account entity
        Date date = new Date();
        Entity MobiletaggedUrl;
		Key key = KeyFactory.createKey("Account", longAccountId);
		key = KeyFactory.createKey(key, "mobileCampaign", longCampaignId);
		if (0==longTaggedUrlId) {
			// new account
			MobiletaggedUrl = new Entity("MobileTaggedUrl", key);
			MobiletaggedUrl.setProperty("createdBy", myEmail);
			MobiletaggedUrl.setProperty("createdAt", date);
    	} else {
			// editing existing taggedUrl
    		MobiletaggedUrl = getSingleTaggedUrl(longAccountId, longCampaignId, longTaggedUrlId);
    	}

        // setting properties
		//MobiletaggedUrl.setProperty("packageName", packageName);
		//MobiletaggedUrl.setProperty("utm_url", utm_url);
		MobiletaggedUrl.setProperty("utm_term", utm_term);
    	MobiletaggedUrl.setProperty("utm_content", utm_content);
    	MobiletaggedUrl.setProperty("utm_extra", utm_extra);
    	MobiletaggedUrl.setProperty("modifiedBy", myEmail);
    	MobiletaggedUrl.setProperty("modifiedAt", date);

        // saving entity
        PMUtil.persistEntity(MobiletaggedUrl);
        return MobiletaggedUrl;
	}

	public static Iterable<Entity> getAllTaggedUrlsFor(long longAccountId, long longCampaignId) {
	    Key key = KeyFactory.createKey("Account", longAccountId);
	    key = KeyFactory.createKey(key, "mobileCampaign", longCampaignId);
	    return PMUtil.listChildren("MobileTaggedUrl", key);
	}

	public static Entity getSingleTaggedUrl(long longAccountId, long longCampaignId, long longTaggedUrlId) {
		Key key = KeyFactory.createKey("Account", longAccountId);
		key = KeyFactory.createKey(key, "mobileCampaign", longCampaignId);
		key = KeyFactory.createKey(key, "MobileTaggedUrl", longTaggedUrlId);
		return PMUtil.findEntity(key);
	}
	
	public static String renderTaggedUrlShareForm(String shortUrl, String completeUrl) {
	    StringBuilder sb = new StringBuilder();

	    sb.append("<form class=\"mws-form\" action=\"\" method=\"post\" name=\"shortUrl\">\r\n");
    	sb.append("<div class=\"mws-form-inline\">\r\n");

    	// Short URL
    	sb.append("<div class=\"mws-form-row\">\r\n");
    	sb.append("\t<label>Short URL:</label>\r\n");
    	sb.append("\t<div class=\"mws-form-item small\">\r\n");
		sb.append("\t<input type=\"text\" id=\"shorturl\" name=\"shortUrl\" value=\"" + shortUrl +"\" class=\"mws-textinput\" />\r\n");
		sb.append("<button class=\"copyURL mws-button red\" style=\"padding:3px 9px; \" onclick='copyToClipboard(\"#shorturl\")'>Copy Short URL</button>");
    	sb.append("\t</div>\r\n");
    	sb.append("</div>\r\n");

    	// Full Tagged URL
    	sb.append("<div class=\"mws-form-row\">\r\n");
    	sb.append("\t<label>Full Tagged URL:</label>\r\n");
    	sb.append("\t<div class=\"mws-form-item small\">\r\n");
		sb.append("\t\t<input type=\"text\" id=\"fullurl\" name=\"completeUrl\" value=\"" + completeUrl +"\" class=\"mws-textinput\" />\r\n");
		sb.append("<button class=\"copyURL mws-button red\" style=\"padding:3px 9px; \" onclick='copyToClipboard(\"#fullurl\")'>Copy Full URL</button>");
		sb.append("\t</div>\r\n");
    	sb.append("</div>\r\n");

    	// Sharing buttons
    	sb.append("<div class=\"mws-form-row\">\r\n");
    	sb.append("\tShare with your friends:&nbsp;\r\n");
		sb.append("<a href=\"http://www.facebook.com/sharer.php?u=" + HtmlUtil.encode(shortUrl) + "&t=" + HtmlUtil.encode("") +
				"\" onclick=\"window.open(this.href, 'sharer', 'toolbar=0,status=0,width=740,height=320'); return false;\"><img src=\"/images/shareFacebook.png\"></a>&nbsp;\r\n");
    	sb.append("\t<a target=\"_blank\" name=\"twt_share\" href=\"https://twitter.com/share?url=" + HtmlUtil.encode(shortUrl) +
    			"\" onclick=\"window.open(this.href, 'sharer', 'toolbar=0,status=0,width=740,height=320'); return false;\"><img src=\"/images/shareTwitter.png\"></a>&nbsp;\r\n");
    	sb.append("\t<a target=\"_blank\" name=\"google_share\" href=\"https://plus.google.com/u/1/share?url=" + HtmlUtil.encode(shortUrl) + "&source=campaignalyzer.com" +
    			"\" onclick=\"window.open(this.href, 'sharer', 'toolbar=0,status=0,width=740,height=320'); return false;\"><img src=\"/images/shareGoogle.png\"></a>&nbsp;\r\n");
    	sb.append("\t<a target=\"_blank\" name=\"linkedin_share\" href=\"http://www.linkedin.com/shareArticle?url=" + HtmlUtil.encode(shortUrl) + "&source=campaignalyzer.com" +
    			"\" onclick=\"window.open(this.href, 'sharer', 'toolbar=0,status=0,width=740,height=320'); return false;\"><img src=\"/images/shareLinkedin.png\"></a>&nbsp;\r\n");
    	sb.append("</div>\r\n");

		sb.append("</div></form>\r\n");

	    return sb.toString();
	}

	/*public static String renderTaggedUrlListForm(String myRole, Entity account, Entity mobilecampaign, long myLongTaggedUrlId,
			String my_utm_url, String my_utm_term, String my_utm_content, String my_utm_extra) {
	    StringBuilder sb = new StringBuilder();
	    long longAccountId = account.getKey().getId();
	    long longCampaignId = mobilecampaign.getKey().getId();

	    // retrieve campaign data
	    String packageName = (String) mobilecampaign.getProperty("packageName");
		String utm_campaign = (String) mobilecampaign.getProperty("utm_campaign");
		String utm_medium = (String) mobilecampaign.getProperty("utm_medium");
		String utm_source = (String) mobilecampaign.getProperty("utm_source");
		
	    // retrieve and render the Tagged URLs of the selected Campaign.
	    Iterable<Entity> taggedUrls = PMUtil.listChildren("MobileTaggedUrl", mobilecampaign.getKey());

	    sb.append("<table class=\"mws-table\">\r\n");
	    sb.append("<thead><tr>\r\n");
	    //sb.append("\t<th>Id</th>\r\n");
	    //sb.append("\t<th>URL*</th>\r\n");
	    sb.append("\t<th>Term</th>\r\n");
	    sb.append("\t<th>Content</th>\r\n");
	    //sb.append("\t<th>Extra Parameter</th>\r\n");
	    sb.append("\t<th>Action</th>\r\n");
	    sb.append("</tr>\r\n");
	    sb.append("</thead>\r\n");
	    sb.append("<tbody>\r\n");
	
		long longTaggedUrlId = 0;
		String utm_url = "";
		String utm_term = "";
		String utm_content = "";
		String utm_extra = "";
		String utm_long_url = "";
		Date modifiedAt = null;
		for (Entity moniletaggedUrl : taggedUrls) {
			// retrieve tagged url id
			longTaggedUrlId = moniletaggedUrl.getKey().getId();

	    	utm_url = (String) moniletaggedUrl.getProperty("utm_url");
	    	if (null==utm_url) utm_url = "";
	    	utm_term = (String) moniletaggedUrl.getProperty("utm_term");
	    	if (null==utm_term) utm_term = "";
	    	utm_content = (String) moniletaggedUrl.getProperty("utm_content");
	    	if (null==utm_content) utm_content = "";
	    	utm_extra = (String) moniletaggedUrl.getProperty("utm_extra");
	    	if (null==utm_extra) utm_extra="";
	    	modifiedAt = (Date) moniletaggedUrl.getProperty("modifiedAt");

			// if user has permission AND is editing this tagged url
			if (UserType.hasPermission(myRole, UserType.PERMISSION_MANAGE_TAGGED_URLS) &&
					longTaggedUrlId == myLongTaggedUrlId) {
				if ("".equals(my_utm_url) && "".equals(my_utm_term) && "".equals(my_utm_content) && "".equals(my_utm_extra)) {
					// if first time (no errors)
					// pre-fill with retrieved data
					sb.append(renderTaggedUrlFormRow(longAccountId, longCampaignId, longTaggedUrlId, packageName, 
							utm_url, utm_term, utm_content, utm_extra));   
				} else {
					// if it has errors
					// pre-fill with previously entered values
					sb.append(renderTaggedUrlFormRow(longAccountId, longCampaignId, longTaggedUrlId, packageName,
							my_utm_url, my_utm_term, my_utm_content, my_utm_extra));   
				}
			} else {
				// if not editing
				// display tagged url details
				
				//utm_long_url = constructCompleteUrl(account,
				//	utm_campaign, utm_medium, utm_source, utm_url, utm_term, utm_content, utm_extra);
				utm_long_url = "https://play.google.com/store/apps/details?id="+ packageName + 
						 "&referrer=utm_source=" + utm_source +
						 "&utm_medium=" + utm_medium + 
						 "&utm_Term=" + utm_term +
						 "&utm_Content=" + utm_content +
						 "&utm_Campaign=" + utm_campaign;

				sb.append(renderTaggedUrlDetailsRow(myRole, longAccountId, longCampaignId, longTaggedUrlId, 
						utm_url, utm_term, utm_content, utm_extra, utm_long_url));
				
				//new 23/9
				
				sb.append(renderTaggedUrlDetailsRow(myRole, longAccountId, longCampaignId, longTaggedUrlId, 
						utm_url, utm_term, utm_content, utm_extra, utm_long_url, modifiedAt));
				//
			}
		}

		// if use has permission AND not editing
		if (UserType.hasPermission(myRole, UserType.PERMISSION_MANAGE_TAGGED_URLS) &&
				0==myLongTaggedUrlId) {
			if ("".equals(my_utm_url) && "".equals(my_utm_term) && "".equals(my_utm_content) && "".equals(my_utm_extra)) {
				// if first time (no errors)
				// display empty fields
				sb.append(renderTaggedUrlFormRow(longAccountId, longCampaignId, 0, "", "", "", "", packageName));   
			} else {
				// if it has errors
				// display previously entered values
				sb.append(renderTaggedUrlFormRow(longAccountId, longCampaignId, 0, packageName,
						my_utm_url, my_utm_term, my_utm_content, my_utm_extra));   
			}
		}

		sb.append("</tbody>\r\n");
		sb.append("</table>\r\n");

	    return sb.toString();
	}*/
	
	
	//new 23/9
	public static String renderTaggedUrlListForm(String myRole, Entity account, Entity mobilecampaign, long myLongTaggedUrlId,
			String my_utm_url, String my_utm_term, String my_utm_content, String my_utm_extra) {
		StringBuilder sb = new StringBuilder();
	    long longAccountId = account.getKey().getId();
	    long longCampaignId = mobilecampaign.getKey().getId();

	    // retrieve campaign data
	    String packageName = (String) mobilecampaign.getProperty("packageName");
		String utm_campaign = (String) mobilecampaign.getProperty("utm_campaign");
		String utm_medium = (String) mobilecampaign.getProperty("utm_medium");
		String utm_source = (String) mobilecampaign.getProperty("utm_source");
		
	    // retrieve and render the Tagged URLs of the selected Campaign.
	    Iterable<Entity> taggedUrls = PMUtil.listChildren("MobileTaggedUrl", mobilecampaign.getKey());
/*	    
	 // if user has permission AND is editing this tagged url
	 			if (UserType.hasPermission(myRole, UserType.PERMISSION_MANAGE_TAGGED_URLS) &&
	 					longTaggedUrlId == myLongTaggedUrlId) {
	 				if ("".equals(my_utm_url) && "".equals(my_utm_term) && "".equals(my_utm_content) && "".equals(my_utm_extra)) {
	 					// if first time (no errors)
	 					// pre-fill with retrieved data
	 					sb.append(renderTaggedUrlFormRow(longAccountId, longCampaignId, longTaggedUrlId, 
	 							utm_url, utm_term, utm_content, utm_extra));   
	 				} else {
	 					// if it has errors
	 					// pre-fill with previously entered values
	 					sb.append(renderTaggedUrlFormRow(longAccountId, longCampaignId, longTaggedUrlId, 
	 							my_utm_url, my_utm_term, my_utm_content, my_utm_extra));   
	 				}
	 			}
	 			
	 			else{
	 			// if use has permission AND not editing
	 			if (UserType.hasPermission(myRole, UserType.PERMISSION_MANAGE_TAGGED_URLS) &&
	 					0==myLongTaggedUrlId) {
	 				if ("".equals(my_utm_url) && "".equals(my_utm_term) && "".equals(my_utm_content) && "".equals(my_utm_extra)) {
	 					// if first time (no errors)
	 					// display empty fields
	 					sb.append(renderTaggedUrlFormRow(longAccountId, longCampaignId, 0, "", "", "", ""));   
	 				} else {
	 					// if it has errors
	 					// display previously entered values
	 					sb.append(renderTaggedUrlFormRow(longAccountId, longCampaignId, 0, 
	 							my_utm_url, my_utm_term, my_utm_content, my_utm_extra));   
	 				}
	 			}
	 			}*/
		//renderTaggedUrlFormRow(longAccountId, longCampaignId, longTaggedUrlId);
	    
	    sb.append("<form method='post' action='/editmobiletaggedurl?aid="+longAccountId+"&cid="+longCampaignId+"&act=" + Messages.ACTION_DELETEAll+"'>");
		
	    sb.append("<table class=\"table table-bordered table-hover modified4 \">\r\n");
	    sb.append("<thead><tr>\r\n");
	    //sb.append("\t<th>Id</th>\r\n");
	    sb.append("\t<th style='display:none;'>Id</th>\r\n");
	    sb.append("<th class='no-sort' style='padding:8px 10px; width:49px !important; min-width:49px !important;'><input type=\"checkbox\" id='deletAll'/></th>\r\n");
	    sb.append("\t<th>Destination URL*</th>\r\n");
	    sb.append("\t<th style='display:none;'>Shortened</th>\r\n");	    
	    sb.append("\t<th>Term</th>\r\n");
	    sb.append("\t<th>Content</th>\r\n");
	   // sb.append("\t<th>Extra Parameter</th>\r\n");
	    sb.append("\t<th>Modified</th>\r\n");
	    sb.append("\t<th class='no-sort' style='width:130px !important;'>Copy Tagged URL</th>");
	    sb.append("\t<th class='no-sort'>Action</th>\r\n");
	    sb.append("</tr>\r\n");
	    sb.append("</thead>\r\n");
	    sb.append("<tbody>\r\n");
	
		
	    long longTaggedUrlId = 0;
		String utm_url = "";
		String utm_term = "";
		String utm_content = "";
		String utm_extra = "";
		String utm_long_url = "";
		Date modifiedAt = null;
		for (Entity mobiletaggedUrl : taggedUrls) {
			// retrieve tagged url id
			longTaggedUrlId = mobiletaggedUrl.getKey().getId();

	    	utm_url = (String) mobiletaggedUrl.getProperty("utm_url");
	    	if (null==utm_url) utm_url = "";
	    	utm_term = (String) mobiletaggedUrl.getProperty("utm_term");
	    	if (null==utm_term) utm_term = "";
	    	utm_content = (String) mobiletaggedUrl.getProperty("utm_content");
	    	if (null==utm_content) utm_content = "";
	    	utm_extra = (String) mobiletaggedUrl.getProperty("utm_extra");
	    	if (null==utm_extra) utm_extra="";
	    	modifiedAt = (Date) mobiletaggedUrl.getProperty("modifiedAt");

	    	// old block
			// if user has permission AND is editing this tagged url
			/*if (UserType.hasPermission(myRole, UserType.PERMISSION_MANAGE_TAGGED_URLS) &&
					longTaggedUrlId == myLongTaggedUrlId) {
				if ("".equals(my_utm_url) && "".equals(my_utm_term) && "".equals(my_utm_content) && "".equals(my_utm_extra)) {
					// if first time (no errors)
					// pre-fill with retrieved data
					sb.append(renderTaggedUrlFormRow(longAccountId, longCampaignId, longTaggedUrlId, 
							utm_url, utm_term, utm_content, utm_extra));   
				} else {
					// if it has errors
					// pre-fill with previously entered values
					sb.append(renderTaggedUrlFormRow(longAccountId, longCampaignId, longTaggedUrlId, 
							my_utm_url, my_utm_term, my_utm_content, my_utm_extra));   
				}
			} else {*/
				// if not editing
				// display tagged url details
				
	    	utm_long_url = "https://play.google.com/store/apps/details?id="+ packageName + 
					 "&referrer=utm_source=" + utm_source +
					 "&utm_medium=" + utm_medium + 
					 "&utm_Term=" + utm_term +
					 "&utm_Content=" + utm_content +
					 "&utm_Campaign=" + utm_campaign;
				/*utm_long_url = constructCompleteUrl(account,
						utm_campaign, utm_medium, utm_source, utm_url, utm_term, utm_content, utm_extra);*/

				sb.append(renderTaggedUrlDetailsRow(myRole, longAccountId, longCampaignId, longTaggedUrlId, 
						utm_url, utm_term, utm_content, utm_extra, utm_long_url, modifiedAt));
			//}
		}

		

		sb.append("</tbody>\r\n");
		sb.append("</table>\r\n");
		
		if (UserType.hasPermission(myRole, UserType.PERMISSION_MANAGE_CAMPAIGNS)) {
			Iterable<Entity> URLS = getAllTaggedUrlsFor(longAccountId, longCampaignId);
			Iterator<Entity> n = URLS.iterator();
			
			if (n.hasNext()){
			sb.append("<div class='form-group'>"
					+ "<input id='cids' type='hidden' value=''/>"
					+ "<input type='submit' value='Delete Selected' class='btn red btn-outline'></div></form>");
					//+ "<a href=\"javascript:confirmCampaignDelete('/editcampaign?aid=" + longAccountId  +"&act=" + Messages.ACTION_DELETEAll + "');\">Delete Selected</a></div></form>");
			}
			}


	    return sb.toString();
	}
	//

	public static String renderTaggedUrlFormRow(long longAccountId, long longCampaignId, Entity taggedUrl, long longTaggedUrlId) {
	    StringBuilder sb = new StringBuilder();

	    String packageName = (String)taggedUrl.getProperty("packageName");
	    //String utm_url = (String)taggedUrl.getProperty("utm_url");
	    String utm_term = (String)taggedUrl.getProperty("utm_term");
	    String utm_content = (String)taggedUrl.getProperty("utm_content");
	    //String utm_extra = (String)taggedUrl.getProperty("utm_extra");	
	    
		String buttonLabel = "";
		//if new tagged URL
		if (0==longTaggedUrlId) {
			buttonLabel = " Tag URL ";
		} else {
			buttonLabel = " Save ";
		}
		
		sb.append("<table class=\"table table bordered\">\r\n");
	    sb.append("<thead><tr>\r\n");
	    //sb.append("\t<th>Id</th>\r\n");
	    //sb.append("\t<th>URL*</th>\r\n");
	    //sb.append("\t<th>Shortned</th>\r\n");
	    sb.append("\t<th>Term</th>\r\n");
	    sb.append("\t<th>Content</th>\r\n");
	    //sb.append("\t<th>Extra Parameter</th>\r\n");
	    sb.append("\t<th class=\"no-sort\">Action</th>\r\n");
	    sb.append("</tr>\r\n");
	    sb.append("</thead>\r\n");
	    sb.append("<tbody>\r\n");
		
    	sb.append("<form action=\"/editmobiletaggedurl\" method=\"post\" name=\"mobiletaggedUrlForm\" onsubmit=\"return validateTaggedUrlForm()\"><tr class=\"urlForm\">");
	    //sb.append("<td>&nbsp;</td>\r\n");
	    //sb.append("<td class='form'><input name=\"utm_url\" type=\"text\" value=\"" + utm_url + "\" height=\"4\"/></td>\r\n");
	    sb.append("<td><input class='form-control' name=\"utm_term\" type=\"text\" value=\"" + utm_term + "\" /></td>\r\n");
	    sb.append("<td><input class='form-control' name=\"utm_content\" type=\"text\" value=\"" + utm_content + "\" /></td>\r\n");
	    //sb.append("<td><input name=\"utm_extra\" type=\"text\" value=\"" + utm_extra + "\" /></td>\r\n");
	    sb.append("<td><input class='btn green' name=\"save\" type=\"submit\" value=\"" + buttonLabel + "\" /></td>\r\n");
	    sb.append("</tr>\r\n");
	    sb.append("<input type=\"hidden\" name=\"aid\" value=\"" + longAccountId +"\"/>");
	    sb.append("<input type=\"hidden\" name=\"cid\" value=\"" + longCampaignId +"\"/>");
	    sb.append("<input type=\"hidden\" name=\"tid\" value=\"" + longTaggedUrlId + "\"/>");
	    sb.append("<input type=\"hidden\" name=\"packageName\" value=\"" + packageName + "\"/>");
	    sb.append("</form>");
	    
	    sb.append("</tbody>\r\n");
		sb.append("</table>\r\n");

	    return sb.toString();
	}

	
	public static String renderNewTaggedUrlFormRow(long longAccountId, long longCampaignId) {
	    StringBuilder sb = new StringBuilder();
		
	    //Entity taggedUrl = getSingleTaggedUrl(longAccountId, longCampaignId,longTaggedUrlId);
	    	    
	    String buttonLabel = "Tag URL";
		
		 sb.append("<table class=\"table table-bordered\">\r\n");
		    sb.append("<thead><tr>\r\n");
		    //sb.append("\t<th>Id</th>\r\n");
		    //sb.append("\t<th>URL*</th>\r\n");
		    //sb.append("\t<th>Shortned</th>\r\n");
		    sb.append("\t<th>Term</th>\r\n");
		    sb.append("\t<th>Content</th>\r\n");
		    //sb.append("\t<th>Extra Parameter</th>\r\n");
		    sb.append("\t<th class=\"np-sort\">Action</th>\r\n");
		    sb.append("</tr>\r\n");
		    sb.append("</thead>\r\n");
		    sb.append("<tbody>\r\n");
		
    	sb.append("<form action=\"/editmobiletaggedurl\" method=\"post\" name=\"taggedUrlForm\" onsubmit=\"return validateTaggedUrlForm()\"><tr class=\"urlForm\">");
	    //sb.append("<td>&nbsp;</td>\r\n");
	    //sb.append("<td><input style='width:97%' name=\"utm_url\" type=\"text\" value='' height=\"4\"/></td>\r\n");
	    sb.append("<td><input class='form-control' name=\"utm_term\" type=\"text\" value='' /></td>\r\n");
	    sb.append("<td><input class='form-control' name=\"utm_content\" type=\"text\" value='' /></td>\r\n");
	    //sb.append("<td><input style='width:97%' name=\"utm_extra\" type=\"text\" value='' /></td>\r\n");
	    sb.append("<td><input class='btn green' name=\"save\" type=\"submit\" value=\"" + buttonLabel + "\" /></td>\r\n");
	    sb.append("</tr>\r\n");
	    sb.append("<input type=\"hidden\" name=\"aid\" value=\"" + longAccountId +"\"/>");
	    sb.append("<input type=\"hidden\" name=\"cid\" value=\"" + longCampaignId +"\"/>");
	    //sb.append("<input type=\"hidden\" name=\"tid\" value=\"" + longTaggedUrlId + "\"/>");
	    sb.append("</form>");
	    
	    
	    sb.append("</tbody>\r\n");
		sb.append("</table>\r\n");
	    return sb.toString();
	}

	/*private static String renderTaggedUrlDetailsRow(String myRole, long longAccountId, long longCampaignId, long longTaggedUrlId, 
		    String utm_url, String utm_term, String utm_content, String utm_extra, String utm_long_url) {
	    StringBuilder sb = new StringBuilder();

		sb.append("<tr>\r\n");
		//sb.append("<td>" + longTaggedUrlId + "</td>\r\n");
		//sb.append("<td>" + utm_url + "</td>\r\n");
		sb.append("<td>" + utm_term + "&nbsp;</td>\r\n");
		sb.append("<td>" + utm_content + "&nbsp;</td>\r\n");
		//sb.append("<td>" + utm_extra + "&nbsp;</td>\r\n");
		sb.append("<td>");
		if (UserType.hasPermission(myRole, UserType.PERMISSION_MANAGE_TAGGED_URLS) ){
			sb.append("<a href=\"/MCTaggedURLs.jsp?aid=" + longAccountId  + "&cid=" + longCampaignId  + "&tid=" + longTaggedUrlId + "\">Edit</a>");
			sb.append("&nbsp;|&nbsp;");
			sb.append("<a href=\"javascript:confirmTaggedUrlDelete('/editmobiletaggedurl?aid=" + longAccountId  + "&cid=" + longCampaignId  + "&tid=" + longTaggedUrlId + "&act=" + Messages.ACTION_DELETE + "')\">Delete</a>");
			sb.append("&nbsp;|&nbsp;");
		}
		sb.append("<a target=\"_blank\" href=\"" + utm_long_url + "\">Verify URL</a>");
		sb.append("&nbsp;|&nbsp;");
		sb.append("<a href=\"UrlShortener.jsp?url=" + HtmlUtil.encode(utm_long_url) + "\" onclick=\"window.open(this.href, '_blank', 'toolbar=no,location=yes,directories=yes,resizable=yes,scrollbars=yes,width=800,height=300'); return false;\">Share</a>");
		sb.append("</td></tr>\r\n");
		sb.append("</tr>\r\n");

	    return sb.toString();
	}*/

	// new 23/9
	
	public static String renderTaggedUrlDetailsRow(String myRole, long longAccountId, long longCampaignId, long longTaggedUrlId, 
		    String utm_url, String utm_term, String utm_content, String utm_extra, String utm_long_url, Date modifiedAt) {
	    
		 StringBuilder sb = new StringBuilder();
		 String shortUrl = GShortenerService.shorten(utm_long_url);
		 
		 sb.append("<tr>\r\n");
			sb.append("<td style='display:none;'>" + longTaggedUrlId + "</td>\r\n");
			sb.append("<td style='min-width:49px !important;width:49px !important;'><input type=\"checkbox\" name=\"tid\" class='checkboxes' value=\"" + longTaggedUrlId + "\" /></td>\r\n");
			sb.append("<td><a id="+longTaggedUrlId+" href='"+ utm_long_url +"' target='_blank' title='"+ utm_long_url+"'>" + utm_long_url + "</a><p></p>");
			sb.append("<a style='display:none;' id="+longTaggedUrlId+2+" href='"+ utm_long_url +"' target='_blank' title='"+ utm_long_url+"'>" + utm_long_url + "</a>");
		 
		//*sb.append("<tr>\r\n");
		//sb.append("<td>" + longTaggedUrlId + "</td>\r\n");
		//*sb.append("<td><a id="+longTaggedUrlId+" href='"+ utm_long_url +"' target='_blank' title='"+ utm_long_url+"'>" + utm_long_url + "</a><p></p>");
    			//+ "<button class=\"copyURL btn green\" onclick='copyToClipboard(\"#"+longTaggedUrlId+"\")'>Copy</button>");
    			
    			/*+ "<div class=\"sharethis mws-button red\"><div style='display:none;'><p class='shareButtons'>"
				
    			+ "<a href=\"http://www.facebook.com/sharer.php?u=" + HtmlUtil.encode(utm_long_url) + "&t=" + HtmlUtil.encode("") 
    			
    			+"\" onclick=\"window.open(this.href, 'sharer', 'toolbar=0,status=0,width=740,height=320'); return false;\"><img src=\"/images/shareFacebook.png\"></a>&nbsp;\r\n"
				
    			+ "\t<a target=\"_blank\" name=\"twt_share\" href=\"https://twitter.com/share?url=" + HtmlUtil.encode(utm_long_url) 
    			
    			+"\" onclick=\"window.open(this.href, 'sharer', 'toolbar=0,status=0,width=740,height=320'); return false;\"><img src=\"/images/shareTwitter.png\"></a>&nbsp;\r\n"
    			
    			+ "\t<a target=\"_blank\" name=\"google_share\" href=\"https://plus.google.com/u/1/share?url=" + HtmlUtil.encode(utm_long_url) + "&source=campaignalyzer.com" 
    			
    			+"\" onclick=\"window.open(this.href, 'sharer', 'toolbar=0,status=0,width=740,height=320'); return false;\"><img src=\"/images/shareGoogle.png\"></a>&nbsp;\r\n"
    			
    			+ "\t<a target=\"_blank\" name=\"linkedin_share\" href=\"http://www.linkedin.com/shareArticle?url=" + HtmlUtil.encode(utm_long_url) + "&source=campaignalyzer.com" 
    			
    			+"\" onclick=\"window.open(this.href, 'sharer', 'toolbar=0,status=0,width=740,height=320'); return false;\"><img src=\"/images/shareLinkedin.png\"></a>&nbsp;\r\n</div></div>");*/
    			
		// old design changed at 12-1-2016
		/*if (UserType.hasPermission(myRole, UserType.PERMISSION_MANAGE_TAGGED_URLS) ){
    				sb.append("<a class='btn default' href=\"/ManageTaggedURLs.jsp?aid=" + longAccountId  + "&cid=" + longCampaignId  + "&tid=" + longTaggedUrlId +"&cname=" + cName + "\">Edit</a>");
    				//sb.append("&nbsp;|&nbsp;");
    				sb.append("<a class='btn red' href=\"javascript:confirmTaggedUrlDelete('/edittaggedurl?aid=" + longAccountId  + "&cid=" + longCampaignId  + "&tid=" + longTaggedUrlId + "&act=" + Messages.ACTION_DELETE + "')\">Delete</a>");
    				//sb.append("&nbsp;|&nbsp;");
    			}*/
    	
		sb.append("</td>\r\n");
		sb.append("<td style='display:none;'><a id='"+longTaggedUrlId+1+"' href='"+ shortUrl +"' target='_blank'>" + shortUrl + "</a><p></P>");
		
		// old design changed at 12-1-2016
		/*+ "<button class=\"copyURL btn green\" onclick='copyToClipboard(\"#"+longTaggedUrlId+1+"\")'>Copy</button>"
		+ "<div class=\"sharethis btn blue\"><div style='display:none; float:left'><p class='shareButtons'>"
		
    			+ "<a href=\"http://www.facebook.com/sharer.php?u=" + HtmlUtil.encode(shortUrl) + "&t=" + HtmlUtil.encode("") 
    			
    			+"\" onclick=\"window.open(this.href, 'sharer', 'toolbar=0,status=0,width=740,height=320'); return false;\"><img src=\"/images/shareFacebook.png\"></a>&nbsp;\r\n"
				
    			+ "\t<a target=\"_blank\" name=\"twt_share\" href=\"https://twitter.com/share?url=" + HtmlUtil.encode(shortUrl) 
    			
    			+"\" onclick=\"window.open(this.href, 'sharer', 'toolbar=0,status=0,width=740,height=320'); return false;\"><img src=\"/images/shareTwitter.png\"></a>&nbsp;\r\n"
    			
    			+ "\t<a target=\"_blank\" name=\"google_share\" href=\"https://plus.google.com/u/1/share?url=" + HtmlUtil.encode(shortUrl) + "&source=campaignalyzer.com" 
    			
    			+"\" onclick=\"window.open(this.href, 'sharer', 'toolbar=0,status=0,width=740,height=320'); return false;\"><img src=\"/images/shareGoogle.png\"></a>&nbsp;\r\n"
    			
    			+ "\t<a target=\"_blank\" name=\"linkedin_share\" href=\"http://www.linkedin.com/shareArticle?url=" + HtmlUtil.encode(shortUrl) + "&source=campaignalyzer.com" 
    			
    			+"\" onclick=\"window.open(this.href, 'sharer', 'toolbar=0,status=0,width=740,height=320'); return false;\"><img src=\"/images/shareLinkedin.png\"></a>&nbsp;\r\n</div>Share</div></td>\r\n");*/
		
		sb.append("<td>" + utm_term + "&nbsp;</td>\r\n");
		sb.append("<td>" + utm_content + "&nbsp;</td>\r\n");
		//sb.append("<td>" + utm_extra + "&nbsp;</td>\r\n");		
		
		if (null!=modifiedAt)
			sb.append("<td>" + DateFormat.getDateInstance(DateFormat.MEDIUM).format(modifiedAt) + "</td>\r\n");
		else
			sb.append("<td>&nbsp;</td>\r\n");
			sb.append("<td>");
		sb.append("<a class=\"copyURL btn green btn-outline\" onclick='copyToClipboard(\"#"+longTaggedUrlId+2+"\")'>Full</a>");
		sb.append("<a class=\"copyURL btn blue btn-outline\" onclick='copyToClipboard(\"#"+longTaggedUrlId+1+"\")'>Shortened</a>");
		sb.append("</td>");
		
		//sb.append("<td>&nbsp;</td>\r\n");
		//sb.append("<td>" + modifiedAt + "&nbsp;</td>\r\n");
		
		//sb.append("<td>");
		
		//sb.append("<a target=\"_blank\" href=\"" + utm_long_url + "\">Verify URL</a>");
		//sb.append("&nbsp;|&nbsp;");
		//sb.append("<a href=\"UrlShortener.jsp?url=" + HtmlUtil.encode(utm_long_url) + "\" onclick=\"window.open(this.href, '_blank', 'toolbar=no,location=yes,directories=yes,resizable=yes,scrollbars=yes,width=800,height=300'); return false;\">Share</a>");
		//sb.append("</td></tr>\r\n");
		sb.append("<td>");
		/*sb.append("<button class=\"copyURL btn green btn-outline\" onclick='copyToClipboard(\"#"+longTaggedUrlId+1+"\")'>Copy</button>");
		sb.append("<div class=\"sharethis btn blue btn-outline\"><div style='display:none; float:left'><p class='shareButtons'>"
				
    			+ "<a href=\"http://www.facebook.com/sharer.php?u=" + HtmlUtil.encode(shortUrl) + "&t=" + HtmlUtil.encode("") 
    			
    			+"\" onclick=\"window.open(this.href, 'sharer', 'toolbar=0,status=0,width=740,height=320'); return false;\"><img src=\"/images/shareFacebook.png\"></a>&nbsp;\r\n"
				
    			+ "\t<a target=\"_blank\" name=\"twt_share\" href=\"https://twitter.com/share?url=" + HtmlUtil.encode(shortUrl) 
    			
    			+"\" onclick=\"window.open(this.href, 'sharer', 'toolbar=0,status=0,width=740,height=320'); return false;\"><img src=\"/images/shareTwitter.png\"></a>&nbsp;\r\n"
    			
    			+ "\t<a target=\"_blank\" name=\"google_share\" href=\"https://plus.google.com/u/1/share?url=" + HtmlUtil.encode(shortUrl) + "&source=campaignalyzer.com" 
    			
    			+"\" onclick=\"window.open(this.href, 'sharer', 'toolbar=0,status=0,width=740,height=320'); return false;\"><img src=\"/images/shareGoogle.png\"></a>&nbsp;\r\n"
    			
    			+ "\t<a target=\"_blank\" name=\"linkedin_share\" href=\"http://www.linkedin.com/shareArticle?url=" + HtmlUtil.encode(shortUrl) + "&source=campaignalyzer.com" 
    			
    			+"\" onclick=\"window.open(this.href, 'sharer', 'toolbar=0,status=0,width=740,height=320'); return false;\"><img src=\"/images/shareLinkedin.png\"></a>&nbsp;\r\n</div>Share</div>\r\n");*/
		
		if (UserType.hasPermission(myRole, UserType.PERMISSION_MANAGE_TAGGED_URLS) ){
			sb.append("<a class='btn purple btn-outline' href=\"/MCTaggedURLs.jsp?aid=" + longAccountId  + "&cid=" + longCampaignId  + "&tid=" + longTaggedUrlId + "\">Edit</a>");
			//sb.append("&nbsp;|&nbsp;");
			sb.append("<a class='btn red btn-outline' href=\"javascript:confirmTaggedUrlDelete('/editmobiletaggedurl?aid=" + longAccountId  + "&cid=" + longCampaignId  + "&tid=" + longTaggedUrlId + "&act=" + Messages.ACTION_DELETE + "')\">Delete</a>");
			//sb.append("&nbsp;|&nbsp;");
			
			
		}
		
		sb.append("</td></tr>\r\n");

	    return sb.toString();
	}
	
	// 
	
	
	public static String renderTaggedUrlExportList(Entity account, String[] sCampaignIds, String delimiter) {
		StringBuilder sb = new StringBuilder();
		long longAccountId = account.getKey().getId();

		if (Porting.DEL_TSV.equalsIgnoreCase(delimiter))
			delimiter = "\t";
		else if (Porting.DEL_CSVEXCEL.equalsIgnoreCase(delimiter))
			delimiter = ",";
		else if (Porting.DEL_COMMA.equalsIgnoreCase(delimiter))
			delimiter = ",";
		else if (Porting.DEL_SEMICOLON.equalsIgnoreCase(delimiter))
			delimiter = ";";

		//String queryParam = (String) account.getProperty("queryParam");
		//if (null==queryParam) queryParam = "";

		// header row
		sb.append("Campaign" + delimiter + "Medium" + delimiter + "Source" + delimiter + 
				"Term" + delimiter + "Content" + delimiter + "Extra Parameter" + delimiter +
				"URL" + delimiter + "Tagged URL\r\n");

		// retrieve parameters of the selected campaign
		long longCampaignId = 0;
		Entity mobilecampaign = null;
		String utm_campaign = "";
		String utm_medium = "";
		String utm_source = "";
		
		if (null!=sCampaignIds) {
			for (int i=0; i<sCampaignIds.length; i++) {
				longCampaignId = Long.valueOf(sCampaignIds[i]);
				if (0!=longCampaignId) {
					mobilecampaign = MobileCampaignService.getSingleMobileCampaign(longAccountId, longCampaignId);
		
					if (null!=mobilecampaign) {
						longCampaignId = mobilecampaign.getKey().getId();
						utm_campaign = (String) mobilecampaign.getProperty("utm_campaign");
						utm_medium = (String) mobilecampaign.getProperty("utm_medium");
						utm_source = (String) mobilecampaign.getProperty("utm_source");
			
						// retrieve and render the Tagged URLs of the selected Campaign.
					    Iterable<Entity> taggedUrls = PMUtil.listChildren("MobileTaggedUrl", mobilecampaign.getKey());
		
						//long longTaggedUrlId = 0;
						String utm_url = "";
						String utm_term = "";
						String utm_content = "";
						String utm_extra = "";
			
						// loop thru tagged urls
						for (Entity mobitaggedUrl : taggedUrls) {
							// retrieve tagged url parameters
							//longTaggedUrlId = taggedUrl.getKey().getId();
			
					    	utm_url = (String) mobitaggedUrl.getProperty("utm_url");
					    	if (null==utm_url) utm_url = "";
					    	utm_term = (String) mobitaggedUrl.getProperty("utm_term");
					    	if (null==utm_term) utm_term = "";
					    	utm_content = (String) mobitaggedUrl.getProperty("utm_content");
					    	if (null==utm_content) utm_content = "";
					    	utm_extra = (String) mobitaggedUrl.getProperty("utm_extra");
					    	if (null==utm_extra) utm_extra="";
			
							sb.append(renderTaggedUrlExportRow(account, delimiter,
									utm_campaign, utm_medium, utm_source, utm_url, utm_term, utm_content, utm_extra));
			
						}
					}
				}
			}
		}

		return sb.toString();
	}

	private static String renderTaggedUrlExportRow(Entity myAccount, String delimiter,
			String utm_campaign, String utm_medium, String utm_source,
			String utm_url, String utm_term, String utm_content, String utm_extra) {

		// construct each row
		StringBuilder row = new StringBuilder();

		row.append(utm_campaign + delimiter + utm_medium + delimiter + utm_source + delimiter + 
					utm_term + delimiter + utm_content + delimiter + utm_extra + delimiter + utm_url);

		// and add complete URL
		row.append(delimiter + constructCompleteUrl(myAccount, utm_campaign, utm_medium, utm_source, utm_url, utm_term, utm_content, utm_extra));

		return row.toString() + "\r\n";
	}

	private static String constructCompleteUrl(Entity myAccount,
			String utm_campaign, String utm_medium, String utm_source,
			String utm_url, String utm_term, String utm_content, String utm_extra) {

		// retrieve Tagging Settings
		String conAttribution = (String) myAccount.getProperty("conAttribution");
		String parSeparator = (String) myAccount.getProperty("parSeparator");
		String caseSensetive = (String) myAccount.getProperty("caseSensetive");

		// retrieve Tagging Parameters
		String campaignParam = (String) myAccount.getProperty("campaignParam");
		if (null==campaignParam) campaignParam = TaggingParameters.DEFAULT_CAMPAIGN_PARAMETER;

		String mediumParam = (String) myAccount.getProperty("mediumParam");
		if (null==mediumParam) mediumParam = TaggingParameters.DEFAULT_MEDIUM_PARAMETER;

		String sourceParam = (String) myAccount.getProperty("sourceParam");
		if (null==sourceParam) sourceParam = TaggingParameters.DEFAULT_SOURCE_PARAMETER;

		String termParam = (String) myAccount.getProperty("termParam");
		if (null==termParam) termParam = TaggingParameters.DEFAULT_TERM_PARAMETER;

		String contentParam = (String) myAccount.getProperty("contentParam");
		if (null==contentParam) contentParam = TaggingParameters.DEFAULT_CONTENT_PARAMETER;

		String queryParam = (String) myAccount.getProperty("queryParam");
		if (null==queryParam) queryParam = TaggingParameters.DEFAULT_QUERY_PARAMETER;

		//if lower case
		if (TaggingSettings.NONDEFAULT_PARAMETER_CASE.equalsIgnoreCase(caseSensetive)) {
			utm_campaign = utm_campaign.toLowerCase();
			utm_medium = utm_medium.toLowerCase();
			utm_source = utm_source.toLowerCase();
			utm_term = utm_term.toLowerCase();
			utm_content = utm_content.toLowerCase();
			utm_extra = utm_extra.toLowerCase();
		}
		
		// construct complete URL
		// and escape special characters
		StringBuilder completeUrl = new StringBuilder();
		completeUrl = new StringBuilder(utm_url);

		if (!TaggingSettings.NONDEFAULT_PARAMETER_SEPARATOR.equalsIgnoreCase(parSeparator)) {
			if (utm_url.contains("?"))
				completeUrl.append("&");
			else
				completeUrl.append("?");
		} else {
			completeUrl.append("#");
		}

		try {
			completeUrl.append(campaignParam + "=" +  URLEncoder.encode(utm_campaign,"UTF-8"));
			completeUrl.append("&"+ mediumParam + "=" +  URLEncoder.encode(utm_medium,"UTF-8"));
			completeUrl.append("&" + sourceParam + "=" + URLEncoder.encode(utm_source,"UTF-8"));
			if (!utm_term.equals(""))
				completeUrl.append("&" + termParam + "=" +  URLEncoder.encode(utm_term,"UTF-8"));
			if (!utm_content.equals(""))
				completeUrl.append("&" + contentParam + "="+  URLEncoder.encode(utm_content,"UTF-8"));
			if (!"".equals(utm_extra) && !"".equals(queryParam))
				completeUrl.append("&" + queryParam + "="+  URLEncoder.encode(utm_extra,"UTF-8"));
	
			if (TaggingSettings.NONDEFAULT_CONVERSION_ATTRIBUTION.equalsIgnoreCase(conAttribution))
				completeUrl.append("&utm_nooverride=1");
		} catch(UnsupportedEncodingException ex) {
			// todo
			// do nothing
		}

		return completeUrl.toString();
	}

	public static boolean isValidTaggedUrlFields(String utm_url, String utm_term, String utm_content, String utm_extra) {
		if (null==utm_url || null==utm_term || null==utm_content || null==utm_extra)
			return false;
		else if ("".equals(utm_url))
			return false;
    	else if (!HtmlUtil.isMatchingRegex(utm_url, HtmlUtil.URL_PATTERN))
    		return false;
    	else
    		return true;
	}

}
