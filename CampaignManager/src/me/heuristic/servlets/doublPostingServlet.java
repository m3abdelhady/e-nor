package me.heuristic.servlets;
import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import me.heuristic.util.HtmlUtil;



@SuppressWarnings("serial")
public class doublPostingServlet extends HttpServlet {
    public void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
    	doPost(req, resp);
    }

    public void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
    	String company = HtmlUtil.handleNullWhiteSpaces(req.getParameter("company"));
    	/*resp.sendRedirect("/dpublePosting.jsp/compamy");*/
    	HttpSession session = req.getSession(true);
    	session.setAttribute("company", company);
    	    	  
	    resp.getWriter().println(session.getAttribute("company"));    	  
		
    }
    
}       
