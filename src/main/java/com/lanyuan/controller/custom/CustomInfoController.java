package com.lanyuan.controller.custom;


import com.lanyuan.annotation.SystemLog;
import com.lanyuan.controller.index.BaseController;
import com.lanyuan.entity.*;
import com.lanyuan.exception.SystemException;
import com.lanyuan.mapper.CustomBelonetoEntMapper;
import com.lanyuan.mapper.CustomCutItemMapper;
import com.lanyuan.mapper.CustomInfoMapper;
import com.lanyuan.mapper.CutItemMapper;
import com.lanyuan.plugin.PageView;
import com.lanyuan.util.Common;
import com.lanyuan.util.CommonConstants;
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

	@RequestMapping("list")
	public String listUI(Model model) throws Exception {
		model.addAttribute("res", findByRes());
		return Common.BACKGROUND_PATH + "/custom/info/list";
	}

	@ResponseBody
	@RequestMapping("findByPage")
	public PageView findByPage( String pageNow,String pageSize,String column,String sort) throws Exception {
		CustomInfoFormMap customInfoFormMap = getFormMap(CustomInfoFormMap.class);
		customInfoFormMap=toFormMap(customInfoFormMap, pageNow, pageSize,customInfoFormMap.getStr("orderby"));
		customInfoFormMap.put("column", column);
		customInfoFormMap.put("sort", sort);
		//todo 获取当前登录用户的企业和检测点
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




	@RequestMapping("addUI")
	public String addUI(Model model) throws Exception {
		return Common.BACKGROUND_PATH + "/custom/info/add";
	}

	@ResponseBody
	@RequestMapping("addEntity")
	@SystemLog(module="系统管理",methods="企业管理，新增企业信息")//凡需要处理业务逻辑的.都需要记录操作日志
	@Transactional(readOnly=false)//需要事务操作必须加入此注解
	public String addEntity(String txtGroupsSelect){
		try {
			Session session = SecurityUtils.getSubject().getSession();
			UserEntrelationFormMap userEntrelationFormMap = (UserEntrelationFormMap)session.getAttribute(CommonConstants.ENERPRISE_RELATION_INSESSION);

			if(userEntrelationFormMap==null){
				throw new SystemException("请切换为企业用户才可以添加客户!");
			}

			SimpleDateFormat datetimeformat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			CustomInfoFormMap customInfoFormMap = getFormMap(CustomInfoFormMap.class);
			customInfoFormMap.put("insert_time",datetimeformat.format(new Date()));
			customInfoFormMap.put("update_time",datetimeformat.format(new Date()));
			customInfoFormMap.put("ent_id",userEntrelationFormMap.get("ent_id").toString());
			customInfoFormMap.put("sub_ent_point_id",userEntrelationFormMap.get("sub_point_id").toString());
			customInfoFormMap.put("show_in_ent",0);

			customInfoMapper.addEntity(customInfoFormMap);//新增后返回新增信息

			//todo 保存客户所属企业关系
			CustomBelonetoEntFormMap customBelonetoEntFormMap = getFormMap(CustomBelonetoEntFormMap.class);
			customBelonetoEntFormMap.put("ent_id",userEntrelationFormMap.get("ent_id").toString());
			customBelonetoEntFormMap.put("sub_point_id",userEntrelationFormMap.get("sub_point_id").toString());
			customBelonetoEntFormMap.put("insert_time",datetimeformat.format(new Date()));
			customBelonetoEntFormMap.put("custom_id",customInfoFormMap.get("id").toString());
			customBelonetoEntMapper.addEntity(customBelonetoEntFormMap);

			CustomCutItemFormMap customCutItemFormMap =getFormMap(CustomCutItemFormMap.class);
			System.out.println(customCutItemFormMap);

		} catch (Exception e) {
			 throw new SystemException("添加客户异常["+e.getMessage()+"]");
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
			customInfoMapper.deleteByAttribute("id", id, CustomInfoFormMap.class);
		}
		return "success";
	}

	@RequestMapping("editUI")
	public String editUI(Model model) throws Exception {
		String id = getPara("id");
		if(Common.isNotEmpty(id)){
			model.addAttribute("enterprise", customInfoMapper.findbyFrist("id", id, CustomInfoFormMap.class));
		}
		return Common.BACKGROUND_PATH + "/custom/info/edit";
	}

	@ResponseBody
	@RequestMapping("editEntity")
	@Transactional(readOnly=false)//需要事务操作必须加入此注解
	@SystemLog(module="系统管理",methods="用户管理-修改用户")//凡需要处理业务逻辑的.都需要记录操作日志
	public String editEntity(String txtGroupsSelect) throws Exception {
		CustomInfoFormMap customInfoFormMap = getFormMap(CustomInfoFormMap.class);
		customInfoFormMap.put("txtGroupsSelect", txtGroupsSelect);
		customInfoMapper.editEntity(customInfoFormMap);
		customInfoMapper.deleteByAttribute("userId", customInfoFormMap.get("id")+"", UserGroupsFormMap.class);
		if(!Common.isEmpty(txtGroupsSelect)){
			String[] txt = txtGroupsSelect.split(",");
			for (String roleId : txt) {
				UserGroupsFormMap userGroupsFormMap = new UserGroupsFormMap();
				userGroupsFormMap.put("userId", customInfoFormMap.get("id"));
				userGroupsFormMap.put("roleId", roleId);
				customInfoMapper.addEntity(userGroupsFormMap);
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
		CustomInfoFormMap account = customInfoMapper.findbyFrist("accountName", name, CustomInfoFormMap.class);
		if (account == null) {
			return true;
		} else {
			return false;
		}
	}
	

}