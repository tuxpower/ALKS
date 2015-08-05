/**
 * An object representing a long term key
 * 
 * @author Shoban Sriram
 * @author Kamen Tsvetkov
 *
 */

package com.alks.model.aws;

public class LongTermKey {

	private String accessKey;
	private String secretKey;
	
	/**
	 * Return the long term access key
	 * 
	 * @return accessKey
	 */
	public String getAccessKey() {
		return accessKey;
	}
	
	/**
	 * Sets the long term access key
	 * 
	 * @param accessKey
	 */
	public void setAccessKey(String accessKey) {
		this.accessKey = accessKey;
	}
	
	/**
	 * Return the long term secret key
	 * 
	 * @return secretKey
	 */
	public String getSecretKey() {
		return secretKey;
	}
	
	/**
	 * Sets the long term secret key
	 * 
	 * @param secretKey
	 */
	public void setSecretKey(String secretKey) {
		this.secretKey = secretKey;
	}
	
	
}
