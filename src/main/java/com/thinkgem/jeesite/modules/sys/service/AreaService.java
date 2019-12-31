/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.sys.service;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.service.TreeService;
import com.thinkgem.jeesite.modules.sys.dao.AreaDao;
import com.thinkgem.jeesite.modules.sys.entity.Area;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;

/**
 * 区域Service
 * @author ThinkGem
 * @version 2014-05-16
 */
@Service
@Transactional(readOnly = true)
public class AreaService extends TreeService<AreaDao, Area> {
	
	@Autowired
	private AreaDao areaDao;

	public List<Area> findAll(){
		return UserUtils.getAreaList();
	}
	
	/**
	 * 获取省列表
	 * @return
	 */
	public List<Area> findProvinceList(){
		return UserUtils.getProvinceList();
	}
	
	/**
	 * 获取市列表
	 * @return
	 */
	public List<Area> findCountryOrCityList(String parentId){
		return areaDao.findCountryOrCityList(parentId);
	}
	
	/**
	 * 获取市列表
	 * @return
	 */
	public List<Area> findCityList(String provinceCode){
		return areaDao.findCityList(provinceCode);
	}
	
	/**
	 * 获取市兄弟列表
	 * @return
	 */
	public List<Area> findBrotherList(String cityCode){
		return areaDao.findBrotherList(cityCode);
	}
	
	public List<Area> findByParentId(String parentId){
	    return areaDao.findByParentId(parentId);
	}
	

	@Transactional(readOnly = false)
	public void save(Area area) {
		super.save(area);
		UserUtils.removeCache(UserUtils.CACHE_AREA_LIST);
	}
	
	@Transactional(readOnly = false)
	public void delete(Area area) {
		super.delete(area);
		UserUtils.removeCache(UserUtils.CACHE_AREA_LIST);
	}
	
	public List<Area> findListByUserArea(String userId){
		return areaDao.findListByUserArea(userId);
	}
	
	public List<String> findAreaIdsByUserArea(String userId){
		return areaDao.findAreaIdsByUserArea(userId);
	}
}
