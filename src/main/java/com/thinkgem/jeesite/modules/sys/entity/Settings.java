/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.sys.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import com.thinkgem.jeesite.common.persistence.DataEntity;

/**
 * 运行参数Entity<br/>
 * key唯一，一个key对应一个value，像map一样。
 */
public class Settings extends DataEntity<Settings> {

	private static final long serialVersionUID = -1378308212925258036L;

	private String name; // 参数名
	private String key; // 参数键
	private String value; // 参数值
	private String dataType; // 参数值数据类型  String Integer Boolean

	private String systemDefined; // yes_no 是否是系统定义的参数，非系统定义的参数才能被普通的管理员修改。

	public static final String DATA_TYPE_LONG = Long.class.getSimpleName();
	public static final String DATA_TYPE_DOUBLE = Double.class.getSimpleName();
	public static final String DATA_TYPE_STRING = String.class.getSimpleName();
	public static final String DATA_TYPE_BOOLEAN = Boolean.class.getSimpleName();
	public static final String DATA_TYPE_DATE = Date.class.getSimpleName();
	//
	public static final String DATA_TYPE_INTEGER = Integer.class.getSimpleName();
	public static final String DATA_TYPE_FLOAT = Float.class.getSimpleName();
	public static final String DATA_TYPE_BYTE = Byte.class.getSimpleName();
	public static final String DATA_TYPE_SHORT = Short.class.getSimpleName();

	public static final String[] DATA_TYPES = { DATA_TYPE_LONG, DATA_TYPE_DOUBLE, DATA_TYPE_STRING, DATA_TYPE_BOOLEAN, DATA_TYPE_DATE,
			//
			DATA_TYPE_INTEGER, DATA_TYPE_FLOAT, DATA_TYPE_BYTE, DATA_TYPE_SHORT
			//
	};

	private List<Settings> settingsList = new ArrayList<>();

	public Settings() {
		super();
	}

	public Settings(String id) {
		super(id);
	}

	public Settings(String key, String value, String dataType) {
		this.key = key;
		this.value = value;
		this.dataType = dataType;
	}

	@NotEmpty(message = "参数名不能为空，最少1个字符，最多255个字符")
	@Length(min = 1, max = 255)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@NotEmpty(message = "参数键不能为空，最少1个字符，最多255个字符")
	@Length(min = 1, max = 255)
	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	@Length(max = 1000, message = "参数值最多1000个字符")
	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@NotEmpty(message = "参数类型不能为空，最少1个字符，最多50个字符")
	@Length(min = 1, max = 50)
	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	@Override
	public String toString() {
		return key;
	}

	public String[] getAllDataTypes() {
		return DATA_TYPES;
	}

	public List<Settings> getSettingsList() {
		return settingsList;
	}

	public void setSettingsList(List<Settings> settingsList) {
		this.settingsList = settingsList;
	}

	public String getSystemDefined() {
		return systemDefined;
	}

	public void setSystemDefined(String systemDefined) {
		this.systemDefined = systemDefined;
	}

}