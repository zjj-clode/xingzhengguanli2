package com.thinkgem.jeesite.modules.sys.utils;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.utils.CacheUtils;
import com.thinkgem.jeesite.common.utils.DateUtils;
import com.thinkgem.jeesite.common.utils.SpringContextHolder;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.modules.sys.dao.SettingsDao;
import com.thinkgem.jeesite.modules.sys.entity.Settings;

/**
 * 运行参数配置<br/>
 * Settings部分配置在数据库中，用户可以运行时自定义；<br/>
 * Global部分配置在jeesite.properties文件中，用户不能自定义。<br/>
 * Settings部分配置的优先级比Global部分配置要高。<br/>
 * 使用 {@link #getSysConfig(String)} 和 {@link #getSysConfig(String, String)} 、 {@link #getSysConfig(String, Object)} 方法获取配置参数值<br/>
 * 页面中使用对应fns.tld文件中的${fns:getSysConfig(key)}和${fns:getSysConfigWithDefaultValue (key, defaultValue)}
 */
public final class SettingsUtils {

	private SettingsUtils() {
	}

	private static Logger logger = LoggerFactory.getLogger(SettingsUtils.class);

	// private static SettingsDao settingsDao = SpringContextHolder.getBean(SettingsDao.class);

	private static final String CACHE_SETTINGS_MAP = "settingsMap";

	/**
	 * true开发模式；false产品模式。默认为false
	 */
	public static boolean isDevMode() {
		return getSysConfig("devMode", false);
	}

	/**
	 * 获取参数配置<br/>
	 * 
	 * 优先从Settings（数据库）中获取，没有得到再从Global（jeesite.properties文件）中获取
	 * 
	 * @param key
	 * @return String类型的值
	 */
	public static String getSysConfig(String key) {
		if (StringUtils.isBlank(key)) {
			return null;
		}
		// 
		String value = getSettingsValue(key); // 从Settings（数据库）中获取
		if (!StringUtils.isBlank(value)) {
			return value;
		}
		// 
		return Global.getConfig(key); // 从Global（jeesite.properties文件）中获取
	}

	/**
	 * 获取参数配置<br/>
	 * 
	 * 优先从Settings（数据库）中获取，没有得到再从Global（jeesite.properties文件）中获取
	 * 
	 * @param defaultValue 获取失败得到默认值
	 * @return String类型的值
	 */
	public static String getSysConfig(String key, String defaultValue) {
		if (StringUtils.isBlank(key)) {
			return defaultValue;
		}
		//
		String value = getSysConfig(key);
		if (StringUtils.isBlank(value)) {
			return defaultValue;
		}
		return value;
	}

	/**
	 * 获取参数配置<br/>
	 * 优先从Settings（数据库）中获取，没有得到再从Global（jeesite.properties文件）中获取
	 * 
	 * @param key
	 * @param defaultValue 获取失败得到默认值
	 * @return 返回值类型会根据defaultValue的类型进行数据转换，转换失败最终会返回defaultValue
	 */
	public static <T> T getSysConfig(String key, T defaultValue) {
		if (StringUtils.isBlank(key)) {
			return defaultValue;
		}
		// 
		T value = null;
		//
		String settingsValue = getSettingsValue(key); // 从Settings（数据库）中获取
		if (!StringUtils.isBlank(settingsValue)) {
			value = convertValue(defaultValue, settingsValue);
		}
		if (value != null) {
			return value;
		}
		// 
		String globalValue = Global.getConfig(key); // 从Global（jeesite.properties文件）中获取
		if (!StringUtils.isBlank(globalValue)) {
			value = convertValue(defaultValue, globalValue);
		}
		if (value != null) {
			return value;
		}
		//
		return defaultValue;
	}

	/**
	 * 根据key获取value
	 * 
	 * @return String 类型原始值
	 */
	private static String getSettingsValue(String key) {
		Settings settings = getSettings(key);
		if (settings == null) {
			return null;
		}
		return settings.getValue();
	}

	/**
	 * 将String类型的value转换成T类型值返回，如果转换失败或不支持的转换类型，返回defaultValue
	 */
	@SuppressWarnings("unchecked")
	private static <T> T convertValue(T defaultValue, String value) {
		if (StringUtils.isBlank(value)) {
			return defaultValue;
		}
		if (defaultValue instanceof String) {
			return (T) value;
		} else if (defaultValue instanceof Date) {
			return (T) DateUtils.parseDate(value);
		} else if (defaultValue instanceof Long || defaultValue != null && defaultValue.getClass() == long.class) {
			try {
				return (T) Long.valueOf(value);
			} catch (NumberFormatException e) {
				logger.warn("配置参数转换错误：{}", e.getMessage());
			}
		} else if (defaultValue instanceof Double || defaultValue != null && defaultValue.getClass() == double.class) {
			try {
				return (T) Double.valueOf(value);
			} catch (NumberFormatException e) {
				logger.warn("配置参数转换错误：{}", e.getMessage());
			}
		} else if (defaultValue instanceof Boolean || defaultValue != null && defaultValue.getClass() == boolean.class) {
			Boolean b = Boolean.valueOf(value) || "yes".equalsIgnoreCase(value) || "1".equalsIgnoreCase(value) || "on".equalsIgnoreCase(value);
			return (T) b;
		} else if (defaultValue instanceof Byte || defaultValue != null && defaultValue.getClass() == byte.class) {
			try {
				return (T) Byte.valueOf(value);
			} catch (NumberFormatException e) {
				logger.warn("配置参数转换错误：{}", e.getMessage());
			}
		} else if (defaultValue instanceof Short || defaultValue != null && defaultValue.getClass() == short.class) {
			try {
				return (T) Short.valueOf(value);
			} catch (NumberFormatException e) {
				logger.warn("配置参数转换错误：{}", e.getMessage());
			}
		} else if (defaultValue instanceof Integer || defaultValue != null && defaultValue.getClass() == int.class) {
			try {
				return (T) Integer.valueOf(value);
			} catch (NumberFormatException e) {
				logger.warn("配置参数转换错误：{}", e.getMessage());
			}
		} else if (defaultValue instanceof Float || defaultValue != null && defaultValue.getClass() == float.class) {
			try {
				return (T) Float.valueOf(value);
			} catch (NumberFormatException e) {
				logger.warn("配置参数转换错误：{}", e.getMessage());
			}
		}
		return defaultValue;
	}

	/**
	 * 根据key获取唯一的配置对象
	 */
	@SuppressWarnings("unchecked")
	private static Settings getSettings(String key) {
		try {
			Map<String, Settings> settingsMap = (Map<String, Settings>) CacheUtils.get(CACHE_SETTINGS_MAP);
			if (settingsMap == null) {
				SettingsDao settingsDao = null;
				settingsDao = SpringContextHolder.getBean(SettingsDao.class);
				if (settingsDao != null) {
					List<Settings> allSettings = settingsDao.findAllList(new Settings());
					settingsMap = new HashMap<>();
					for (Settings settings : allSettings) {
						settingsMap.put(settings.getKey(), settings);
					}
					CacheUtils.put(CACHE_SETTINGS_MAP, settingsMap);
				}
			}
			return settingsMap.get(key);
		} catch (Throwable e) { // main 方法 Run As 测试时爆 applicaitonContext属性未注入， 此时不读缓存也不读数据库，可以忽略掉错误
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 根据dataType属性值进行了数据转换
	 * 
	 * @return value与dataType不相符，转换失败时返回null
	 */
	public static Object convertSettingsValueByDataType(Settings settings) {
		if (settings != null) {
			String value = settings.getValue();
			String dataType = settings.getDataType();
			if (Settings.DATA_TYPE_BOOLEAN.equals(dataType)) {
				return Boolean.valueOf(value) || "yes".equalsIgnoreCase(value) || "1".equalsIgnoreCase(value) || "on".equalsIgnoreCase(value);
			} else if (Settings.DATA_TYPE_DATE.equals(dataType)) {// 字符串转日期失败时返回 null
				return DateUtils.parseDate(value);
			} else if (Settings.DATA_TYPE_DOUBLE.equals(dataType)) {
				try {
					return Double.valueOf(value);
				} catch (NumberFormatException e) {
					logger.warn("数据库配置参数转换错误：{}", e.getMessage());
				}
			} else if (Settings.DATA_TYPE_LONG.equals(dataType)) {
				try {
					return Long.valueOf(value);
				} catch (NumberFormatException e) {
					logger.warn("数据库配置参数转换错误：{}", e.getMessage());
				}
			} else if (Settings.DATA_TYPE_FLOAT.equals(dataType)) {
				try {
					return Float.valueOf(value);
				} catch (NumberFormatException e) {
					logger.warn("数据库配置参数转换错误：{}", e.getMessage());
				}
			} else if (Settings.DATA_TYPE_INTEGER.equals(dataType)) {
				try {
					return Integer.valueOf(value);
				} catch (NumberFormatException e) {
					logger.warn("数据库配置参数转换错误：{}", e.getMessage());
				}
			} else if (Settings.DATA_TYPE_SHORT.equals(dataType)) {
				try {
					return Short.valueOf(value);
				} catch (NumberFormatException e) {
					logger.warn("数据库配置参数转换错误：{}", e.getMessage());
				}
			} else if (Settings.DATA_TYPE_BYTE.equals(dataType)) {
				try {
					return Byte.valueOf(value);
				} catch (NumberFormatException e) {
					logger.warn("数据库配置参数转换错误：{}", e.getMessage());
				}
			} else if (Settings.DATA_TYPE_STRING.equals(dataType)) {
				return value;
			}
		}
		return null;
	}

	public static void clearCache() {
		CacheUtils.remove(CACHE_SETTINGS_MAP);
	}
}
