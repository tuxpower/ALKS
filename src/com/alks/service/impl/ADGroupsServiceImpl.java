package com.alks.service.impl;


import java.text.MessageFormat;
import java.util.*;    

import javax.naming.*;    
import javax.naming.directory.Attribute;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;

import org.apache.log4j.Logger;

import com.alks.service.config.MessageUtils;

public class ADGroupsServiceImpl {
	
	private static Logger logger = Logger.getLogger(ADGroupsServiceImpl.class);

    public static final String DISTINGUISHED_NAME = MessageUtils.getMessage("ldap.service.conf.dn");
    public static final String CN = MessageUtils.getMessage("ldap.service.conf.cn");
    public static final String MEMBER = MessageUtils.getMessage("ldap.service.conf.member");
    public static final String MEMBER_OF = MessageUtils.getMessage("ldap.service.conf.memberOf");
    public static final String MAIL = MessageUtils.getMessage("ldap.service.conf.mail");
    public static final String SAM = MessageUtils.getMessage("ldap.service.conf.sam");
    public static final String SEARCH_GROUP_BY_GROUP_CN = MessageUtils.getMessage("ldap.service.conf.search_group");
    

    /**
     * Prepares and returns CN that can be used for AD query
     * e.g. Converts "CN=**Dev - Test Group" to "**Dev - Test Group"
     * Converts CN=**Dev - Test Group,OU=Distribution Lists,DC=DOMAIN,DC=com to "**Dev - Test Group"
     * 
     * @param cnName
     * @return CN that can be used for AD query
     */
    public static String getCN(String cnName) {
    	int position=0;
        if (cnName != null && cnName.toUpperCase().startsWith("CN=")) {
            cnName = cnName.substring(3);
            position = cnName.indexOf(',');
        }
       
        if (position == -1) {
            return cnName;
        } else {
            return cnName.substring(0, position);
        }
    }
    
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
        env.put(Context.PROVIDER_URL, MessageUtils.getMessage("ldap.service.url"));
        env.put(Context.SECURITY_AUTHENTICATION, "simple");
        env.put(Context.SECURITY_PRINCIPAL, username);
        env.put(Context.SECURITY_CREDENTIALS, password);
        DirContext ctx = null;
        String defaultSearchBase = MessageUtils.getMessage("ldap.service.conf.search_base");

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
            
//            String groupCN = getCN(groupDistinguishedName);
            
//            HashMap<String,String> processedUserGroups = new HashMap<String,String>();
//            HashMap<String,String> unProcessedUserGroups = new HashMap<String,String>();

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
            
//            for(int i =0 ; i<awsList.size();i++){
//            	logger.debug("MemberOf:"+ awsList.get(i));
//            }
            
//            
//            if (memberOf != null) {
//                for ( Enumeration e1 = memberOf.getAll() ; e1.hasMoreElements() ; ) {
//                    String unprocessedGroupDN = e1.nextElement().toString();
//                    String unprocessedGroupCN = getCN(unprocessedGroupDN);
//                    // Quick check for direct membership
//                    if (isSame (groupCN, unprocessedGroupCN) && isSame (groupDistinguishedName, unprocessedGroupDN)) {
//                        logger.info(username + " is authorized.");
//                        return true;
//                    } else {
//                        unProcessedUserGroups.put(unprocessedGroupDN, unprocessedGroupCN);
//                    }
//                }
//                if (userMemberOf(ctx, defaultSearchBase, processedUserGroups, unProcessedUserGroups, groupCN, groupDistinguishedName)) {
//                    logger.info(username + " is authorized.");
//                    return true;
//                }
//            }

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
     * Checks whether a user is a member of a group
     * 
     * @param ctx
     * @param searchBase
     * @param processedUserGroups
     * @param unProcessedUserGroups
     * @param groupCN
     * @param groupDistinguishedName
     * @return whether or not the user is a member
     * @throws NamingException
     */
    public static boolean userMemberOf(DirContext ctx, String searchBase, HashMap<String,String> processedUserGroups, HashMap<String,String> unProcessedUserGroups, String groupCN, String groupDistinguishedName) throws NamingException {
        HashMap<String,String> newUnProcessedGroups = new HashMap<String,String>();
        for (Iterator<String> entry = unProcessedUserGroups.keySet().iterator(); entry.hasNext();) {
            String  unprocessedGroupDistinguishedName = (String) entry.next();
            String unprocessedGroupCN = (String)unProcessedUserGroups.get(unprocessedGroupDistinguishedName);
            if ( processedUserGroups.get(unprocessedGroupDistinguishedName) != null) {
                logger.debug("Found  : " + unprocessedGroupDistinguishedName +" in processedGroups. skipping further processing of it..." );
                // We already traversed this.
                continue;
            }
            if (isSame (groupCN, unprocessedGroupCN) && isSame (groupDistinguishedName, unprocessedGroupDistinguishedName)) {
            	logger.debug("Found Match DistinguishedName : " + unprocessedGroupDistinguishedName +", CN : " + unprocessedGroupCN );
                return true;
            }
        }

        for (Iterator<String> entry = unProcessedUserGroups.keySet().iterator(); entry.hasNext();) {
            String  unprocessedGroupDistinguishedName = (String) entry.next();
            String unprocessedGroupCN = (String)unProcessedUserGroups.get(unprocessedGroupDistinguishedName);

            processedUserGroups.put(unprocessedGroupDistinguishedName, unprocessedGroupCN);

            logger.debug("Search Base: "+searchBase);
            logger.debug("UnprocessedGroupCN: "+unprocessedGroupCN);
            logger.debug("");
            logger.debug("");
                      
            // Fetch Groups in unprocessedGroupCN and put them in newUnProcessedGroups
            NamingEnumeration ns = executeSearch(ctx, SearchControls.SUBTREE_SCOPE, searchBase,
                    MessageFormat.format( SEARCH_GROUP_BY_GROUP_CN, new Object[] {unprocessedGroupCN}),
                    new String[] {CN, DISTINGUISHED_NAME, MEMBER_OF});

            // Loop through the search results
            while (ns.hasMoreElements()) {
                SearchResult sr = (SearchResult) ns.next();

                // Make sure we're looking at correct distinguishedName, because we're querying by CN
                String userDistinguishedName = sr.getAttributes().get(DISTINGUISHED_NAME).get().toString();
                if (!isSame(unprocessedGroupDistinguishedName, userDistinguishedName)) {
                	logger.debug("Processing CN : " + unprocessedGroupCN + ", DN : " + unprocessedGroupDistinguishedName +", Got DN : " + userDistinguishedName +", Ignoring...");
                    continue;
                }

                logger.debug("Processing for memberOf CN : " + unprocessedGroupCN + ", DN : " + unprocessedGroupDistinguishedName);
                // Look for and process memberOf
                Attribute memberOf = sr.getAttributes().get(MEMBER_OF);
                if (memberOf != null) {
                    for ( Enumeration e1 = memberOf.getAll() ; e1.hasMoreElements() ; ) {
                        String unprocessedChildGroupDN = e1.nextElement().toString();
                        String unprocessedChildGroupCN = getCN(unprocessedChildGroupDN);
                        logger.debug("Adding to List of un-processed groups : " + unprocessedChildGroupDN +", CN : " + unprocessedChildGroupCN);
                        newUnProcessedGroups.put(unprocessedChildGroupDN, unprocessedChildGroupCN);
                    }
                }
            }
        }
        if (newUnProcessedGroups.size() == 0) {
        	logger.debug("newUnProcessedGroups.size() is 0. returning false...");
            return false;
        }

        //  process unProcessedUserGroups
        return userMemberOf(ctx, searchBase, processedUserGroups, newUnProcessedGroups, groupCN, groupDistinguishedName);
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
    public static List<String> getGroupsByServiceAccount() {
    	
    	String username = MessageUtils.getMessage("ldap.service.account.username");
      	String password = MessageUtils.getMessage("ldap.service.account.password");
        List<String> awsAllGroups = null;  	
    	
    	logger.debug("Starting.....");
    	if(username==null || password==null){
    		return new ArrayList<String>(); 
    	}
        Hashtable<String,String> env = new Hashtable<String,String>();
        env.put(Context.INITIAL_CONTEXT_FACTORY,"com.sun.jndi.ldap.LdapCtxFactory");
        env.put(Context.PROVIDER_URL, MessageUtils.getMessage("ldap.service.url"));
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