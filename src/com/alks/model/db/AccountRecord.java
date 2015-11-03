package com.alks.model.db;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.amazonaws.services.dynamodbv2.datamodeling.encryption.DoNotTouch;

/**
 * A class representing an Account record
 * 
 * @author Shoban Sriram
 * @author Kamen Tsvetkov
 *
 */
@DynamoDBTable(tableName = "com.alks.table.accounts")
public class AccountRecord extends BaseModel {
		
	private String accountNo;
	private String accountDesc;
	private String secretKey;
	private String accessKey;
	private String rotateDate;
	private String rotateBy;
	 
	/**
	 * Returns an Account Record's account number
	 * Note: Not encrypted because it is a hashkey
	 * 
	 * @return accountNo
	 */
	@DynamoDBHashKey (attributeName = "accountNo")
	public String getAccountNo() {
		return accountNo;
	}
	
	/**
	 * Sets an Account Record's account number
	 * 
	 * @param accountNo
	 */
	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}
	
	/**
	 * Returns an Account Record's account description
	 * 
	 * @return accountDesc
	 */
	@DoNotTouch	
	@DynamoDBAttribute (attributeName = "accountName")
	public String getAccountDesc() {
		return accountDesc;
	}
	
	/**
	 * Sets an Account Record's account description
	 * 
	 * @param accountDesc
	 */
	public void setAccountDesc(String accountDesc) {
		this.accountDesc = accountDesc;
	}
	
	/**
	 * Returns an Account Record's secret key
	 * Note: Encrypted by default
	 * 
	 * @return secretKey
	 */
	@DynamoDBAttribute (attributeName = "secretKey")
	public String getSecretKey() {
		return secretKey;
	}
	
	/**
	 * Sets an Account Record's secret key
	 * 
	 * @param secretKey
	 */
	public void setSecretKey(String secretKey) {
		this.secretKey = secretKey;
	}
	
	/**
	 * Returns an Account Record's access key
	 * 
	 * @return accessKey
	 */
	@DoNotTouch
	@DynamoDBAttribute (attributeName = "accessKey")
	public String getAccessKey() {
		return accessKey;
	}
	
	/**
	 * Sets an Account Record's access key
	 * 
	 * @param accessKey
	 */
	public void setAccessKey(String accessKey) {
		this.accessKey = accessKey;
	}
	
	/**
	 * Returns an Account Record's last rotated date
	 * 
	 * @return rotateDate
	 */
	@DoNotTouch	
	@DynamoDBAttribute (attributeName = "rotateDate")
	public String getRotateDate() {
		return rotateDate;
	}
	
	/**
	 * Sets an Account Record's last rotated date
	 * 
	 * @param rotateDate
	 */
	public void setRotateDate(String rotateDate) {
		this.rotateDate = rotateDate;
	}

	/**
	 * Returns the user ID of the user who last rotated 
	 * the Account Record
	 * 
	 * @return rotateBy
	 */
	@DoNotTouch	
	@DynamoDBAttribute (attributeName = "rotateBy")
	public String getRotateBy() {
		return rotateBy;
	}
	
	/**
	 * Sets the user ID of the user who last rotated 
	 * the Account Record
	 * 
	 * @param rotateBy
	 */
	public void setRotateBy(String rotateBy) {
		this.rotateBy = rotateBy;
	}
	
	/**
	 * Returns the string representation of an Account record
	 * Does not add secret key in order to keep it secure
	 * and not log or use anywhere.
	 */
	public String toString() { 
	    return  super.toString() + "AccountNo='" + this.accountNo + "', Description='" + this.accountDesc+ "', AccessKey='" + this.accessKey + "', RotateDate='" + this.rotateDate + "', RotateBy='" + this.rotateBy + "'";
	} 

}
