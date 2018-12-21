package me.heuristic.services;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import me.heuristic.util.PMUtil;
import me.heuristic.references.Messages;
import me.heuristic.references.Porting;
import me.heuristic.references.UserType;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
//import com.google.appengine.api.datastore.Query.FilterOperator;
/*import com.google.appengine.api.users.UserServiceFactory;*/
import com.google.appengine.api.datastore.QueryResultList;
import com.google.appengine.api.datastore.Query.FilterOperator;

public class MobileCampaignHolderService {
	public static void deleteCampaignHolder(String campaignName, long longAccountId) {

    	Iterable<Entity> mobileCampaigns = MobileCampaignService.getAllMobileCampaignsFor(longAccountId);    	
    	for (Entity campaign : mobileCampaigns) {	
    		if (campaign.getProperty("utm_campaign").toString().contentEquals(campaignName)){
    			
    			Key campaignKey = campaign.getKey(); 

    		    try {
    				//CASCADE_ON_DELETE
    				Iterable<Entity> taggedUrls = PMUtil.listChildKeys("MobileTaggedUrl", campaignKey);
    				final List<Key> taggedUrlKeys = new ArrayList<Key>();
    				for (Entity taggedUrl : taggedUrls) {
    					  taggedUrlKeys.add(taggedUrl.getKey());
    				}
    				PMUtil.deleteEntity(taggedUrlKeys);
    				PMUtil.deleteEntity(campaignKey);
    		    } catch (Exception ex) {
    		    	// TODO
    		    	// String msg = Util.getErrorResponse(e);
    		    }
    	}	
    	}	
		
	}
	
	public static void deleteSelectedMobileCampaignHolder(String[] campaignName, long longAccountId) {

    	Iterable<Entity> mobileCampaigns = MobileCampaignService.getAllMobileCampaignsFor(longAccountId);
    	for(int i=0; i < campaignName.length; i++ ){
    	for (Entity campaign : mobileCampaigns) {	
    		if (campaign.getProperty("utm_campaign").toString().contains(campaignName[i])){
    			
    			Key campaignKey = campaign.getKey(); 

    		    try {
    				//CASCADE_ON_DELETE
    				Iterable<Entity> taggedUrls = PMUtil.listChildKeys("MobileTaggedUrl", campaignKey);
    				final List<Key> taggedUrlKeys = new ArrayList<Key>();
    				for (Entity taggedUrl : taggedUrls) {
    					  taggedUrlKeys.add(taggedUrl.getKey());
    				}
    				PMUtil.deleteEntity(taggedUrlKeys);
    				PMUtil.deleteEntity(campaignKey);
    		    } catch (Exception ex) {
    		    	// TODO
    		    	// String msg = Util.getErrorResponse(e);
    		    }
    	}	
    	}	
    	}
	}
	
	public static Entity createOrUpdateMobileCampaignHolder(String myEmail, long longAccountId, long longCampaignId,
			String utm_campaign, String utm_medium, String utm_source, String utm_notes) {

		// creating key and campaign entity
        Date date = new Date();
        Entity mobileCampaign;
		Key key = KeyFactory.createKey("Account", longAccountId);
		if (0==longCampaignId) {
			// new account
			mobileCampaign = new Entity("mobileCampaign", key);
			mobileCampaign.setProperty("createdBy", myEmail);
			mobileCampaign.setProperty("createdAt", date);
	        
    	} else {
			// editing existing campaign
    		// retrieve it from datastore
    		mobileCampaign = getSingleCampaignHolder(longAccountId, longCampaignId);
    	}

		// setting properties
		mobileCampaign.setProperty("utm_campaign", utm_campaign);
		mobileCampaign.setProperty("utm_medium", utm_medium);
		mobileCampaign.setProperty("utm_source", utm_source);
    	mobileCampaign.setProperty("utm_notes", utm_notes);
    	mobileCampaign.setProperty("modifiedBy", myEmail);
    	mobileCampaign.setProperty("modifiedAt", date);

        // saving entity
        PMUtil.persistEntity(mobileCampaign);
        return mobileCampaign;
	}
	
	public static void UpdateMobileCampaignHolder(String myEmail, long longAccountId, String old_utm_campaign ,String utm_campaign) {

		// creating key and campaign entity
        Date date = new Date();
       // Entity campaign;
		//Key key = KeyFactory.createKey("Account", longAccountId);
		
			// editing existing campaign
    		// retrieve it from datastore
		Iterable<Entity> mobileCampaignHolders = getAllMobileCampaignHoldersFor(longAccountId);
		
			for(Entity campaignHolder : mobileCampaignHolders){
				String ut_medium = campaignHolder.getProperty("utm_medium").toString();
				String utm_source = campaignHolder.getProperty("utm_source").toString();
				String utm_notes = campaignHolder.getProperty("utm_notes").toString();
				if(campaignHolder.getProperty("utm_campaign").toString().contentEquals(old_utm_campaign)){
					// setting properties
					campaignHolder.setProperty("utm_campaign", utm_campaign);
					campaignHolder.setProperty("utm_medium",ut_medium );
					campaignHolder.setProperty("utm_source", utm_source);
					campaignHolder.setProperty("utm_notes",utm_notes );
					campaignHolder.setProperty("modifiedBy", myEmail);
					campaignHolder.setProperty("modifiedAt", date);

			        // saving entity
			        PMUtil.persistEntity(campaignHolder);
			        
				}
				
			}
    	
			//return campaignHolder;
		
	}
	
	public static void upMobileCampaignHolder(String campaignName, String utm_campaign,long longAccountId) {

    	Iterable<Entity> mobileCampaigns = MobileCampaignService.getAllMobileCampaignsFor(longAccountId);
    	for (Entity campaign : mobileCampaigns) {	
    		Key campaignKey = campaign.getKey(); 

    		if (campaign.getProperty("utm_campaign").toString().contentEquals(campaignName)){
    			

    			campaign.setProperty("utm_campaign", utm_campaign);
    	}	
    	}	
		
	}

	@SuppressWarnings("unused")
	public static int getAllMobileCampaignHoldersCountFor(long longAccountId) {
	    Key ancestorKey = KeyFactory.createKey("Account", longAccountId);
	    Iterable<Entity> mobileCampaigns = PMUtil.listChildKeys("mobileCampaign", ancestorKey);
	   Iterable<Entity> mobileCampaignHolders = MobileCampaignHolderService.getAllMobileCampaignHoldersFor(longAccountId);
	   List<Long> sCampaignIds = new ArrayList<Long>();
	   List<String> sCampaignHolderIds = new ArrayList<String>();
    		
	    int i = 0;
		for (Entity campaignHolder: mobileCampaignHolders) {
			if (!sCampaignHolderIds.contains(campaignHolder.getProperty("utm_campaign").toString())){
				
				sCampaignHolderIds.add(campaignHolder.getProperty("utm_campaign").toString());
			i++;
		}
		}
	    return i;
	}

	
	public static int getAllMobileCampaignHoldersCountForEmail(String myEmail) {
		 int pageSize = 1000;
		 int i = 0;
		    FetchOptions fetchOptions = FetchOptions.Builder.withLimit(pageSize); 		    
		    Query q = new Query("mobileCampaign").addFilter("createdBy", FilterOperator.EQUAL, myEmail);
		    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
			PreparedQuery pq = datastore.prepare(q);
		    
		    QueryResultList<Entity> results = pq.asQueryResultList(fetchOptions);
		    for(Entity result : results){
		    	
		    	i++;
		    }  
		    
		    return i;
	}

	public static Iterable<Entity> getAllMobileCampaignHoldersFor(long longAccountId) {
	    Key ancestorKey = KeyFactory.createKey("Account", longAccountId);
	    return PMUtil.listChildren("mobileCampaign", ancestorKey);
	}

	public static Entity getSingleCampaignHolder(long longAccountId, long longCampaignId) {
		Key key = KeyFactory.createKey("Account", longAccountId);
		key = KeyFactory.createKey(key, "mobileCampaign", longCampaignId);
		return PMUtil.findEntity(key);
	}

	public static String renderMobileCampaignHolderForm(Entity account,	long longCampaignId, String utm_campaign ) {
	    StringBuilder sb = new StringBuilder();

	    long longAccountId = account.getKey().getId();

	    // default for new campaign
		String buttonText = "";

		//if new campaign
		if (0==longCampaignId) {
			buttonText = " Create Campaign ";
		} else {
			buttonText = " Save ";
		}

	    sb.append("<form class=\"form\" action=\"/editmobilecampaignholder\" method=\"post\" name=\"campaignForm\" onsubmit=\"return validateCampaignForm();\">\r\n");
    	sb.append("<div class=\"form-body\">\r\n");
    	sb.append("<div class=\"form-group\">\r\n");
    	sb.append("\t<label>Campaign*</label>\r\n");
    	sb.append("\t<div class=\"mws-form-item small\">\r\n");
		sb.append("\t<input type=\"text\" name=\"utm_campaign\" value=\"" + utm_campaign +"\" class=\"form-control\" />\r\n");
		sb.append("\t<input type=\"hidden\" name=\"cname\" value=\"" + utm_campaign +"\" class=\"form-control\" />\r\n");
    	sb.append("\t</div>\r\n");
    	sb.append("</div>\r\n");

    	// medium
    	/*sb.append("<div class=\"form-group\">\r\n");
    	sb.append("\t<label>Medium*</label>\r\n");
    	sb.append("\t<div class=\"mws-form-item small\">\r\n");
	    sb.append(renderChannelFormElement(account, utm_medium));
    	sb.append("\t</div>\r\n");
    	sb.append("</div>\r\n");*/

    	// source
    	/*sb.append("<div class=\"form-group\">\r\n");
    	sb.append("\t<label>Source*</label>\r\n");
    	sb.append("\t<div class=\"mws-form-item small\">\r\n");
		sb.append("\t\t<input type=\"text\" name=\"utm_source\" value=\"" + utm_source +"\" class=\"form-control\" />\r\n");
    	sb.append("\t</div>\r\n");
    	sb.append("</div>\r\n");*/

    	// notes
    	/*sb.append("<div class=\"form-group\">\r\n");
    	sb.append("\t<label>Notes</label>\r\n");
    	sb.append("\t<div class=\"mws-form-item small\">\r\n");
		sb.append("\t\t<textarea class='form-control' name=\"utm_notes\">" + utm_notes +"</textarea>\r\n");
    	sb.append("\t</div>\r\n");
    	sb.append("</div>\r\n");
*/
    	// submit
    	sb.append("<div class=\"mws-button-row\">\r\n");
		sb.append("\t<input type=\"submit\" value=\"" + buttonText + "\" class=\"btn sbold green\" />\r\n");
		//sb.append("<a href='/editcampaignholder?act=UpdateCHolder&aid="+longAccountId+"&cname="+utm_campaign+"' class=\"btn sbold green\">save</a>");
    	//sb.append("\t<button type='reset' class=\"btn default\" onclick='goBack()'>Cancel</button>\r\n");  
		// sb.append("<a class=\"btn sbold green_default\" href='ManageCampaigns.jsp?aid="+longAccountId+"&cname="+utm_campaign+"&cid="+longCampaignId+"'>Cancel</a>");
		 sb.append("<a  class=\"btn default\" href='ManageMobileCampaignHolders.jsp?aid="+longAccountId+"'>Cancel</a>");
		
		sb.append("</div>\r\n");

		sb.append("<input type=\"hidden\" name=\"aid\" value=\"" + longAccountId + "\" />\r\n");
	    sb.append("<input type=\"hidden\" name=\"cid\" value=\"" + longCampaignId + "\" />\r\n");
	    sb.append("<input type=\"hidden\" name=\"act\" value='UpdateCHolder'\" />\r\n");
	    
		//sb.append("</div></form>\r\n");

	    return sb.toString();
	}

	private static String renderChannelFormElement(Entity account, String utm_medium) {
	    StringBuilder sb = new StringBuilder();

	    @SuppressWarnings("unchecked")
		List<String> channels = (ArrayList<String>) account.getProperty("channels");

		sb.append("\t\t<select name=\"utm_medium\" class='form-control'><option value=''>Select...</option>\r\n");

		
		for (String channel:channels) {
			sb.append("\t\t\t<option value=\""+ channel + "\"");
			if (channel.equals(utm_medium)) {
				sb.append(" selected=\"selected\"");
			}
			sb.append(">" + channel + "</option>\r\n");	
		}
		
		sb.append("\t\t</select>\r\n");

	    return sb.toString();
	}

	public static String renderMobileCampaignHolderDetails(String myRole, Entity campaign) {
	    StringBuilder sb = new StringBuilder();

		long longAccountId = campaign.getParent().getId();
		long longCampaignId = campaign.getKey().getId();

		String utm_campaign = (String) campaign.getProperty("utm_campaign");
		String utm_medium = (String) campaign.getProperty("utm_medium");
		String utm_source = (String) campaign.getProperty("utm_source");
		//String utm_notes = (String) campaign.getProperty("utm_notes");

		sb.append("<div style='padding:5px 0 0 5px;' class=\"mws-panel-content\">\r\n");
		sb.append("<div style='min-height: 40px;height: 100%;width: 100%;display: block;' class=\"mws-button-row\">\r\n");
		sb.append("<div style='width: 75%;clear: none;display: inline-block; margin:auto 5px;'>");
		sb.append("<ul class='cdetails' style='width:88%'>"
		  + "<li><ul><li>Campaign</li><li style='background-color:#cfcfcf; text-align:center; font-weight:normal; font-size:14px;'>"+ utm_campaign +"</li></ul></li>"
		  + "<li><ul><li>Medium</li><li style='background-color:#cfcfcf; text-align:center; font-weight:normal; font-size:14px;'>" + utm_medium + "</li></ul></li>"
		  + "<li><ul><li>Source</li><li style='background-color:#cfcfcf; text-align:center; font-weight:normal; font-size:14px;'>" + utm_source + "</li></ul></li>"
		  + "</ul>");	
		

	    if (UserType.hasPermission(myRole, UserType.PERMISSION_MANAGE_CAMPAIGNS)) {
	    	sb.append("<form class=\"mws-form\" style='float:left; clear:none; height:105px; line-height: 84px;' action=\"/editcampaign\">\r\n");
	    	//sb.append("<div class=\"mws-form-inline\">\r\n");
			//sb.append("\t<div class=\"mws-button-row\">\r\n");
			sb.append("\t\t<input type=\"button\" value=\" Edit \" class=\"btn default\" onClick=\"location.href='/EditCampaign.jsp?aid=" + longAccountId + "&cid=" + longCampaignId + "'\"/>\r\n");
			sb.append("\t\t<input type=\"submit\" value=\" Delete \" class=\"btn red\" onClick=\"return confirmCampaignDeleteSubmit();\"/>\r\n");
			//sb.append("\t</div>\r\n");
	    	sb.append("<input type=\"hidden\" name=\"aid\" value=\"" + longAccountId +"\"/>\r\n");
	    	sb.append("<input type=\"hidden\" name=\"cid\" value=\"" + longCampaignId +"\"/>\r\n");
	    	sb.append("<input type=\"hidden\" name=\"act\" value=\"" + Messages.ACTION_DELETE +"\"/>\r\n");
			//sb.append("</div>\r\n");
			sb.append("</form>\r\n");
			sb.append("</div>\r\n");
			sb.append("</div>\r\n");
			sb.append("</div>\r\n");
		}

	    return sb.toString();
	}

	public static String renderMobileCampaignHolderList(String myRole, long longAccountId) {
	    StringBuilder sb = new StringBuilder();

		// retrieve the campaigns belonging to the selected Account.
		Iterable<Entity> mobileCampaignHolders = getAllMobileCampaignHoldersFor(longAccountId);
		
		if (UserType.hasPermission(myRole, UserType.PERMISSION_MANAGE_CAMPAIGNS)) {
			sb.append("<form class=\"mws-form\">\r\n");
	    	sb.append("<div class=\"mws-form-inline\">\r\n");
			sb.append("\t<div style='text-align:right;' class=\"form-group\">\r\n");
			sb.append("\t\t<input type=\"button\" value=\"+ New Mobile Campaign \" class=\"btn sbold green\" onClick=\"location.href='/EditMobileCampaign.jsp?aid=" + longAccountId + "'\"/>\r\n");
			sb.append("<input style='display:none;' type='button' value='+ Import Mobile Campaigns' class='btn sbold blue' onclick=\"location.href='/ImportMobileCampaigns.jsp?aid=" + longAccountId + "'\">");
			sb.append("\t</div>\r\n");
			sb.append("</div>\r\n");
			sb.append("</form>\r\n");
		}
		sb.append("<form id='delSelected'  method='post' action='/editmobilecampaignholder?aid="+longAccountId+"&act=" + Messages.ACTION_DELETEAll+"'>");
	//	=" + longAccountId  + "&act=" + Messages.ACTION_DELETEAll +"
		sb.append("<table class=\"sortable table table-striped table-bordered table-hover table-checkable order-column dataTable no-footer\">\r\n");
	    sb.append("<thead><tr>\r\n");
	    sb.append("<th class='no-sort chekboxTD' style='padding:8px 10px'><input type=\"checkbox\" id='deletAll'/></th>\r\n");
	    sb.append("<th>Campaign</th>\r\n");
	    //sb.append("<th>Medium</th>\r\n");
	    //sb.append("<th>Source</th>\r\n");
	    sb.append("<th>Creation Date</th>\r\n");
	    sb.append("<th class='no-sort action'>Action</th>\r\n");
	    sb.append("</tr>\r\n");
	    sb.append("</thead>\r\n");
	    sb.append("<tbody>\r\n");
	    
	    int i=0;
		long longCampaignId = 0;
		Date date = null;
		//String [] sCampaignIds;
		List<Long> sCampaignIds = new ArrayList<Long>();
		List<String> sCampaignHolderIds = new ArrayList<String>();
		
		for (Entity mobileCampaignHolder : mobileCampaignHolders) {		
				
			if (!sCampaignHolderIds.contains(mobileCampaignHolder.getProperty("utm_campaign").toString())){
				
				sCampaignHolderIds.add(mobileCampaignHolder.getProperty("utm_campaign").toString());
				
			
			//sCampaignIds.add(longCampaignId);
			longCampaignId = mobileCampaignHolder.getKey().getId();
			sCampaignIds.add(longCampaignId);
			date = (Date) mobileCampaignHolder.getProperty("createdAt");
			sb.append("<tr>\r\n");
			sb.append("<td class='chekboxTD'><input type=\"checkbox\" name=\"chid\" class='checkboxes' value=\"" + mobileCampaignHolder.getProperty("utm_campaign").toString() + "\" /></td>\r\n");
			sb.append("<td><a href=\"/ManageMobileCampaigns.jsp?aid=" + longAccountId + "&cname=" + mobileCampaignHolder.getProperty("utm_campaign") + "&cid=" + longCampaignId + "\">" + mobileCampaignHolder.getProperty("utm_campaign") + "</a></td>\r\n");
			//sb.append("<td>" + campaign.getProperty("utm_medium") + "</td>\r\n");
			//sb.append("<td>" + campaign.getProperty("utm_source") + "</td>\r\n");
			if (null!=date)
				sb.append("<td>" + DateFormat.getDateInstance(DateFormat.MEDIUM).format(date) + "</td>\r\n");
			else
				sb.append("<td>&nbsp;</td>\r\n");
			sb.append("<td class='action'>\r\n");
			sb.append("<a href=\"/ManageMobileCampaigns.jsp?aid=" + longAccountId + "&cname=" + mobileCampaignHolder.getProperty("utm_campaign") + "&cid=" + longCampaignId + "\">Details</a>");
			if (UserType.hasPermission(myRole, UserType.PERMISSION_MANAGE_CAMPAIGNS) || UserType.hasPermission("ADMIN", UserType.PERMISSION_MANAGE_CAMPAIGNS)) {
				sb.append("&nbsp;|&nbsp;");
				sb.append("<a href=\"/EditMobileCampaignHolder.jsp?aid=" + longAccountId + "&utm_campaign="+mobileCampaignHolder.getProperty("utm_campaign")+"&cname="+mobileCampaignHolder.getProperty("utm_campaign")+"&act=UpdateCHolder"+ "\">Edit</a>");
				sb.append("&nbsp;|&nbsp;");
				sb.append("<a id='copyCHolder' onclick=\"return confirm('Are you sure you want to copy campaign?')\" href=\"/editmobilecampaignholder?aid=" + longAccountId + "&utm_campaign=" + mobileCampaignHolder.getProperty("utm_campaign") + "&utm_medium=" + mobileCampaignHolder.getProperty("utm_medium") + "&utm_source=" + mobileCampaignHolder.getProperty("utm_source") +"&act=" + Messages.ACTION_COPY+ "\">Copy</a>");
				sb.append("&nbsp;|&nbsp;");
				sb.append("<a href=\"javascript:confirmCampaignDelete(\'/editmobilecampaignholder?aid=" + longAccountId  + "&utm_campaign=" + mobileCampaignHolder.getProperty("utm_campaign").toString().replace("'", "\\\'")+ "&act=" + Messages.ACTION_DELETE + "\');\">Delete</a>");
				//sb.append("<a href=\"javascript:confirmCampaignDelete(\'/editcampaign?utm_campaign="+campaignName.replace("'", "\\\'")+"&aid="+longAccountId+"&cid="+longCampaignId+"&act=DELETE\');\">Delete</a>");
			}
			sb.append("</td>\r\n");
			sb.append("</tr>\r\n");
			i++;
		}
	}
		if (i==0) {
			sb.append("<tr><td colspan=\"6\">This account has no campaigns yet.</td></tr>\r\n");
		}
		
		
		sb.append("</tbody>\r\n");
		sb.append("</table>\r\n");
		if (UserType.hasPermission(myRole, UserType.PERMISSION_MANAGE_CAMPAIGNS)) {
			if(0 != i){
				
				sb.append("<div class='form-group'>"
						+ "<input id='cids' type='hidden' value=''/>"
						+ "<input type='submit' value='Delete Selected' class='btn red btn-outline'  onclick=\"return confirm('Are you sure you want to delete?')\" ></div></form>");
				//+"<a class='btn red btn-outline' href=\"javascript:confirmCampaignDelete('/editcampaignholder?aid="+longAccountId+"&act=" + Messages.ACTION_DELETEAll+"');\">Delete</a></form>");
						//+ "<a href=\"javascript:confirmCampaignDelete('/editcampaign?aid=" + longAccountId  +"&act=" + Messages.ACTION_DELETEAll + "');\">Delete Selected</a></div></form>");
			}
		}

		if (UserType.hasPermission(myRole, UserType.PERMISSION_MANAGE_CAMPAIGNS)) {
			sb.append("<form class=\"mws-form\">\r\n");
	    	sb.append("<div class=\"mws-form-inline\">\r\n");
			sb.append("\t<div class=\"mws-button-row\">\r\n");
			//sb.append("\t\t<input type=\"button\" value=\" Create New Campaign \" class=\"mws-button green\" onClick=\"location.href='/EditCampaign.jsp?aid=" + longAccountId + "'\"/>\r\n");
			sb.append("\t</div>\r\n");
			sb.append("</div>\r\n");			
			sb.append("</form>\r\n");
			
			
			
			
		}

	    return sb.toString();
	}

	public static String renderMobileCampaignHolderExportForm(long longAccountId) {
	    StringBuilder sb = new StringBuilder();

		// retrieve the campaigns belonging to the selected Account.
		Iterable<Entity> mobileCampaigns = getAllMobileCampaignHoldersFor(longAccountId);

	    sb.append("<form class=\"mws-form\" action=\"/exportcampaigns\" method=\"post\">\r\n");
		sb.append("<table class=\"table sortable table table-striped table-bordered table-hover table-checkable order-column dataTable no-footer\">\r\n");
	    sb.append("<thead><tr>\r\n");
	    sb.append("<th class='no-sort chekboxTD' style='padding:8px 10px'><input type=\"checkbox\" id='exportAll'/></th>\r\n");
	    sb.append("<th>Campaign</th>\r\n");
	    sb.append("<th>Medium</th>\r\n");
	    sb.append("<th>Source</th>\r\n");
	    sb.append("<th>Creation Date</th>\r\n");
	    sb.append("</tr>\r\n");
	    sb.append("</thead>\r\n");
	    sb.append("<tbody>\r\n");

	    int i=0;
		long longCampaignId = 0;
		Date date = null;
		for (Entity mobileCampaign : mobileCampaigns) {
			longCampaignId = mobileCampaign.getKey().getId();
			date = (Date) mobileCampaign.getProperty("createdAt");
			sb.append("<tr>\r\n");
			sb.append("<td class='chekboxTD'><input type=\"checkbox\" name=\"cid\" class='checkboxes' value=\"" + longCampaignId + "\" /></td>\r\n");
			sb.append("<td>" + mobileCampaign.getProperty("utm_campaign") + "</td>\r\n");
			sb.append("<td>" + mobileCampaign.getProperty("utm_medium") + "</td>\r\n");
			sb.append("<td>" + mobileCampaign.getProperty("utm_source") + "</td>\r\n");
			if (null!=date)
				sb.append("<td>" + DateFormat.getDateInstance(DateFormat.MEDIUM).format(date) + "</td>\r\n");
			else
				sb.append("<td>&nbsp;</td>\r\n");
			sb.append("</tr>\r\n");
			i++;
		}
		if (i==0) {
			sb.append("<tr><td colspan=\"6\">This account has no campaigns yet.</td></tr>\r\n");
		}
		sb.append("</tbody></table>\r\n");

    	//sb.append("<div class=\"mws-form-inline\">\r\n");
		if (0!=i) {
	    /*	sb.append("<div class=\"form-group\">\r\n");
	    	sb.append("\t<label>Delimiter</label>\r\n");
	    	sb.append("\t<div class='radio-list'><lable>\r\n");
			sb.append("\t\t<input type=\"radio\" name=\"delimiter\" value=\"" + Porting.DEL_TSV + "\" checked>Tab</label>\r\n" +
					"\t\t<label><input type=\"radio\" name=\"delimiter\" value=\"" + Porting.DEL_SEMICOLON + "\"> Semicolon</label>\r\n" +
					"\t\t<label><input type=\"radio\" name=\"delimiter\" value=\"" + Porting.DEL_CSVEXCEL + "\"> CSV for Excel</label>\r\n");
	    	sb.append("\t</div>\r\n");
	    	sb.append("</div>\r\n");
	    	*/
	    	
	    	sb.append("<div class='form-group'>"
            +"<label class='font-green bold uppercase'>Delimiter:</label>"
            +"<div class='radio-list'>"
            +"<label class='radio-inline' style='padding-left:20px;'>"
            +"<div class='radio'><span><input type='radio' name='delimiter' value='" + Porting.DEL_TSV +"' checked=''></span></div> Tab </label>"
            +"<label class='radio-inline'>"
            +"<div class='radio'><span><input type='radio' name='delimiter' value='" + Porting.DEL_SEMICOLON +"'></span></div> Semicolon </label>"
            +"<label class='radio-inline'>"
            +"<div class='radio disabled'><span><input type='radio' name='delimiter' value='"+ Porting.DEL_CSVEXCEL +"'></span></div> CSV for Excel </label>"
            +"</div>"
            +"</div>");

		    sb.append("<div class=\"mws-button-row\">\r\n");
			sb.append("\t<input type=\"submit\" value=\" Export \" class=\"btn sbold green\" />\r\n");
	    	sb.append("</div>\r\n");

			sb.append("<input type=\"hidden\" name=\"aid\" value=\"" + longAccountId + "\" />\r\n");
		}

			//sb.append("</div>");
			sb.append("</form>\r\n");

	    return sb.toString();
	}

	public static String renderMobileCampaignHolderImportForm(long longAccountId) {
	    StringBuilder sb = new StringBuilder();

		sb.append("<form class=\"mws-form\" action=\"/importmobilecampaigns\" enctype=\"multipart/form-data\" method=\"post\">\r\n");
    	sb.append("<div class=\"mws-form-inline\">\r\n");
    	sb.append("<div class=\"mws-form-row\">\r\n");
    	sb.append("\t<label>File*</label>\r\n");
    	sb.append("\t<div class=\"form-group\">\r\n");
		//sb.append("\t\t<input class='form-control' type=\"file\" name=\"file\" />\r\n");
		
		sb.append("<div class='fileinput fileinput-new' data-provides='fileinput'>"
        +"<div class='input-group input-large'>"
        +"<div class='form-control uneditable-input input-fixed input-medium' data-trigger='fileinput'>"
        +"<i class='fa fa-file fileinput-exists'></i>&nbsp;"
        +"<span class='fileinput-filename'> </span>"
        +"</div>"
        +"<span class='input-group-addon btn default btn-file'>"
        +"<span class='fileinput-new'> Select file </span>"
        +"<span class='fileinput-exists'> Change </span>"
        +"<input type='file' name='file'> </span>"
        +"<a href='javascript:;' class='input-group-addon btn red fileinput-exists' data-dismiss='fileinput'> Remove </a>"
        +" </div>"
   +" </div>");
		
    	sb.append("\t</div>\r\n");
    	sb.append("</div>\r\n");

		sb.append("<div class=\"form-group\">\r\n");
		sb.append("\t<input type=\"submit\" value=\" Import \" class=\"btn sbold green\" />\r\n");
		sb.append("</div>\r\n");

    	sb.append("<input type=\"hidden\" name=\"aid\" value=\"" + longAccountId +"\"/>\r\n");
		sb.append("</div></form>\r\n");

	    return sb.toString();
	}

	public static boolean isValidCampaignFields(Entity myAccount, String utm_campaign, String utm_medium, String utm_source) {
		if (null==utm_campaign || null==utm_medium || null==utm_source)
			return false;
		if ("".equals(utm_campaign) || "".equals(utm_medium) || "".equals(utm_source))
			return false;

		@SuppressWarnings("unchecked")
		List<String> channels = (List<String>) myAccount.getProperty("channels");
		int i = channels.indexOf(utm_medium);
		// if not found
		if (i<0)
			return false;
		return true;
	}

}