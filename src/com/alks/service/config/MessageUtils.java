package com.alks.service.config;

import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

import javax.servlet.http.HttpServletRequest;

import org.springframework.context.annotation.Scope;
import org.springframework.context.support.ResourceBundleMessageSource;

/**
 * A class that manages the error messages
 * 
 * @author Shoban Sriram
 * @author Kamen Tsvetkov
 *
 */
@Scope("singleton")
public class MessageUtils {

	public static String ERROR = "error";
	public static String WARN = "warn";
	public static String INFO = "info";
	public static String DEBUG = "debug";
	private static SimpleDateFormat dateFormatter 
			= new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
	static{
		dateFormatter.setTimeZone(TimeZone.getTimeZone("EST"));
	}
	
	/**
	 * Retrieves the current error message 
	 * 
	 * @param key
	 * @return error message
	 */
	public static String getMessage(String key) {

		try {
			ResourceBundleMessageSource rbms = new ResourceBundleMessageSource();
			rbms.setBasename("messages");
			return rbms.getMessage(key, null, Locale.getDefault());
		} catch (Exception e) {
			return "Unresolved key: " + key;
		}
	}

	//TODO Needs to be tested on production environment with load balancers.
	/**
	 * This method returns IP address of the client.
	 *  
	 * @param request
	 * @return IP address
	 */
	public static String getIpAddr(HttpServletRequest request) {
		String ip = request.getHeader("X-Real-IP");
		if (null != ip && !"".equals(ip.trim())
				&& !"unknown".equalsIgnoreCase(ip)) {
			return ip;
		}
		ip = request.getHeader("X-Forwarded-For");
		if (null != ip && !"".equals(ip.trim())
				&& !"unknown".equalsIgnoreCase(ip)) {
			// get first ip from proxy ip
			int index = ip.indexOf(',');
			if (index != -1) {
				return ip.substring(0, index);
			} else {
				return ip;
			}
		}
		return request.getRemoteAddr();
	}
	
	
	/**
	 * This method is an utility method to get account number 
	 * from accountNumber+account description string which is used for display purposes
	 * @param accountNumber
	 * @return
	 */
	public static String getAccountString(String accountNumber){
		return accountNumber.substring(0, accountNumber.indexOf('-')-1);
	}
	/**
	 * This method returns a cancatenated string of account number and account description 
	 * for display purposes
	 * @param accountNumber
	 * @param accountDesc
	 * @return
	 */
	public static String getAccountDisplayString(String accountNumber, String accountDesc){
		return accountNumber + " - " + accountDesc;
	}
	

	// public static String getConfigValue(String key) {
	//
	// try {
	// PropertyPlaceholderConfigurer ppc = new PropertyPlaceholderConfigurer();
	// ppc.setBeanName("locations");
	// return rbms.getMessage(key, null, Locale.getDefault());
	// }
	// catch (Exception e) {
	// return "Unresolved key: " + key;
	// }
	// }

}