package me.heuristic.references;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/*import com.braintreegateway.Plan;*/

import me.heuristic.braintree.engine.BrainTreeEngine;

public class AccountType {
	public static final String NEW = "New";
	public static final String TEST = "Test";
	public static final String FREE = "Free";
	public static final String STANDARD = "Standard";
	public static final String PLUS = "Plus";
	public static final String PREMIUM = "Premium";
	public static final String BASIC = "Basic";
	public static final String PROFESSIONAL = "Professional";
	public static final String TRAIL = "Free Trail";

	
	

	public static final String PERMISSION_MANAGE_USERS = "PERMISSION_MANAGE_USERS";
	public static final String PERMISSION_IMPORT_CAMPAIGNS = "PERMISSION_IMPORT_CAMPAIGNS";
	public static final String PERMISSION_EXPORT_CAMPAIGNS = "PERMISSION_EXPORT_CAMPAIGNS";

	public static final Map<String, Integer> maxUsersMap = new HashMap<String, Integer>();
	public static final Map<String, Integer> maxCampaignsMap = new HashMap<String, Integer>();
	public static final Map<String, Double> priceMap = new HashMap<String, Double>();
	public static final Map<String, String> PlanMap = new HashMap<String, String>();
	
	public static final Set<String> manageUsersPermissions = new HashSet<String>();
	public static final Set<String> importPermissions = new HashSet<String>();
	public static final Set<String> exportPermissions = new HashSet<String>();

	static {
		maxUsersMap.put(NEW, 1);
		maxUsersMap.put(TEST, 10);
		maxUsersMap.put(FREE, 50);
		maxUsersMap.put(STANDARD, 5);
		maxUsersMap.put(PLUS, 30);
		maxUsersMap.put(PREMIUM, 100);
		maxUsersMap.put(BASIC, 10);// 10
		maxUsersMap.put(PROFESSIONAL, 50);// 10
		maxUsersMap.put(TRAIL, 10);// 10


		maxCampaignsMap.put(NEW, 0); // 0
		maxCampaignsMap.put(TEST, 3); // 1
		maxCampaignsMap.put(FREE, 100); // 1
		maxCampaignsMap.put(STANDARD, 20); // 20
		maxCampaignsMap.put(PLUS, 100);	// 100
		maxCampaignsMap.put(PREMIUM, 500);// 500
		maxCampaignsMap.put(BASIC, 100);// 100
		maxCampaignsMap.put(PROFESSIONAL, 500);
		maxCampaignsMap.put(TRAIL, 100);// 100


		priceMap.put(NEW, 0.0);
		priceMap.put(TEST, BrainTreeEngine.getInstance().getTestAmount().doubleValue());
		priceMap.put(FREE, 0.0);	// 19
		priceMap.put(STANDARD, BrainTreeEngine.getInstance().getStandardAmount().doubleValue());	// 19
		priceMap.put(PLUS, BrainTreeEngine.getInstance().getPlusAmount().doubleValue());		// 29
		priceMap.put(PREMIUM, BrainTreeEngine.getInstance().getPremiumAmount().doubleValue());	// 79
		priceMap.put(BASIC, BrainTreeEngine.getInstance().getBasicAmount().doubleValue());	// 99
		priceMap.put(PROFESSIONAL, BrainTreeEngine.getInstance().getProfessionalAmount().doubleValue());	// 499
		priceMap.put(TRAIL, 0.0);

		
		PlanMap.put(TEST, BrainTreeEngine.getInstance().getTestPlanId());
		PlanMap.put(STANDARD, BrainTreeEngine.getInstance().getStandardPlanId());
		PlanMap.put(PLUS, BrainTreeEngine.getInstance().getPlusPlanId());
		PlanMap.put(PREMIUM, BrainTreeEngine.getInstance().getPremiumPlanId());
		PlanMap.put(BASIC,BrainTreeEngine.getInstance().getBasicPlanId());
		PlanMap.put(PROFESSIONAL,BrainTreeEngine.getInstance().getProfessionalPlanId());
		

		manageUsersPermissions.add(TEST);
		manageUsersPermissions.add(FREE);
		manageUsersPermissions.add(STANDARD);
		manageUsersPermissions.add(PLUS);
		manageUsersPermissions.add(PREMIUM);
		manageUsersPermissions.add(BASIC);
		manageUsersPermissions.add(PROFESSIONAL);
		manageUsersPermissions.add(TRAIL);


		exportPermissions.add(TEST);
		exportPermissions.add(FREE);
		//exportPermissions.add(STANDARD);
		exportPermissions.add(PLUS);
		exportPermissions.add(PREMIUM);
		exportPermissions.add(BASIC);
		exportPermissions.add(PROFESSIONAL);
		exportPermissions.add(TRAIL);


		importPermissions.add(TEST);
		importPermissions.add(FREE);
		//importPermissions.add(STANDARD);
		importPermissions.add(PLUS);
		importPermissions.add(PREMIUM);
		importPermissions.add(BASIC);
		importPermissions.add(PROFESSIONAL);
		importPermissions.add(TRAIL);

	}

	public static boolean allowsManageUsers(String accountType) {
		return manageUsersPermissions.contains(accountType);
	}

	public static boolean allowsExport(String accountType) {
		return exportPermissions.contains(accountType);
	}

	public static boolean allowsImport(String accountType) {
		return importPermissions.contains(accountType);
	}

	public static double calculateUpgradeFees(String currType, String newType) {
		if ((STANDARD.equals(currType) && PLUS.equals(newType)) ||
			(STANDARD.equals(currType) && PREMIUM.equals(newType)) ||
			(PLUS.equals(currType) && PREMIUM.equals(newType)) )
			return priceMap.get(newType) - priceMap.get(currType);
		else
			return 0.0;
	}
	
	/*public static double calculateUpgradeFees(String currType, String newType) {
		if (BASIC.equals(currType) && PROFESSIONAL.equals(newType))
			return priceMap.get(newType) - priceMap.get(currType);
		else
			return 0.0;
	}*/
}
