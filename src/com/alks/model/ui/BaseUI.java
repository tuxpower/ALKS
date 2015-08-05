package com.alks.model.ui;

import org.springframework.context.annotation.ComponentScan;

/**
 * A class that is extended by all other UI classes
 * that sets the basic functions of a UI model
 * 
 * @author Shoban Sriram
 * @author Kamen Tsvetkov
 *
 */
@ComponentScan
public class BaseUI {

	private int active;
	private String lastUpdateTime;
	private String lastUpdatedBy;
		
	/**
	 * Returns whether or not the element is active
	 * 
	 * @return active
	 */ 
	public int getActive() {
		return active;
	}
	
	/**
	 * Sets whether or not the element is active
	 * 
	 * @param active
	 */ 
	public void setActive(int active) {
		this.active = active;
	}
	
	/**
	 * Returns the last time the element was updated
	 * 
	 * @return lastUpdateTime
	 */ 
	public String getLastUpdateTime() {
		return lastUpdateTime;
	}
	
	/**
	 * Sets the last time the element was updated
	 * 
	 * @param lastUpdateTime
	 */ 
	public void setLastUpdateTime(String lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}
	
	/**
	 * Returns the ID of the last person 
	 * who updated the element
	 * 
	 * @return lastUpdateBy
	 */
	public String getLastUpdatedBy() {
		return lastUpdatedBy;
	}
	
	/**
	 * Sets the ID of the last person 
	 * who updated the element
	 * 
	 * @param lastUpdateBy
	 */
	public void setLastUpdatedBy(String lastUpdatedBy) {
		this.lastUpdatedBy = lastUpdatedBy;
	}
	
	/**
	 * Returns the base string representation of a UI element
	 * 
	 */
	public String toString() { 
	    return "User='" + this.lastUpdatedBy + "', Date='" + this.lastUpdateTime + "', Active='" + this.active + "'";
	} 
}
