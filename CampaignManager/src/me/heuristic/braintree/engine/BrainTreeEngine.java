package me.heuristic.braintree.engine;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Properties;

import me.heuristic.braintree.engine.exceptions.CustomerCreationException;
import me.heuristic.braintree.engine.exceptions.PaymntMethodCreationException;
import me.heuristic.braintree.engine.exceptions.SubscriptionCreationException;



import com.braintreegateway.BraintreeGateway;
import com.braintreegateway.ClientTokenRequest;
import com.braintreegateway.Customer;
import com.braintreegateway.CustomerRequest;
import com.braintreegateway.Environment;
import com.braintreegateway.PaymentMethod;
import com.braintreegateway.PaymentMethodRequest;
import com.braintreegateway.Result;
import com.braintreegateway.Subscription;
import com.braintreegateway.SubscriptionRequest;
import com.braintreegateway.Transaction;
import com.braintreegateway.TransactionRequest;


public class BrainTreeEngine {

	private static BrainTreeEngine instance = null;
	private Environment env;
	private String merchantKey;
	private String publicKey;
	private String privateKey;
	
	private BraintreeGateway gateway;
	
	private BigDecimal premiumAmount;
	private BigDecimal plusAmount;
	private BigDecimal standardAmount;
	private BigDecimal basicAmount;
	private BigDecimal professionalAmount;
	private BigDecimal testAmount;
	
	private String premiumPlanId;
	private String plusPlanId;
	private String standardPlanId;
	private String basicPlanId;
	private String professionalPlanId;
	private String testPlanId;
	
	private String checkOutUrl;

	public String getCheckOutUrl() {
		return checkOutUrl;
	}

	public void setCheckOutUrl(String checkOutUrl) {
		this.checkOutUrl = checkOutUrl;
	}

	public static final String BrainTreeServelt = "/braintreeservlet";

	public BigDecimal getPremiumAmount() {
		return premiumAmount;
	}

	public BigDecimal getPlusAmount() {
		return plusAmount;
	}

	public BigDecimal getStandardAmount() {
		return standardAmount;
	}

	public BigDecimal getBasicAmount() {
		return basicAmount;
	}
	
	public BigDecimal getProfessionalAmount() {
		return professionalAmount;
	}
	
	public BigDecimal getTestAmount() {
		return testAmount;
	}
	
	public Environment getEnv() {
		return env;
	}

	public String getMerchantKey() {
		return merchantKey;
	}

	public String getPublicKey() {
		return publicKey;
	}

	public String getPrivateKey() {
		return privateKey;
	}

	public BraintreeGateway getGateway() {
		return gateway;
	}

	public static BrainTreeEngine getInstance() {
		if (instance == null) {
			instance = new BrainTreeEngine();
		}
		return instance;
	}

	private BrainTreeEngine() {
		Properties prop = new Properties();
		try {
			prop.load(this.getClass().getResourceAsStream(
					"/me/heuristic/braintree/properties/braintree.properties"));

			String strEnv = prop.getProperty("entironment");
			merchantKey = prop.getProperty("merchantId");
			publicKey = prop.getProperty("publickey");
			privateKey = prop.getProperty("privatekey");

			premiumAmount = new BigDecimal(prop.getProperty("premium"));
			plusAmount = new BigDecimal(prop.getProperty("plus"));
			standardAmount = new BigDecimal(prop.getProperty("standard"));
			basicAmount = new BigDecimal(prop.getProperty("basic"));
			professionalAmount = new BigDecimal(prop.getProperty("professional"));
			testAmount = new BigDecimal(prop.getProperty("test"));

			premiumPlanId = prop.getProperty("premiumPlanId");
			plusPlanId = prop.getProperty("plusPlanId");
			standardPlanId = prop.getProperty("standardPlanId");
			basicPlanId = prop.getProperty("basicPlanId");
			professionalPlanId = prop.getProperty("professionalPlanId");
			testPlanId = prop.getProperty("testPlanId");
			checkOutUrl = prop.getProperty("checkOutUrl");

			env = Environment.SANDBOX;

			if (strEnv.equals("DEVELOPMENT")) {
				env = Environment.DEVELOPMENT;
			} else if (strEnv.equals("SANDBOX")) {
				env = Environment.SANDBOX;
			} else if (strEnv.equals("PRODUCTION")) {
				env = Environment.PRODUCTION;
			}

			gateway = new BraintreeGateway(env, merchantKey,
					publicKey, privateKey);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String getPremiumPlanId() {
		return premiumPlanId;
	}

	public void setPremiumPlanId(String premiumPlanId) {
		this.premiumPlanId = premiumPlanId;
	}

	public String getPlusPlanId() {
		return plusPlanId;
	}

	public void setPlusPlanId(String plusPlanId) {
		this.plusPlanId = plusPlanId;
	}

	public String getStandardPlanId() {
		return standardPlanId;
	}

	public void setStandardPlanId(String standardPlanId) {
		this.standardPlanId = standardPlanId;
	}
	
	public String getBasicPlanId() {
		return basicPlanId;
	}

	public void setBasicPlanId(String basicPlanId) {
		this.basicPlanId = basicPlanId;
	}
	
	public String getProfessionalPlanId() {
		return professionalPlanId;
	}

	public void setProfessionalPlanId(String professionalPlanId) {
		this.professionalPlanId = professionalPlanId;
	}
	
	public String getTestPlanId() {
		return testPlanId;
	}

	public void setTestPlanId(String testPlanId) {
		this.testPlanId = testPlanId;
	}
	
	

	public String GetClientToken(String clientId) {
		ClientTokenRequest clientTokenRequest = new ClientTokenRequest()
				.customerId(clientId);
		String clientToken = gateway.clientToken().generate(clientTokenRequest);
		return clientToken;

	}

	public String GetClientToken() {
		String clientToken = gateway.clientToken().generate();
		return clientToken;
	}

	public boolean doPayment(String nonce, PaymentPlan plan) {
		BigDecimal amount = null;

		switch (plan) {
		case Premium:
			amount = premiumAmount;
			break;
		case Plus:
			amount = plusAmount;
			break;
		case Standard:
			amount = standardAmount;
			break;
		case Basic:
			amount = basicAmount;
			break;
		case Professional:
			amount = professionalAmount;
			break;

		default:
			amount = new BigDecimal(0.0);
			break;
		}

		TransactionRequest request = new TransactionRequest().amount(amount)
				.paymentMethodNonce(nonce);
		Result<Transaction> result = gateway.transaction().sale(request);
		return result.isSuccess();
	}

	public void cancelSubscribtion(String subscriptionId){	

		Result<Subscription> SubResult = gateway.subscription().cancel(subscriptionId);
		

	}
	
	/*public void refundDuringFreeTrial(String AccoundId){
		
		Result<Transaction> Refresult = gateway.transaction().refund(AccoundId);
	}*/

	@SuppressWarnings("unchecked")
	public void updateSubscribtion(String firstName, String lastName, String email,
			String paymentNonce, String AccountId, String PlanId,
			String brainCid) throws SubscriptionCreationException,
			CustomerCreationException, PaymntMethodCreationException {
				
		if (paymentNonce == null) {
			throw new PaymntMethodCreationException();
		}

		String cId = "";

		Customer customerResult = gateway.customer().find(brainCid);

		if (customerResult == null) {

			CustomerRequest customerRequest = new CustomerRequest()
					.firstName(firstName).lastName(lastName)
					.email(email)
					.paymentMethodNonce(paymentNonce);

			Result<Customer> customerCreationResult = gateway.customer()
					.create(customerRequest);
			if (customerCreationResult.isSuccess()) {
				cId = customerCreationResult.getTarget().getId();
			} else {
				throw new CustomerCreationException();

			}
		} else {
			cId = customerResult.getId();
		}

		PaymentMethodRequest paymentMethodRequest = new PaymentMethodRequest()
				.customerId(cId).paymentMethodNonce(paymentNonce);

		Result<PaymentMethod> paymentMethodResult = (Result<PaymentMethod>) gateway
				.paymentMethod().create(paymentMethodRequest);		
		
		if (paymentMethodResult.isSuccess()) {			
			BigDecimal np = null;
			switch (PlanId) {			
			case "Basic":
				np = basicAmount;
				break;
			case "Professional":
				np = professionalAmount;
				break;

			default:
				np = new BigDecimal(0.0);
				break;
			}
			SubscriptionRequest subscriptionRequest = new SubscriptionRequest()
					.id(AccountId)
					.paymentMethodToken(paymentMethodResult.getTarget().getToken())
					.price(np)
					.planId(PlanId).options().prorateCharges(true)
					.revertSubscriptionOnProrationFailure(true).done();

			Result<Subscription> SubResult = gateway.subscription().update(
					AccountId, subscriptionRequest);
			
			if (SubResult.getErrors() != null) {
				throw new SubscriptionCreationException();
			}
		} else {
			throw new PaymntMethodCreationException();
		}
	}

	public String createSubscribtion(String firstName, String lastName, String email,
			String paymentNonce, String AccountId, String PlanId)
			throws SubscriptionCreationException, CustomerCreationException,
			PaymntMethodCreationException {

		if (paymentNonce == null) {
			throw new PaymntMethodCreationException();
		}

		String customerId = null;
		CustomerRequest customerRequest = new CustomerRequest()
				.firstName(firstName).lastName(lastName)
				.email(email)
				.paymentMethodNonce(paymentNonce);

		Result<Customer> customerResult = gateway.customer().create(
				customerRequest);

		if (customerResult.isSuccess()) {
			Customer customer = customerResult.getTarget();
			customerId = customer.getId();
			
			SubscriptionRequest subscriptionRequest = new SubscriptionRequest()
					.id(AccountId)
					.paymentMethodToken(
							customer.getPaymentMethods().get(0).getToken())
					.planId(PlanId);
			Result<Subscription> SubResult = gateway.subscription().create(
					subscriptionRequest);
			
			if (SubResult.getErrors() != null) {
				throw new SubscriptionCreationException();
			}
		} else {
			throw new CustomerCreationException();
		}
		return customerId;

	}
}