package com.cloudinte.modules.xingzhengguanli.web;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.cloudinte.modules.xingzhengguanli.entity.WorkAttendance;
import com.cloudinte.modules.xingzhengguanli.entity.WorkAttendanceSetting;
import com.cloudinte.modules.xingzhengguanli.service.WorkAttendanceService;
import com.cloudinte.modules.xingzhengguanli.service.WorkAttendanceSettingService;
import com.google.common.collect.Lists;
import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.utils.DateUtils;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.utils.excel.ExportExcel;
import com.thinkgem.jeesite.common.utils.excel.ImportExcel;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.sys.entity.Office;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.sys.service.OfficeService;
import com.thinkgem.jeesite.modules.sys.service.SystemService;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;

/**
 * 工作考勤Controller
 * @author dcl
 * @version 2019-12-07
 */
@Controller
@RequestMapping(value = "${adminPath}/xingzhengguanli/workAttendance")
public class WorkAttendanceController extends BaseController {

	private static final String FUNCTION_NAME_SIMPLE = "工作考勤";

	@Autowired
	private WorkAttendanceService workAttendanceService;
	
	@Autowired
	private SystemService systemService;
	
	@Autowired
	private OfficeService officeService;
	
	@Autowired
	private WorkAttendanceSettingService workAttendanceSettingService;
	
	@ModelAttribute
	public WorkAttendance get(@RequestParam(required=false) String id) {
		WorkAttendance entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = workAttendanceService.get(id);
		}
		if (entity == null){
			entity = new WorkAttendance();
		}
		return entity;
	}
	
	@RequiresPermissions("xingzhengguanli:workAttendance:view")
	@RequestMapping(value = {"list", ""})
	public String list(WorkAttendance workAttendance, HttpServletRequest request, HttpServletResponse response, Model model) {
		
		User user = new User();
		
		User opUser = UserUtils.getUser();
		if("6".equals(opUser.getUserType())){
			workAttendance.setCreateBy(opUser);
			user.setOffice(opUser.getOffice());
			
			
			List<Office> officeList = new ArrayList<Office>();
			officeList.add(opUser.getOffice());
			model.addAttribute("officeList", officeList);
			
		}else{
			Office office = new Office();
			office.setType("5");//科室
			List<Office> officeList = officeService.findAllListOffice(office);
			model.addAttribute("officeList", officeList);
		}
		
		List<User> userList = systemService.findUser(user);
		model.addAttribute("userList", userList);
		
		
		
		Page<WorkAttendance> page = workAttendanceService.findPage(new Page<WorkAttendance>(request, response), workAttendance); 
		model.addAttribute("page", page);
		model.addAttribute("ename", "workAttendance");
		setBase64EncodedQueryStringToEntity(request, workAttendance);
		return "modules/xingzhengguanli/workAttendance/workAttendanceList";
	}

	@RequiresPermissions("xingzhengguanli:workAttendance:view")
	@RequestMapping(value = "form")
	public String form(WorkAttendance workAttendance, Model model) {
		
		User user = new User();
		User opUser = UserUtils.getUser();
		if("6".equals(opUser.getUserType())){
			user.setOffice(opUser.getOffice());
		}
		List<User> userList = systemService.findUser(user);
		model.addAttribute("userList", userList);
		model.addAttribute("workAttendance", workAttendance);
		model.addAttribute("ename", "workAttendance");
		return "modules/xingzhengguanli/workAttendance/workAttendanceForm";
	}

	@RequiresPermissions("xingzhengguanli:workAttendance:edit")
	@RequestMapping(value = "save")
	public String save(WorkAttendance workAttendance, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, workAttendance)){
			return form(workAttendance, model);
		}
		addMessage(redirectAttributes, StringUtils.isBlank(workAttendance.getId()) ? "保存"+FUNCTION_NAME_SIMPLE+"成功" : "修改"+FUNCTION_NAME_SIMPLE+"成功");
		if(workAttendance.getOvertimeUser() != null){
			User overtimeuser = systemService.getUser(workAttendance.getOvertimeUser().getId());
			if(overtimeuser != null){
				workAttendance.setKeshi(overtimeuser.getOffice().getName());
				workAttendance.setName(overtimeuser.getName());
				workAttendance.setJobNumber(overtimeuser.getNo());
			}
		}
		workAttendanceService.save(workAttendance);
		return "redirect:"+adminPath+"/xingzhengguanli/workAttendance/?repage&" + StringUtils.trimToEmpty(getBase64DecodedQueryStringFromEntity(workAttendance));
	}
	
	
	@RequestMapping(value = "checkDaysSave")
	@ResponseBody
	public Map<String, Object> checkDaysSave(WorkAttendance workAttendance, Model model, RedirectAttributes redirectAttributes) {
		
		Map<String, Object> result = new HashMap<String, Object>();
		
		WorkAttendanceSetting workAttendanceSetting = new WorkAttendanceSetting();
		workAttendanceSetting.setYearMon(workAttendance.getYearMon());
		List<WorkAttendanceSetting> settingList = workAttendanceSettingService.findList(workAttendanceSetting);
		if(settingList != null && settingList.size() != 0){
			workAttendanceSetting = settingList.get(0);
		}
		
		if(StringUtils.isNotBlank(workAttendanceSetting.getId())){
			int days = workAttendanceSetting.getOvertimeDays();
			int inputDays = workAttendance.getOvertimeDays();
			
			if(inputDays > days){
				result.put("re","error");
				result.put("message","填写的时间超过了本月最多加班天数，本月最富哦加班天数为："+days+"天");
				return result;
			}
			
		}
		
		addMessage(redirectAttributes, StringUtils.isBlank(workAttendance.getId()) ? "保存"+FUNCTION_NAME_SIMPLE+"成功" : "修改"+FUNCTION_NAME_SIMPLE+"成功");
		if(workAttendance.getOvertimeUser() != null){
			User overtimeuser = systemService.getUser(workAttendance.getOvertimeUser().getId());
			if(overtimeuser != null){
				workAttendance.setKeshi(overtimeuser.getOffice().getName());
				workAttendance.setName(overtimeuser.getName());
				workAttendance.setJobNumber(overtimeuser.getNo());
			}
		}
		workAttendanceService.save(workAttendance);
		result.put("re","ok");
		result.put("message","保存成功");
		return result;
	}
	
	@RequiresPermissions("xingzhengguanli:workAttendance:edit")
	@RequestMapping(value = "disable")
	public String disable(WorkAttendance workAttendance, RedirectAttributes redirectAttributes) {
		workAttendanceService.disable(workAttendance);
		addMessage(redirectAttributes, "禁用"+FUNCTION_NAME_SIMPLE+"成功");
		return "redirect:"+adminPath+"/xingzhengguanli/workAttendance/?repage&" + StringUtils.trimToEmpty(getBase64DecodedQueryStringFromEntity(workAttendance));
	}
	
	@RequiresPermissions("xingzhengguanli:workAttendance:edit")
	@RequestMapping(value = "delete")
	public String delete(WorkAttendance workAttendance, RedirectAttributes redirectAttributes) {
		workAttendanceService.delete(workAttendance);
		addMessage(redirectAttributes, "删除"+FUNCTION_NAME_SIMPLE+"成功");
		return "redirect:"+adminPath+"/xingzhengguanli/workAttendance/?repage&" + StringUtils.trimToEmpty(getBase64DecodedQueryStringFromEntity(workAttendance));
	}
	
	@RequiresPermissions("xingzhengguanli:workAttendance:edit")
	@RequestMapping(value = "deleteBatch")
	public String deleteBatch(WorkAttendance workAttendance, RedirectAttributes redirectAttributes) {
		workAttendanceService.deleteByIds(workAttendance);
		addMessage(redirectAttributes, "批量删除"+FUNCTION_NAME_SIMPLE+"成功");
		return "redirect:"+adminPath+"/xingzhengguanli/workAttendance/?repage&" + StringUtils.trimToEmpty(getBase64DecodedQueryStringFromEntity(workAttendance));
	}
	
	/**
	 * 导出数据
	 */
	@RequiresPermissions("xingzhengguanli:workAttendance:edit")
	@RequestMapping(value = "export", method = RequestMethod.POST)
	public String exportFile(WorkAttendance workAttendance, HttpServletRequest request,
			HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
			User opUser = UserUtils.getUser();
			if("6".equals(opUser.getUserType())){
				workAttendance.setCreateBy(opUser);
			}
			String fileName = FUNCTION_NAME_SIMPLE + DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";
			workAttendance.setPage(new Page<WorkAttendance>(request, response, -1));
			String title = "加班天数统计";
			if(workAttendance.getYearMon() != null){
				title = DateUtils.formatDate(workAttendance.getYearMon(), "MM")+"加班天数统计";
			}
			new ExportExcel(title, WorkAttendance.class).setDataList(workAttendanceService.findList(workAttendance)).write(response, fileName).dispose();
			return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出" + FUNCTION_NAME_SIMPLE + "失败！失败信息：" + e.getMessage());
			return "redirect:"+adminPath+"/xingzhengguanli/workAttendance/?repage&" + StringUtils.trimToEmpty(getBase64DecodedQueryStringFromEntity(workAttendance));
		}
	}

	/**
	 * 下载导入数据模板
	 */
	@RequiresPermissions("xingzhengguanli:workAttendance:view")
	@RequestMapping(value = "import/template")
	public String importFileTemplate(WorkAttendance workAttendance, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
			String fileName = FUNCTION_NAME_SIMPLE + "导入模板.xlsx";
			List<WorkAttendance> list = workAttendanceService.findPage(new Page<WorkAttendance>(1, 5), new WorkAttendance()).getList();
			if (list == null) {
				list = Lists.newArrayList();
			}
			if (list.size() < 1) {
				list.add(new WorkAttendance());
			}
			new ExportExcel(FUNCTION_NAME_SIMPLE, WorkAttendance.class).setDataList(list).write(response, fileName).dispose();
			return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息：" + e.getMessage());
			return "redirect:"+adminPath+"/xingzhengguanli/workAttendance/?repage&" + StringUtils.trimToEmpty(getBase64DecodedQueryStringFromEntity(workAttendance));
		}
	}

	/**
	 * 导入数据<br>
	 */
	@RequiresPermissions("xingzhengguanli:workAttendance:edit")
	@RequestMapping(value = "import", method = RequestMethod.POST)
	public String importFile(WorkAttendance workAttendance, MultipartFile file, RedirectAttributes redirectAttributes) {
		if(Global.isDemoMode()){
			addMessage(redirectAttributes, "演示模式，不允许操作！");
			return "redirect:"+adminPath+"/xingzhengguanli/workAttendance/?repage&" + StringUtils.trimToEmpty(getBase64DecodedQueryStringFromEntity(workAttendance));
		}
		try {
			long beginTime = System.currentTimeMillis();//1、开始时间  
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<WorkAttendance> list = ei.getDataList(WorkAttendance.class);
			List<WorkAttendance> insertList=new ArrayList<WorkAttendance>();
			List<WorkAttendance> subList=new ArrayList<WorkAttendance>();
			int batchinsertSize=500;
			int batchinsertCount=list.size()/batchinsertSize+(list.size()%batchinsertSize==0?0:1);
			int addNum=0;
			int updateNum=0;
			int toIndex=0;
			for(int i=0;i<batchinsertCount;i++)
			{
				insertList=new ArrayList<WorkAttendance>();
			    toIndex=(i+1)*batchinsertSize;
			    if(toIndex>list.size())
			    	toIndex=list.size();
				subList=list.subList(i*batchinsertSize, toIndex);
				for(WorkAttendance zsJh : subList)
				{
					zsJh.preInsert();
					insertList.add(zsJh);
				}
				
				if(insertList!=null&&insertList.size()>0)
				{
					System.out.println("insertList size is :"+insertList.size());
					workAttendanceService.batchInsertUpdate(insertList);
					addNum+=insertList.size();
				}
			}
			long endTime = System.currentTimeMillis(); 	//2、结束时间  
			addMessage(redirectAttributes, "执行时间"+DateUtils.formatDateTime(endTime - beginTime)+",导入 "+addNum+"条"+FUNCTION_NAME_SIMPLE+"信息,"+failureMsg);
			
			System.out.println("执行时间："+DateUtils.formatDateTime(endTime - beginTime));
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入"+FUNCTION_NAME_SIMPLE+"信息失败！失败信息："+e.getMessage());
		}
			return "redirect:"+adminPath+"/xingzhengguanli/workAttendance/?repage&" + StringUtils.trimToEmpty(getBase64DecodedQueryStringFromEntity(workAttendance));
		}
	
	
	
	
}