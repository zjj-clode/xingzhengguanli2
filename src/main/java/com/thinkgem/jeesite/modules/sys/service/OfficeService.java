/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.sys.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.service.TreeService;
import com.thinkgem.jeesite.modules.sys.dao.OfficeDao;
import com.thinkgem.jeesite.modules.sys.entity.Office;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;

/**
 * 机构Service
 * 
 * @author ThinkGem
 * @version 2014-05-16
 */
@Service
@Transactional(readOnly = true)
public class OfficeService extends TreeService<OfficeDao, Office> {

	
	public Office getOfficeByCode(Office office){
		return dao.getOfficeByCode(office);
	}
	
	@Transactional(readOnly = false)
	public void updateSort(Office office) {
		dao.updateSort(office);
	}
	
	public List<Office> findAll() {
		return UserUtils.getOfficeList();
	}

	public List<Office> findList(Boolean isAll) {
		if (isAll != null && isAll) {
			return UserUtils.getOfficeAllList();
		} else {
			return UserUtils.getOfficeList();
		}
	}

	public Office findOfficeByOfficeInf(Office office) {
		return dao.findOfficeByOfficeInf(office);
	}

	@Transactional(readOnly = true)
	public List<Office> findAllListOffice(Office office) {
		return dao.findAllListOffice(office);
	}

	@Transactional(readOnly = true)
	public List<Office> findListByParentid(Office office) {
		office.setParentIds("%" + office.getParentIds() + "%");
		return dao.findByParentIdsLike(office);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Office> findList(Office office) {
		office.setParentIds(office.getParentIds() + "%");
		return dao.findByParentIdsLike(office);
	}

	@Transactional(readOnly = true)
	public List<Office> findList2(Office office) {
		office = new Office();
		office.setParentIds("0,1,418bfda252e74aa4b35d099959ef1364%");
		return dao.findByParentIdsLike2(office);
	}

	@Override
	@Transactional(readOnly = false)
	public void save(Office office) {
		super.save(office);
		UserUtils.removeCache(UserUtils.CACHE_OFFICE_LIST);
		UserUtils.removeCache(UserUtils.CACHE_OFFICE_LIST + "=" + office.getType());
	}
	
	@Transactional(readOnly = false)
	public void insertOffice(Office office) {
		dao.insert(office);
		UserUtils.removeCache(UserUtils.CACHE_OFFICE_LIST);
		UserUtils.removeCache(UserUtils.CACHE_OFFICE_LIST + "=" + office.getType());
	}
	
	@Transactional(readOnly = false)
	public void batchInsertUpdate(List<Office> officeList){
		dao.batchInsertUpdate(officeList);
		UserUtils.removeCache(UserUtils.CACHE_OFFICE_LIST);
		for (Office office : officeList) {
			UserUtils.removeCache(UserUtils.CACHE_OFFICE_LIST + "=" + office.getType());
		}
	}

	/**
	 * 不清除缓存
	 */
	@Transactional(readOnly = false)
	public void saveWithoutCache(Office office) {
		super.save(office);
	}

	@Override
	@Transactional(readOnly = false)
	public void delete(Office office) {
		super.delete(office);
		UserUtils.removeCache(UserUtils.CACHE_OFFICE_LIST);
		UserUtils.removeCache(UserUtils.CACHE_OFFICE_LIST + "=" + office.getType());

	}

	/**
	 * @see OfficeDao#findByType
	 * 
	 * @param office
	 * @return
	 */
	@Transactional(readOnly = true)
	public List<Office> findByType(Office office) {
		return dao.findByType(office);
	}
	
	
	public List<Office> findListByName(Office office){
		return dao.findListByName(office);
	}
}
