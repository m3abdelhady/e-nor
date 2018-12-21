package me.heuristic.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
/*import javax.swing.text.StyledEditorKit.ItalicAction;*/

import me.heuristic.util.HtmlUtil;
import me.heuristic.util.PMUtil;
/*import me.heuristic.util.PMUtil;*/
import me.heuristic.references.AccountType;
import me.heuristic.references.Messages;
import me.heuristic.references.UserType;
import me.heuristic.services.CampaignHolderService;
import me.heuristic.services.AccountService;
import me.heuristic.services.CampaignService;
import me.heuristic.services.MobileCampaignService;
import me.heuristic.services.TaggedUrlService;

import com.google.appengine.api.datastore.Entity;
/*import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;*/
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.labs.repackaged.com.google.common.collect.Iterators;

@SuppressWarnings("serial")
public class EditCampaignHolderServlet extends HttpServlet {
    public void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
    	doPost(req, resp);
    }

    public void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

    	Entity myAccount = null;
    	//String myEmail = "";
    	//String myEmail = HtmlUtil.handleNullWhiteSpaces(req.getParameter("myEmail"));
    	String myEmail = HtmlUtil.handleNullWhiteSpaces(req.getSession().getAttribute("userEmail").toString());
    	String myRole = "";
    	List<String> errors = new ArrayList<String>();
    	StringBuilder returnUrl = new StringBuilder();

        // retrieve form elements
    	String act = HtmlUtil.handleNullWhiteSpaces(req.getParameter("act"));
        long longAccountId = HtmlUtil.handleNullforLongNumbers(req.getParameter("aid"));
        long longCampaignId = HtmlUtil.handleNullforLongNumbers(req.getParameter("cid"));
		String utm_campaign = HtmlUtil.handleNullWhiteSpaces(req.getParameter("utm_campaign"));
		String old_utm_campaign = HtmlUtil.handleNullWhiteSpaces(req.getParameter("cname"));
    	String utm_medium = HtmlUtil.handleNullWhiteSpaces(req.getParameter("utm_medium"));
    	String utm_source = HtmlUtil.handleNullWhiteSpaces(req.getParameter("utm_source"));
    	String utm_notes = HtmlUtil.handleNullWhiteSpaces(req.getParameter("utm_notes"));
    	String[] sCampaignIds = req.getParameterValues("chid");  
    	Iterable<Entity> campaignNames = PMUtil.listEntities("Campaign", "utm_campaign", old_utm_campaign);
    	Iterator<Entity> b = campaignNames.iterator();
    	int size = Iterators.size(b);
    	int i =0;
    	//UserService userService = UserServiceFactory.getUserService();
        //User user = userService.getCurrentUser();

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
			   //Entity myCampaign = CampaignService.getSingleCampaign(longAccountId, longCampaignId);
	    		CampaignHolderService.deleteCampaignHolder(utm_campaign, longAccountId);	    		
	    		returnUrl.append("/ManageCampaignHolders.jsp?aid=" + longAccountId + "&info=" + Messages.INFO_DELETED);
	    		
	    	} else if(Messages.ACTION_DELETEAll.equalsIgnoreCase(act)){
	    		//Entity myCampaign;	    		
	    		
	    		if (null==sCampaignIds)
					errors.add(Messages.NO_CAMPAIGNS);
	    		
	    		if (errors.size()>0) {
					returnUrl.append("/ManageCampaignHolders.jsp?aid=" + longAccountId);
					for (String error:errors) {
						returnUrl.append("&err=" + error);
					}
					//returnUrl.append("&err=" + errors);
					//returnUrl.append("/ManageCampaigns.jsp?aid=" + longAccountId + "&err=" + errors);
	    		}else{
	    			
	    			
	    			CampaignHolderService.deleteSelectedCampaignHolder(sCampaignIds, longAccountId);
	    			returnUrl.append("/ManageCampaignHolders.jsp?aid=" + longAccountId + "&info=" + Messages.INFO_DELETED);
	    			
	    		}
	    	}
	    	
	    	else if(Messages.ACTION_COPY.equalsIgnoreCase(act)){
	    		Entity campaignCopy = null;
	    		//Entity taggedUrlCopy = null;
	    		Iterable<Entity> myCampaignHolder = CampaignService.getAllCampaignsFor(longAccountId);
	    		Key key = KeyFactory.createKey("Account", longAccountId);
	    		for (Entity campaignHolder : myCampaignHolder) {	    			
	        		if (campaignHolder.getProperty("utm_campaign").toString().contentEquals(utm_campaign)){	        			
	        			//CampaignService.createOrUpdateCampaign(myEmail, longAccountId, 0, utm_campaign +" (copy)" ,utm_medium,utm_source, utm_notes);
	        			campaignCopy = new Entity("Campaign", key);
	    	    		campaignCopy.setProperty("createdBy", myEmail);
	    	    		campaignCopy.setProperty("createdAt", new Date());
	    	    		campaignCopy.setProperty("utm_campaign", utm_campaign + " (Copy)");
	    	    		campaignCopy.setProperty("utm_medium", campaignHolder.getProperty("utm_medium"));
	    	    		campaignCopy.setProperty("utm_source", campaignHolder.getProperty("utm_source"));
	    	    		campaignCopy.setProperty("utm_notes", campaignHolder.getProperty("utm_notes"));
	    	    		campaignCopy.setProperty("modifiedBy", myEmail);
	    	    		campaignCopy.setProperty("modifiedAt", new Date());
	        			Key campaignKey = campaignHolder.getKey();
	    	    		longCampaignId = campaignKey.getId();
	        			PMUtil.persistEntity(campaignCopy);	        				        			
	        			try {
	        				//CASCADE_ON_DELETE
	        				Iterable<Entity> taggedUrls = PMUtil.listChildKeys("TaggedUrl", campaignKey);
	        				Iterable<Entity> oTaggedUrls = PMUtil.listChildren("TaggedUrl", campaignKey);
	        				//final List<Key> taggedUrlKeys = new ArrayList<Key>();
	        				
	        				for (Entity taggedUrl : oTaggedUrls) {
	        					
	        					// taggedUrlKeys.add(taggedUrl.getKey());
	        					 TaggedUrlService.createOrUpdateTaggedUrl(myEmail, longAccountId, campaignCopy.getKey().getId(), 0, taggedUrl.getProperty("utm_url").toString(), taggedUrl.getProperty("utm_term").toString(), taggedUrl.getProperty("utm_content").toString(), taggedUrl.getProperty("utm_extra").toString());
	        					
	        					// PMUtil.persistEntity(taggedUrl);
	        				}
	        				
	        				
	        		    } catch (Exception ex) {
	        		    	// TODO
	        		    	// String msg = Util.getErrorResponse(e);
	        		    }
	        	}	
	        	}	
	    		returnUrl.append("/EditCampaignHolder.jsp?aid=" + longAccountId + "&utm_campaign="+ utm_campaign + " (Copy)&info= Campaign Cloned Successfully");
	    	}
	    	
	    	else if("UpdateCHolder".equalsIgnoreCase(act)){
	    		//CampaignHolderService.UpdateCampaignHolder(myEmail, longAccountId, old_utm_campaign, utm_campaign);
	    		
	    		// creating key and campaign entity
	           // Date date = new Date();
	         //  Entity campaign;
	    		//Key key = KeyFactory.createKey("Account", longAccountId);
	    		
	    			// editing existing campaign
	        		// retrieve it from datastore
	    		//Iterable<Entity> campaignHolders = CampaignService.getAllCampaignsFor(longAccountId);
	    		//Iterator<Entity> vvs =  CampaignHolderService.getAllCampaignHoldersFor(longAccountId).iterator();
	    		//List<Entity> bbs = (List<Entity>) CampaignService.getAllCampaignsFor(longAccountId);
	    		
	    		
	    		//Iterable<Entity> cocos = PMUtil.listEntities("Campaign", "utm_campaign", old_utm_campaign);
	    		
	    		
	    			for(Entity campaignName : campaignNames){
	    				PrintWriter out = resp.getWriter();  
	    				resp.setContentType("text/html");  
	    				out.println("<script type=\"text/javascript\">");  
	    				out.println("alert('deadbeef');");  
	    				out.println("</script>");
	    				//Entity campaign;
	    				//Key key = KeyFactory.createKey("Account", longAccountId);
	    				longCampaignId = campaignName.getKey().getId();
	    				CampaignService.createOrUpdateCampaign(myEmail, longAccountId,longCampaignId, utm_campaign, campaignName.getProperty("utm_medium").toString(),campaignName.getProperty("utm_source").toString(), campaignName.getProperty("utm_notes").toString());
	    				//campaign = CampaignService.getSingleCampaign(longAccountId, longCampaignId);
	    				//longCampaignId = campaignHolder.getKey().getId();
	    				//if(campaignHolder.getProperty("utm_campaign)").toString().contains(old_utm_campaign)){
	    					//campaign = CampaignService.getSingleCampaign(longAccountId, campaignHolder.getKey().getId());
	    					/*String new_ut_medium = campaignName.getProperty("utm_medium").toString();
		    				String new_utm_source = campaignName.getProperty("utm_source").toString();
		    			    String new_utm_notes = campaignName.getProperty("utm_notes").toString();*/
	    					// setting properties
	    					
		    				//campaignHolder.setProperty("utm_campaign", utm_campaign);
		    				//campaign.setProperty("utm_medium",new_ut_medium );
		    				//campaign.setProperty("utm_source", new_utm_source);
		    				//campaign.setProperty("utm_notes",new_utm_notes );
		    				//campaignHolder.setProperty("modifiedBy", myEmail);
		    				//campaignHolder.setProperty("modifiedAt", date);

	    			        // saving entity
	    			        //PMUtil.persistEntity(campaignHolder);
	    			       
	    					
	    				//}
	    			
		    			  //  PMUtil.persistEntity(campaign);
	    			        
		    			    
	    				
	    			}
	         //  CampaignHolderService.upCampaignHolder(old_utm_campaign, utm_campaign, longAccountId);
	    			returnUrl.append("/ManageCampaignHolders.jsp?aid=" + longAccountId + "&info= Campaign Saved Successfully");
	    	}
	    	
	    	else {
	    		//CREATE OR UPDATE
	    		String accountType = (String) myAccount.getProperty("type");
	    		int maxCampaigns =  AccountType.maxCampaignsMap.get(accountType);
	    		//int campaignsCount = CampaignService.getAllCampaignsCountFor(longAccountId);
	    		
	    		int mobCampaignsCount = MobileCampaignService
	    				.getAllMobileCampaignsCountFor(longAccountId);
	    		
	    		int webCampaignsCount = CampaignHolderService
	    				.getAllCampaignHoldersCountFor(longAccountId);
	    		
	    		/*int campaignsCount = CampaignService
	    				.getAllCampaignsCountFor(longAccountId);*/
	    		
	    		int campaignsCount = mobCampaignsCount + webCampaignsCount;


	        	//validate required field
		    	if ("".equals(utm_campaign)) errors.add(Messages.CAMPAIGN_REQUIRED);
		    	//if ("".equals(utm_medium)) errors.add(Messages.MEDIUM_REQUIRED);
		    	//if ("".equals(utm_source)) errors.add(Messages.SOURCE_REQUIRED);

		    	if (0==longCampaignId && campaignsCount >= maxCampaigns)
	        		errors.add(Messages.EXCEED_MAX_CAMPAIGNS);

		    	if (errors.size()>0) {
		    		returnUrl.append("/EditCampaign.jsp?aid=" + longAccountId + "&cid=" + longCampaignId);
		    		returnUrl.append("&utm_campaign=" + HtmlUtil.encode(utm_campaign) + "&utm_medium=" + utm_medium + "&utm_source=" + utm_source);
		    		for (String error:errors) {
		    			returnUrl.append("&err=" + error);
		    		}
		    	} else {
		            // creating/ updating campaign entity
		        	Entity campaign = CampaignHolderService.createOrUpdateCampaignHolder(myEmail, longAccountId, longCampaignId, utm_campaign, utm_medium, utm_source, utm_notes);
		        	returnUrl.append("/ManageTaggedURLs.jsp?aid=" + longAccountId + "&cid=" + campaign.getKey().getId()+"&cname=" + utm_campaign + "&info=" + Messages.INFO_SAVED + size);
		    	}
		    }
    	}
		resp.sendRedirect (returnUrl.toString());
		
    }
}
