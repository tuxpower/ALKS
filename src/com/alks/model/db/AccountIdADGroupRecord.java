package com.alks.model.db;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

/**
 * A class representing an AD Group record
 * 
 * @author Shoban Sriram
 * @author Kamen Tsvetkov
 *
 */
@DynamoDBTable(tableName = "com.alks.table.accountidgroup")
public class AccountIdADGroupRecord extends BaseModel {

	private String accountId;
	private String adGroup;
	 
	/**
	 * Returns the account ID of an AD Group record
	 * 
	 * @return accountId
	 */
	 @DynamoDBHashKey(attributeName = "accountrolepolicy_id")
	public String getAccountId() {
		return accountId;
	}
	 
	/**
	 * Sets the account ID of an AD Group record
	 * 
	 * @param accountId
	 */
	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}
	
	/**
	 * Returns the AD Group of an AD Group record
	 * 
	 * @return adGroup
	 */
	 @DynamoDBRangeKey(attributeName = "adGroup")
	 public String getAdGroup() {
		return adGroup;
	}
	 
	/**
	 * Sets the AD Group of an AD Group record
	 * 
	 * @param adGroup
	 */
	public void setAdGroup(String adGroup) {
		this.adGroup = adGroup;
	}
	
	/**
	 * Returns the string representation of an AD Group record
	 * 
	 */
	public String toString() { 
	    return super.toString() + " AccountRolePolicy_id='" + this.accountId + "', ADGroup='" + this.adGroup + "'";
	} 
	
	
}
