package me.heuristic.services;

import java.util.Date;
import java.util.ArrayList;
import java.util.List;

import me.heuristic.util.PMUtil;
import me.heuristic.references.AccountType;
import me.heuristic.references.Messages;
import me.heuristic.references.UserType;

import com.google.appengine.api.datastore.Entity;

public class AUserService {

	public static void deleteAccountUser(String myEmail, Entity account, String oldUserEmail) {
		@SuppressWarnings("unchecked")
		List<String> users = (ArrayList<String>) account.getProperty("users");
		
		account.removeProperty(oldUserEmail);
		users.remove(oldUserEmail);
		account.setProperty("users", users);
		
		Date date = new Date();
		account.setProperty("modifiedBy", myEmail);
		account.setProperty("modifiedAt", date);
		PMUtil.persistEntity(account);
	}

	public static void setDefaultAccountUser(Entity account, String myEmail) {
		List<String> users = new ArrayList<String>();
		users.add(myEmail);
		account.setProperty("users", users);
		account.setProperty(myEmail, UserType.ADMIN);
	}

	public static void createOrUpdateAccountUser(String myEmail, Entity account,
			String oldUserEmail, String newUserEmail, String newUserRole) {
		@SuppressWarnings("unchecked")
		List<String> users = (ArrayList<String>) account.getProperty("users");

		// if creating new user
		if (""==oldUserEmail) {
			users.add(newUserEmail);
			account.setProperty("users", users);
    	} else {

    		// if changing the email
    		if (!oldUserEmail.equals(newUserEmail)) {
        		account.removeProperty(oldUserEmail);
    			users.remove(oldUserEmail);
    			users.add(newUserEmail);
    			account.setProperty("users", users);
    		}
    	}
		account.setProperty(newUserEmail, newUserRole);

		Date date = new Date();
        account.setProperty("modifiedBy", myEmail);
        account.setProperty("modifiedAt", date);

        // saving entity
        PMUtil.persistEntity(account);
	}

	public static String renderAccountUsersList(String myRole, Entity account) {
	    StringBuilder sb = new StringBuilder();
		long longAccountId = account.getKey().getId();
	    
	    @SuppressWarnings("unchecked")
		ArrayList<String> userEmails = (ArrayList<String>) account.getProperty("users");

		sb.append("<a href=\"/EditAUser.jsp?aid=" + longAccountId + "\">Create New User</a><br/><br/>");
	    sb.append("<table class='table table-striped table-bordered table-hover table-checkable order-column dataTable no-footer' border=\"1\" cellspacing=\"0\" cellpadding=\"3\">\r\n");
	    sb.append("<thead><tr>\r\n");
	    sb.append("<th>User</th>\r\n");
	    sb.append("<th>User Type</th>\r\n");
	    sb.append("<th>Action</th>\r\n");
	    sb.append("</tr>\r\n");
	    sb.append("</thead>\r\n");
	    sb.append("<tbody>\r\n");

		for (String userEmail : userEmails) {
			sb.append("<tr>\r\n");
			sb.append("<td>" + userEmail + "</td>\r\n");
			sb.append("<td>" + account.getProperty(userEmail) + "</td>\r\n");
			if (UserType.hasPermission(myRole, UserType.PERMISSION_MANAGE_USERS)) {
				sb.append("<td><a href=\"/EditAUser.jsp?aid=" + longAccountId + "&oldUserEmail=" + userEmail + "\">Edit</a>");
				sb.append("&nbsp;|&nbsp;");
				sb.append("<a href=\"javascript:confirmUserDelete('/editauser?aid=" + longAccountId  + "&oldUserEmail=" + userEmail + "&act=" + Messages.ACTION_DELETE + "');\">Remove</a>");
			}
			sb.append("</td>\r\n");
			sb.append("</tr>\r\n");
		}
		sb.append("</tbody>\r\n");
		sb.append("</table>\r\n");

		sb.append("<br/><a href=\"/EditAUser.jsp?aid=" + longAccountId + "\">Create New User</a>");

	    return sb.toString();
	}

	public static String renderAccountUsersListForm(String myRole, Entity account, String oldUserEmail, String newUserEmail, String newUserRole) {
	    StringBuilder sb = new StringBuilder();
		long longAccountId = account.getKey().getId();

	    @SuppressWarnings("unchecked")
		ArrayList<String> userEmails = (ArrayList<String>) account.getProperty("users");

	    sb.append("<table class=\"table table table-striped table-bordered table-hover table-checkable order-column dataTable no-footer\">\r\n");
	    sb.append("<thead><tr><th>User</th><th>User Type</th><th>Action</th></tr></thead>\r\n");
	    sb.append("<tbody>\r\n");

		String userRole= "";
		for (String userEmail : userEmails) {
			// retrieve from datastore
			userRole = (String) account.getProperty(userEmail);
			if (null==userRole) userRole = "";

			// if user has permission AND editing this user
			if (UserType.hasPermission(myRole, UserType.PERMISSION_MANAGE_USERS) && 
					userEmail.equalsIgnoreCase(oldUserEmail)) {
				if ("".equals(newUserEmail) && "".equals(newUserRole)) {
					// if first time (no errors)
					// pre-fill with retrieved data
					sb.append(renderAccountUserFormRow(longAccountId, userEmail, userEmail, userRole));
				} else {
					// if it has errors
					// pre-fill with previously entered values
					sb.append(renderAccountUserFormRow(longAccountId, userEmail, newUserEmail, newUserRole));
				}
			} else {
				// if not editing display details
				sb.append(renderAccountUserDetailsRow(myRole, longAccountId, userEmail, userRole));
			}
		}

		// user has permission AND not editing
		if (UserType.hasPermission(myRole, UserType.PERMISSION_MANAGE_USERS) && "".equals(oldUserEmail)) {
			if ("".equals(newUserEmail) && "".equals(newUserRole)) {
				// if first time (no errors)
				// display empty fields
				sb.append(renderAccountUserFormRow(longAccountId, "", "", ""));   
			} else {
				// if it has errors
				// display previously entered values
				sb.append(renderAccountUserFormRow(longAccountId, oldUserEmail, newUserEmail, newUserRole));   
			}
		}

		sb.append("</tbody>\r\n");
		sb.append("</table>\r\n");

	    return sb.toString();
	}

	public static String renderAccountUserFormRow(long longAccountId, String oldUserEmail, String newUserEmail, String newUserRole) {
	    StringBuilder sb = new StringBuilder();

		String buttonLabel = "";
		// if new account user
		if ("".equals(oldUserEmail)) {
			buttonLabel = " Add ";
		} else {
			buttonLabel = " Save ";
		}

		 sb.append("<form action=\"/editauser\" method=\"post\" name=\"userForm\" onsubmit=\"return validateUserForm()\">\r\n");
		 sb.append("<tr><td><input class='form-control' name=\"newUserEmail\" type=\"text\" value=\"" + newUserEmail + "\" height=\"4\"/><div style='padding:5px 0'>*User e-mail that is registered in Google accounts</div></td>\r\n");
		 sb.append("<td><select class='form-control' name=\"newUserRole\">\r\n");

	    sb.append("\t<option value=\"" + UserType.VIEWER + "\"");
	    if (UserType.VIEWER.equalsIgnoreCase(newUserRole)) sb.append(" selected");
	    sb.append(">" + UserType.VIEWER + "</option>\r\n");

	    sb.append("\t<option value=\"" + UserType.EDITOR + "\"");
	    if (UserType.EDITOR.equalsIgnoreCase(newUserRole)) sb.append(" selected");
	    sb.append(">" + UserType.EDITOR + "</option>\r\n");

	    sb.append("\t<option value=\"" + UserType.ADMIN + "\"");
	    if (UserType.ADMIN.equalsIgnoreCase(newUserRole)) sb.append(" selected");
	    sb.append(">" + UserType.ADMIN + "</option>\r\n");

	    sb.append("</select></td>\r\n");
	    sb.append("<td><input class='btn green' type=\"submit\" value=\"" + buttonLabel + "\" /></td></tr>\r\n");
	    
	    sb.append("<input type=\"hidden\" name=\"aid\" value=\"" + longAccountId + "\" />\r\n");
	    sb.append("<input type=\"hidden\" name=\"oldUserEmail\" value=\"" + oldUserEmail + "\" />\r\n");
	    sb.append("</form>\r\n");

	    return sb.toString();
	}

	public static String renderAccountUserDetailsRow(String myRole, long longAccountId, String userEmail, String userRole) {
	    StringBuilder sb = new StringBuilder();

		sb.append("<tr>\r\n");
		sb.append("<td>" + userEmail + "</td>\r\n");
		sb.append("<td>" + userRole + "</td>\r\n");
		sb.append("<td>");
		if (UserType.hasPermission(myRole, UserType.PERMISSION_MANAGE_USERS)) {
			sb.append("<a href=\"/ManageAUsers.jsp?aid=" + longAccountId + "&oldUserEmail=" + userEmail + "\">Edit</a>");
			sb.append("&nbsp;|&nbsp;");
			sb.append("<a href=\"javascript:confirmUserDelete('/editauser?aid=" + longAccountId  + "&oldUserEmail=" + userEmail + "&act=" + Messages.ACTION_DELETE + "');\">Remove</a>");
		}
		sb.append("</td>\r\n");
		sb.append("</tr>\r\n");

	    return sb.toString();
	}

    public static boolean isOnlyAdmin(Entity account, String userEmail) {
		@SuppressWarnings("unchecked")
		List<String> users = (ArrayList<String>) account.getProperty("users");

    	int count = 0;
    	//users
    	for(String user:users) {
    		if (UserType.ADMIN.equalsIgnoreCase((String) account.getProperty(user))) {
    			count++;
    		}
    	}
    	if (1==count)
    		return true;
    	else
    		return false;
    }

    public static boolean hasExceededMaxUsers(Entity account) {
		String accountType = (String) account.getProperty("type");
		int maxUsers =  AccountType.maxUsersMap.get(accountType);

		@SuppressWarnings("unchecked")
		List<String> users = (ArrayList<String>) account.getProperty("users");

		return (users.size() >= maxUsers);
    }
}
