package com.lanyuan.controller.instrument;


import com.lanyuan.annotation.SystemLog;
import com.lanyuan.controller.index.BaseController;
import com.lanyuan.entity.BmiSickRiskRoutFormMap;
import com.lanyuan.entity.BmiSickRiskRoutFormMap;
import com.lanyuan.mapper.BmiSickRiskRoutMapper;
import com.lanyuan.mapper.SickRiskMapper;
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
@RequestMapping("/instrument/bmiRiskRoutConfig/")
public class BmiSickRiskConfigController extends BaseController {



	@Inject
	private BmiSickRiskRoutMapper bmiSickRiskRoutMapper;




	private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	@RequestMapping("list")
	public String listUI(Model model) throws Exception {
		BmiSickRiskRoutFormMap bmiSickRiskRoutFormMap = getFormMap(BmiSickRiskRoutFormMap.class);
		List<BmiSickRiskRoutFormMap> bmiSickRiskRoutFormMapList = bmiSickRiskRoutMapper.findByNames(bmiSickRiskRoutFormMap);
		model.addAttribute("sickId",bmiSickRiskRoutFormMap.get("sick_risk_id").toString());
		model.addAttribute("bmiSickRiskRoutFormMapList",bmiSickRiskRoutFormMapList);
		return Common.BACKGROUND_PATH + "/instrument/bmisickriskroutconfig/list";
	}

	@RequestMapping("addPage")
	public String addPage(Model model,String checkItemId,Integer checkItemType) throws Exception {
		model.addAttribute("checkItemId",checkItemId);
		model.addAttribute("checkItemType",checkItemType);
		return Common.BACKGROUND_PATH + "/instrument/bmisickriskroutconfig/add";
	}

	@RequestMapping("editPage")
	public String editPage(Model model,String id) throws Exception {
		BmiSickRiskRoutFormMap bmiSickRiskRoutFormMap = bmiSickRiskRoutMapper.findbyFrist("id",id,BmiSickRiskRoutFormMap.class);
		model.addAttribute("bmiSickRiskRoutFormMap",bmiSickRiskRoutFormMap);
		return Common.BACKGROUND_PATH + "/instrument/bmisickriskroutconfig/edit";
	}





	@ResponseBody
	@RequestMapping("dataGrid")
	@SystemLog(module="用户管理",methods="加载用户列表")//凡需要处理业务逻辑的.都需要记录操作日志
	@Transactional(readOnly=false)//需要事务操作必须加入此注解
	public Grid dataGrid(PageFilter ph)throws Exception {
		Grid grid = new Grid();
		BmiSickRiskRoutFormMap bmiSickRiskRoutFormMap = getFormMap(BmiSickRiskRoutFormMap.class);
		bmiSickRiskRoutFormMap.put("orderby",ph.getSort()+" "+ph.getOrder());
		bmiSickRiskRoutFormMap=toFormMap(bmiSickRiskRoutFormMap,  String.valueOf(ph.getPage()), String.valueOf(ph.getRows()),bmiSickRiskRoutFormMap.getStr("orderby"));
		List<BmiSickRiskRoutFormMap> userFormMapList =bmiSickRiskRoutMapper.findEnterprisePage(bmiSickRiskRoutFormMap);
		if(CollectionUtils.isNotEmpty(userFormMapList)){
			grid.setRows(userFormMapList);
		}
		PageView pageView = (PageView) bmiSickRiskRoutFormMap.get("paging");
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
			BmiSickRiskRoutFormMap bmiSickRiskRoutFormMap = getFormMap(BmiSickRiskRoutFormMap.class);
			bmiSickRiskRoutFormMap.put("update_time",dateFormat.format(new Date()));
			bmiSickRiskRoutMapper.addEntity(bmiSickRiskRoutFormMap);
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
			BmiSickRiskRoutFormMap bmiSickRiskRoutFormMap = getFormMap(BmiSickRiskRoutFormMap.class);
			bmiSickRiskRoutMapper.deleteByNames(bmiSickRiskRoutFormMap);
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
			BmiSickRiskRoutFormMap bmiSickRiskRoutFormMap = getFormMap(BmiSickRiskRoutFormMap.class);
			bmiSickRiskRoutMapper.editEntity(bmiSickRiskRoutFormMap);
			retMap.put("msg","修改成功");
			retMap.put("status",1);
		}catch (Exception ex){
			retMap.put("msg",ex.getMessage());
			ex.printStackTrace();
		}
		return retMap;
	}


}