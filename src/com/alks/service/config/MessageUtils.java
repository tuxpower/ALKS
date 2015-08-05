package com.alks.service.config;

import java.util.Locale;

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

}