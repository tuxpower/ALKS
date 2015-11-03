package com.alks.dao;

import java.text.SimpleDateFormat;
import org.apache.log4j.Logger;
import com.amazonaws.regions.RegionUtils;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.alks.service.aws.BaseAWS;
import com.alks.service.config.MessageUtils;

/**
 * The base for any database class to establish connection to the database
 * 
 * @author Shoban Sriram
 * @author Kamen Tsvetkov
 *
 */
public class BaseDAO {

	private static Logger logger = Logger.getLogger(BaseDAO.class);

	 protected AmazonDynamoDBClient client = null;
	 protected DynamoDBMapper mapper = null;
	 protected DynamoDB dynamoDB = null;
	 static SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat(MessageUtils.getMessage("alks.date.format"));	 
	 
	/**
	 * Constructor for BaseDAO setting credentials and settings for
	 * the database requests
	 *  
	 */
	public BaseDAO(){
		BaseAWS baseAWS = new BaseAWS();
		client = new AmazonDynamoDBClient(baseAWS.getCredentials());
		try{
			client.setRegion(RegionUtils.getRegion(MessageUtils.getMessage("alks.dynamodb.region")));
			mapper = new DynamoDBMapper(client);
			dynamoDB = new DynamoDB(client);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 * Return an object mapper for interacting with DynamoDB
	 * 
	 * @return mapper
	 */
	public DynamoDBMapper getMapper(){
		return mapper;
	}
	
	/**
	 * Return an instance of DynamoDB
	 * 
	 * @return dynamoDB
	 */
	public DynamoDB getDynamoDB(){
		return dynamoDB;
	}
	
	public AmazonDynamoDBClient getClient(){
		return client;
	}
		
}
