package com.thinkgem.jeesite.common.db;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * @author 廖水平
 */
public class DynamicDataSource extends AbstractRoutingDataSource {

	private static final ThreadLocal<String> contextHolder = new ThreadLocal<String>();

	@Override
	protected Object determineCurrentLookupKey() {
		return contextHolder.get();
	}

	public static String getCurrentLookupKey() {
		return contextHolder.get();
	}

	public static void setCurrentLookupKey(String currentLookupKey) {
		contextHolder.set(currentLookupKey);
	}
}
