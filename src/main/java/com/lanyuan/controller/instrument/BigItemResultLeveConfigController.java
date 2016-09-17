package com.lanyuan.controller.instrument;


import com.lanyuan.annotation.SystemLog;
import com.lanyuan.controller.index.BaseController;
import com.lanyuan.entity.BigItemResultLeveConfigFormMap;
import com.lanyuan.entity.CheckBigItemFormMap;
import com.lanyuan.entity.BigItemResultLeveConfigFormMap;
import com.lanyuan.mapper.BigItemResultLeveConfigMapper;
import com.lanyuan.mapper.CheckBigItemMapper;
import com.lanyuan.mapper.DuanbanConfigMapper;
import com.lanyuan.plugin.PageView;
import com.lanyuan.util.Common;
import com.lanyuan.vo.Grid;
import com.lanyuan.vo.PageFilter;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Controller
@RequestMapping("/instrument/bigitemresultleveconfig/")
public class BigItemResultLeveConfigController extends BaseController {



	@Inject
	private BigItemResultLeveConfigMapper bigItemResultLeveConfigMapper;

	@Inject
	private CheckBigItemMapper checkBigItemMapper;


	@RequestMapping("list")
	public String listUI(Model model,String checkItemId,String checkItemType) throws Exception {
		if(StringUtils.isBlank(checkItemId)){
			checkItemId = "0";
		}
		model.addAttribute("checkItemId",checkItemId);
		model.addAttribute("checkItemType",checkItemType);
		CheckBigItemFormMap checkBigItemFormMap = checkBigItemMapper.findbyFrist("id",checkItemId,CheckBigItemFormMap.class);
		model.addAttribute("checkBigItemFormMap",checkBigItemFormMap);
		return Common.BACKGROUND_PATH + "/instrument/bigitemresultleveconfig/list";
	}

	@RequestMapping("addPage")
	public String addPage(Model model,String checkItemId,String checkItemType) throws Exception {
		if(StringUtils.isBlank(checkItemId)){
			checkItemId = "0";
		}
		model.addAttribute("checkItemId",checkItemId);
		model.addAttribute("checkItemType",checkItemType);
		return Common.BACKGROUND_PATH + "/instrument/bigitemresultleveconfig/add";
	}

	@RequestMapping("editPage")
	public String editPage(Model model,String id) throws Exception {
		BigItemResultLeveConfigFormMap bigItemResultLeveConfigFormMap = bigItemResultLeveConfigMapper.findbyFrist("id",id,BigItemResultLeveConfigFormMap.class);
		model.addAttribute("bigItemResultLeveConfigFormMap",bigItemResultLeveConfigFormMap);
		return Common.BACKGROUND_PATH + "/instrument/bigitemresultleveconfig/edit";
	}




	@ResponseBody
	@RequestMapping("dataGrid")
	@SystemLog(module="短板管理",methods="短板列表")//凡需要处理业务逻辑的.都需要记录操作日志
	@Transactional(readOnly=false)//需要事务操作必须加入此注解
	public Grid dataGrid(PageFilter ph)throws Exception {
		Grid grid = new Grid();
		BigItemResultLeveConfigFormMap bigItemResultLeveConfigFormMap = getFormMap(BigItemResultLeveConfigFormMap.class);
		bigItemResultLeveConfigFormMap.put("orderby",ph.getSort()+" "+ph.getOrder());
		bigItemResultLeveConfigFormMap=toFormMap(bigItemResultLeveConfigFormMap,  String.valueOf(ph.getPage()), String.valueOf(ph.getRows()),bigItemResultLeveConfigFormMap.getStr("orderby"));
		List<BigItemResultLeveConfigFormMap> userFormMapList =bigItemResultLeveConfigMapper.findPage(bigItemResultLeveConfigFormMap);
		if(CollectionUtils.isNotEmpty(userFormMapList)){
			grid.setRows(userFormMapList);
		}
		PageView pageView = (PageView) bigItemResultLeveConfigFormMap.get("paging");
		grid.setTotal(pageView.getRowCount());
		return grid;

	}




	@ResponseBody
	@RequestMapping("add")
	@SystemLog(module="短板管理",methods="新增用户")//凡需要处理业务逻辑的.都需要记录操作日志
	@Transactional(readOnly=false)//需要事务操作必须加入此注解
	public Map<String,Object> add(){
		Map<String,Object> retMap = new HashMap<String, Object>();
		retMap.put("status",0);
		try {
			BigItemResultLeveConfigFormMap bigItemResultLeveConfigFormMap = getFormMap(BigItemResultLeveConfigFormMap.class);


			bigItemResultLeveConfigMapper.addEntity(bigItemResultLeveConfigFormMap);
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
	@SystemLog(module="短板管理",methods="用户管理-删除用户")//凡需要处理业务逻辑的.都需要记录操作日志
	public Map<String,Object>  delete() throws Exception {
		Map<String,Object> retMap = new HashMap<String, Object>();
		retMap.put("status",0);
		try {
			BigItemResultLeveConfigFormMap bigItemResultLeveConfigFormMap = getFormMap(BigItemResultLeveConfigFormMap.class);
			bigItemResultLeveConfigMapper.deleteByNames(bigItemResultLeveConfigFormMap);
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
	@SystemLog(module="短板管理",methods="修改权限组")//凡需要处理业务逻辑的.都需要记录操作日志
	@Transactional(readOnly=false)//需要事务操作必须加入此注解
	public Map<String,Object>  update(Model model) {
		Map<String,Object> retMap = new HashMap<String, Object>();
		retMap.put("status",0);
		try {
			BigItemResultLeveConfigFormMap bigItemResultLeveConfigFormMap = getFormMap(BigItemResultLeveConfigFormMap.class);
			bigItemResultLeveConfigMapper.editEntity(bigItemResultLeveConfigFormMap);
			retMap.put("msg","修改成功");
			retMap.put("status",1);
		}catch (Exception ex){
			retMap.put("msg",ex.getMessage());
			ex.printStackTrace();
		}
		return retMap;
	}




}