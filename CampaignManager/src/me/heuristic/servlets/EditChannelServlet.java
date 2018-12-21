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
import me.heuristic.services.ChannelService;
import me.heuristic.services.AccountService;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

@SuppressWarnings("serial")
public class EditChannelServlet extends HttpServlet {
    public void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
    	doPost(req, resp);
    }

    public void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

    	Entity myAccount = null;
    	//String myEmail = "";
    	String myEmail = HtmlUtil.handleNullWhiteSpaces(req.getSession().getAttribute("userEmail").toString());
    	String myRole = "";
    	List<String> errors = new ArrayList<String>();
    	StringBuilder returnUrl = new StringBuilder();
    	
        // retrieve form elements
    	String act = HtmlUtil.handleNullWhiteSpaces(req.getParameter("act"));
        long longAccountId = HtmlUtil.handleNullforLongNumbers(req.getParameter("aid"));
    	String[] availChannels = req.getParameterValues("availChannels");
    	String[] selChannels = req.getParameterValues("selChannels");

    	//UserService userService = UserServiceFactory.getUserService();
        //User user = userService.getCurrentUser();

        // validation
        // make sure there's a user
        // make sure it's a valid account and the user has permission
        // make sure no required field is missing
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
		    	} else if (!UserType.hasPermission(myRole, UserType.PERMISSION_MANAGE_CHANNELS)){
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
        	returnUrl.append("/ManageChannels.jsp?aid=" + longAccountId);
	        if (null!=act && act.contains(Messages.ACTION_REMOVE)) {
	    		// DELETE

    	    	// validate selected channels
	        	ChannelService.validateSelChannels(errors, myAccount, selChannels);

    	    	if (errors.size()>0) {
		    		for (String error:errors)
		    			returnUrl.append("&err=" + error);
    	    	} else {
		        	ChannelService.deleteAccountChannels(myEmail, myAccount, selChannels);	        			
		    		returnUrl.append("&info=" + Messages.INFO_DELETED);
    	    	}
	    	} else {
            	// adding account channels

    	    	// validate available channels
        		ChannelService.validateAvailChannels(errors, myAccount, availChannels);

	        	if (errors.size()>0) {
		    		for (String error:errors)
		    			returnUrl.append("&err=" + error);
		        } else {
		    		ChannelService.addAccountChannels(myEmail, myAccount, availChannels);
	    	        returnUrl.append("&info=" + Messages.INFO_SAVED);
		        }
	    	}
    	}
		resp.sendRedirect (returnUrl.toString());
    }
}
