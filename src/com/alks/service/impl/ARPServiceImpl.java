package com.alks.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.alks.dao.AccountRolePolicyDAO;
import com.alks.model.db.AccountRolePolicyRecord;
import com.alks.model.ui.ARP;
import com.alks.service.ARPService;

/**
 * The implementation of a Role Policies 
 * forming/retrieving mechanism
 * 
 * @author Shoban Sriram
 * @author Kamen Tsvetkov
 *
 */
@Service
public class ARPServiceImpl implements ARPService {

	private static Logger logger = Logger.getLogger(ARPServiceImpl.class);
	private static AccountRolePolicyDAO arpDao = new AccountRolePolicyDAO();
	//private static List<Account> accounts = new ArrayList<Account>();
	
	/**
	 * Return a Role Policies database object
	 * to get data from database
	 * 
	 * @return Role Policies database object
	 */
	public static AccountRolePolicyDAO getArpDao() {
		return arpDao;
	}

	@Override
	public List<ARP> getAllARPs() {
		List<ARP>  arpList = new ArrayList<ARP>();
		List<AccountRolePolicyRecord> recList = arpDao.getAllAccountRolePolicy();
		if(recList!=null){
			for(int i=0;i<recList.size();i++){
				AccountRolePolicyRecord ar = recList.get(i);
				ARP arp = new ARP();
				arp.setAccountNo(ar.getAccountNo());
				arp.setPolicy(ar.getPolicy());
				arp.setRole(ar.getRole());
				arp.setId(ar.getId());
				arp.setActive(ar.getActive());
				arp.setLastUpdatedBy(ar.getUser());
				arp.setLastUpdateTime(ar.getDate());
				arpList.add(arp);
			}
		}
		
		return arpList; 
	}

	/**
	 * Returns a list of Role Policies 
	 * given a account number
	 * 
	 * @param accountNo
	 * @return list of Role Policies
	 */
	public List<ARP> getARPByAccountNo(String accountNo){
		List<ARP>  arpList = new ArrayList<ARP>();
		
		List<AccountRolePolicyRecord> recList = arpDao.getARPByAccountNo(accountNo);
		if(recList!=null){
			for(int i=0;i<recList.size();i++){
				AccountRolePolicyRecord ar = recList.get(i);
				ARP arp = new ARP();
				arp.setAccountNo(ar.getAccountNo());
				arp.setPolicy(ar.getPolicy());
				arp.setRole(ar.getRole());
				arp.setId(ar.getId());
				arp.setActive(ar.getActive());
				arp.setLastUpdatedBy(ar.getUser());
				arp.setLastUpdateTime(ar.getDate());
				arpList.add(arp);
			}
		}
		
		return arpList; 
	}

	public String getAccountIdbyAccountNoAndAWSRole(String accountNo, String role){	
		List<AccountRolePolicyRecord> recList = arpDao.getARPByAccountNoAndAWSRole(accountNo, role);
		AccountRolePolicyRecord ar = null;
		if(recList!=null && recList.size()>0){

				 ar = recList.get(0);
					return ar.getId(); 
		}
		
		return "";
	}

	/**
	 * Get a Role Policies by using an
	 * account ID
	 * 
	 * @param accountId
	 * @return Role Policies
	 */
	public ARP getARPByAccountId(String accountId){
		List<String> accountIds = new ArrayList<String>();
		accountIds.add(accountId);
		
		List<AccountRolePolicyRecord> arpRecs = arpDao.getARPsByAccountIds(accountIds);
		if(arpRecs!=null && arpRecs.size()>0){
			ARP arp = new ARP();
			AccountRolePolicyRecord arpRec = arpRecs.get(0);
			arp.setAccountNo(arpRec.getAccountNo());
			arp.setId(arpRec.getId());
			arp.setPolicy(arpRec.getPolicy());
			arp.setRole(arpRec.getRole());
			arp.setActive(arpRec.getActive());
			arp.setLastUpdatedBy(arpRec.getUser());
			arp.setLastUpdateTime(arpRec.getDate());
			return arp;
		}else{
			return null;
		}
	}

	/**
	 * Get a list of Role Policies by using a
	 * list of account IDs
	 * 
	 * @param accountIds
	 * @return List of Role Policies
	 */
	public List<ARP> getARPByAccountId(List<String> accountIds){
		List<AccountRolePolicyRecord> arpRecs = arpDao.getARPsByAccountIds(accountIds);
		List<ARP> arps = new ArrayList<ARP>();
		if(arpRecs!=null && arpRecs.size()>0){
			for(int i =0;i<arpRecs.size();i++){
				ARP arp = new ARP();
				AccountRolePolicyRecord arpRec = arpRecs.get(i);
				arp.setAccountNo(arpRec.getAccountNo());
				arp.setId(arpRec.getId());
				arp.setPolicy(arpRec.getPolicy());
				arp.setRole(arpRec.getRole());
				arp.setActive(arpRec.getActive());
				arp.setLastUpdatedBy(arpRec.getUser());
				arp.setLastUpdateTime(arpRec.getDate());
				arps.add(arp);
			}
		}
		return arps;
	}

	
	@Override
	public void addARP(ARP arp) {
		
		//Check if already arp exists for same account number and role
		
		List<AccountRolePolicyRecord> arpList = arpDao.getARPByAccountNo(arp.getAccountNo());
		boolean updateFlag = true;
		if(arpList!=null){
		for(int i =0;i<arpList.size();i++){
			AccountRolePolicyRecord arpRec = arpList.get(i);
			if(arpRec.getRole().equals(arp.getRole())){
				//Update arp
				arpRec.setPolicy(arp.getPolicy());
				arpDao.saveARP(arpRec);
				updateFlag = false;
				break;
			}
		}
		}
		if(updateFlag){
			arpDao.saveARP(arp.getAccountNo(), arp.getRole(), arp.getPolicy(),arp.getLastUpdatedBy());
		}

	}

}
