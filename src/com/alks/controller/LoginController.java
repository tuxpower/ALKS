package com.alks.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import com.amazonaws.util.json.JSONArray;
import com.alks.model.ui.Account;
import com.alks.model.ui.User;
import com.alks.service.config.MessageUtils;
import com.alks.service.delegate.AccountMaster;
import com.alks.service.delegate.GenerateKeyDelegate;
import com.alks.service.impl.ADGroupsServiceImpl;

/**
 * The controller for logging in and the ALKS home page
 * 
 * @author Shoban Sriram
 * @author Kamen Tsvetkov
 *
 */

@Controller
@Scope("session")
@RequestMapping(value="login")
@SessionAttributes({"user"})
public class LoginController {
	private static Logger logger = Logger.getLogger(LoginController.class);
	private static String ADMIN_AD_GROUP = MessageUtils.getMessage("ldap.service.account.adminGroup");
	
	/**
	 * A request for the login page
	 * 
	 * @param request
	 * @return mav an object containing all page attributes
	 */
	@RequestMapping(value="loginPage")
	public ModelAndView loginPage(HttpServletRequest request){
		request.getSession().invalidate();
		ModelAndView mav = new ModelAndView();
		mav.setViewName("loginPage");
		mav.addObject("user",new User() );
		return mav;
	}

	/**
	 * A request for the user to be validated
	 *  
	 * @param user
	 * @param modelMap
	 * @param request
	 * @return mav an object containing all page attributes
	 */
	@RequestMapping(value="validate")
	public ModelAndView validate(@ModelAttribute(value="user") User user, ModelMap modelMap, HttpServletRequest request){
		ModelAndView mav = new ModelAndView();
	
		if(user!=null && !user.isAuthenticated()){
			String ipAddress = MessageUtils.getIpAddr(request);  
			   if (ipAddress == null) {  
				   ipAddress = request.getRemoteAddr();  
			   }
			logger.info("ClientIP="+ipAddress + " User="+user);
	
			if(request.getSession().getAttribute("user")==null){
				logger.info("User is null");
				mav.addObject("error", "Session Expired please re-login.");
				mav.setViewName("loginPage");
				return mav;
			}
	
			List<String> groups = AccountMaster.getGroups(user);
			
			//TODO checks user if he belongs to admin AD group. Move to properties file.
			if(groups!=null && groups.contains(ADMIN_AD_GROUP)){
				user.setAdmin(true);
				user.setAuthenticated(true);
				//If the logged in user is an admin then get all the groups so that he can create token for any role.
				//Else we can change the logic so that admin can only create tokens when he adds himself to the AD group.
				groups = ADGroupsServiceImpl.getADGroupsByServiceAccount();
				logger.info("Admin user="+ user);
			}
			request.getSession().setAttribute("groups", groups);
		}
		
		//We need to get a fresh list of accounts from DB as users can change the account in their session. 
		
		List<String> groups = (List<String>) request.getSession().getAttribute("groups");
		
		Map<String,List<String>> acctRoles = new HashMap<String, List<String>>();//AccountMaster.getAccounts(user);

		logger.info("Logged in user to alks. User=" + user);		

		
		if(groups==null){
			//Invalid userid password
			mav.addObject(MessageUtils.ERROR, MessageUtils.getMessage("com.alks.controller.LoginController.login.inValid") );
			logger.error(MessageUtils.getMessage("com.alks.controller.LoginController.login.inValid"));
			logger.error("User:" + user);
			mav.addObject("user",new User());
			mav.setViewName("loginPage");
		} else{
			user.setAuthenticated(true);
			if(groups.size()>0){
			acctRoles = AccountMaster.getAccounts(groups);
			}
			logger.debug(acctRoles);

			Set<String> accounts = acctRoles.keySet();
			
			logger.debug("accounts:"+accounts);
			/*
			//TODO This logic is needed only if we decide to make a user admin if he has been assigned to AWS Admin group and not LDAP.
			Iterator<String> itr = accounts.iterator();
				while(itr.hasNext()){
					List<String> roles = acctRoles.get(itr.next());
					if(roles!=null && roles.size()>0){
						for(int j = 0; j < roles.size();j++){
							String role = roles.get(j);
							logger.info("Role="+role);
							if(role!=null && !role.trim().equals("") && role.trim().contains("AdministratorAccessRole")){
								user.setAdmin(true);
								logger.debug("Setting admin previlege");
							}
					}
				}
			}
			*/
			
			
		mav.addObject("acctRoles", acctRoles );
		logger.debug("User has admin?"+user.isAdmin());
		logger.debug("User emailid:"+user.getEmailId());

				modelMap.addAttribute(user);
				mav.addObject("selectedAccount",new Account());
				mav.addObject("emailId", user.getEmailId());
				mav.setViewName("listRoles");
		}
		
		return mav;
	}

	/**
	 * A request to logout the user
	 * 
	 * @param user
	 * @param modelMap
	 * @param request
	 * @return mav an object containing all page attributes
	 */
	@RequestMapping(value="logout")
	public ModelAndView logout(HttpServletRequest request){
		User sessionUser = (User) request.getSession().getAttribute("user");
		logger.info("Logging out user="+sessionUser);
		request.getSession().setAttribute("user", null);
		request.getSession().invalidate();
		ModelAndView mav = new ModelAndView();
		mav.setViewName("loginPage");
		mav.addObject("user",new User() );
		return mav;
	}
	
	/**
	 * A request to return keys for the ALKS home page
	 * 
	 * @param user
	 * @param modelMap
	 * @param request
	 * @return mav an object containing all page attributes
	 */
	@RequestMapping(value = "getKeys", method = RequestMethod.GET)
	public @ResponseBody String getAccountId(@RequestParam(value = "selectedAccount", required = true) String accountNumber,
			@RequestParam(value = "selectedRole", required = true) String role, @RequestParam(value = "time", required = true) String time, HttpServletRequest request) {
		
		logger.debug("Selected account number is "+ accountNumber);
		logger.debug("Selected role is "+ role);
		logger.info("Selected time is "+time);
		
		User sessionUser = (User) request.getSession().getAttribute("user");
		logger.debug(sessionUser.getEmailId());
		accountNumber = MessageUtils.getAccountString(accountNumber);
		logger.debug("accountNo:" + accountNumber);
		logger.debug("User email:" + sessionUser.getEmailId());
		String userName = sessionUser.getEmailId().substring(0, sessionUser.getEmailId().indexOf('@'));
		logger.debug(userName);
		String[] keys = GenerateKeyDelegate.getKeys(userName, accountNumber,role,time);
	
		JSONArray arr = new JSONArray();
		arr.put(keys[0]);
		arr.put(keys[1]);
		arr.put(keys[2]);
		arr.put(keys[3]);

		
		logger.debug("returing keys: AccessKey:" + keys[0] );		
		return arr.toString();
	}
	
	/*
	// TODO This is a generic error handler for Login/getKeys methods. Uncommet and customize if needed
	 @ExceptionHandler(Exception.class)
	  public ModelAndView handleError(HttpServletRequest req, Exception exception) {
	    logger.error("Request: " + req.getRequestURL() + " raised " + exception);
	    ModelAndView mav = new ModelAndView();
	    mav.addObject("exception", exception);
	    mav.addObject("url", req.getRequestURL());
	    mav.setViewName("error");
	    return mav;
	  }
	*/
}
