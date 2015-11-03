package com.alks.service.aws;

import java.net.URLDecoder;
import java.util.List;

import org.apache.log4j.Logger;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.identitymanagement.AmazonIdentityManagementClient;
import com.amazonaws.services.identitymanagement.model.AccessKey;
import com.amazonaws.services.identitymanagement.model.AccessKeyMetadata;
import com.amazonaws.services.identitymanagement.model.CreateAccessKeyResult;
import com.amazonaws.services.identitymanagement.model.DeleteAccessKeyRequest;
import com.amazonaws.services.identitymanagement.model.GetRoleRequest;
import com.amazonaws.services.identitymanagement.model.GetRoleResult;
import com.amazonaws.services.identitymanagement.model.ListAccessKeysResult;
import com.amazonaws.services.identitymanagement.model.ListAccountAliasesResult;
import com.amazonaws.services.identitymanagement.model.Role;
import com.alks.model.aws.LongTermKey;

/**
 * Manages a Amazon Identity Management Client
 * 
 * @author Shoban Sriram
 * @author Kamen Tsvetkov
 *
 */
public class MasterIAMService {

	private static Logger logger = Logger.getLogger(MasterIAMService.class);
	private AmazonIdentityManagementClient client = null;
	// private DynamoDBMapper mapper = null;
	// private DynamoDB dynamoDB = null;

	/**
	 * Constructor initializing the client from a new BaseAWS
	 * 
	 */
	public MasterIAMService() {
		BaseAWS baseAWS = new BaseAWS();
		client = new AmazonIdentityManagementClient(baseAWS.getCredentials());

		// Region regionWest2 = Region.getRegion(Regions.US_EAST_1);
		// client.setRegion(regionWest2);
		// mapper = new DynamoDBMapper(client);
		// dynamoDB = new DynamoDB(client);
	}

	/**
	 * Constructor initializing the client from a secret key and access key
	 * 
	 */
	public MasterIAMService(String accessKey, String secretKey) {
		client = new AmazonIdentityManagementClient(new BasicAWSCredentials(accessKey,secretKey));
	}
	

	/**
	 * Returns the Amazon Identity Management Client
	 * 
	 * @return client
	 */
	public AmazonIdentityManagementClient getClient() {
		return client;
	}


	/**
	 * Sets the Amazon Identity Management Client
	 * 
	 * @return client
	 */
	public void setClient(AmazonIdentityManagementClient client) {
		this.client = client;
	}

	/**
	 * Returns a list of roles given
	 * the access key and the secret key
	 * 
	 * @param accessKey
	 * @param secretKey
	 * @return list of roles
	 */
	public static List<Role> getRolesByKeys(String accessKey, String secretKey) {
		BasicAWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);
		AmazonIdentityManagementClient aimc = new AmazonIdentityManagementClient(credentials);
		List<Role> roles = aimc.listRoles().getRoles() ;
		return roles;
	}
	
	/**
	 * Returns the account number given
	 * the access key and the secret key
	 * 
	 * @param accessKey
	 * @param secretKey
	 * @return account number
	 */
	public static String[] getAccountNumberByKeys(String accessKey, String secretKey){
		BasicAWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);
		
		AmazonIdentityManagementClient aimc = new AmazonIdentityManagementClient(credentials);
		
		ListAccountAliasesResult alias = aimc.listAccountAliases();
		logger.info(alias.getAccountAliases().get(0));		
		logger.info(aimc.getUser());
		logger.info(aimc.getUser().getUser().getArn());
		logger.info(aimc.getUser().getUser().getUserName());
	       
		String msg = aimc.getUser().getUser().getArn();
		int arnIdx = msg.indexOf("::")+2;
		String[] account = new String[3];
	        if (arnIdx != -1) {
	            String arn = msg.substring(arnIdx, arnIdx+12);
	            account[0] = arn;
	        }
	        //Account Alias
	        account[1] = alias.getAccountAliases().get(0);
	        account[2] = aimc.getUser().getUser().getUserName();
	        
	       return account;
	}

	/**
	 * Returns a list of policies given the access key,
	 * secret key, and role name
	 * 
	 * @param accessKey
	 * @param secretKey
	 * @param roleName
	 * @return list of policies
	 * @throws Exception
	 */
	public static String getPolicyByRole(String accessKey, String secretKey, String roleName) throws Exception {
		BasicAWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);
		AmazonIdentityManagementClient aimc = new AmazonIdentityManagementClient(credentials);
		
		GetRoleRequest grr = new GetRoleRequest();
		grr.setRoleName(roleName);
		GetRoleResult grresult = aimc.getRole(grr);
		Role role = grresult.getRole();
//		GetRolePolicyRequest grpr = new GetRolePolicyRequest();
//		//Have to set the role name and the policy name (both are mandatory fields)
//		grpr.setRoleName(role);
//		GetRolePolicyResult rpr = aimc.getRolePolicy(grpr);
//		List<String> policies = new ArrayList<String>();
		return URLDecoder.decode(role.getAssumeRolePolicyDocument(),"UTF-8");
	}
	
	/**
	 * Refreshes the keys returning the long term keys
	 * 
	 * @return long term keys
	 */
	public LongTermKey refreshKeys(){
	
		LongTermKey ltk = new LongTermKey();
		if(client!=null){
			ListAccessKeysResult lakr = client.listAccessKeys();
			List<AccessKeyMetadata> listAkmd = lakr.getAccessKeyMetadata();
			AccessKeyMetadata akmd = listAkmd.get(0);
			CreateAccessKeyResult cakr = client.createAccessKey();
			AccessKey ak = cakr.getAccessKey();
			ltk.setAccessKey(ak.getAccessKeyId());
			ltk.setSecretKey(ak.getSecretAccessKey());
			DeleteAccessKeyRequest dakr1 = new DeleteAccessKeyRequest();
			dakr1.withAccessKeyId(akmd.getAccessKeyId());
			client.deleteAccessKey(dakr1);
		}
		return ltk;
	}
}

