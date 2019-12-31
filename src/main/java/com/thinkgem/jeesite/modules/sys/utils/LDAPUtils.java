package com.thinkgem.jeesite.modules.sys.utils;

import java.util.Hashtable;

import javax.naming.AuthenticationException;
import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.Control;
import javax.naming.ldap.InitialLdapContext;
import javax.naming.ldap.LdapContext;

import com.thinkgem.jeesite.common.utils.StringUtils;

public class LDAPUtils {

	private static String URL = "ldap://202.204.208.175:389/";
    private static String BASEDN = "ou=people,dc=cnu,dc=edu,dc=cn";
    private static String FACTORY = "com.sun.jndi.ldap.LdapCtxFactory";
    private static LdapContext  ctx = null;
	private static Hashtable<String, String> env = null;
    private static Control[] connCtls = null;
    		
	private static void LDAP_connect(String ID,String passwd){
        
		env = new Hashtable<String, String>();
        env.put(Context.INITIAL_CONTEXT_FACTORY,FACTORY);   
        env.put(Context.PROVIDER_URL, URL+BASEDN);//LDAP server   
        env.put(Context.SECURITY_AUTHENTICATION, "simple");   
        env.put(Context.SECURITY_PRINCIPAL,"uid="+ID+",ou=people,dc=cnu,dc=edu,dc=cn");
        env.put(Context.SECURITY_CREDENTIALS,passwd);
      
        try{
            ctx = new InitialLdapContext(env,connCtls);
           
        }catch(javax.naming.AuthenticationException e){
            //System.out.println("Authentication faild: "+e.toString());
        }catch(Exception e){
            //System.out.println("Something wrong while authenticating: "+e.toString());
        }
    }
	
	 private static String getUserDN(String uid,String password) {
	        String userDN = "";
	        LDAP_connect(uid,password);
	        try {
	            SearchControls constraints = new SearchControls();
	            constraints.setSearchScope(SearchControls.SUBTREE_SCOPE);
	            NamingEnumeration<SearchResult> en = ctx.search("", "uid="+uid, constraints);
	
	            if(en == null){
	                //System.out.println("Have no NamingEnumeration.");
	               }
	               if(!en.hasMoreElements()){
	                //System.out.println("Have no element.");
	               }
	               
	            // maybe more than one element
	            while (en != null && en.hasMoreElements()) {
	                Object obj = en.nextElement();
	                if (obj instanceof SearchResult) {
	                    SearchResult si = (SearchResult) obj;
	                    userDN += si.getName();
	                    userDN += "," + BASEDN;
	                } else {
	                    //System.out.println(obj);
	                }
	            }
	        } catch (Exception e) {
	            //System.out.println("查找用户时产生异常。");
	            e.printStackTrace();
	        }
	  
	        return userDN;
	    }
	public  static boolean authenricate(String ID,String password){
        boolean valide = false;
        String userDN = getUserDN(ID,password);
       
        if(StringUtils.isNoneBlank(userDN))
        {
	        try {
	            ctx.addToEnvironment(Context.SECURITY_PRINCIPAL,userDN);
	            ctx.addToEnvironment(Context.SECURITY_CREDENTIALS,password);
	            ctx.reconnect(connCtls);
	            //System.out.println(userDN + " is authenticated1");
	            valide = true;
	        }catch (AuthenticationException e) {
	            //System.out.println(userDN + " is not authenticated2");
	            //System.out.println(e.toString());
	            valide = false;
	        }catch (NamingException e) {
	            //System.out.println(userDN + " is not authenticated3");
	            valide = false;
	        }
        }
        return valide;
    }
}
