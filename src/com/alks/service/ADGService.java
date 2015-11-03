package com.alks.service;

import java.util.List;

import com.alks.model.ui.ADG;

/**
 * An interface that structures an AD Group
 * forming/retrieving mechanism
 * 
 * @author Shoban Sriram
 * @author Kamen Tsvetkov
 *
 */
public interface ADGService {

	/**
	 * Returns a list of AD Groups
	 * 
	 * @return AD Groups
	 */
	List<ADG> getAllADGs();
	
	/**
	 * Adds an AD Group
	 * 
	 * @param adg
	 */
	void addADG(ADG adg);
	
	boolean deleteADG(ADG adg);
	
}
