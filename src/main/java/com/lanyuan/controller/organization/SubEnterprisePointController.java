package com.lanyuan.controller.organization;


import com.lanyuan.annotation.SystemLog;
import com.lanyuan.controller.index.BaseController;
import com.lanyuan.entity.EnterpriseFormMap;
import com.lanyuan.entity.SubEnterprisePointFormMap;
import com.lanyuan.entity.UserFormMap;
import com.lanyuan.entity.UserGroupsFormMap;
import com.lanyuan.exception.SystemException;
import com.lanyuan.mapper.EnterpriseMapper;
import com.lanyuan.mapper.SubEnterprisePointMapper;
import com.lanyuan.plugin.PageView;
import com.lanyuan.util.Common;
import com.lanyuan.util.PasswordHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.inject.Inject;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author lanyuan 2014-11-19
 * @Email: mmm333zzz520@163.com
 * @version 3.0v
 */
@Controller
@RequestMapping("/sub_point/")
public class SubEnterprisePointController extends BaseController {
	@Inject
	private SubEnterprisePointMapper subEnterprisePointMapper;

	@Autowired
	private EnterpriseMapper enterpriseMapper;

	@RequestMapping("list")
	public String listUI(Model model) throws Exception {
		model.addAttribute("res", findByRes());
		return Common.BACKGROUND_PATH + "/organization/subpoint/list";
	}

	@ResponseBody
	@RequestMapping("findByPage")
	public PageView findByPage( String pageNow,String pageSize,String column,String sort) throws Exception {
		SubEnterprisePointFormMap userFormMap = getFormMap(SubEnterprisePointFormMap.class);
		userFormMap=toFormMap(userFormMap, pageNow, pageSize,userFormMap.getStr("orderby"));
		userFormMap.put("column", column);
		userFormMap.put("sort", sort);
		userFormMap.put("valid",1);
		pageView.setRecords(subEnterprisePointMapper.findEnterprisePage(userFormMap));//不调用默认分页,调用自已的mapper中findUserPage
		return pageView;
	}


	@RequestMapping("addUI")
	public String addUI(Model model) throws Exception {
		// 加载所有企业信息
		EnterpriseFormMap enterpriseFormMap=new EnterpriseFormMap();
		enterpriseFormMap.put("orderby","order by id desc");
		model.addAttribute("enterpriseFormMap",enterpriseMapper.findEnterprisePage(enterpriseFormMap));
		return Common.BACKGROUND_PATH + "/organization/subpoint/add";
	}

	@ResponseBody
	@RequestMapping("addEntity")
	@SystemLog(module="系统管理",methods="添加检测点")//凡需要处理业务逻辑的.都需要记录操作日志
	@Transactional(readOnly=false)//需要事务操作必须加入此注解
	public String addEntity(String txtGroupsSelect){
		try {
			SimpleDateFormat datetimeformat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			SubEnterprisePointFormMap userFormMap = getFormMap(SubEnterprisePointFormMap.class);
			userFormMap.put("insert_time",datetimeformat.format(new Date()));
			userFormMap.put("update_time",datetimeformat.format(new Date()));

			subEnterprisePointMapper.addEntity(userFormMap);//新增后返回新增信息

		} catch (Exception e) {
			throw new SystemException("添加检测点异常");
		}
		return "success";
	}

	@ResponseBody
	@RequestMapping("deleteEntity")
	@Transactional(readOnly=false)//需要事务操作必须加入此注解
	@SystemLog(module="企业管理",methods="删除检测点")//凡需要处理业务逻辑的.都需要记录操作日志
	public String deleteEntity() throws Exception {
		String[] ids = getParaValues("ids");
		for (String id : ids) {
			subEnterprisePointMapper.deleteByAttribute("id", id, SubEnterprisePointFormMap.class);
		}
		return "success";
	}

	@RequestMapping("editUI")
	public String editUI(Model model) throws Exception {
		String id = getPara("id");
		if(Common.isNotEmpty(id)){
			model.addAttribute("enterprise", subEnterprisePointMapper.findbyFrist("id", id, SubEnterprisePointFormMap.class));
		}
		return Common.BACKGROUND_PATH + "/organization/subpoint/edit";
	}

	@ResponseBody
	@RequestMapping("editEntity")
	@Transactional(readOnly=false)//需要事务操作必须加入此注解
	@SystemLog(module="企业管理",methods="编辑检测点")//凡需要处理业务逻辑的.都需要记录操作日志
	public String editEntity(String txtGroupsSelect) throws Exception {
		UserFormMap userFormMap = getFormMap(UserFormMap.class);
		userFormMap.put("txtGroupsSelect", txtGroupsSelect);
		subEnterprisePointMapper.editEntity(userFormMap);
		subEnterprisePointMapper.deleteByAttribute("userId", userFormMap.get("id")+"", UserGroupsFormMap.class);
		if(!Common.isEmpty(txtGroupsSelect)){
			String[] txt = txtGroupsSelect.split(",");
			for (String roleId : txt) {
				UserGroupsFormMap userGroupsFormMap = new UserGroupsFormMap();
				userGroupsFormMap.put("userId", userFormMap.get("id"));
				userGroupsFormMap.put("roleId", roleId);
				subEnterprisePointMapper.addEntity(userGroupsFormMap);
			}
		}
		return "success";
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
		UserFormMap account = subEnterprisePointMapper.findbyFrist("accountName", name, UserFormMap.class);
		if (account == null) {
			return true;
		} else {
			return false;
		}
	}

}