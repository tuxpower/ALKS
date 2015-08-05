package com.alks.model.db;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument;
import com.amazonaws.services.dynamodbv2.datamodeling.encryption.DoNotTouch;

/**
 * The base class that any record class would extend
 * Implements basic characteristics of a record
 * 
 * @author Shoban Sriram
 * @author Kamen Tsvetkov
 *
 */
@DynamoDBDocument
public class BaseModel {

	int active;
	String date;
	String user;

	/**
	 * Returns whether a record is active or not 
	 * 
	 * @return active or not active (0 or 1) 
	 */
	@DoNotTouch
	@DynamoDBAttribute (attributeName = "active")
	public int getActive() {
		return active;
	}
	
	/**
	 * Sets whether a record is active or not 
	 * 
	 * @param active or not actove (0 or 1)
	 */
	public void setActive(int active) {
		this.active = active;
	}

	/**
	 * Returns the last time a record
	 * was modified
	 * 
	 * @return date
	 */
	@DoNotTouch
	@DynamoDBAttribute (attributeName = "date")
	public String getDate() {
		return date;
	}
	
	/**
	 * Sets the last time a record
	 * was modified
	 * 
	 * @param date
	 */
	public void setDate(String date) {
		this.date = date;
	}
	
	/**
	 * Returns the last person that modified
	 * the record
	 * 
	 * @return user
	 */
	@DoNotTouch
	@DynamoDBAttribute (attributeName = "user")
	public String getUser() {
		return user;
	}
	
	/**
	 * Sets the last person that modified
	 * the record
	 * 
	 * @param user
	 */
	public void setUser(String lastUpdatedBy) {
		this.user = lastUpdatedBy;
	}
	
	/**
	 * Returns the string representation of a record
	 * 
	 */
	public String toString() { 
	    return "User='" + this.user + "', Date='" + this.date + "', Active='" + this.active + "'";
	} 
	
}

