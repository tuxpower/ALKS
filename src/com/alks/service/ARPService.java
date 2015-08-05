package com.alks.service;

import java.util.List;

import com.alks.model.ui.ARP;

/**
 * An interface that structures a Role Policies 
 * forming/retrieving mechanism
 * 
 * @author Shoban Sriram
 * @author Kamen Tsvetkov
 *
 */
public interface ARPService {

	/**
	 * Returns a list of Role Policies
	 * 
	 * @return Role Policies
	 */
	List<ARP> getAllARPs();
	
	/**
	 * Adds a Role Policy
	 * 
	 * @param arp
	 */
	void addARP(ARP arp);
}
