package com.lanyuan.controller.system;

import java.text.SimpleDateFormat;
import java.util.*;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import com.lanyuan.entity.*;
import com.lanyuan.exception.SystemException;
import com.lanyuan.mapper.ResourcesMapper;
import com.lanyuan.mapper.RoleResourcesMapper;
import com.lanyuan.vo.Grid;
import com.lanyuan.vo.Organization;
import com.lanyuan.vo.PageFilter;
import com.lanyuan.vo.Tree;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lanyuan.annotation.SystemLog;
import com.lanyuan.controller.index.BaseController;
import com.lanyuan.entity.RoleFormMap;
import com.lanyuan.mapper.RoleMapper;
import com.lanyuan.plugin.PageView;
import com.lanyuan.util.Common;

/**
 * 
 * @author lanyuan 2014-11-19
 * @Email: mmm333zzz520@163.com
 * @version 3.0v
 */
@Controller
@RequestMapping("/role/")
public class RoleController extends BaseController {
	@Inject
	private RoleMapper roleMapper;


	@Autowired
	private ResourcesMapper resourcesMapper;

	@Autowired
	private RoleResourcesMapper roleResourcesMapper;




	private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	@RequestMapping("list")
	public String list(Model model) {
		return Common.BACKGROUND_PATH + "/system/role/list";
	}

	@RequestMapping("addPage")
	public String addPage(Model model) {
		return Common.BACKGROUND_PATH + "/system/role/add";
	}

	@RequestMapping("editPage")
	public String editPage(Model model,String id)throws Exception {
		//
		RoleFormMap roleFormMap = roleMapper.findbyFrist("id",id,RoleFormMap.class);
		if(null == roleFormMap){
			throw new Exception("没有对象");
		}
		model.addAttribute("roleFormMap",roleFormMap);
		return Common.BACKGROUND_PATH + "/system/role/edit";
	}


	@RequestMapping("grantPage")
	public String grantPage(Model model)throws Exception {

		RoleFormMap roleFormMap = getFormMap(RoleFormMap.class);

		roleFormMap = roleMapper.findbyFrist("id",roleFormMap.get("id").toString(),RoleFormMap.class);
		model.addAttribute("roleFormMap",roleFormMap);
		return  Common.BACKGROUND_PATH + "/system/role/grant";
	}


	@ResponseBody
	@RequestMapping("get")
	@SystemLog(module="权限管理",methods="加载角色权限列表")//凡需要处理业务逻辑的.都需要记录操作日志
	@Transactional(readOnly=false)//需要事务操作必须加入此注解
	public RoleFormMap get(String id)throws Exception {
		RoleFormMap roleFormMap =roleMapper.findbyFrist("id",id,RoleFormMap.class);

		// 查找角色绑定的资源
		List<ResFormMap> resFormMapList = resourcesMapper.findResByRole(id);
		if(CollectionUtils.isNotEmpty(resFormMapList)){
			String ids =null;
			String names=null;
			boolean b = false;
			for(ResFormMap item:resFormMapList){
				if (b) {
					ids += ",";
					names += ",";
				} else {
					b = true;
				}
				ids += item.get("id").toString();
				names += item.get("name").toString();
			}
			roleFormMap.put("resourceIds",ids);
			roleFormMap.put("ResourceNames",names);
		}

		return roleFormMap;

	}


	@ResponseBody
	@RequestMapping("dataGrid")
	@SystemLog(module="用户管理",methods="加载用户列表")//凡需要处理业务逻辑的.都需要记录操作日志
	@Transactional(readOnly=false)//需要事务操作必须加入此注解
	public Grid dataGrid(PageFilter ph)throws Exception {
		Grid grid = new Grid();

		RoleFormMap roleFormMap = getFormMap(RoleFormMap.class);
		roleFormMap.put("orderby",ph.getSort()+" "+ph.getOrder());
		roleFormMap=toFormMap(roleFormMap,  String.valueOf(ph.getPage()), String.valueOf(ph.getRows()),roleFormMap.getStr("orderby"));
		List<RoleFormMap> userFormMapList =roleMapper.findEnterprisePage(roleFormMap);
		if(CollectionUtils.isNotEmpty(userFormMapList)){
			grid.setRows(userFormMapList);
		}
		PageView pageView = (PageView) roleFormMap.get("paging");
		grid.setTotal(pageView.getRowCount());
		return grid;

	}



	@ResponseBody
	@RequestMapping("tree")
	public List<Tree> tree(Model model)throws Exception{
		List<Tree> lt = new ArrayList<Tree>();

		RoleFormMap roleFormMap = getFormMap(RoleFormMap.class);
		roleFormMap.put("orderby"," order by  id asc");
		List<RoleFormMap> roleFormMapList = roleMapper.findEnterprisePage(roleFormMap);

		if (CollectionUtils.isNotEmpty(roleFormMapList)) {
			for (RoleFormMap r : roleFormMapList) {
				Tree tree = new Tree();
				tree.setId(r.get("id").toString());
				tree.setText(r.get("name").toString());
				lt.add(tree);
			}
		}
		return lt;
	}







	@RequestMapping("grant")
	@ResponseBody
	public Map<String,Object> grant() {
		Map<String,Object> retMap = new HashMap<String, Object>();
		retMap.put("status",0);
		try {
			RoleFormMap roleFormMap = getFormMap(RoleFormMap.class);
			List<RoleResourcesFormMap> roleResourcesFormMapList =new ArrayList<RoleResourcesFormMap>();
			if(StringUtils.isNotBlank(roleFormMap.get("resourceIds").toString())){
				for (String id : roleFormMap.get("resourceIds").toString().split(",")) {
					RoleResourcesFormMap roleResourcesFormMap = new RoleResourcesFormMap();
					roleResourcesFormMap.put("role_id",roleFormMap.get("id").toString());
					roleResourcesFormMap.put("resource_id",id);

					roleResourcesFormMapList.add(roleResourcesFormMap);
				}
			}
			roleResourcesMapper.batchSave(roleResourcesFormMapList);
			retMap.put("status",1);
		}catch (Exception ex){
			retMap.put("msg",ex.getMessage());
		}
		return retMap;
	}




	@ResponseBody
	@RequestMapping("add")
	@SystemLog(module="权限组管理",methods="新增权限组")//凡需要处理业务逻辑的.都需要记录操作日志
	@Transactional(readOnly=false)//需要事务操作必须加入此注解
	public Map<String,Object> add(Model model) {
		Map<String,Object> retMap = new HashMap<String, Object>();
		retMap.put("status",0);
		try {
			RoleFormMap roleFormMap = getFormMap(RoleFormMap.class);
			roleMapper.addEntity(roleFormMap);
			retMap.put("msg","添加成功");
			retMap.put("status",1);
		}catch (Exception ex){
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
			RoleFormMap roleFormMap = getFormMap(RoleFormMap.class);
			roleMapper.editEntity(roleFormMap);
			retMap.put("msg","修改组织成功");
			retMap.put("status",1);
		}catch (Exception ex){
			retMap.put("msg",ex.getMessage());
		}
		return retMap;
	}


	@ResponseBody
	@RequestMapping("delete")
	@SystemLog(module="权限管理",methods="删除权限")//凡需要处理业务逻辑的.都需要记录操作日志
	@Transactional(readOnly=false)//需要事务操作必须加入此注解
	public Map<String,Object> delete(Model model) {
		Map<String,Object> retMap = new HashMap<String, Object>();
		retMap.put("status",0);
		try {
			RoleFormMap roleFormMap = getFormMap(RoleFormMap.class);
			roleMapper.deleteByNames(roleFormMap);
			retMap.put("msg","删除成功");
			retMap.put("status",1);
		}catch (Exception ex){
			retMap.put("msg",ex.getMessage());
		}
		return retMap;
	}

}