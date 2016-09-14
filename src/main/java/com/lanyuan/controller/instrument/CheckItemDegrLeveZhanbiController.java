package com.lanyuan.controller.instrument;


import com.lanyuan.annotation.SystemLog;
import com.lanyuan.controller.index.BaseController;
import com.lanyuan.entity.CheckItemDegrLeveZhanbiFormMap;
import com.lanyuan.mapper.CheckItemDegrLeveZhanbiMapper;
import com.lanyuan.plugin.PageView;
import com.lanyuan.util.Common;
import com.lanyuan.vo.Grid;
import com.lanyuan.vo.PageFilter;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author lanyuan 2014-11-19
 * @Email: mmm333zzz520@163.com
 * @version 3.0v
 */
@Controller
@RequestMapping("/instrument/leveZhanbi/")
public class CheckItemDegrLeveZhanbiController extends BaseController {



	@Inject
	private CheckItemDegrLeveZhanbiMapper checkItemDegrLeveZhanbiMapper;




	@RequestMapping("list")
	public String listUI(Model model,String checkItemId,Integer checkItemType) throws Exception {
		model.addAttribute("checkItemId",checkItemId);
		model.addAttribute("checkItemType",checkItemType);
		return Common.BACKGROUND_PATH + "/instrument/levezhanbi/list";
	}

	@RequestMapping("addPage")
	public String addPage(Model model,String checkItemId,Integer checkItemType) throws Exception {
		model.addAttribute("checkItemId",checkItemId);
		model.addAttribute("checkItemType",checkItemType);
		return Common.BACKGROUND_PATH + "/instrument/levezhanbi/add";
	}

	@RequestMapping("editPage")
	public String editPage(Model model,String id) throws Exception {
		CheckItemDegrLeveZhanbiFormMap checkItemDegrLeveZhanbiFormMap = checkItemDegrLeveZhanbiMapper.findbyFrist("id",id,CheckItemDegrLeveZhanbiFormMap.class);
		model.addAttribute("checkItemDegrLeveZhanbiFormMap",checkItemDegrLeveZhanbiFormMap);
		return Common.BACKGROUND_PATH + "/instrument/levezhanbi/edit";
	}




	@ResponseBody
	@RequestMapping("dataGrid")
	@SystemLog(module="占比权重配置管理",methods="占比列表")//凡需要处理业务逻辑的.都需要记录操作日志
	@Transactional(readOnly=false)//需要事务操作必须加入此注解
	public Grid dataGrid(PageFilter ph)throws Exception {
		Grid grid = new Grid();
		CheckItemDegrLeveZhanbiFormMap checkItemDegrLeveZhanbiFormMap = getFormMap(CheckItemDegrLeveZhanbiFormMap.class);
		checkItemDegrLeveZhanbiFormMap.put("orderby",ph.getSort()+" "+ph.getOrder());
		checkItemDegrLeveZhanbiFormMap=toFormMap(checkItemDegrLeveZhanbiFormMap,  String.valueOf(ph.getPage()), String.valueOf(ph.getRows()),checkItemDegrLeveZhanbiFormMap.getStr("orderby"));
		List<CheckItemDegrLeveZhanbiFormMap> userFormMapList =checkItemDegrLeveZhanbiMapper.findPage(checkItemDegrLeveZhanbiFormMap);
		if(CollectionUtils.isNotEmpty(userFormMapList)){
			grid.setRows(userFormMapList);
		}
		PageView pageView = (PageView) checkItemDegrLeveZhanbiFormMap.get("paging");
		grid.setTotal(pageView.getRowCount());
		return grid;

	}




	@ResponseBody
	@RequestMapping("add")
	@SystemLog(module="占比权重配置管理",methods="新增占比")//凡需要处理业务逻辑的.都需要记录操作日志
	@Transactional(readOnly=false)//需要事务操作必须加入此注解
	public Map<String,Object> add(){
		Map<String,Object> retMap = new HashMap<String, Object>();
		retMap.put("status",0);
		try {
			CheckItemDegrLeveZhanbiFormMap checkItemDegrLeveZhanbiFormMap = getFormMap(CheckItemDegrLeveZhanbiFormMap.class);
			checkItemDegrLeveZhanbiMapper.addEntity(checkItemDegrLeveZhanbiFormMap);
			retMap.put("msg","添加成功");
			retMap.put("status",1);
		}catch (Exception ex){
			retMap.put("msg",ex.getMessage());
		}
		return retMap;
	}



	@ResponseBody
	@RequestMapping("delete")
	@Transactional(readOnly=false)//需要事务操作必须加入此注解
	@SystemLog(module="占比权重配置管理",methods="删除占比")//凡需要处理业务逻辑的.都需要记录操作日志
	public Map<String,Object>  delete() throws Exception {
		Map<String,Object> retMap = new HashMap<String, Object>();
		retMap.put("status",0);
		try {
			CheckItemDegrLeveZhanbiFormMap checkItemDegrLeveZhanbiFormMap = getFormMap(CheckItemDegrLeveZhanbiFormMap.class);
			checkItemDegrLeveZhanbiMapper.deleteByNames(checkItemDegrLeveZhanbiFormMap);
			retMap.put("msg","删除成功");
			retMap.put("status",1);
		}catch (Exception ex){
			ex.printStackTrace();
			retMap.put("msg",ex.getMessage());
		}
		return retMap;
	}





	@ResponseBody
	@RequestMapping("update")
	@SystemLog(module="占比权重配置管理",methods="修改占比")//凡需要处理业务逻辑的.都需要记录操作日志
	@Transactional(readOnly=false)//需要事务操作必须加入此注解
	public Map<String,Object>  update(Model model) {
		Map<String,Object> retMap = new HashMap<String, Object>();
		retMap.put("status",0);
		try {
			CheckItemDegrLeveZhanbiFormMap checkItemDegrLeveZhanbiFormMap = getFormMap(CheckItemDegrLeveZhanbiFormMap.class);
			checkItemDegrLeveZhanbiMapper.editEntity(checkItemDegrLeveZhanbiFormMap);
			retMap.put("msg","修改成功");
			retMap.put("status",1);
		}catch (Exception ex){
			retMap.put("msg",ex.getMessage());
			ex.printStackTrace();
		}
		return retMap;
	}





}