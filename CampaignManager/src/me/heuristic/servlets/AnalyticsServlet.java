package me.heuristic.servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;





/*import javax.mail.Session;*/
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import me.heuristic.util.HtmlUtil;
/*import me.heuristic.braintree.engine.BrainTreeEngine;*/
import me.heuristic.references.Messages;
import me.heuristic.references.UserType;
import me.heuristic.services.AccountService;





/*import com.braintreegateway.BraintreeGateway;*/
import com.google.appengine.api.datastore.Entity;
/*import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;*/
/*import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

import java.util.Date;

import static java.util.concurrent.TimeUnit.*;*/
/*import com.google.appengine.api.users.UserServiceFactory;*/

@SuppressWarnings("serial")
public class AnalyticsServlet extends HttpServlet {
    public void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
    	doPost(req, resp);
    }

    public void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
    	//Object value = req.getSession().getAttribute("userEmail");
    	//String myEmail = HtmlUtil.handleNullWhiteSpaces(req.getSession().getAttribute("userEmail").toString());
    	//String myEmail = HtmlUtil.handleNullWhiteSpaces(req.getParameter("myEmail"));
    	String myEmail = HtmlUtil.handleNullWhiteSpaces(req.getSession().getAttribute("userEmail").toString());
    	String myToken = HtmlUtil.handleNullWhiteSpaces(req.getParameter("myToken"));
    	Entity myAccount = null;
    	//String myEmail = "";
    	//String myEmail = HtmlUtil.handleNullWhiteSpaces(req.getParameter("myEmail"));
    	String myRole = "";
        List<String> errors = new ArrayList<String>();
    	StringBuilder returnUrl = new StringBuilder();    	

        // retrieve form elements
    	//String act = HtmlUtil.handleNullWhiteSpaces(req.getParameter("act"));
        long longAccountId = HtmlUtil.handleNullforLongNumbers(req.getParameter("aid"));
    	
    	
    	//myEmail = HtmlUtil.handleNullWhiteSpaces(req.getParameter("myEmail"));
    	
    	
    	/*Date previous = (Date)myAccount.getProperty("startDate"); 
    	Date now = new Date();
    	
    	long MAX_DURATION = MILLISECONDS.convert(30, DAYS);

    	long duration = now.getTime() - previous.getTime();
*/

    	
    	/*UserService userService = UserServiceFactory.getUserService();
        User user = userService.getCurrentUser();*/

        // make sure there's a user
        // if editing or deleting, make sure it's a valid account and the user has permission
        if (""==myEmail) {
    		errors.add(Messages.UNRECOGNIZED_USER);
    	} else {
    		if (0!=longAccountId) {
    			// if editing an existing account
    		    myAccount = AccountService.getSingleAccount(longAccountId);
    		    if (null==myAccount) {
    		    	errors.add(Messages.NO_SUCH_ACCOUNT);
    			} else {
    		    	myRole = (String) myAccount.getProperty(myEmail);
    		    	myToken = (String) myAccount.getProperty("myToken");
    		    	if (null==myRole) {
    		    		errors.add(Messages.NO_PERMISSION);
    		    	} else if (!UserType.hasPermission(myRole, UserType.PERMISSION_MANAGE_ACCOUNT) && !myEmail.contains("webmaster@campaignalyzer.com")) {
    		    		errors.add(Messages.NO_PERMISSION);
    		    	}
    		    	
    		    	if(null == myToken){
    		    		returnUrl.append("/AnalyticsSettings.jsp");
    		    		
    		    	}
    		    	else{
    		    		
    		    		returnUrl.append("/AnalyticsCallBack?myToken="+myToken);
    		    	}
    		    }
    		}
        }

          
    	resp.sendRedirect(returnUrl.toString());
    }
}
