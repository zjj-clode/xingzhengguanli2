/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.sys.dao;

import java.util.List;

import com.thinkgem.jeesite.common.persistence.TreeDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.sys.entity.Office;

/**
 * 机构DAO接口
 * 
 * @author ThinkGem
 * @version 2014-05-16
 */
@MyBatisDao
public interface OfficeDao extends TreeDao<Office> {

	public List<Office> findAllListOffice(Office office);

	/**
	 * 根据机构类型查
	 * 
	 * @param office
	 * @return
	 */
	List<Office> findByType(Office office);
	
	/**
	 *根据名称获得机构信息 
	 *
	 * @param office
	 * @return
	 * */
	Office findByOfficeName(Office offfice);

	/**
	 *根据机构信息获得机构
	 *
	 * @param office
	 * @return
	 * */
	Office findOfficeByOfficeInf(Office offfice);

	public Office getOfficeByCode(Office office);

	public void updateSort(Office office);
	
	List<Office> findListByName(Office office);
	public void batchInsertUpdate(List<Office> officeList);
}
