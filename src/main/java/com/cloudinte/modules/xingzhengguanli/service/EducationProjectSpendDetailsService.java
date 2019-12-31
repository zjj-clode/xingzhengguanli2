package com.cloudinte.modules.xingzhengguanli.service;

import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringEscapeUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cloudinte.modules.xingzhengguanli.dao.EducationProjectSpendDetailsDao;
import com.cloudinte.modules.xingzhengguanli.entity.EducationProjectDescription;
import com.cloudinte.modules.xingzhengguanli.entity.EducationProjectSpendDetails;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.modules.sys.entity.User;

/**
 * 项目支出明细Service
 * @author dcl
 * @version 2019-12-14
 */
@Service
@Transactional(readOnly = true)
public class EducationProjectSpendDetailsService extends CrudService<EducationProjectSpendDetailsDao, EducationProjectSpendDetails> {
	
	public Page<EducationProjectSpendDetails> findPage(Page<EducationProjectSpendDetails> page, EducationProjectSpendDetails educationProjectSpendDetails) {
		page.setCount(dao.findCount(educationProjectSpendDetails));
		return super.findPage(page, educationProjectSpendDetails);
	}
	
	@Transactional(readOnly = false)
	public void disable(EducationProjectSpendDetails educationProjectSpendDetails) {
		dao.disable(educationProjectSpendDetails);
	}
	
	/*
	@Transactional(readOnly = false)
	public void deleteByIds(String[] ids) {
		if (ids == null || ids.length < 1) {
			return;
		}
		dao.deleteByIds(ids);
	}
	*/
	
	@Transactional(readOnly = false)
	public void deleteByIds(EducationProjectSpendDetails educationProjectSpendDetails) {
		if (educationProjectSpendDetails == null || educationProjectSpendDetails.getIds() == null || educationProjectSpendDetails.getIds().length < 1) {
			return;
		}
		dao.deleteByIds(educationProjectSpendDetails);
	}
	
	@Transactional(readOnly = false)
	public void batchInsert(List<EducationProjectSpendDetails> objlist) {
		dao.batchInsert(objlist);
	}
	
	@Transactional(readOnly = false)
	public void batchUpdate(List<EducationProjectSpendDetails> objlist) {
		dao.batchUpdate(objlist);
	}
	
	@Transactional(readOnly = false)
	public void batchInsertUpdate(List<EducationProjectSpendDetails> objlist) {
		dao.batchInsertUpdate(objlist);
	}
	
	@Transactional(readOnly = false)
	public void deleteByUserAndYear(EducationProjectSpendDetails educationProjectSpendDetails){
		dao.deleteByUserAndYear(educationProjectSpendDetails);
	}
	
	@Transactional(readOnly = false)
	public void batchSave(EducationProjectSpendDetails educationProjectSpendDetails,String addDetails){
		dao.deleteByUserAndYear(educationProjectSpendDetails);
		
		if(StringUtils.isNotBlank(addDetails)){
			
			List<EducationProjectSpendDetails> spendDetailsList = new ArrayList<EducationProjectSpendDetails>();
			
			addDetails = StringEscapeUtils.unescapeHtml4(addDetails);
			JSONArray arrayJson=JSONArray.fromObject(addDetails);
			if (arrayJson != null && arrayJson.size() != 0) {
				for (Object object : arrayJson) {
					JSONObject json = JSONObject.fromObject(object);
					
					String year = json.getString("year");
					String userid = json.getString("userid");
					String projectDesid = json.getString("projectDesid");
					String projectName = json.getString("projectName");
					String projectDescription = json.getString("projectDescription");
					String childProject = json.getString("childProject");
					String childProjectDescription = json.getString("childProjectDescription");
					String subItemExpenditure = json.getString("subItemExpenditure");
					String numberFrequency = json.getString("numberFrequency");
					String money = json.getString("money");
					
					if(StringUtils.isNotBlank(money)){
						
						EducationProjectSpendDetails spendDetails = new EducationProjectSpendDetails();
						spendDetails.setYear(year);
						spendDetails.setUser(new User(userid));
						spendDetails.setProjectDes(new EducationProjectDescription(projectDesid));
						spendDetails.setProjectName(projectName);
						spendDetails.setProjectDescription(projectDescription);
						spendDetails.setChildProject(childProject);
						spendDetails.setChildProjectDescription(childProjectDescription);
						spendDetails.setSubItemExpenditure(subItemExpenditure);
						spendDetails.setNumberFrequency(numberFrequency);
						
						double m = Double.valueOf(money);
						spendDetails.setMoney(m);
						
						spendDetails.preInsert();
						spendDetailsList.add(spendDetails);
					}
				}
			}
			if(spendDetailsList.size() != 0){
				batchInsert(spendDetailsList);
			}
		}
	}
	
	public List<EducationProjectSpendDetails> findExportSpendDetails(EducationProjectSpendDetails educationProjectSpendDetails){
		return dao.findExportSpendDetails(educationProjectSpendDetails);
	}
}