package com.lanyuan.controller.instrument;


import com.lanyuan.annotation.SystemLog;
import com.lanyuan.controller.index.BaseController;
import com.lanyuan.entity.BmiNormalConfigFormMap;
import com.lanyuan.entity.BmiNormalConfigFormMap;
import com.lanyuan.mapper.BmiNormalConfigMapper;
import com.lanyuan.mapper.ZongpingLeveDescConfigMapper;
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
@RequestMapping("/instrument/bmi_normal_config/")
public class BmiNormalConfigController extends BaseController {
	@Inject
	private BmiNormalConfigMapper bmiNormalConfigMapper;




	@RequestMapping("list")
	public String listUI(Model model) throws Exception {
		return Common.BACKGROUND_PATH + "/instrument/bminormalconfig/list";
	}

	@RequestMapping("addPage")
	public String addPage(Model model) throws Exception {
		return Common.BACKGROUND_PATH + "/instrument/bminormalconfig/add";
	}

	@RequestMapping("editPage")
	public String editPage(Model model,String id) throws Exception {
		BmiNormalConfigFormMap bmiNormalConfigFormMap = bmiNormalConfigMapper.findbyFrist("id",id,BmiNormalConfigFormMap.class);
		model.addAttribute("bmiNormalConfigFormMap",bmiNormalConfigFormMap);
		return Common.BACKGROUND_PATH + "/instrument/bminormalconfig/edit";
	}




	@ResponseBody
	@RequestMapping("dataGrid")
	@SystemLog(module="用户管理",methods="加载用户列表")//凡需要处理业务逻辑的.都需要记录操作日志
	@Transactional(readOnly=false)//需要事务操作必须加入此注解
	public Grid dataGrid(PageFilter ph)throws Exception {
		Grid grid = new Grid();
		BmiNormalConfigFormMap bmiNormalConfigFormMap = getFormMap(BmiNormalConfigFormMap.class);
		bmiNormalConfigFormMap.put("orderby",ph.getSort()+" "+ph.getOrder());
		bmiNormalConfigFormMap=toFormMap(bmiNormalConfigFormMap,  String.valueOf(ph.getPage()), String.valueOf(ph.getRows()),bmiNormalConfigFormMap.getStr("orderby"));
		List<BmiNormalConfigFormMap> userFormMapList =bmiNormalConfigMapper.findPage(bmiNormalConfigFormMap);
		if(CollectionUtils.isNotEmpty(userFormMapList)){
			grid.setRows(userFormMapList);
		}
		PageView pageView = (PageView) bmiNormalConfigFormMap.get("paging");
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
			BmiNormalConfigFormMap bmiNormalConfigFormMap = getFormMap(BmiNormalConfigFormMap.class);
			bmiNormalConfigMapper.addEntity(bmiNormalConfigFormMap);
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
			BmiNormalConfigFormMap bmiNormalConfigFormMap = getFormMap(BmiNormalConfigFormMap.class);
			bmiNormalConfigMapper.deleteByNames(bmiNormalConfigFormMap);
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
			BmiNormalConfigFormMap bmiNormalConfigFormMap = getFormMap(BmiNormalConfigFormMap.class);
			bmiNormalConfigMapper.editEntity(bmiNormalConfigFormMap);
			retMap.put("msg","修改成功");
			retMap.put("status",1);
		}catch (Exception ex){
			retMap.put("msg",ex.getMessage());
			ex.printStackTrace();
		}
		return retMap;
	}






}