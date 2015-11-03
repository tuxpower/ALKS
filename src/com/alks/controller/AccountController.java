package com.alks.controller;

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
import com.alks.model.ui.Account;
import com.alks.model.ui.User;
import com.alks.service.AccountService;
import com.alks.service.aws.MasterIAMService;

/**
 * The controller for accounts section of ALKS
 * 
 * @author Shoban Sriram
 * @author Kamen Tsvetkov
 *
 */

@Controller
@RequestMapping(value="account")
public class AccountController extends MasterController {

	private static Logger logger = Logger.getLogger(AccountController.class);
	private static String ACCOUNT_VIEW_ALL = "accountViewAll";
	private static String NEW_ACCOUNT = "newAccount";

	@Autowired
	private AccountService accountService;
	
	/**
	 * A request to take the user to the Accounts page
	 * 
	 * @return mav an object containing all page attributes
	 */
	@RequestMapping(value="viewAllAccounts", method = RequestMethod.GET)
	public ModelAndView viewAll(){
		ModelAndView mav = new ModelAndView();
		mav.setViewName(ACCOUNT_VIEW_ALL);
		mav.addObject("accounts", accountService.getAllAccounts());
		mav.addObject("account",new Account());
		logger.info("action=viewAllAccounts");
		return mav;
	}
	
	/**
	 * A request to take the user to the add new Accounts page
	 * 
	 * @param error
	 * @return mav an object containing all page attributes
	 */
	@RequestMapping(value="new", method = RequestMethod.GET)
	public ModelAndView newAccount(){
		ModelAndView mav = new ModelAndView();
		mav.setViewName(NEW_ACCOUNT);
		mav.addObject("account", new Account());
		logger.info("action=newAccount");
		return mav;
	}
	
	/**
	 * A request to add a new Account
	 * 
	 * @param arp
	 * @param request
	 * @return a string to redirect the page back to Accounts page
	 */
	@RequestMapping(value="add", method=RequestMethod.POST)
	public String add(@ModelAttribute(value="accountNo") Account account, HttpServletRequest request){
		
		User sessionUser = (User) request.getSession().getAttribute("user");
		
		accountService.addAccount(account,sessionUser.getEmailId());

		logger.info("Account="+ account + " User="+sessionUser);
		
		return "redirect:viewAllAccounts.htm";
	}
		
	/**
	 * A request to get the role number of an account
	 * 
	 * @param accessKey
	 * @param secretKey
	 * @param request
	 * @return string representation of an array that contains the account numbers
	 */
	@RequestMapping(value = "getAccountNo", method = RequestMethod.GET)
	public @ResponseBody String getAccountNo(@RequestParam(value = "accessKey") String accessKey,
			@RequestParam(value = "secretKey") String secretKey, HttpServletRequest request) {
		
		//TODO Check if there is a better way to handle + 
		//Currently + is converted to empty space. 
		if(secretKey!=null && !secretKey.isEmpty() && secretKey.contains(" ")){
			secretKey = secretKey.replace(" ", "+");
		}
		
		JSONArray arr = new JSONArray();
		if(accessKey!=null && accessKey.length()>0 && secretKey!=null && secretKey.length()>0){
	
		String[] accountNoAlias = MasterIAMService.getAccountNumberByKeys(accessKey, secretKey);
		
		arr.put(accountNoAlias[0]);
		arr.put(accountNoAlias[1]);
		arr.put(accountNoAlias[2]);
		
		logger.info("returing=" + arr);
		}
		return arr.toString();
	}	
	
	/**
	 * A request to update the info of an account
	 * 
	 * @param accountNumber
	 * @param flag
	 * @param request
	 * @return a string to redirect the page back to Accounts page
	 */
	@RequestMapping(value="updateAccount", method=RequestMethod.GET)
	public String updateAccount(@RequestParam(value = "accountNo", required = true) String accountNumber, @RequestParam(value = "flag", required = true) String flag, HttpServletRequest request){
			
		User sessionUser = (User) request.getSession().getAttribute("user");
		logger.info("AccountNo="+accountNumber+ " Update flag="+flag + " User="+sessionUser);
	
		if(flag!=null && flag.equals("1")){
			accountService.inactivateAccount(accountNumber,sessionUser.getEmailId());
		}
		
		if(flag!=null && flag.equals("0")){
			accountService.activateAccount(accountNumber,sessionUser.getEmailId());
		}

		if(flag!=null && flag.equals("2")){
			accountService.regenerateAccountKey(accountNumber,sessionUser.getEmailId());
		}
		
		return "redirect:viewAllAccounts.htm";
	}
	
	
	/*  
	 //TODO This is a generic error handler for account related functionality. Uncommet and customize if needed
	 @ExceptionHandler(Exception.class)
	  public ModelAndView handleError(HttpServletRequest req, Exception exception) {
	    logger.error("Request: " + req.getRequestURL() + " raised " + exception);
	    ModelAndView mav = new ModelAndView();
	    mav.addObject("exception", exception);
	    mav.addObject("url", req.getRequestURL());
	    mav.setViewName("error");
	    return mav;
	  }
	
	@RequestMapping(value="delete", method=RequestMethod.POST)
	public String delete(@ModelAttribute(value="account") Account account){
		//account.setDeleteFlag(true);
		accountService.addAccount(account);
		return "redirect:viewAllAccounts.htm";
	}
	*/
}
