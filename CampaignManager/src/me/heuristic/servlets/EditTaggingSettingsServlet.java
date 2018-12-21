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
import me.heuristic.services.TaggingSettingsService;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

@SuppressWarnings("serial")
public class EditTaggingSettingsServlet extends HttpServlet {
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
        long longAccountId = HtmlUtil.handleNullforLongNumbers(req.getParameter("aid"));
    	String conAttribution = HtmlUtil.handleNullWhiteSpaces(req.getParameter("conAttribution"));
    	String parSeparator = HtmlUtil.handleNullWhiteSpaces(req.getParameter("parSeparator"));
    	String urlShortener = HtmlUtil.handleNullWhiteSpaces(req.getParameter("urlShortener"));
    	String caseSensetive = HtmlUtil.handleNullWhiteSpaces(req.getParameter("caseSensetive"));

    	/*UserService userService = UserServiceFactory.getUserService();
        User user = userService.getCurrentUser();*/

        // make sure there's a user
        // if editing or deleting, make sure it's a valid account and the user has permission
        if (""==myEmail) {
    		errors.add(Messages.UNRECOGNIZED_USER);
    	} else {
    		//myEmail = user.getEmail();

			// if editing an existing account
		    myAccount = AccountService.getSingleAccount(longAccountId);
		    if (null==myAccount) {
		    	errors.add(Messages.NO_SUCH_ACCOUNT);
			} else {
		    	myRole = (String) myAccount.getProperty(myEmail);
		    	if (null==myRole) {
		    		errors.add(Messages.NO_PERMISSION);
		    	} else if (!UserType.hasPermission(myRole, UserType.PERMISSION_EDIT_TAGGING_SETTINGS)) {
		    		errors.add(Messages.NO_PERMISSION);
		    	}
		    }
        }

        // start processing
        if (errors.size()>0) {
	        returnUrl.append("/ManageAccounts.jsp?");
    		for (String error:errors) {
    			returnUrl.append("&err=" + error);
    		}
    	} else {
    		// update

			TaggingSettingsService.updateTaggingSettings(myEmail, myAccount, conAttribution, parSeparator, urlShortener, caseSensetive);
			returnUrl.append("/EditTaggingSettings.jsp?aid="+ longAccountId + "&info=" + Messages.INFO_SAVED);
    	}
    	resp.sendRedirect(returnUrl.toString());
    }
}
