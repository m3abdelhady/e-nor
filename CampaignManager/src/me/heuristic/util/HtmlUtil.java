package me.heuristic.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import me.heuristic.references.AccountType;
import me.heuristic.references.UserType;
import me.heuristic.services.AccountService;
//import me.heuristic.services.CampaignService;

import me.heuristic.services.AnalyticsService;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.users.UserServiceFactory;

public class HtmlUtil {
	private static final String EMAIL_REGEX = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
	private static final String GMAIL_REGEX = "^[a-z0-9](\\.?[a-z0-9]){5,}@gmail\\.com$";
	private static final String URL_REGEX = "^(https?://)?(([\\w!~*'().&=+$%-]+: )?[\\w!~*'().&=+$%-]+@)?(([0-9]{1,3}\\.){3}[0-9]{1,3}|([\\w!~*'()-]+\\.)*([\\w^-][\\w-]{0,61})?[\\w]\\.[a-z]{2,6})(:[0-9]{1,4})?((/*)|(/+[\\w!~*'().;?:@&=+$,%#-]+)+/*)$";
	public static Pattern EMAIL_PATTERN = null;
	public static Pattern GMAIL_PATTERN = null;
	public static Pattern URL_PATTERN = null;

	static {
		try {
			EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);
			//GMAIL_PATTERN = Pattern.compile(GMAIL_REGEX);
			URL_PATTERN = Pattern.compile(URL_REGEX);		
		} catch (PatternSyntaxException e) {
			// TODO
		}
	}

	public static boolean isMatchingRegex(final String text, Pattern pattern) {
		Matcher matcher;

		if (null==text|| "".equals(text))
			return false;

		matcher = pattern.matcher(text);
		return matcher.matches();
	}

    // XXX: obsolete method: surround with tags
	public static String surroundWithTag(String s, String sTag) {
		return "<" + sTag + ">" + s + "</" + sTag + ">";
	}

	//returns html title tag
	public static String renderTitle(String s) {
		StringBuilder sb = new StringBuilder();

		if (null==s || "".equals(s)){
			sb.append("<title>CampaignAlyzer</title>");
		} else {
			sb.append("<title>CampaignAlyzer - " + s + "</title>");			
		}
		return sb.toString();
	}

	public static String renderWelcomeBox(String myEmail, String myRole, String signoutURI, String signinURI) {
		StringBuilder sb = new StringBuilder();

//		sb.append("<div class=\"welcomebox\">");
	    if (""==myEmail) {
	    	sb.append("<b>Welcome!</b><br/> ");
	    	sb.append("<a id='signin' href=\"" + signinURI + "\">Sign In</a>");
	    } else {
	    	//sb.append(myEmail + "<br/>");
	    	
	    	sb.append("<a href='javascript:;' class='dropdown-toggle' data-toggle='dropdown' data-hover='dropdown' data-close-others='true' style='padding:24px 4px !important; float:left;'>"
    	            //+"<img alt='' class='img-circle' src='../assets/layouts/layout2/img/avatar3_small.jpg'>"
    	            +"<span class='username username-hide-on-mobile'> "+ myEmail+" </span><i class='fa fa-angle-down'></i>");
	    	if (null!=myRole && !"".equals(myRole))
	    		sb.append(" [" + myRole + "] ");
	    	sb.append("</a>");
	    	sb.append("<ul class='dropdown-menu dropdown-menu-default'>"	    	           
	            +"<li>"
	            +"<a class='ologout' href="+signoutURI+">"
	            +"<i class='icon-logout'></i>"
	            +"Logout"
	            +"</a>"
	            +"</li>"	            
	            + "</ul>");
	    	
	    	//sb.append("<a id='signout' href=\"" + signoutURI +"\">Sign Out</a>");
	    	
	    	
	    	
	    	// New Template
	    	/*sb.append("<a href='javascript:;' class='dropdown-toggle' data-toggle='dropdown' data-hover='dropdown' data-close-others='true'>"
    	            +"<img alt='' class='img-circle' src='../assets/layouts/layout2/img/avatar3_small.jpg'>"
    	            +"<span class='username username-hide-on-mobile'> "+ myRole+" </span>"
    	            +"<i class='fa fa-angle-down'></i>"
    	            +"</a>"
    	            +"<ul class='dropdown-menu dropdown-menu-default'>"	    	           
    	            +"<li>"
    	            +"<a href='app_inbox.html'>"
    	            +"<i class='icon-key'></i>Logout"            
    	            +"</a>"
    	            +"</li></ul>");*/
	    	// End New Template
	    }
//		sb.append("</div>");
		return sb.toString();
	}

	public static String renderAccountSwitcher(String myEmail, long longAccountId) {
	    StringBuilder sb = new StringBuilder();

		sb.append("<!-- Begin Account Selector -->\r\n<div id=\"mws-switchbox\" class=\"mws-inset\">\r\n");
		sb.append("<form id='laid' action=\"\" method=\"get\">\r\n");
		sb.append("\t<select id='aid' style='max-width:100%;' name=\"aid\" class='bs-select form-control bs-select-hidden' onchange=\"javascript:this.form.submit();\">\r\n");
		//sb.append("\t\t<option value=\"0\">Select an Account</option>\r\n");
		if (""!=myEmail)	{
		    Iterable<Entity> accounts = AccountService.getActiveAccountsForUser(myEmail);
			for (Entity account : accounts) {
				sb.append("\t\t<option value=\"" + account.getKey().getId() + "\"");
				if (longAccountId == account.getKey().getId()) {
					sb.append(" selected");
				}
				sb.append(">" + account.getProperty("name") + "</option>\r\n");
			}
		}
		sb.append("\t</select>\r\n");
		sb.append("</form>\r\n</div>\r\n<!-- End Account Selector -->\r\n");

		return sb.toString();
	}
	
	public static String renderTopAccountSwitcher(String myEmail, long longAccountId) {
	    StringBuilder sb = new StringBuilder();

		sb.append("<!-- Begin Account Selector -->\r\n<div style='float:left; margin-top:10px; width:10%' id=\"mws-switchbox\" class=\"mws-inset\">\r\n");
		sb.append("<form id='laid' action=\"\" method=\"get\">\r\n");
		sb.append("\t<select style='max-width:100%' name=\"aid\" class='form-control' onchange=\"javascript:this.form.submit();\">\r\n");
		sb.append("\t\t<option value=\"0\">Select an Account</option>\r\n");
		if (""!=myEmail)	{
		    Iterable<Entity> accounts = AccountService.getActiveAccountsForUser(myEmail);
			for (Entity account : accounts) {
				sb.append("\t\t<option value=\"" + account.getKey().getId() + "\"");
				if (longAccountId == account.getKey().getId()) {
					sb.append(" selected");
				}
				sb.append(">" + account.getProperty("name") + "</option>\r\n");
			}
		}
		sb.append("\t</select>\r\n");
		sb.append("</form>\r\n</div>\r\n<!-- End Account Selector -->\r\n");

		return sb.toString();
	}

	
	//Breadcrump
/*	public static String renderBreadcrumbs(String myEmail, String myRole, long longAccountId, String cName, String newType) {
	    StringBuilder sb = new StringBuilder();
	    //sb.append("<ul class='breadcrumbs'>");
		sb.append("<li><a href=\"/\">My Accounts</a>");		
		if (""!=myEmail)	{
		    Iterable<Entity> accounts = AccountService.getActiveAccountsForUser(myEmail);
		    
		     //long longCampaignId = campaign.getKey().getId();
			for (Entity account : accounts) {				
				if (longAccountId == account.getKey().getId() && (null != cName || null != newType) ) {
					//sb.append(" selected");
					sb.append("<i class='fa fa-angle-right'></i></li><li title='account name'><a href='/ManageCampaigns.jsp?aid="+longAccountId+"'>"+account.getProperty("name")+"</a>");
					//sb.append("<li>Campaigns</li>");
				}
				
				else if(longAccountId == account.getKey().getId() && null == cName){
					sb.append("<i class='fa fa-angle-right'></i></li>");
					sb.append("<li title='account name'>"+account.getProperty("name")+"<i class='fa fa-angle-right'></i></li>");
					
				}
				
				else if (longAccountId == account.getKey().getId() && 0 != longCampaignId){
					sb.append("<li><a href='/ManageCampaigns.jsp?aid="+longAccountId+"'>"+account.getProperty("name")+"</a></li>");
					
				}
				//sb.append(campaign.getProperty("name"));
				
				
			}				
			//sb.append("<li><a href='/ManageTaggedURLs.jsp?aid="+longAccountId+"&cid="+longCampaignId+"'>"+campaign.getProperty("utm_campaign")+"</a></li>");
				//sb.append(campaign.getProperty("name"));
			if(null != cName){
				
				sb.append("<i class='fa fa-angle-right'></i><li title="+cName+">"+cName+"<i class='fa fa-angle-right'></i></li>");
			}
			
			if(null !=newType){
				
				sb.append("<i class='fa fa-angle-right'></i><li title="+newType+">"+newType+"</li> ");
			}
			
			
			
			
		}
		
		
		//sb.append("</ul>");

		return sb.toString();
	}*/
	
	public static String renderBreadcrumbs(String myEmail, String myRole, long longAccountId, String cName,String ms, long cid, String newType, String title) {
	    StringBuilder sb = new StringBuilder();
	    
	    //sb.append("<ul class='breadcrumbs'>");
		sb.append("<li><a href=\"/SC_CreateAccount\">My Accounts</a>");		
		if (""!=myEmail)	{
		   /* Iterable<Entity> accounts = AccountService.getActiveAccountsForUser(myEmail);*/		    
		    
		     //long longCampaignId = campaign.getKey().getId();
			/*for (Entity account : accounts) {				
				if (longAccountId == account.getKey().getId()) {
					//sb.append(" selected");
					sb.append("<i class='fa fa-angle-right'></i></li><li title='account name'><a href='/ManageCampaigns.jsp?aid="+longAccountId+"'>"+account.getProperty("name")+"</a>");
					//sb.append("<li>Campaigns</li>");
				}				
				
				
			}	*/		
		    
		    if(longAccountId !=0){
		    	Entity Account = AccountService.getSingleAccount(longAccountId);
		    	sb.append("<i class='fa fa-angle-right'></i></li><li title='Go To Account Page'><a href='/ManageCampaignHolders.jsp?aid="+longAccountId+"'>"+Account.getProperty("name")+"</a><i class='fa fa-angle-right'></i></li>");
		    }
			
			if(null != cName){
				//cName.contains("'");
				cName.replace("'", "\\'");			
				
				//sb.append("<li title='Go To Campaign Page'><a href='ManageCampaigns.jsp?aid="+longAccountId+"&cid="+cid+"&cname="+cName+"'>"+cName+"</a><i class='fa fa-angle-right'></i></li>");
				
				sb.append("<li title=\"Go To Campaign Page\"><a href=\"ManageCampaigns.jsp?cname="+cName+"&aid="+longAccountId+"&cid="+cid+" \">"+cName+"</a><i class=\"fa fa-angle-right\"></i></li>");
				//sb.append("<li title=\"Go To Campaign Page\"><a href=\"ManageCampaign.jsp?aid="+longAccountId+" &cid='"+cid+"' &cname='"+cName+"'>'"+cName+"'</a><i class='fa fa-angle-right'></i></li>");
			}
			
			if(null != ms){
				sb.append("<li title=\"Go To Campaign Page\">"+ms+"<i class=\"fa fa-angle-right\"></i></li>");
				
			}
			
			if(null !=newType){
				
				sb.append("<li title='Selected Plan'>"+newType+"<i class='fa fa-angle-right'></i></li>");
			}
			
			if(null !=title){
				
				sb.append("<li title="+title+">"+title+"</li>");
			}
			
			
		}
		
		
		//sb.append("</ul>");

		return sb.toString();
	}
	
	public static String renderNavigationLinks(String myEmail, String myRole, long longAccountId) {
		
       // String url = 
        StringBuilder sb = new StringBuilder();
		Entity account = null;
		String accountType = null;
		

		sb.append("<!-- Begin Navigation Links -->\r\n<ul class='page-sidebar-menu  page-header-fixed page-sidebar-menu-hover-submenu'>\r\n");
		if(AccountService.getAllAccountsCountFor(myEmail) != 0){
			
//			sb.append("\t<li class='nav-item start'><a href='/dashboard.jsp?aid="+AccountService.getFirstActiveAccountsForUser(myEmail).getKey().getId()+"' id='dashboardlink' class=\"nav-link nav-toggle\"><i class='icon-home'></i><span class='title'>Dashboard</span></a></li>\r\n");
			sb.append("\t<li class='nav-item start'><a href='/dashboard.jsp?aid="+longAccountId+"' id='dashboardlink' class=\"nav-link nav-toggle\"><i class='icon-home'></i><span class='title'>Dashboard</span></a></li>\r\n");

		if (!"".equals(myEmail)) {
			}
			if (0!=longAccountId) {
			    account = AccountService.getSingleAccount(longAccountId);
				if (null!=account) {
					accountType = (String) account.getProperty("type");
					String refreshToken = (String) account.getProperty("refreshToken");
					sb.append("\t<li class='campaigns nav-item  '>\r\n");
					sb.append("\t\t<a href='/ManageCampaignHolders.jsp?aid=" + longAccountId +"' class='nav-link nav-toggle'><i class='fa fa-list'></i><span class='title'>Campaigns</span><span class='arrow'></span></a>\r\n");
					sb.append("\t\t<ul class='sub-menu'>\r\n");
					sb.append("\t<li class='nav-item'  ><a class=\"nav-link\" href=\"/ManageCampaignHolders.jsp?aid=" + longAccountId + "\">Web Campaigns</a></li>\r\n");
					sb.append("\t<li class='nav-item'  ><a class=\"nav-link\" href=\"/ManageMobileCampaignHolders.jsp?aid=" + longAccountId + "\">Mobile Apps Campaigns<span style='padding-left: 2px; color: #dd7127; font-size: 9px; font-weight: bold; position: relative; text-transform: uppercase; top: -3px;'>BETA</span></a></li>\r\n");
					/*if (UserType.hasPermission(myRole, UserType.PERMISSION_MANAGE_CAMPAIGNS) || UserServiceFactory.getUserService().isUserAdmin() ) {
						sb.append("\t<li class='newCampaigns'><a class=\"mws-i-24 i-plus\" href=\"/EditCampaign.jsp?aid=" + longAccountId + "\">New Campaign</a></li>\r\n");
					}*/
					sb.append("\t\t</ul>\r\n");
					sb.append("\t</li>\r\n");
					
					
					if (AccountType.allowsImport(accountType) &&
						UserType.hasPermission(myRole, UserType.PERMISSION_IMPORT_CAMPAIGNS)) {
						sb.append("\t<li class='nav-item'><a class=\"nav-link\" href=\"/ImportCampaigns.jsp?aid=" + longAccountId + "\"><i class='icon-cloud-download'></i><span class='title'>Import Campaigns</span></a></li>\r\n");
					}
					if (AccountType.allowsExport(accountType) &&
						UserType.hasPermission(myRole, UserType.PERMISSION_EXPORT_CAMPAIGNS)) {
						sb.append("\t<li class='nav-item'><a class='nav-link' href=\"/ExportCampaigns.jsp?aid=" + longAccountId + "\"><i class='icon-cloud-upload'></i><span class='title'>Export Campaigns</span></a></li>\r\n");
					}
					
					if(AnalyticsService.GetRefreshKey(longAccountId) != null && AnalyticsService.GetViewId(longAccountId) != null){
						sb.append("\t<li class='nav-item'><a class='nav-link' href=\"/AnalyticsCallBack?dis=analyticsReports&aid=" + longAccountId + "\"><i class='icon-bar-chart'></i><span class='title'>Analytics</span><span style='padding-left: 2px; color: #dd7127; font-size: 9px; font-weight: bold; position: relative; text-transform: uppercase; top: -3px;'>BETA</span></a></li>\r\n");
					}
					
					sb.append("\t<li class='settings nav-item'>\r\n");
					sb.append("\t\t<a href='javascript:;' class='nav-link nav-toggle'><i class='icon-settings'></i><span class='title'>Settings</span><span class='arrow'></span></a>\r\n");
					sb.append("\t\t<ul class='sub-menu'>\r\n");
					sb.append("\t\t\t<li class='nav-item'><a class='nav-link' href=\"/ViewAccount.jsp?aid=" + longAccountId + "\"><i class='icon-equalizer'></i><span class='title'>Account Settings</span></a></li>\r\n");
					//sb.append("<li class='nav-item  '><a href='ecommerce_index.html' class='nav-link '><i class='icon-home'></i><span class='title'>Dashboard</span></a> </li>");
					if (UserType.hasPermission(myRole, UserType.PERMISSION_MANAGE_ACCOUNT)) {
						sb.append("\t\t\t<li class='nav-item'  ><a class='nav-link' href=\"/ManageAUsers.jsp?aid=" + longAccountId + "\"><i class='fa fa-users'></i><span class='title'>User Manager</span></a></li>\r\n");
						sb.append("\t\t\t<li class='nav-item'  ><a class='nav-link' href=\"/EditAccountPlan.jsp?aid=" + longAccountId + "\"><i class='fa fa-credit-card'></i><span class='title'>Manage Billing</span></a></li>\r\n");
						sb.append("\t\t\t<li class='nav-item'  ><a class='nav-link' href=\"/ManageChannels.jsp?aid=" + longAccountId + "\"><i class='fa fa-pie-chart'></i><span class='title'>Marketing Channels</span></a></li>\r\n");
						sb.append("\t\t\t<li class='nav-item'  ><a class='nav-link' href=\"/EditTaggingSettings.jsp?aid=" + longAccountId + "\"><i class='fa fa-tags'></i><span class='title'>Tagging Settings</span></a></li>\r\n");
						sb.append("\t\t\t<li class='nav-item'  ><a class='nav-link' href=\"/AnalyticsCallBack?act=&dis=AnalyticsSettings&aid=" + longAccountId + "\"><i class='fa fa-bar-chart'></i><span class='title'>Google Analytics</span><span style='padding-left: 2px; color: #dd7127; font-size: 9px; font-weight: bold; position: relative; text-transform: uppercase; top: -3px;'>BETA</span></a></li>\r\n");
					}
					sb.append("\t\t</ul>\r\n");
					sb.append("\t</li>\r\n");
				}
			}
		}
		sb.append("\t<li class='nav-item'><a target=\"_blank\" href=\"https://campaignalyzer.com/support\" class=\"mws-i-24 i-tools\"><i class='fa fa-mortar-board'></i><span class='title'>Support</span></a></li>\r\n");
		//sb.append("\t<li class='nav-item'><a target=\"_blank\" href=\"https://campaignalyzer.com/support\" class=\"mws-i-24 i-info-about\"><i class='icon-question'></i><span class='title'>Help</span></a></li>\r\n");
		
		sb.append("</ul>\r\n</div>\r\n<!-- End Navigation Links -->\r\n");

		return sb.toString();
	}
	
	//top Nav Added By Mahmoud Morsi
	
	public static String renderTopNavigationLinks(String myEmail, String myRole, long longAccountId) {
		
	       // String url = 
	        StringBuilder sb = new StringBuilder();
			Entity account = null;
			String accountType = null;

			sb.append("<!-- Begin Navigation Links -->\r\n<div id='hmenu'>\r\n<ul id='hnav'>\r\n");
			sb.append("\t<li><a class=\"mws-i-24 i-home\" href=\"/\" style='display:block; width:50px;'>&nbsp;</a></li>\r\n");
			if (!"".equals(myEmail)) {
				if (0!=longAccountId) {
				    account = AccountService.getSingleAccount(longAccountId);
					if (null!=account) {
						accountType = (String) account.getProperty("type");
						sb.append("\t<li class='topcampaigns'>\r\n");
						sb.append("\t\t<a title='Campaigns' href=\"\" class=\"mws-i-24 i-tags-2\" style='display:block; width:50px;' >&nbsp;</a>\r\n");
						sb.append("\t\t<ul style='display:none; width:200px; position:absolute'>\r\n");
						sb.append("\t<li><a title='Manage Campaigns' class=\"mws-i-24 i-tags-2\" href=\"/ManageCampaigns.jsp?aid=" + longAccountId + "\">Campaigns</a></li>\r\n");
						sb.append("\t<li><a title='Mobile Campaigns' class=\"mws-i-24 i-tags-2\" href=\"/ManageMobileCampaigns.jsp?aid=" + longAccountId + "\">Mobile Campaigns<span style='padding-left: 2px; color: #dd7127; font-size: 9px; font-weight: bold; position: relative; text-transform: uppercase; top: -3px;'>BETA</span></a></li>\r\n");
						if (UserType.hasPermission(myRole, UserType.PERMISSION_MANAGE_CAMPAIGNS) || UserServiceFactory.getUserService().isUserAdmin() ) {
							sb.append("\t<li ><a class=\"mws-i-24 i-plus\" href=\"/EditCampaign.jsp?aid=" + longAccountId + "\">New Campaign</a></li>\r\n");
						sb.append("\t\t</ul>\r\n");						
						sb.append("\t</li>\r\n");						
						
						}
						sb.append("\t<li class='topImportExport'>\r\n");
						sb.append("\t\t<a href=\"\" class=\"mws-i-24 i-box-incoming-2\" style='display:block; width:50px;' >&nbsp;</a>\r\n");
						sb.append("\t\t<ul style='display:none; width:200px; position:absolute'>\r\n");
						if (AccountType.allowsImport(accountType) &&
							UserType.hasPermission(myRole, UserType.PERMISSION_IMPORT_CAMPAIGNS) || UserServiceFactory.getUserService().isUserAdmin() ) {
							sb.append("\t<li class='topimportCampaigns'><a class=\"mws-i-24 i-box-incoming-2\" href=\"/ImportCampaigns.jsp?aid=" + longAccountId + "\" >Import Campaigns</a></li>\r\n");
						}
						if (AccountType.allowsExport(accountType) &&
							UserType.hasPermission(myRole, UserType.PERMISSION_EXPORT_CAMPAIGNS) || UserServiceFactory.getUserService().isUserAdmin() ) {
							sb.append("\t<li class='topexportCampaigns'><a class=\"mws-i-24 i-box-outgoing-2\" href=\"/ExportCampaigns.jsp?aid=" + longAccountId + "\">Export Campaigns</a></li>\r\n");
						}
						sb.append("\t\t</ul>\r\n");
						sb.append("\t</li>\r\n");
						sb.append("\t<li class='topsettings'>\r\n");
						sb.append("\t\t<a title='settings' href=\"\" class=\"mws-i-24 i-cog\" style='display:block; width:50px;'></a>\r\n");
						sb.append("\t\t<ul style='display:none; width:200px; position:absolute;'>\r\n");
						sb.append("\t\t\t<li><a href=\"/ViewAccount.jsp?aid=" + longAccountId + "\">Account Settings</a></li>\r\n");
						if (UserType.hasPermission(myRole, UserType.PERMISSION_MANAGE_ACCOUNT) || UserServiceFactory.getUserService().isUserAdmin()) {
							sb.append("\t\t\t<li><a href=\"/ManageAUsers.jsp?aid=" + longAccountId + "\"><i class='fa fa-users'></i><span>User Manager</span></a></li>\r\n");
							sb.append("\t\t\t<li><a href=\"/ManageChannels.jsp?aid=" + longAccountId + "\">Channel Manager</a></li>\r\n");
							sb.append("\t\t\t<li><a href=\"/EditTaggingSettings.jsp?aid=" + longAccountId + "\">Tagging Settings</a></li>\r\n");
							sb.append("\t\t\t<li><a href=\"/AnalyticsSettings.jsp?"
									+ "AnalyticsSettings&aid=" + longAccountId + "\">GA Settings</a></li>\r\n");
						}
						sb.append("\t\t</ul>\r\n");
						sb.append("\t</li>\r\n");
					}
				}
			}
			sb.append("\t<li><a target=\"_blank\" href=\"https://campaignalyzer.com/support\" class=\"mws-i-24 i-info-about\" style='display:block; width:50px;'>&nbsp;</a></li>\r\n");
			sb.append("</ul>\r\n</div>\r\n<!-- End Navigation Links -->\r\n");

			return sb.toString();
		}
	
	// end of Top Nav

	public static String renderFooter() {
		StringBuilder sb = new StringBuilder();

        sb.append("</div>\r\n");
        sb.append("<!-- Inner Container End -->\r\n");
        sb.append("<!-- Footer -->\r\n");
        sb.append("<div class='page-footer'><div class='page-footer-inner'>");
  		sb.append("&copy; Copyright 2015 CampaignAlyzer. All rights reserved. | " +
  				"<a target=\"_blank\" href=\"https://campaignalyzer.com/\">Home</a> | " +
  				"<a target=\"_blank\" href=\"https://campaignalyzer.com/terms\">Terms Of Use</a> | " +
  				"<a target=\"_blank\" href=\"https://campaignalyzer.com/privacy-policy\">Privacy Policy</a> | " +
  				/*"<a target=\"_blank\" href=\"http://campaignalyzer.com/help.html\">Help</a> | " +*/
  				"<a target=\"_blank\" href=\"https://campaignalyzer.com/contact-us\">Contact Us</a>\r\n");
        sb.append("\t</div>\r\n</div>\r\n");
        sb.append("<!-- Main Container End -->\r\n");
        sb.append("</div><div class='scroll-to-top'><i class='icon-arrow-up'></i>");
        sb.append("\t</div></div>\r\n");
        

		//sb.append(" <!--[if lt IE 9]>"
        /*sb.append("<script src='/assets/global/plugins/respond.min.js'></script>"
		+"<script src='/assets/global/plugins/excanvas.min.js'></script>" 
		+"<![endif]-->"
        +"<!-- BEGIN CORE PLUGINS -->"
        +"<script src='../assets/global/plugins/jquery.min.js' type='text/javascript'></script>"
        +"<script src='../assets/global/plugins/bootstrap/js/bootstrap.min.js' type='text/javascript'></script>"
        +"<script src='../assets/global/plugins/js.cookie.min.js' type='text/javascript'></script>"
        +"<script src='../assets/global/plugins/bootstrap-hover-dropdown/bootstrap-hover-dropdown.min.js' type='text/javascript'></script>"
        +"<script src='../assets/global/plugins/jquery-slimscroll/jquery.slimscroll.min.js' type='text/javascript'></script>"
        +"<script src='../assets/global/plugins/jquery.blockui.min.js' type='text/javascript'></script>"
        +"<script src='../assets/global/plugins/uniform/jquery.uniform.min.js' type='text/javascript'></script>"
        +"<script src='../assets/global/plugins/bootstrap-switch/js/bootstrap-switch.min.js' type='text/javascript'></script>"
        +"<!-- END CORE PLUGINS -->"
        +"<!-- BEGIN PAGE LEVEL PLUGINS -->"
        +"<script src='../assets/global/scripts/datatable.js' type='text/javascript'></script>"
        +"<script src='../assets/global/scripts/datatable.min.js' type='text/javascript'></script>"
        +"<script src='/assets/pages/scripts/table-datatables-managed.min.js' type='text/javascript'></script>"
        +"<script src='../assets/global/plugins/datatables/plugins/bootstrap/datatables.bootstrap.js' type='text/javascript'></script>"
        +"<script src='../assets/global/plugins/moment.min.js' type='text/javascript'></script>"
        +"<script src='../assets/global/plugins/bootstrap-daterangepicker/daterangepicker.min.js' type='text/javascript'></script>"
        +"<script src='../assets/global/plugins/morris/morris.min.js' type='text/javascript'></script>"
        +"<script src='../assets/global/plugins/morris/raphael-min.js' type='text/javascript'></script>"
        +"<script src='../assets/global/plugins/counterup/jquery.waypoints.min.js' type='text/javascript'></script>"
        +"<script src='../assets/global/plugins/amcharts/amcharts/amcharts.js' type='text/javascript'></script>"
        +"<script src='../assets/global/plugins/amcharts/amcharts/serial.js' type='text/javascript'></script>"
        +"<script src='../assets/global/plugins/amcharts/amcharts/pie.js' type='text/javascript'></script>"
        +"<script src='../assets/global/plugins/amcharts/amcharts/radar.js' type='text/javascript'></script>"
        +"<script src='../assets/global/plugins/amcharts/amcharts/themes/light.js' type='text/javascript'></script>"
        +"<script src='../assets/global/plugins/amcharts/amcharts/themes/patterns.js' type='text/javascript'></script>"
        +"<script src='../assets/global/plugins/amcharts/amcharts/themes/chalk.js' type='text/javascript'></script>"
        +"<script src='../assets/global/plugins/amcharts/ammap/ammap.js' type='text/javascript'></script>"
        +"<script src='../assets/global/plugins/amcharts/ammap/maps/js/worldLow.js' type='text/javascript'></script>"
        +"<script src='../assets/global/plugins/amcharts/amstockcharts/amstock.js' type='text/javascript'></script>"
        +"<script src='../assets/global/plugins/fullcalendar/fullcalendar.min.js' type='text/javascript'></script>"
        +"<script src='../assets/global/plugins/flot/jquery.flot.min.js' type='text/javascript'></script>"
        +"<script src='../assets/global/plugins/flot/jquery.flot.resize.min.js' type='text/javascript'></script>"
        +"<script src='../assets/global/plugins/flot/jquery.flot.categories.min.js' type='text/javascript'></script>"
        +"<script src='../assets/global/plugins/jquery-easypiechart/jquery.easypiechart.min.js' type='text/javascript'></script>"
        +"<script src='../assets/global/plugins/jquery.sparkline.min.js' type='text/javascript'></script>"
        
        +"<script src='../assets/global/scripts/app.min.js' type='text/javascript'></script>"
       
        +"<!-- <script src='../assets/pages/scripts/dashboard.min.js' type='text/javascript'></script>-->"
       
        +"<script src='../assets/layouts/layout2/scripts/layout.min.js' type='text/javascript'></script>"
        +"<script src='../assets/layouts/layout2/scripts/demo.min.js' type='text/javascript'></script>"
        +"<script src='../assets/layouts/global/scripts/quick-sidebar.min.js' type='text/javascript'></script>");  */
    	sb.append("<script src='https://apis.google.com/js/platform.js?onload=onLoad' async defer></script>");
    	//sb.append("<script>$('#dashboardlink').attr('href', '/dashboard.jsp?aid='+$('#aid option:first').val())</script>");
    	//sb.append("<script src='../assets/pages/scripts/login-5.min.js' type='text/javascript'></script>");
    	sb.append("<script type='text/javascript'>defautAvatar();</script>");
		sb.append("</body>\r\n");
		sb.append("</html>\r\n");
		
		
	
		return sb.toString();
	}

	// handles null string variables and returns empty strings
	public static String handleNullWhiteSpaces(String s) {
		if (s == null) {
			return "";
		}
		return s.trim();
	}

	// handles null and parsing exceptions by returning zero
	public static long handleNullforLongNumbers(String s) {
		long l;
		try {
			l = Long.parseLong(s);
		} catch (Exception e) {
			return 0;
		}
		return l;
	}

	public static String encode(String input) {
		String output = input;

		try {
			output = URLEncoder.encode(input,"UTF-8");
		} catch(UnsupportedEncodingException ex) {
			// TODO:
			// do nothing
		}

		return output;
	}
}