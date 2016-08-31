package com.lanyuan.controller.instrument;


import com.lanyuan.annotation.SystemLog;
import com.lanyuan.controller.index.BaseController;
import com.lanyuan.entity.CheckBigItemFormMap;
import com.lanyuan.entity.DuanbanConfigFormMap;
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

/**
 *
 * @author lanyuan 2014-11-19
 * @Email: mmm333zzz520@163.com
 * @version 3.0v
 */
@Controller
@RequestMapping("/instrument/duanbanconfig/")
public class DuanbanConfigController extends BaseController {



	@Inject
	private DuanbanConfigMapper duanbanConfigMapper;

	@Inject
	private CheckBigItemMapper checkBigItemMapper;


	@RequestMapping("list")
	public String listUI(Model model,String checkItemId) throws Exception {
		if(StringUtils.isBlank(checkItemId)){
			checkItemId = "0";
		}
		model.addAttribute("checkItemId",checkItemId);
		CheckBigItemFormMap checkBigItemFormMap = checkBigItemMapper.findbyFrist("id",checkItemId,CheckBigItemFormMap.class);
		if(checkBigItemFormMap == null || checkBigItemFormMap.get("id")==null){
			checkBigItemFormMap = new CheckBigItemFormMap();
			checkBigItemFormMap.put("name","短板总评");
		}
		model.addAttribute("checkBigItemFormMap",checkBigItemFormMap);
		return Common.BACKGROUND_PATH + "/instrument/duanbanconfig/list";
	}

	@RequestMapping("addPage")
	public String addPage(Model model,String checkItemId) throws Exception {
		if(StringUtils.isBlank(checkItemId)){
			checkItemId = "0";
		}
		model.addAttribute("checkItemId",checkItemId);
		return Common.BACKGROUND_PATH + "/instrument/duanbanconfig/add";
	}

	@RequestMapping("editPage")
	public String editPage(Model model,String id) throws Exception {
		DuanbanConfigFormMap duanbanConfigFormMap = duanbanConfigMapper.findbyFrist("id",id,DuanbanConfigFormMap.class);
		model.addAttribute("duanbanConfigFormMap",duanbanConfigFormMap);
		return Common.BACKGROUND_PATH + "/instrument/duanbanconfig/edit";
	}




	@ResponseBody
	@RequestMapping("dataGrid")
	@SystemLog(module="短板管理",methods="短板列表")//凡需要处理业务逻辑的.都需要记录操作日志
	@Transactional(readOnly=false)//需要事务操作必须加入此注解
	public Grid dataGrid(PageFilter ph)throws Exception {
		Grid grid = new Grid();
		DuanbanConfigFormMap duanbanConfigFormMap = getFormMap(DuanbanConfigFormMap.class);
		duanbanConfigFormMap.put("orderby",ph.getSort()+" "+ph.getOrder());
		duanbanConfigFormMap=toFormMap(duanbanConfigFormMap,  String.valueOf(ph.getPage()), String.valueOf(ph.getRows()),duanbanConfigFormMap.getStr("orderby"));
		List<DuanbanConfigFormMap> userFormMapList =duanbanConfigMapper.findDuanbanConfigByBigItem(duanbanConfigFormMap);
		if(CollectionUtils.isNotEmpty(userFormMapList)){
			grid.setRows(userFormMapList);
		}
		PageView pageView = (PageView) duanbanConfigFormMap.get("paging");
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
			DuanbanConfigFormMap duanbanConfigFormMap = getFormMap(DuanbanConfigFormMap.class);
			if(duanbanConfigFormMap.get("big_item_id")==null || Long.parseLong(duanbanConfigFormMap.get("big_item_id").toString())==0){
				duanbanConfigFormMap.put("is_zongping",1);
			}else{
				duanbanConfigFormMap.put("is_zongping",0);
			}

			duanbanConfigMapper.addEntity(duanbanConfigFormMap);
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
			DuanbanConfigFormMap duanbanConfigFormMap = getFormMap(DuanbanConfigFormMap.class);
			duanbanConfigMapper.deleteByNames(duanbanConfigFormMap);
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
			DuanbanConfigFormMap duanbanConfigFormMap = getFormMap(DuanbanConfigFormMap.class);
			duanbanConfigMapper.editEntity(duanbanConfigFormMap);
			retMap.put("msg","修改成功");
			retMap.put("status",1);
		}catch (Exception ex){
			retMap.put("msg",ex.getMessage());
			ex.printStackTrace();
		}
		return retMap;
	}




}