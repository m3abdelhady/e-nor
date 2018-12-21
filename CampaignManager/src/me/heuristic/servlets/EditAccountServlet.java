package me.heuristic.servlets;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;



/*import javax.mail.Session;*/
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import me.heuristic.util.HtmlUtil;
import me.heuristic.braintree.engine.BrainTreeEngine;
import me.heuristic.references.Messages;
import me.heuristic.references.UserType;
import me.heuristic.services.AccountService;



/*import com.braintreegateway.BraintreeGateway;*/
import com.google.appengine.api.datastore.Entity;
/*import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

import java.util.Date;

import static java.util.concurrent.TimeUnit.*;*/

@SuppressWarnings("serial")
public class EditAccountServlet extends HttpServlet {
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
    	Entity myAccount = null;
    	//String myEmail = "";
    	//String myEmail = HtmlUtil.handleNullWhiteSpaces(req.getParameter("myEmail"));
    	String myRole = "";
        List<String> errors = new ArrayList<String>();
    	StringBuilder returnUrl = new StringBuilder();    	

        // retrieve form elements
    	String act = HtmlUtil.handleNullWhiteSpaces(req.getParameter("act"));
        long longAccountId = HtmlUtil.handleNullforLongNumbers(req.getParameter("aid"));
    	String emailAddress = HtmlUtil.handleNullWhiteSpaces(req.getParameter("email"));
    	String firstName = HtmlUtil.handleNullWhiteSpaces(req.getParameter("fname"));
    	String lastName = HtmlUtil.handleNullWhiteSpaces(req.getParameter("lname"));
    	String company = HtmlUtil.handleNullWhiteSpaces(req.getParameter("company"));
    	String accountName = HtmlUtil.handleNullWhiteSpaces(req.getParameter("name"));
    	String address = HtmlUtil.handleNullWhiteSpaces(req.getParameter("address"));
    	String city = HtmlUtil.handleNullWhiteSpaces(req.getParameter("city"));
    	String state = HtmlUtil.handleNullWhiteSpaces(req.getParameter("state"));
    	String zip = HtmlUtil.handleNullWhiteSpaces(req.getParameter("zip"));
    	String country = HtmlUtil.handleNullWhiteSpaces(req.getParameter("country"));
    	String phone = HtmlUtil.handleNullWhiteSpaces(req.getParameter("phone"));
    	String website = HtmlUtil.handleNullWhiteSpaces(req.getParameter("website"));
    	String agreement = HtmlUtil.handleNullWhiteSpaces(req.getParameter("agreement"));
    	String planid = HtmlUtil.handleNullWhiteSpaces(req.getParameter("pid"));
    	String urlParameters =
    	        "company=" + URLEncoder.encode(company, "UTF-8") +
    	        "&accountName=" + URLEncoder.encode(accountName, "UTF-8")+
    	        "&address=" + URLEncoder.encode(address, "UTF-8")+
    	        "&city=" + URLEncoder.encode(city, "UTF-8")+
    	        "&state=" + URLEncoder.encode(state, "UTF-8")+
    	        "&zip=" + URLEncoder.encode(zip, "UTF-8")+
    	        "&country=" + URLEncoder.encode(country, "UTF-8")+
    	        "&phone=" + URLEncoder.encode(phone, "UTF-8")+
    	        "&website=" + URLEncoder.encode(website, "UTF-8")+
    	        "&email=" + URLEncoder.encode(emailAddress, "UTF-8")+
    	        "&planid=" + URLEncoder.encode(planid, "UTF-8")+
    	        "&firstName=" + URLEncoder.encode(firstName, "UTF-8")+
    	        "&lastName=" + URLEncoder.encode(lastName, "UTF-8");
    	
    	String targetURL = "http://marketing.e-nor.com/acton/eform/23616/0001/d-ext-0001";
    	double np = 0;
    	switch(planid){
    	case "Basic":
    		np = 1188.0;
    		break;
    	case "Professional":
    		np = 5988.0;
    		break;
    	
    	}
    	//myEmail = HtmlUtil.handleNullWhiteSpaces(req.getParameter("myEmail"));
    	
    	
    	/*Date previous = (Date)myAccount.getProperty("startDate"); 
    	Date now = new Date();
    	
    	long MAX_DURATION = MILLISECONDS.convert(30, DAYS);

    	long duration = now.getTime() - previous.getTime();
*/

    	if (!Messages.ACTION_DELETE.equalsIgnoreCase(act)) {
    		if (0==longAccountId)
    			act = Messages.ACTION_CREATE;
    		else
    			act = Messages.ACTION_UPDATE;
    	}
    	//UserService userService = UserServiceFactory.getUserService();
        //User user = userService.getCurrentUser();

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
    		    	if (null==myRole) {
    		    		errors.add(Messages.NO_PERMISSION);
    		    	} else if (!UserType.hasPermission(myRole, UserType.PERMISSION_MANAGE_ACCOUNT) && !myEmail.contains("webmaster@campaignalyzer.com")) {
    		    		errors.add(Messages.NO_PERMISSION);
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
        	// DELETE
    		if (Messages.ACTION_DELETE.equalsIgnoreCase(act)) {
				//BrainTreeEngine.getInstance().cancelSubscribtion(""+longAccountId);				
				if(myAccount.getProperty("type").toString().contentEquals("Basic") || myAccount.getProperty("type").toString().contentEquals("Professional")){					
					BrainTreeEngine.getInstance().cancelSubscribtion(""+longAccountId);
				}	
				AccountService.deleteAccount(myAccount);	
								
				
	    		//refund if in free trial
	    		/*if(duration <= MAX_DURATION){
	    		BrainTreeEngine.getInstance().refundDuringFreeTrial(""+longAccountId);
	    		}*/
	    		returnUrl.append("/ManageAccounts.jsp?info=" + Messages.INFO_DELETED);
    		
	    		// CREATE OR UPDATE
	    	} else {
		
		    	// validate required field
		    	if ("".equals(emailAddress))
		    		errors.add(Messages.EMAIL_REQUIRED);
		    	else if (!HtmlUtil.isMatchingRegex(emailAddress, HtmlUtil.EMAIL_PATTERN))
	        		errors.add(Messages.MALFORMATTED_EMAIL);
		    	if ("".equals(firstName))		errors.add(Messages.FNAME_REQUIRED);
		    	if ("".equals(lastName))	errors.add(Messages.LNAME_REQUIRED);
		    	if ("".equals(company))		errors.add(Messages.COMPANY_REQUIRED);
		    	if ("".equals(accountName))		errors.add(Messages.ANAME_REQUIRED);
		    	if ("".equals(address))		errors.add(Messages.ADDRESS_REQUIRED);
		    	if ("".equals(city))	errors.add(Messages.CITY_REQUIRED);
		    	if ("".equals(state))		errors.add(Messages.STATE_REQUIRED);
		    	if ("".equals(zip))		errors.add(Messages.ZIP_REQUIRED);
		    	if ("".equals(country))		errors.add(Messages.COUNTRY_REQUIRED);
		    	if ("".equals(phone))		errors.add(Messages.PHONE_REQUIRED);
		    	if ("".equals(website))		errors.add(Messages.WEBSITE_REQUIRED);
		    	if ("".equals(agreement))		errors.add(Messages.AGREEMENT_REQUIRED);

//		    	if (AccountService.hasUserExceededMaxFreeAccounts(myEmail))
//	        		errors.add(Messages.EXCEED_MAX_FREE_ACCOUNTS);

		    	if (errors.size()>0 && !myEmail.contains("webmaster@campaignalyzer.com") ) {
	    			returnUrl.append("/EditAccount.jsp?aid=" + longAccountId);

		    		returnUrl.append("&email=" + emailAddress + "&fname=" + firstName + "&lname=" + lastName);
		    		returnUrl.append("&company=" + company + "&name=" + accountName + "&address=" + address);
		    		returnUrl.append("&city=" + city + "&state=" + state + "&zip=" + zip + "&country=" + country);
		    		returnUrl.append("&phone=" + phone + "&website=" + website + "&agreement=" + agreement);
		    		for (String error:errors) {
		    			returnUrl.append("&err=" + error);
		    		}
		    	} else {
		            // creating or updating account entity
		    		AccountService.executePost(targetURL, urlParameters);
		    		Entity account = AccountService.createOrUpdateAccount(myEmail, longAccountId, emailAddress, firstName, lastName, company, accountName, address, city, state, zip, country, phone, website, agreement);
		    		if (0==longAccountId){
		    			//returnUrl.append("/EditAccountPlan.jsp?pid="+planid+"&aid=" + account.getKey().getId());
		    			if(planid != ""){
		    				returnUrl.append("/Payment.jsp?pid="+planid+"&aid=" + account.getKey().getId()+"&update=null&np="+np);
		    			}
		    			else
		    				returnUrl.append("/dashboard.jsp?aid=" + account.getKey().getId());
//			    			returnUrl.append("/ManageAccounts.jsp?info=" + Messages.INFO_SAVED);
//		    			else{returnUrl.append("/EditAccountPlan.jsp?pid="+planid+"&aid=" + account.getKey().getId());}
		    		}
		    		
		    		/*if (0==longAccountId)
		    			returnUrl.append("/EditAccountPlan.jsp?pid="+planid+"&aid=" + account.getKey().getId());*/
		    		else
		    			returnUrl.append("/ManageAccounts.jsp?info=" + Messages.INFO_SAVED);
		    	}
		    }
    	}       
    	resp.sendRedirect(returnUrl.toString());
    }
}
