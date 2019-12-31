package com.cloudinte.common.utils;

import com.thinkgem.jeesite.common.utils.StringUtils;

public class ProvinceUtils {

	public static String toShortName(String longName) {
		if (StringUtils.isBlank(longName)) {
			return longName;
		}

		//省市
		if (longName.endsWith("市") || longName.endsWith("省") || longName.endsWith("区")) {
			return longName.substring(0, longName.length() - 1);
		}
		//自治区
		if ("广西壮族自治区".equals(longName)) {
			return "广西";
		}
		if ("宁夏回族自治区".equals(longName)) {
			return "宁夏";
		}
		if ("新疆维吾尔自治区".equals(longName)) {
			return "新疆";
		}
		if ("内蒙古自治区".equals(longName)) {
			return "内蒙古";
		}
		if ("西藏自治区".equals(longName)) {
			return "西藏";
		}
		//特别行政区
		if ("香港特别行政区".equals(longName)) {
			return "香港";
		}
		if ("澳门特别行政区".equals(longName)) {
			return "澳门";
		}

		//
		return longName;
	}

	public static void main(String[] args) {
		System.out.println(toShortName("黑龙江"));
	}
}
