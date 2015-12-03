package com.alks.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.alks.model.ui.Account;

/**
 * An interface that structures an Account
 * forming/retrieving mechanism
 * 
 * @author Shoban Sriram
 * @author Kamen Tsvetkov
 *
 */
@Service
public interface AccountService {

	/**
	 * Returns a list of Accounts
	 * 
	 * @return Accounts
	 */
	List<Account> getAllAccounts();
	
	/**
	 * Returns a list of active Accounts
	 * 
	 * @return Active Accounts
	 */
	List<Account> getAllActiveAccounts();
	
	/**
	 * Adds an Account
	 * 
	 * @param account
	 * @param emailId
	 * @return whether or not Account was added
	 */
	boolean addAccount(Account account, String emailId);
	
	/**
	 * Activates an Account
	 * 
	 * @param account
	 * @param emailId
	 * @return whether or not Account was activated
	 */
	boolean activateAccount(String account, String emailId);
	
	/**
	 * Inactivates an Account
	 * 
	 * @param account
	 * @param emailId
	 * @return whether or not Account was inactivated
	 */
	boolean inactivateAccount(String account, String emailId);

	/**
	 * Rotates the Account's keys
	 * 
	 * @param account
	 * @param emailId
	 * @return whether or not Account keys were regenerated/rotated
	 */
	boolean regenerateAccountKey(String account, String emailId);
	
	/**
	 * Returns an Account from an account number
	 * 
	 * @param accountNo
	 * @return Account
	 */
	Account getAccountByAccountNo(String accountNo);
	
}
