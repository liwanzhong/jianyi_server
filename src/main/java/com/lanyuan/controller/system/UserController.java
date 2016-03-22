package com.lanyuan.controller.system;


import java.io.IOException;
import java.util.*;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.lanyuan.entity.*;
import com.lanyuan.mapper.RoleUserMapper;
import com.lanyuan.vo.Grid;
import com.lanyuan.vo.Organization;
import com.lanyuan.vo.PageFilter;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lanyuan.mapper.UserMapper;
import com.lanyuan.annotation.SystemLog;
import com.lanyuan.controller.index.BaseController;
import com.lanyuan.exception.SystemException;
import com.lanyuan.plugin.PageView;
import com.lanyuan.util.Common;
import com.lanyuan.util.JsonUtils;
import com.lanyuan.util.POIUtils;
import com.lanyuan.util.PasswordHelper;

/**
 *
 * @author lanyuan 2014-11-19
 * @Email: mmm333zzz520@163.com
 * @version 3.0v
 */
@Controller
@RequestMapping("/user/")
public class UserController extends BaseController {
	@Inject
	private UserMapper userMapper;

	@RequestMapping("list")
	public String listUI(Model model) throws Exception {
		return Common.BACKGROUND_PATH + "/system/user/list";
	}

	@RequestMapping("addPage")
	public String addPage(Model model) throws Exception {
		return Common.BACKGROUND_PATH + "/system/user/add";
	}

	@RequestMapping("editPage")
	public String editPage(Model model,String id) throws Exception {
		UserFormMap userFormMap = userMapper.findbyFrist("id",id,UserFormMap.class);
		//todo  查询用户权限

		List<String> roleIds = new ArrayList<String>();
		RoleUserFormMap roleUserFormMap = new RoleUserFormMap();
		roleUserFormMap.put("userId",id);
		List<RoleUserFormMap> roleUserFormMapList =  roleUserMapper.findByNames(roleUserFormMap);
		for(RoleUserFormMap item:roleUserFormMapList){
			roleIds.add(item.get("roleId").toString());

		}
		userFormMap.put("roleIds",roleIds);
		model.addAttribute("userFormMap",userFormMap);

		return Common.BACKGROUND_PATH + "/system/user/edit";
	}


	@ResponseBody
	@RequestMapping("dataGrid")
	@SystemLog(module="用户管理",methods="加载用户列表")//凡需要处理业务逻辑的.都需要记录操作日志
	@Transactional(readOnly=false)//需要事务操作必须加入此注解
	public Grid dataGrid( PageFilter ph)throws Exception {
		Grid grid = new Grid();

		UserFormMap userFormMap = getFormMap(UserFormMap.class);
		userFormMap.put("orderby",ph.getSort()+" "+ph.getOrder());
		userFormMap=toFormMap(userFormMap,  String.valueOf(ph.getPage()), String.valueOf(ph.getRows()),userFormMap.getStr("orderby"));
		List<UserFormMap> userFormMapList =userMapper.findUserPage(userFormMap);
		if(CollectionUtils.isNotEmpty(userFormMapList)){
			grid.setRows(userFormMapList);
		}
		PageView pageView = (PageView) userFormMap.get("paging");
		grid.setTotal(pageView.getRowCount());
		return grid;

	}



	@Inject
	private RoleUserMapper roleUserMapper;



	@ResponseBody
	@RequestMapping("add")
	@SystemLog(module="用户管理",methods="新增用户")//凡需要处理业务逻辑的.都需要记录操作日志
	@Transactional(readOnly=false)//需要事务操作必须加入此注解
	public Map<String,Object> add(){
		Map<String,Object> retMap = new HashMap<String, Object>();
		retMap.put("status",0);
		try {
			UserFormMap userFormMap = getFormMap(UserFormMap.class);
			//这里对修改的密码进行加密
			PasswordHelper passwordHelper = new PasswordHelper();
			passwordHelper.encryptPassword(userFormMap);

			userMapper.addEntity(userFormMap);

			//todo 更新用户权限关系表
			String[] roleIds = (String[])userFormMap.get("roleIds");
			if(roleIds!=null && roleIds.length>0){
				List<RoleUserFormMap> roleUserFormMapList = new ArrayList<RoleUserFormMap>();
				for(String item:roleIds){
					RoleUserFormMap roleUserFormMap = new RoleUserFormMap();
					roleUserFormMap.put("userId",userFormMap.get("id").toString());
					roleUserFormMap.put("roleId",item);
					roleUserFormMapList.add(roleUserFormMap);
				}
				roleUserMapper.batchSave(roleUserFormMapList);
			}

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
			UserFormMap userFormMap = getFormMap(UserFormMap.class);
			userMapper.deleteByNames(userFormMap);
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
			UserFormMap userFormMap = getFormMap(UserFormMap.class);
			userMapper.editEntity(userFormMap);

			//todo 更新用户权限关系表
			String[] roleIds = (String[])userFormMap.get("roleIds");
			if(roleIds!=null && roleIds.length>0){
				RoleUserFormMap roleUserFormMapDel = new RoleUserFormMap();
				roleUserFormMapDel.put("userId",userFormMap.get("id").toString());
				roleUserMapper.deleteByNames(roleUserFormMapDel);

				List<RoleUserFormMap> roleUserFormMapList = new ArrayList<RoleUserFormMap>();
				for(String item:roleIds){
					RoleUserFormMap roleUserFormMap = new RoleUserFormMap();
					roleUserFormMap.put("userId",userFormMap.get("id").toString());
					roleUserFormMap.put("roleId",item);
					roleUserFormMapList.add(roleUserFormMap);
				}
				roleUserMapper.batchSave(roleUserFormMapList);
			}

			retMap.put("msg","修改组织成功");
			retMap.put("status",1);
		}catch (Exception ex){
			retMap.put("msg",ex.getMessage());
			ex.printStackTrace();
		}
		return retMap;
	}



	/**
	 * 验证账号是否存在
	 *
	 * @author lanyuan Email：mmm333zzz520@163.com date：2014-2-19
	 * @param name
	 * @return
	 */
	@RequestMapping("isExist")
	@ResponseBody
	public boolean isExist(String name) {
		UserFormMap account = userMapper.findbyFrist("accountName", name, UserFormMap.class);
		if (account == null) {
			return true;
		} else {
			return false;
		}
	}

	//密码修改
	@RequestMapping("updatePassword")
	public String updatePassword(Model model) throws Exception {
		return Common.BACKGROUND_PATH + "/system/user/userEditPwd";
	}

	//保存新密码
	@RequestMapping("editPassword")
	@ResponseBody
	@Transactional(readOnly=false)//需要事务操作必须加入此注解N
	@SystemLog(module="系统管理",methods="用户管理-修改密码")//凡需要处理业务逻辑的.都需要记录操作日志
	public Map<String,Object> editPassword() throws Exception{

		Map<String,Object> retMap = new HashMap<String, Object>();
		retMap.put("status",0);
		try {
			//todo 验证旧密码是否通过
			UserFormMap userFormMap = getFormMap(UserFormMap.class);
			userFormMap.put("password", userFormMap.get("pwd"));
			//这里对修改的密码进行加密
			PasswordHelper passwordHelper = new PasswordHelper();
			passwordHelper.encryptPassword(userFormMap);

			userMapper.editEntity(userFormMap);
			retMap.put("msg","修改密码成功");
			retMap.put("status",1);
		}catch (Exception ex){
			ex.printStackTrace();
			retMap.put("msg",ex.getMessage());
		}
		return retMap;
	}
}