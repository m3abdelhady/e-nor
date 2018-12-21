package me.heuristic.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.text.SimpleDateFormat;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import me.heuristic.util.HtmlUtil;
import me.heuristic.references.Messages;
import me.heuristic.references.Porting;
import me.heuristic.references.UserType;
import me.heuristic.services.AccountService;
import me.heuristic.services.TaggedUrlService;

import com.google.appengine.api.datastore.Entity;


@SuppressWarnings("serial")
public class ExportCampaignsMSServlet extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		doPost(req, resp);
	}

	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {

		Entity myAccount = null;
		//String myEmail = "";
		String myRole = "";
		List<String> errors = new ArrayList<String>();
		StringBuilder returnUrl = new StringBuilder();
		
		String myEmail = HtmlUtil.handleNullWhiteSpaces(req.getSession().getAttribute("userEmail").toString());
		// retrieve form elements
		long longAccountId = HtmlUtil.handleNullforLongNumbers(req.getParameter("aid"));
		String delimiter = HtmlUtil.handleNullWhiteSpaces(req.getParameter("delimiter"));
		String[] sCampaignIds = req.getParameterValues("cid");

		/*UserService userService = UserServiceFactory.getUserService();
		User user = userService.getCurrentUser();*/

		// make sure there's a valid user
		// make sure it's a valid account and the user has permission
		if (""==myEmail) {
			errors.add(Messages.UNRECOGNIZED_USER);
		} else {
			//myEmail = user.getEmail();

			myAccount = AccountService.getSingleAccount(longAccountId);
			if (null==myAccount) {
				errors.add(Messages.NO_SUCH_ACCOUNT);
			} else {
				myRole = (String) myAccount.getProperty(myEmail);
				if (null==myRole) {
					errors.add(Messages.NO_PERMISSION);
				} else if (!UserType.hasPermission(myRole, UserType.PERMISSION_EXPORT_CAMPAIGNS)){
					errors.add(Messages.NO_PERMISSION);
				}
			}
		}

		// if any errors
		if (errors.size()>0) {
			returnUrl.append("/ManageAccounts.jsp?");
			for (String error:errors) {
				returnUrl.append("&err=" + error);
			}
			resp.sendRedirect (returnUrl.toString());
		} else {
			//validate export form
			if (null==sCampaignIds)
				errors.add(Messages.NO_CAMPAIGNS);

			if (errors.size()>0) {
				returnUrl.append("/ExportCampaigns.jsp?aid=" + longAccountId);
				for (String error:errors) {
					returnUrl.append("&err=" + error);
				}
				resp.sendRedirect (returnUrl.toString());
			} else {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
				Date now = new Date();
				String filename = "Campaigns-" + sdf.format(now);

				// setting content type and header
				// according to format/ delimiter
				resp.setCharacterEncoding("UTF-8");
				if (Porting.DEL_TSV.equals(delimiter)) {
					resp.setContentType("application/TSV");
					resp.setHeader("Content-Disposition","inline; filename=\""+filename+".tsv\"");
				} else if (Porting.DEL_CSVEXCEL.equals(delimiter)) {
					resp.setContentType("application/CSV");
					resp.setHeader("Content-Disposition","inline; filename=\""+filename+".csv\"");
				} else {
					resp.setContentType("text/CSV");
					resp.setHeader("Content-Disposition","inline; filename=\""+filename+".txt\"");
				}

				// export campaigns
				PrintWriter pw = resp.getWriter();
				//pw.print('\uFEFF'); // BOM for UTF8
				pw.print(TaggedUrlService.renderTaggedUrlExportListForMS(myAccount, sCampaignIds, delimiter));
			}
		}
	}
}
