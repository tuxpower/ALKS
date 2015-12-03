package com.alks.service.impl;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import org.apache.log4j.Logger;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.identitymanagement.model.CreateAccessKeyRequest;
import com.amazonaws.services.securitytoken.AWSSecurityTokenServiceClient;
import com.amazonaws.services.securitytoken.model.Credentials;
import com.amazonaws.services.securitytoken.model.GetFederationTokenRequest;
import com.amazonaws.services.securitytoken.model.GetFederationTokenResult;
import com.amazonaws.util.json.JSONObject;

public class CreateKeyImpl {

	private static Logger logger = Logger.getLogger(CreateKeyImpl.class);

	CreateAccessKeyRequest cakr = new CreateAccessKeyRequest();

	/**
	 * Returns a string array containing secret/access keys,
	 * the session token, and URL
	 * 
	 * @param userName
	 * @param accessKey
	 * @param secretKey
	 * @param policy
	 * @param durationInSeconds
	 * @return string of key values
	 */
	public static String[] getKey(String userName, String accessKey, String secretKey, String policy, int durationInSeconds){
		
		AWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);

		AWSSecurityTokenServiceClient stsClient = new AWSSecurityTokenServiceClient(
				credentials);

		GetFederationTokenRequest getFederationTokenRequest = new GetFederationTokenRequest();
		getFederationTokenRequest.setDurationSeconds(durationInSeconds);
		getFederationTokenRequest.setName(userName);
		getFederationTokenRequest.setPolicy(policy);

		// Get the temporary security credentials.
		GetFederationTokenResult federationTokenResult = stsClient
				.getFederationToken(getFederationTokenRequest);

		Credentials sessionCredentials = federationTokenResult.getCredentials();
			
		String loginURL[] = new String[4];
		loginURL[0] = sessionCredentials.getAccessKeyId();
		loginURL[1] = sessionCredentials.getSecretAccessKey();
		loginURL[2] = sessionCredentials.getSessionToken();
		loginURL[3] = getUrl(sessionCredentials.getAccessKeyId(), sessionCredentials.getSecretAccessKey(), sessionCredentials.getSessionToken());

		return loginURL;
	}

	/**
	 * Returns a user-friendly session URL the user can use to
	 * be granted access 
	 * 
	 * @param accessKey
	 * @param secretKey
	 * @param sessionToken
	 * @return session URL
	 */
	public static String getUrl(String accessKey,String secretKey, String sessionToken){
		
		// The issuer parameter specifies your internal sign-in
		// page, for example https://mysignin.internal.mycompany.com/.
		// The console parameter specifies the URL to the destination console of the
		// AWS Management Console. This example goes to Amazon SNS.
		// The signin parameter is the URL to send the request to.
		//String issuerURL = "https://mysignin.internal.mycompany.com/";
		
		String consoleURL = "https://console.aws.amazon.com/";
		String signInURL = "https://signin.aws.amazon.com/federation";
		  
		
		// Create the sign-in token using temporary credentials,
		// including the Access Key ID,  Secret Access Key, and security token.
		String sessionJson = String.format(
		  "{\"%1$s\":\"%2$s\",\"%3$s\":\"%4$s\",\"%5$s\":\"%6$s\"}",
		  "sessionId", accessKey,
		  "sessionKey", secretKey,
		  "sessionToken", sessionToken);
		
		try{              
		String getSigninTokenURL = signInURL + "?Action=getSigninToken" +
		  "&SessionType=json&Session=" + URLEncoder.encode(sessionJson,
		  "UTF-8");
		URL url = new URL(getSigninTokenURL);
		URLConnection conn = url.openConnection ();
		BufferedReader bufferReader = new BufferedReader(new 
		  InputStreamReader(conn.getInputStream()));  
		String returnContent = bufferReader.readLine();
		String signinToken = new JSONObject(returnContent).getString("SigninToken");
		String signinTokenParameter = "&SigninToken=" + 
				  URLEncoder.encode(signinToken,"UTF-8");

			// The issuer parameter is optional, but recommended. Use it to direct users
			// to your sign-in page when their session expires.
			//String issuerParameter = "&Issuer=" + URLEncoder.encode(issuerURL, "UTF-8");
			String destinationParameter = "&Destination=" + 
			  URLEncoder.encode(consoleURL,"UTF-8");
			
			 return signInURL + "?Action=login" + signinTokenParameter + destinationParameter ; 
			//issuerParameter + ;
			
		}catch(Exception e){
		 e.printStackTrace();	
		}
		return null;
	}

	/**
	 * Returns a string array containing secret/access keys,
	 * the session token, and URL
	 * 
	 * @return string of key values
	 */
	@Deprecated
	public String[] getKey() {

		 AWSCredentials credentials = new
		 ProfileCredentialsProvider().getCredentials();
		 logger.info("Credentials AWS Access Key:"+credentials.getAWSAccessKeyId());
		 logger.info("Credentials AWS Secret Key:"+credentials.getAWSSecretKey());


		AWSSecurityTokenServiceClient stsClient = new AWSSecurityTokenServiceClient(
				new ProfileCredentialsProvider());

		GetFederationTokenRequest getFederationTokenRequest = new GetFederationTokenRequest();
		getFederationTokenRequest.setDurationSeconds(7200);
		getFederationTokenRequest.setName("shoban");

		// Define the policy and add to the request.
		//Policy policy = Policy.fromJson("{    \"Type\": \"AWS::IAM::Group\",    \"Properties\": {      \"Path\": \"/\"     }   }");

		//policy.withStatements(new Statement(Effect.Allow).withActions(
		//SNSActions.Subscribe).withResources(new Resource("*")));

		getFederationTokenRequest.setPolicy("{\"Statement\":[{\"Resource\":\"*\",\"Action\":\"*\",\"Effect\":\"Allow\"}],\"Version\":\"2012-10-17\"}");

		// Get the temporary security credentials.
		GetFederationTokenResult federationTokenResult = stsClient
				.getFederationToken(getFederationTokenRequest);

		Credentials sessionCredentials = federationTokenResult.getCredentials();

		logger.info("Session Credentials: AccessKey: " + sessionCredentials.getAccessKeyId());
		logger.info("Session Credentials: Session Toke: " + sessionCredentials.getSessionToken());
		logger.info("Session Credentials: Expiration Date/Time : " + sessionCredentials.getExpiration());

		// The issuer parameter specifies your internal sign-in
		// page, for example https://mysignin.internal.mycompany.com/.
		// The console parameter specifies the URL to the destination console of the
		// AWS Management Console. This example goes to Amazon SNS.
		// The signin parameter is the URL to send the request to.
		//String issuerURL = "https://mysignin.internal.mycompany.com/";
		
		String consoleURL = "https://console.aws.amazon.com/sns";
		String signInURL = "https://signin.aws.amazon.com/federation";
		  
		
		// Create the sign-in token using temporary credentials,
		// including the Access Key ID,  Secret Access Key, and security token.
		String sessionJson = String.format(
		  "{\"%1$s\":\"%2$s\",\"%3$s\":\"%4$s\",\"%5$s\":\"%6$s\"}",
		  "sessionId", sessionCredentials.getAccessKeyId(),
		  "sessionKey", sessionCredentials.getSecretAccessKey(),
		  "sessionToken", sessionCredentials.getSessionToken());
		
		String loginURL[] = new String[4];
		loginURL[0] = sessionCredentials.getAccessKeyId();
		loginURL[1] = sessionCredentials.getSecretAccessKey();
		loginURL[2] = sessionCredentials.getSessionToken();
		
		try{              
		String getSigninTokenURL = signInURL + "?Action=getSigninToken" +
		  "&SessionType=json&Session=" + URLEncoder.encode(sessionJson,
		  "UTF-8");
		
		URL url = new URL(getSigninTokenURL);
		URLConnection conn = url.openConnection ();
		BufferedReader bufferReader = new BufferedReader(new 
		  InputStreamReader(conn.getInputStream()));  
		String returnContent = bufferReader.readLine();
		String signinToken = new JSONObject(returnContent).getString("SigninToken");
		String signinTokenParameter = "&SigninToken=" + 
				  URLEncoder.encode(signinToken,"UTF-8");

			// The issuer parameter is optional, but recommended. Use it to direct users
			// to your sign-in page when their session expires.
			//String issuerParameter = "&Issuer=" + URLEncoder.encode(issuerURL, "UTF-8");
			//String destinationParameter = "&Destination=" + 
			  URLEncoder.encode(consoleURL,"UTF-8");
			 loginURL[3] = signInURL + "?Action=login" + signinTokenParameter ;// + 
					 // issuerParameter + destinationParameter;


			
		}catch(Exception e){
			
		}
		
		
		// Package the session credentials as a BasicSessionCredentials
		// object for an S3 client object to use.
		// BasicSessionCredentials basicSessionCredentials =
		// new BasicSessionCredentials(sessionCredentials.getAccessKeyId(),
		// sessionCredentials.getSecretAccessKey(),
		// sessionCredentials.getSessionToken());
		// AmazonS3Client s3 = new AmazonS3Client(basicSessionCredentials);
		//
		//
		// // Test. For example, send ListBucket request using the temporary
		// security credentials.
		// ObjectListing objects = s3.listObjects(bucketName);
		// logger.info("No. of Objects = " +
		// objects.getObjectSummaries().size());
		logger.info(loginURL);
		return loginURL;
	}

}
