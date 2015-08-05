package com.alks.model.ui;

/**
 * A class that represents a Role Policies UI element
 * 
 * @author Shoban Sriram
 * @author Kamen Tsvetkov
 *
 */
public class ARP extends BaseUI {

	private String accountNo;
	private String policy;
	private String role;
	private String id;
	
	/**
	 * Returns the ID of the Role Policies UI element
	 * 
	 * @return id
	 */
	public String getId() {
		return id;
	}
	
	/**
	 * Sets the ID of the Role Policies UI element
	 * 
	 * @param id
	 */
	public void setId(String id) {
		this.id = id;
	}
	
	/**
	 * Returns the account number of the Role Policies UI element
	 * 
	 * @return accountNo
	 */
	public String getAccountNo() {
		return accountNo;
	}
	
	/**
	 * Sets the account number of the Role Policies UI element
	 * 
	 * @param accountNo
	 */
	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}
	
	/**
	 * Returns the policy of the Role Policies UI element
	 * 
	 * @return policy
	 */
	public String getPolicy() {
		return policy;
	}
	
	/**
	 * Sets the policy of the Role Policies UI element
	 * 
	 * @param policy
	 */
	public void setPolicy(String policy) {
		this.policy = policy;
	}
	
	/**
	 * Returns the role of the Role Policies UI element
	 * 
	 * @return role
	 */
	public String getRole() {
		return role;
	}
	
	/**
	 * Sets the role of the Role Policies UI element
	 * 
	 * @param role
	 */
	public void setRole(String role) {
		this.role = role;
	}
	
	/**
	 * Returns the string representation of an Role Policies UI element
	 */
	public String toString() { 
	    return super.toString() + " AccountNo='" + this.accountNo + "', Role='" + this.role + " Policy='" + this.policy + "', id='" + this.id + "'";
	} 
}
