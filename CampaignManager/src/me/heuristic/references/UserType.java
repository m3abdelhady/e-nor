package me.heuristic.references;

import java.util.HashSet;
import java.util.Set;

public class UserType {
	public static final String ADMIN = "Administrator";
	public static final String EDITOR = "Editor";
	public static final String VIEWER = "Viewer";

	public static final String PERMISSION_VIEW_ACCOUNT = "PERMISSION_VIEW_ACCOUNT";
	public static final String PERMISSION_MANAGE_ACCOUNT = "PERMISSION_MANAGE_ACCOUNT";

	public static final String PERMISSION_VIEW_USERS = "PERMISSION_VIEW_USERS";
	public static final String PERMISSION_MANAGE_USERS = "PERMISSION_MANAGE_USERS";
	public static final String PERMISSION_MANAGE_CHANNELS = "PERMISSION_MANAGE_CHANNELS";

	public static final String PERMISSION_EDIT_TAGGING_SETTINGS = "PERMISSION_EDIT_TAGGING_SETTINGS";
	public static final String PERMISSION_EDIT_TAGGING_PARAMETERS = "PERMISSION_EDIT_TAGGING_PARAMETERS";

	public static final String PERMISSION_VIEW_CAMPAIGNS = "PERMISSION_VIEW_CAMPAIGNS";
	public static final String PERMISSION_MANAGE_CAMPAIGNS = "PERMISSION_MANAGE_CAMPAIGNS";
	
	public static final String PERMISSION_VIEW_TAGGED_URLS = "PERMISSION_VIEW_TAGGED_URLS";
	public static final String PERMISSION_MANAGE_TAGGED_URLS = "PERMISSION_MANAGE_TAGGED_URLS";
	
	public static final String PERMISSION_EXPORT_CAMPAIGNS = "PERMISSION_EXPORT_CAMPAIGNS";
	public static final String PERMISSION_IMPORT_CAMPAIGNS = "PERMISSION_IMPORT_CAMPAIGNS";

	public static final Set<String> viewerPermissions = new HashSet<String>();
	public static final Set<String> editorPermissions = new HashSet<String>();
	public static final Set<String> adminPermissions = new HashSet<String>();

	static {
		// viewer
		viewerPermissions.add(PERMISSION_VIEW_ACCOUNT);
		viewerPermissions.add(PERMISSION_VIEW_USERS);
		viewerPermissions.add(PERMISSION_VIEW_CAMPAIGNS);
		viewerPermissions.add(PERMISSION_VIEW_TAGGED_URLS);
		viewerPermissions.add(PERMISSION_EXPORT_CAMPAIGNS);

		// editor
		editorPermissions.addAll(viewerPermissions);
		editorPermissions.add(PERMISSION_MANAGE_CAMPAIGNS);
		editorPermissions.add(PERMISSION_MANAGE_TAGGED_URLS);
		editorPermissions.add(PERMISSION_IMPORT_CAMPAIGNS);

		// admin
		adminPermissions.addAll(editorPermissions);
		adminPermissions.add(PERMISSION_MANAGE_ACCOUNT);
		adminPermissions.add(PERMISSION_MANAGE_USERS);
		adminPermissions.add(PERMISSION_MANAGE_CHANNELS);
		adminPermissions.add(PERMISSION_EDIT_TAGGING_SETTINGS);
		adminPermissions.add(PERMISSION_EDIT_TAGGING_PARAMETERS);
	}

	public static boolean hasPermission(String myRole, String permission) {
		if (ADMIN.equalsIgnoreCase(myRole)) {
			return adminPermissions.contains(permission);
		} else if (EDITOR.equalsIgnoreCase(myRole)) {
			return editorPermissions.contains(permission);
		} else if (VIEWER.equalsIgnoreCase(myRole)) {
			return viewerPermissions.contains(permission);
		} else {
			return false;	
		}
	}
}
