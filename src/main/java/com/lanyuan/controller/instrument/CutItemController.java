package com.lanyuan.controller.instrument;


import com.lanyuan.annotation.SystemLog;
import com.lanyuan.controller.index.BaseController;
import com.lanyuan.entity.CheckBigItemFormMap;
import com.lanyuan.entity.CheckSmallItemFormMap;
import com.lanyuan.entity.CutItemFormMap;
import com.lanyuan.entity.CutItemRefsmallitemConfigFormMap;
import com.lanyuan.mapper.CheckBigItemMapper;
import com.lanyuan.mapper.CheckSmallItemMapper;
import com.lanyuan.mapper.CutItemMapper;
import com.lanyuan.mapper.CutItemRefsmallitemConfigMapper;
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
import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 *
 * @author lanyuan 2014-11-19
 * @Email: mmm333zzz520@163.com
 * @version 3.0v
 */
@Controller
@RequestMapping("/instrument/cut_item/")
public class CutItemController extends BaseController {
	@Inject
	private CutItemMapper cutItemMapper;


	@Inject
	private CheckBigItemMapper checkBigItemMapper;


	@Inject
	private CheckSmallItemMapper checkSmallItemMapper;


	@Inject
	private CutItemRefsmallitemConfigMapper cutItemRefsmallitemConfigMapper;




	private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	@RequestMapping("list")
	public String listUI(Model model) throws Exception {
		return Common.BACKGROUND_PATH + "/instrument/cutitem/list";
	}

	@RequestMapping("addPage")
	public String addPage(Model model) throws Exception {
		return Common.BACKGROUND_PATH + "/instrument/cutitem/add";
	}

	@RequestMapping("editPage")
	public String editPage(Model model,String id) throws Exception {
		CutItemFormMap cutItemFormMap = cutItemMapper.findbyFrist("id",id,CutItemFormMap.class);
		model.addAttribute("cutItemFormMap",cutItemFormMap);
		return Common.BACKGROUND_PATH + "/instrument/cutitem/edit";
	}


	@RequestMapping("configRefPage")
	public String grantPage(Model model)throws Exception {
		CutItemFormMap cutItemFormMap = getFormMap(CutItemFormMap.class);
		cutItemFormMap = cutItemMapper.findbyFrist("id",cutItemFormMap.get("id").toString(),CutItemFormMap.class);
		model.addAttribute("cutItemFormMap",cutItemFormMap);
		//todo 加载检测大小项目
		List<CheckBigItemFormMap> checkBigItemFormMapList = checkBigItemMapper.findByNames(getFormMap(CheckBigItemFormMap.class));

		List<CheckSmallItemFormMap> checkSmallItemFormMapList = checkSmallItemMapper.findByNames(getFormMap(CheckSmallItemFormMap.class));

		model.addAttribute("checkBigItemFormMapList",checkBigItemFormMapList);

		model.addAttribute("checkSmallItemFormMapList",checkSmallItemFormMapList);


		CutItemRefsmallitemConfigFormMap cutItemRefsmallitemConfigFormMap = getFormMap(CutItemRefsmallitemConfigFormMap.class);
		cutItemRefsmallitemConfigFormMap.put("cut_item_id",cutItemFormMap.getLong("id"));
		List<CutItemRefsmallitemConfigFormMap> cutItemRefsmallitemConfigFormMapList = cutItemRefsmallitemConfigMapper.findByNames(cutItemRefsmallitemConfigFormMap);
		model.addAttribute("cutItemRefsmallitemConfigFormMapList",cutItemRefsmallitemConfigFormMapList);
		return  Common.BACKGROUND_PATH + "/instrument/cutitem/configRef";
	}


	@ResponseBody
	@RequestMapping("config")
	@SystemLog(module="权限管理",methods="加载角色权限列表")//凡需要处理业务逻辑的.都需要记录操作日志
	@Transactional(readOnly=false)//需要事务操作必须加入此注解
	public Map<String,Object> get(HttpServletRequest request, Long cutItemId)throws Exception {
		Map<String,Object> retMap = new HashMap<String, Object>();
		retMap.put("status",0);
		try{
			// 删除原来的配置
			cutItemRefsmallitemConfigMapper.deleteByAttribute("cut_item_id",cutItemId+"", CutItemRefsmallitemConfigFormMap.class);
			//新增配置
			List<CutItemRefsmallitemConfigFormMap> cutItemRefsmallitemConfigFormMapList = new ArrayList<CutItemRefsmallitemConfigFormMap>();
			for(String item:request.getParameterValues("refItem")){
				CutItemRefsmallitemConfigFormMap cutItemRefsmallitemConfigFormMap = getFormMap(CutItemRefsmallitemConfigFormMap.class);
				cutItemRefsmallitemConfigFormMap.put("cut_item_id",cutItemId);
				cutItemRefsmallitemConfigFormMap.put("ref_checksmall_id",item);

				cutItemRefsmallitemConfigFormMapList.add(cutItemRefsmallitemConfigFormMap);
			}
			cutItemRefsmallitemConfigMapper.batchSave(cutItemRefsmallitemConfigFormMapList);
			retMap.put("status",1);
			retMap.put("msg","success");
		}catch (Exception ex){
			retMap.put("msg",ex.getMessage());
		}

		return retMap;

	}




	@ResponseBody
	@RequestMapping("dataGrid")
	@SystemLog(module="用户管理",methods="加载用户列表")//凡需要处理业务逻辑的.都需要记录操作日志
	@Transactional(readOnly=false)//需要事务操作必须加入此注解
	public Grid dataGrid(PageFilter ph)throws Exception {
		Grid grid = new Grid();
		CutItemFormMap cutItemFormMap = getFormMap(CutItemFormMap.class);
		cutItemFormMap.put("orderby",ph.getSort()+" "+ph.getOrder());
		cutItemFormMap=toFormMap(cutItemFormMap,  String.valueOf(ph.getPage()), String.valueOf(ph.getRows()),cutItemFormMap.getStr("orderby"));
		List<CutItemFormMap> userFormMapList =cutItemMapper.findEnterprisePage(cutItemFormMap);
		if(CollectionUtils.isNotEmpty(userFormMapList)){
			grid.setRows(userFormMapList);
		}
		PageView pageView = (PageView) cutItemFormMap.get("paging");
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
			CutItemFormMap cutItemFormMap = getFormMap(CutItemFormMap.class);
			cutItemFormMap.put("create_time",dateFormat.format(new Date()));
			cutItemMapper.addEntity(cutItemFormMap);
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
			CutItemFormMap cutItemFormMap = getFormMap(CutItemFormMap.class);
			cutItemMapper.deleteByNames(cutItemFormMap);
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
			CutItemFormMap cutItemFormMap = getFormMap(CutItemFormMap.class);
			cutItemMapper.editEntity(cutItemFormMap);
			retMap.put("msg","修改成功");
			retMap.put("status",1);
		}catch (Exception ex){
			retMap.put("msg",ex.getMessage());
			ex.printStackTrace();
		}
		return retMap;
	}


	@ResponseBody
	@RequestMapping("loadCutItems")
	@SystemLog(module="权限组管理",methods="修改权限组")//凡需要处理业务逻辑的.都需要记录操作日志
	@Transactional(readOnly=false)//需要事务操作必须加入此注解
	public Map<String,Object>  loadCutItems(Model model) {
		Map<String,Object> retMap = new HashMap<String, Object>();
		retMap.put("status",0);
		try {
			CutItemFormMap cutItemFormMap = getFormMap(CutItemFormMap.class);
			List<CutItemFormMap> cutItemFormMapList = cutItemMapper.findByNames(cutItemFormMap);
			retMap.put("data",cutItemFormMapList);
			retMap.put("status",1);
		}catch (Exception ex){
			retMap.put("msg",ex.getMessage());
			ex.printStackTrace();
		}
		return retMap;
	}


}