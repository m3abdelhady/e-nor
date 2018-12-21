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

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
//import com.google.appengine.api.datastore.Query.FilterOperator;
/*import com.google.appengine.api.users.UserServiceFactory;*/

public class MobileCampaignService {
	public static void deleteMobileCampaign(Entity mobileCampaign) {

    	Key mobileCampaignKey = mobileCampaign.getKey(); 

	    try {
			//CASCADE_ON_DELETE
			Iterable<Entity> taggedUrls = PMUtil.listChildKeys("TaggedUrl", mobileCampaignKey);
			final List<Key> taggedUrlKeys = new ArrayList<Key>();
			for (Entity taggedUrl : taggedUrls) {
				  taggedUrlKeys.add(taggedUrl.getKey());
			}
			PMUtil.deleteEntity(taggedUrlKeys);
			PMUtil.deleteEntity(mobileCampaignKey);
	    } catch (Exception ex) {
	    	// TODO
	    	// String msg = Util.getErrorResponse(e);
	    }
	}

	public static Entity createOrUpdateMobileCampaign(String myEmail, long longAccountId, long longCampaignId, String packageName,
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
    		mobileCampaign = getSingleMobileCampaign(longAccountId, longCampaignId);
    	}

		// setting properties
		mobileCampaign.setProperty("packageName", packageName);
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
	
	
	public static Entity copyMobileCampaign(long longAccountId , long longCampaignId) {

		// creating key and campaign entity
		Entity mobileCampaign = new Entity("mobileCampaign");
		DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
        Query q = new Query("mobileCampaign");

        PreparedQuery pq = ds.prepare(q);
        for (Entity e : pq.asIterable()) {
        	mobileCampaign.setProperty("utm_campaign", e.getProperty("utm_campaign"));
        	mobileCampaign.setProperty("utm_medium",  e.getProperty("utm_medium"));
        	mobileCampaign.setProperty("utm_source",  e.getProperty("utm_source"));
        	mobileCampaign.setProperty("utm_notes",  e.getProperty("utm_notes"));
        	mobileCampaign.setProperty("modifiedBy",  e.getProperty("myEmail"));
        	mobileCampaign.setProperty("modifiedAt",  e.getProperty("date"));
            }

           //String longtext = ((Text)e.getProperty("somelongdescription")).getValue();}
		
		//Key key = KeyFactory.createKey("Account", longAccountId);
		
        return mobileCampaign;
	}


	public static int getAllMobileCampaignsCountFor(long longAccountId) {
	    Key ancestorKey = KeyFactory.createKey("Account", longAccountId);
	    Iterable<Entity> mobileCampaigns = PMUtil.listChildKeys("mobileCampaign", ancestorKey);

	    int i = 0;
		for (@SuppressWarnings("unused") Entity	mobileCampaign: mobileCampaigns) {
			i++;
		}
	    return i;
	}

	public static Iterable<Entity> getAllMobileCampaignsFor(long longAccountId) {
	    Key ancestorKey = KeyFactory.createKey("Account", longAccountId);
	    return PMUtil.listChildren("mobileCampaign", ancestorKey);
	}

	public static Entity getSingleMobileCampaign(long longAccountId, long longCampaignId) {
		Key key = KeyFactory.createKey("Account", longAccountId);
		key = KeyFactory.createKey(key, "mobileCampaign", longCampaignId);
		return PMUtil.findEntity(key);
	}

	public static String renderMobileCampaignForm(Entity account,	long longCampaignId, String packageName,
			String utm_campaign, String utm_medium, String utm_source, String utm_notes ) {
	    StringBuilder sb = new StringBuilder();

	    long longAccountId = account.getKey().getId();
	    Iterable<Entity> campaignHolders = MobileCampaignHolderService.getAllMobileCampaignHoldersFor(longAccountId);
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
			firstOption = "Choose from an existing campaign";
		} else {
			buttonText = " Save ";
			firstOption = utm_campaign;
			
		}

	    sb.append("<form class=\"form\" action=\"/editmobilecampaign\" method=\"post\" name=\"mobileCampaignForm\" onsubmit=\"return validateMobileCampaignForm();\">\r\n");
    	sb.append("<div class=\"form-body\">\r\n");
    	
    	//package
    	sb.append("<div class=\"form-group\">\r\n");
    	sb.append("\t<label>Package *</label>\r\n");
    	sb.append("\t<div class=\"mws-form-item small\">\r\n");
		sb.append("\t<input type=\"text\" name=\"packageName\" value=\"" + packageName +"\" class=\"form-control\" />\r\n");
    	sb.append("\t</div>\r\n");
    	sb.append("</div>\r\n");
    	
    	//camapgin
    	
    
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
    	sb.append("</div>\r\n");

		sb.append("<input type=\"hidden\" name=\"aid\" value=\"" + longAccountId + "\" />\r\n");
	    sb.append("<input type=\"hidden\" name=\"cid\" value=\"" + longCampaignId + "\" />\r\n");
		sb.append("</div></form>\r\n");

	    return sb.toString();
	}

	private static String renderChannelFormElement(Entity account, String utm_medium) {
	    StringBuilder sb = new StringBuilder();

	    @SuppressWarnings("unchecked")
		List<String> channels = (ArrayList<String>) account.getProperty("channels");

		sb.append("\t\t<select  data-placeholder='Select a marketing channel'  class='form-control select2' name=\"utm_medium\"><option></option>\r\n");	

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

	public static String renderMobileCampaignDetails(String myRole, Entity mobileCampaign) {
	    StringBuilder sb = new StringBuilder();

		long longAccountId = mobileCampaign.getParent().getId();
		long longCampaignId = mobileCampaign.getKey().getId();

		String packageName = (String) mobileCampaign.getProperty("packageName");
		String utm_campaign = (String) mobileCampaign.getProperty("utm_campaign");
		String utm_medium = (String) mobileCampaign.getProperty("utm_medium");
		String utm_source = (String) mobileCampaign.getProperty("utm_source");
		String utm_notes = (String) mobileCampaign.getProperty("utm_notes");

		sb.append("<div style='padding:5px 0 0 5px; class=\"mws-panel-content\">\r\n");
		sb.append("<div style='min-height: 40px;height: 100%;width: 100%;display: block;' class=\"mws-button-row\">\r\n");
		sb.append("<div style='width: 100%;clear: none;display: inline-block; margin:auto 5px;'>");
		sb.append("<ul class='cdetails' style='width:88%'>"
				 + "<li><ul><li>Package Name</li><li style='background-color:#cfcfcf; text-align:center; font-weight:normal; font-size:14px;'>"+ packageName +"</li></ul></li>"
		  + "<li><ul><li>Campaign</li><li style='background-color:#cfcfcf; text-align:center; font-weight:normal; font-size:14px;'>"+ utm_campaign +"</li></ul></li>"
		  + "<li><ul><li>Medium</li><li style='background-color:#cfcfcf; text-align:center; font-weight:normal; font-size:14px;'>" + utm_medium + "</li></ul></li>"
		  + "<li><ul><li>Source</li><li style='background-color:#cfcfcf; text-align:center; font-weight:normal; font-size:14px;'>" + utm_source + "</li></ul></li>"
		  + "</ul>");	
		

	    if (UserType.hasPermission(myRole, UserType.PERMISSION_MANAGE_CAMPAIGNS) ) {
	    	sb.append("<form class=\"mws-form\" style='float:left; clear:none; height:105px; line-height: 84px;' action=\"/editcampaign\">\r\n");
	    	//sb.append("<div class=\"mws-form-inline\">\r\n");
			//sb.append("\t<div class=\"mws-button-row\">\r\n");
			sb.append("\t\t<input type=\"button\" value=\" Edit \" class=\"btn default\" onClick=\"location.href='/EditMobileCampaign.jsp?aid=" + longAccountId + "&cid=" + longCampaignId + "'\"/>\r\n");
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

	public static String renderMobileCampaignList(String myRole, long longAccountId, String campaignName) {
	    StringBuilder sb = new StringBuilder();

		// retrieve the campaigns belonging to the selected Account.
		Iterable<Entity> mobileCampaigns = getAllMobileCampaignsFor(longAccountId);
		if (UserType.hasPermission(myRole, UserType.PERMISSION_MANAGE_CAMPAIGNS)) {
			sb.append("<form class=\"mws-form\">\r\n");
	    	sb.append("<div class=\"mws-form-inline\" style='text-align:right;'>\r\n");
			sb.append("\t<div style='text-align:right;' class=\"form-group\">\r\n");
			sb.append("\t\t<input type=\"button\" value=\" + Create New Mobile Campaign \" class=\"btn sbold green\" onClick=\"location.href='/EditMobileCampaign.jsp?aid=" + longAccountId + "&utm_campaign="+campaignName.replace("'", "\\\'")+"'\"/>\r\n");
			sb.append("\t</div>\r\n");
			sb.append("</div>\r\n");
			sb.append("</form>\r\n");
		}
		sb.append("<form method='post' action='/editmobilecampaign?aid="+longAccountId+"&act=" + Messages.ACTION_DELETEAll+"'>");
		sb.append("<table class=\"sortable table table-striped table-bordered table-hover table-checkable order-column dataTable no-footer\">\r\n");
	    sb.append("<thead><tr>\r\n");
	    sb.append("<th class='no-sort' style='padding:8px 10px'><input type=\"checkbox\" id='deletAll'/></th>\r\n");
	   // sb.append("<th>Campaign</th>\r\n");
	    sb.append("<th>Package</th>\r\n");
	    sb.append("<th>Medium</th>\r\n");
	    sb.append("<th>Source</th>\r\n");
	    sb.append("<th>Creation Date</th>\r\n");
	    sb.append("<th class='no-sort'>Action</th>\r\n");
	    sb.append("</tr>\r\n");
	    sb.append("</thead>\r\n");
	    sb.append("<tbody>\r\n");

	    int i=0;
		long longCampaignId = 0;
		Date date = null;
		List<Long> sCampaignIds = new ArrayList<Long>();
		for (Entity mobileCampaign : mobileCampaigns) {
			if(mobileCampaign.getProperty("utm_campaign").toString().contentEquals(campaignName)){
			longCampaignId = mobileCampaign.getKey().getId();
			date = (Date) mobileCampaign.getProperty("createdAt");
			sb.append("<tr>\r\n");
			sb.append("<td><input type=\"checkbox\" name=\"cid\" class='checkboxes' value=\"" + longCampaignId + "\" /></td>\r\n");
			//sb.append("<td><a href=\"/MCTaggedURLs.jsp?aid=" + longAccountId + "&cname=" + mobileCampaign.getProperty("utm_campaign") +"&cid=" + longCampaignId + "\">" + mobileCampaign.getProperty("utm_campaign") + "</a></td>\r\n");
			//sb.append("<td>" + mobileCampaign.getProperty("utm_campaign") + "</td>\r\n");
			sb.append("<td>" + mobileCampaign.getProperty("packageName") + "</td>\r\n");			
			sb.append("<td>" + mobileCampaign.getProperty("utm_medium") + "</td>\r\n");
			sb.append("<td>" + mobileCampaign.getProperty("utm_source") + "</td>\r\n");
			if (null!=date)
				sb.append("<td>" + DateFormat.getDateInstance(DateFormat.MEDIUM).format(date) + "</td>\r\n");
			else
				sb.append("<td>&nbsp;</td>\r\n");
			sb.append("<td>\r\n");
			sb.append("<a href=\"/MCTaggedURLs.jsp?aid=" + longAccountId + "&cid=" + longCampaignId + "\">Tagged URLs</a>");
			if (UserType.hasPermission(myRole, UserType.PERMISSION_MANAGE_CAMPAIGNS)) {
				sb.append("&nbsp;|&nbsp;");
				sb.append("<a href=\"/EditMobileCampaign.jsp?aid=" + longAccountId + "&cid=" + longCampaignId + "\">Edit</a>");
				sb.append("&nbsp;|&nbsp;");
				
				//sb.append("<a href=\"/EditMobileCampaign.jsp?aid=" + longAccountId + "&packageName=" + mobileCampaign.getProperty("packageName") + "&utm_campaign=" + mobileCampaign.getProperty("utm_campaign") + "&utm_medium=" + mobileCampaign.getProperty("utm_medium") + "&utm_source=" + mobileCampaign.getProperty("utm_source") + "\">Copy</a>");
				sb.append("<a onclick=\"return confirm('Are you sure you want to copy campaign?')\" href=\"/editmobilecampaign?aid=" + longAccountId + "&packageName=" + mobileCampaign.getProperty("packageName") + "&cid=" + longCampaignId + "&utm_campaign=" + mobileCampaign.getProperty("utm_campaign") + "&utm_medium=" + mobileCampaign.getProperty("utm_medium") + "&utm_source=" + mobileCampaign.getProperty("utm_source")  +"&act=" + Messages.ACTION_COPY+ "\">Copy</a>");
				sb.append("&nbsp;|&nbsp;");
				//sb.append("<a href=\"javascript:confirmCampaignDelete('/editmobilecampaign?aid=" + longAccountId  + "&cid=" + longCampaignId + "&act=" + Messages.ACTION_DELETE + "');\">Delete</a>");
				sb.append("<a href=\"javascript:confirmCampaignDelete(\'/editmobilecampaign?utm_campaign="+campaignName.replace("'", "\\\'")+"&aid="+longAccountId+"&cid="+longCampaignId+"&act=DELETE\');\">Delete</a>");
			}
			sb.append("</td>\r\n");
			sb.append("</tr>\r\n");
			i++;
		}
		}
		if (i==0) {
			sb.append("<tr><td colspan=\"7\">This account has no campaigns yet.</td></tr>\r\n");
		}

		sb.append("</tbody>\r\n");
		sb.append("</table>\r\n");
		
		if (UserType.hasPermission(myRole, UserType.PERMISSION_MANAGE_CAMPAIGNS)) {
			if(0 !=i){
				
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
			//sb.append("\t\t<input type=\"button\" value=\" Create New Mobile Campaign \" class=\"mws-button red\" onClick=\"location.href='/EditMobileCampaign.jsp?aid=" + longAccountId + "'\"/>\r\n");
			sb.append("\t</div>\r\n");
			sb.append("</div>\r\n");
			sb.append("</form>\r\n");
		}

	    return sb.toString();
	}

	public static String renderMobileCampaignExportForm(long longAccountId) {
	    StringBuilder sb = new StringBuilder();

		// retrieve the campaigns belonging to the selected Account.
		Iterable<Entity> mobileCampaigns = getAllMobileCampaignsFor(longAccountId);

	    sb.append("<form class=\"mws-form\" action=\"/exportcampaigns\" method=\"post\">\r\n");
		sb.append("<table class=\"mws-table table table-striped table-bordered table-hover table-checkable order-column dataTable no-footer\">\r\n");
	    sb.append("<thead><tr>\r\n");
	    sb.append("<th>&nbsp;</th>\r\n");
	    sb.append("<th>Package</th>\r\n");
	    sb.append("<th>Campaign</th>\r\n");
	   // sb.append("<th>Medium</th>\r\n");
	   // sb.append("<th>Source</th>\r\n");
	    sb.append("<th>Creation Date</th>\r\n");
	    sb.append("</tr>\r\n");
	    sb.append("</thead>\r\n");
	    sb.append("<tbody>\r\n");

	    int i=0;
		long longCampaignId = 0;
		Date date = null;
		for (Entity mobilecampaign : mobileCampaigns) {
			longCampaignId = mobilecampaign.getKey().getId();
			date = (Date) mobilecampaign.getProperty("createdAt");
			sb.append("<tr>\r\n");
			sb.append("<td><input type=\"checkbox\" name=\"cid\" value=\"" + longCampaignId + "\" /></td>\r\n");
			sb.append("<td>" + mobilecampaign.getProperty("utm_campaign") + "</td>\r\n");
			//sb.append("<td>" + mobilecampaign.getProperty("utm_medium") + "</td>\r\n");
			//sb.append("<td>" + mobilecampaign.getProperty("utm_source") + "</td>\r\n");
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

    	sb.append("<div class=\"mws-form-inline\">\r\n");
		if (0!=i) {
	    	sb.append("<div class=\"form-group\">\r\n");
	    	sb.append("\t<label>Delimiter</label>\r\n");
	    	sb.append("\t<div class=\"mws-form-item clearfix\"><ul class=\"mws-form-list inline\">\r\n");
			sb.append("\t\t<li><input type=\"radio\" name=\"delimiter\" value=\"" + Porting.DEL_TSV + "\" checked> <label>Tab</label></li>\r\n" +
					"\t\t<li><input type=\"radio\" name=\"delimiter\" value=\"" + Porting.DEL_SEMICOLON + "\"> <label>Semicolon</label></li>\r\n" +
					"\t\t<li><input type=\"radio\" name=\"delimiter\" value=\"" + Porting.DEL_CSVEXCEL + "\"> <label>CSV for Excel</label></li>\r\n");
	    	sb.append("\t</ul></div>\r\n");
	    	sb.append("</div>\r\n");

		    sb.append("<div class=\"mws-button-row\">\r\n");
			sb.append("\t<input type=\"submit\" value=\" Export \" class=\"mws-button red\" />\r\n");
	    	sb.append("</div>\r\n");

			sb.append("<input type=\"hidden\" name=\"aid\" value=\"" + longAccountId + "\" />\r\n");
		}

		sb.append("</div></form>\r\n");

	    return sb.toString();
	}

	public static String renderMobileCampaignImportForm(long longAccountId) {
	    StringBuilder sb = new StringBuilder();

		sb.append("<form class=\"mws-form\" action=\"/importcampaigns\" enctype=\"multipart/form-data\" method=\"post\">\r\n");
    	sb.append("<div class=\"mws-form-inline\">\r\n");
    	sb.append("<div class=\"form-group\">\r\n");
    	sb.append("\t<label>File*</label>\r\n");
    	sb.append("\t<div class=\"mws-form-item medium\">\r\n");
		sb.append("\t\t<input type=\"file\" name=\"file\" />\r\n");
    	sb.append("\t</div>\r\n");
    	sb.append("</div>\r\n");

		sb.append("<div class=\"mws-button-row\">\r\n");
		sb.append("\t<input type=\"submit\" value=\" Import \" class=\"mws-button red\" />\r\n");
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
