package com.cloudinte.modules.xingzhengguanli.utils;

public class NumberUtils {
	
	 private static String[] units = { "", "十", "百", "千", "万", "十万", "百万", "千万", "亿",
	      "十亿", "百亿", "千亿", "万亿" };
	 private static char[] numArray = { '零', '一', '二', '三', '四', '五', '六', '七', '八', '九' };
	 
	 
	 public static String foematInteger(int num) {
	    char[] val = String.valueOf(num).toCharArray();
	    int len = val.length;
	    StringBuilder sb = new StringBuilder();
	    for (int i = 0; i < len; i++) {
	      String m = val[i] + "";
	      int n = Integer.valueOf(m);
	      boolean isZero = n == 0;
	      String unit = units[(len - 1) - i];
	      if (isZero) {
	        if ('0' == val[i - 1]) {
	          continue;
	        } else {
	          sb.append(numArray[n]);
	        }
	      } else {
	        sb.append(numArray[n]);
	        sb.append(unit);
	      }
	    }
	    return sb.toString();
	  }
		 
		  /**
		   * 对于double类型的的装换
		   * 
		   * @param decimal
		   * @return
		   */
	 public static String formatDecimal(double decimal) {
		    String decimals = String.valueOf(decimal);
		    int decIndex = decimals.indexOf(".");
		    int integ = Integer.valueOf(decimals.substring(0, decIndex));
		    int dec = Integer.valueOf(decimals.substring(decIndex + 1));
		    String result = foematInteger(integ) + "." + formatFractionalPart(dec);
		    return result;
		  }
		 
		  /**
		   * 对整数类型的转换
		   * 
		   * @param decimal
		   * @return
		   */
	 public static String formatFractionalPart(int decimal) {
		    char[] val = String.valueOf(decimal).toCharArray();
		    int len = val.length;
		    StringBuilder sb = new StringBuilder();
		    for (int i = 0; i < len; i++) {
		      int n = Integer.valueOf(val[i] + "");
		      sb.append(numArray[n]);
		    }
		    return sb.toString();
		  }
		public static void main(String[] args) {
			 int num = 1;
			    String numStr = foematInteger(num);
			    System.out.println("num= " + num + ", convert result: " + numStr);
		}

}
