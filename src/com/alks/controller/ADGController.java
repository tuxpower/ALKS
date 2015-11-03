package com.alks.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.amazonaws.util.json.JSONArray;
import com.alks.model.ui.ADG;
import com.alks.model.ui.ARP;
import com.alks.model.ui.Account;
import com.alks.model.ui.User;
import com.alks.service.config.MessageUtils;
import com.alks.service.delegate.ADGroupRoleMaster;
import com.alks.service.impl.ADGServiceImpl;
import com.alks.service.impl.ARPServiceImpl;
import com.alks.service.impl.ADGroupsServiceImpl;
import com.alks.service.impl.AccountServiceImpl;

/**
 * The controller for AD Groups section of ALKS
 * 
 * @author Shoban Sriram
 * @author Kamen Tsvetkov
 *
 */

@Controller
@RequestMapping(value = "adg")
public class ADGController extends MasterController {

	private static Logger logger = Logger.getLogger(ADGController.class);

	@Autowired
	private ADGServiceImpl adgService;
	@Autowired
	private AccountServiceImpl acctService;

	/**
	 * A request to take the user to the AD Groups page
	 * 
	 * @return mav an object containing all page attributes
	 */
	@RequestMapping(value = "viewAllADG", method = {RequestMethod.GET, RequestMethod.POST})
	public ModelAndView viewAll() {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("adgViewAll");
		mav.addObject("adgs", ADGroupRoleMaster.getAllADG());
		mav.addObject("adg",new ADG());
		logger.debug("action=viewAllADG");
		return mav;
	}

	/**
	 * A request to take the user to the add new AD Groups page
	 * 
	 * @param error
	 * @return mav an object containing all page attributes
	 */
	@RequestMapping(value = "new", method = {RequestMethod.GET,RequestMethod.POST})
	public ModelAndView newADG(HttpServletRequest request) {
		// Get All the AD groups
		ModelAndView mav = new ModelAndView();
		List<Account> accounts = acctService.getAllAccounts();
		List<String> accountNumbers = new ArrayList<String>();
		for (int i = 0; i < accounts.size(); i++) {
			Account acct = accounts.get(i);
			accountNumbers.add(MessageUtils.getAccountDisplayString(acct.getAccountNo(), acct.getAccountDesc()));
		}

		mav.addObject("accounts", accountNumbers);

		//List<String> adGroups = AWSGroupsServiceImpl.authenticate(sessionUser.getEmailId(), sessionUser.getPassword());
		List<String> adGroups = ADGroupsServiceImpl.getADGroupsByServiceAccount();
		
		mav.addObject("adGroups", adGroups);

		mav.setViewName("newADG");
		mav.addObject("adg", new ADG());
		logger.debug("action=newADG");
		return mav;
	}

	/**
	 * A request to add a new AD Group
	 * 
	 * @param arp
	 * @param request
	 * @return a string to redirect the page back to AD Groups
	 */
	@RequestMapping(value = "add", method = RequestMethod.POST)
	public ModelAndView add(@ModelAttribute(value = "adg") ADG adg, HttpServletRequest request) {
		User sessionUser = (User) request.getSession().getAttribute("user");
		adg.setLastUpdatedBy(sessionUser.getEmailId());
		adg.setAccountNo(MessageUtils.getAccountString(adg.getAccountNo()));
		adgService.addADG(adg);
		logger.info("Added new ADG="+adg +" sessionUser="+sessionUser);
		ModelAndView mav = new ModelAndView();
		mav.setViewName("adgViewAll");
		mav.addObject("adgs", ADGroupRoleMaster.getAllADG());
		mav.addObject("adg",new ADG());
		return mav;
	}

	/**
	 * Request to get the roles for the AD Groups controller
	 * 
	 * @param accountNumber
	 * @return string representation of an array that contains the roles
	 */
	@RequestMapping(value = "getRoles", method = RequestMethod.GET)
	public @ResponseBody String getRoles(
			@RequestParam(value = "accountNumber", required = true) String accountNumber) {

		logger.info("accountNo=" + accountNumber);

		ARPServiceImpl arpService = new ARPServiceImpl();

		List<ARP> arps = arpService.getARPByAccountNo(MessageUtils.getAccountString(accountNumber));

		JSONArray arr = new JSONArray();
		if (arps != null && arps.size() > 0) {
			for (int i = 0; i < arps.size(); i++) {
				ARP arp = arps.get(i);
				arr.put(arp.getRole());
			}
		}

		return arr.toString();
	}

	/**
	 * A request to get the account IDs for the AD Groups controller
	 * 
	 * @param accountNumber
	 * @param role
	 * @return string representation of an array that contains the account IDs
	 */
	@RequestMapping(value = "getAccountId", method = RequestMethod.GET)
	public @ResponseBody String getAccountId(
			@RequestParam(value = "accountNumber", required = true) String accountNumber,
			@RequestParam(value = "role", required = true) String role) {

		logger.info("accountNo=" + accountNumber + " role=" + role);

		ARPServiceImpl arpService = new ARPServiceImpl();
		String accountId = arpService.getAccountIdbyAccountNoAndAWSRole(MessageUtils.getAccountString(accountNumber), role);
		JSONArray arr = new JSONArray();
		arr.put(accountId);
		logger.info("getAccountId=" + arr);
		return arr.toString();
	}


	@RequestMapping(value="deleteADG", method=RequestMethod.GET)
	public String updateAccount(@RequestParam(value = "accountId", required = true) String accountId, @RequestParam(value = "adGroup", required = true) String adGroup, HttpServletRequest request){
			
		User sessionUser = (User) request.getSession().getAttribute("user");
		logger.info("AccountId="+accountId+ " Update flag="+adGroup + " User="+sessionUser);

		// account.setDeleteFlag(true);
		ADG adg = new ADG();
		adg.setAccountId(accountId);
		adg.setAdGroup(adGroup);
		adg.setLastUpdatedBy(sessionUser.getEmailId());
		
		boolean flag = adgService.deleteADG(adg);
		
		//TODO Handle error/success
		
		if(flag){
			
		}else{
			
		}
		
		return "redirect:viewAllADG.htm";
	}
	
}
