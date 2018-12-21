package me.heuristic.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import me.heuristic.braintree.engine.BrainTreeEngine;
import me.heuristic.services.OrderService;

import com.braintreegateway.WebhookNotification;

public class BrainTreeWebHook extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		WebhookNotification webhookNotification = BrainTreeEngine
				.getInstance()
				.getGateway()
				.webhookNotification()
				.parse(req.getParameter("bt_signature"),
						req.getParameter("bt_payload"));
		System.out.println("[Webhook Received "
				+ webhookNotification.getTimestamp().getTime() + "] | Kind: "
				+ webhookNotification.getKind() + " | Subscription: "
				+ webhookNotification.getSubscription().getId());
		Random rn = new Random();
		int rnd = rn.nextInt(1000000);
		
		String OrderId=webhookNotification.getSubscription().getId()+"_"+rnd;
		OrderService.recordNotification(OrderId,webhookNotification);
		switch(webhookNotification.getKind())
		{
		case SUBSCRIPTION_CHARGED_SUCCESSFULLY:
			OrderService.handleApprovedOrder(OrderId,webhookNotification);
			break;
		/*case SUBSCRIPTION_CHARGED_UNSUCCESSFULLY:
			OrderService.handleApprovedOrder(OrderId,webhookNotification);
			break;*/
		case SUBSCRIPTION_CANCELED:
			//no need as the cancellation is done n subscription			
			break;
		default:
			break;
		}
		
		resp.setStatus(200);
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		// super.doGet(req, resp);
		resp.setContentType("text/html");
		String res = BrainTreeEngine.getInstance().getGateway()
				.webhookNotification().verify(req.getParameter("bt_challenge"));
		PrintWriter out = resp.getWriter();
		out.println(res);
		out.close();
	}

}
