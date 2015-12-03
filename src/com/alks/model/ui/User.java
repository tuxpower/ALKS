package com.alks.model.ui;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * User class that defines variables for the person
 * interacting with the applications 
 * 
 * @author Shoban Sriram
 * @author Kamen Tsvetkov
 *
 */
@Component
@Scope("session")
public class User {

	private String emailId=null;
	private String password;
	private boolean admin = false;
	private boolean authenticated;

	/**
	 * Gets users email ID
	 * 
	 * @return emailId
	 */	
	public String getEmailId() {
		return emailId;
	}
	
	/**
	 * Sets users email ID
	 * 
	 * @param emailId
	 */
	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	/**
	 * Gets users password
	 * 
	 * @return password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * Sets users password
	 * 
	 * @param password
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * Returns whether user is an administrator
	 * 
	 * @return isAdmin
	 */
	public boolean isAdmin() {
		return admin;
	}
	
	/**
	 * Sets whether user is an administrator
	 * 
	 * @param isAdmin
	 */
	public void setAdmin(boolean isAdmin) {
		this.admin = isAdmin;
	}
	

	/**
	 * Gets whether user has been authenticated at login
	 * 
	 * @return isAuthenticated
	 */
	public boolean isAuthenticated() {
		return authenticated;
	}
	
	/**
	 * Sets whether user is authenticated at login
	 * 
	 * @param isAuthenticated
	 */
	public void setAuthenticated(boolean isAuthenticated) {
		this.authenticated = isAuthenticated;
	}
	

	/**
	 * 
	 * Do not include password as this method is used in logging. 
	 */
	public String toString(){
	 return	"emailId=" + this.emailId + ","
                + "isAdmin=" + this.admin + "," 
                + "isAuthenticated="+ this.authenticated;
	}
}
