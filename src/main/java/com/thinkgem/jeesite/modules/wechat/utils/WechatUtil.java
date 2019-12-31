package com.thinkgem.jeesite.modules.wechat.utils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.UnsupportedCharsetException;
import java.security.MessageDigest;
import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sword.lang.HttpUtils;
import org.sword.wechat4j.token.TokenProxy;

import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.utils.PropertiesLoader;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.modules.sys.utils.SettingsUtils;

import net.sf.json.JSONObject;

public class WechatUtil {
	private static Logger logger = LoggerFactory.getLogger(WechatUtil.class);

	private static final String restfulURL = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=";

	private WechatUtil() {

	}

	/**
	 *
	 * @param openid
	 *            关注者ID
	 * @param template_id
	 *            模板ID
	 * @param url
	 *            点击消息后跳转的地址
	 * @param topcolor
	 *            顶部颜色
	 * @param data
	 *            json 里面包含value, color等信息 例如"User": {"value":"黄先生","color":"#173177"},"Date":{"value" :"06月07日
	 *            19时24分","color":"#173177"},...传入数据需要与模板消息的占位符相同 json部分可用map,例如maps.put("user", new
	 *            DTO("value值","颜色"));然后统一转换为json传入
	 * @return 生成json字符串
	 * @throws Exception
	 */
	public static String getTemplateMsg(String openid, String template_id, String url, String topcolor, String data) throws Exception {
		try {
			if (openid == null || "".equals(openid)) {
				throw new Exception("openId不能为空");
			}
			if (template_id == null || "".equals(template_id)) {
				throw new Exception("template_id不能为空");
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		if (url == null || "".equals(url)) {
			url = "";
		}
		if (topcolor == null || "".equals(topcolor)) {
			topcolor = "#FF0000";
		}
		String json = "{\"touser\":\"" + openid + "\",\"template_id\":\"" + template_id + "\",\"url\":\"" + url + "\",\"topcolor\":\"#FF0000\"";
		if (data != null && !"".equals(data)) {
			json += ",\"data\":" + data + "}";
		}
		return json;
	}

	/**
	 *
	 * @param openid
	 *            关注者ID
	 * @param template_id
	 *            模板ID
	 * @param url
	 *            点击模板消息后将要跳转的地址
	 * @param topcolor
	 *            顶部颜色
	 * @param data
	 *            json 里面包含value, color等信息 例如"User": {"value":"黄先生","color":"#173177"},"Date":{"value" :"06月07日
	 *            19时24分","color":"#173177"},...传入数据需要与模板消息的占位符相同 json部分可用map,例如maps.put("user", new
	 *            DTO("value值","颜色"));然后统一转换为json传入
	 * @return 响应码
	 * @throws Exception
	 */
	public static String sendTemplateMsg(String openid, String template_id, String url, String topcolor, String data) throws Exception {
		CloseableHttpClient httpClient = null;
		CloseableHttpResponse httpResponse = null;
		try {
			String token = TokenProxy.accessToken();
			logger.debug("token={}", token);
			String json = getTemplateMsg(openid, template_id, url, topcolor, data);
			logger.debug("发送模板消息 ，json ==> " + json);
			httpClient = HttpClients.createDefault();
			HttpPost httpPost = new HttpPost(restfulURL + token);
			StringEntity entity = new StringEntity(json, "utf-8");
			entity.setContentType("application/x-www-form-urlencoded");
			httpPost.setEntity(entity);
			httpResponse = httpClient.execute(httpPost);
			HttpEntity entity1 = httpResponse.getEntity();
			return entity1 != null ? EntityUtils.toString(entity1) : null;
		} catch (UnsupportedCharsetException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
			throw new Exception("网络故障");
		} finally {
			try {
				if (httpResponse != null) {
					httpResponse.close();
				}
				if (httpClient != null) {
					httpClient.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	/**
	 *
	 * @param json
	 *            封装好的json数据,此方法适合本地调用
	 * @return 响应码
	 */
	public static String sendTemplateMsg(String json) {
		try {
			String token = TokenProxy.accessToken();
			HttpClient httpClient = HttpClients.createDefault();
			HttpPost httpPost = new HttpPost(restfulURL + token);
			StringEntity entity = new StringEntity(json, "utf-8");
			entity.setContentType("application/x-www-form-urlencoded");
			httpPost.setEntity(entity);
			HttpResponse httpResponse = httpClient.execute(httpPost);
			HttpEntity entity1 = httpResponse.getEntity();
			return entity1 != null ? EntityUtils.toString(entity1) : null;
		} catch (UnsupportedCharsetException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 通过code获取openid
	 *
	 * @param code
	 * @return
	 */
	public static String getOpenid(String code) {
		String openid = "";
		String result = HttpUtils
				.get("https://api.weixin.qq.com/sns/oauth2/access_token?appid=" + getWexinAppid() + "&secret=" + getWexinSecret() + "&code=" + code + "&grant_type=authorization_code");
		logger.error(result);
		JSONObject jobj = JSONObject.fromObject(result);
		if (jobj.containsKey("openid")) {
			openid = jobj.getString("openid");
		}
		return openid;
	}
	
	/**
	 * 获取access_token
	 * */
	public static String accessToken(HttpSession session) {
	    String appid = getWexinAppid();
	    String secret = getWexinSecret();
	    String accessToken = (String) session.getAttribute("accessToken");
	    if (StringUtils.isEmpty(accessToken)) {
	        String result = HttpUtils.get("https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=" + appid + "&secret=" + secret);
            logger.error(result);
            JSONObject jobj = JSONObject.fromObject(result);
            if (jobj.containsKey("access_token")) {
                accessToken = jobj.getString("access_token");
            }
            session.setAttribute("access_token", accessToken);
        }
	    return accessToken;
	}

	/**
	 * 判断是否是微信请求
	 */
	public static boolean isWeixinLogin(HttpServletRequest request) {
		String useragent = request.getHeader("User-Agent");
		//logger.debug("User-Agent ---> {}", useragent);
		if (useragent.indexOf("MicroMessenger") != -1) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 通过appid构造url获取微信回传code值.<br/>
	 * 微信是否弹出授权页面url的区别只在一个参数scope，不弹出微信授权页面：scope=snsapi_base，弹出微信授权页面：scope=snsapi_userinfo
	 *
	 * @param gotourl
	 *            redirect_uri跳转url里面可以携带一个参数
	 */
	public static String getWeixinAuthonStr(String gotourl) {//http://cloudinte.com/f/weixin
		if (gotourl == null) {
			gotourl = "";
		}
		// http://202.205.92.137/a/login?url=
		String url = SettingsUtils.getSysConfig("weixinurl") + "?url=" + gotourl;
		try {
			url = URLEncoder.encode(url, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		// https://open.weixin.qq.com/connect/oauth2/authorize?appid=wxff4f4d2be0afbf95&redirect_uri=http%3A%2F%2Fwww.cloudinte.com%2Fa%2Flogin%3Furl%3D&response_type=code&scope=snsapi_userinfo&state=STATE#wechat_redirect
		String redirecturl = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=" + getWexinAppid() + "&redirect_uri=" + url
				+ "&response_type=code&scope=snsapi_userinfo&state=STATE#wechat_redirect";
		return redirecturl;
	}

	/**
	 * 生成微信跳转码code
	 *
	 * @param gotourl
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static String getQyWeixinAuthonStr(String gotourl) throws UnsupportedEncodingException {
		// TODO Auto-generated method stub
		String appid = Global.getConfig("qyweixinappid");
		if (gotourl == null) {
			gotourl = "";
		}
		String url = URLEncoder.encode(SettingsUtils.getSysConfig("weixinurl") + "?url=" + gotourl, "UTF-8");
		String redirecturl = "https://open.weixin.qq.com/connect/oauth2/authorize?" + "appid=" + appid + "&redirect_uri=" + url + "" + "&response_type=code&" + "scope=snsapi_userinfo&"
				+ "state=STATE#wechat_redirect";
		return redirecturl;
	}

	/**
	 * 通过openid、access token获取用户信息
	 */
	public static String getQyOpenid(String code) {

		String openid = "";
		String token = Global.getConfig("qyweixintoken");
		String result = HttpUtils.get("https://qyapi.weixin.qq.com/cgi-bin/user/getuserinfo?" + "access_token=" + token + "&code=" + code);

		logger.error(result);
		JSONObject jobj = JSONObject.fromObject(result);

		if (jobj.containsKey("openid")) {
			openid = jobj.getString("openid");
		}
		return openid;
	}

	/**
	 * 判断是否是微信请求。通过请求头User-Agent判断。
	 */
	public static boolean isWeixinRequest(HttpServletRequest request) {
		String userAgent = request.getHeader("User-Agent");
		if (!StringUtils.isBlank(userAgent) && userAgent.indexOf("MicroMessenger") != -1) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * 验证signature
	 * */
    public static boolean checkSignature(String signature, String timestamp, String nonce) {
        String token = getWexinToken();
        String[] arr = new String[] { token, timestamp, nonce };
        Arrays.sort(arr); // 生成字符串 StringBuffer content = new StringBuffer();
        StringBuffer content = new StringBuffer();
        for (int i = 0; i < arr.length; i++) {
            content.append(arr[i]);
        }
        String temp = getSha1(content.toString());//sha1加密
        return temp.equals(signature);
    }
	
	/**
	 * sha1加密
	 * */
    public static String getSha1(String str) {
        if (str == null || str.length() == 0) {
            return null;
        }
        char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
        try {
            MessageDigest mdTemp = MessageDigest.getInstance("SHA1");
            mdTemp.update(str.getBytes("UTF-8"));
            byte[] md = mdTemp.digest();
            int j = md.length;
            char buf[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                buf[k++] = hexDigits[byte0 >>> 4 & 0xf];
                buf[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(buf);
        } catch (Exception e) {
            return null;
        }
    }

	////////////////////////////////
	private static PropertiesLoader loader = new PropertiesLoader("wechat4j.properties");

	/**
	 * 获取微信appid
	 *
	 * @return
	 */
	public static String getWexinAppid() {
		return loader.getProperty("wechat.appid");
	}

	/**
	 * 获取微信密钥
	 *
	 * @return
	 */
	public static String getWexinSecret() {
		return loader.getProperty("wechat.appsecret");
	}
	
	/**
	 * 获取回转地址
	 *
	 * @return
	 */
	public static String getWexinUrl() {
	    return loader.getProperty("wechat.url");
	}
	
	/**
	 * 获取微信密钥
	 *
	 * @return
	 */
	public static String getWexinToken() {
		return loader.getProperty("wechat.token");
	}

}
