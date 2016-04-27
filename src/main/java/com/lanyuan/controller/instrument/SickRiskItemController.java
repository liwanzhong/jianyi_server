package com.lanyuan.controller.instrument;


import com.lanyuan.annotation.SystemLog;
import com.lanyuan.controller.index.BaseController;
import com.lanyuan.entity.SickRiskItemFormMap;
import com.lanyuan.mapper.SickRiskItemMapper;
import com.lanyuan.plugin.PageView;
import com.lanyuan.util.Common;
import com.lanyuan.vo.Grid;
import com.lanyuan.vo.PageFilter;
import com.lanyuan.vo.Tree;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.inject.Inject;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 *
 * @author lanyuan 2014-11-19
 * @Email: mmm333zzz520@163.com
 * @version 3.0v
 */
@Controller
@RequestMapping("/instrument/sickRiskItem/")
public class SickRiskItemController extends BaseController {



	@Inject
	private SickRiskItemMapper sickRiskItemMapper;



	private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	@RequestMapping("list")
	public String listUI(Model model,String checkItemId,Integer checkItemType) throws Exception {
		model.addAttribute("checkItemId",checkItemId);
		model.addAttribute("checkItemType",checkItemType);
		return Common.BACKGROUND_PATH + "/instrument/sickriskitem/list";
	}

	@RequestMapping("addPage")
	public String addPage(Model model,String checkItemId,Integer checkItemType) throws Exception {
		model.addAttribute("checkItemId",checkItemId);
		model.addAttribute("checkItemType",checkItemType);
		return Common.BACKGROUND_PATH + "/instrument/sickriskitem/add";
	}

	@RequestMapping("editPage")
	public String editPage(Model model,String id) throws Exception {
		SickRiskItemFormMap sickRiskItemFormMap = sickRiskItemMapper.findbyFrist("id",id,SickRiskItemFormMap.class);
		model.addAttribute("sickRiskItemFormMap",sickRiskItemFormMap);
		return Common.BACKGROUND_PATH + "/instrument/sickriskitem/edit";
	}




	@ResponseBody
	@RequestMapping("dataGrid")
	@SystemLog(module="用户管理",methods="加载用户列表")//凡需要处理业务逻辑的.都需要记录操作日志
	@Transactional(readOnly=false)//需要事务操作必须加入此注解
	public Grid dataGrid(PageFilter ph)throws Exception {
		Grid grid = new Grid();
		SickRiskItemFormMap sickRiskItemFormMap = getFormMap(SickRiskItemFormMap.class);
		sickRiskItemFormMap.put("orderby",ph.getSort()+" "+ph.getOrder());
		sickRiskItemFormMap=toFormMap(sickRiskItemFormMap,  String.valueOf(ph.getPage()), String.valueOf(ph.getRows()),sickRiskItemFormMap.getStr("orderby"));
		List<SickRiskItemFormMap> userFormMapList =sickRiskItemMapper.findEnterprisePage(sickRiskItemFormMap);
		if(CollectionUtils.isNotEmpty(userFormMapList)){
			grid.setRows(userFormMapList);
		}
		PageView pageView = (PageView) sickRiskItemFormMap.get("paging");
		grid.setTotal(pageView.getRowCount());
		return grid;

	}




	@ResponseBody
	@RequestMapping("add")
	@SystemLog(module="用户管理",methods="新增用户")//凡需要处理业务逻辑的.都需要记录操作日志
	@Transactional(readOnly=false)//需要事务操作必须加入此注解
	public Map<String,Object> add(){
		Map<String,Object> retMap = new HashMap<String, Object>();
		retMap.put("status",0);
		try {
			SickRiskItemFormMap sickRiskItemFormMap = getFormMap(SickRiskItemFormMap.class);
			sickRiskItemFormMap.put("update_time",dateFormat.format(new Date()));
			sickRiskItemMapper.addEntity(sickRiskItemFormMap);
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
	@SystemLog(module="系统管理",methods="用户管理-删除用户")//凡需要处理业务逻辑的.都需要记录操作日志
	public Map<String,Object>  delete() throws Exception {
		Map<String,Object> retMap = new HashMap<String, Object>();
		retMap.put("status",0);
		try {
			SickRiskItemFormMap sickRiskItemFormMap = getFormMap(SickRiskItemFormMap.class);
			sickRiskItemMapper.deleteByNames(sickRiskItemFormMap);
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
	@SystemLog(module="权限组管理",methods="修改权限组")//凡需要处理业务逻辑的.都需要记录操作日志
	@Transactional(readOnly=false)//需要事务操作必须加入此注解
	public Map<String,Object>  update(Model model) {
		Map<String,Object> retMap = new HashMap<String, Object>();
		retMap.put("status",0);
		try {
			SickRiskItemFormMap sickRiskItemFormMap = getFormMap(SickRiskItemFormMap.class);
			sickRiskItemMapper.editEntity(sickRiskItemFormMap);
			retMap.put("msg","修改成功");
			retMap.put("status",1);
		}catch (Exception ex){
			retMap.put("msg",ex.getMessage());
			ex.printStackTrace();
		}
		return retMap;
	}


}