package com.lanyuan.controller.instrument;


import com.lanyuan.annotation.SystemLog;
import com.lanyuan.controller.index.BaseController;
import com.lanyuan.entity.*;
import com.lanyuan.exception.SystemException;
import com.lanyuan.mapper.CheckBigItemMapper;
import com.lanyuan.mapper.RoleUserMapper;
import com.lanyuan.plugin.PageView;
import com.lanyuan.util.Common;
import com.lanyuan.util.CommonConstants;
import com.lanyuan.util.PasswordHelper;
import com.lanyuan.vo.Grid;
import com.lanyuan.vo.PageFilter;
import org.apache.commons.collections.CollectionUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
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
@RequestMapping("/instrument/bigitem/")
public class CheckBigItemController extends BaseController {
	@Inject
	private CheckBigItemMapper checkBigItemMapper;

	private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	@RequestMapping("list")
	public String listUI(Model model) throws Exception {
		return Common.BACKGROUND_PATH + "/instrument/checkbigitem/list";
	}

	@RequestMapping("addPage")
	public String addPage(Model model) throws Exception {
		return Common.BACKGROUND_PATH + "/instrument/checkbigitem/add";
	}

	@RequestMapping("editPage")
	public String editPage(Model model,String id) throws Exception {
		CheckBigItemFormMap checkBigItemFormMap = checkBigItemMapper.findbyFrist("id",id,CheckBigItemFormMap.class);
		model.addAttribute("checkBigItemFormMap",checkBigItemFormMap);
		return Common.BACKGROUND_PATH + "/instrument/checkbigitem/edit";
	}


	@ResponseBody
	@RequestMapping("dataGrid")
	@SystemLog(module="用户管理",methods="加载用户列表")//凡需要处理业务逻辑的.都需要记录操作日志
	@Transactional(readOnly=false)//需要事务操作必须加入此注解
	public Grid dataGrid(PageFilter ph)throws Exception {
		Grid grid = new Grid();
		CheckBigItemFormMap checkBigItemFormMap = getFormMap(CheckBigItemFormMap.class);
		checkBigItemFormMap.put("orderby",ph.getSort()+" "+ph.getOrder());
		checkBigItemFormMap=toFormMap(checkBigItemFormMap,  String.valueOf(ph.getPage()), String.valueOf(ph.getRows()),checkBigItemFormMap.getStr("orderby"));
		List<CheckBigItemFormMap> userFormMapList =checkBigItemMapper.findEnterprisePage(checkBigItemFormMap);
		if(CollectionUtils.isNotEmpty(userFormMapList)){
			grid.setRows(userFormMapList);
		}
		PageView pageView = (PageView) checkBigItemFormMap.get("paging");
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
			CheckBigItemFormMap checkBigItemFormMap = getFormMap(CheckBigItemFormMap.class);
			checkBigItemFormMap.put("insert_time",dateFormat.format(new Date()));
			checkBigItemFormMap.put("update_time",dateFormat.format(new Date()));
			checkBigItemMapper.addEntity(checkBigItemFormMap);
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
			CheckBigItemFormMap checkBigItemFormMap = getFormMap(CheckBigItemFormMap.class);
			checkBigItemMapper.deleteByNames(checkBigItemFormMap);
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
			CheckBigItemFormMap checkBigItemFormMap = getFormMap(CheckBigItemFormMap.class);
			checkBigItemMapper.editEntity(checkBigItemFormMap);
			retMap.put("msg","修改成功");
			retMap.put("status",1);
		}catch (Exception ex){
			retMap.put("msg",ex.getMessage());
			ex.printStackTrace();
		}
		return retMap;
	}






}