package me.heuristic.services;

import java.util.Date;
import java.util.ArrayList;
import java.util.List;

import me.heuristic.util.PMUtil;
import me.heuristic.references.Channels;
import me.heuristic.references.Messages;

import com.google.appengine.api.datastore.Entity;

public class ChannelService {

	public static void deleteAccountChannels(String myEmail, Entity account, String[] selChannels) {
		@SuppressWarnings("unchecked")
		List<String> channels = (ArrayList<String>) account.getProperty("channels");
		
		for (String channel:selChannels) {
			channels.remove(channel);
		}
		account.setProperty("channels", channels);
		
		Date date = new Date();
		account.setProperty("modifiedBy", myEmail);
		account.setProperty("modifiedAt", date);
		PMUtil.persistEntity(account);
	}

	public static void setDefaultChannels(Entity account) {
		List<String> channels = new ArrayList<String>();
		channels.addAll(Channels.defaults);
		account.setProperty("channels", channels);
	}

	public static void addAccountChannels(String myEmail, Entity account, String[] availChannels) {
		@SuppressWarnings("unchecked")
		List<String> channels = (ArrayList<String>) account.getProperty("channels");

		for (String channel:availChannels) {
			// only if it doesn't exist
			if (!channels.contains(channel))
				channels.add(channel.toLowerCase());
		}
		account.setProperty("channels", channels);

		Date date = new Date();
		account.setProperty("modifiedBy", myEmail);
		account.setProperty("modifiedAt", date);
		PMUtil.persistEntity(account);
	}

	public static String renderSimpleChannelsForm(String myRole, Entity account) {
		StringBuilder sb = new StringBuilder();
		long longAccountId = account.getKey().getId();

		sb.append("<form class=\"mws-form\" action=\"/editchannel\" method=\"post\" name=\"channelForm\" onsubmit=\"return validateChannelForm()\">\r\n");
		sb.append("<div class=\"mws-form-inline\">\r\n");
		sb.append("\t<div class=\"form-group\">\r\n");
		sb.append("\t\t<label>New Marketing Channel (Medium)</label>\r\n");
		sb.append("\t\t<div class=\"mws-form-item small\">\r\n");
		sb.append("\t\t\t<input type=\"text\" name=\"availChannels\" value=\"\" class=\"form-control\">\r\n");
		sb.append("\t\t</div>\r\n");
		sb.append("\t</div>\r\n");
		sb.append("\t<div class=\"mws-button-row\">\r\n");
		sb.append("\t\t<input type=\"submit\" value=\"Add &gt;&gt;\" class=\"btn green\" />\r\n");
		sb.append("\t</div>\r\n");
		sb.append("</div>\r\n");
		sb.append("<input type=\"hidden\" name=\"aid\" value=\"" + longAccountId + "\" />\r\n");
		sb.append("</form>\r\n");

		return sb.toString();
	}

	public static String renderDefaultChannelsForm(String myRole, Entity account) {
			StringBuilder sb = new StringBuilder();
			long longAccountId = account.getKey().getId();

		sb.append("<!-------------------------------->\r\n");
		sb.append("<form class=\"mws-form\" action=\"/editchannel\" method=\"post\" name=\"channelForm\" onsubmit=\"return validateChannelForm()\">\r\n");
		sb.append("\t<div class=\"form-group\">\r\n");
		sb.append("\t\t<div class=\"mws-form-item large\">\r\n");
		sb.append("\t\t\t<div class=\"mws-dualbox clearfix\">\r\n\r\n");
		
		List<String> channels = Channels.defaults;

		@SuppressWarnings("unchecked")
		List<String> accountChannels = (ArrayList<String>) account.getProperty("channels");
		
		sb.append("<div class=\"col-md-4\">\r\n");
		sb.append("\t<label>Available Channels</label>\r\n");
		sb.append("\t<select name=\"availChannels\" size=\"10\" multiple=\"true\" class='form-control'>\r\n");
		for (String channel : channels) {
			if (!accountChannels.contains(channel))
				sb.append("\t\t<option value=\"" + channel + "\">" + channel +"</option>\r\n");
		}
		sb.append("</select>\r\n");
		sb.append("</div>\r\n");

		sb.append("<div class=\"col-md-4\" style='margin-top:75px; text-align:center;'>\r\n");
		sb.append("\t<input class=\"btn green\" type=\"submit\" name=\"act\" value=\" Add >> \" />\r\n" +
				"\t<div class=\"clear\"><br></div>\r\n" +
				"\t<input class=\"btn red\" type=\"submit\" name=\"act\" value=\" << Remove \" />\r\n");
		sb.append("</div>\r\n");

		sb.append("<div class=\"col-md-4\">\r\n");
		sb.append("\t<label>Selected Channels</label>\r\n");
		sb.append("\t<select name=\"selChannels\" size=\"10\" multiple=\"true\" class='form-control'>\r\n");
		for (String channel : accountChannels) {
			sb.append("\t\t<option value=\"" + channel + "\">" + channel +"</option>\r\n");
		}
		sb.append("\t</select>\r\n\r\n");
		sb.append("\t\t\t</div>\r\n");
		sb.append("\t\t</div>\r\n");
		sb.append("\t</div>\r\n");
		sb.append("<input type=\"hidden\" name=\"aid\" value=\"" + longAccountId + "\" />\r\n");
		sb.append("</form>\r\n");
		sb.append("<!-------------------------------->\r\n");

		return sb.toString();
	}

    public static void validateSelChannels(List<String> errors, Entity account, String[] selChannels) {
    	if (null==selChannels || "".equals(selChannels[0])) {
    		errors.add(Messages.NO_CHANNELS);
    		return;
    	}
    }

    public static void validateAvailChannels(List<String> errors, Entity account, String[] availChannels) {
    	if (null==availChannels || "".equals(availChannels[0])) {
    		errors.add(Messages.NO_CHANNELS);
    		return;
    	}

		int maxChannels = Channels.MAX_CHANNELS;

		@SuppressWarnings("unchecked")
		List<String> channels = (ArrayList<String>) account.getProperty("channels");

		if (channels.size() + availChannels.length > maxChannels) {
    		errors.add(Messages.EXCEED_MAX_CHANNELS);
    		return;
		}
    }
}
