package com.lanyuan.controller.instrument;


import com.lanyuan.annotation.SystemLog;
import com.lanyuan.controller.index.BaseController;
import com.lanyuan.entity.CheckBigItemFormMap;
import com.lanyuan.entity.CheckSmallItemFormMap;
import com.lanyuan.mapper.CheckBigItemMapper;
import com.lanyuan.mapper.CheckSmallItemMapper;
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
import java.text.SimpleDateFormat;
import java.util.Date;
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
@RequestMapping("/instrument/smallitem/")
public class CheckSmallItemController extends BaseController {



	@Inject
	private CheckSmallItemMapper checkSmallItemMapper;

	@Inject
	private CheckBigItemMapper checkBigItemMapper;

	private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	@RequestMapping("list")
	public String listUI(Model model,String bigItemId) throws Exception {
		CheckBigItemFormMap checkBigItemFormMap =checkBigItemMapper.findbyFrist("id",bigItemId,CheckBigItemFormMap.class);
		model.addAttribute("checkBigItemFormMap",checkBigItemFormMap);
		return Common.BACKGROUND_PATH + "/instrument/checksmallitem/list";
	}

	@RequestMapping("addPage")
	public String addPage(Model model,String bigItemId) throws Exception {
		CheckBigItemFormMap checkBigItemFormMap =checkBigItemMapper.findbyFrist("id",bigItemId,CheckBigItemFormMap.class);
		model.addAttribute("checkBigItemFormMap",checkBigItemFormMap);
		return Common.BACKGROUND_PATH + "/instrument/checksmallitem/add";
	}

	@RequestMapping("editPage")
	public String editPage(Model model,String id) throws Exception {
		CheckSmallItemFormMap checkSmallItemFormMap = checkSmallItemMapper.findbyFrist("id",id,CheckSmallItemFormMap.class);
		model.addAttribute("checkSmallItemFormMap",checkSmallItemFormMap);
		return Common.BACKGROUND_PATH + "/instrument/checksmallitem/edit";
	}


	@ResponseBody
	@RequestMapping("dataGrid")
	@SystemLog(module="用户管理",methods="加载用户列表")//凡需要处理业务逻辑的.都需要记录操作日志
	@Transactional(readOnly=false)//需要事务操作必须加入此注解
	public Grid dataGrid(PageFilter ph,String bigItemId)throws Exception {
		Grid grid = new Grid();
		CheckSmallItemFormMap checkSmallItemFormMap = getFormMap(CheckSmallItemFormMap.class);
		checkSmallItemFormMap.put("orderby",ph.getSort()+" "+ph.getOrder());
		checkSmallItemFormMap=toFormMap(checkSmallItemFormMap,  String.valueOf(ph.getPage()), String.valueOf(ph.getRows()),checkSmallItemFormMap.getStr("orderby"));
		List<CheckSmallItemFormMap> userFormMapList =checkSmallItemMapper.findEnterprisePage(checkSmallItemFormMap);
		if(CollectionUtils.isNotEmpty(userFormMapList)){
			grid.setRows(userFormMapList);
		}
		PageView pageView = (PageView) checkSmallItemFormMap.get("paging");
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
			CheckSmallItemFormMap checkSmallItemFormMap = getFormMap(CheckSmallItemFormMap.class);
			checkSmallItemFormMap.put("insert_time",dateFormat.format(new Date()));
			checkSmallItemFormMap.put("update_time",dateFormat.format(new Date()));
			checkSmallItemMapper.addEntity(checkSmallItemFormMap);
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
			CheckSmallItemFormMap checkSmallItemFormMap = getFormMap(CheckSmallItemFormMap.class);
			checkSmallItemMapper.deleteByNames(checkSmallItemFormMap);
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
			CheckSmallItemFormMap checkSmallItemFormMap = getFormMap(CheckSmallItemFormMap.class);
			checkSmallItemMapper.editEntity(checkSmallItemFormMap);
			retMap.put("msg","修改成功");
			retMap.put("status",1);
		}catch (Exception ex){
			retMap.put("msg",ex.getMessage());
			ex.printStackTrace();
		}
		return retMap;
	}


}