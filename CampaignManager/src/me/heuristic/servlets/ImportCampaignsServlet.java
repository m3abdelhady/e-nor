package me.heuristic.servlets;

import java.io.InputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import me.heuristic.references.AccountType;
import me.heuristic.references.Messages;
import me.heuristic.references.Porting;
import me.heuristic.references.UserType;
import me.heuristic.services.AccountService;
import me.heuristic.services.CampaignService;
import me.heuristic.services.TaggedUrlService;
import me.heuristic.util.HtmlUtil;

import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.util.Streams;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

@SuppressWarnings("serial")
public class ImportCampaignsServlet extends HttpServlet {

	public void doPost(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {

		List<String> errors = new ArrayList<String>();
		StringBuilder returnUrl = new StringBuilder();

		Map<String, String> settings = new HashMap<String, String>();
		InputStream stream;
		FileItemStream item;
		String fieldName = "";
		String fieldValue = "";
		String fileName = "";
		String buffer = "";

		// Check that we have a file upload request
		// retrieve all form elements
		if (ServletFileUpload.isMultipartContent(req)) {
			try {

				// Create a new file upload handler
				ServletFileUpload upload = new ServletFileUpload();

				// Parse the request
				FileItemIterator iter = upload.getItemIterator(req);
				while (iter.hasNext()) {
				    item = iter.next();
				    fieldName = item.getFieldName();
				    stream = item.openStream();
				    if (item.isFormField()) {
				        fieldValue = Streams.asString(stream, "UTF8");
				        settings.put(fieldName, fieldValue);
				    } else {
				    	fileName = item.getName();
				    	if (!"".equals(fileName)) {
					       buffer = Streams.asString(stream, "UTF8");
					       if (buffer.charAt(0)=='\uFEFF')
					    	   buffer = buffer.substring(1);
				    	}
				    }
				}
			} catch (Exception ex) {
				throw new ServletException(ex);
			}
		}

		Entity myAccount = null;
		//String myEmail = "";
		String myEmail = HtmlUtil.handleNullWhiteSpaces(req.getSession().getAttribute("userEmail").toString());
		String myRole = "";
		
		// make sure all needed parameters were provided

		long longAccountId = 0;
		String strAccountId = settings.get("aid");
		if (null==strAccountId || "".equals(strAccountId))
			errors.add(Messages.NO_SUCH_ACCOUNT);
		else
			longAccountId = Long.valueOf(strAccountId);

		//UserService userService = UserServiceFactory.getUserService();
		//User user = userService.getCurrentUser();

		// make sure there's a valid user and a valid account
		// and the user has permission
		if (""==myEmail) {
			errors.add(Messages.UNRECOGNIZED_USER);
		} else {
			//myEmail = user.getEmail();

			if (0==longAccountId) {
				errors.add(Messages.NO_SUCH_ACCOUNT);
			} else {
				myAccount = AccountService.getSingleAccount(longAccountId);
				if (null==myAccount) {
					errors.add(Messages.NO_SUCH_ACCOUNT);
				} else {
					myRole = (String) myAccount.getProperty(myEmail);
					if (null==myRole) {
						errors.add(Messages.NO_PERMISSION);
					} else if (!UserType.hasPermission(myRole, UserType.PERMISSION_IMPORT_CAMPAIGNS)){
						errors.add(Messages.NO_PERMISSION);
					}
				}
			}
		}

		// validate remaining form elements
		if ("".equals(fileName))
	    	errors.add(Messages.NO_IMPORT_FILE);
		else if (0>=buffer.length())
			errors.add(Messages.EMPTY_IMPORT_FILE);

		returnUrl.append("/ImportCampaigns.jsp?aid=" + longAccountId);
		if (errors.size()>0) {
			for (String error:errors) {
				returnUrl.append("&err=" + error);
			}
		} else {
			int savedRows = processRows(myEmail, myAccount, buffer);
			returnUrl.append("&info=" + savedRows + " rows imported successfully.");
		}
		res.sendRedirect (returnUrl.toString());
	}

	private int processRows(String myEmail, Entity myAccount, String buffer) {
		String accountType = (String) myAccount.getProperty("type");
		int maxCampaigns =  AccountType.maxCampaignsMap.get(accountType);
		
		Map<String, String> rowsMap = new HashMap<String, String>();

		int savedRowsCount = 0;
		String delimiterRegEx = "";
		String[] elements = new String[8];
		String[] headers = new String[8];

		// divide buffer into lines
		String[] lines = buffer.split("\\r?\\n|\\r"); // to cover all cases of new line
		if (0==lines.length)
			return savedRowsCount;

		// read first row and figure out the delimiter
		if (lines[0].contains("\t"))
			delimiterRegEx = Porting.REGEX_TSV; 
		else if (lines[0].contains(","))
			delimiterRegEx = Porting.REGEX_CSVEXCEL;
		else if (lines[0].contains(";"))
			delimiterRegEx = Porting.REGEX_SEMICOLON;
		else
			return savedRowsCount;

		// read first header row
		// save parameters and order
		elements = lines[0].split(delimiterRegEx);
		for (int j=0; j<elements.length; j++) {
			headers[j] = new String(elements[j]);
		}

		// move to next row
		int i = 1;
		while (i<lines.length) {
			if (lines[i].length()>0) {
				elements = lines[i].split(delimiterRegEx,-1);

				for (int j=0; j<elements.length; j++){
					rowsMap.put(headers[j],elements[j]);
					elements[j] = ""; // erase values to make sure next row won't inherit
				}

				savedRowsCount += saveRow(myEmail, myAccount, maxCampaigns, rowsMap);
			}
			i++;
		}
		return savedRowsCount;
	}

	// returns 1 on success and 0 on failure
	private int saveRow(String myEmail, Entity myAccount, long maxCampaigns, Map<String, String> rowMap) {
		long longAccountId = myAccount.getKey().getId();
		
		Entity myCampaign;
		String utm_campaign = rowMap.get("Campaign");
		if (null==utm_campaign) utm_campaign = "";
		String utm_medium = rowMap.get("Medium");
		if (null==utm_medium) utm_medium = "";
		String utm_source = rowMap.get("Source");
		if (null==utm_source) utm_source = "";
		
		// search in our campaign list
		long longCampaignId = 0;
		Iterable<Entity> campaigns = CampaignService.getAllCampaignsFor(longAccountId);
		int campaignsCount = 0;
		for (Entity campaign:campaigns) {
			if (utm_campaign.equalsIgnoreCase((String) campaign.getProperty("utm_campaign")) &&
				utm_medium.equalsIgnoreCase((String) campaign.getProperty("utm_medium")) &&
				utm_source.equalsIgnoreCase((String) campaign.getProperty("utm_source"))) {
				longCampaignId = campaign.getKey().getId();
			}
			campaignsCount++;
		}

		// if no matching campaign was found
		if (0==longCampaignId) {
			if (!CampaignService.isValidCampaignFields(myAccount, utm_campaign, utm_medium, utm_source))
				return 0;
	    	if (campaignsCount >= maxCampaigns)
        		return 0;

			// create and save campaign
			// also add it to our campaign list
			myCampaign = CampaignService.createOrUpdateCampaign(myEmail, longAccountId, 0, utm_campaign, utm_medium, utm_source, "");
//			campaigns.add(myCampaign);
			longCampaignId = myCampaign.getKey().getId();
		}

		String utm_url = rowMap.get("URL");
		if (null==utm_url) utm_url = "";
		String utm_term = rowMap.get("Term");
		if (null==utm_term) utm_term = "";
		String utm_content = rowMap.get("Content");
		if (null==utm_content) utm_content = "";
		String utm_extra = rowMap.get("Extra Parameter");
		if (null==utm_extra) utm_extra = "";

		if (!TaggedUrlService.isValidTaggedUrlFields(utm_url, utm_term, utm_content, utm_extra))
			return 0;

		TaggedUrlService.createOrUpdateTaggedUrl(myEmail, longAccountId, longCampaignId, 0, utm_url, utm_term, utm_content, utm_extra);
		return 1;
	}
}
