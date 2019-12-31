package com.cloudinte.zhaosheng.modules.common.utils;


public class DataTypeUtils {
	
	public static Boolean isNumer(String str)
	{
	  final String number = "0123456789";
	  for(int i = 0;i < number.length(); i ++)
	  {
		  if(i < str.length()){
			  if(number.indexOf(str.charAt(i)) == -1){
				  return false;
			  }
		  }else {
			  break;
		  }
	  }  
	  return true;
	}
	
	public static boolean  isDouble(String str)
	{
	   try
	   {
	      Double.parseDouble(str);
	      return true;
	   }
	   catch(NumberFormatException ex){}
	   return false;
	}
		
}
