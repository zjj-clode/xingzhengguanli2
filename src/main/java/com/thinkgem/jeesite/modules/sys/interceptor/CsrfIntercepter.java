package com.thinkgem.jeesite.modules.sys.interceptor;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

public class CsrfIntercepter  implements HandlerInterceptor {

	public  static final String CSRFNUMBER = "csrftoken";
    public boolean preHandle(HttpServletRequest request,HttpServletResponse response, Object handler) throws Exception {
    
        String keyFromRequestParam = (String) request.getParameter(CSRFNUMBER);
        
        //System.out.println(keyFromRequestParam);
        boolean result=false;
        
        /*
        String keyFromCookies=""; 
        boolean result=false;
        Cookie[] cookies = request.getCookies(); 
        if(cookies!=null){
            for (int i = 0; i < cookies.length; i++) {    
                String name = cookies[i].getName(); 
                if(CSRFNUMBER.equals(name) ) {    
                    keyFromCookies= cookies[i].getValue();    
                }    
            }
        }
    
        if((keyFromRequestParam!=null && keyFromRequestParam.length()>0 && 
                keyFromRequestParam.equals(keyFromCookies) &&
                keyFromRequestParam.equals((String)request.getSession().getAttribute(CSRFNUMBER)))) { 
            result=true;
        }else{
            request.getRequestDispatcher("/error/400").forward(request, response);
        }
        */
        return result;
    }
    
    public void afterCompletion(HttpServletRequest arg0, HttpServletResponse arg1,
            Object arg2, Exception arg3) throws Exception {
        
    }
    
    public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1,
            Object arg2, ModelAndView arg3) throws Exception {
    }
}
