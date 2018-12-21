package me.heuristic.servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import me.heuristic.util.HtmlUtil;

import me.heuristic.services.AccountService;

import com.google.appengine.api.datastore.Entity;

import com.google.appengine.labs.repackaged.com.google.common.collect.Iterables;

@SuppressWarnings("serial")
public class SecurityCheck_CreateAccount extends HttpServlet {
    public void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
    	doPost(req, resp);
    }

    public void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
    	HttpSession session = req.getSession(true);
    	Entity myAccount = null;
    	//String myEmail = req.getParameter("myEmail");
    	String myEmail = HtmlUtil.handleNullWhiteSpaces(req.getSession().getAttribute("userEmail").toString());
    	String myPhoto = req.getParameter("myPhoto");
    	String action = HtmlUtil.handleNullWhiteSpaces(req.getParameter("createAction"));
    		//session.setAttribute("userEmail", myEmail);
    		//session.setAttribute("userPhoto", myPhoto);
    		
    		Iterable<Entity> accounts = AccountService.getActiveAccountsForUser(myEmail);
    		Iterables.getFirst(accounts, null);
    		Entity firstAccont = Iterables.getFirst(accounts, null);
    		
	    	String myRole = "";
	    	List<String> errors = new ArrayList<String>();
	    	StringBuilder returnUrl = new StringBuilder();
    	
    	//if(null != errors){
    		
    	if(AccountService.getAllMyAccountsCountFor(myEmail) != 0) {
    		if (action == ""){
    		if(0 != AccountService.getAllAccountsCountFor(myEmail)){
    			returnUrl.append("/dashboard.jsp?aid="+ firstAccont.getKey().getId());	
    		}
    		else{returnUrl.append("/ManageAccounts.jsp");}
    		
    		
    	}  
    		else{
    			returnUrl.append("/NewAccount.jsp");
    			
    			}

    	}
    	else{
			returnUrl.append("/NewAccount.jsp");
			
			} 		
    		
    	//}
    
    	resp.sendRedirect(returnUrl.toString());
    }
    
}       
