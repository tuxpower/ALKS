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

import com.alks.model.ui.ARP;
import com.alks.model.ui.Account;
import com.alks.model.ui.User;
import com.alks.service.ARPService;
import com.alks.service.aws.MasterIAMService;
import com.alks.service.config.MessageUtils;
import com.alks.service.delegate.ARPDeligate;
import com.alks.service.delegate.GenerateKeyDelegate;
import com.alks.service.impl.AccountServiceImpl;
import com.amazonaws.services.identitymanagement.model.Role;
import com.amazonaws.util.json.JSONArray;

/**
 * The controller for Role Policies section of ALKS
 * 
 * @author Shoban Sriram
 * @author Kamen Tsvetkov
 *
 */

@Controller
@RequestMapping(value="arp")
public class ARPController extends MasterController{

		private static Logger logger = Logger.getLogger(ARPController.class);

		@Autowired
		private ARPService arpService;
		@Autowired
		private AccountServiceImpl acctService;
		
		/**
		 * A request to take the user to the Role Policy page
		 * 
		 * @return mav an object containing all page attributes
		 */
		@RequestMapping(value="viewAllARP", method = RequestMethod.GET)
		public ModelAndView viewAll(){
			ModelAndView mav = new ModelAndView();
			mav.setViewName("arpViewAll");
			mav.addObject("arps", ARPDeligate.getAllARP());
			return mav;
		}
		
		/**
		 * A request to take the user to the add new Role Policy page
		 * 
		 * @param error
		 * @return mav an object containing all page attributes
		 */
		@RequestMapping(value="new", method = RequestMethod.GET)
		public ModelAndView newARP(@RequestParam(value = "error", required = false) String error){
			//
			ModelAndView mav = new ModelAndView();
			List<Account> accounts = acctService.getAllActiveAccounts();
		
			List<String>  accountNumbers = new ArrayList<String>();
			for(int i=0;i<accounts.size();i++){
				Account acct = accounts.get(i);
				accountNumbers.add(acct.getAccountNo()+" - "+acct.getAccountDesc());
			}
			if(accounts == null || accounts.size()<1){
				mav.addObject(MessageUtils.ERROR,MessageUtils.getMessage("com.alks.controller.ARPController.new.noAccount"));
			}
			if(error!=null && error.equals("true")){
				mav.addObject(MessageUtils.ERROR,MessageUtils.getMessage("com.alks.controller.ARPController.new.invalidPolicy"));
			}
			
			mav.addObject("accounts", accountNumbers );
			mav.setViewName("newARP");
			mav.addObject("arp", new ARP());
			return mav;
		}
		
		/**
		 * A request to add a role policy relationship
		 * 
		 * @param arp
		 * @param request
		 * @return a string to redirect the page back to Role Policies
		 */
		@RequestMapping(value="add", method=RequestMethod.POST)
		public String add(@ModelAttribute(value="arp") ARP arp, HttpServletRequest request){

			String arpDesc = arp.getAccountNo();
			logger.debug("AccountDesc="+ arpDesc);
			arp.setAccountNo(arpDesc.substring(0, 12));
			User sessionUser = (User) request.getSession().getAttribute("user");
			arp.setLastUpdatedBy(sessionUser.getEmailId());
			
			//Validate policy by creating a fake session tokens
			String[] keys = null;
			try{
					keys = GenerateKeyDelegate.validatePolicy(arp.getAccountNo(),arp.getPolicy());
			}catch(Exception e){
				
			}
			if(keys==null){
				logger.debug("Keys null, invalid policy");
				return "redirect:new.htm?error=true";
			}else{			
				arpService.addARP(arp);
				logger.info("Add new ARP="+arp);
			}
			return "redirect:viewAllARP.htm";
		}
		
		/**
		 * Request to get the roles for the Role Policies controller
		 * 
		 * @param accountNumber
		 * @return string representation of an array that contains the roles
		 */
		@RequestMapping(value = "getRoles", method = RequestMethod.GET)
		public @ResponseBody String getRoles(@RequestParam(value = "accountNumber", required = true) String accountNumber) {
			
			logger.debug("accountNo="+ accountNumber);
			accountNumber = accountNumber.substring(0,12);
			AccountServiceImpl asi = new AccountServiceImpl();
			String[] keys = asi.getLongTermKeys(accountNumber);
			
			List<Role> roles = new ArrayList<Role>();
			try{
			roles = MasterIAMService.getRolesByKeys(keys[0], keys[1]);
			}catch(Exception e){
			//Handle exception	
			}
			JSONArray arr = new JSONArray();
			
			for(int i =0;i<roles.size();i++){
				Role role = roles.get(i);
				arr.put(role.getRoleName());
			}

			return arr.toString();
		}

		/**
		 * Request to get the policies for the Role Policies controller
		 * 
		 * @param accountNumber
		 * @param roleName
		 * @return string representation of an array that contains the policies
		 */
		@RequestMapping(value = "getPolicy", method = RequestMethod.GET)
		public @ResponseBody String getPolicy(@RequestParam(value = "accountNumber", required = true) String accountNumber,@RequestParam(value = "roleName", required = true) String roleName) {
			
			logger.debug("accountNo="+ accountNumber);
			accountNumber = accountNumber.substring(0,12);
			AccountServiceImpl asi = new AccountServiceImpl();
			String[] keys = asi.getLongTermKeys(accountNumber);
			
			
			List<Role> roles = new ArrayList<Role>();
			try{
			roles = MasterIAMService.getRolesByKeys(keys[0], keys[1]);
			}catch(Exception e){
			//Handle exception	
			}
			
			
//			ARPServiceImpl asi = new ARPServiceImpl();
//			
//			List<ARP> arps = asi.getARPByAccountNo(accountNumber);
//			
			JSONArray arr = new JSONArray();
			
			for(int i =0;i<roles.size();i++){
				Role role = roles.get(i);
				arr.put(role.getRoleName());
			}

			return arr.toString();
		}

		/*
		@RequestMapping(value="delete", method=RequestMethod.POST)
		public String delete(@ModelAttribute(value="arp") ARP arp){
			//account.setDeleteFlag(true);
			arpService.addARP(arp);
			return "redirect:viewAllARP.htm";
		}
		*/

}
