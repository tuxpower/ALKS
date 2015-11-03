package com.alks.service.aws;

import org.apache.log4j.Logger;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.alks.dao.AccountIdADGroupDAO;
import com.alks.service.config.MessageUtils;

/**
 * The base AWS object used in the application
 * containing AWS credentials
 * 
 * @author Shoban Sriram
 * @author Kamen Tsvetkov
 *
 */
public class BaseAWS {

	private AWSCredentials credentials = null;
	private static Logger logger = Logger.getLogger(AccountIdADGroupDAO.class);
	
	/**
	 * Constructor for BaseAWS that initializes the AWS credentials using
	 * the main access key and secret key
	 * 
	 */
	public BaseAWS(){
		//TODO should read from a properties file instead of the standard folder structure
		 credentials = new BasicAWSCredentials(MessageUtils.getMessage("alks.dynamodb.accessKey"),MessageUtils.getMessage("alks.dynamodb.secretKey"));
	}
	
	/**
	 * Return the AWS credentials
	 * 
	 * @return AWS credentials
	 */
	public AWSCredentials getCredentials(){
		return credentials;
	}
	
}
