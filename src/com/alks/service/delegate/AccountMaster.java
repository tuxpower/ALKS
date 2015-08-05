package com.alks.service.delegate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.alks.dao.AccountIdADGroupDAO;
import com.alks.dao.AccountRolePolicyDAO;
import com.alks.dao.AccountsDAO;
import com.alks.model.db.AccountIdADGroupRecord;
import com.alks.model.db.AccountRecord;
import com.alks.model.db.AccountRolePolicyRecord;
import com.alks.model.ui.User;
import com.alks.service.impl.ADGroupsServiceImpl;

/**
 * A class that is used to get Account data
 * 
 * @author Shoban Sriram
 * @author Kamen Tsvetkov
 *
 */
public class AccountMaster {

	private static Logger logger = Logger.getLogger(AccountMaster.class);

	/**
	 * Get a list of groups a user belongs to
	 * 
	 * @param user
	 * @return list of groups
	 */
	public static List<String> getGroups(User user){
	
		List<String> groups = ADGroupsServiceImpl.authenticate(user.getEmailId(), user.getPassword());
		
		return groups;
	}

	/**
	 * Get a map of accounts given a list of groups
	 * 
	 * @param groups
	 * @return map of accounts
	 */
	public static Map<String,List<String>> getAccounts(List<String> groups){
		
		//Get all the AccountIds for this user
		AccountIdADGroupDAO dao = new AccountIdADGroupDAO();
		List<AccountIdADGroupRecord> accountIdRecs = dao.getAccountIdsByGroup(groups);
		
		List<String> accountIds = new ArrayList<String>();
		
		for(int i =0;i<accountIdRecs.size();i++){
			AccountIdADGroupRecord acctIdRec = (AccountIdADGroupRecord) accountIdRecs.get(i);
			accountIds.add(acctIdRec.getAccountId());
		}
		
		//Get all ARP's using accountId list
		AccountRolePolicyDAO arpDao = new AccountRolePolicyDAO();
		List<AccountRolePolicyRecord> arps = arpDao.getARPsByAccountIds(accountIds);

		//As we need account description when displaying acccounts we need to get it from Accounts table
		AccountsDAO acctDao = new AccountsDAO();

		//Create a Map that has AccountNo - Roles
		
		Map<String,List<String>> accountRoles = new HashMap<String,List<String>>();
		
		if(arps!=null && arps.size()>0){
		  for(int i = 0;i<arps.size();i++){
			  //Get the role
			  AccountRolePolicyRecord arp = (AccountRolePolicyRecord) arps.get(i);
			  AccountRecord acctRec = acctDao.getAccountByAccountNo(arp.getAccountNo());
			  if(acctRec.getActive()==1){
				  String AccountNoDesc = arp.getAccountNo() + " - " + acctRec.getAccountDesc();
			  
					if(accountRoles.containsKey(AccountNoDesc)){
						List<String> tacctRoles = accountRoles.get(AccountNoDesc);
						tacctRoles.add(arp.getRole());
						//Should I add the list back?
					}else{
						List<String> tacctRoles = new ArrayList<String>();
						tacctRoles.add(arp.getRole());
						accountRoles.put(AccountNoDesc, tacctRoles);
					}
			  }
		  }
		}
		return accountRoles;
		
	}
	
	/**
	 * Get a map of accounts that the user has access to
	 * 
	 * @param user
	 * @return map of accounts
	 */
	public static Map<String,List<String>> getAccounts(User user){
		
//		//Get all the AD groups user belongs to
		List<String>  groups = getGroups(user);
		if(groups==null || groups.size()==0){
			return null;
		}
		logger.info("AD Groups"+ groups);
		return getAccounts(groups);
	}
	
	
}
