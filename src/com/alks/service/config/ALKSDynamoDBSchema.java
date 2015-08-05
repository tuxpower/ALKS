package com.alks.service.config;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.model.AttributeDefinition;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.KeySchemaElement;
import com.amazonaws.services.dynamodbv2.model.KeyType;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;

/**
 * A class that represents a database schema
 * helping facilitate database calls
 * 
 * @author Shoban Sriram
 * @author Kamen Tsvetkov
 *
 */
public class ALKSDynamoDBSchema {

	private static Logger logger = Logger.getLogger(ALKSDynamoDBSchema.class);
	static private AmazonDynamoDBClient client = null;
	static private DynamoDB dynamoDB = null;
	static SimpleDateFormat dateFormatter = new SimpleDateFormat(
			"yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

	static String accountsTable = "accounts";
	static String accountrolepolicyTable = "accountrolepolicy";
	static String accountidgroupTable = "accountidgroup";
	static long accountReads = 1L;
	static long accountWrites = 1L;
	static String stringType = "S";
	static String numberType = "N";

	/**
	 * Initializes a connection to dynamoDB
	 * 
	 */
	public static void initDyDB() {

		client = new AmazonDynamoDBClient(new BasicAWSCredentials("", ""));
		try {
			Region regionEast1 = Region.getRegion(Regions.US_EAST_1);
			client.setRegion(regionEast1);
			dynamoDB = new DynamoDB(client);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * Deletes a table from dynamoDB given the 
	 * tables name
	 * 
	 * @param tableName
	 */
	private static void deleteTable(String tableName) {

		try {
			Table table = dynamoDB.getTable(tableName);
			logger.info("Issuing DeleteTable request for " + tableName);
			table.delete();
			logger.info("Waiting for " + tableName
					+ " to be deleted...this may take a while...");
			table.waitForDelete();
		} catch (Exception e) {
			System.err.println("DeleteTable request failed for " + tableName);
			System.err.println(e.getMessage());
		}
	}

	/**
	 * Creates a table in dynamoDB setting rangeKeyName
	 * and rangeKeyType to null
	 * 
	 * @param tableName
	 * @param readCapacityUnits
	 * @param writeCapacityUnits
	 * @param hashKeyName
	 * @param hashKeyType
	 */
	private static void createTable(String tableName, long readCapacityUnits,
			long writeCapacityUnits, String hashKeyName, String hashKeyType) {
		createTable(tableName, readCapacityUnits, writeCapacityUnits,
				hashKeyName, hashKeyType, null, null);
	}

	/**
	 * Creates a table in dynamoDB
	 * 
	 * @param tableName
	 * @param readCapacityUnits
	 * @param writeCapacityUnits
	 * @param hashKeyName
	 * @param hashKeyType
	 * @param rangeKeyName
	 * @param rangeKeyType
	 */
	private static void createTable(String tableName, long readCapacityUnits,
			long writeCapacityUnits, String hashKeyName, String hashKeyType,
			String rangeKeyName, String rangeKeyType) {

		try {

			ArrayList<KeySchemaElement> keySchema = new ArrayList<KeySchemaElement>();
			keySchema.add(new KeySchemaElement().withAttributeName(hashKeyName)
					.withKeyType(KeyType.HASH));

			ArrayList<AttributeDefinition> attributeDefinitions = new ArrayList<AttributeDefinition>();
			attributeDefinitions.add(new AttributeDefinition()
					.withAttributeName(hashKeyName).withAttributeType(
							hashKeyType));

			if (rangeKeyName != null) {
				keySchema.add(new KeySchemaElement().withAttributeName(
						rangeKeyName).withKeyType(KeyType.RANGE));
				attributeDefinitions.add(new AttributeDefinition()
						.withAttributeName(rangeKeyName).withAttributeType(
								rangeKeyType));
			}

			CreateTableRequest request = new CreateTableRequest()
					.withTableName(tableName)
					.withKeySchema(keySchema)
					.withProvisionedThroughput(
							new ProvisionedThroughput().withReadCapacityUnits(
									readCapacityUnits).withWriteCapacityUnits(
									writeCapacityUnits));

			request.setAttributeDefinitions(attributeDefinitions);

			logger.info("Issuing CreateTable request for " + tableName);
			Table table = dynamoDB.createTable(request);
			logger.info("Waiting for " + tableName
					+ " to be created...this may take a while...");
			table.waitForActive();

		} catch (Exception e) {
			System.err.println("CreateTable request failed for " + tableName);
			System.err.println(e.getMessage());
		}
	}

	// private static void loadInitialAccounts(String tableName) {
	//
	// Table table = dynamoDB.getTable(tableName);
	//
	// try {
	//
	// logger.info("Adding data to " + tableName);
	// AccountsDAO actDAO = new AccountsDAO();
	// actDAO.saveAccount("568389488636", "svcalks account",
	// "AKIAI35USU4MHIRKRKQA", "CLU3gyBnaGkifp8ZXbybZv9EBS/goQ/M+qyxizkh",
	// "SystemLoad");
	// // Item item = new Item()
	// // .withPrimaryKey("accountNo", "568389488636")
	// // .withString("accountName", "svcalks account")
	// // .withString("accessKey", "AKIAI35USU4MHIRKRKQA")
	// // .withString("secretKey", "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")
	// // .withString("user", "SystemLoad")
	// // .withString("date", dateFormatter.format(new Date().getTime()))
	// // .withInt("active",1);
	// // table.putItem(item);
	//
	//
	// } catch (Exception e) {
	// System.err.println("Failed to create item in " + tableName);
	// System.err.println(e.getMessage());
	// }
	//
	// }

	// private static void loadInitialAccountRolePolicy(String tableName) {
	//
	// Table table = dynamoDB.getTable(tableName);
	//
	// AccountRolePolicyDAO arpDAO = new AccountRolePolicyDAO();
	// arpDAO.saveARP("568389488636",
	// "IAM-1-AdministratorAccessRole-V0QNHDF38138",
	// "{\"Statement\":[{\"Resource\":\"*\",\"Action\":\"*\",\"Effect\":\"Allow\"}],\"Version\":\"2012-10-17\"}",
	// "SystemLoad");
	// // try {
	// //
	// // logger.info("Adding data to " + tableName);
	// // //TODO Need to check on how can we pass an initial value to
	// autogenerated key column?
	// // Item item = new Item()
	// // .withString("accounts_accountNo", "568389488636")
	// // .withString("policy",
	// "{\"Statement\":[{\"Resource\":\"*\",\"Action\":\"*\",\"Effect\":\"Allow\"}],\"Version\":\"2012-10-17\"}")
	// // .withString("role","IAM-1-AdministratorAccessRole-V0QNHDF38138")
	// // .withString("user", "SystemLoad")
	// // .withString("date", dateFormatter.format(new Date().getTime()))
	// // .withInt("delete",1);
	// // table.putItem(item);
	// //
	// // } catch (Exception e) {
	// // System.err.println("Failed to create item in " + tableName);
	// // System.err.println(e.getMessage());
	// // }
	// }

	// TODO This has to be manually done as the key in the above table is auto
	// generated and the ADmin AD Group need to be identified and added.
	// private static void loadInitialAccountIdGroup(String tableName) {
	// AccountIdADGroupDAO adgDAO = new AccountIdADGroupDAO();
	// AccountRolePolicyDAO arpDAO = new AccountRolePolicyDAO();
	//
	// List<AccountRolePolicyRecord> arpList =
	// arpDAO.getARPByAccountNo("568389488636");
	// AccountRolePolicyRecord arp = arpList.get(0);
	// adgDAO.saveAccountIdGroupRecord(arp.getId(), "AWSG_LABS1-AppSupport",
	// "SystemLoad");
	// //
	// // try {
	// //
	// // dateFormatter.setTimeZone(TimeZone.getTimeZone("UTC"));
	// //
	// // Table table = dynamoDB.getTable(tableName);
	// //
	// // logger.info("Adding data to " + tableName);
	// //
	// // Item item = new Item()
	// // .withPrimaryKey("accountrolepolicy_id", "")
	// // .withString("adGroup", "AWSG_LABS1-AppSupport")
	// // .withString("user", "SystemLoad")
	// // .withString("date", dateFormatter.format(new Date().getTime()))
	// // .withInt("delete",1);
	// // table.putItem(item);
	// //
	// // } catch (Exception e) {
	// // System.err.println("Failed to create item in " + tableName);
	// // System.err.println(e.getMessage());
	// // }
	//
	// }

}