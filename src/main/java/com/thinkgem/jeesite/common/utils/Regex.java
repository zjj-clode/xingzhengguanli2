package com.thinkgem.jeesite.common.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Regex {


	   public static  boolean startCheck(String reg,String string)  
	    {  
	        boolean tem=false;  
	          
	        Pattern pattern = Pattern.compile(reg);  
	        Matcher matcher=pattern.matcher(string);  
	          
	        tem=matcher.matches();  
	        return tem;  
	    }  
	    
	 
	/**
	 * 判断是否是正确的用户名
	 * @param username
	 * @param min
	 * @param max
	 * @return
	 */
	public static boolean  checkUsername(String username,int min,int max)  
    {  
        String regex="[\\w]{"+min+","+max+"}";  
        return startCheck(regex,username);  
    }  
	
	
	public static boolean  checkUserPwd(String userpwd,int min,int max)  
    {  
        String regex="[\\w]{"+min+","+max+"}";  
        return startCheck(regex,userpwd);  
    }  
}
