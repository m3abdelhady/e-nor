package me.heuristic.references;

import java.util.ArrayList;
import java.util.List;

public class Channels {
	public static final String AFFILIATE = "affiliate";
	public static final String BANNER = "banner";
	public static final String DIRECT_MAIL = "direct mail";
	public static final String EMAIL = "email";
	public static final String INSTANT_MESSAGING = "instant messaging";
	public static final String PAID_LISTING = "paid listing";
	public static final String PRINT_MEDIA = "print media";
	public static final String CPC = "cpc";
	public static final String SOCIAL = "social";
	public static final String OFFLINE = "offline";
	public static final int MAX_CHANNELS = 32;

	public static final List<String> defaults = new ArrayList<String>();
	
	static {
		defaults.add(AFFILIATE);
		defaults.add(BANNER);
		defaults.add(DIRECT_MAIL);
		defaults.add(EMAIL);
		defaults.add(INSTANT_MESSAGING);
		defaults.add(PAID_LISTING);
		defaults.add(PRINT_MEDIA);
		defaults.add(CPC);
		defaults.add(SOCIAL);
		defaults.add(OFFLINE);
	}
}
