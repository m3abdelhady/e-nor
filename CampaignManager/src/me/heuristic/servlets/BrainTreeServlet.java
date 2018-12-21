package me.heuristic.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import me.heuristic.braintree.engine.BrainTreeEngine;
import me.heuristic.braintree.engine.exceptions.CustomerCreationException;
import me.heuristic.braintree.engine.exceptions.PaymntMethodCreationException;
import me.heuristic.braintree.engine.exceptions.SubscriptionCreationException;
import me.heuristic.references.Messages;
import me.heuristic.services.AccountService;
/*import me.heuristic.services.OrderService;*/
import me.heuristic.util.HtmlUtil;
import me.heuristic.util.MailUtil;
import me.heuristic.util.PMUtil;

/*import com.braintreegateway.WebhookNotification;*/
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
/*import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;*/

public class BrainTreeServlet extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		String nonce = req.getParameter("payment_method_nonce");
		String aid = req.getParameter("aid");
		String pid = req.getParameter("pid");
		String update = req.getParameter("update");
		/*UserService userService = UserServiceFactory.getUserService();
        User user = userService.getCurrentUser();*/
        String monthlyPay = req.getParameter("np");
       
        
		System.out.println(nonce);
		
		/*
		 * TransactionRequest request = new TransactionRequest().amount( new
		 * BigDecimal("100.00")).paymentMethodNonce( "nonce-from-the-client");
		 * 
		 * Result<Transaction> result =
		 * BrainTreeEngine.getInstance().getGateway()
		 * .transaction().sale(request);
		 */
		String url = "";

		long longAccountId = HtmlUtil.handleNullforLongNumbers(req
				.getParameter("aid"));
		Entity myAccount = AccountService.getSingleAccount(longAccountId);
		if (myAccount != null && (nonce!=null && !nonce.isEmpty())) {

			try {
				if (update.equals("1")) {
					
					Key key = KeyFactory.createKey("BrainTreeCustomer", longAccountId);
					Entity BrainTreeCustomer= PMUtil.findEntity(key);
					String customerId=(String)BrainTreeCustomer.getProperty("CustomerId");
					//String customerName = (String)myAccount.getProperty("fname");
					
					BrainTreeEngine.getInstance().updateSubscribtion(
							(String) myAccount.getProperty("fname"),
							(String) myAccount.getProperty("lname"), 
							(String) myAccount.getProperty("email"),
							nonce,
							aid, pid,customerId);
					//update the plan lable 23/9
					myAccount.setProperty("type", pid);
					PMUtil.persistEntity(myAccount);
					//
					MailUtil.sendChangePlanMail((String) myAccount.getProperty("email"), (String) myAccount.getProperty("fname"), pid);
					
				} else {
					String customerId=BrainTreeEngine.getInstance().createSubscribtion(
							(String) myAccount.getProperty("fname"),
							(String) myAccount.getProperty("lname"), 
							(String) myAccount.getProperty("email"),
							nonce,
							aid, pid);
					
					//store customer id
					Entity BrainTreeCustomer= new Entity("BrainTreeCustomer",longAccountId);
	
					BrainTreeCustomer.setProperty("CustomerId", customerId);
					BrainTreeCustomer.setProperty("firstName", (String)myAccount.getProperty("fname"));
					BrainTreeCustomer.setProperty("lastName", (String)myAccount.getProperty("lname"));
					BrainTreeCustomer.setProperty("Email", (String)myAccount.getProperty("email"));
					
					//String myEmail = user.getEmail();
					String myEmail = HtmlUtil.handleNullWhiteSpaces(req.getSession().getAttribute("userEmail").toString());
					AccountService.makeAccountFreeTrial(myEmail, longAccountId, pid);
			        // saving entity
					PMUtil.persistEntity(BrainTreeCustomer);
					

				}
				
				//url = "/ManageAccounts.jsp?&info="+ Messages.INFO_WAITING_PAYMENT_APPROVAL;
				
				url = "/ManageAccounts.jsp?&info="+ Messages.INFO_WAITING_PAYMENT_APPROVAL;
				
				switch(pid){
				case "Basic": url = "/Confirmation.jsp?pid=basic&revenue="+monthlyPay+"&aid="+aid+"&info="+ Messages.INFO_WAITING_PAYMENT_APPROVAL;
				break;
				
				case "Professional":  url = "/Confirmation.jsp?pid=professional&revenue="+monthlyPay+"&aid="+aid+"&info="+ Messages.INFO_WAITING_PAYMENT_APPROVAL;
				break;
				
				default:
				}
			} catch (SubscriptionCreationException e) {
				// TODO Auto-generated catch block
				url = "/Payment.jsp?aid="+longAccountId+"&pid="+pid+"&np="+monthlyPay+"&info="
						+ Messages.ERR_SUBSCRIPTION_CREATION;
			} catch (CustomerCreationException e) {
				// TODO Auto-generated catch block
				url = "/Payment.jsp?aid="+longAccountId+"&pid="+pid+"&np="+monthlyPay+"&info="
						+ Messages.ERR_BRAINTREE_CUSTOMER;

			} catch (PaymntMethodCreationException e) {
				// TODO Auto-generated catch block
				url = "/Payment.jsp?aid="+longAccountId+"&pid="+pid+"&np="+monthlyPay+"&info="
						+ Messages.ERR_PAYMENTMETHOD;
			}

		}
		else
		{
			url = "/Payment.jsp?aid="+longAccountId+"&pid="+pid+"&np="+monthlyPay+"&info="
					+ Messages.ERR_PAYMENTMETHOD;
		}

		System.out.println(nonce);
		resp.sendRedirect(url);
	}
}
