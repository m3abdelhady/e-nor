package me.heuristic.servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import me.heuristic.util.HtmlUtil;
import me.heuristic.references.Messages;
import me.heuristic.references.UserType;
import me.heuristic.services.AccountService;
import me.heuristic.services.CampaignService;
import me.heuristic.services.TaggedUrlService;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

@SuppressWarnings("serial")
public class EditTaggedUrlServlet extends HttpServlet {

    public void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
    	doPost(req, resp);
    }

    public void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

    	Entity myAccount = null;
    	Entity myCampaign = null;   
    	String cName="";
    	//String myEmail = "";
    	String myRole = "";
    	List<String> errors = new ArrayList<String>();
    	StringBuilder returnUrl = new StringBuilder();
    	
    	String myEmail = HtmlUtil.handleNullWhiteSpaces(req.getSession().getAttribute("userEmail").toString());
        // retrieve form elements
    	String act = HtmlUtil.handleNullWhiteSpaces(req.getParameter("act"));
        long longAccountId = HtmlUtil.handleNullforLongNumbers(req.getParameter("aid"));
        long longCampaignId = HtmlUtil.handleNullforLongNumbers(req.getParameter("cid"));
        long longTaggedUrlId = HtmlUtil.handleNullforLongNumbers(req.getParameter("tid"));
    	String utm_url = HtmlUtil.handleNullWhiteSpaces(req.getParameter("utm_url"));
    	String utm_term = HtmlUtil.handleNullWhiteSpaces(req.getParameter("utm_term"));
    	String utm_content = HtmlUtil.handleNullWhiteSpaces(req.getParameter("utm_content"));
    	String utm_extra = HtmlUtil.handleNullWhiteSpaces(req.getParameter("utm_extra"));
    	myCampaign = CampaignService.getSingleCampaign(longAccountId, longCampaignId);
		cName = (String) myCampaign.getProperty("utm_campaign");
		String[] sTaggedUrlIds = req.getParameterValues("tid");

    	/*UserService userService = UserServiceFactory.getUserService();
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
		    	} else if (!UserType.hasPermission(myRole, UserType.PERMISSION_MANAGE_TAGGED_URLS)){
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
    		returnUrl.append("/ManageTaggedURLs.jsp?aid=" + longAccountId + "&cid=" + longCampaignId+"&cname="+cName);
	    	if (Messages.ACTION_DELETE.equalsIgnoreCase(act)) {
	        	// DELETE
	    		Entity myTaggedUrl = TaggedUrlService.getSingleTaggedUrl(longAccountId, longCampaignId, longTaggedUrlId);
	    		TaggedUrlService.deleteTaggedUrl(myTaggedUrl);
	    		returnUrl.append("&info=" + Messages.INFO_DELETED);
		    } 
	    	else if(Messages.ACTION_DELETEAll.equalsIgnoreCase(act)){
	    		Entity myTaggedUrl;
	    		
	    		if (null==sTaggedUrlIds)
					errors.add(Messages.NO_TAGGEDURLS);
	    		
	    		if (errors.size()>0) {
					//returnUrl.append("/ManageTaggedURLs.jsp?aid="  + longAccountId + "&cid=" + longCampaignId);
					for (String error:errors) {
						returnUrl.append("&err=" + error);
					}
					//returnUrl.append("&err=" + errors);
					//returnUrl.append("/ManageCampaigns.jsp?aid=" + longAccountId + "&err=" + errors);
	    		}else{
	    			for(int i=0; i< sTaggedUrlIds.length; i++){
	    			   // myCampaign = CampaignService.getSingleCampaign(longAccountId, Long.parseLong(sTaggedUrlIds[i]));
	    			    myTaggedUrl = TaggedUrlService.getSingleTaggedUrl(longAccountId, longCampaignId, Long.parseLong(sTaggedUrlIds[i]));
	    			    TaggedUrlService.deleteTaggedUrl(myTaggedUrl);  
	    				
	    				
	    			} 
	    			returnUrl.append("&info=" + Messages.INFO_DELETED);
	    			
	    		}
	    	}
	    	else {
	    		//CREATE OR UPDATE
	
		    	// validate required field
			    if ("".equals(utm_url))
			    	errors.add(Messages.URL_REQUIRED);
	        	else if (!HtmlUtil.isMatchingRegex(utm_url, HtmlUtil.URL_PATTERN))
	        		errors.add(Messages.MALFORMATTED_URL);
		 
		    	if (errors.size()>0) {
		    			    	
		    		returnUrl.append("&tid=" +longTaggedUrlId + "&utm_url=" + utm_url +
		    				"&utm_term=" + utm_term + "&utm_content=" + utm_content + "&utm_extra=" + utm_extra+ "&cname=" + cName);
		    		for (String error:errors) {
		    			returnUrl.append("&err=" + error);
		    		}
		    	} else {
		            // creating/ updating tagged URL entity

		    		if (!utm_url.startsWith("http://") && !utm_url.startsWith("https://"))
				    	utm_url = "http://" + utm_url;

				    // save
				    TaggedUrlService.createOrUpdateTaggedUrl(myEmail, longAccountId, longCampaignId, longTaggedUrlId,
		    				utm_url, utm_term, utm_content, utm_extra);
		
		    		returnUrl.append("&cname=" + cName +"&info=" + Messages.INFO_SAVED);
		    	}
		    }
    	}
		resp.sendRedirect (returnUrl.toString());
    }
}
