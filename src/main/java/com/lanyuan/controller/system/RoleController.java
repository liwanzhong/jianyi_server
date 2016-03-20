package com.lanyuan.controller.system;

import java.text.SimpleDateFormat;
import java.util.*;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import com.lanyuan.entity.RoleFormMap;
import com.lanyuan.entity.UserEntrelationFormMap;
import com.lanyuan.entity.UserFormMap;
import com.lanyuan.exception.SystemException;
import com.lanyuan.vo.Grid;
import com.lanyuan.vo.Organization;
import com.lanyuan.vo.PageFilter;
import com.lanyuan.vo.Tree;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
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


	@RequestMapping("/grantPage")
	public String grantPage(HttpServletRequest request, Long id) {
		/*Trole t = roleDao.get(Trole.class, id);
		Role r = new Role();
		r.setDescription(t.getDescription());
		r.setId(t.getId());
		r.setIsdefault(t.getIsdefault());
		r.setName(t.getName());
		r.setSeq(t.getSeq());
		Set<Tresource> s = t.getResources();
		if ((s != null) && !s.isEmpty()) {
			boolean b = false;
			String ids = "";
			String names = "";
			for (Tresource tr : s) {
				if (b) {
					ids += ",";
					names += ",";
				} else {
					b = true;
				}
				ids += tr.getId();
				names += tr.getName();
			}
			r.setResourceIds(ids);
			r.setResourceNames(names);
		}
		return r;
		request.setAttribute("role", r);*/
		return "/admin/roleGrant";
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







	@RequestMapping("/grant")
	@ResponseBody
	public Map<String,Object> grant() {
		Map<String,Object> retMap = new HashMap<String, Object>();
		/*Json j = new Json();
		try {
			roleService.grant(role);
			j.setMsg("授权成功！");
			j.setSuccess(true);
		} catch (Exception e) {
			j.setMsg(e.getMessage());
		}*/
		return retMap;
	}

	/*public void grant(Role role)throws Exception {
		Trole t = roleDao.get(Trole.class, role.getId());
		if ((role.getResourceIds() != null) && !role.getResourceIds().equalsIgnoreCase("")) {
			String ids = "";
			boolean b = false;
			for (String id : role.getResourceIds().split(",")) {
				if (b) {
					ids += ",";
				} else {
					b = true;
				}
				ids += id;
			}
			t.setResources(new HashSet<Tresource>(resourceDao.find("select distinct t from Tresource t where t.id in ("
					+ ids + ")")));
		} else {
			t.setResources(null);
		}
	}*/


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