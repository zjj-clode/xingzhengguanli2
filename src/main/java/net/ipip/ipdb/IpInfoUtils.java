package net.ipip.ipdb;

import com.alibaba.druid.support.http.util.IPAddress;
import com.thinkgem.jeesite.common.utils.StringUtils;
import org.springframework.web.util.UrlPathHelper;

import javax.servlet.http.HttpServletRequest;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * ip地址工具类
 *
 * @author gyl
 */
public class IpInfoUtils {
    // City类可用于IPDB格式的IPv4免费库
    private static City db;

    public static String getLocation(HttpServletRequest request) {
        if (request == null)
            return "";
        UrlPathHelper helper = new UrlPathHelper();
        StringBuffer buff = request.getRequestURL();
        String uri = request.getRequestURI();
        String origUri = helper.getOriginatingRequestUri(request);
        buff.replace(buff.length() - uri.length(), buff.length(), origUri);
        //获取请求参数，这里不需要
//		String queryString = helper.getOriginatingQueryString(request);
//		if (queryString != null) {
//			buff.append("?").append(queryString);
//		}
        return buff.toString();
    }

    /**
     * 获取访问者IP
     * <p>
     * 在一般情况下使用Request.getRemoteAddr()即可，但是经过nginx等反向代理软件后，这个方法会失效。
     * <p>
     * 本方法先从Header中获取X-Real-IP，如果不存在再从X-Forwarded-For获得第一个IP(用,分割)， 如果还不存在则调用Request
     * .getRemoteAddr()。
     *
     * @param request
     * @return
     */
    public static String getIpAddr(HttpServletRequest request) {
        if (request == null)
            return "";
        String ip = request.getHeader("X-Real-IP");
        if (!isIP(ip)) {
            ip = request.getHeader("X-Forwarded-For");
            // 多次反向代理后会有多个IP值，第一个为真实IP。
            if (isIP(ip)) {
                int index = ip.indexOf(',');
                if (index != -1)
                    ip = ip.substring(0, index);
            }
        }
        if (!isIP(ip))
            ip = request.getHeader("Proxy-Client-IP");
        if (!isIP(ip))
            ip = request.getHeader("WL-Proxy-Client-IP");
        if (isIP(ip) && (ip.contains("../") || ip.contains("..\\")))
            ip = request.getRemoteAddr();
        if (!isIP(ip))
            ip = request.getRemoteAddr();
        if (isIP(ip) && ip.equals("0:0:0:0:0:0:0:1"))
            ip = "127.0.0.1";
        return ip;
    }

    private static boolean isIP(String ip) {
        return !StringUtils.isBlank(ip) && !"unknown".equalsIgnoreCase(ip);
    }

    /**
     * 根据ip获取其所在国家、地区、省份、城市
     */
    public static Map<String, String> getIpInfo(String ip) {
        dbInit();
        Map<String, String> map = new HashMap<String, String>();
        if (StringUtils.isBlank(ip)) {
            return map;
        }
        try {
            new IPAddress(ip);
        } catch (Exception e) {// not ip
            return map;
        }
        try {
            // db.find(address, language) 返回索引数组
            // db.findInfo(address, language) 返回 CityInfo 对象
            // db.findMap(address, language) 返回结果Map
            map = db.findMap(ip, "CN");
        } catch (InvalidDatabaseException | IPFormatException e) {
//			System.out.println("<<<<IPDB ERROR>>>>" + e.getMessage());
            e.printStackTrace();
        }
        return map;
    }

    private static void dbInit() {
        if (db == null) {
            try {
                /*URL url = IpInfoUtils.class.getResource("ipipfree.ipdb");
                System.out.println("url==null ? " + (url == null));
                String path = url.toURI().getPath();
 				System.out.println(path);
                db = new City(path);*/

                InputStream inputStream = IpInfoUtils.class.getResourceAsStream("/net/ipip/ipdb/ipipfree.ipdb");
                db = new City(inputStream);

//				System.out.println("------------------ipDB load complete---------------------");
            } catch (Exception e) {
//				System.out.println("------------------ipDB load fail---------------------");
//				System.out.println("<<<<IPDB ERROR>>>>" + e.getMessage());
                e.printStackTrace();
            }
        }
    }


}
