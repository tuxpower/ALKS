package com.alks.model.ui;

/**
 * A class that represents an Account UI element
 * 
 * @author Shoban Sriram
 * @author Kamen Tsvetkov
 *
 */
public class Account extends BaseUI {
	
	private String role;
	private String sessionToken;
	private String flag;
	private String rotateBy;
	private String rotateDate;
	private String accountNo;
	private String accountDesc;
	private String secretKey;
	private String accessKey;

	/**
	 * Returns the role of the Account UI element
	 * 
	 * @return role
	 */
	public String getRole() {
		return role;
	}

	/**
	 * Sets the role of the Account UI element
	 * 
	 * @param role
	 */
	public void setRole(String role) {
		this.role = role;
	}

	/**
	 * Returns the session token of the Account UI element
	 * 
	 * @return sessionToken
	 */
	public String getSessionToken() {
		return sessionToken;
	}

	/**
	 * Sets the session token of the Account UI element
	 * 
	 * @param sessionToken
	 */
	public void setSessionToken(String sessionToken) {
		this.sessionToken = sessionToken;
	}

	/**
	 * Returns the flag of the Account UI element
	 * 
	 * @return flag
	 */
	public String getFlag() {
		return flag;
	}

	/**
	 * Sets the flag of the Account UI element
	 * 
	 * @param flag
	 */
	public void setFlag(String flag) {
		this.flag = flag;
	}

	/**
	 * Returns the ID of the user who last rotated the 
	 * Account keys
	 * 
	 * @return rotateBy
	 */
	public String getRotateBy() {
		return rotateBy;
	}

	/**
	 * Sets the ID of the user who last rotated the 
	 * Account keys
	 * 
	 * @param rotateBy
	 */
	public void setRotateBy(String rotateBy) {
		this.rotateBy = rotateBy;
	}

	/**
	 * Returns the date the Account keys were
	 * last rotated
	 * 
	 * @return rotateDate
	 */
	public String getRotateDate() {
		return rotateDate;
	}

	/**
	 * Sets the date the Account keys were
	 * last rotated
	 * 
	 * @param rotateDate
	 */
	public void setRotateDate(String rotateDate) {
		this.rotateDate = rotateDate;
	}

	/**
	 * Returns the account number of the Account UI element
	 * 
	 * @return accountNo
	 */
	public String getAccountNo() {
		return accountNo;
	}

	/**
	 * Sets the account number of the Account UI element
	 * 
	 * @param accountNo
	 */
	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}

	/**
	 * Returns the account description of the Account UI element
	 * 
	 * @return accountDesc
	 */
	public String getAccountDesc() {
		return accountDesc;
	}

	/**
	 * Sets the account description of the Account UI element
	 * 
	 * @param accountDesc
	 */
	public void setAccountDesc(String accountDesc) {
		this.accountDesc = accountDesc;
	}

	/**
	 * Returns the secret key of the Account UI element
	 * 
	 * @return secretKey
	 */
	public String getSecretKey() {
		return secretKey;
	}

	/**
	 * Sets the secret key of the Account UI element
	 * 
	 * @param secretKey
	 */
	public void setSecretKey(String secretKey) {
		this.secretKey = secretKey;
	}

	/**
	 * Returns the access key of the Account UI element
	 * 
	 * @return accessKey
	 */
	public String getAccessKey() {
		return accessKey;
	}

	/**
	 * Sets the access key of the Account UI element
	 * 
	 * @param accessKey
	 */
	public void setAccessKey(String accessKey) {
		this.accessKey = accessKey;
	}

	/**
	 * Returns the string representation of an Account UI element
	 * 
	 */
	public String toString() {
		return super.toString() + " AccountNo='" + this.accountNo
				+ "', Description='" + this.accountDesc + "'";
	}
}
