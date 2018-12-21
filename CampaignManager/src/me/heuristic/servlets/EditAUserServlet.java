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
import me.heuristic.services.AUserService;
import me.heuristic.services.AccountService;

import com.google.appengine.api.datastore.Entity;
/*import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;*/

@SuppressWarnings("serial")
public class EditAUserServlet extends HttpServlet {
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
    	String oldUserEmail = HtmlUtil.handleNullWhiteSpaces(req.getParameter("oldUserEmail"));
		String newUserEmail = HtmlUtil.handleNullWhiteSpaces(req.getParameter("newUserEmail"));
		newUserEmail = newUserEmail.toLowerCase();
    	String newUserRole = HtmlUtil.handleNullWhiteSpaces(req.getParameter("newUserRole"));

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
		    	} else if (!UserType.hasPermission(myRole, UserType.PERMISSION_MANAGE_USERS)){
		    		errors.add(Messages.NO_PERMISSION);
		    	}
		    }
        }

		String oldUserRole = (String) myAccount.getProperty(oldUserEmail);

		if (errors.size()>0) {
	        returnUrl.append("/ManageAccounts.jsp?");
    		for (String error:errors) {
    			returnUrl.append("&err=" + error);
    		}
    	} else {
    		// DELETE
	        if (Messages.ACTION_DELETE.equalsIgnoreCase(act)) {
	        	if (UserType.ADMIN.equalsIgnoreCase(oldUserRole) && AUserService.isOnlyAdmin(myAccount, oldUserEmail) ) {
	        		returnUrl.append("/ManageAUsers.jsp?aid=" + longAccountId + "&err=" + Messages.AT_LEAST_1_ADMIN);
	        	} else {
	        		AUserService.deleteAccountUser(myEmail, myAccount, oldUserEmail);
	        		if (myEmail.equalsIgnoreCase(oldUserEmail))
		        		returnUrl.append("/ManageAccounts.jsp?aid=" + longAccountId + "&info=" + Messages.INFO_DELETED);
		        	else
		        		returnUrl.append("/ManageAUsers.jsp?aid=" + longAccountId + "&info=" + Messages.INFO_DELETED);
	        	}
    		// CREATE OR UPDATE
	    	} else {

	    		//validate required field
	    		// empty email
	        	if ("".equals(newUserEmail))
	        		errors.add(Messages.EMAIL_REQUIRED);
	        	// malformatted email
	        	else if (!HtmlUtil.isMatchingRegex(newUserEmail, HtmlUtil.EMAIL_PATTERN))
	        		errors.add(Messages.MALFORMATTED_EMAIL);
	        	// redundant  email
	        	else if (!oldUserEmail.equalsIgnoreCase(newUserEmail) && null!=oldUserRole)
	        		errors.add(Messages.REDUNDANT_EMAIL);

	        	// create
	        	if ("".equals(oldUserEmail)) {
	        		if (AUserService.hasExceededMaxUsers(myAccount))
	        			errors.add(Messages.EXCEED_MAX_USERS);
	        	// edit
	        	} else {
	        		// if changing role from admin to something else
	        		// and is only admin
	        		if (UserType.ADMIN.equalsIgnoreCase(oldUserRole) &&
		        		!UserType.ADMIN.equalsIgnoreCase(newUserRole) &&
		        		AUserService.isOnlyAdmin(myAccount, oldUserEmail))
		        		errors.add(Messages.AT_LEAST_1_ADMIN);
	        	}

	        	if (errors.size()>0) {
	        		returnUrl.append("/ManageAUsers.jsp?aid=" + longAccountId);
	        		returnUrl.append("&oldUserEmail=" + oldUserEmail + "&newUserEmail=" + newUserEmail + "&newUserRole=" + newUserRole);
	        		for (String error:errors) {
	        			returnUrl.append("&err=" + error);
	        		}
	        	} else {
	            	// creating/ updating account user
	    	    	AUserService.createOrUpdateAccountUser(myEmail, myAccount, oldUserEmail, newUserEmail, newUserRole);
	        		returnUrl.append("/ManageAUsers.jsp?aid=" + longAccountId + "&info=" + Messages.INFO_SAVED);
	            }
	    	}
    	}
		resp.sendRedirect (returnUrl.toString());
    }
}
