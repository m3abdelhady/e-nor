package me.heuristic.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import me.heuristic.util.HtmlUtil;
import me.heuristic.util.MailUtil;
import me.heuristic.references.Messages;


@SuppressWarnings("serial")
public class SendEmailServlet extends HttpServlet {
    public void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
    	doPost(req, resp);
    }

    public void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        List<String> errors = new ArrayList<String>();
    	StringBuilder returnUrl = new StringBuilder();

        // retrieve form elements
    	String fromAddress = HtmlUtil.handleNullWhiteSpaces(req.getParameter("fromAddress"));
    	String fromName = HtmlUtil.handleNullWhiteSpaces(req.getParameter("fromName"));
    	String toAddress = HtmlUtil.handleNullWhiteSpaces(req.getParameter("toAddress"));
    	String toName = HtmlUtil.handleNullWhiteSpaces(req.getParameter("toName"));
    	String emailSubject = HtmlUtil.handleNullWhiteSpaces(req.getParameter("emailSubject"));
    	String emailBody = HtmlUtil.handleNullWhiteSpaces(req.getParameter("emailBody"));

    	// validate required field
        if("".equals(fromAddress))
        	errors.add("Missing Sender Address.");
    	else if (!HtmlUtil.isMatchingRegex(fromAddress, HtmlUtil.EMAIL_PATTERN))
    		errors.add(Messages.MALFORMATTED_EMAIL);
        if("".equals(toAddress))
        	errors.add("Missing Recepient Address.");
    	else if (!HtmlUtil.isMatchingRegex(toAddress, HtmlUtil.EMAIL_PATTERN))
    		errors.add(Messages.MALFORMATTED_EMAIL);
        if("".equals(emailSubject))   	errors.add("Missing Email Subject.");
        if("".equals(emailBody))   		errors.add("Missing Email Body.");

    	if (errors.size()>0) {
			returnUrl.append("/TestEmail.jsp?");

    		returnUrl.append("&fromAddress=" + fromAddress + "&fromName=" + fromName);
    		returnUrl.append("&toAddress=" + toAddress + "&toName=" + toName);
    		returnUrl.append("&emailSubject=" + emailSubject + "&emailBody=" + emailBody);
    		for (String error:errors) {
    			returnUrl.append("&err=" + error);
    		}
    	} else {
            // no errors
    		MailUtil.sendMail(fromAddress, fromName, toAddress, toName, emailSubject, emailBody);
			returnUrl.append("&success=on");		    		
    	}

	    resp.setContentType("text/html");
	    PrintWriter out = resp.getWriter();
	
	    out.println("<html>");
	    out.println("<head>");
	    out.println("<title>Hola</title>");
	    out.println("</head>");
	    out.println("<body>");

	    out.println("Return Url: " + returnUrl + "<br/>\r\n");
	    out.println("From Address: " + fromAddress + "<br/>\r\n");
	    out.println("From Name: " + fromName + "<br/>\r\n");
	    out.println("To Address: " + toAddress + "<br/>\r\n");
	    out.println("To Name: " + toName + "<br/>\r\n");
	    out.println("Email Subject: " + emailSubject + "<br/>\r\n");
	    out.println("Email Body: " + emailBody + "<br/>\r\n");
	    
	    out.println("</body>");
	    out.println("</html>");
    }
}
