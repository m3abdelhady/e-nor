package me.heuristic.servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import me.heuristic.util.HtmlUtil;
import me.heuristic.references.Messages;
import me.heuristic.references.UserType;
import me.heuristic.services.AUserService;
import me.heuristic.services.AccountService;

import com.google.appengine.api.datastore.Entity;
/*import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;*/
import com.google.appengine.labs.repackaged.com.google.common.collect.Iterables;

@SuppressWarnings("serial")
public class SecurityCheck extends HttpServlet {
    public void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
    	doPost(req, resp);
    }

    public void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
    	HttpSession session = req.getSession(true);
    	Entity myAccount = null;
    	String myEmail = req.getParameter("myEmail");
    	String myPhoto = req.getParameter("myPhoto");
    	String myRole ="";
    	String action = HtmlUtil.handleNullWhiteSpaces(req.getParameter("createAction"));
    	String planID = HtmlUtil.handleNullWhiteSpaces(req.getParameter("planID"));
    		session.setAttribute("userEmail", myEmail);
    		session.setAttribute("userPhoto", myPhoto);
    		
    		Iterable<Entity> accounts = AccountService.getActiveAccountsForUser(myEmail);
    		Iterables.getFirst(accounts, null);
    		Entity firstAccont = Iterables.getFirst(accounts, null);
    		//String myEmail = HtmlUtil.handleNullWhiteSpaces(req.getSession().getAttribute("userEmail").toString());
	    	if(myEmail == "webmaster@campaignalyzer.com"){
    		myRole = "ADMIN";}
	    	session.setAttribute("adminRole", myRole);
	    	List<String> errors = new ArrayList<String>();
	    	StringBuilder returnUrl = new StringBuilder();
    	
    	//if(null != errors){
    		
    	if(AccountService.getAllMyAccountsCountFor(myEmail) != 0) {
    		if (action == "" && "" == planID){
    		if(0 != AccountService.getAllAccountsCountFor(myEmail)){
    			returnUrl.append("/dashboard.jsp?aid="+ firstAccont.getKey().getId());	
    		}
    		else{returnUrl.append("/ManageAccounts.jsp");}
    		
    		
    	}  
    		else if(action !="" || planID != ""){
    			returnUrl.append("/NewAccount.jsp?pid="+planID);
    			
    			}

    	}
    	else{
			returnUrl.append("/NewAccount.jsp");
			
			} 		
    		
    	//}
	    	/*if(AccountService.getAllMyAccountsCountFor(myEmail) != 0) {
	    		if (action == "" && planID ==""){
	    		if(0 != AccountService.getAllAccountsCountFor(myEmail)){
	    			returnUrl.append("/dashboard.jsp?aid="+ firstAccont.getKey().getId());	
	    		}
	    		else{returnUrl.append("/ManageAccounts.jsp");}
	    		
	    		
	    	}  else if(planID != "" || planID !=null || planID !="null"){
	    		
	    		returnUrl.append("/NewAccount.jsp?pid="+planID);
	    	}
	    		
	    		

	    	}
	    	else if(planID == "" || planID !=null || planID !="null"){
				returnUrl.append("/NewAccount.jsp");
				
				}*/
	    	
	    	
    	resp.sendRedirect(returnUrl.toString());
    }
    
}       
