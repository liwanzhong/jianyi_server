package com.lanyuan.controller.custom;


import com.lanyuan.annotation.SystemLog;
import com.lanyuan.controller.index.BaseController;
import com.lanyuan.entity.CustomBelonetoEntFormMap;
import com.lanyuan.entity.CustomInfoFormMap;
import com.lanyuan.entity.UserEntrelationFormMap;
import com.lanyuan.mapper.CustomBelonetoEntMapper;
import com.lanyuan.mapper.CustomCutItemMapper;
import com.lanyuan.mapper.CustomInfoMapper;
import com.lanyuan.plugin.PageView;
import com.lanyuan.util.Common;
import com.lanyuan.util.CommonConstants;
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
@RequestMapping("/custom/info/")
public class CustomInfoController extends BaseController {
	@Inject
	private CustomInfoMapper customInfoMapper;


	@Inject
	private CustomCutItemMapper customCutItemMapper;


	@Inject
	private CustomBelonetoEntMapper customBelonetoEntMapper;

	private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	@RequestMapping("list")
	public String listUI(Model model) throws Exception {
		return Common.BACKGROUND_PATH + "/custom/info/list";
	}


	@RequestMapping("list_client")
	public String list_client(Model model) throws Exception {
		return Common.BACKGROUND_PATH + "/front/member-list";
	}


	@ResponseBody
	@RequestMapping("findByPage")
	public PageView findByPage( String pageNow,String pageSize,String column,String sort) throws Exception {
		CustomInfoFormMap customInfoFormMap = getFormMap(CustomInfoFormMap.class);
		customInfoFormMap=toFormMap(customInfoFormMap, pageNow, pageSize,customInfoFormMap.getStr("orderby"));
		customInfoFormMap.put("column", column);
		customInfoFormMap.put("sort", sort);
		// 获取当前登录用户的企业和检测点
		Session session = SecurityUtils.getSubject().getSession();
		UserEntrelationFormMap userEntrelationFormMap = (UserEntrelationFormMap)session.getAttribute(CommonConstants.ENERPRISE_RELATION_INSESSION);

		if(userEntrelationFormMap!=null){//为空表示是系统管理用户,不为空表示是企业的用户
			String ent_id = userEntrelationFormMap.get("ent_id").toString();
			String sub_point_id = userEntrelationFormMap.get("sub_point_id").toString();

			customInfoFormMap.put("ent_id",ent_id);
			customInfoFormMap.put("sub_point_id",sub_point_id);
		}

		pageView.setRecords(customInfoMapper.findEnterprisePage(customInfoFormMap));//不调用默认分页,调用自已的mapper中findUserPage
		return pageView;
	}

	@RequestMapping("addPage")
	public String addPage(Model model) throws Exception {
		return Common.BACKGROUND_PATH + "/custom/info/add";
	}

	@RequestMapping("editPage")
	public String editPage(Model model,String id) throws Exception {
		CustomInfoFormMap customInfoFormMap = customInfoMapper.findbyFrist("id",id,CustomInfoFormMap.class);
		model.addAttribute("customInfoFormMap",customInfoFormMap);
		return Common.BACKGROUND_PATH + "/custom/info/edit";
	}




	@ResponseBody
	@RequestMapping("dataGrid")
	@SystemLog(module="用户管理",methods="加载用户列表")//凡需要处理业务逻辑的.都需要记录操作日志
	@Transactional(readOnly=false)//需要事务操作必须加入此注解
	public Grid dataGrid(PageFilter ph)throws Exception {
		Grid grid = new Grid();
		CustomInfoFormMap customInfoFormMap = getFormMap(CustomInfoFormMap.class);
		customInfoFormMap.put("orderby",ph.getSort()+" "+ph.getOrder());
		customInfoFormMap=toFormMap(customInfoFormMap,  String.valueOf(ph.getPage()), String.valueOf(ph.getRows()),customInfoFormMap.getStr("orderby"));
		List<CustomInfoFormMap> userFormMapList =customInfoMapper.findEnterprisePage(customInfoFormMap);
		if(CollectionUtils.isNotEmpty(userFormMapList)){
			grid.setRows(userFormMapList);
		}
		PageView pageView = (PageView) customInfoFormMap.get("paging");
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
			CustomInfoFormMap customInfoFormMap = getFormMap(CustomInfoFormMap.class);
			customInfoFormMap.put("insert_time",dateFormat.format(new Date()));
			customInfoFormMap.put("update_time",dateFormat.format(new Date()));
			customInfoMapper.addEntity(customInfoFormMap);
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
			CustomInfoFormMap customInfoFormMap = getFormMap(CustomInfoFormMap.class);
			customBelonetoEntMapper.deleteByAttribute("id",customInfoFormMap.get("id").toString(), CustomBelonetoEntFormMap.class);
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
			CustomInfoFormMap customInfoFormMap = getFormMap(CustomInfoFormMap.class);
			customInfoMapper.editEntity(customInfoFormMap);
			retMap.put("msg","修改成功");
			retMap.put("status",1);
		}catch (Exception ex){
			retMap.put("msg",ex.getMessage());
			ex.printStackTrace();
		}
		return retMap;
	}



}