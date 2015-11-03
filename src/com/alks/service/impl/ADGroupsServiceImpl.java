package com.alks.service.impl;


import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import javax.naming.AuthenticationException;
import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;

import org.apache.log4j.Logger;

import com.alks.service.config.MessageUtils;

public class ADGroupsServiceImpl {
	
	private static Logger logger = Logger.getLogger(ADGroupsServiceImpl.class);

    public static final String DISTINGUISHED_NAME = MessageUtils.getMessage("ldap.service.account.conf.dn");
    public static final String CN = "cn";
    public static final String MEMBER_OF = "memberOf";
    public static final String MAIL = "(mail={0})";
    public static final String SEARCH_GROUP_BY_GROUP_CN = MessageUtils.getMessage("ldap.service.account.conf.search_group");
    

 
    
    /**
     * Returns whether the target and the candidate
     * are the same
     * 
     * @param target
     * @param candidate
     * @return whether target and candidate are the same
     */
    public static boolean isSame(String target, String candidate) {
        if (target != null && target.equalsIgnoreCase(candidate)) {
            return true;
        }
        return false;
    }

    /**
     * Authenticated a user and returns a list of groups
     * that the user belongs to
     * 
     * @param username
     * @param password
     * @return list of groups
     */
    public static List<String> authenticate(String username, String password) {
    	logger.debug("Starting.....");
    	if(username==null || password==null){
    		return null; 
    	}
    	List<String> awsList = null;
        Hashtable<String,String> env = new Hashtable<String,String>();
        env.put(Context.INITIAL_CONTEXT_FACTORY,"com.sun.jndi.ldap.LdapCtxFactory");
        env.put(Context.PROVIDER_URL, MessageUtils.getMessage("ldap.service.account.url"));
        env.put(Context.SECURITY_AUTHENTICATION, "simple");
        env.put(Context.SECURITY_PRINCIPAL, username);
        env.put(Context.SECURITY_CREDENTIALS, password);
        DirContext ctx = null;
        String defaultSearchBase = MessageUtils.getMessage("ldap.service.account.conf.search_base");

        try {
        	logger.debug("In the try block");
            ctx = new InitialDirContext(env);
            logger.debug("After InitialDirContext");
            
            // userName is SAMAccountName
            SearchResult sr = executeSearchSingleResult(ctx, SearchControls.SUBTREE_SCOPE, defaultSearchBase,
                    MessageFormat.format( MAIL, new Object[] {username}),
                    new String[] {DISTINGUISHED_NAME, CN, MEMBER_OF}
                    );
            logger.debug("Search results :" + sr.getName());
            
            // Look for and process memberOf
            Attribute memberOf = sr.getAttributes().get(MEMBER_OF);
            
            logger.debug("Member of :"+memberOf);
            NamingEnumeration<?> ne = memberOf.getAll();
            
            awsList = new ArrayList<String>();
            
            while(ne.hasMoreElements()){
            	String member = (String) ne.nextElement();
            	
            	if(member!=null && member.startsWith("CN=AWS")){
            		member = member.substring(3,member.indexOf(","));
            		awsList.add(member);
            	}
            	
            }
            logger.debug("Size in auth method: "+ awsList.size());

            return awsList;
        } catch (AuthenticationException e) {
        	logger.info(username + " is NOT authenticated");
            return null;
        } catch (NamingException e) {
        	logger.info("Unable to connect");
        	logger.fatal(e.getStackTrace());
        	 return null; 
        } finally {
            if (ctx != null) {
                try {
                    ctx.close();
                } catch (NamingException e) {
                    
                }
            }
        }
    }

  
    /**
     * Fetches groups given certain attributes
     * 
     * @param ctx
     * @param searchScope
     * @param searchBase
     * @param searchFilter
     * @param attributes
     * @return groups
     * @throws NamingException
     */
    private static NamingEnumeration<SearchControls> executeSearch(DirContext ctx, int searchScope,  String searchBase, String searchFilter, String[] attributes) throws NamingException {
        // Create the search controls
        SearchControls searchCtls = new SearchControls();

        // Specify the attributes to return
        if (attributes != null) {
            searchCtls.setReturningAttributes(attributes);
        }

        // Specify the search scope
        searchCtls.setSearchScope(searchScope);
        logger.debug("In executeSearch");
        // Search for objects using the filter
        NamingEnumeration result = ctx.search(searchBase, searchFilter,searchCtls);
        return result;
    }

    /**
     * Fetches a group given certain attributes
     * 
     * @param ctx
     * @param searchScope
     * @param searchBase
     * @param searchFilter
     * @param attributes
     * @return group
     * @throws NamingException
     */
    private static SearchResult executeSearchSingleResult(DirContext ctx, int searchScope,  String searchBase, String searchFilter, String[] attributes) throws NamingException {
        NamingEnumeration result = executeSearch(ctx, searchScope,  searchBase, searchFilter, attributes);

        SearchResult sr = null;
        // Loop through the search results
        while (result.hasMoreElements()) {
            sr = (SearchResult) result.next();
            break;
        }
        return sr;
    }
    
    //TODO Do we need to cache this list? How frequently are we going to change the groups.
    /**
     * Returns a list of AD Groups using the 
     * service account
     * 
     * @return list of AD Groups
     */
    public static List<String> getADGroupsByServiceAccount() {
    	
    	String username = MessageUtils.getMessage("ldap.service.account.username");
      	String password = MessageUtils.getMessage("ldap.service.account.password");
        List<String> awsAllGroups = null;  	
    	
    	logger.debug("Starting.....");
    	if(username==null || password==null){
    		return new ArrayList<String>(); 
    	}
        Hashtable<String,String> env = new Hashtable<String,String>();
        env.put(Context.INITIAL_CONTEXT_FACTORY,"com.sun.jndi.ldap.LdapCtxFactory");
        env.put(Context.PROVIDER_URL, MessageUtils.getMessage("ldap.service.account.url"));
        env.put(Context.SECURITY_AUTHENTICATION, "simple");
        env.put(Context.SECURITY_PRINCIPAL, username);
        env.put(Context.SECURITY_CREDENTIALS, password);
        DirContext ctx = null;

        try {
        	logger.debug("Getting AWS groups using service account");
            ctx = new InitialDirContext(env);
            logger.debug("After InitialDirContext");
     	   SearchControls controls = new SearchControls();
    	   controls.setSearchScope(SearchControls.SUBTREE_SCOPE);
    	   String attr[] = {"samaccountname"};
    	   controls.setReturningAttributes(attr);
    	   NamingEnumeration<SearchResult> result =  ctx.search(MessageUtils.getMessage("ldap.service.account.conf.name"),MessageUtils.getMessage("ldap.service.account.conf.filter"), controls);
    	   awsAllGroups = new ArrayList<String>();
    	   
    	     SearchResult sr = null;
    	        // Loop through the search results
    	        while (result.hasMoreElements()) {
    	            sr = (SearchResult) result.next();
    	            awsAllGroups.add(sr.getName().substring(3));
    	        }  
    	        
            return awsAllGroups;
        } catch (AuthenticationException e) {
        	logger.error("Service account details are not correct."+e.getMessage());
            return null;
        } catch (NamingException e) {
        	logger.error("Unable to connect"+e.getMessage());
        	 return new ArrayList<String>(); 
        } finally {
            if (ctx != null) {
                try {
                    ctx.close();
                } catch (NamingException e) {
                    
                }
            }
        }
    }

}