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

import com.google.appengine.api.datastore.Entity;
/*import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

import com.google.appengine.api.oauth.OAuthRequestException;
import com.google.appengine.api.oauth.OAuthService;
import com.google.appengine.api.oauth.OAuthServiceFactory;*/

@SuppressWarnings("serial")
public class EditAccountPlanServlet extends HttpServlet {
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
        long longAccountId = HtmlUtil.handleNullforLongNumbers(req.getParameter("aid"));

    	//UserService userService = UserServiceFactory.getUserService();
        //User user = userService.getCurrentUser();
        
        //OAuthService userService = OAuthServiceFactory.getOAuthService();
        

        // make sure there's a user
        // if editing or deleting, make sure it's a valid account and the user has permission
        if (""==myEmail) {
    		errors.add(Messages.UNRECOGNIZED_USER);
    	} else {
    		//myEmail = userService.getCurrentUser().getEmail();

    		if (0!=longAccountId) {
    			// if editing an existing account
    		    myAccount = AccountService.getSingleAccount(longAccountId);
    		    if (null==myAccount) {
    		    	errors.add(Messages.NO_SUCH_ACCOUNT);
    			} else {
    		    	myRole = (String) myAccount.getProperty(myEmail);
    		    	//if (null==myRole) {
    		    	//	errors.add(Messages.NO_PERMISSION);
    		    	//} else 
    		    		if (!UserType.hasPermission(myRole, UserType.PERMISSION_MANAGE_ACCOUNT) && !myEmail.contains("webmaster@campaignalyzer.com")) {
    		    		errors.add(Messages.NO_PERMISSION + myEmail);
    		    	}
    		    }
    		}
        }
        

        // start processing
        if (errors.size()>0 && !myEmail.contains("webmaster@campaignalyzer.com")) {
	        returnUrl.append("/ManageAccounts.jsp?");
    		for (String error:errors) {
    			returnUrl.append("&err=" + error);
    		}
    	} else {
	    	//if (AccountService.hasUserExceededMaxFreeAccounts(myEmail))
        		//errors.add(Messages.EXCEED_MAX_FREE_ACCOUNTS);

	    	if (errors.size()>0 && !myEmail.contains("webmaster@campaignalyzer.com")) {
	    		returnUrl.append("/EditAccountPlan.jsp?aid=" + longAccountId);
	    		for (String error:errors) {
	    			returnUrl.append("&err=" + error);
	    		}
	    	} else {
	            // creating or updating account entity
	    		//AccountService.makeAccountFree(myEmail, longAccountId);
	    		
	    		
	   			returnUrl.append("/ViewAccount.jsp?aid=" + longAccountId);
	    	}
    	}
    	resp.sendRedirect(returnUrl.toString());
    }
}
