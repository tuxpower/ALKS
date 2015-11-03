package com.alks.service.config;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
 
import com.alks.model.ui.User;
 
/**
 * A class that redirects the user based on authentication
 * 
 * @author Shoban Sriram
 * @author Kamen Tsvetkov
 *
 */

public class LoginFilter implements Filter {

	private static Logger logger = Logger.getLogger(LoginFilter.class);
 
	/**
	 * Creates the login filter
	 * 
	 * @param filterConfig
	 */
	public void init(FilterConfig filterConfig) throws ServletException {
	}
 
	/**
	 * Destroys the login filter
	 * 
	 */
	public void destroy() {
	}

	/**
	 * Use the login filter to redirect the user
	 * 
	 * @param request
	 * @param response
	 * @param chain
	 */
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {

		HttpServletResponse resp = (HttpServletResponse) response;
		HttpServletRequest req = (HttpServletRequest) request;
		String servletPath = req.getServletPath();
		HttpSession session = req.getSession();
		User sessionUser = (User) session.getAttribute("user");

		if(servletPath.contains("/rest") || servletPath.equals("/index.jsp") ||
									servletPath.equals(req.getContextPath()+"/index.jsp") || 
										servletPath.equals("/login/loginPage.htm") ||
										servletPath.equals("/login/validate.htm")||
										servletPath.endsWith("getAllAccounts.htm") ||
										servletPath.endsWith("/login/logout.htm")){
			chain.doFilter(req, resp);
			return;
		}
		
/*
		logger.debug("Path: " + servletPath);
		// Allow access to login functionality.
		if (servletPath.equals("/index.jsp") || 
				servletPath.equals(req.getContextPath()+"/index.jsp") || 
				servletPath.equals("/login/loginPage.htm")) {
			
			if(servletPath.equals("/login/validate.htm") || servletPath.equals("/login/loginPage.htm")){
				if(sessionUser == null){
					logger.info("Session expired so redirect to login page.");					
					resp.sendRedirect(req.getContextPath());
				}
				return;
			}

					
			chain.doFilter(req, resp);
			return;
		}
*/
		
		// Allow access to news feed.
		if (servletPath.endsWith(".png") || (servletPath.endsWith(".css")) || (servletPath.endsWith(".jpg") )) {
			chain.doFilter(req, resp);
			return;
		}
		// All other functionality requires authentication.
		logger.info("is User object null : "+sessionUser);
		if (sessionUser != null && sessionUser.isAuthenticated()) {
			
			// User is logged in.
			
			//But if user is general user do not allow admin pages
			//Allow all user related paths.
			if(!servletPath.equals("/login/getKeys.htm") && !servletPath.equals("/login/validate.htm") && !sessionUser.isAdmin()){
				resp.sendRedirect(req.getContextPath()+"/login/validate.htm");
			}
			
			chain.doFilter(req, resp);
			return;
		}
		
		

		// Request is not authorized.
		resp.sendRedirect(req.getContextPath());
	}
}
