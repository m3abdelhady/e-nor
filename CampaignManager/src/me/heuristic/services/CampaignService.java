package me.heuristic.services;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import me.heuristic.util.PMUtil;
import me.heuristic.references.AccountType;
import me.heuristic.references.Messages;
import me.heuristic.references.Porting;
import me.heuristic.references.UserType;








/*import com.google.appengine.api.datastore.BaseDatastoreService;
import com.google.appengine.api.datastore.Cursor;*/
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.QueryResultList;
/*import com.google.appengine.api.users.UserServiceFactory;*/
import com.google.appengine.api.datastore.FetchOptions;

public class CampaignService {
	public static void deleteCampaign(Entity campaign) {

    	Key campaignKey = campaign.getKey(); 

	    try {
			//CASCADE_ON_DELETE
			Iterable<Entity> taggedUrls = PMUtil.listChildKeys("TaggedUrl", campaignKey);
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
	
	public static Entity createOrUpdateCampaign(String myEmail, long longAccountId, long longCampaignId,
			String utm_campaign, String utm_medium, String utm_source, String utm_notes) {

		// creating key and campaign entity
        Date date = new Date();
        Entity campaign;
		Key key = KeyFactory.createKey("Account", longAccountId);
		if (0==longCampaignId) {
			// new account
			campaign = new Entity("Campaign", key);
	        campaign.setProperty("createdBy", myEmail);
	        campaign.setProperty("createdAt", date);
    	} else {
			// editing existing campaign
    		// retrieve it from datastore
			campaign = getSingleCampaign(longAccountId, longCampaignId);
    	}

		// setting properties
    	campaign.setProperty("utm_campaign", utm_campaign);
    	campaign.setProperty("utm_medium", utm_medium);
    	campaign.setProperty("utm_source", utm_source);
    	campaign.setProperty("utm_notes", utm_notes);
        campaign.setProperty("modifiedBy", myEmail);
        campaign.setProperty("modifiedAt", date);

        // saving entity
        PMUtil.persistEntity(campaign);
        return campaign;
	}

	@SuppressWarnings("unused")
	public static int getAllCampaignsCountFor(long longAccountId) {
	    Key ancestorKey = KeyFactory.createKey("Account", longAccountId);
	    Iterable<Entity> campaigns = PMUtil.listChildKeys("Campaign", ancestorKey);

	    int i = 0;
		for (Entity campaign: campaigns) {
			i++;
		}
	    return i;
	}

	
	public static int getAllCampaignsCountForEmail(String myEmail) {
		 int pageSize = 1000;
		 int i = 0;
		    FetchOptions fetchOptions = FetchOptions.Builder.withLimit(pageSize); 		    
		    Query q = new Query("Campaign").addFilter("createdBy", FilterOperator.EQUAL, myEmail);
		    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
			PreparedQuery pq = datastore.prepare(q);
		    
		    QueryResultList<Entity> results = pq.asQueryResultList(fetchOptions);
		    for(Entity result : results){
		    	
		    	i++;
		    }  
		    
		    return i;
	}

	public static Iterable<Entity> getAllCampaignsFor(long longAccountId) {
	    Key ancestorKey = KeyFactory.createKey("Account", longAccountId);
	    return PMUtil.listChildren("Campaign", ancestorKey);
	}

	public static Entity getSingleCampaign(long longAccountId, long longCampaignId) {
		Key key = KeyFactory.createKey("Account", longAccountId);
		key = KeyFactory.createKey(key, "Campaign", longCampaignId);
		return PMUtil.findEntity(key);
	}

	public static String renderCampaignForm(Entity account,	long longCampaignId,
			String utm_campaign, String utm_medium, String utm_source, String utm_notes ) {
	    StringBuilder sb = new StringBuilder();

	    long longAccountId = account.getKey().getId();
	    Iterable<Entity> campaignHolders = CampaignHolderService.getAllCampaignHoldersFor(longAccountId);
	    List<Long> sCampaignIds = new ArrayList<Long>();
		List<String> sCampaignHolderIds = new ArrayList<String>();	
		String accountType = (String) account.getProperty("type");		
			
	    // default for new campaign
		String buttonText = "";
		String firstOption ="";
		String selected = "";

		//if new campaign
		if (0==longCampaignId) {
			buttonText = " Create Campaign ";
			firstOption = "Choose from an existing campaigns";
		} else {
			buttonText = " Save ";
			firstOption = utm_campaign;
			
		}

	    sb.append("<form class=\"form\" action=\"/editcampaign\" method=\"post\" name=\"campaignForm\" onsubmit=\"return validateCampaignForm();\">\r\n");
    	sb.append("<div class=\"form-body\">\r\n");
    	
    	if(campaignHolders.iterator().hasNext()){
    	sb.append("<div class=\"col-md-6\" style='padding-left:0'>\r\n");
    	sb.append("<div class=\"form-group\">\r\n");
    	sb.append("\t<label>Campaign*</label>\r\n");
		sb.append("\t<input type=\"text\" name=\"utm_campaign\" id='utm_campaign' value=\"" + utm_campaign +"\" class=\"form-control\" />\r\n");
		sb.append("</div>\r\n");
		sb.append("</div>\r\n");
		
		sb.append("<div class=\"col-md-6\" style='padding-right:0'>\r\n");
    	sb.append("<div class=\"form-group\">\r\n");
    	sb.append("\t<label style='visibility:hidden'>Existing Campaigns</label>\r\n");
    	//sb.append("\t<div class=\"mws-form-item small\">\r\n");
      	
    		//sb.append("\t<input style='display:none;' type=\"text\" id='utm_campaign' name=\"utm_campaign\" placeholder = 'Search'\" class=\"form-control\" />\r\n");
    		sb.append("<select data-placeholder='Select an existing campaign' name='eutm_campaign' id='eutm_campaign' class='form-control select2'> ");    	
    		//sb.append("<option>"+firstOption+"</option>");
    		sb.append("<option></option>");
    		/*sb.append("<option id='newCamp'>Create New Campaign  &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;  using "+CampaignHolderService
    				.getAllCampaignHoldersCountFor(longAccountId)+" out of "+AccountType.maxCampaignsMap.get(accountType)+"</option>");*/
    		
    		//sb.append("<option id='newCamp'><span class='leftpart'>Create New Campaign</span><span class='rightpart'> &nbsp; / &nbsp; using "+MobileCampaignHolderService
    			//	.getAllMobileCampaignHoldersCountFor(longAccountId)+ MobileCampaignHolderService
    			//	.getAllMobileCampaignHoldersCountFor(longAccountId)+" out of "+AccountType.maxCampaignsMap.get(accountType)+"</span></option>");
    		for (Entity campaignHolder : campaignHolders) {						
        				//longCampaignId = campaignHolder.getKey().getId();
    			if (!sCampaignHolderIds.contains(campaignHolder.getProperty("utm_campaign").toString())){
    				
    				sCampaignHolderIds.add(campaignHolder.getProperty("utm_campaign").toString());   				
        				
    }
        				
        			}
    		for(int i =0; i< sCampaignHolderIds.size(); i++){
    			/*if(sCampaignHolderIds.get(i).contentEquals(utm_campaign)){
    				selected = "selected";
    				sb.append("<option "+selected+">"+sCampaignHolderIds.get(i)+"</option>");
    			}*/
    			//else{
    				sb.append("<option>"+sCampaignHolderIds.get(i)+"</option>");
    			//}
    		}
    		
    		sb.append("</select>");
    		sb.append("</div>\r\n");
    		sb.append("</div>\r\n");
	}
  	
    	else{
    		//sb.append("<div class=\"col-md-12\" style='padding-right:0:'>\r\n");
        	sb.append("<div class=\"form-group\">\r\n");
        	sb.append("\t<label>Campaign*</label>\r\n");
    		sb.append("\t<input type=\"text\" name=\"utm_campaign\" id='utm_campaign' value=\"" + utm_campaign +"\" class=\"form-control\" />\r\n");
    		sb.append("</div>\r\n");
    		//sb.append("</div>\r\n");
    	}
	
	
	
	
	//sb.append("\t<input type=\"text\" name=\"utm_campaign\" value=\"" + utm_campaign +"\" class=\"form-control\" />\r\n");
	//sb.append("\t</div>\r\n");
  	
  	

    	// medium
    	sb.append("<div class=\"form-group\">\r\n");
    	sb.append("\t<label>Medium*</label>\r\n");
    	sb.append("\t<div class=\"mws-form-item small\">\r\n");
	    sb.append(renderChannelFormElement(account, utm_medium));
    	sb.append("\t</div>\r\n");
    	sb.append("</div>\r\n");

    	// source
    	sb.append("<div class=\"form-group\">\r\n");
    	sb.append("\t<label>Source*</label>\r\n");
    	sb.append("\t<div class=\"mws-form-item small\">\r\n");
		sb.append("\t\t<input type=\"text\" name=\"utm_source\" value=\"" + utm_source +"\" class=\"form-control\" />\r\n");
    	sb.append("\t</div>\r\n");
    	sb.append("</div>\r\n");

    	// notes
    	sb.append("<div class=\"form-group\">\r\n");
    	sb.append("\t<label>Notes</label>\r\n");
    	sb.append("\t<div class=\"mws-form-item small\">\r\n");
		sb.append("\t\t<textarea class='form-control' name=\"utm_notes\">" + utm_notes +"</textarea>\r\n");
    	sb.append("\t</div>\r\n");
    	sb.append("</div>\r\n");

    	// submit
    	sb.append("<div class=\"mws-button-row\">\r\n");
		sb.append("\t<input type=\"submit\" value=\"" + buttonText + "\" class=\"btn sbold green\" />\r\n");
		sb.append("\t<button type='reset' class=\"btn default\" onclick='goBack()'>Cancel</button>\r\n");  
		// sb.append("<a class=\"btn sbold green_default\" onclick='goBack()'>Cancel</a>");

		
		sb.append("</div>\r\n");

		sb.append("<input type=\"hidden\" name=\"aid\" value=\"" + longAccountId + "\" />\r\n");
	    sb.append("<input type=\"hidden\" name=\"cid\" value=\"" + longCampaignId + "\" />\r\n");
	    //sb.append("<input type=\"hidden\" name=\"cname\" value=\"" + utm_campaign + "\" />\r\n");
		sb.append("</div></form>\r\n");

	    return sb.toString();
	}

	private static String renderChannelFormElement(Entity account, String utm_medium) {
	    StringBuilder sb = new StringBuilder();

	    @SuppressWarnings("unchecked")
		List<String> channels = (ArrayList<String>) account.getProperty("channels");

		sb.append("\t\t<select data-placeholder='Select a marketing channel' name=\"utm_medium\" class='form-control select2'><option></option>\r\n");

		
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

	public static String renderCampaignDetails(String myRole, Entity campaign) {
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

	public static String renderCampaignList(String myRole, long longAccountId, String campaignName) {
	    StringBuilder sb = new StringBuilder();
	    Entity myAccount = AccountService.getSingleAccount(longAccountId);
		// retrieve the campaigns belonging to the selected Account.
		Iterable<Entity> campaigns = getAllCampaignsFor(longAccountId);
		if (UserType.hasPermission(myRole, UserType.PERMISSION_MANAGE_CAMPAIGNS)) {
			sb.append("<form class=\"mws-form\">\r\n");
	    	sb.append("<div class=\"mws-form-inline\">\r\n");
			sb.append("\t<div style='text-align:right;' class=\"form-group\">\r\n");
			sb.append("\t\t<input type=\"button\" value=\"+ New Campaign \" class=\"btn sbold green\" onClick=\"location.href='/EditCampaign.jsp?aid=" + longAccountId + "&utm_campaign="+campaignName.replace("'", "\\\'")+"'\"/>\r\n");
			sb.append("<input type='button' value='+ Import Campaigns' class='btn sbold blue' onclick=\"location.href='/ImportCampaigns.jsp?aid=" + longAccountId + "'\">");
			sb.append("\t</div>\r\n");
			sb.append("</div>\r\n");
			sb.append("</form>\r\n");
		}
		//sb.append("<form method='post' action='/editcampaign?aid="+longAccountId+"&utm_campaign="+campaignName+"&act=" + Messages.ACTION_DELETEAll+"'>");
		sb.append("<form method='post' action=\"/editcampaign?aid="+longAccountId+"&utm_campaign="+campaignName+"&act=" + Messages.ACTION_DELETEAll+"\">");
		//sb.append("<form method='post' action="editcampaign?utm_campaign="+campaignName+"&aid="+longAccountId+"&act="+Messages.ACTION_DELETEAll+">");
		
		//sb.append("<a href=\"javascript:confirmCampaignDelete(\'/editcampaign?utm_campaign="+campaignName.replace("'", "\\\'")+"&aid="+longAccountId+"&act="+Messages.ACTION_DELETEAll+"\');\">Delete "+campaignName+"</a>");
		//	=" + longAccountId  + "&act=" + Messages.ACTION_DELETEAll +"
		sb.append("<table class=\"sortable table table-striped table-bordered table-hover table-checkable order-column dataTable no-footer\">\r\n");
	    sb.append("<thead><tr>\r\n");
	    sb.append("<th class='no-sort chekboxTD' style='padding:8px 10px'><input type=\"checkbox\" id='deletAll'/></th>\r\n");
	    //sb.append("<th>Campaign</th>\r\n");
	    /*sb.append("<th>Medium</th>\r\n");
	    sb.append("<th>Source</th>\r\n");*/
	    sb.append("<th>Source / Medium</th>\r\n");
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
		
		for (Entity campaign : campaigns) {	
			if(campaign.getProperty("utm_campaign").toString().contentEquals(campaignName)){	
			
			//campaignName.replace("'", "\\\'");	
			longCampaignId = campaign.getKey().getId();
			sCampaignIds.add(longCampaignId);
			date = (Date) campaign.getProperty("createdAt");
			sb.append("<tr>\r\n");
			sb.append("<td class='chekboxTD'><input type=\"checkbox\" name=\"cid\" class='checkboxes' value=\"" + longCampaignId + "\" /></td>\r\n");
			//sb.append("<td><a href=\"/ManageTaggedURLs.jsp?aid=" + longAccountId + "&cname=" + campaign.getProperty("utm_campaign") + "&cid=" + longCampaignId + "\">" + campaign.getProperty("utm_campaign") + "</a></td>\r\n");
			/*sb.append("<td>" + campaign.getProperty("utm_medium") + "</td>\r\n");
			sb.append("<td>" + campaign.getProperty("utm_source") + "</td>\r\n");*/
			sb.append("<td>" + campaign.getProperty("utm_source") +" / "+ campaign.getProperty("utm_medium")+"</td>\r\n");
			if (null!=date)
				sb.append("<td>" + DateFormat.getDateInstance(DateFormat.MEDIUM).format(date) + "</td>\r\n");
			else
				sb.append("<td>&nbsp;</td>\r\n");
			sb.append("<td class='action'>\r\n");
			sb.append("<a href=\"/ManageTaggedURLs.jsp?aid=" + longAccountId + "&cname=" + campaign.getProperty("utm_campaign") + "&cid=" + longCampaignId + "&ms="+ campaign.getProperty("utm_medium") +"/"+  campaign.getProperty("utm_source")+"\">Tagged URLs</a>");
			if (UserType.hasPermission(myRole, UserType.PERMISSION_MANAGE_CAMPAIGNS) || UserType.hasPermission("ADMIN", UserType.PERMISSION_MANAGE_CAMPAIGNS)) {
				sb.append("&nbsp;|&nbsp;");
				sb.append("<a href=\"/EditCampaign.jsp?aid=" + longAccountId + "&cid=" + longCampaignId + "\">Edit</a>");
				sb.append("&nbsp;|&nbsp;");
				sb.append("<a onclick=\"return confirm('Are you sure you want to copy campaign?')\" href=\"/editcampaign?aid=" + longAccountId + "&cid=" + longCampaignId + "&utm_campaign=" + campaign.getProperty("utm_campaign") + "&utm_medium=" + campaign.getProperty("utm_medium") + "&utm_source=" + campaign.getProperty("utm_source")  +"&act=" + Messages.ACTION_COPY+ "\">Copy</a>");
				
				sb.append("&nbsp;|&nbsp;");
				//sb.append("<a href=\"javascript:confirmCampaignDelete('/editcampaign?aid=" + longAccountId  + "&utm_campaign="+campaignName+"&cid=" + longCampaignId + "&act=" + Messages.ACTION_DELETE + "');\">Delete</a>");
				sb.append("<a href=\"javascript:confirmCampaignDelete(\'/editcampaign?utm_campaign="+campaignName.replace("'", "\\\'")+"&aid="+longAccountId+"&cid="+longCampaignId+"&act=DELETE\');\">Delete</a>");
				
			}
			if(AnalyticsService.GetRefreshKey(longAccountId) !=null && AnalyticsService.GetViewId(longAccountId) !=null ){
			sb.append("&nbsp;|&nbsp;");
			sb.append("<a href=\"/AnalyticsCallBack?dis=AnalyzLanding&aid=" + longAccountId + "&cname=" + campaign.getProperty("utm_campaign") + "&ms="+ campaign.getProperty("utm_source") +"/"+  campaign.getProperty("utm_medium")+"\">Analyze <span style='padding-left: 2px; color: #dd7127; font-size: 9px; font-weight: bold; position: relative; text-transform: uppercase; top: -3px;'>BETA</span></a>");
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
						+ "<input type='submit' value='Delete Selected' class='btn red btn-outline'></div></form>");
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

	public static String renderCampaignExportForm(long longAccountId) {
	    StringBuilder sb = new StringBuilder();

		// retrieve the campaigns belonging to the selected Account.
		Iterable<Entity> campaigns = CampaignHolderService.getAllCampaignHoldersFor(longAccountId);

	    sb.append("<form class=\"mws-form\" action=\"/exportcampaigns\" method=\"post\">\r\n");
		sb.append("<table class=\"sortable table table-striped table-bordered table-hover table-checkable order-column dataTable no-footer\">\r\n");
	    sb.append("<thead><tr>\r\n");
	    sb.append("<th class='no-sort chekboxTD' style='padding:8px 10px'><input type=\"checkbox\" id='exportAll'/></th>\r\n");
	    sb.append("<th>Campaign</th>\r\n");
	    //sb.append("<th>Medium</th>\r\n");
	   // sb.append("<th>Source</th>\r\n");
	    sb.append("<th>Creation Date</th>\r\n");
	    sb.append("</tr>\r\n");
	    sb.append("</thead>\r\n");
	    sb.append("<tbody>\r\n");

	    int i=0;
		long longCampaignId = 0;
		Date date = null;
		List<Long> sCampaignIds = new ArrayList<Long>();
		List<String> sCampaignHolderIds = new ArrayList<String>();
		for (Entity campaign : campaigns) {
			
if (!sCampaignHolderIds.contains(campaign.getProperty("utm_campaign").toString())){
				
			sCampaignHolderIds.add(campaign.getProperty("utm_campaign").toString());
			longCampaignId = campaign.getKey().getId();
			date = (Date) campaign.getProperty("createdAt");
			sb.append("<tr>\r\n");
			sb.append("<td class='chekboxTD'><input type=\"checkbox\" name=\"cid\" class='checkboxes' value=\"" + campaign.getProperty("utm_campaign").toString() + "\" /></td>\r\n");
			//sb.append("<td>" + campaign.getProperty("utm_campaign") + "</td>\r\n");
			//sb.append("<td><a href='/Export-M-S.jsp?aid="+longAccountId+"&cname="+campaign.getProperty("utm_campaign").toString()+"'>" + campaign.getProperty("utm_campaign").toString() + "</td>\r\n");
			sb.append("<td><a href=\"/Export-M-S.jsp?aid="+longAccountId+"&cname=" + campaign.getProperty("utm_campaign") + "&cid=" + longCampaignId + "\">" + campaign.getProperty("utm_campaign") + "</a></td>\r\n");
			//sb.append("<td>" + campaign.getProperty("utm_medium") + "</td>\r\n");
			//sb.append("<td>" + campaign.getProperty("utm_source") + "</td>\r\n");
			if (null!=date)
				sb.append("<td>" + DateFormat.getDateInstance(DateFormat.MEDIUM).format(date) + "</td>\r\n");
			else
				sb.append("<td>&nbsp;</td>\r\n");
			sb.append("</tr>\r\n");
			i++;
}
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

	
	public static String renderCampaignMSExportForm(long longAccountId, String cname) {
		StringBuilder sb = new StringBuilder();

		// retrieve the campaigns belonging to the selected Account.
		Iterable<Entity> campaigns = getAllCampaignsFor(longAccountId);
		Iterable<Entity> mediumSource = PMUtil.listEntities("Campaign", "utm_campaign", cname);

	    sb.append("<form class=\"mws-form\" action=\"/exportcampaignsMS\" method=\"post\">\r\n");
		sb.append("<table class=\"sortable table table table-striped table-bordered table-hover table-checkable order-column dataTable no-footer \">\r\n");
	    sb.append("<thead><tr>\r\n");
	    sb.append("<th class='no-sort chekboxTD' style='padding:8px 10px'><input type=\"checkbox\" id='exportAll'/></th>\r\n");
	   // sb.append("<th>Campaign</th>\r\n");
	    sb.append("<th>Medium</th>\r\n");
	    sb.append("<th>Source</th>\r\n");
	    sb.append("<th>Creation Date</th>\r\n");
	    sb.append("</tr>\r\n");
	    sb.append("</thead>\r\n");
	    sb.append("<tbody>\r\n");

	    int i=0;
		long longCampaignId = 0;
		Date date = null;
		for (Entity campaign : campaigns) {
			if(campaign.getProperty("utm_campaign").equals(cname)){				
			
			longCampaignId = campaign.getKey().getId();
			date = (Date) campaign.getProperty("createdAt");
			sb.append("<tr>\r\n");
			sb.append("<td class='chekboxTD'><input type=\"checkbox\" name=\"cid\" class='checkboxes' value=\"" + longCampaignId + "\" /></td>\r\n");
			//sb.append("<td>" + campaign.getProperty("utm_campaign") + "</td>\r\n");
			sb.append("<td>" + campaign.getProperty("utm_medium") + "</td>\r\n");
			sb.append("<td>" + campaign.getProperty("utm_source") + "</td>\r\n");
			if (null!=date)
				sb.append("<td>" + DateFormat.getDateInstance(DateFormat.MEDIUM).format(date) + "</td>\r\n");
			else
				sb.append("<td>&nbsp;</td>\r\n");
			sb.append("</tr>\r\n");
			i++;
		}
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
	
	public static String renderCampaignImportForm(long longAccountId) {
	    StringBuilder sb = new StringBuilder();

		sb.append("<form class=\"mws-form\" action=\"/importcampaigns\" enctype=\"multipart/form-data\" method=\"post\">\r\n");
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