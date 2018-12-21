package me.heuristic.util;

import java.io.UnsupportedEncodingException;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class MailUtil {
	
	public static final StringBuilder sb = new StringBuilder(); 
	public static final String FROM_ADDRESS = "webmaster@campaignalyzer.com";
	//public static final String FROM_ADDRESS = "memo.mrsy@gmail.com";
	public static final String FROM_NAME = "Campaignalyzer";
	
	public static final String ACCOUNT_CREATED_EMAIL_SUBJECT = "CampaignAlyzer Account";
	public static final String ACCOUNT_CREATED_EMAIL_BODY = "<div style='padding:0px 30px;max-width:600px;'>"
			+ "<div style='color:rgb(17,17,17);font-family:Arial,sans-serif;font-size:12.8px;line-height:140%;border-bottom-width:1px;border-bottom-style:solid;border-bottom-color:rgb(153,153,153);overflow:hidden;padding-top:11px;'>"
			+ "<h1 style='font-size:33px;clear:both;font-weight:normal;'>Welcome to CampaignAlyzer!</h1>"
			+ "</div>"
			+ "<p style='color:rgb(17,17,17);font-family:Arial,sans-serif;font-size:12.8px;line-height:140%;padding:0px 0px 1em;'>Congratulations on gaining access to CampaignAlyzer!<br>"
			+ "Please note, to get started you will need to create an account then activate it by subscribing to a plan.</span></p>"
			+ "<a style='color:#32C5D2; font-size:28px; font-weight:normal;' href='https://app.campaignalyzer.com'>Click here to create and activate an account!</a>"
            + "<br><span style='font-size:20px; font'>Already created and activated? </span><a style='color:#32C5D2; font-size:28px; font-weight:normal;' href='https://app.campaignalyzer.com'>Click here to access sign in.</a> "
            + "<P style='padding:0px 0px 1em; color:rgb(17,17,17); font-family:Arial,sans-serif;font-size:12.8px;line-height:140%;padding:0px 0px 1em;'>CampaignAlyzer is the #1 online campaign tagging management platform on the market. "
            + "It has extensive technical capabilities and is incredibly easy to use.</P>"
			+ "<p style='padding:0px 0px 1em; color:rgb(17,17,17); font-family:Arial,sans-serif;font-size:12.8px;line-height:140%;padding:0px 0px 1em;'>CampaignAlyzer is a web-based application that acts as a central repository platform where organizations can store their marketing campaign values in one database. Marketing agencies and digital marketers across an organization have the ability to collaborate in tagging various online and offline campaigns, and ensure consistency in their campaign tagging.</p>"
			+ "<p style='padding:0px 0px 1em; font-family:Arial,sans-serif;font-size:12.8px;line-height:140%;padding:0px 0px 1em;'>For questions or support, please contact 866-638-7367 or email <a style='color:#32C5D2;' href='mailto:support@campaignalyzer.com'>support@campaignalyzer.com</a>.</p><p style='padding:0px 0px 1em; font-family:Arial,sans-serif;font-size:12.8px;line-height:140%;padding:0px 0px 1em;'>CampaignAlyzer Team</span></p>"
			+ "<h2 style='color:rgb(0,0,0);font-family:Arial,sans-serif;font-size:115%;line-height:140%;clear:both;border-bottom-style:solid;border-bottom-width:1px;border-bottom-color:rgb(153,153,153);padding-bottom:0.2em;'>Learn more about CampaignAlyzer</h2>"
			+ "<table style='color:rgb(17,17,17);font-family:Arial,sans-serif;font-size:95%;line-height:140%;min-width:600px;' border='0' cellpadding='5' cellspacing='5'>"
			+ "<tbody><tr>"
			+ "<td align='center' width='25%'><a href='https://campaignalyzer.com/features' style='text-decoration:none;' target='_blank' class=''><img src='https://campaignalyzer.com/wp-content/uploads/2012/04/icon12.png' /><br><span style='text-decoration:underline;'>Features</span></a></td>"
			+ "<td align='center' width='25%'><a href='https://campaignalyzer.com/pricing' style='text-decoration:none;' target='_blank'><img class='alignleft size-full wp-image-741' title='Google Analytics Integration' alt='' src='https://campaignalyzer.com/wp-content/uploads/2012/04/icon13.png' width='69' height='45'><br><span style='text-decoration:underline;'>Plans and Pricing</span></a></td>"
			+ "<td align='center' width='25%'><a href='https://www.campaignalyzer.com/why-campaignalyzer/' style='text-decoration:none;' target='_blank'><img src='https://campaignalyzer.com/wp-content/uploads/2012/04/icon14.png' /><br><span style='text-decoration:underline;'>Why CampaignAlyzer</span></a></td>"
			+ "<td align='center' width='25%'><a href='https://www.campaignalyzer.com/support/' style='text-decoration:none;' target='_blank'><img src='https://campaignalyzer.com/wp-content/uploads/2012/04/icon5.png' /><br><span style='text-decoration:underline;'>Learning Center</span></a></td>"
			+ "</tr></tbody></table>"
			+ "<div style='color:rgb(153,153,153);font-family:Arial,sans-serif;font-size:11px;line-height:120%;border-top-width:1px;border-top-style:solid;border-top-color:rgb(153,153,153);padding-top:8px;'>"
			+ "<p style='padding:0px 0px 1em;'>© Copyright 2016 CampaignAlyzer | <a href='https://campaignalyzer.com/' target='_blank'>Home</a> | <a href='https://campaignalyzer.com/terms' target='_blank'>Terms Of Use</a> | <a href='https://campaignalyzer.com/privacy-policy' target='_blank'>Privacy Policy</a> | <a href='https://campaignalyzer.com/support.html' target='_blank'>Help</a> | <a href='https://campaignalyzer.com/contact-us' target='_blank'>Contact us</a></p></div></div>";
		
	public static final String ACCOUNT_PLAN_CHANGED_EMAIL_SUBJECT = "Campaignalyzer Account";
	public static final String ACCOUNT_PLAN_CHANGED_EMAIL_BODY = "Hello,\r\nYour Campaignalyzer Account plan has been changed to";
	/*public static void sendAccountCreationMail(String ToAddress, String ToName) {
		sendMail(FROM_ADDRESS, FROM_NAME, ToAddress, ToName, ACCOUNT_CREATED_EMAIL_SUBJECT,ACCOUNT_CREATED_EMAIL_BODY);
	}*/

	/*public static void sendMail(String ToAddress, String ToName,
			String EmailSubject, String EmailBody) {
		sendMail(FROM_ADDRESS, FROM_NAME, ToAddress, ToName, EmailSubject,EmailBody);
	}*/

	public static void sendMail(String FromAddress, String FromName,
				String ToAddress, String ToName,
				String EmailSubject, String EmailBody) {
		//StringBuilder sb = new StringBuilder();
		/*sb.append("<div><h1>Welcome to CampaignAlyzer!</h1></div>"
				+ "<p>Congratulations on activating your CampaignAlyzer account. You will find CampaignAlyzer to be the best online campaign tagging management platform in the market.It has extensive technical capabilities and is incredibly easy to use.</p>"
				+ "<p>CampaignAlyzer is a web-based application that acts as a central repository platform where organizations can store their marketing campaign values in one database. Marketing agencies and digital marketers across an organization would have the ability to collaborate in tagging various online and offline campaigns, and ensure consistency in their campaign tagging.</p>"
				+ "<p>Happy Tagging,</p><p>CampaignAlyzer Team</p>"
				+ "<h2>Learn more about CampaignAlyzer</h2>"
				+ "<table border='0' cellpadding='5' cellspacing='5'><tbody><tr>"
				+ "<td align='center' width='25%'><a href='http://campaignalyzer.com/features.html' target='_blank'>"
				+ "<img src='http://campaignalyzer.com/wp-content/uploads/2012/04/icon12.png' /><br />Features</a></td>"
				+ "<td align='center' width='25%'><a href='http://campaignalyzer.com/pricing.html' target='_blank'><img class='alignleft size-full wp-image-741' title='Google Analytics Integration' alt='' src='http://campaignalyzer.com/wp-content/uploads/2012/04/icon13.png' width='69' height='45'><br />Plans and Pricing</a></td>"
				+ "<td align='center' width='25%'><a href='http://campaignalyzer.com/about/why-campaignalyzer.html' target='_blank'><img src='http://campaignalyzer.com/wp-content/uploads/2012/04/icon14.png' /><br />Why CampaignAlyzer</a></td>"
				+ "<td align='center' width='25%'><a href='http://campaignalyzer.com/help.html' target='_blank'><img src='http://campaignalyzer.com/wp-content/uploads/2012/04/icon5.png' /><br />Learning Center</a></td>"
				+ "</tr></tbody></table>"
				+ "<div><p>© Copyright 2012 CampaignAlyzer | <a href='http://campaignalyzer.com/' target='_blank'>Home</a> | <a href='http://campaignalyzer.com/terms.html' target='_blank'>Terms Of Use</a> | <a href='http://campaignalyzer.com/privacy-policy.html' target='_blank'>Privacy Policy</a> | <a href='http://campaignalyzer.com/help.html' target='_blank'>Help</a> | <a href='http://campaignalyzer.com/contact.html' target='_blank'>Contact us</a></p></div>");*/
		
		
		Properties props = new Properties();
		Session session = Session.getDefaultInstance(props, null);

		try {
		    Message msg = new MimeMessage(session);
		    msg.setFrom(new InternetAddress(FromAddress, FromName));
		    msg.addRecipient(Message.RecipientType.TO, new InternetAddress(ToAddress, ToName));
		    msg.setSubject(EmailSubject);
		    msg.setText(EmailBody);
		    Transport.send(msg);

		} catch (UnsupportedEncodingException e) {
		    // ToDo: handle AddressException
		} catch (AddressException e) {
		    // ToDo: handle AddressException
		} catch (MessagingException e) {
		    // ToDo: handle MessagingException
		} catch (Exception e) {
		    // ToDo: ...
		}		
	}
	
	public static void sendAccountCreationMail(String ToAddress, String ToName, String ToOwnerAddress, String ToOwnerName) {
	Properties props = new Properties();
	Session session = Session.getDefaultInstance(props, null);

	try {
	    Message msg = new MimeMessage(session);
	    msg.setFrom(new InternetAddress(FROM_ADDRESS, FROM_NAME));
	    msg.addRecipient(Message.RecipientType.TO, new InternetAddress(ToAddress, ToName));
	    msg.addRecipient(Message.RecipientType.CC, new InternetAddress(ToOwnerAddress, ToOwnerName));	    
	    msg.setSubject(ACCOUNT_CREATED_EMAIL_SUBJECT);
	    msg.setContent(ACCOUNT_CREATED_EMAIL_BODY, "text/html; charset=utf-8");
	    //msg.setText(sb);
	    Transport.send(msg);

	} catch (UnsupportedEncodingException e) {
	    // ToDo: handle AddressException
	} catch (AddressException e) {
	    // ToDo: handle AddressException
	} catch (MessagingException e) {
	    // ToDo: handle MessagingException
	} catch (Exception e) {
	    // ToDo: ...
	}		
}
	
	public static void sendChangePlanMail(String ToAddress, String ToName,String plan) {
		Properties props = new Properties();
		Session session = Session.getDefaultInstance(props, null);

		try {
		    Message msg = new MimeMessage(session);
		    msg.setFrom(new InternetAddress(FROM_ADDRESS, FROM_NAME));
		    msg.addRecipient(Message.RecipientType.TO, new InternetAddress(ToAddress, ToName));
		    //msg.addRecipient(Message.RecipientType.CC, new InternetAddress(ToOwnerAddress, ToOwnerName));	    
		    msg.setSubject(ACCOUNT_PLAN_CHANGED_EMAIL_SUBJECT);
		    msg.setText(ACCOUNT_PLAN_CHANGED_EMAIL_BODY +" "+plan + "\r\n\r\n\r\nRegards,\r\n\r\nCampaignalyzer Staff\r\n\r\n866-638-7367") ;
		    Transport.send(msg);

		} catch (UnsupportedEncodingException e) {
		    // ToDo: handle AddressException
		} catch (AddressException e) {
		    // ToDo: handle AddressException
		} catch (MessagingException e) {
		    // ToDo: handle MessagingException
		} catch (Exception e) {
		    // ToDo: ...
		}		
	}
}
