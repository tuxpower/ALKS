package com.alks.service.config;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import com.alks.dao.BaseDAO;
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
public class ALKSDynamoDBSchema extends BaseDAO{

	static String accountsTable = "com.alks.table.accounts";
	static String accountrolepolicyTable = "com.alks.table.accountrolepolicy";
	static String accountidgroupTable = "com.alks.table.accountidgroup";

	
	private static Logger logger = Logger.getLogger(ALKSDynamoDBSchema.class);
	static SimpleDateFormat dateFormatter = new SimpleDateFormat(
			"yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
	static long accountReads = 1L;
	static long accountWrites = 1L;
	static String stringType = "S";
	static String numberType = "N";

	/**
	 * Initializes a connection to dynamoDB
	 * 
	 */
	public  void initDyDB() {

		try {
			dynamoDB = new DynamoDB(getClient());
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
	private void deleteTable(String tableName) {

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
	private  void createTable(String tableName, long readCapacityUnits,
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
	private  void createTable(String tableName, long readCapacityUnits,
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

	public static void main(String args[]){

		ALKSDynamoDBSchema alksSchema = new ALKSDynamoDBSchema();
		alksSchema.deleteTable(accountsTable);
		alksSchema.deleteTable(accountrolepolicyTable);
		alksSchema.deleteTable(accountidgroupTable);
		alksSchema.createTable(accountsTable, 1, 1, "accountNo", "S");
		alksSchema.createTable(accountrolepolicyTable, 1, 1, "id", "S");
		alksSchema.createTable(accountidgroupTable, 1, 1, "accountrolepolicy_id", "S","adGroup","S");
	}

}