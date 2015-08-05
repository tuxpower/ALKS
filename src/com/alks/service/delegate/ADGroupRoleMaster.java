package com.alks.service.delegate;

import java.util.List;

import org.apache.log4j.Logger;

import com.alks.model.ui.ADG;
import com.alks.model.ui.ARP;
import com.alks.model.ui.Account;
import com.alks.service.impl.ADGServiceImpl;
import com.alks.service.impl.ARPServiceImpl;
import com.alks.service.impl.AccountServiceImpl;
 
/**
 * A class that is used to get AD Group data
 * 
 * @author Shoban Sriram
 * @author Kamen Tsvetkov
 *
 */
public class ADGroupRoleMaster {

	private static Logger logger = Logger.getLogger(ADGroupRoleMaster.class);

	/**
	 * Gets account role policy values for each AD group.
	 * 
	 * @return role policy values
	 */
	public static List<ADG> getAllADG(){
		 ADGServiceImpl adgService = new ADGServiceImpl();
		 ARPServiceImpl arpService = new ARPServiceImpl();
		 AccountServiceImpl actService = new AccountServiceImpl();
		List<ADG> list = adgService.getAllADGs(); 
		//TODO Check how to reduce number of calls to arpService.
		if(list!=null && list.size()>0){
			for(int i =0;i<list.size();i++){
				ADG adg = list.get(i);
				ARP arp = arpService.getARPByAccountId(adg.getAccountId());
				Account act = actService.getAccountByAccountNo(arp.getAccountNo());
				adg.setAccountNo(arp.getAccountNo() +" - " +act.getAccountDesc());
				adg.setRole(arp.getRole());
			}
		}
		return list;
	}

	/**
	 * Gets account role policy values for each AD group
	 * that matches the account number given
	 * 
	 * @return role policy values
	 */
	public static List<ADG> getADGByAccountNo(String accountNo){
		 ADGServiceImpl adgService = new ADGServiceImpl();
		 ARPServiceImpl arpService = new ARPServiceImpl();
		 List<ARP> arpList = arpService.getARPByAccountNo(accountNo);

		List<ADG> list = adgService.getAllADGs(); 
		//TODO Check how to reduce number of calls to arpService.
		if(arpList!=null && arpList.size()>0){
			for(int i =0;i<arpList.size();i++){
				ARP arp = arpList.get(i);				
				ADG adg = adgService.getADGByAccountId(arp.getId());
				adg.setAccountNo(arp.getAccountNo());
				adg.setRole(arp.getRole());
			}
		}
		return list;
	}
	
	
	
}
