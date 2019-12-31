/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.common.filter;

import java.io.IOException;
import java.util.zip.DataFormatException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.thinkgem.jeesite.common.utils.CacheUtils;
import com.thinkgem.jeesite.common.utils.UserAgentUtils;

import net.sf.ehcache.CacheManager;
import net.sf.ehcache.constructs.web.PageInfo;
import net.sf.ehcache.constructs.web.ResponseHeadersNotModifiableException;
import net.sf.ehcache.constructs.web.filter.SimplePageCachingFilter;

/**
 * 页面高速缓存过滤器
 *
 * @author ThinkGem
 * @version 2013-8-5
 */
public class PageCachingFilter extends SimplePageCachingFilter {

	protected Logger logger = LoggerFactory.getLogger(getClass());

	@Override
	protected CacheManager getCacheManager() {
		return CacheUtils.getCacheManager();
	}

	/**
	 * 程序里面不进行Gzip压缩，让Tomcat统一来压缩。<br/>
	 * 测试时发现，程序里面压缩，直接返回给火狐浏览器，无法识别，提示“内容编码错误 无法显示您尝试查看的页面，因为它使用了无效或者不支持的压缩格式。”
	 */
	@Override
	protected boolean acceptsGzipEncoding(HttpServletRequest request) {
		//boolean acceptsGzipEncoding = super.acceptsGzipEncoding(request);
		//logger.debug("acceptsGzipEncoding---->{}", acceptsGzipEncoding);
		//return acceptsGzipEncoding;

		// 程序不压缩
		return false;
	}

	@Override
	protected String calculateKey(HttpServletRequest httpRequest) {
		String calculateKey = super.calculateKey(httpRequest) + UserAgentUtils.isMobileOrTablet(httpRequest); // 手机版和PC版本不同的页面内容
		logger.debug("calculateKey---->{}", calculateKey);
		return calculateKey;
	}

	@Override
	protected void writeResponse(HttpServletRequest request, HttpServletResponse response, PageInfo pageInfo) throws IOException, DataFormatException, ResponseHeadersNotModifiableException {
		//logger.debug(" pageInfo.getGzippedBody() ---->{}", new String(pageInfo.getGzippedBody()));
		//logger.debug(" pageInfo.getUngzippedBody() ---->{}", new String(pageInfo.getUngzippedBody()));
		super.writeResponse(request, response, pageInfo);
	}
}
