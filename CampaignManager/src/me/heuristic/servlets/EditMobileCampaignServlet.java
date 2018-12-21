package me.heuristic.servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import me.heuristic.util.HtmlUtil;
import me.heuristic.util.PMUtil;
import me.heuristic.references.AccountType;
import me.heuristic.references.Messages;
import me.heuristic.references.UserType;
import me.heuristic.services.CampaignHolderService;
import me.heuristic.services.CampaignService;
import me.heuristic.services.MCTaggedUrlService;
import me.heuristic.services.MobileCampaignHolderService;
import me.heuristic.services.MobileCampaignService;
import me.heuristic.services.AccountService;
import me.heuristic.services.TaggedUrlService;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

@SuppressWarnings("serial")
public class EditMobileCampaignServlet extends HttpServlet {
    public void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
    	doPost(req, resp);
    }

    public void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

    	Entity myAccount = null;
    	//String myEmail = "";
    	String myRole = "";
    	List<String> errors = new ArrayList<String>();
    	StringBuilder returnUrl = new StringBuilder();
    	
    	String myEmail = HtmlUtil.handleNullWhiteSpaces(req.getSession().getAttribute("userEmail").toString());
        // retrieve form elements
    	String act = HtmlUtil.handleNullWhiteSpaces(req.getParameter("act"));
        long longAccountId = HtmlUtil.handleNullforLongNumbers(req.getParameter("aid"));
        long longCampaignId = HtmlUtil.handleNullforLongNumbers(req.getParameter("cid"));
        String packageName = HtmlUtil.handleNullWhiteSpaces(req.getParameter("packageName"));
		String utm_campaign = HtmlUtil.handleNullWhiteSpaces(req.getParameter("utm_campaign"));
    	String utm_medium = HtmlUtil.handleNullWhiteSpaces(req.getParameter("utm_medium"));
    	String utm_source = HtmlUtil.handleNullWhiteSpaces(req.getParameter("utm_source"));
    	String utm_notes = HtmlUtil.handleNullWhiteSpaces(req.getParameter("utm_notes"));
    	String[] sCampaignIds = req.getParameterValues("cid");
    	/*
    	UserService userService = UserServiceFactory.getUserService();
        User user = userService.getCurrentUser();*/

        // make sure there's a user
        // if editing or deleting, make sure it's a valid account and the user has permission
        if (""==myEmail) {
    		errors.add(Messages.UNRECOGNIZED_USER);
    	} else {
    		//myEmail = user.getEmail();

		    myAccount = AccountService.getSingleAccount(longAccountId);
		    if (null==myAccount) {
		    	errors.add(Messages.NO_SUCH_ACCOUNT);
			} else {
		    	myRole = (String) myAccount.getProperty(myEmail);
		    	if (null==myRole) {
		    		errors.add(Messages.NO_PERMISSION);
		    	} else if (!UserType.hasPermission(myRole, UserType.PERMISSION_MANAGE_CAMPAIGNS)){
		    		errors.add(Messages.NO_PERMISSION);
		    	}
		    }
        }

		if (errors.size()>0) {
	        returnUrl.append("/ManageAccounts.jsp?");
    		for (String error:errors) {
    			returnUrl.append("&err=" + error);
    		}
    	} else {
	    	if (Messages.ACTION_DELETE.equalsIgnoreCase(act)) {
	        	// DELETE
			    Entity myMobileCampaign = MobileCampaignService.getSingleMobileCampaign(longAccountId, longCampaignId);
			    MobileCampaignService.deleteMobileCampaign(myMobileCampaign);
	    		returnUrl.append("/ManageMobileCampaigns.jsp?aid=" + longAccountId + "&cname="+utm_campaign+"&info=" + Messages.INFO_DELETED);
	    	} 
	    	else if(Messages.ACTION_DELETEAll.equalsIgnoreCase(act)){
	    		Entity myCampaign;
	    		
	    		if (null==sCampaignIds)
					errors.add(Messages.NO_CAMPAIGNS);
	    		
	    		if (errors.size()>0) {
					returnUrl.append("/ManageMobileCampaigns.jsp?aid=" + longAccountId);
					for (String error:errors) {
						returnUrl.append("&err=" + error);
					}
					//returnUrl.append("&err=" + errors);
					//returnUrl.append("/ManageCampaigns.jsp?aid=" + longAccountId + "&err=" + errors);
	    		}else{
	    			for(int i=0; i< sCampaignIds.length; i++){
	    			    myCampaign = MobileCampaignService.getSingleMobileCampaign(longAccountId, Long.parseLong(sCampaignIds[i]));
	    			    MobileCampaignService.deleteMobileCampaign(myCampaign);  
	    				
	    				
	    			} 
	    			returnUrl.append("/ManageMobileCampaigns.jsp?aid=" + longAccountId + "&info=" + Messages.INFO_DELETED);
	    			
	    		}
	    	}
	    	
else if(Messages.ACTION_COPY.equalsIgnoreCase(act)){
	    		
	    		Entity originalCampaign = MobileCampaignService.getSingleMobileCampaign(longAccountId, longCampaignId);
	    		Entity campaignCopy = null;
	    		//Entity taggedUrlCopy = null;
	    		//Iterable<Entity> myCampaignHolder = CampaignService.getAllCampaignsFor(longAccountId);
	    		Key key = KeyFactory.createKey("Account", longAccountId);
	    				
	        			//CampaignService.createOrUpdateCampaign(myEmail, longAccountId, 0, utm_campaign +" (copy)" ,utm_medium,utm_source, utm_notes);
	        			campaignCopy = new Entity("mobileCampaign", key);
	    	    		campaignCopy.setProperty("createdBy", myEmail);
	    	    		campaignCopy.setProperty("createdAt", new Date());
	    	    		campaignCopy.setProperty("packageName", (String) originalCampaign.getProperty("packageName"));
	    	    		campaignCopy.setProperty("utm_campaign", (String) originalCampaign.getProperty("utm_campaign"));
	    	    		campaignCopy.setProperty("utm_medium", (String) originalCampaign.getProperty("utm_medium"));
	    	    		campaignCopy.setProperty("utm_source", (String) originalCampaign.getProperty("utm_source"));
	    	    		campaignCopy.setProperty("utm_notes", (String) originalCampaign.getProperty("utm_notes"));
	    	    		campaignCopy.setProperty("modifiedBy", myEmail);
	    	    		campaignCopy.setProperty("modifiedAt", new Date());
	        			Key campaignKey = originalCampaign.getKey();
	    	    		longCampaignId = campaignKey.getId();
	        			PMUtil.persistEntity(campaignCopy);	        				        			
	        			try {
	        				//CASCADE_ON_DELETE
	        				Iterable<Entity> taggedUrls = PMUtil.listChildKeys("MobileTaggedUrl", campaignKey);
	        				Iterable<Entity> oTaggedUrls = PMUtil.listChildren("MobileTaggedUrl", campaignKey);
	        				//final List<Key> taggedUrlKeys = new ArrayList<Key>();
	        				
	        				for (Entity taggedUrl : oTaggedUrls) {
	        					
	        					// taggedUrlKeys.add(taggedUrl.getKey());
	        					 MCTaggedUrlService.createOrUpdateTaggedUrl(myEmail, longAccountId, campaignCopy.getKey().getId(), 0, taggedUrl.getProperty("utm_term").toString(), taggedUrl.getProperty("utm_content").toString(), taggedUrl.getProperty("utm_content").toString());
	        					 
	        					// PMUtil.persistEntity(taggedUrl);
	        				}
	        				
	        				
	        		    } catch (Exception ex) {
	        		    	// TODO
	        		    	// String msg = Util.getErrorResponse(e);
	        		
	        	}	
	    		returnUrl.append("/ManageMobileCampaigns.jsp?aid=" + longAccountId + "&cname="+utm_campaign+"&info= Campaign Cloned Successfully");
	    	}
	    	
	    	else {
	    		//CREATE OR UPDATE
	    		String accountType = (String) myAccount.getProperty("type");
	    		int maxCampaigns =  AccountType.maxCampaignsMap.get(accountType);
	    		//int mobileCampaignsCount = MobileCampaignService.getAllMobileCampaignsCountFor(longAccountId);
	    		
	    		int mobCampaignsCount = MobileCampaignHolderService
	    				.getAllMobileCampaignHoldersCountFor(longAccountId);
	    		
	    		int webCampaignsCount = CampaignHolderService
	    				.getAllCampaignHoldersCountFor(longAccountId);
	    		
	    		/*int campaignsCount = CampaignService
	    				.getAllCampaignsCountFor(longAccountId);*/
	    		
	    		int campaignsCount = mobCampaignsCount + webCampaignsCount;


	        	//validate required field
	    		//if ("".equals(packageName)) errors.add(Messages.PACKAGENAME_REQUIRED);
		    	if ("".equals(utm_campaign)) errors.add(Messages.CAMPAIGN_REQUIRED);
		    	if ("".equals(utm_medium)) errors.add(Messages.MEDIUM_REQUIRED);
		    	if ("".equals(utm_source)) errors.add(Messages.SOURCE_REQUIRED);

		    	if (0==longCampaignId && campaignsCount >= maxCampaigns)
	        		errors.add(Messages.EXCEED_MAX_CAMPAIGNS);

		    	if (errors.size()>0) {
		    		returnUrl.append("/EditMobileCampaign.jsp?aid=" + longAccountId + "&cid=" + longCampaignId);
		    		returnUrl.append("&utm_campaign=" + HtmlUtil.encode(utm_campaign) + "&utm_medium=" + utm_medium + "&utm_source=" + utm_source);
		    		for (String error:errors) {
		    			returnUrl.append("&err=" + error);
		    		}
		    	} else {
		            // creating/ updating campaign entity
		        	Entity mobileCampaign = MobileCampaignService.createOrUpdateMobileCampaign(myEmail, longAccountId, longCampaignId, packageName, utm_campaign, utm_medium, utm_source, utm_notes);
		        	returnUrl.append("/MCTaggedURLs.jsp?aid=" + longAccountId + "&cid=" + mobileCampaign.getKey().getId() +"&cname=" + utm_campaign + "&info=" + Messages.INFO_SAVED);
		    	}
		    }
    	}
		resp.sendRedirect (returnUrl.toString());
    }
}
