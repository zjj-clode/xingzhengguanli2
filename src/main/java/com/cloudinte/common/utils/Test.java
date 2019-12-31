package com.cloudinte.common.utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.collections.MapUtils;
import org.apache.http.HeaderIterator;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.thinkgem.jeesite.common.utils.StringUtils;

public class Test {

	private static Logger logger = LoggerFactory.getLogger(Test.class);

	private static String getContinentLabelFromNet(String name) throws Exception {
		CloseableHttpClient httpClient = null;
		CloseableHttpResponse response = null;
		try {
			String host = "https://baike.baidu.com";
			String url = host + "/item/" + URLEncoder.encode(name, "utf-8");
			logger.debug("url --------->  {}", url);
			httpClient = HttpsUtils.getHttpClient();
			HttpGet httpGet = new HttpGet(url);
			response = httpClient.execute(httpGet);
			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode == 302) {
				url = host + response.getHeaders("Location");
				logger.debug("302 url --------->  {}", url);

				response.close();

				httpGet = new HttpGet(url);
				response = httpClient.execute(httpGet);
				statusCode = response.getStatusLine().getStatusCode();
			}
			logger.debug("statusCode --------->  {}", statusCode);
			if (statusCode == 200) {
				String html = EntityUtils.toString(response.getEntity(), "UTF-8");
				//logger.debug("html --------->  {}", html);
				Document document = Jsoup.parse(html);
				Elements elements = document.select("div.basic-info dl.basicInfo-left");
				if (elements.size() > 0) {
					Element basicInfo_left = elements.get(0);
					logger.debug("basicInfo_left ---------> \n {}", basicInfo_left.html());

					Elements children = basicInfo_left.children();
					for (int i = 0; i < children.size(); i++) {
						Element child = children.get(i);
						if ("所属洲".equals(child.text().trim())) {
							return children.get(i + 1).text().trim();
						}
					}
				}
			}
			return null;

		} finally {
			if (httpClient != null) {
				httpClient.close();
			}
			if (response != null) {
				response.close();
			}
		}
	}

	/**
	 * 从“<a href="https://www.yidaiyilu.gov.cn/info/iList.jsp?cat_id=10037">中国一带一路网</a>”抓取一带一路国家名称
	 * 
	 * @throws Exception
	 */
	private Set<String> getYdylgj() throws Exception {
		Set<String> ydylgjList = Sets.newHashSet();
		CloseableHttpClient closeableHttpClient = null;
		try {
			closeableHttpClient = HttpsUtils.getHttpClient();

			String url = "https://www.yidaiyilu.gov.cn/info/iList.jsp?cat_id=10037";

			Map<String, String> header = new HashMap<>();
			header.put("Host", "www.yidaiyilu.gov.cn");
			header.put("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:62.0) Gecko/20100101 Firefox/62.0");
			header.put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
			header.put("Referer", "https://www.yidaiyilu.gov.cn/info/iList.jsp?tm_id=513");
			// 网站使用了加速乐的反爬虫机制，此处的Cookie从浏览器中copy过来用一下
			header.put("Cookie", "__jsluid=159876d6cc31a274c441e54d44a9d53e; __jsl_clearance=1555993613.213|0|7jJrdr%2BjlFqVuEVMs39wOtrt6VY%3D");
			header.put("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:62.0) Gecko/20100101 Firefox/62.0");

			String html = HttpsUtils.get(closeableHttpClient, url, header);
			logger.debug("=--------------------->\n {}", html);

			// 分5页
			for (int i = 1; i <= 5; i++) {
				url = "https://www.yidaiyilu.gov.cn/info/iList.jsp?cat_id=10037&cur_page=" + i;

				html = HttpsUtils.get(closeableHttpClient, url, header);
				logger.debug("=--------------------->\n {}", html);

				Document document = Jsoup.parse(html);

				Elements lis = document.select("div.list_right ul.commonList_dot li");
				for (Element li : lis) {
					String text = li.text().trim();
					logger.debug("text ---------------------> {}", text);
					ydylgjList.add(text);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			if (closeableHttpClient != null) {
				try {
					closeableHttpClient.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return ydylgjList;
	}

	public static class HttpsUtils {

		private static Logger logger = LoggerFactory.getLogger(HttpsUtils.class);

		private static PoolingHttpClientConnectionManager poolingHttpClientConnectionManager = null;
		static {
			try {
				Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create().register("http", new PlainConnectionSocketFactory()).build();
				poolingHttpClientConnectionManager = new PoolingHttpClientConnectionManager(registry);
				poolingHttpClientConnectionManager.setMaxTotal(200);//max connection
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		public static String post(CloseableHttpClient httpClient, String url, Map<String, String> headerMap, Map<String, String> paramsMap) throws Exception {
			logger.debug("post请求 -----------> {}", url);
			CloseableHttpResponse httpResponse = null;
			String result = "";
			try {
				HttpPost httpPost = new HttpPost(url);

				// 设置HTTP头信息
				if (MapUtils.isNotEmpty(headerMap)) {
					for (Map.Entry<String, String> entry : headerMap.entrySet()) {
						httpPost.addHeader(entry.getKey(), entry.getValue());
					}
				}

				// 设置请求参数
				if (MapUtils.isNotEmpty(paramsMap)) {
					List<NameValuePair> nameValuePairs = Lists.newArrayList();
					for (Entry<String, String> entry : paramsMap.entrySet()) {
						nameValuePairs.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
					}
					httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "utf-8"));
				}

				httpResponse = httpClient.execute(httpPost);

				int statusCode = httpResponse.getStatusLine().getStatusCode();
				logger.debug("statusCode ---------> {}", statusCode);

				//
				if (statusCode == HttpStatus.SC_OK) {
					HttpEntity resEntity = httpResponse.getEntity();
					result = EntityUtils.toString(resEntity);
				} else {
					readHttpResponse(httpResponse);
				}

			} catch (Exception e) {
				e.printStackTrace();
				throw e;
			} finally {
				if (httpResponse != null) {
					httpResponse.close();
				}
			}
			return result;
		}

		public static String get(CloseableHttpClient httpClient, String url, Map<String, String> headerMap) throws Exception {
			logger.debug("get请求 -----------> {}", url);
			CloseableHttpResponse httpResponse = null;
			String result = "";
			try {
				HttpGet httpGet = new HttpGet(url);

				// 设置头信息
				if (MapUtils.isNotEmpty(headerMap)) {
					for (Map.Entry<String, String> entry : headerMap.entrySet()) {
						httpGet.addHeader(entry.getKey(), entry.getValue());
					}
				}
				httpResponse = httpClient.execute(httpGet);

				int statusCode = httpResponse.getStatusLine().getStatusCode();
				logger.debug("statusCode ---------> {}", statusCode);

				//
				if (statusCode == HttpStatus.SC_OK) {
					HttpEntity resEntity = httpResponse.getEntity();
					result = EntityUtils.toString(resEntity);
				} else if (statusCode == HttpStatus.SC_MOVED_TEMPORARILY || statusCode == HttpStatus.SC_MOVED_PERMANENTLY) {
					String location = httpResponse.getFirstHeader("Location").getValue();
					if (!location.startsWith("http")) {
						location += "http://xdf.wmzy.com"; // /api/school/bvp5la.html
					}

					return get(httpClient, location, headerMap);

				} else {
					readHttpResponse(httpResponse);
				}

			} catch (Exception e) {
				e.printStackTrace();
				throw e;
			} finally {
				if (httpResponse != null) {
					httpResponse.close();
				}
			}
			return result;
		}

		public static CloseableHttpClient getHttpClient() throws Exception {
			return HttpClients.custom().setConnectionManager(poolingHttpClientConnectionManager).build();
		}

		private static String readHttpResponse(HttpResponse httpResponse) throws ParseException, IOException {
			StringBuilder builder = new StringBuilder();
			// 获取响应消息实体
			HttpEntity entity = httpResponse.getEntity();
			// 响应状态
			builder.append("status:" + httpResponse.getStatusLine());
			builder.append("headers:");
			HeaderIterator iterator = httpResponse.headerIterator();
			while (iterator.hasNext()) {
				builder.append("\t" + iterator.next());
			}
			// 判断响应实体是否为空
			if (entity != null) {
				String responseString = EntityUtils.toString(entity);
				builder.append("response length:" + responseString.length());
				builder.append("response content:" + responseString.replace("\r\n", ""));
			}
			return builder.toString();
		}
	}

	public static void main(String[] args) throws Exception {

		/*
		http://xdf.wmzy.com/account/ajaxLogin?_=1556854151325
		account	18810953720
		forceBindeCard	false
		password	62738000
		
		{"msg":"登录成功","code":0,"isVip":true,"cardPrivilege":1}
		
		Set-Cookie: token=0BXCvbY3kkpMU2viflY8urLxot2FAcd9KBJjSCbT966m9xs5zWB1SjF96jLOFyibz9cyRSu3_xcWWOAGgjL6qa==; path=/; expires=Wed, 30 Oct 2019 03:29:26 GMT; domain=wmzy.com; httponly
		Set-Cookie: login=login; path=/; expires=Fri, 03 May 2019 03:29:56 GMT; domain=wmzy.com
		
		 */

		//
		CloseableHttpClient httpClient = HttpsUtils.getHttpClient();

		Map<String, String> headerMap = new HashMap<>();
		headerMap.put("Host", "xdf.wmzy.com");
		headerMap.put("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/69.0.3497.100 Safari/537.36");
		headerMap.put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
		headerMap.put("Referer", "http://xdf.wmzy.com/");
		headerMap.put("X-Requested-With", "XMLHttpRequest");

		Map<String, String> paramsMap = new HashMap<>();
		paramsMap.put("account", "18810953720");
		paramsMap.put("forceBindeCard", "false");
		paramsMap.put("password", "62738000");

		String content = HttpsUtils.post(httpClient, "http://xdf.wmzy.com/account/ajaxLogin?_=" + new Date().getTime(), headerMap, paramsMap);

		//logger.debug("content --------------> {}", content);

		JSONObject jsonObject = JSONObject.parseObject(content);
		logger.debug("jsonObject --------------> {}", jsonObject);

		// http://xdf.wmzy.com/api/school?home_tool
		content = HttpsUtils.get(httpClient, "http://xdf.wmzy.com/api/school?home_tool", headerMap);
		//logger.debug("content --------------> {}", content);
		writeLog(content);

		// 反调试代码  (function() {var a = new Date(); debugger; return new Date() - a > 100;}())

		// 学校列表，不限学校地区、学校类型、学校级别、著名学校
		// http://xdf.wmzy.com/api/school/getSchList?prov_filter=00&type_filter=0&diploma_filter=0&flag_filter=0&page=2&page_len=20&_=1556856180490

		content = HttpsUtils.get(httpClient, "http://xdf.wmzy.com/api/school/getSchList?prov_filter=00&type_filter=0&diploma_filter=0&flag_filter=0&page=2&page_len=20&_=" + new Date().getTime(), headerMap);
		//logger.debug("content --------------> {}", content);
		writeLog(content);

		// 点击学校查录取分数 http://xdf.wmzy.com/api/school/52ac2e97747aec013fcf49d7.html
		content = HttpsUtils.get(httpClient, "http://xdf.wmzy.com/api/school/52ac2e97747aec013fcf49d7.html", headerMap);
		//logger.debug("content --------------> {}", content);
		writeLog(content);

		// 301 ---》 Location: /api/school/bvp5la.html  （学校概况）

		// 点击录取招生 http://xdf.wmzy.com/api/school-score/bvp5la.html
		content = HttpsUtils.get(httpClient, "http://xdf.wmzy.com/api/school-score/bvp5la.html", headerMap);
		writeLog(content);

		// 招生计划接口：  http://xdf.wmzy.com/api/school/enrollment-list?sch_id=52ac2e97747aec013fcf49d7&diploma=7&province=340000000000&ty=li&year=2018&batch=1&page_len=10&&page=1&_=1556856877242
		// 返回： {"code":0,"category":["经济学","法学","文学","理学","工学","管理学"],"yearsList":[2017,2016,2015],"listHTML":{"enrollPlan":"\n\n\n<table  width=\"100%\">\n    <tr style=\"background: #f8f8f8; border-bottom: 1px solid #e4e4e4\">\n        <th style=\"width:auto;\">专业名称</th>\n        <th>学科门类</th>\n        <th style=\"width:250px!important;\">二级门类</th>\n        <th>计划招生</th>\n    </tr>\n    \n        \n            <tr>\n                <td>\n                \n                    <a href=\"/api/school-major-score/52ac2e97747aec013fcf49d7-52aedf5b747aec1cfc84158f.html\" target=\"_blank\">国际经济与贸易</a>\n                \n                </td>\n                <td width=\"15%\">经济学</td>\n                <td >经济与贸易类</td>\n                <td width=\"15%\">3</td>\n            </tr>\n        \n            <tr>\n                <td>\n                \n                    <a href=\"/api/school-major-score/52ac2e97747aec013fcf49d7-52aedf5b747aec1cfc841591.html\" target=\"_blank\">法学</a>\n                \n                </td>\n                <td width=\"15%\">法学</td>\n                <td >法学类</td>\n                <td width=\"15%\">1</td>\n            </tr>\n        \n            <tr>\n                <td>\n                \n                    <a href=\"/api/school-major-score/52ac2e97747aec013fcf49d7-52aedf5b747aec1cfc8415c8.html\" target=\"_blank\">英语</a>\n                \n                </td>\n                <td width=\"15%\">文学</td>\n                <td >外国语言文学类</td>\n                <td width=\"15%\">1</td>\n            </tr>\n        \n            <tr>\n                <td>\n                \n                    <a href=\"/api/school-major-score/52ac2e97747aec013fcf49d7-52aedf5b747aec1cfc8415ce.html\" target=\"_blank\">日语</a>\n                \n                </td>\n                <td width=\"15%\">文学</td>\n                <td >外国语言文学类</td>\n                <td width=\"15%\">2</td>\n            </tr>\n        \n            <tr>\n                <td>\n                \n                    <a href=\"/api/school-major-score/52ac2e97747aec013fcf49d7-52aedf5b747aec1cfc84161a.html\" target=\"_blank\">化学</a>\n                \n                </td>\n                <td width=\"15%\">理学</td>\n                <td >化学类</td>\n                <td width=\"15%\">2</td>\n            </tr>\n        \n            <tr>\n                <td>\n                \n                    <a href=\"/api/school-major-score/52ac2e97747aec013fcf49d7-52aedf5b747aec1cfc84161b.html\" target=\"_blank\">应用化学</a>\n                \n                </td>\n                <td width=\"15%\">理学</td>\n                <td >化学类</td>\n                <td width=\"15%\">1</td>\n            </tr>\n        \n            <tr>\n                <td>\n                \n                    <a href=\"/api/school-major-score/52ac2e97747aec013fcf49d7-52aedf5b747aec1cfc841630.html\" target=\"_blank\">生物技术</a>\n                \n                </td>\n                <td width=\"15%\">理学</td>\n                <td >生物科学类</td>\n                <td width=\"15%\">1</td>\n            </tr>\n        \n            <tr>\n                <td>\n                \n                    <a href=\"/api/school-major-score/52ac2e97747aec013fcf49d7-52aedf5b747aec1cfc84163b.html\" target=\"_blank\">材料成型及控制工程</a>\n                \n                </td>\n                <td width=\"15%\">工学</td>\n                <td >机械类</td>\n                <td width=\"15%\">1</td>\n            </tr>\n        \n            <tr>\n                <td>\n                \n                    <a href=\"/api/school-major-score/52ac2e97747aec013fcf49d7-52aedf5b747aec1cfc84163d.html\" target=\"_blank\">工业设计</a>\n                \n                </td>\n                <td width=\"15%\">工学</td>\n                <td >机械类</td>\n                <td width=\"15%\">3</td>\n            </tr>\n        \n            <tr>\n                <td>\n                \n                    <a href=\"/api/school-major-score/52ac2e97747aec013fcf49d7-52aedf5b747aec1cfc841645.html\" target=\"_blank\">测控技术与仪器</a>\n                \n                </td>\n                <td width=\"15%\">工学</td>\n                <td >仪器类</td>\n                <td width=\"15%\">1</td>\n            </tr>\n        \n\n        \n\n    \n</table>\n\n    ","enrollPagination":"\n\n\n\t\n\n\t\n\t        <li data-page=\"1\" class=\"active\">1</li>\n\t\n\t        <li data-page=\"2\" class=\"\">2</li>\n\t\n\t        <li data-page=\"3\" class=\"\">3</li>\n\t\n\t        <li data-page=\"4\" class=\"\">4</li>\n\t\n\n\t\n\t    <li data-page=\"2\" class=\"next\">></li>\n\t\n\n"}}
		// 
		headerMap.put("Referer", "http://xdf.wmzy.com/api/school-score/bvp5la.html");
		headerMap.put("Upgrade-Insecure-Requests", "1");
		headerMap.put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");

		headerMap.put("Cookie",
				"_ga=GA1.2.309188124.1550798840; Hm_lvt_8a2f2a00c5aff9efb919ee7af23b5366=1550798840,1551146618; EkcP=8b38968e4181379368c85d6c539912a951c0b61adb7a610; Hm_lvt_4e78cec54b127fcb9b8b73c4331f5b8a=1556856052; token=0BXCvbY3kkpMU2viflY8uqS82MhL1wR6qmFyQxDAji4hk9BN0xF_SpvKP9_FubPM021t7CwmAMMPL161dTRWGq==; Hm_lpvt_4e78cec54b127fcb9b8b73c4331f5b8a=1556860658; __cEIpv_=V0QLV3fStcmjCaWTVUb8lxMD/wjOTv4gBdGblk0b99q1A39sBZag9ktltpKtv5O/xRxjRTwqd2OkeIJjIuBY/vvznA0J6KvYXH7mxiz/sjwSwbGLe/8zUVX1");

		content = HttpsUtils.get(httpClient, "http://xdf.wmzy.com/api/school/enrollment-list?sch_id=52ac2e97747aec013fcf49d7&diploma=7&province=340000000000&ty=li&year=2018&batch=1&page_len=10&&page=1&_=" + new Date().getTime(), headerMap);
		logger.debug("content --------------> {}", content);
		writeLog(content);

	}

	private static Map<String, String> createHeader(boolean ajax, String referer, String accept) {
		Map<String, String> headerMap = new HashMap<>();
		headerMap.put("Host", "xdf.wmzy.com");
		headerMap.put("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/69.0.3497.100 Safari/537.36");
		headerMap.put("Accept", StringUtils.isBlank(accept) ? "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8" : accept);
		headerMap.put("Referer", StringUtils.isBlank(referer) ? "http://xdf.wmzy.com/" : referer);
		if (ajax) {
			headerMap.put("X-Requested-With", "XMLHttpRequest");
		}
		return headerMap;
	}

	private static void writeLog(String str) {
		try {
			FileWriter fileWriter = new FileWriter(new File("C:\\Users\\pc\\Desktop\\test.log"), true);
			fileWriter.write("\n" + "\n ---------------------------------------------------------------------------------------------------------------" + "\n" + str);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}