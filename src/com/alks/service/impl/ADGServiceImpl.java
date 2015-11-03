package com.alks.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.alks.dao.AccountIdADGroupDAO;
import com.alks.model.db.AccountIdADGroupRecord;
import com.alks.model.ui.ADG;
import com.alks.service.ADGService;

/**
 * The implementation an AD Group
 * forming/retrieving mechanism
 * 
 * @author Shoban Sriram
 * @author Kamen Tsvetkov
 *
 */
@Service
public class ADGServiceImpl implements ADGService {

	private static Logger logger = Logger.getLogger(ADGServiceImpl.class);
	private static AccountIdADGroupDAO adgDao = new AccountIdADGroupDAO();	
	//private static List<Account> accounts = new ArrayList<Account>();
	
	@Override
	public List<ADG> getAllADGs() {
		List<ADG>  adgList = new ArrayList<ADG>();
		List<AccountIdADGroupRecord> recList = adgDao.getAllActiveAccountIDADGroup();
		if(recList!=null){
			for(int i=0;i<recList.size();i++){
				AccountIdADGroupRecord ar = recList.get(i);
				ADG adg = new ADG();
				adg.setAccountId(ar.getAccountId());
				adg.setAdGroup(ar.getAdGroup());
				adg.setLastUpdatedBy(ar.getUser());
				adg.setLastUpdateTime(ar.getDate());
				adgList.add(adg);
			}
		}
		
		return adgList; 
	}

//	/**
//	 * Returns the AD Group using the account ID
//	 * 
//	 * @param accountId
//	 * @return AD Group
//	 */
//	public ADG getADGByAccountId(String accountId){
//		
//		AccountIdADGroupRecord adgRec = adgDao.getAccountIdGroup(accountId);
//		ADG adg = new ADG();
//		
//		adg.setAccountId(adgRec.getAccountId());
//		adg.setAdGroup(adgRec.getAdGroup());
//		adg.setLastUpdatedBy(adgRec.getUser());
//		adg.setLastUpdateTime(adgRec.getDate());
//		
//		return adg; 
//	}
	
		
	@Override
	public void addADG(ADG adg) {
		
		adgDao.saveAccountIdGroupRecord(adg.getAccountId(), adg.getAdGroup(),adg.getLastUpdatedBy());

	}

	@Override
	public boolean deleteADG(ADG adg) {
		
		return adgDao.deleteAccountIdGroupRecord(adg.getAccountId(), adg.getAdGroup(),adg.getLastUpdatedBy());

	}

}
