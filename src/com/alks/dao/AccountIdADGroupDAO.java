package com.alks.dao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.alks.model.db.AccountIdADGroupRecord;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ComparisonOperator;
import com.amazonaws.services.dynamodbv2.model.Condition;

/**
 * Database class to retrieve and store account ID and AD group data
 * 
 * @author Shoban Sriram
 * @author Kamen Tsvetkov
 *
 */
public class AccountIdADGroupDAO extends BaseDAO {

	private static Logger logger = Logger.getLogger(AccountIdADGroupDAO.class);

	static String ACCOUNTROLEPOLICY_ID = "accountrolepolicy_id";
	static String ADGROUP = "adGroup";
	
	/**
	 * Return a list of AD Group records from the database
	 * 
	 * @return a list of AD Group records
	 */
	public List<AccountIdADGroupRecord> getAllAccountIDADGroup() {
		DynamoDBScanExpression expression = new DynamoDBScanExpression();
		logger.info("DB Listing all ADGs: getAllAccountIDADGroup");
		return getMapper().scan(AccountIdADGroupRecord.class, expression);
	}

	/**
	 * Returns an AD Group record from the database based on an account ID
	 * This method can return only one record as accountId is unique.
	 * 
	 * @param accountId
	 * @return an AD Group record
	 */
	public AccountIdADGroupRecord getAccountIdGroup(String accountId) {
		DynamoDBScanExpression scanExpression = new DynamoDBScanExpression();
		scanExpression.addFilterCondition(ACCOUNTROLEPOLICY_ID , new Condition()
				.withComparisonOperator(ComparisonOperator.EQ)
				.withAttributeValueList(new AttributeValue().withS(accountId)));

		List<AccountIdADGroupRecord> list = getMapper().scan(AccountIdADGroupRecord.class, scanExpression);
		logger.debug("DB Getting ADG by record id: getAccountIdGroup"+ accountId);
		if(list!=null && list.size()>0){
			return (AccountIdADGroupRecord)list.get(0);
		}else{
			return null;
		}
	}

	/**
	 * Returns a list of AD Group records from the database based 
	 * on a list of account IDs
	 *
	 * @param accountIds
	 * @return a list of AD Group records
	 */
	public List<AccountIdADGroupRecord> getGroupsByAccountIds(List<String> accountIds) {
		DynamoDBScanExpression scanExpression = new DynamoDBScanExpression();
		Collection<AttributeValue> c = new ArrayList<AttributeValue>();
		
		for(int i =0;i<accountIds.size();i++){
			c.add(new AttributeValue().withS(accountIds.get(i)));
		}
		
		scanExpression.addFilterCondition(ACCOUNTROLEPOLICY_ID, new Condition()
				.withComparisonOperator(ComparisonOperator.IN)
				.withAttributeValueList(c));
		logger.debug("DB getGroupsByAccountIds ADG:"+ accountIds);
		List<AccountIdADGroupRecord> list = getMapper().scan(AccountIdADGroupRecord.class, scanExpression);
			return list;
			
	}

	/**
	 * Return a list of AD Group records from the database based 
	 * on a list of AD Groups
	 * 
	 * @param adGroups
	 * @return a list of AD Group records
	 */
	public List<AccountIdADGroupRecord> getAccountIdsByGroup(List<String> adGroups) {
		DynamoDBScanExpression scanExpression = new DynamoDBScanExpression();
		Collection<AttributeValue> c = new ArrayList<AttributeValue>();
		
		for(int i =0;i<adGroups.size();i++){
			c.add(new AttributeValue().withS(adGroups.get(i)));
		}
		
		scanExpression.addFilterCondition(ADGROUP, new Condition()
				.withComparisonOperator(ComparisonOperator.IN)
				.withAttributeValueList(c));
		List<AccountIdADGroupRecord> list = getMapper().scan(AccountIdADGroupRecord.class, scanExpression);
			return list;
			
	}
	
	/**
	 * Save an individual AD Group record to the database
	 * 
	 * @param accountId
	 * @param adGroup
	 * @param emailId
	 * @return whether or not save succeeded
	 */
	public boolean saveAccountIdGroupRecord(String accountId, String adGroup, String emailId) {

		AccountIdADGroupRecord acctIdGroup = new AccountIdADGroupRecord();

		acctIdGroup.setAccountId(accountId);
		acctIdGroup.setAdGroup(adGroup);
		acctIdGroup.setUser(emailId);
		acctIdGroup.setDate(DATE_FORMATTER.format(new Date()));
		saveAccountIdGroup(acctIdGroup);
		return true;
	}

	/**
	 * Save an individual AD Group to the database
	 * 
	 * @param acctIdGroupRecord
	 * @return whether or not save succeeded
	 */
	private boolean saveAccountIdGroup(AccountIdADGroupRecord acctIdGroupRecord) {
		acctIdGroupRecord.setDate(DATE_FORMATTER.format(new Date()));
		logger.info("DB Saving ADG:"+ acctIdGroupRecord);
		getMapper().save(acctIdGroupRecord);
		return true;
	}
}
