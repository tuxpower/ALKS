package com.alks.dao;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.alks.model.db.AccountRecord;
import com.alks.service.config.MessageUtils;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ComparisonOperator;
import com.amazonaws.services.dynamodbv2.model.Condition;

/**
 * Database class to retrieve and store account data
 * 
 * @author Shoban Sriram
 * @author Kamen Tsvetkov
 *
 */
public class AccountsDAO extends BaseEncryptDAO {

	private static Logger logger = Logger.getLogger(AccountsDAO.class);

	static String ACCOUNT_NO = "accountNo";
	static String ACCOUNTS_TABLE = "accounts";
	static String ACCESSKEY = "accessKey";
	static String SECRETKEY = "secretKey";
	static String ACTIVE = "active";
	static String ROTATE_DATE = "rotateDate";
	
	/**
	 * Return a list of Account records from the database
	 * 
	 * @return a list of Account records
	 */
	public List<AccountRecord> getAllAccounts(){
	    DynamoDBScanExpression expression = new DynamoDBScanExpression();
	    logger.info("DB getAllAccounts");
		return getMapper().scan(AccountRecord.class, expression);
	}
	
	/**
	 * Return a list of active Account records from the database
	 * 
	 * @return a list of active Account records
	 */
	public List<AccountRecord> getAllActiveAccounts(){
	    DynamoDBScanExpression expression = new DynamoDBScanExpression();
	    expression.addFilterCondition(ACTIVE, 	                
	    		new Condition()
        .withComparisonOperator(ComparisonOperator.EQ)
        .withAttributeValueList(new AttributeValue().withS("1")));
	    logger.info("DB getAllActiveAccounts");
		return getMapper().scan(AccountRecord.class, expression);
	}
	
	/**
	 * Return an Account record that corresponds to the
	 * account number passed into the parameter
	 * 
	 * @param accountNo
	 * @return an Account record
	 */
	public AccountRecord getAccountByAccountNo(String accountNo){
	       DynamoDBScanExpression scanExpression = new DynamoDBScanExpression();
	        scanExpression.addFilterCondition(ACCOUNT_NO, 
	                new Condition()
	                   .withComparisonOperator(ComparisonOperator.EQ)
	                   .withAttributeValueList(new AttributeValue().withS(accountNo)));
	    
	     List<AccountRecord> list = getMapper().scan(AccountRecord.class, scanExpression);
	     logger.debug("DB accountNo:"+accountNo);
	     if(list!=null && list.size()>0){
	    	 return list.get(0);
	     }
		return null;
	}
	
	/**
	 * Save an Account record to the database
	 * Note: This method should be called only when new account is added and when new keys are generated. 
	 * 
	 * @param acctNo
	 * @param acctDesc
	 * @param accessKey
	 * @param secretKey
	 * @param lastUpdatedBy
	 * @return whether or not save succeeded
	 */
	public boolean saveAccount(String acctNo, String acctDesc, String accessKey, String secretKey, String lastUpdatedBy){
		
		AccountRecord actRec = new AccountRecord();
		
		actRec.setAccountNo(acctNo);
		actRec.setAccountDesc(acctDesc);
		actRec.setAccessKey(accessKey);
		actRec.setSecretKey(secretKey);
		actRec.setUser(lastUpdatedBy);
		actRec.setDate(DATE_FORMATTER.format(new Date()));
		actRec.setActive(1);
		actRec.setRotateDate(DATE_FORMATTER.format(new Date()));
		actRec.setRotateBy(lastUpdatedBy);
		saveAccounts(actRec);
		
		return true;
	}
	
	/**
	 * Change status of an Account record in the database to active
	 * 
	 * @param acctNumber
	 * @param emailId
	 * @return whether or not the Account record was activated
	 */
	public boolean activateAccount(String acctNumber, String emailId){
		return updateAccount(acctNumber,1,emailId);
	}
	
	/**
	 * Change status of an Account record in the database to inactive
	 * 
	 * @param acctNumber
	 * @param emailId
	 * @return whether or not the Account record was inactivated
	 */
	public boolean inactivateAccount(String acctNumber, String emailId){
		return updateAccount(acctNumber,0,emailId);
	}
	
	/**
	 * Update an already existing Account record in the database
	 * Note: Record and keys must already exist in the database
	 * 
	 * @param accountNumber
	 * @param flag
	 * @param emailId
	 * @return whether or not the Account record was updated
	 */
	public boolean updateAccount(String accountNumber, int flag, String emailId){
		AccountRecord account = getAccountByAccountNo(accountNumber);
		account.setActive(flag);
		account.setUser(emailId);
		account.setDate(DATE_FORMATTER.format(new Date()));
		saveAccounts(account);
		logger.info("DB Update Account Record:"+account);
		return true;
	}

	/**
	 * Saves an Account into the database given an Account record
	 * 
	 * @param acct
	 * @return whether or not save succeeded
	 */
	public boolean saveAccounts(AccountRecord acct){
		try{
			logger.info("DB Saving Account:"+acct);
			getMapper().save(acct);
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	/**
	 * Retrieves the long term keys of an Account given the
	 * Accounts number
	 * 
	 * @param accountNo
	 * @return a string array of long term keys
	 */
	public String[] getLongTermKeysByAccount(String accountNo){
		/*
		Table table = getDynamoDB().getTable(ACCOUNTS_TABLE);
		QuerySpec querySpec = new QuerySpec()
		.withHashKey(ACCOUNT_NO, accountNo);

		ItemCollection<QueryOutcome> items = table.query(querySpec);
		Iterator<Item> iterator = items.iterator();
		Item item = (Item) iterator.next();
		*/
		AccountRecord accRec = getAccountByAccountNo(accountNo);
		String [] keys = new String[2];
		
		keys[0] = accRec.getAccessKey();
		keys[1] = accRec.getSecretKey();
		
		logger.info("DB accountNo:"+accountNo);
		
		return keys;
	}

}