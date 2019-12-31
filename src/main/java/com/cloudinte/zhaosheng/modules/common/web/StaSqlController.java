package com.cloudinte.zhaosheng.modules.common.web;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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

import com.cloudinte.zhaosheng.modules.common.entity.StaSql;
import com.cloudinte.zhaosheng.modules.common.entity.TableTdItem;
import com.cloudinte.zhaosheng.modules.common.service.StaSqlService;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.utils.DateUtils;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.utils.excel.ExportExcel;
import com.thinkgem.jeesite.common.utils.excel.ImportExcel;
import com.thinkgem.jeesite.common.web.BaseController;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 统计模板Controller
 * 
 * @author hhw
 * @version 2018-09-06
 */
@Controller
@RequestMapping(value = "${adminPath}/common/staSql")
public class StaSqlController extends BaseController {
	
	private static final String FUNCTION_NAME_SIMPLE = "统计模板";
	
	@Autowired
	private StaSqlService staSqlService;

	
	@ModelAttribute
	public StaSql get(@RequestParam(required = false) String id) {
		StaSql entity = null;
		if (StringUtils.isNotBlank(id)) {
			entity = staSqlService.get(id);
		}
		if (entity == null) {
			entity = new StaSql();
		}
		return entity;
	}
	
	@RequiresPermissions("common:staSql:view")
	@RequestMapping(value = { "list", "" })
	public String list(StaSql staSql, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<StaSql> page = staSqlService.findPage(new Page<StaSql>(request, response), staSql);
		model.addAttribute("page", page);
		model.addAttribute("ename", "staSql");
		
		setBase64EncodedQueryStringToEntity(request, staSql);
		return "modules/common/staSql/staSqlList";
	}

	/**
	 * 就业中心统计分析模块儿
	 * 
	 * @param staSql
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequiresPermissions("common:staSql:view")
	@RequestMapping(value = "scc")
	public String sccstalist(StaSql staSql, HttpServletRequest request, HttpServletResponse response, Model model) {

		List<StaSql> tjList = staSqlService.findList(staSql);
		model.addAttribute("tjList", tjList);
		model.addAttribute("ename", "sccstasql");
		setBase64EncodedQueryStringToEntity(request, staSql);
		return "modules/common/staSql/tjIndex";

	}

	/**
	 * 统计分析展示
	 * 
	 * @param staSql
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */

	int[] getSubNode(JSONObject jobj, int headlevel, int nowlevel) {
		int[] colrowspan = new int[2];
		int colspan = 0;
		if (jobj.containsKey("subnode")) {
			JSONArray jarr = JSONArray.fromObject(jobj.get("subnode"));
			for (int i = 0; i < jarr.size(); i++) {
				colspan += getSubNode(JSONObject.fromObject(jarr.get(i)), headlevel, nowlevel + 1)[1];
			}
			colrowspan[0] = 1;
			colrowspan[1] = colspan;
		} else {
			colrowspan[0] = headlevel + 1 - nowlevel;//rowspan
			colrowspan[1] = 1;//colspan
		}
		headStrinf[nowlevel - 1].add(new TableTdItem(jobj.get("nodename").toString(), colrowspan[0], colrowspan[1]));
		return colrowspan;
	}

	List<TableTdItem>[] headStrinf = null;
	
	@SuppressWarnings("unchecked")
	@RequiresPermissions("common:staSql:view")
	@RequestMapping(value = "commonsta")
	public String commonsta(StaSql staSql, HttpServletRequest request, HttpServletResponse response, Model model) {

		
		String stasql = staSql.getStasql();
		String sqlwhere = "";
		int headlevel = staSql.getHeadlevel();
		
		//多为表头判断
		if (staSql.getHeadlevel() > 1) {
			headStrinf = new ArrayList[headlevel];
			for (int i = 0; i < headlevel; i++) {
				headStrinf[i] = new ArrayList<TableTdItem>();
			}
			String json = staSql.getHeadinf();
			JSONArray jarr = JSONArray.fromObject(json);
			for (int i = 0; i < jarr.size(); i++) {
				getSubNode(JSONObject.fromObject(jarr.get(i)), headlevel, 1);
			}
			model.addAttribute("headinf", headStrinf);
		} else {
			model.addAttribute("headinf", staSql.getHeadinf().split(","));
		}


		stasql = stasql.replace("{sqlwhere}", sqlwhere);
		
		staSql.setStasql(stasql);
		String[] colinf = staSql.getColinf().split(",");
		List<HashMap<String, Object>> commonsta = staSqlService.commonSta(staSql);
		
		//合并单元格判断
		Boolean[] colmer = new Boolean[colinf.length]; //列数目
		for (int i = 0; i < colmer.length; i++) {
			colmer[i] = false;
		}
		int[] rowspan = new int[commonsta.size()];  //行数目
		
		for (int i = 0; i < rowspan.length; i++) {
			rowspan[i] = 0;
		}
		
		int[][] colrowspan = new int[colinf.length][commonsta.size()];   //二位数组  第几行几列
		for (int i = 0; i < colinf.length; i++) {
			for (int j = 0; j < commonsta.size(); j++) {
				colrowspan[i][j] = 0;
			}
		}

		try {
			
			if (staSql.getMercol() != null && staSql.getMercol() != "") {  //合并列
				String[] mercols = staSql.getMercol().split(",");
				for (int i = 0; i < mercols.length; i++) {
					colmer[StringUtils.toInteger(mercols[i]) - 1] = true;   //将该列设置为true
					int mercol = StringUtils.toInteger(mercols[i]) - 1;    //第几列 从0开始
					int premercol = StringUtils.toInteger(mercols[i]) - 1;    //前一列是什么
					if(i != 0){
						premercol = StringUtils.toInteger(mercols[i-1]) - 1;    //前一列是什么
					}
					
					String precolcellvalue = "";//同一列，前一个单元格内容    与nowcolcellvalue相对应
					String nowcolcellvalue = "";//同一行，前一个单元格内容 与nowcellvalue   相对应
					
					String precellvalue = "";//同一列，前一个单元格内容   与nowcellvalue   相对应
					String nowcellvalue = "";//当前单元格内容
					
					List<Integer> intList = new ArrayList<Integer>();
					if(commonsta.size() == 1){
						intList.add(1);//第几列合并数目，如果只有一条数据，则在该合并的单元格上写1
					}
					int nowrowspan = 0;
					for (int j = 0; j < commonsta.size(); j++) {
						if(i == 0){
							nowcellvalue = (String) commonsta.get(j).get(colinf[mercol]);//当前单元格内容
							
							if (j > 0) {
								nowrowspan++;
								if (j == commonsta.size() - 1) {//最后一行
									if (!nowcellvalue.equals(precellvalue)) {
										intList.add(nowrowspan); //第几列合并数目，
										intList.add(1); //第几列合并数目，如果是最后一行不与前一行合并，则最后一行的单元格写1
									} else {
										intList.add(nowrowspan + 1);
									}
								} else {
									if (!nowcellvalue.equals(precellvalue)) {
										intList.add(nowrowspan);
										nowrowspan = 0;
									}
								}
							}
							precellvalue = nowcellvalue;
						}else{
							nowcolcellvalue = (String) commonsta.get(j).get(colinf[premercol]);//同一行，前一个单元格内容
							nowcellvalue = (String) commonsta.get(j).get(colinf[mercol]);
							if (j > 0) {
								nowrowspan++;
								if(nowcolcellvalue.equals(precolcellvalue)){
									if (j == commonsta.size() - 1) {
										if (!nowcellvalue.equals(precellvalue)) {
											intList.add(nowrowspan);
											intList.add(1);
										} else {
											intList.add(nowrowspan + 1);
										}
									} else {
										if (!nowcellvalue.equals(precellvalue)) {
											intList.add(nowrowspan);
											nowrowspan = 0;
										}
									}
								}else{
									if (j == commonsta.size() - 1) {
										intList.add(nowrowspan);
										intList.add(1);
									}else{
										intList.add(nowrowspan);
										nowrowspan = 0;
									}
									
								}
								
							}
							precellvalue = nowcellvalue;
							precolcellvalue = nowcolcellvalue;
						}
						
					}
					int nowrowindex = 0;
					for (int k = 0; k < intList.size(); k++) {
						colrowspan[mercol][nowrowindex] = intList.get(k);
						rowspan[nowrowindex] = intList.get(k);
						nowrowindex += intList.get(k);
					}
				}
			}
			
			model.addAttribute("isrowspan", true);
			model.addAttribute("colmer", colmer);
			model.addAttribute("rowspan", rowspan);
			model.addAttribute("colrowspan", colrowspan);
			
			/*List<Integer> intList = new ArrayList<Integer>();
			 * if (staSql.getMercol() != null && staSql.getMercol() != "") {
				String[] mercols = staSql.getMercol().split(",");
				int firstrmercol = StringUtils.toInteger(mercols[0]) - 1;
				for (int i = 0; i < mercols.length; i++) {
					colmer[StringUtils.toInteger(mercols[i]) - 1] = true;
				}
				String precellvalue = "";
				String nowcellvalue = "";
				
				int nowrowspan = 0;
				for (int i = 0; i < commonsta.size(); i++) {
					nowcellvalue = (String) commonsta.get(i).get(colinf[firstrmercol]);
					if (i > 0) {
						nowrowspan++;
						if (i == commonsta.size() - 1) {
							if (!nowcellvalue.equals(precellvalue)) {
								intList.add(nowrowspan);
								intList.add(1);
							} else {
								intList.add(nowrowspan + 1);
							}
						} else {
							if (!nowcellvalue.equals(precellvalue)) {
								intList.add(nowrowspan);
								nowrowspan = 0;
							}
						}
					}
					
					precellvalue = nowcellvalue;
					
				}
				int nowrowindex = 0;
				for (int i = 0; i < intList.size(); i++) {
					rowspan[nowrowindex] = intList.get(i);
					nowrowindex += intList.get(i);
				}
				for (int i = 0; i < rowspan.length; i++) {
					
				}
				model.addAttribute("isrowspan", true);
				model.addAttribute("colmer", colmer);
				model.addAttribute("rowspan", rowspan);
			}*/
		} catch (Exception ex) {

		}
		
		model.addAttribute("colinf", colinf);
		model.addAttribute("commonsta", commonsta);
		model.addAttribute("staSql", staSql);
		model.addAttribute("staname", staSql.getStaname());
		model.addAttribute("headlevel", staSql.getHeadlevel());
		
		return "modules/common/staSql/tjShow";
	}
	
	@RequestMapping({ "commonstajson" })
	@ResponseBody
	@RequiresPermissions("common:staSql:view")
	public HashMap<String, Object> commonstajson(StaSql staSql, HttpServletRequest request, HttpServletResponse response, Model model) {
		String stasql = staSql.getStasql();
		String sqlwhere = "";

		  stasql = stasql.replace("{sqlwhere}", sqlwhere);
		  staSql.setStasql(stasql);
		  List<HashMap<String, Object>> commonsta = staSqlService.commonSta(staSql);
		
		  HashMap<String, Object> map = Maps.newHashMap();
		  map.put("commonsta",commonsta);
		 return map;

	}
	
	/**
	 * 导出数据
	 */
	@RequiresPermissions("common:staSql:view")
	@RequestMapping(value = "exportCommonStaFile")
	public String exportCommonStaFile(StaSql staSql, HttpServletRequest request, HttpServletResponse response) {
		String stasql = staSql.getStasql();
		String sqlwhere = "";

		stasql = stasql.replace("{sqlwhere}", sqlwhere);
		staSql.setStasql(stasql);
		String fileName = staSql.getStaname() + DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";
		String title = staSql.getStaname();
		ExportExcel exportExcel = null;
		String[] colinf = staSql.getColinf().split(",");
		//合并单元格判断
		Boolean ismer = false;
		Boolean[] colmer = new Boolean[colinf.length];
		for (int i = 0; i < colmer.length; i++) {
			colmer[i] = false;
		}
		List<HashMap<String, Object>> commonsta = staSqlService.commonSta(staSql);
		int[] rowspan = new int[commonsta.size()];
		for (int i = 0; i < rowspan.length; i++) {
			rowspan[i] = 0;
		}
		
		int[][] colrowspan = new int[colinf.length][commonsta.size()];
		for (int i = 0; i < colinf.length; i++) {
			for (int j = 0; j < commonsta.size(); j++) {
				colrowspan[i][j] = 0;
			}
		}
		
		try {
			
			if (staSql.getMercol() != null && staSql.getMercol() != "") {
				ismer = true;
				String[] mercols = staSql.getMercol().split(",");
				for (int i = 0; i < mercols.length; i++) {
					colmer[StringUtils.toInteger(mercols[i]) - 1] = true;   //将该列设置为true
					int mercol = StringUtils.toInteger(mercols[i]) - 1;    //第几列 从0开始
					int premercol = StringUtils.toInteger(mercols[i]) - 1;    //前一列是什么
					if(i != 0){
						premercol = StringUtils.toInteger(mercols[i-1]) - 1;    //前一列是什么
					}
					
					String precolcellvalue = "";
					String nowcolcellvalue = "";
					
					String precellvalue = "";
					String nowcellvalue = "";
					
					List<Integer> intList = new ArrayList<Integer>();
					if(commonsta.size() == 1){
						intList.add(1);//第几列合并数目，如果只有一条数据，则在该合并的单元格上写1
					}
					int nowrowspan = 0;
					for (int j = 0; j < commonsta.size(); j++) {
						if(i == 0){
							nowcellvalue = (String) commonsta.get(j).get(colinf[mercol]);
							if (j > 0) {
								nowrowspan++;
								if (j == commonsta.size() - 1) {
									if (!nowcellvalue.equals(precellvalue)) {
										intList.add(nowrowspan);
										intList.add(1);
									} else {
										intList.add(nowrowspan + 1);
									}
								} else {
									if (!nowcellvalue.equals(precellvalue)) {
										intList.add(nowrowspan);
										nowrowspan = 0;
									}
								}
							}
							precellvalue = nowcellvalue;
						}else{
							nowcolcellvalue = (String) commonsta.get(j).get(colinf[premercol]);
							nowcellvalue = (String) commonsta.get(j).get(colinf[mercol]);
							if (j > 0) {
								nowrowspan++;
								if(nowcolcellvalue.equals(precolcellvalue)){
									if (j == commonsta.size() - 1) {
										if (!nowcellvalue.equals(precellvalue)) {
											intList.add(nowrowspan);
											intList.add(1);
										} else {
											intList.add(nowrowspan + 1);
										}
									} else {
										if (!nowcellvalue.equals(precellvalue)) {
											intList.add(nowrowspan);
											nowrowspan = 0;
										}
									}
								}else{
									if (j == commonsta.size() - 1) {
										intList.add(nowrowspan);
										intList.add(1);
									}else{
										intList.add(nowrowspan);
										nowrowspan = 0;
									}
								}
								
							}
							precellvalue = nowcellvalue;
							precolcellvalue = nowcolcellvalue;
						}
						
					}
					int nowrowindex = 0;
					for (int k = 0; k < intList.size(); k++) {
						colrowspan[mercol][nowrowindex] = intList.get(k);
						rowspan[nowrowindex] = intList.get(k);
						nowrowindex += intList.get(k);
					}
					
				}
			}
			/*List<Integer> intList = new ArrayList<Integer>();
			*/
			/*if (staSql.getMercol() != null && staSql.getMercol().trim() != "" && staSql.getMercol().trim().split(",").length > 0) {
				ismer = true;
				String[] mercols = staSql.getMercol().split(",");
				int firstrmercol = StringUtils.toInteger(mercols[0]) - 1;
				
				for (int i = 0; i < mercols.length; i++) {
					colmer[StringUtils.toInteger(mercols[i]) - 1] = true;
				}
				String precellvalue = "";
				String nowcellvalue = "";
				
				int nowrowspan = 0;
				for (int i = 0; i < commonsta.size(); i++) {
					nowcellvalue = (String) commonsta.get(i).get(colinf[firstrmercol]);
					if (i > 0) {
						nowrowspan++;
						if (i == commonsta.size() - 1) {
							if (!nowcellvalue.equals(precellvalue)) {
								intList.add(nowrowspan);
								intList.add(1);
							} else {
								intList.add(nowrowspan + 1);
							}
						} else {
							if (!nowcellvalue.equals(precellvalue)) {
								intList.add(nowrowspan);
								nowrowspan = 0;
							}
						}
					}
					
					precellvalue = nowcellvalue;
				}
				int nowrowindex = 0;
				for (int i = 0; i < intList.size(); i++) {
					rowspan[nowrowindex] = intList.get(i);
					nowrowindex += intList.get(i);
				}
			}*/
		} catch (Exception ex) {
		}
		
		if (staSql.getHeadlevel() > 1) {
			exportExcel = new ExportExcel(title, staSql.getHeadinf(), staSql.getHeadlevel(), colinf.length);
			exportExcel.setDataList(commonsta, colinf, ismer, colmer, colrowspan);
		} else {
			exportExcel = new ExportExcel(title, staSql.getHeadinf().split(","));
			exportExcel.setDataList(commonsta, colinf, ismer, colmer, colrowspan);
		}
		try {
			exportExcel.write(response, fileName).dispose();
		} catch (IOException e) {

			e.printStackTrace();
		}
		return null;
	}

	@RequiresPermissions("common:staSql:view")
	@RequestMapping(value = "form")
	public String form(StaSql staSql, Model model) {
		model.addAttribute("staSql", staSql);
		model.addAttribute("ename", "staSql");
		return "modules/common/staSql/staSqlForm";
	}
	
	@RequiresPermissions("common:staSql:edit")
	@RequestMapping(value = "save")
	public String save(StaSql staSql, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, staSql)) {
			return form(staSql, model);
		}
		addMessage(redirectAttributes, StringUtils.isBlank(staSql.getId()) ? "保存" + FUNCTION_NAME_SIMPLE + "成功" : "修改" + FUNCTION_NAME_SIMPLE + "成功");
		staSqlService.save(staSql);
		return redirectToList(staSql);
	}

	private String redirectToList(StaSql staSql) {
		return "redirect:" + adminPath + "/common/staSql/?repage&" + (staSql == null ? "" : getBase64DecodedQueryStringFromEntity(staSql));
	}
	
	@RequiresPermissions("common:staSql:edit")
	@RequestMapping(value = "disable")
	public String disable(StaSql staSql, RedirectAttributes redirectAttributes) {
		staSqlService.disable(staSql);
		addMessage(redirectAttributes, "禁用" + FUNCTION_NAME_SIMPLE + "成功");
		return redirectToList(staSql);
	}
	
	@RequiresPermissions("common:staSql:delete")
	@RequestMapping(value = "delete")
	public String delete(StaSql staSql, RedirectAttributes redirectAttributes) {
		staSqlService.delete(staSql);
		addMessage(redirectAttributes, "删除" + FUNCTION_NAME_SIMPLE + "成功");
		return redirectToList(staSql);
	}
	
	@RequiresPermissions("common:staSql:delete")
	@RequestMapping(value = "deleteBatch")
	public String deleteBatch(StaSql staSql, RedirectAttributes redirectAttributes) {
		staSqlService.deleteByIds(staSql);
		addMessage(redirectAttributes, "批量删除" + FUNCTION_NAME_SIMPLE + "成功");
		return redirectToList(staSql);
	}
	
	/**
	 * 导出数据
	 */
	@RequiresPermissions("common:staSql:edit")
	@RequestMapping(value = "export", method = RequestMethod.POST)
	public String exportFile(StaSql staSql, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
			String fileName = FUNCTION_NAME_SIMPLE + DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";
			new ExportExcel(FUNCTION_NAME_SIMPLE, StaSql.class).setDataList(staSqlService.findList(staSql)).write(response, fileName).dispose();
			return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出" + FUNCTION_NAME_SIMPLE + "失败！失败信息：" + e.getMessage());
			return redirectToList(staSql);
		}
	}
	
	/**
	 * 下载导入数据模板
	 */
	@RequiresPermissions("common:staSql:view")
	@RequestMapping(value = "import/template")
	public String importFileTemplate(StaSql staSql, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
			String fileName = FUNCTION_NAME_SIMPLE + "导入模板.xlsx";
			List<StaSql> list = staSqlService.findPage(new Page<StaSql>(1, 5), new StaSql()).getList();
			if (list == null) {
				list = Lists.newArrayList();
			}
			if (list.size() < 1) {
				list.add(new StaSql());
			}
			new ExportExcel(FUNCTION_NAME_SIMPLE, StaSql.class).setDataList(list).write(response, fileName).dispose();
			return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息：" + e.getMessage());
			return redirectToList(staSql);
		}
	}
	
	/**
	 * 导入数据<br>
	 */
	@RequiresPermissions("common:staSql:edit")
	@RequestMapping(value = "import", method = RequestMethod.POST)
	public String importFile(StaSql staSql, MultipartFile file, RedirectAttributes redirectAttributes) {
		if (Global.isDemoMode()) {
			addMessage(redirectAttributes, "演示模式，不允许操作！");
			return "redirect:" + adminPath + "/common/staSql/?repage&" + StringUtils.trimToEmpty(getBase64DecodedQueryStringFromEntity(staSql));
		}
		try {
			long beginTime = System.currentTimeMillis();//1、开始时间
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<StaSql> list = ei.getDataList(StaSql.class);
			List<StaSql> insertList = new ArrayList<StaSql>();
			List<StaSql> subList = new ArrayList<StaSql>();
			int batchinsertSize = 500;
			int batchinsertCount = list.size() / batchinsertSize + (list.size() % batchinsertSize == 0 ? 0 : 1);
			int addNum = 0;
			int toIndex = 0;
			for (int i = 0; i < batchinsertCount; i++) {
				insertList = new ArrayList<StaSql>();
				toIndex = (i + 1) * batchinsertSize;
				if (toIndex > list.size()) {
					toIndex = list.size();
				}
				subList = list.subList(i * batchinsertSize, toIndex);
				for (StaSql zsJh : subList) {
					zsJh.preInsert();
					insertList.add(zsJh);
				}
				if (insertList != null && insertList.size() > 0) {
					System.out.println("insertList size is :" + insertList.size());
					staSqlService.batchInsertUpdate(insertList);
					addNum += insertList.size();
				}
			}
			long endTime = System.currentTimeMillis(); //2、结束时间
			addMessage(redirectAttributes, "执行时间" + DateUtils.formatDateTime(endTime - beginTime) + ",导入 " + addNum + "条" + FUNCTION_NAME_SIMPLE + "信息," + failureMsg);
			
			System.out.println("执行时间：" + DateUtils.formatDateTime(endTime - beginTime));
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入" + FUNCTION_NAME_SIMPLE + "信息失败！失败信息：" + e.getMessage());
		}
		return redirectToList(staSql);
	}
	
}