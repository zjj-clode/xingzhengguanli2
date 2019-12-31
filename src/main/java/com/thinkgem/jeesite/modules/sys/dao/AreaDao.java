/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.sys.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.thinkgem.jeesite.common.persistence.TreeDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.sys.entity.Area;

/**
 * 区域DAO接口
 * @author ThinkGem
 * @version 2014-05-16
 */
@MyBatisDao
public interface AreaDao extends TreeDao<Area> {
	
	List<Area> findProvinceList(Area area);
	
	List<Area> findCityList(@Param("parentId") String provinceCode);
	
	List<Area> findBrotherList(@Param("cityId") String cityId);
	
	List<Area> findByParentId(@Param("parentId") String parentId);
	
	List<Area> findCountryOrCityList(@Param("parentId") String parentId);
	
	List<Area> findListByUserArea(@Param("userId") String userId);
	List<String> findAreaIdsByUserArea(@Param("userId") String userId);
}
