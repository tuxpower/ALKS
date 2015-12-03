package com.alks.model.ui;

/**
 * A class that represents an AD Group UI element
 * 
 * @author Shoban Sriram
 * @author Kamen Tsvetkov
 *
 */
public class ADG extends BaseUI{

	private String accountId;
	private String adGroup;
	private String accountNo;
	private String role;
	
	/**
	 * Returns the role of the AD Group UI element
	 * 
	 * @return role
	 */
	public String getRole() {
		return role;
	}
	
	/**
	 * Sets the role of the AD Group UI element
	 * 
	 * @param role
	 */
	public void setRole(String role) {
		this.role = role;
	}
	
	/**
	 * Returns the account number of the AD Group UI element
	 * 
	 * @return accountNo
	 */
	public String getAccountNo() {
		return accountNo;
	}
	
	/**
	 * Sets the account number of the AD Group UI element
	 * 
	 * @param accountNo
	 */
	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}
	
	/**
	 * Returns the account ID of the AD Group UI element
	 * 
	 * @return accountId
	 */
	public String getAccountId() {
		return accountId;
	}
	
	/**
	 * Sets the account ID of the AD Group UI element
	 * 
	 * @param accountId
	 */
	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}
	
	/**
	 * Returns the AD Group of the AD Group UI element
	 * 
	 * @return adGroup
	 */
	public String getAdGroup() {
		return adGroup;
	}
	
	/**
	 * Sets the AD Group of the AD Group UI element
	 * 
	 * @param adGroup
	 */
	public void setAdGroup(String adGroup) {
		this.adGroup = adGroup;
	}

	/**
	 * Returns the string representation of an AD Group UI element
	 */
	public String toString() { 
	    return super.toString() + " AccountNo='" + this.accountNo + "', Role='" + this.role + " accountrolepolicy_id='" + this.accountId + "', ADGroup='" + this.adGroup + "'";
	} 
}
