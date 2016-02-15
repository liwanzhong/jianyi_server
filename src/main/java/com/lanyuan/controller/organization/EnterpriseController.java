package com.lanyuan.controller.organization;


import com.lanyuan.annotation.SystemLog;
import com.lanyuan.controller.index.BaseController;
import com.lanyuan.entity.*;
import com.lanyuan.exception.SystemException;
import com.lanyuan.mapper.EnterpriseMapper;
import com.lanyuan.plugin.PageView;
import com.lanyuan.util.*;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 
 * @author lanyuan 2014-11-19
 * @Email: mmm333zzz520@163.com
 * @version 3.0v
 */
@Controller
@RequestMapping("/enterprise/")
public class EnterpriseController extends BaseController {
	@Inject
	private EnterpriseMapper enterpriseMapper;
	
	@RequestMapping("list")
	public String listUI(Model model) throws Exception {
		model.addAttribute("res", findByRes());

		/*Session session = SecurityUtils.getSubject().getSession();
		UserEntrelationFormMap userEntrelationFormMap = (UserEntrelationFormMap)session.getAttribute(CommonConstants.ENERPRISE_RELATION_INSESSION);
		System.out.println(userEntrelationFormMap);*/

		return Common.BACKGROUND_PATH + "/organization/enterprise/list";
	}

	@ResponseBody
	@RequestMapping("findByPage")
	public PageView findByPage( String pageNow,String pageSize,String column,String sort) throws Exception {
		EnterpriseFormMap userFormMap = getFormMap(EnterpriseFormMap.class);
		userFormMap=toFormMap(userFormMap, pageNow, pageSize,userFormMap.getStr("orderby"));
		userFormMap.put("column", column);
		userFormMap.put("sort", sort);
		userFormMap.put("valid",1);
		pageView.setRecords(enterpriseMapper.findEnterprisePage(userFormMap));//不调用默认分页,调用自已的mapper中findUserPage
		return pageView;
	}


	@ResponseBody
	@RequestMapping("findAll")
	public List<EnterpriseFormMap> findAll( ) throws Exception {
		EnterpriseFormMap userFormMap = getFormMap(EnterpriseFormMap.class);
		return enterpriseMapper.findByWhere(userFormMap);
	}
	

	@RequestMapping("addUI")
	public String addUI(Model model) throws Exception {
		return Common.BACKGROUND_PATH + "/organization/enterprise/add";
	}

	@ResponseBody
	@RequestMapping("addEntity")
	@SystemLog(module="系统管理",methods="企业管理，新增企业信息")//凡需要处理业务逻辑的.都需要记录操作日志
	@Transactional(readOnly=false)//需要事务操作必须加入此注解
	public String addEntity(String txtGroupsSelect){
		try {
			SimpleDateFormat datetimeformat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			EnterpriseFormMap userFormMap = getFormMap(EnterpriseFormMap.class);
			userFormMap.put("txtGroupsSelect", txtGroupsSelect);
			userFormMap.put("insert_time",datetimeformat.format(new Date()));
			userFormMap.put("update_time",datetimeformat.format(new Date()));

			enterpriseMapper.addEntity(userFormMap);//新增后返回新增信息

		} catch (Exception e) {
			 throw new SystemException("添加账号异常");
		}
		return "success";
	}

	@ResponseBody
	@RequestMapping("deleteEntity")
	@Transactional(readOnly=false)//需要事务操作必须加入此注解
	@SystemLog(module="系统管理",methods="删除企业")//凡需要处理业务逻辑的.都需要记录操作日志
	public String deleteEntity() throws Exception {
		String[] ids = getParaValues("ids");
		for (String id : ids) {
			enterpriseMapper.deleteByAttribute("id", id, EnterpriseFormMap.class);
		}
		return "success";
	}

	@RequestMapping("editUI")
	public String editUI(Model model) throws Exception {
		String id = getPara("id");
		if(Common.isNotEmpty(id)){
			model.addAttribute("enterprise", enterpriseMapper.findbyFrist("id", id, EnterpriseFormMap.class));
		}
		return Common.BACKGROUND_PATH + "/organization/enterprise/edit";
	}

	@ResponseBody
	@RequestMapping("editEntity")
	@Transactional(readOnly=false)//需要事务操作必须加入此注解
	@SystemLog(module="系统管理",methods="用户管理-修改用户")//凡需要处理业务逻辑的.都需要记录操作日志
	public String editEntity(String txtGroupsSelect) throws Exception {
		UserFormMap userFormMap = getFormMap(UserFormMap.class);
		userFormMap.put("txtGroupsSelect", txtGroupsSelect);
		enterpriseMapper.editEntity(userFormMap);
		enterpriseMapper.deleteByAttribute("userId", userFormMap.get("id")+"", UserGroupsFormMap.class);
		if(!Common.isEmpty(txtGroupsSelect)){
			String[] txt = txtGroupsSelect.split(",");
			for (String roleId : txt) {
				UserGroupsFormMap userGroupsFormMap = new UserGroupsFormMap();
				userGroupsFormMap.put("userId", userFormMap.get("id"));
				userGroupsFormMap.put("roleId", roleId);
				enterpriseMapper.addEntity(userGroupsFormMap);
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
		UserFormMap account = enterpriseMapper.findbyFrist("accountName", name, UserFormMap.class);
		if (account == null) {
			return true;
		} else {
			return false;
		}
	}
	

}