package com.alks.dao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ComparisonOperator;
import com.amazonaws.services.dynamodbv2.model.Condition;
import com.alks.model.db.AccountRolePolicyRecord;

/**
 * Database class to retrieve and store Role Policy relationship data
 * 
 * @author Shoban Sriram
 * @author Kamen Tsvetkov
 *
 */
public class AccountRolePolicyDAO extends BaseDAO {

	private static Logger logger = Logger.getLogger(AccountRolePolicyDAO.class);

	static String ACCOUNT_NO = "accounts_accountNo";
	static String ROLE = "role";
	static String ID = "id";
	
	/**
	 * Return a list of Role Policy records from the database
	 * 
	 * @return a list of Role Policy records
	 */
	public List<AccountRolePolicyRecord> getAllAccountRolePolicy() {
		DynamoDBScanExpression expression = new DynamoDBScanExpression();
		logger.info("DB getAllAccountRolePolicy records");
		return getMapper().scan(AccountRolePolicyRecord.class, expression);
	}

	/**
	 * Returns a list of Role Policy records from the database
	 * based on an account number
	 * 
	 * @param accountNo
	 * @return a list of Role Policy records
	 */
	public List<AccountRolePolicyRecord> getARPByAccountNo(String accountNo) {	
        DynamoDBScanExpression scanExpression = new DynamoDBScanExpression();
        scanExpression.addFilterCondition( ACCOUNT_NO, 
                new Condition()
                   .withComparisonOperator(ComparisonOperator.EQ)
                   .withAttributeValueList(new AttributeValue().withS(accountNo)));
		
		logger.info("DB For accountNo"+accountNo);
		return getMapper().scan(AccountRolePolicyRecord.class, scanExpression);
	}

	/**
	 * Returns a list of Role Policy records from the database
	 * based on an account number and AWS role
	 * 
	 * @param accountNo
	 * @param awsRole
	 * @return a list of Role Policy records
	 */
	public List<AccountRolePolicyRecord> getARPByAccountNoAndAWSRole(String accountNo, String awsRole) {
		
        DynamoDBScanExpression scanExpression = new DynamoDBScanExpression();
        scanExpression.addFilterCondition(ACCOUNT_NO, 
                new Condition()
                   .withComparisonOperator(ComparisonOperator.EQ)
                   .withAttributeValueList(new AttributeValue().withS(accountNo)));
        scanExpression.addFilterCondition(ROLE, 
                new Condition()
                   .withComparisonOperator(ComparisonOperator.EQ)
                   .withAttributeValueList(new AttributeValue().withS(awsRole)));
				
		logger.info("DB AccountNo:"+accountNo + " awsRole:"+awsRole);
		
		return getMapper().scan(AccountRolePolicyRecord.class, scanExpression);//(AccountRolePolicyRecord.class, query);
	}

	/**
	 * Returns a list of Role Policy records from the database
	 * based on a list of account IDs
	 * 
	 * @param accountIds
	 * @return a list of Role Policy records
	 */
	public List<AccountRolePolicyRecord> getARPsByAccountIds(List<String> accountIds) {
		DynamoDBScanExpression scanExpression = new DynamoDBScanExpression();
		Collection<AttributeValue> c = new ArrayList<AttributeValue>();
		
		if(accountIds!=null && accountIds.size()>0){
		for(int i =0;i<accountIds.size();i++){
			c.add(new AttributeValue().withS(accountIds.get(i)));
		}
		}else{
			return new ArrayList<AccountRolePolicyRecord>();
		}
		
		scanExpression.addFilterCondition(ID, new Condition()
				.withComparisonOperator(ComparisonOperator.IN)
				.withAttributeValueList(c));

		List<AccountRolePolicyRecord> list = getMapper().scan(AccountRolePolicyRecord.class, scanExpression);
		logger.debug(list);
		return list;

	}

	/**
	 * Save an individual Role Policy record to the database
	 * 
	 * @param acctNo
	 * @param role
	 * @param policy
	 * @param emailId
	 * @return whether or not save succeeded
	 */
	public boolean saveARP(String acctNo, String role, String policy,String emailId) {

		AccountRolePolicyRecord actRolePlcyRec = new AccountRolePolicyRecord();

		actRolePlcyRec.setAccountNo(acctNo);
		actRolePlcyRec.setRole(role);
		actRolePlcyRec.setPolicy(policy);
		actRolePlcyRec.setDate(DATE_FORMATTER.format(new Date()));
		actRolePlcyRec.setUser(emailId);
		actRolePlcyRec.setActive(1);
		saveARP(actRolePlcyRec);

		return true;
	}

	/**
	 * Save an individual Role Policy to the database
	 * 
	 * @param acctRecord
	 * @return whether or not save succeeded
	 */
	public boolean saveARP(AccountRolePolicyRecord acctRecord) {
		acctRecord.setDate(DATE_FORMATTER.format(new Date()));
		logger.info("DB Saving ARP:"+acctRecord);
		getMapper().save(acctRecord);
		return true;
	}

	/*
	 public String[] getLongTermKeysByAccount(String accountId){
	
	 Table table = getDynamoDB().getTable("accounts");
	 QuerySpec querySpec = new QuerySpec()
	 .withHashKey("accountNo", accountId);
	
	 ItemCollection<QueryOutcome> items = table.query(querySpec);
	 Iterator<Item> iterator = items.iterator();
	 Item item = (Item) iterator.next();
	 String [] keys = new String[2];
	
	 keys[0] = item.getString("accessKey");
	 keys[1] = item.getString("secretKey");
	
	 return keys;
	 }
	 */
}
