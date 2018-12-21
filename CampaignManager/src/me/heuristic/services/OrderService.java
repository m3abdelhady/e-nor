package me.heuristic.services;

import java.util.Calendar;
import java.util.Date;

import me.heuristic.util.PMUtil;

import com.braintreegateway.WebhookNotification;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

//import com.google.appengine.api.datastore.Text;

public class OrderService {
	public static Entity recordNotification(String orderId,
			WebhookNotification webhookNotification) {

		// creating id/name and notification entity
		Entity notifi = new Entity("Notification", orderId);
		Date date = new Date();
		notifi.setProperty("receivedAt", date);

		// setting properties
		notifi.setProperty("orderNumber", orderId);
		// Text notificationText= new Text(notification.toString());
		// notifi.setProperty("notification", notificationText);

		// saving entity
		PMUtil.persistEntity(notifi);
		return notifi;
	}

	public static void handleApprovedOrder(String OrderId,
			WebhookNotification webhookNotification) {

		Entity order = recordApprovedOrder(OrderId, webhookNotification);
		AccountService.updateAccountWithOrderData(order);
	}

	private static Entity recordApprovedOrder(String OrderId,
			WebhookNotification webhookNotification) {

		// creating id/name and account entity
		String orderNumber = OrderId;
		Entity order = new Entity("Order", orderNumber);

		String orderPlan = null;
		long longAccountId = 0l;
		String price = null;
		String shippingEmail = null;
		Calendar cal = Calendar.getInstance();
		order.setProperty("receivedAt", cal.getTime());

		if (webhookNotification != null) {
			orderPlan = webhookNotification.getSubscription().getPlanId();
			longAccountId = Long.parseLong(webhookNotification
					.getSubscription().getId());
			shippingEmail = null;
			price = webhookNotification.getSubscription().getPrice().toString();
			
			order.setProperty("startDate", webhookNotification.getSubscription().getBillingPeriodStartDate().getTime());
			order.setProperty("endDate", webhookNotification.getSubscription().getBillingPeriodEndDate().getTime());

		} else {
			longAccountId = Long.parseLong(OrderId);
			order.setProperty("startDate", cal.getTime());
			cal.add(Calendar.YEAR, 1);
			order.setProperty("endDate", cal.getTime());
		}
		// setting properties

		order.setProperty("orderNumber", orderNumber);
		order.setProperty("shippingEmail", shippingEmail);
		order.setProperty("price", price);
		order.setProperty("accountId", longAccountId);
		order.setProperty("accountType", orderPlan);
		// Text orderSummaryText = new Text(orderSummary.toString());
		// order.setProperty("orderSummary", orderSummaryText);

		// saving entity
		PMUtil.persistEntity(order);
		return order;
	}

	public static void handleSubscriptionCancellation(String OrderId,
			WebhookNotification webhookNotification) {
		recordCancellation(OrderId, webhookNotification);
		Entity order = updateOrderWithCancellation(OrderId, webhookNotification);
		AccountService.updateAccountWithOrderData(order);
	}

	private static Entity recordCancellation(String OrderId,
			WebhookNotification webhookNotification) {

		// creating id/name and notification entity
		String nSerialNumber = OrderId;
		Entity cancellation = new Entity("Cancellation", nSerialNumber);
		Date date = new Date();
		cancellation.setProperty("receivedAt", date);

		// setting properties
		cancellation.setProperty("orderNumber", OrderId);
		cancellation.setProperty("reason", webhookNotification
				.getSubscription().getStatus());

		// saving entity
		PMUtil.persistEntity(cancellation);
		return cancellation;
	}

	private static Entity updateOrderWithCancellation(String OrderId,
			WebhookNotification webhookNotification) {

		String orderNumber = OrderId;
		Entity order = getSingleOrder(orderNumber);
		Date startDate = (Date) order.getProperty("startDate");
		order.setProperty("endDate", calculateNextPaymentDate(startDate));

		// saving entity
		PMUtil.persistEntity(order);
		return order;
	}

	private static Entity getSingleOrder(String orderNumber) {
		Key key = KeyFactory.createKey("Order", orderNumber);
		return PMUtil.findEntity(key);
	}

	public static Date calculateNextPaymentDate(Date startDate) {
		Date now = new Date();

		Calendar cal = Calendar.getInstance();
		cal.setTime(startDate);

		while (!now.before(cal.getTime()))
			cal.add(Calendar.YEAR, 1);
		return cal.getTime();
	}
}
