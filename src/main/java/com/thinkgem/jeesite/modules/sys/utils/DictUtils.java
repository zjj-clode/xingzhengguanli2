/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.sys.utils;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.beanutils.BeanToPropertyValueTransformer;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.thinkgem.jeesite.common.utils.CacheUtils;
import com.thinkgem.jeesite.common.utils.Collections3;
import com.thinkgem.jeesite.common.utils.SpringContextHolder;
import com.thinkgem.jeesite.modules.sys.dao.DictDao;
import com.thinkgem.jeesite.modules.sys.entity.Dict;

/**
 * 字典工具类
 * 
 * @author ThinkGem
 * @version 2013-5-29
 */
public class DictUtils {

	private static DictDao dictDao = SpringContextHolder.getBean(DictDao.class);

	public static final String CACHE_DICT_MAP = "dictMap";

	public static String getDictLabel(String value, String type, String defaultValue) {
		if (StringUtils.isNotBlank(type) && StringUtils.isNotBlank(value)) {
			for (Dict dict : getDictList(type)) {
				if (type.equals(dict.getType()) && value.equals(dict.getValue())) {
					return dict.getLabel();
				}
			}
		}
		return defaultValue;
	}

	public static String getDictLabels(String values, String type, String defaultValue) {
		if (StringUtils.isNotBlank(type) && StringUtils.isNotBlank(values)) {
			List<String> valueList = Lists.newArrayList();
			for (String value : StringUtils.split(values, ",")) {
				valueList.add(getDictLabel(value, type, defaultValue));
			}
			return StringUtils.join(valueList, ",");
		}
		return defaultValue;
	}

	public static String getDictValue(String label, String type, String defaultLabel) {
		if (StringUtils.isNotBlank(type) && StringUtils.isNotBlank(label)) {
			for (Dict dict : getDictList(type)) {
				if (type.equals(dict.getType()) && label.equals(dict.getLabel())) {
					return dict.getValue();
				}
			}
		}
		return defaultLabel;
	}

	public static List<Dict> getDictList(String type) {
		@SuppressWarnings("unchecked")
		Map<String, List<Dict>> dictMap = (Map<String, List<Dict>>) CacheUtils.get(CACHE_DICT_MAP);

		if (dictMap == null) {
			dictMap = Maps.newHashMap();
			for (Dict dict : dictDao.findAllList(new Dict())) {
				List<Dict> dictList = dictMap.get(dict.getType());
				if (dictList != null) {
					dictList.add(dict);
				} else {
					dictMap.put(dict.getType(), Lists.newArrayList(dict));
				}
			}
			CacheUtils.put(CACHE_DICT_MAP, dictMap);
		}
		List<Dict> dictList = dictMap.get(type);
		if (dictList == null) {
			dictList = Lists.newArrayList();
		}
		return dictList;
	}

	/**
	 * 取type类型下的所有label，多个以中文顿号、隔开。
	 */
	public static String getDictAllLabels(String type) {
		Set<String> dictAllLabelsSet = getDictAllLabelsSet(type);
		if (Collections3.isEmpty(dictAllLabelsSet)) {
			return "";
		}
		return StringUtils.join(dictAllLabelsSet, "、");
	}

	@SuppressWarnings("unchecked")
	public static List<String> getDictAllLabelsList(String type) {
		List<Dict> dicts = getDictList(type);
		if (Collections3.isEmpty(dicts)) {
			return Lists.newArrayList();
		}
		return (List<String>) CollectionUtils.collect(dicts, new BeanToPropertyValueTransformer("label"));
	}

	public static LinkedHashSet<String> getDictAllLabelsSet(String type) {
		List<String> list = getDictAllLabelsList(type);
		LinkedHashSet<String> set = new LinkedHashSet<>();
		if (Collections3.isEmpty(list)) {
			return set;
		}
		set.addAll(list);
		return set;
	}

	/**
	 * 取type类型下的所有value
	 */
	public static Set<String> getDictAllValues(String type) {
		Set<String> set = new HashSet<>();
		List<Dict> dicts = getDictList(type);
		if (Collections3.isEmpty(dicts)) {
			return set;
		}
		@SuppressWarnings("unchecked")
		List<String> labeles = (List<String>) CollectionUtils.collect(dicts, new BeanToPropertyValueTransformer("value"));
		set.addAll(labeles);
		return set;
	}
}
